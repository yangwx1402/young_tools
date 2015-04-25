package com.young.tools.export.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.young.tools.export.config.BaseConfig;
import com.young.tools.export.dao.DataDao;
import com.young.tools.export.template.TemplateParserConfig;
/**
 * 数据导出服务类
 * @author yangy
 *
 */
public abstract class ExportService {

	protected static final Log log = LogFactory.getLog(ExportService.class);
	
	protected static final DataDao dao = new DataDao();
	/**
	 * 导出execl表格
	 * @param config_stream
	 * @param output
	 * @param parser_config
	 * @throws Exception
	 */
	public abstract void export(InputStream config_stream,OutputStream output,TemplateParserConfig parser_config) throws Exception;
	/**
	 * 导出execl表格
	 * @param config
	 * @param output
	 * @param parser_config
	 * @throws Exception
	 */
	public abstract void export(BaseConfig config,OutputStream output,TemplateParserConfig parser_config) throws Exception;

}
