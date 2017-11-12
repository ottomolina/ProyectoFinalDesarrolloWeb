package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.CategoriaORM;
import com.umg.gt.gestorcuenta.orm.TipoCuentaORM;

public class ManDAO extends GeneralDAO {
	
	public String verificaTipoCuenta(Connection conn, String tipoCuentaId) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_CAT_TIPO_CUENTA ";
		SQL = SQL + " WHERE DWATTIPOCUENTAID = " + tipoCuentaId + " ";
		SQL = SQL + "   AND DWATTIPOCUENTAID IN (SELECT DWATTIPOCUENTAID ";
		SQL = SQL + "                              FROM DW_AL_CUENTA) ";
		return executeQuery(conn, SQL, "verificaTipoCuenta: " + SQL);
	}
	
	public String verificaCategoriaCuenta(Connection conn, String categoriaId) throws SQLException {
		String SQL = " SELECT COUNT(1) ";
		SQL = SQL + "  FROM DW_CAT_CATEGORIA ";
		SQL = SQL + " WHERE DWCACATEGORIAID = " + categoriaId + " ";
		SQL = SQL + "   AND DWCACATEGORIAID IN (SELECT DWCACATEGORIAID ";
		SQL = SQL + "                              FROM DW_AL_CUENTA) ";
		return executeQuery(conn, SQL, "verificaTipoCuenta: " + SQL);
	}
	
	public int insertaTipoCuenta(Connection conn, TipoCuentaORM obj) throws SQLException {
		String SQL = " INSERT INTO DW_CAT_TIPO_CUENTA( ";
		SQL = SQL + "        DWATTIPOCUENTAID, ";
		SQL = SQL + "        DWATDESCRIPCION, ";
		SQL = SQL + "        DWATINTERES, ";
		SQL = SQL + "        DWATFECHACORTE, ";
		SQL = SQL + "        DWATFECHALIMITE ) ";
		SQL = SQL + " VALUES(DW_TIPO_CUENTA_SQ.NEXTVAL, ";
		SQL = SQL + "		'" + obj.getDwatdescripcion() + "',";
		SQL = SQL + "		 " + obj.getDwatinteres() + ",";
		SQL = SQL + "TO_DATE('" + obj.getDwatfechacorte() + "/01/2150', 'dd/MM/yyyy'),";
		SQL = SQL + "		'" + obj.getDwatfechalimite() + "')";
		return executeUpdate(conn, SQL, "insertaTipoCuenta: " + SQL);
	}
	
	public int insertaCategoriaCuenta(Connection conn, CategoriaORM obj) throws SQLException {
		String SQL = " INSERT INTO DW_CAT_CATEGORIA( ";
		SQL = SQL + "        DWCACATEGORIAID, ";
		SQL = SQL + "        DWCADESCRIPCION, ";
		SQL = SQL + "        DWCACREDITOMAXIMO, ";
		SQL = SQL + "        DWCACREDITOMINIMO) ";
		SQL = SQL + " VALUES(DW_CATEGORIA_SQ.NEXTVAL, ";
		SQL = SQL + "	'" + obj.getDwcadescripcion() + "',";
		SQL = SQL + "	" + obj.getDwcacreditomaximo() + ",";
		SQL = SQL + "	" + obj.getDwcacreditominimo() + ") ";
		return executeUpdate(conn, SQL, "insertaCategoriaCuenta: " + SQL);
	}
	
	public List<TipoCuentaORM> obtieneTipoCuenta(Connection conn) throws SQLException {
		String SQL = " SELECT DWATTIPOCUENTAID, ";
		SQL = SQL + "        DWATDESCRIPCION, ";
		SQL = SQL + "        DWATINTERES, ";
		SQL = SQL + "        EXTRACT(DAY FROM DWATFECHACORTE) AS DWATFECHACORTE, ";
		SQL = SQL + "        DWATFECHALIMITE ";
		SQL = SQL + "  FROM DW_CAT_TIPO_CUENTA ";
		return selectStatement(conn, SQL, TipoCuentaORM.class, "obtieneTipoCuenta: " + SQL);
	}
	
	public List<CategoriaORM> obtieneCategoria(Connection conn) throws SQLException {
		String SQL = "SELECT DWCACATEGORIAID, ";
		SQL = SQL + "        DWCADESCRIPCION, ";
		SQL = SQL + "        DWCACREDITOMAXIMO, ";
		SQL = SQL + "        DWCACREDITOMINIMO ";
		SQL = SQL + "   FROM DW_CAT_CATEGORIA ";

		return selectStatement(conn, SQL, CategoriaORM.class, "obtieneCategoria: " + SQL);
	}
	
	public int actualizaTipoCuenta(Connection conn, TipoCuentaORM obj) throws SQLException {
		String SQL = "UPDATE DW_CAT_TIPO_CUENTA SET ";
		SQL = SQL + "        DWATDESCRIPCION = '" + obj.getDwatdescripcion() + "', ";
		SQL = SQL + "        DWATINTERES = " + obj.getDwatinteres() + ", ";
		SQL = SQL + "        DWATFECHACORTE = TO_DATE('" + obj.getDwatfechacorte() + "/01/2150', 'dd/MM/yyyy'),";
		SQL = SQL + "        DWATFECHALIMITE = '" + obj.getDwatfechalimite() + "' ";
		SQL = SQL + "  WHERE DWATTIPOCUENTAID = " + obj.getDwattipocuentaid() + " ";
		return executeUpdate(conn, SQL, "actualizaTipoCuenta: " + SQL);
	}
	
	public int actualizaCategoriaCuenta(Connection conn, CategoriaORM obj) throws SQLException {
		String SQL = "UPDATE DW_CAT_CATEGORIA SET ";
		SQL = SQL + "        DWCADESCRIPCION = '" + obj.getDwcadescripcion() + "', ";
		SQL = SQL + "        DWCACREDITOMAXIMO = " + obj.getDwcacreditomaximo() + ", ";
		SQL = SQL + "        DWCACREDITOMINIMO = " + obj.getDwcacreditominimo() + " ";
		SQL = SQL + "  WHERE DWCACATEGORIAID = " + obj.getDwcacategoriaid() + " ";
		return executeUpdate(conn, SQL, "actualizaCategoriaCuenta: " + SQL);
	}
	
	public int eliminaTipoCuenta(Connection conn, String tipoCuentaId) throws SQLException {
		String SQL = "DELETE FROM DW_CAT_TIPO_CUENTA ";
		SQL = SQL + "  WHERE DWATTIPOCUENTAID = " + tipoCuentaId + " ";
		return executeUpdate(conn, SQL, "eliminaTipoCuenta: " + SQL);
	}
	
	public int eliminaCategoria(Connection conn, String categoriaId) throws SQLException {
		String SQL = "DELETE FROM DW_CAT_CATEGORIA ";
		SQL = SQL + "  WHERE DWCACATEGORIAID = " + categoriaId + " ";
		return executeUpdate(conn, SQL, "eliminaCategoria: " + SQL);
	}
	
}
