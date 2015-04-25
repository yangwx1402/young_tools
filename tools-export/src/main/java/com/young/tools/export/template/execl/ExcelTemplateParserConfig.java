package com.young.tools.export.template.execl;

/**
 * execl模板解配置
 * @author yangy
 *
 */
public class ExcelTemplateParserConfig {
	 public String getREPORT_TEMPLATE_FILE_PATH() {
		return REPORT_TEMPLATE_FILE_PATH;
	}

	public void setREPORT_TEMPLATE_FILE_PATH(String rEPORT_TEMPLATE_FILE_PATH) {
		REPORT_TEMPLATE_FILE_PATH = rEPORT_TEMPLATE_FILE_PATH;
	}

	public String getSERIAL_NO() {
		return SERIAL_NO;
	}

	public void setSERIAL_NO(String sERIAL_NO) {
		SERIAL_NO = sERIAL_NO;
	}

	public String getDATA_BEGIN() {
		return DATA_BEGIN;
	}

	public void setDATA_BEGIN(String dATA_BEGIN) {
		DATA_BEGIN = dATA_BEGIN;
	}

	public String getUSE_STYLES() {
		return USE_STYLES;
	}

	public void setUSE_STYLES(String uSE_STYLES) {
		USE_STYLES = uSE_STYLES;
	}

	public String getDEFAULT_STYLES() {
		return DEFAULT_STYLES;
	}

	public void setDEFAULT_STYLES(String dEFAULT_STYLES) {
		DEFAULT_STYLES = dEFAULT_STYLES;
	}

	public int getTemplate_sheet_index() {
		return template_sheet_index;
	}

	public void setTemplate_sheet_index(int template_sheet_index) {
		this.template_sheet_index = template_sheet_index;
	}

	public float getDefaultRowHeight() {
		return defaultRowHeight;
	}

	public void setDefaultRowHeight(float defaultRowHeight) {
		this.defaultRowHeight = defaultRowHeight;
	}

	/**报表模板文件的存储位置*/
    private String REPORT_TEMPLATE_FILE_PATH;
    /**本列开始填充序号的标识*/
    private String SERIAL_NO = "serialNo";
    /**本行开始填充数据的标识*/
    private String DATA_BEGIN = "dataBegin";
    /**表格采用同列样式的标识*/
    private String USE_STYLES = "useStyles";
    /**表格样式采用的默认样式*/
    private String DEFAULT_STYLES = "defaultStyles"; 
   
    private int template_sheet_index = 0;
    
    private float defaultRowHeight;
    
    private String placeholder = "#";

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
    
}
