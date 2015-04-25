package com.young.tools.export.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 基本配置Base中的扩展字段,可用于execl填写预置的变量
 * @author yangy
 *
 */
@XStreamAlias("constant")
public class ConstantConfig {
    @XStreamAsAttribute
	private String key;
    @XStreamAsAttribute
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
