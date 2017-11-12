package com.umg.cuentaws.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.cuentaws.orm.DatosCuenta;
import com.umg.cuentaws.orm.Documento;
import com.umg.cuentaws.orm.DocumentoCredito;
import com.umg.cuentaws.orm.FiltroCuenta;
import com.umg.cuentaws.orm.Nota;

public class FinanzasDAO extends GeneralDao {
	
	public List<DatosCuenta> obtieneDatosCuenta(Connection conn, FiltroCuenta filtro) throws SQLException {
		String SQL = " SELECT CODIGOCLIENTE, ";
		SQL = SQL + "       NOMBRES, ";
		SQL = SQL + "       APELLIDOS, ";
		SQL = SQL + "       DIRECCION, ";
		SQL = SQL + "       TELEFONO, ";
		SQL = SQL + "       NUMCUENTA, ";
		SQL = SQL + "       CATEGORIACUENTA, ";
		SQL = SQL + "       TIPOCUENTA, ";
		SQL = SQL + "       FECHACREADO ";
		SQL = SQL + " FROM (";
		
		SQL = SQL + " SELECT CODIGO AS CODIGOCLIENTE, ";
		SQL = SQL + "          DWPEPRIMERNOMBRE ";
		SQL = SQL + "       || ' ' ";
		SQL = SQL + "       || NVL (DWPESEGUNDONOMBRE, '') ";
		SQL = SQL + "       || ' ' ";
		SQL = SQL + "       || NVL (DWPETERCERNOMBRE, '') ";
		SQL = SQL + "          AS NOMBRES, ";
		SQL = SQL + "          DWPEPRIMERAPELLIDO ";
		SQL = SQL + "       || ' ' ";
		SQL = SQL + "       || NVL (DWPESEGUNDOAPELLIDO, '') ";
		SQL = SQL + "       || ' ' ";
		SQL = SQL + "       || NVL (DWPEAPELLIDOCASADA, '') ";
		SQL = SQL + "          AS APELLIDOS, ";
		SQL = SQL + "       DIRECCION, ";
		SQL = SQL + "       DWPETELEFONO AS TELEFONO, ";
		SQL = SQL + "       DWACNUMCUENTA AS NUMCUENTA, ";
		SQL = SQL + "       DWCADESCRIPCION AS CATEGORIACUENTA, ";
		SQL = SQL + "       DWATDESCRIPCION AS TIPOCUENTA, ";
		SQL = SQL + "       TO_CHAR (DWACFECHACREADO, 'dd/MM/yyyy') AS FECHACREADO ";

		SQL = SQL + "  FROM DW_AL_CUENTA CTA  ";
		SQL = SQL + "  JOIN DW_CAT_CATEGORIA CAT  ";
		SQL = SQL + "    ON CTA.DWCACATEGORIAID = CAT.DWCACATEGORIAID  ";
		SQL = SQL + "  JOIN DW_CAT_TIPO_CUENTA TIP ";
		SQL = SQL + "    ON CTA.DWATTIPOCUENTAID = TIP.DWATTIPOCUENTAID ";
		SQL = SQL + "  JOIN DW_PE_PERSONA PE  ";
		SQL = SQL + "    ON PE.CODIGO = CTA.DWACCLIENTEID AND PE.DWPEPERSONAID >= 10000 )";

		SQL = SQL + " WHERE 1 = 1 ";
		
		if(filtro != null){
			if(filtro.getNombres() != null && !"".equals(filtro.getNombres())){
				SQL = SQL + " AND UPPER(NOMBRES) LIKE UPPER('%" + filtro.getNombres() + "%') ";
			}
			if(filtro.getApellidos() != null && !"".equals(filtro.getApellidos())){
				SQL = SQL + " AND UPPER(APELLIDOS) LIKE UPPER('%" + filtro.getApellidos() + "%') ";
			}
			if(filtro.getTelefono() != null && !"".equals(filtro.getTelefono())){
				SQL = SQL + " AND TELEFONO LIKE '%" + filtro.getTelefono() + "%'";
			}
			if(filtro.getNumCuenta() != null && !"".equals(filtro.getNumCuenta())){
				SQL = SQL + " AND NUMCUENTA = '" + filtro.getNumCuenta() + "' ";
			}
			if(filtro.getCategoriaCuenta() != null && !"".equals(filtro.getCategoriaCuenta())){
				SQL = SQL + " AND CATEGORIACUENTA = '" + filtro.getCategoriaCuenta() + "' ";
			}
			if(filtro.getTipoCuenta() != null && !"".equals(filtro.getTipoCuenta())){
				SQL = SQL + " AND TIPOCUENTA = '" + filtro.getTipoCuenta() + "' ";
			}
		}
		
		return selectStatement(conn, SQL, DatosCuenta.class, "obtieneDatosCuenta: ");
	}
	
	public List<Nota> obtieneDocumentos(Connection conn, String tipo_documento, String nit) throws SQLException {
		String SQL = " SELECT (SELECT DWPADESCRIPCION FROM DW_PAR_PARAMETROS WHERE DWPANOMBREPARAMETRO = 'CLIENTE') AS NOMBRE_CUENTA, ";
		SQL = SQL + "       AC.DWACNUMCUENTA AS NUMERO_CUENTA, ";
		SQL = SQL + "       TD.DWDTDESCRIPCION AS TIPO_DOCUMENTO, ";
		SQL = SQL + "       AD.DWDCDESCRIPCION AS DESCRIPCION, ";
		SQL = SQL + "       AD.DWDCMONTO AS MONTO, ";
		SQL = SQL + "       TO_CHAR(AD.DWDCFECHACREADO, 'dd/MM/yyyy') AS FECHA ";
		SQL = SQL + "  FROM DW_AL_DOCUMENTO AD ";
		SQL = SQL + "  JOIN DW_CAT_TIPO_DOCUMENTO TD ";
		SQL = SQL + "    ON AD.DWDTTIPODOCUMENTOID = TD.DWDTTIPODOCUMENTOID ";
		SQL = SQL + "  JOIN DW_AL_DESEMBOLSO DES ";
		SQL = SQL + "    ON DES.DWDIDESEMBOLSOID = AD.DWDIDESEMBOLSOID ";
		SQL = SQL + "   AND DES.DWACCUENTAID = AD.DWACCUENTAID ";
		SQL = SQL + "  JOIN DW_AL_CUENTA AC ";
		SQL = SQL + "    ON DES.DWACCUENTAID = AC.DWACCUENTAID ";
		SQL = SQL + " WHERE AD.DWDTTIPODOCUMENTOID = " + tipo_documento + " ";
		if(nit != null && !"".equals(nit)){
			SQL = SQL + " AND DWACCLIENTEID = " + nit + " ";
		}
		return selectStatement(conn, SQL, Nota.class, "obtieneDocumentos");
	}
	
	public String obtieneSaldo(Connection conn, String nit, String facturaid) throws SQLException {
		String SQL = " SELECT SUM(AD.DWDCMONTO) AS MONTO_DEUDA ";
		SQL = SQL + "  FROM DW_AL_DOCUMENTO AD ";
		SQL = SQL + "  JOIN DW_AL_DESEMBOLSO DES ";
		SQL = SQL + "    ON DES.DWDIDESEMBOLSOID = AD.DWDIDESEMBOLSOID ";
		SQL = SQL + "   AND DES.DWACCUENTAID = AD.DWACCUENTAID ";
		SQL = SQL + "  JOIN DW_AL_CUENTA AC ";
		SQL = SQL + "    ON DES.DWACCUENTAID = AC.DWACCUENTAID ";
		SQL = SQL + " WHERE AD.DWDTTIPODOCUMENTOID IN (1, 2) ";
		SQL = SQL + "   AND DWACCLIENTEID = " + nit + " ";
		if(facturaid != null){
			SQL = SQL + " AND DES.DWDIIFACTURAID = " + facturaid + " ";
		}
		return executeQuery(conn, SQL, "obtieneSaldo: ");
	}
	
	public String verificaClientePoseeCuenta(Connection conn, String nit) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_AL_CUENTA ";
		SQL = SQL + " WHERE DWACCLIENTEID = " + nit + " ";
		return executeQuery(conn, SQL, "verificaClientePoseeCuenta: ");
	}
	
	public String verificaFacturaCliente(Connection conn, String nit, String facturaid) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_AL_DESEMBOLSO DES ";
		SQL = SQL + "  JOIN DW_AL_CUENTA CTA ";
		SQL = SQL + "    ON CTA.DWACCUENTAID = DES.DWACCUENTAID ";
		SQL = SQL + " WHERE CTA.DWACCLIENTEID = " + nit + " ";
		SQL = SQL + "   AND DES.DWDIIFACTURAID = " + facturaid + " ";
		return executeQuery(conn, SQL, "verficaFacturaCliente: ");
	}
	
	public String verificaCreditoDisponibleCuenta(Connection conn, Documento documento, String saldoCuenta) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_AL_CUENTA CTA ";
		SQL = SQL + "  JOIN DW_CAT_CATEGORIA CAT ";
		SQL = SQL + "    ON CTA.DWCACATEGORIAID = CAT.DWCACATEGORIAID ";
		SQL = SQL + " WHERE CTA.DWACCLIENTEID = '" + documento.getNit() + "' ";
		SQL = SQL + "   AND CAT.DWCACREDITOMAXIMO >= (" + saldoCuenta + " + " + documento.getMonto() + ") ";
		return executeQuery(conn, SQL, "verificaCreditoDisponibleCuenta: ");
	}
	
	public int creaNota(Connection conn, Documento nota, String tipo_documento) throws SQLException {
		String SQL = " INSERT INTO DW_AL_DOCUMENTO (DWDIDESEMBOLSOID, ";
		SQL = SQL + "                             DWACCUENTAID, ";
		SQL = SQL + "                             DWDTTIPODOCUMENTOID, ";
		SQL = SQL + "                             DWDCDOCUMENTOID, ";
		SQL = SQL + "                             DWDCFECHACREADO, ";
		SQL = SQL + "                             DWDCMONTO, ";
		SQL = SQL + "                             DWDCDESCRIPCION, ";
		SQL = SQL + "                             DWUSCODIGO) ";
		SQL = SQL + "   SELECT DES.DWDIDESEMBOLSOID, ";
		SQL = SQL + "          DES.DWACCUENTAID, ";
		SQL = SQL + "          " + tipo_documento + " AS TIPODOCUMENTOID, ";
		SQL = SQL + "          (SELECT NVL (MAX (DWDCDOCUMENTOID), 0) + 1 ";
		SQL = SQL + "             FROM DW_AL_DOCUMENTO ";
		SQL = SQL + "            WHERE     DWDIDESEMBOLSOID = DES.DWDIDESEMBOLSOID ";
		SQL = SQL + "                  AND DWACCUENTAID = DES.DWACCUENTAID ";
		SQL = SQL + "                  AND DWDTTIPODOCUMENTOID = 2) AS DOCUMENTOID, ";
		SQL = SQL + "          SYSDATE, ";
		if("2".equals(tipo_documento)){
			SQL = SQL + "   -" + nota.getMonto() + ", ";
		}else{
			SQL = SQL + "    " + nota.getMonto() + ", ";
		}
		SQL = SQL + "          UPPER('" + nota.getDescripcion() + "'), ";
		SQL = SQL + "          0 ";
		SQL = SQL + "     FROM    DW_AL_DESEMBOLSO DES ";
		SQL = SQL + "          JOIN ";
		SQL = SQL + "             DW_AL_CUENTA CTA ";
		SQL = SQL + "          ON DES.DWACCUENTAID = CTA.DWACCUENTAID ";
		SQL = SQL + "    WHERE DES.DWDIIFACTURAID = " + nota.getNum_facura() + " AND CTA.DWACCLIENTEID = '" + nota.getNit() + "' ";
		return executeUpdate(conn, SQL, "creaNotaDebito: ");
	}
	
	public int creaDesembolso(Connection conn, DocumentoCredito documento) throws SQLException {
		String SQL = " INSERT INTO DW_AL_DESEMBOLSO (DWDIDESEMBOLSOID, ";
		SQL = SQL + "                              DWACCUENTAID, ";
		SQL = SQL + "                              DWDIIFACTURAID, ";
		SQL = SQL + "                              DWDIDESCRIPCION, ";
		SQL = SQL + "                              DWDINUMCUOTA, ";
		SQL = SQL + "                              DWDIFECHACREADO, ";
		SQL = SQL + "                              DWUSCODIGO) ";
		SQL = SQL + "   SELECT (SELECT NVL (MAX (DWDIDESEMBOLSOID), 0) + 1 ";
		SQL = SQL + "             FROM DW_AL_DESEMBOLSO ";
		SQL = SQL + "            WHERE DWACCUENTAID = AC.DWACCUENTAID) ";
		SQL = SQL + "             AS DESEMBOLSOID, ";
		SQL = SQL + "          AC.DWACCUENTAID, ";
		SQL = SQL + "          " + documento.getNum_facura() + ", ";
		SQL = SQL + "    UPPER('" + documento.getDescripcion() + "'), ";
		SQL = SQL + "          " + documento.getNum_cuota() + ", ";
		SQL = SQL + "          SYSDATE, ";
		SQL = SQL + "          0 ";
		SQL = SQL + "     FROM DW_AL_CUENTA AC ";
		SQL = SQL + "    WHERE DWACCLIENTEID = " + documento.getNit() + " ";
		return executeUpdate(conn, SQL, "creaDesembolso: ");
	}
	
	
	
}
