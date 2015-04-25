package com.young.tools.export.config.parser;

import java.io.FileInputStream;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.young.tools.export.config.Base;
import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.config.DataSourceConfig;
import com.young.tools.export.config.ShowFieldConfig;
import com.young.tools.export.config.SqlConfig;
import com.young.tools.export.config.SqlParamConfig;
/**
 *基础的xml解析器,用于解析默认的BaseConfig对象
 * @author yangy
 *
 */
public abstract class BaseParser implements Parser {

	protected static XStream xmlReaderByXStream = null;

	protected synchronized static XStream getXStream() {
		if (xmlReaderByXStream == null) {
			xmlReaderByXStream = new XStream();
			xmlReaderByXStream.autodetectAnnotations(true);
			xmlReaderByXStream.processAnnotations(BaseConfig.class);
			xmlReaderByXStream.processAnnotations(Base.class);
			xmlReaderByXStream.processAnnotations(SqlConfig.class);
			xmlReaderByXStream.processAnnotations(DataSourceConfig.class);
			xmlReaderByXStream.processAnnotations(SqlParamConfig.class);
			xmlReaderByXStream.processAnnotations(ShowFieldConfig.class);
		}
		return xmlReaderByXStream;
	}

	/**
	 * 从xml中解析BaseConfig对象
	 */
	@Override
	public BaseConfig getConfig(InputStream in) throws Exception {
		BaseConfig config = (BaseConfig) getXStream().fromXML(in);
		return config;
	}
    /**
     * 从xml中解析BaseConfig对象
     */
	@Override
	public BaseConfig getConfig(String filename) throws Exception {
		FileInputStream fis = new FileInputStream(filename);
		return getConfig(fis);
	}

}
