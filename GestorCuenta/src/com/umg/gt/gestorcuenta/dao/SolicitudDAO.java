package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.SolicitudORM;

public class SolicitudDAO extends GeneralDAO{
	
	public int insertaSolicitud(Connection conn, SolicitudORM obj) throws SQLException {
		String SQL = " INSERT INTO DW_AL_SOLICITUD( ";
		SQL = SQL + "        DWRESOLICITUDID, ";
		SQL = SQL + "        DWRECLIENTEID, ";
		SQL = SQL + "        DWREIDENTIFICACION, ";
		SQL = SQL + "        DWRENIT, ";
		SQL = SQL + "        DWREINGRESOMENSUAL, ";
		SQL = SQL + "        DWREMONTO, ";
		SQL = SQL + "        DWSRESTADOID, ";
		SQL = SQL + "        DWUSCODIGO, ";
		SQL = SQL + "        DWREFECHA) ";
		SQL = SQL + "VALUES((SELECT NVL(MAX(DWRESOLICITUDID), 0) + 1 FROM DW_AL_SOLICITUD), ";
		SQL = SQL + " 	 " + obj.getDwreclienteid() + ", ";
		SQL = SQL + " 	'" + obj.getDwreidentificacion() + "', ";
		SQL = SQL + " 	'" + obj.getDwrenit() + "',	";
		SQL = SQL + " 	 " + obj.getDwreingresomensual() + ", ";
		SQL = SQL + " 	 " + obj.getDwremonto() + ", ";
		SQL = SQL + " 	 " + obj.getDwsrestadoid() + ", ";
		SQL = SQL + " 	 " + obj.getDwuscodigo() + ", ";
		SQL = SQL + " 		SYSDATE ) ";
		return executeUpdate(conn, SQL, "insertaSolicitud: " + SQL);
	}
	
	public List<SolicitudORM> obtieneListaSolicitud(Connection conn, SolicitudORM filtro) throws SQLException {
		String SQL = " SELECT SOL.DWRESOLICITUDID, ";
		SQL = SQL + "        SOL.DWRECLIENTEID, ";
		SQL = SQL + "        SOL.DWREIDENTIFICACION, ";
		SQL = SQL + "        SOL.DWRENIT, ";
		SQL = SQL + "        SOL.DWREINGRESOMENSUAL, ";
		SQL = SQL + "        SOL.DWREMONTO, ";
		SQL = SQL + "        SOL.DWSRESTADOID, ";
		SQL = SQL + "        SOL.DWUSCODIGO, ";
		SQL = SQL + "        SOL.DWREFECHA, ";
		SQL = SQL + "        USU.DWUSUSUARIOID, ";
		SQL = SQL + "        EST.DWSRDESCRIPCION ";
		SQL = SQL + "  FROM DW_AL_SOLICITUD SOL, ";
		SQL = SQL + "       DW_US_USUARIO USU, ";
		SQL = SQL + "       DW_CAT_ESTADO_SOLICITUD EST ";
		SQL = SQL + " WHERE SOL.DWUSCODIGO = USU.DWUSCODIGO ";
		SQL = SQL + "   AND SOL.DWSRESTADOID = EST.DWSRESTADOID ";
		
		if(filtro != null){
			if(filtro.getDwreidentificacion() != null){
				SQL = SQL + " AND DWREIDENTIFICACION LIKE '%" + filtro.getDwreidentificacion() + "%'";
			}
			if(filtro.getDwrenit() != null){
				SQL = SQL + " AND DWRENIT LIKE '%" + filtro.getDwrenit() + "%'";
			}
			if(filtro.getDwsrestadoid() != null){
				SQL = SQL + " AND DWSRESTADOID = " + filtro.getDwsrestadoid() + " ";
			}
		}
		return selectStatement(conn, SQL, SolicitudORM.class, "obtieneListaSolicitud: " + SQL);
	}
	
	public int actualizaSolicitud(Connection conn, SolicitudORM obj) throws SQLException {
		String SQL = "UPDATE DW_AL_SOLICITUD ";
		SQL = SQL + "    SET DWREINGRESOMENSUAL = " + obj.getDwreingresomensual() + ", ";
		SQL = SQL + "        DWREMONTO = " + obj.getDwremonto() + ", ";
		SQL = SQL + "        DWSRESTADOID = " + obj.getDwsrestadoid() + " ";
		SQL = SQL + "  WHERE DWRESOLICITUDID = " + obj.getDwresolicitudid() + " ";
		return executeUpdate(conn, SQL, "actualizaSolicitud: " + SQL);
	}
	
}
