package com.young.tools.execl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.young.tools.export.ExportFactory;
import com.young.tools.export.ExportType;
import com.young.tools.export.config.Base;
import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.config.SqlParamConfig;
import com.young.tools.export.config.parser.ExcelParser;
import com.young.tools.export.config.parser.Parser;
import com.young.tools.export.config.parser.ParserFactory;
import com.young.tools.export.config.parser.ParserType;
import com.young.tools.export.service.ExportService;
import com.young.tools.export.template.TemplateParserConfig;
import com.young.tools.export.template.execl.ExcelTemplateParserConfig;

public class ExportExcelTest {

	public static void main(String[] args) throws Exception {
		int day = 7;
		Parser parser = ParserFactory.getParser(ParserType.excel_xml);
		String filename = ExcelParser.class.getResource("/").getPath()
				+ File.separator + "waimei_config.xml";
		BaseConfig config = parser.getConfig(filename);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		for (Base base : config.getBases()) {
			if (base.getParams() != null) {
				for (SqlParamConfig p : base.getParams()) {
					if ("date".equals(p.getName())) {
						p.setValue(format.format(new Date(System.currentTimeMillis()-1000*60*60*24*day)));
						System.out.println(p.getValue());
					}
				}
			}

		}
		System.out.println(config.getBases().size());
		ExportService service = ExportFactory.getExporter(ExportType.EXECL);
		TemplateParserConfig template_config = new TemplateParserConfig();
		ExcelTemplateParserConfig execl_config = new ExcelTemplateParserConfig();
		execl_config.setTemplate_sheet_index(0);
		execl_config.setREPORT_TEMPLATE_FILE_PATH(ExcelParser.class
				.getResource("/").getPath() + File.separator + "外媒数据.xlsx");
		template_config.setExeclConfig(execl_config);
		service.export(config, new FileOutputStream("F:\\" + config.getName()
				+ "." + config.getType()), template_config);
	}
}
