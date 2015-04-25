package com.young.tools.export.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 配置信息入口
 * @author yangy
 *
 */
@XStreamAlias("config")
public class BaseConfig {

	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Base> getBases() {
		return bases;
	}

	public void setBases(List<Base> bases) {
		this.bases = bases;
	}

	@XStreamImplicit(itemFieldName="base")
	private List<Base> bases;
}
