package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.ClienteORM;
import com.umg.gt.gestorcuenta.orm.CuentaORM;
import com.umg.gt.gestorcuenta.orm.DesembolsoORM;
import com.umg.gt.gestorcuenta.orm.TarjetaORM;
import com.umg.ws.ValidaMontoStub.ExisteTC;

public class CuentaDAO extends GeneralDAO {
	
	public List<ClienteORM> obtieneClientes(Connection conn, ClienteORM filtro) throws SQLException {
		String SQL = "  SELECT *  ";
		SQL = SQL + "  FROM (SELECT    DWPEPRIMERNOMBRE  ";
		SQL = SQL + "               || ' '  ";
		SQL = SQL + "               || NVL (DWPESEGUNDONOMBRE, '')  ";
		SQL = SQL + "               || ' '  ";
		SQL = SQL + "               || NVL (DWPETERCERNOMBRE, '')  ";
		SQL = SQL + "                  AS NOMBRES,  ";
		SQL = SQL + "                  DWPEPRIMERAPELLIDO  ";
		SQL = SQL + "               || ' '  ";
		SQL = SQL + "               || NVL (DWPESEGUNDOAPELLIDO, '')  ";
		SQL = SQL + "               || ' '  ";
		SQL = SQL + "               || NVL (DWPEAPELLIDOCASADA, '')  ";
		SQL = SQL + "                  AS APELLIDOS,  ";
		SQL = SQL + "               DWPECORREO AS CORREO, ";
		SQL = SQL + "               DWPETELEFONO AS TELEFONO, ";
		SQL = SQL + "               DIRECCION, ";
		SQL = SQL + "               CODIGO AS NIT, ";
		SQL = SQL + "				DWPEPERSONAID AS CODIGO ";
		SQL = SQL + "          FROM DW_PE_PERSONA ";
		SQL = SQL + " 		  WHERE CODIGO IS NOT NULL ";
		SQL = SQL + "  			AND CODIGO NOT IN (SELECT DWACCLIENTEID FROM DW_AL_CUENTA)) ";

		SQL = SQL + " WHERE 1 = 1 ";
		
		if(filtro != null){
			if(filtro.getNombres() != null){
				SQL = SQL + " AND UPPER(NOMBRES) LIKE UPPER('%" + filtro.getNombres() + "%')";
			}
			if(filtro.getNit() != null){
				SQL = SQL + " AND UPPER(NIT) LIKE UPPER('%" + filtro.getNit() + "%')";
			}
		}
		return selectStatement(conn, SQL, ClienteORM.class, "obtieneClientes: " + SQL);
	}
	
	public String insertaSolicitud(Connection conn, ClienteORM cliente, java.math.BigDecimal ingreso, String usuario) throws SQLException {
		String SQL = " INSERT INTO DW_AL_SOLICITUD (DWRESOLICITUDID, ";
		SQL = SQL + "                             DWRECLIENTEID, ";
		SQL = SQL + "                             DWRENIT, ";
		SQL = SQL + "                             DWREINGRESOMENSUAL, ";
		SQL = SQL + "                             DWSRESTADOID, ";
		SQL = SQL + "                             DWUSCODIGO, ";
		SQL = SQL + "                             DWREFECHA) ";
		SQL = SQL + "   SELECT NVL (MAX (DWRESOLICITUDID), 0) + 1 as solicitudid,  ";
		SQL = SQL + "    	 " + cliente.getCodigo() + ", ";
		SQL = SQL + "    	'" + cliente.getNit() + "', ";
		SQL = SQL + "    	 " + ingreso + ",  ";
		SQL = SQL + "    	 1,  ";
		SQL = SQL + "    	 " + usuario + ",  ";
		SQL = SQL + "    	 sysdate  ";
		SQL = SQL + "     FROM DW_AL_SOLICITUD ";
		executeUpdate(conn, SQL, "insertaSolicitud: " + SQL);
		
		SQL = "    SELECT MAX (DWRESOLICITUDID) ";
		SQL = SQL + "FROM DW_AL_SOLICITUD ";
		return executeQuery(conn, SQL, SQL);
	}
	
	public void insertaDatosTarjeta(Connection conn, ExisteTC tarjeta, String nit_cliente) throws SQLException {
		String SQL = "INSERT INTO DW_TC_TARJETA(NIT_CLIENTE,";
		SQL = SQL + 						  " NUMERO_TARJETA,";
		SQL = SQL + 						  " NOMBRE_TARJETA,";
		SQL = SQL + 						  "	CODIGO_SEGURIDAD,";
		SQL = SQL + 						  "	MES_VENCIMIENTO,";
		SQL = SQL + 						  "	ANIO_VENCIMIENTO)";
		SQL = SQL + "	VALUES('" + nit_cliente + "',";
		SQL = SQL + 		" " + tarjeta.getArg0() + ", ";
		SQL = SQL + 		"'" + tarjeta.getArg1() + "', ";
		SQL = SQL + 		"'" + tarjeta.getArg2() + "', ";
		SQL = SQL + 		" " + tarjeta.getArg3() + ", ";
		SQL = SQL + 		" " + tarjeta.getArg4() + ") ";
		executeUpdate(conn, SQL, "insertaDatosTarjeta: " + SQL);
	}
	
	public String creaCuenta(Connection conn, String solicitudid) throws SQLException {
		String SQL = "SELECT DW_CREA_CUENTA_FNC(" + solicitudid + ") ";
		SQL = SQL + "   FROM DUAL ";
		return executeQuery(conn, SQL, "creaCuenta: " + SQL);
	}
	
	public String validaTarjeta(Connection conn, String num_tarjeta) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_TC_TARJETA ";
		SQL = SQL + " WHERE NUMERO_TARJETA = '" + num_tarjeta + "' ";
		return executeQuery(conn, SQL, "validaTarjeta: " + SQL);
	}
	
	public List<CuentaORM> obtieneDatosCuenta(Connection conn, CuentaORM filtro, int busqueda) throws SQLException {
		String SQL = " SELECT * ";
		SQL = SQL + "    FROM (SELECT DWACCUENTAID, ";
		SQL = SQL + "                 TO_CHAR (DWACNUMCUENTA) AS NUM_CUENTA, ";
		SQL = SQL + "                 DWATDESCRIPCION AS TIPO_CUENTA, ";
		SQL = SQL + "                 DWCADESCRIPCION AS CATEGORIA, ";
		SQL = SQL + "                 DWATINTERES AS TASA_INTERES, ";
		SQL = SQL + "                 DWCACREDITOMAXIMO AS DISPONIBLE, ";
		SQL = SQL + "                 EXTRACT (DAY FROM DWATFECHACORTE) AS FECHA_CORTE, ";
		SQL = SQL + "                 DWATFECHALIMITE AS FECHA_PAGO, ";
		SQL = SQL + "                 TO_CHAR(DWACFECHACREADO, 'dd/MM/yyyy') AS FECHA_CREADO, ";
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
		SQL = SQL + "                   WHERE CODIGO IS NOT NULL AND CODIGO = CTA.DWACCLIENTEID) ";
		SQL = SQL + "                    AS NOMBRE_CUENTA, ";
		SQL = SQL + " 					(SELECT CODIGO ";
		SQL = SQL + "                    FROM DW_PE_PERSONA ";
		SQL = SQL + "                   WHERE CODIGO IS NOT NULL AND CODIGO = CTA.DWACCLIENTEID) ";
		SQL = SQL + "                    AS NIT ";
		SQL = SQL + "            FROM DW_AL_CUENTA CTA ";
		SQL = SQL + "                 JOIN DW_CAT_CATEGORIA CAT ";
		SQL = SQL + "                    ON CTA.DWCACATEGORIAID = CAT.DWCACATEGORIAID ";
		SQL = SQL + "                 JOIN DW_CAT_TIPO_CUENTA TCT ";
		SQL = SQL + "                    ON CTA.DWATTIPOCUENTAID = TCT.DWATTIPOCUENTAID) ";
		if(busqueda == 1){
			SQL = SQL + "  HAVING MAX (DWACCUENTAID) = DWACCUENTAID ";
			SQL = SQL + "GROUP BY DWACCUENTAID, ";
			SQL = SQL + "         NUM_CUENTA, ";
			SQL = SQL + "         TIPO_CUENTA, ";
			SQL = SQL + "         CATEGORIA, ";
			SQL = SQL + "         TASA_INTERES, ";
			SQL = SQL + "         DISPONIBLE, ";
			SQL = SQL + "         FECHA_CORTE, ";
			SQL = SQL + "         FECHA_PAGO, ";
			SQL = SQL + "         FECHA_CREADO, ";
			SQL = SQL + "         NOMBRE_CUENTA ";
		}else if(filtro != null){
			SQL = SQL + " WHERE 1 = 1 ";
			if(filtro.getNombre_cuenta() != null && !"".equals(filtro.getNombre_cuenta())){
				SQL = SQL + " AND UPPER(NOMBRE_CUENTA) LIKE UPPER('%" + filtro.getNombre_cuenta() + "%') ";
			}
			if(filtro.getNit() != null && !"".equals(filtro.getNit())){
				SQL = SQL + " AND NIT = '" + filtro.getNit() + "' ";
			}
		}
		SQL = SQL + " ORDER BY DWACCUENTAID DESC ";
		return selectStatement(conn, SQL, CuentaORM.class, "obtieneDatosCuenta: " + SQL);
	}
	
	public List<DesembolsoORM> obtieneListaDesembolso(Connection conn, String cuentaid) throws SQLException {
		String SQL = " select dwdidescripcion as descripcion, ";
		SQL = SQL + "       NVL(MONTO_PENDIENTE,0) - NVL(MONTO_PAGADO,0) AS PENDIENTE, ";
		SQL = SQL + "       NVL(MONTO_PAGADO,0) AS PAGADO, ";
		SQL = SQL + "       NVL(PAGADAS,0) as cuotas_pagadas, ";
		SQL = SQL + "       DWDINUMCUOTA - NVL(PAGADAS,0) AS CUOTAS_PENDIENTES, ";
		SQL = SQL + "       TO_CHAR(DWDIFECHACREADO,'dd/MM/yyyy') AS FECHA, ";
		SQL = SQL + "		DES.DWDIDESEMBOLSOID ";
		SQL = SQL + "  FROM DW_AL_DESEMBOLSO DES, ";
		SQL = SQL + "      (SELECT SUM(DWDCMONTO) * -1 AS MONTO_PAGADO ,DWDIDESEMBOLSOID, DWACCUENTAID ";
		SQL = SQL + "         FROM DW_AL_DOCUMENTO DOC ";
		SQL = SQL + "        WHERE DOC.DWDTTIPODOCUMENTOID = 2 ";
		SQL = SQL + "        GROUP BY DWDIDESEMBOLSOID, DWACCUENTAID) A, ";
		SQL = SQL + "       (SELECT SUM(DWDCMONTO) AS MONTO_PENDIENTE ,DWDIDESEMBOLSOID, DWACCUENTAID ";
		SQL = SQL + "          FROM DW_AL_DOCUMENTO DOC ";
		SQL = SQL + "         WHERE DOC.DWDTTIPODOCUMENTOID = 1 ";
		SQL = SQL + "         GROUP BY DWDIDESEMBOLSOID, DWACCUENTAID) B, ";
		SQL = SQL + "        (SELECT COUNT(1) AS PAGADAS, DWDIDESEMBOLSOID, DWACCUENTAID ";
		SQL = SQL + "           FROM DW_AL_DOCUMENTO ";
		SQL = SQL + "          WHERE DWDTTIPODOCUMENTOID = 2 ";
		SQL = SQL + "          GROUP BY DWDIDESEMBOLSOID, DWACCUENTAID) C ";
		SQL = SQL + " WHERE DES.DWACCUENTAID = " + cuentaid + " ";
		SQL = SQL + "   AND DES.DWDIDESEMBOLSOID = A.DWDIDESEMBOLSOID(+) ";
		SQL = SQL + "   AND DES.DWDIDESEMBOLSOID = B.DWDIDESEMBOLSOID(+) ";
		SQL = SQL + "   AND DES.DWDIDESEMBOLSOID = C.DWDIDESEMBOLSOID(+) ";
		SQL = SQL + "   AND DES.DWACCUENTAID = A.DWACCUENTAID(+) ";
		SQL = SQL + "   AND DES.DWACCUENTAID = B.DWACCUENTAID(+) ";
		SQL = SQL + "   AND DES.DWACCUENTAID = C.DWACCUENTAID(+) ";
		return selectStatement(conn, SQL, DesembolsoORM.class, "obtieneListaDesembolso: " + SQL);
	}
	
	public List<TarjetaORM> obtieneTarjetaCliente(Connection conn, String nit_cliente) throws SQLException {
		String SQL = " SELECT TO_CHAR(NUMERO_TARJETA) AS NUMERO_TARJETA, ";
		SQL = SQL + "        NOMBRE_TARJETA, ";
		SQL = SQL + "        CODIGO_SEGURIDAD, ";
		SQL = SQL + "        MES_VENCIMIENTO, ";
		SQL = SQL + "        ANIO_VENCIMIENTO  ";
		SQL = SQL + "   FROM DW_TC_TARJETA ";
		return selectStatement(conn, SQL, TarjetaORM.class, "obtieneTarjetaCliente: " + SQL);
	}
	
	public int creaNotaDebito(Connection conn, String monto, String descripcion, String desembolsoid, String cuentaid) throws SQLException {
		String SQL = " INSERT INTO DW_AL_DOCUMENTO (DWDIDESEMBOLSOID, ";
		SQL = SQL + "                             DWACCUENTAID, ";
		SQL = SQL + "                             DWDTTIPODOCUMENTOID, ";
		SQL = SQL + "                             DWDCDOCUMENTOID, ";
		SQL = SQL + "                             DWDCFECHACREADO, ";
		SQL = SQL + "                             DWDCMONTO, ";
		SQL = SQL + "                             DWDCDESCRIPCION, ";
		SQL = SQL + "                             DWUSCODIGO) ";
		SQL = SQL + "   VALUES(" + desembolsoid + ", ";
		SQL = SQL + "          " + cuentaid + ", ";
		SQL = SQL + "          2 , ";
		SQL = SQL + "          (SELECT NVL (MAX (DWDCDOCUMENTOID), 0) + 1 ";
		SQL = SQL + "             FROM DW_AL_DOCUMENTO ";
		SQL = SQL + "            WHERE     DWDIDESEMBOLSOID = " + desembolsoid + " ";
		SQL = SQL + "                  AND DWACCUENTAID = " + cuentaid + " ";
		SQL = SQL + "                  AND DWDTTIPODOCUMENTOID = 2), ";
		SQL = SQL + "          SYSDATE, ";
		SQL = SQL + "   	-" + monto + ", ";
		SQL = SQL + "          UPPER('" + descripcion + "'), ";
		SQL = SQL + "          0 )";
		return executeUpdate(conn, SQL, "creaNotaDebito: " + SQL);
	}
	
}
