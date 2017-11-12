package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.umg.gt.gestorcuenta.orm.SolicitudORM;
import com.umg.gt.gestorcuenta.util.Conf;

public class GeneralDAO {
	
	public <T> List<T> selectStatement(Connection conn, String sql, Class<T> clase, String p_mensaje) throws SQLException {
		Conf.mensajeConsola(p_mensaje);
		List<T> lst;
		
		QueryRunner qry = new QueryRunner();
		BeanListHandler<T> blh = new BeanListHandler<T>(clase);
		
		lst = (List<T>) qry.query(conn, sql, blh);
		
		return lst;
	}
	
	public int executeUpdate(Connection conn, String SQL, String p_mensaje) throws SQLException {
		Conf.mensajeConsola(p_mensaje);
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
		Conf.mensajeConsola(p_mensaje);
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
	
	public String obtieneParametro(Connection conn, String p_nombreParametro) throws SQLException {
		String SQL = " SELECT DWPADESCRIPCION ";
		SQL = SQL + "    FROM DW_PAR_PARAMETROS ";
		SQL = SQL + "   WHERE UPPER(DWPANOMBREPARAMETRO) LIKE UPPER('%" + p_nombreParametro + "%') ";
		return executeQuery(conn, SQL, "obtieneParametro: " + SQL);
	}
	
	public List<SolicitudORM> obtieneEstadosSolicitud(Connection conn) throws SQLException {
		String SQL = " SELECT DWSRESTADOID, ";
		SQL = SQL + "        DWSRDESCRIPCION ";
		SQL = SQL + "  FROM DW_CAT_ESTADO_SOLICITUD ";
		SQL = SQL + " ORDER BY DWSRESTADOID ASC ";
		return selectStatement(conn, SQL, SolicitudORM.class, "obtieneEstadosSolicitud: " + SQL);
	}
	
	
	public SolicitudORM obtieneClientePrueba(Connection conn) throws SQLException {
		String SQL = " SELECT DWPANOMBREPARAMETRO, ";
		SQL = SQL + "   	 DWPADESCRIPCION ";
		SQL = SQL + "    FROM DW_PAR_PARAMETROS ";
		SQL = SQL + "   WHERE DWPANOMBREPARAMETRO IN ('CLIENTE','DPI','NIT', CLIENTEID) ";
		java.sql.Statement st = null;
		java.sql.ResultSet rs = null;
		SolicitudORM orm;
		try{
			st = conn.createStatement();
			rs = st.executeQuery(SQL);
			orm = new SolicitudORM();
			
			while(rs.next()){
				if("CLIENTE".equals(rs.getString(1))){
					orm.setDwrenombrecliente(rs.getString(2));
				}else if("DPI".equals(rs.getString(1))){
					orm.setDwreidentificacion(rs.getString(2));
				}else if("NIT".equals(rs.getString(1))){
					orm.setDwrenit(rs.getString(2));
				}else if("CLIENTEID".equals(rs.getString(1))){
					orm.setDwreclienteid(rs.getString(2));
				}
			}
		}finally{
			DbUtils.closeQuietly(null, st, rs);
		}
		return orm;
	}
	
}
