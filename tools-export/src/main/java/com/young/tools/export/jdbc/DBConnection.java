package com.young.tools.export.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.young.tools.export.config.DataSourceConfig;

public class DBConnection {

	/**
	 * 根据DataSourceConfig信息获取数据库连接
	 * @param datasource @see DataSourceConfig
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(DataSourceConfig datasource) throws Exception{
		Class.forName(datasource.getDriver());
		return DriverManager.getConnection(datasource.getUrl(), datasource.getUser(), datasource.getPassword());
	}
	
	public static void closeConnection(Connection con,PreparedStatement ps,ResultSet rs){
		try {
			if(con!=null&&!con.isClosed()){
				con.close();
			}
			if(ps!=null)
				ps.close();
			if(rs!=null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
