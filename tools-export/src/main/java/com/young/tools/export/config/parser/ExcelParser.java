package com.young.tools.export.config.parser;

import java.io.File;
import java.io.InputStream;

import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.config.ConstantConfig;
import com.young.tools.export.config.execl.ExcelConfig;
/**
 * execl xml解析器,用于扩展BaseParser解析器
 * @author yangy
 *
 */
public class ExcelParser extends BaseParser implements Parser {
	
	@Override
	public BaseConfig getConfig(InputStream in) throws Exception {
		getXStream().processAnnotations(ExcelConfig.class);
		getXStream().processAnnotations(ConstantConfig.class);
		ExcelConfig config = (ExcelConfig) getXStream().fromXML(in);
		return config;
	}

	public static void main(String[] args) throws Exception {
         Parser  parser = ParserFactory.getParser(ParserType.excel_xml);
         String filename = ExcelParser.class.getResource("/").getPath()+File.separator+"excel_config_default.xml";
         BaseConfig config = parser.getConfig(filename);
         System.out.println(config.getBases().size());
	}
}
