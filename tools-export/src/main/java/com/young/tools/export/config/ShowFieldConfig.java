package com.young.tools.export.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 报表sql中需要展示的字段名称配置
 * @author yangy
 *
 */
@XStreamAlias("field")
public class ShowFieldConfig implements Comparable<ShowFieldConfig>{

	@XStreamAsAttribute
	private int index;
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String value;
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
	@Override
	public int compareTo(ShowFieldConfig o) {
		if(this.getIndex()>o.getIndex())
			return 1;
		if(this.getIndex()<o.getIndex())
			return -1;
		return 0;
	}
	
	
}
