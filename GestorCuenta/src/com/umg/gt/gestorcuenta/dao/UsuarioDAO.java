package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.ViewUser;

public class UsuarioDAO extends GeneralDAO {
	
	public String obtieneIdPersona(Connection conn) throws SQLException {
		String SQL = "SELECT NVL (MAX (DWPEPERSONAID), 0) + 1 ";
		SQL = SQL + "	FROM DW_PE_PERSONA ";
		return executeQuery(conn, SQL, "obtieneIdPersona: " + SQL);
	}
	
	public int insertaUsuario(Connection conn, ViewUser usuario) throws SQLException {
		String SQL = " INSERT INTO DW_US_USUARIO (DWUSCODIGO, ";
		SQL = SQL + "                      DWUSUSUARIOID, ";
		SQL = SQL + "                      DWUSPASSWORD, ";
		SQL = SQL + "                      DWUSFECHACREADO, ";
		SQL = SQL + "                      DWPEPERSONAID, ";
		SQL = SQL + "                      DWPFPERFILID) ";
		SQL = SQL + "     VALUES ( (SELECT NVL (MAX (DWUSCODIGO), 0) + 1 FROM DW_US_USUARIO), ";
		SQL = SQL + "				'" + usuario.getDwususuarioid() + "',";
		SQL = SQL + "(SELECT ENCRIPTA('" + usuario.getDwuspassword() + "') FROM DUAL),";
		SQL = SQL + "				SYSDATE,";
		SQL = SQL + "				" + usuario.getDwpepersonaid() + ",";
		SQL = SQL + "				" + usuario.getDwpfperfilid() + ") ";
		
		int ret = executeUpdate(conn, SQL, "insertaUsuario: " + SQL);
		return ret;
	}
	
	public int insertaPersona(Connection conn, ViewUser persona) throws SQLException {
		String SQL = " INSERT INTO DW_PE_PERSONA (DWPEPERSONAID, ";
		SQL = SQL + "                           DWPEPRIMERNOMBRE, ";
		SQL = SQL + "                           DWPESEGUNDONOMBRE, ";
		SQL = SQL + "                           DWPETERCERNOMBRE, ";
		SQL = SQL + "                           DWPEPRIMERAPELLIDO, ";
		SQL = SQL + "                           DWPESEGUNDOAPELLIDO, ";
		SQL = SQL + "                           DWPEAPELLIDOCASADA, ";
		SQL = SQL + "                           DWPECORREO, ";
		SQL = SQL + "                           DWPETELEFONO, ";
		SQL = SQL + "                           DWPEFECHACREADO) ";
		SQL = SQL + "   VALUES ( " + persona.getDwpepersonaid() + ",";
		SQL = SQL + "			'" + persona.getDwpeprimernombre() + "',";
		SQL = SQL + "			'" + persona.getDwpesegundonombre() + "',";
		SQL = SQL + "			'" + persona.getDwpetercernombre() + "',";
		SQL = SQL + "			'" + persona.getDwpeprimerapellido() + "',";
		SQL = SQL + "			'" + persona.getDwpesegundoapellido() + "',";
		SQL = SQL + "			'" + persona.getDwpeapellidocasada() + "',";
		SQL = SQL + "			'" + persona.getDwpecorreo() + "',";
		SQL = SQL + "			" + persona.getDwpetelefono() + ",";
		SQL = SQL + "			SYSDATE ) ";
		
		int ret = executeUpdate(conn, SQL, "insertaPersona " + SQL);
		return ret;
	}
	
	public int actualizaUsuario(Connection conn, ViewUser usuario) throws SQLException {
		String SQL = " UPDATE DW_US_USUARIO SET ";
		SQL = SQL + " 	DWPFPERFILID = " + usuario.getDwpfperfilid() + " ";
		SQL = SQL + " WHERE DWUSCODIGO = " + usuario.getDwuscodigo() + " ";
		
		int ret = executeUpdate(conn, SQL, "actualizaUsuario: " + SQL);
		return ret;
	}
	
	public int actualizaPersona(Connection conn, ViewUser persona) throws SQLException {
		String SQL = " UPDATE DW_PE_PERSONA SET ";
		SQL = SQL + "	DWPEPRIMERNOMBRE = '" + persona.getDwpeprimernombre() + "', ";
		SQL = SQL + "   DWPESEGUNDONOMBRE = '" + persona.getDwpesegundonombre() + "', ";
		SQL = SQL + "   DWPETERCERNOMBRE = '" + persona.getDwpetercernombre() + "', ";
		SQL = SQL + "   DWPEPRIMERAPELLIDO = '" + persona.getDwpeprimerapellido() + "', ";
		SQL = SQL + "   DWPESEGUNDOAPELLIDO = '" + persona.getDwpesegundoapellido() + "', ";
		SQL = SQL + "   DWPEAPELLIDOCASADA = '" + persona.getDwpeapellidocasada() + "', ";
		SQL = SQL + "   DWPECORREO = '" + persona.getDwpecorreo() + "', ";
		SQL = SQL + "   DWPETELEFONO = " + persona.getDwpetelefono() + " ";
		SQL = SQL + " WHERE DWPEPERSONAID = " + persona.getDwpepersonaid() + " ";
		
		int ret = executeUpdate(conn, SQL, "actualizaPersona " + SQL);
		return ret;
	}
	
	public int actualizaPassword(Connection conn, ViewUser usuario) throws SQLException {
		String SQL = "UPDATE DW_US_USUARIO SET ";
		SQL = SQL + " DWUSPASSWORD = (SELECT ENCRIPTA('" + usuario.getDwuspassword() + "') FROM DUAL) ";
		SQL = SQL + " WHERE DWUSCODIGO = " + usuario.getDwuscodigo() + " ";
		return executeUpdate(conn, SQL, "actualizaPassword: " + SQL);
	}
	
	public List<ViewUser> obtieneListaUsuario(Connection conn, ViewUser usuario) throws SQLException {
		String SQL = " SELECT * ";
		SQL = SQL + "  FROM (SELECT    DWPEPRIMERNOMBRE ";
		SQL = SQL + "               || ' ' ";
		SQL = SQL + "               || NVL (DWPESEGUNDONOMBRE, '') ";
		SQL = SQL + "               || ' ' ";
		SQL = SQL + "               || NVL (DWPETERCERNOMBRE, '') ";
		SQL = SQL + "                  AS NOMBRES, ";
		SQL = SQL + "                  DWPEPRIMERAPELLIDO ";
		SQL = SQL + "               || ' ' ";
		SQL = SQL + "               || NVL (DWPESEGUNDOAPELLIDO, '') ";
		SQL = SQL + "               || ' ' ";
		SQL = SQL + "               || NVL (DWPEAPELLIDOCASADA, '') ";
		SQL = SQL + "                  AS APELLIDOS, ";
		SQL = SQL + "               PF.DWPFNOMBRE AS PERFIL, ";
		SQL = SQL + "               PE.DWPEPERSONAID, ";
		SQL = SQL + "               PE.DWPEPRIMERNOMBRE, ";
		SQL = SQL + "               PE.DWPESEGUNDONOMBRE, ";
		SQL = SQL + "               PE.DWPETERCERNOMBRE, ";
		SQL = SQL + "               PE.DWPEPRIMERAPELLIDO, ";
		SQL = SQL + "               PE.DWPESEGUNDOAPELLIDO, ";
		SQL = SQL + "               PE.DWPEAPELLIDOCASADA, ";
		SQL = SQL + "               PE.DWPECORREO, ";
		SQL = SQL + "               PE.DWPETELEFONO, ";
		SQL = SQL + "               TO_CHAR(PE.DWPEFECHACREADO, 'dd/MM/yyyy') AS DWPEFECHACREADO, ";
		SQL = SQL + "               US.DWUSCODIGO, ";
		SQL = SQL + "               US.DWUSUSUARIOID, ";
		SQL = SQL + "               US.DWUSPASSWORD, ";
		SQL = SQL + "               TO_CHAR(US.DWUSFECHACREADO, 'dd/MM/yyyy') AS DWUSFECHACREADO, ";
		SQL = SQL + "               US.DWPFPERFILID ";
		SQL = SQL + "          FROM DW_US_USUARIO US, DW_PE_PERSONA PE, DW_PF_PERFIL PF ";
		SQL = SQL + "         WHERE US.DWPEPERSONAID = PE.DWPEPERSONAID ";
		SQL = SQL + " 			AND PF.DWPFPERFILID = US.DWPFPERFILID ) ";
		SQL = SQL + " WHERE 1 = 1 ";
		SQL = SQL + "  ";
		
		return selectStatement(conn, SQL, ViewUser.class, "obtieneListaUsuario: " + SQL);
	}
	
}
