package com.young.tools.export.api;

import java.io.OutputStream;

import com.young.tools.export.ExportFactory;
import com.young.tools.export.ExportType;
import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.service.ExportService;
import com.young.tools.export.template.TemplateParserConfig;
/**
 * api 只需要继承该类即可
 * @author yangy
 *
 */
public abstract class ExportApi {

	/**
	 * 
	 * @return @see BaseConfig 导出配置对象,客户端只需要继承ExportApi实现该方法来生成BaseConfig对象
	 * @throws Exception
	 */
	public abstract BaseConfig generateConfig() throws Exception;
	/**
	 * 
	 * @return @see TemplateParserConfig 模板配置对象,客户端只需要继承ExportApi实现该方法来生成TemplateParserConfig对象
	 * @throws Exception
	 */
	public abstract TemplateParserConfig getTemplateConfig() throws Exception;
	/**
	 * 按照模板导出数据
	 * @param type 导出服务类型 EXECL,XML,CSV
	 * @param out 输出流
	 * @throws Exception
	 */
	public void export(ExportType type,OutputStream out) throws Exception{
		ExportService service = ExportFactory.getExporter(type);
		BaseConfig config = generateConfig();
		if(config==null)
			throw new Exception("--BaseConfig Object is not null,please implement generateConfig()--");
		
		TemplateParserConfig template = getTemplateConfig();
		if(template==null)
			throw new Exception("--TemplateParserConfig Object is not null,please implement getTemplateConfig()--");
		
		service.export(config, out, template);
	}
}
