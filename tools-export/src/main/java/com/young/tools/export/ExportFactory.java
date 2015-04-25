package com.young.tools.export;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.young.tools.export.service.ExportService;
import com.young.tools.export.service.execl.ExcelService;
/**
 * 导出工具工厂类
 * @author yangy
 *
 */
public class ExportFactory {

	private static final Log log = LogFactory.getLog(ExportFactory.class);
	/**
	 * 工具池
	 */
	private static Map<ExportType,ExportService> export_pool = new HashMap<ExportType,ExportService>();
	/**
	 * 根据type获取导出工具
	 * @param type  execl,xml,csv等等,目前只支持execl
	 * @return @see ExportService 返回一个服务对象
	 */
	public static ExportService getExporter(ExportType type){
		if(export_pool.containsKey(type))
			return export_pool.get(type);
		ExportService exporter = null;
		if(ExportType.EXECL==type)
			exporter = new ExcelService();
		log.info("--create a ExportService name is --["+exporter+"]");
		export_pool.put(type, exporter);
		return exporter;
	}
}
