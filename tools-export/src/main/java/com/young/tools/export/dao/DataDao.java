package com.young.tools.export.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.young.tools.export.config.Base;
import com.young.tools.export.config.ShowFieldConfig;
import com.young.tools.export.config.SqlParamConfig;
import com.young.tools.export.jdbc.DBConnection;
/**
 * 利用配置信息查询数据
 * @author yangy
 *
 */
public class DataDao {
    
	private static final Log log = LogFactory.getLog(DataDao.class);
    /**
     * 根据配置信息获取数据
     * @param base
     * @return
     * @throws Exception
     */
	public List<Object[]> getData(Base base) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData metaData = null;

		StringBuffer sql = new StringBuffer();
		sql.append(base.getSql().getSelect());

		con = DBConnection.getConnection(base.getDatasource());
		ps = con.prepareStatement(sql.toString());
		log.info("--sql--[" + sql.toString() + "]");
		SqlParamConfig param = null;
		if (!CollectionUtils.isEmpty(base.getParams())) {
			for (int i = 0; i < base.getParams().size(); i++) {
				param = base.getParams().get(i);
				if (is_String(param)) {
					ps.setString(param.getIndex(), param.getValue());
				} else {
					ps.setObject(param.getIndex(), getObject(param));
				}
			}
		}
		rs = ps.executeQuery();
		metaData = rs.getMetaData();
		List<Object[]> result = getResultSet(rs, metaData, base.getShowFields());
		DBConnection.closeConnection(con, ps, rs);
		return result;
	}

	private List<Object[]> getResultSet(ResultSet rs,
			ResultSetMetaData metaData, List<ShowFieldConfig> showFields) throws SQLException {
		List<Object[]> result = new ArrayList<Object[]>();
		boolean flag = CollectionUtils.isEmpty(showFields);
		List<String> columns = getColumns(showFields,flag,metaData);
		Object[] temp = null;
		while(rs.next()){
			temp = getObjectArray(rs,columns);
			if(temp!=null)
				result.add(temp);
		}
		return result;
	}
	
	private Object[] getObjectArray(ResultSet rs, List<String> columns) throws SQLException {
		if(CollectionUtils.isEmpty(columns))
			return null;
		Object[] temp = new Object[columns.size()];
		for(int i=0;i<columns.size();i++){
			temp[i]=rs.getObject(columns.get(i));
		}
		return temp;
	}

	private List<String> getColumns(List<ShowFieldConfig> showFields,
			boolean flag, ResultSetMetaData metaData) throws SQLException {
		List<String> columns = new ArrayList<String>();
		int column_length = flag?metaData.getColumnCount():showFields.size();
		if(flag){
			for(int i=1;i<=column_length;i++)
				columns.add(metaData.getColumnName(i));
		}else{
			Collections.sort(showFields);
	        for(ShowFieldConfig config:showFields)
	        	columns.add(config.getValue());
		}
		return columns;
	}

	private boolean is_String(SqlParamConfig param) {
		if(StringUtils.isBlank(param.getType()))
			return true;
		if(param.getType().toLowerCase().contains("string"))
			return true;
		return false;
	}

	private Object getObject(SqlParamConfig param) {
		if(param.getType().toLowerCase().contains("int"))
			return Integer.parseInt(param.getValue());
		if(param.getType().toLowerCase().contains("long"))
			return Long.parseLong(param.getValue());
		if(param.getType().toLowerCase().contains("double"))
			return Double.parseDouble(param.getValue());
		return param.getValue();
	}
}
