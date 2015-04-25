package com.young.tools.export.service.execl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.young.tools.export.config.Base;
import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.config.ConstantConfig;
import com.young.tools.export.config.execl.ExcelConfig;
import com.young.tools.export.config.parser.Parser;
import com.young.tools.export.config.parser.ParserFactory;
import com.young.tools.export.config.parser.ParserType;
import com.young.tools.export.service.ExportService;
import com.young.tools.export.template.TemplateParserConfig;
import com.young.tools.export.template.execl.ExcelTemplateParser;
/**
 * 导出execl服务类
 * @author yangy
 *
 */
public class ExcelService extends ExportService {

	
	@Override
	/**
	 * 导出execl表格
	 */
	public void export(InputStream config_stream, OutputStream output,
			TemplateParserConfig parser_config) throws Exception {
		Parser parser = ParserFactory.getParser(ParserType.excel_xml);
		BaseConfig config = parser.getConfig(config_stream);
		export(config, output, parser_config);
	}

	@Override
	/**
	 * 导出execl表格
	 */
	public void export(BaseConfig sconfig, OutputStream output,
			TemplateParserConfig parser_config) throws Exception {
		if (CollectionUtils.isEmpty(sconfig.getBases())) {
			log.warn("--No base config in xml or BaseConfig Object--");
			return;
		}
		ExcelConfig execlConfig = (ExcelConfig) sconfig;
		Collections.sort(execlConfig.getBases());
		ExcelTemplateParser parser = new ExcelTemplateParser(
				parser_config.getExeclConfig());
		List<Object[]> data = null;
		Base base = null;
		if (CollectionUtils.isEmpty(execlConfig.getBases()))
			return;
		Map<String, Object> constantData = new HashMap<String, Object>();
		for (int i = 0; i < execlConfig.getBases().size(); i++) {
			base = execlConfig.getBases().get(i);
			data = dao.getData(base);
			parser.createSheet(base.getName(), i);
			for (Object[] objArray : data) {
				parser.createNewRow(i);
				for (Object obj : objArray) {
					parser.buildCell(obj);
				}
			}
			if (!CollectionUtils.isEmpty(base.getConstants())) {
				for (ConstantConfig contant : base.getConstants()) {
					constantData.put(contant.getKey(), contant.getValue());
				}
				parser.replaceConstantData(constantData, i);
				constantData.clear();
			}
		}
		parser.writeToStream(output);
	}
}
