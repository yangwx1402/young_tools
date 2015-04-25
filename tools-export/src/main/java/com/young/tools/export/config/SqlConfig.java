package com.young.tools.export.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 报表sql信息配置类
 * @author yangy
 *
 */
@XStreamAlias("sql")
public class SqlConfig {

	@XStreamAsAttribute
	private String select;

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}


}
