package com.young.tools.export.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 报表sql参数配置类
 * @author yangy
 *
 */
@XStreamAlias("param")
public class SqlParamConfig {

	@XStreamAsAttribute
	private int index;
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String value;
	@XStreamAsAttribute
	private String name = "";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
