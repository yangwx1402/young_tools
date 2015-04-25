package com.young.tools.export.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 数据源配置信息类
 * @author yangy
 *
 */
@XStreamAlias("datasource")
public class DataSourceConfig {
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    @XStreamAsAttribute
	private String driver;
    @XStreamAsAttribute
	private String url;
    @XStreamAsAttribute
	private String user;
    @XStreamAsAttribute
	private String password;
}
