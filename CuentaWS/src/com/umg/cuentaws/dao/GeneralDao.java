package com.umg.cuentaws.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.umg.cuentaws.util.Util;

public class GeneralDao {
	
	public <T> List<T> selectStatement(Connection conn, String sql, Class<T> clase, String p_mensaje) throws SQLException {
		Util.mensajeConsola(p_mensaje + sql);
		List<T> lst;
		
		QueryRunner qry = new QueryRunner();
		BeanListHandler<T> blh = new BeanListHandler<T>(clase);
		
		lst = (List<T>) qry.query(conn, sql, blh);
		
		return lst;
	}
	
	public int executeUpdate(Connection conn, String SQL, String p_mensaje) throws SQLException {
		Util.mensajeConsola(p_mensaje + SQL);
		Statement st = null;
		int i = 0;
		try{
			st = conn.createStatement();
			i = st.executeUpdate(SQL);
		}finally{
			closeStatements(st, null);
		}
		return i;
	}
	
	public String executeQuery(Connection conn, String SQL, String p_mensaje) throws SQLException {
		Util.mensajeConsola(p_mensaje + SQL);
		String ret = "";
		Statement st = null;
		ResultSet rs = null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery(SQL);
			
			if(rs.next()){ 
				ret = rs.getString(1); 
			}
		}finally{
			closeStatements(st, rs);
		}
		return ret;
	}
	
	public static void closeStatements(Statement st, ResultSet rs) {
		DbUtils.closeQuietly(null, st, rs);
	}
	
	public String obtieneParam(Connection conn, String param) throws SQLException {
		String SQL = " SELECT DWPADESCRIPCION ";
		SQL = SQL + "  FROM DW_PAR_PARAMETROS ";
		SQL = SQL + " WHERE DWPANOMBREPARAMETRO = '" + param + "' ";
		return executeQuery(conn, SQL, "obtieneParam: ");
	}
	
}
