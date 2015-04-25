package com.young.tools.export.config.parser;

import java.util.Hashtable;
import java.util.Map;
/**
 * xml解析器工厂,因为目前只有execl xml的解析器,以后可能会扩展csv等等解析器
 * @author yangy
 *
 */
public class ParserFactory {
    /**
     * 解析器池
     */
	private static Map<ParserType,Parser> parser_pool = new Hashtable<ParserType,Parser>();
	/**
	 * 根据type获取xml解析器
	 * @param parserType
	 * @return
	 */
	public static Parser getParser(ParserType parserType){
		if(parser_pool.containsKey(parserType))
			return parser_pool.get(parserType);
		Parser parser = null;
		if(ParserType.excel_xml == parserType){
			parser = new ExcelParser();
			parser_pool.put(parserType, parser);
		}
		return parser;
	}
}
