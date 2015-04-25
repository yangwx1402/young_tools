package com.young.tools.export.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 基本配置信息类
 * @author yangy
 *
 */
@XStreamAlias("base")
public class Base implements Comparable<Base>{

	/**
	 * 基本配置的名称,如可以用做execl的sheet的名称
	 */
	@XStreamAsAttribute
	private String name;
	/**
	 * 基本配置的索引位置,如可以用作execl的sheet位置
	 */
	@XStreamAsAttribute
	private int index;
	/**
	 * 基本配置的名称,如可以用做execl的sheet的名称
	 * @return 
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    /**
     * 基本配置的索引位置,如可以用作execl的sheet位置
     * @return
     */
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@XStreamAlias("sql")
	private SqlConfig sql;
	@XStreamAlias("datasource")
	private DataSourceConfig datasource;
	@XStreamImplicit(itemFieldName="param")
	private List<SqlParamConfig> params;
	@XStreamImplicit(itemFieldName="field")
	private List<ShowFieldConfig> showFields;
	
	@XStreamImplicit(itemFieldName="constant")
	private List<ConstantConfig> constants;
    /**
     * 模板中的常量填充值
     * @return
     */
	public List<ConstantConfig> getConstants() {
		return constants;
	}
    
	public void setConstants(List<ConstantConfig> constants) {
		this.constants = constants;
	}
    /**
     * sql信息对象
     * @return
     */
	public SqlConfig getSql() {
		return sql;
	}

	public void setSql(SqlConfig sql) {
		this.sql = sql;
	}
    /**
     * 数据源信息对象
     * @return
     */
	public DataSourceConfig getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSourceConfig datasource) {
		this.datasource = datasource;
	}
   /**
    * sql参数对象列表
    * @return
    */
	public List<SqlParamConfig> getParams() {
		return params;
	}

	public void setParams(List<SqlParamConfig> params) {
		this.params = params;
	}
    /**
     * 报表中展示字段信息对象
     * @return
     */
	public List<ShowFieldConfig> getShowFields() {
		return showFields;
	}

	public void setShowFields(List<ShowFieldConfig> showFields) {
		this.showFields = showFields;
	}

	@Override
	public int compareTo(Base o) {
		if(this.getIndex()>o.getIndex())
			return 1;
		if(this.getIndex()<o.getIndex())
			return -1;
		return 0;
	}

}
