package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.DocumentoORM;
import com.umg.gt.gestorcuenta.orm.TipoDocumentoORM;

public class DocumentoDAO extends GeneralDAO {
	
	public List<TipoDocumentoORM> obtieneTipoDocumento(Connection conn) throws SQLException {
		String SQL = "SELECT DWDTTIPODOCUMENTOID AS TIPODOCUMENTOID, ";
		SQL = SQL +         "DWDTDESCRIPCION AS DESCRIPCION_DOCUMENTO ";
		SQL = SQL +    "FROM DW_CAT_TIPO_DOCUMENTO ";
		return selectStatement(conn, SQL, TipoDocumentoORM.class, "obtieneTipoDocumento: " + SQL);
	}
	
	public List<DocumentoORM> obtieneDocumentos(Connection conn, DocumentoORM filtro) throws SQLException {
		String SQL = " SELECT * ";
		SQL = SQL + "    FROM (SELECT TO_CHAR (DWACNUMCUENTA) AS NUM_CUENTA, ";
		SQL = SQL + "                 DWDCMONTO AS MONTO, ";
		SQL = SQL + "                 DWDCDESCRIPCION AS DESCRIPCION, ";
		SQL = SQL + "                 DWDTDESCRIPCION AS TIPO_DOCUMENTO, ";
		SQL = SQL + "                 TO_CHAR(DWDCFECHACREADO,'dd/MM/yyyy') AS FECHA, ";
		SQL = SQL + "                 (SELECT    DWPEPRIMERNOMBRE ";
		SQL = SQL + "                         || ' ' ";
		SQL = SQL + "                         || NVL (DWPESEGUNDONOMBRE, '') ";
		SQL = SQL + "                         || ' ' ";
		SQL = SQL + "                         || NVL (DWPETERCERNOMBRE, '') ";
		SQL = SQL + "                         || ' ' ";
		SQL = SQL + "                         || DWPEPRIMERAPELLIDO ";
		SQL = SQL + "                         || ' ' ";
		SQL = SQL + "                         || NVL (DWPESEGUNDOAPELLIDO, '') ";
		SQL = SQL + "                         || ' ' ";
		SQL = SQL + "                         || NVL (DWPEAPELLIDOCASADA, '') ";
		SQL = SQL + "                    FROM DW_PE_PERSONA ";
		SQL = SQL + "                   WHERE CODIGO IS NOT NULL AND CODIGO = CTA.DWACCLIENTEID) AS NOMBRE_CUENTA ";
		SQL = SQL + "            FROM DW_AL_CUENTA CTA ";
		SQL = SQL + "            JOIN DW_AL_DESEMBOLSO DES ";
		SQL = SQL + "              ON CTA.DWACCUENTAID = DES.DWACCUENTAID ";
		SQL = SQL + "            JOIN DW_AL_DOCUMENTO DOC ";
		SQL = SQL + "              ON DES.DWDIDESEMBOLSOID = DOC.DWDIDESEMBOLSOID ";
		SQL = SQL + "             AND DES.DWACCUENTAID = DOC.DWACCUENTAID ";
		SQL = SQL + "            JOIN DW_CAT_TIPO_DOCUMENTO TDO ";
		SQL = SQL + "              ON TDO.DWDTTIPODOCUMENTOID = DOC.DWDTTIPODOCUMENTOID ";
		SQL = SQL + "            ) ";
		SQL = SQL + "		WHERE 1 = 1 ";
		
		if(filtro != null){
			if(filtro.getTipo_documento() != null){
				SQL = SQL + " AND TIPO_DOCUMENTO = '" + filtro.getTipo_documento() + "' ";
			}
			if(filtro.getNombre_cuenta() != null && !"".equals(filtro.getNombre_cuenta())){
				SQL = SQL + " AND UPPER(NOMBRE_CUENTA) LIKE UPPER('%" + filtro.getNombre_cuenta() + "%')";
			}
		}
		return selectStatement(conn, SQL, DocumentoORM.class, "obtieneDocumentos: " + SQL);
	}
	
	
}
