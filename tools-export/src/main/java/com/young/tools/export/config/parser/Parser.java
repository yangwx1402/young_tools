package com.young.tools.export.config.parser;

import java.io.InputStream;

import com.young.tools.export.config.BaseConfig;
/**
 * 解析接口
 * @author yangy
 *
 */
public interface Parser {

	
	BaseConfig getConfig(InputStream in)throws Exception;
	
	BaseConfig getConfig(String filename) throws Exception ;
	
}
