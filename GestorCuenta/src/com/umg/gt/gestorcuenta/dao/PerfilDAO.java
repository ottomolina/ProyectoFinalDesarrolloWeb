package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.umg.gt.gestorcuenta.orm.PerfilORM;
import com.umg.gt.gestorcuenta.orm.PermisoORM;

public class PerfilDAO extends GeneralDAO {
	
	public List<PerfilORM> obtieneListaPerfil(Connection conn, PerfilORM obj) throws SQLException {
		String SQL = "select dwpfperfilid, dwpfnombre, to_char(dwpffechacreado, 'dd/MM/yyyy') as dwpffechacreado ";
		SQL = SQL + "   from dw_pf_perfil ";
		SQL = SQL + "  where 1 = 1 ";
		
		if(obj != null){
			if(obj.getDwpfperfilid() != null){
				SQL = SQL + " and dwpfperfilid = " + obj.getDwpfperfilid();
			}
			if(obj.getDwpfnombre() != null){
				SQL = SQL + " and upper(dwpfnombre) like upper('%" + obj.getDwpfnombre() + "%')";
			}
		}
		SQL = SQL + " order by dwpfperfilid asc ";
		
		return selectStatement(conn, SQL, PerfilORM.class, "obtieneListaPerfil: " + SQL);
	}
	
	public String obtieneSecuencia(Connection conn) throws SQLException {
		String SQL = " select nvl(max(dwpfperfilid), 0) + 1 from dw_pf_perfil ";
		String ret = executeQuery(conn, SQL, "obtieneSecuencia: " + SQL);
		return ret;
	}
	
	public int insertaPerfil(Connection conn, PerfilORM obj) throws SQLException {
		String SQL = " insert into dw_pf_perfil(dwpfperfilid, ";
		SQL = SQL + "                         dwpfnombre, ";
		SQL = SQL + "                         dwpffechacreado) ";
		SQL = SQL + "  values(" + obj.getDwpfperfilid() + ", ";
		SQL = SQL + "        '" + obj.getDwpfnombre() + "', ";
		SQL = SQL + "         sysdate) ";
		return executeUpdate(conn, SQL, "insertaPerfil: " + SQL);
	}
	
	public int actualizaPerfil(Connection conn, PerfilORM obj) throws SQLException {
		String SQL = " update dw_pf_perfil ";
		SQL = SQL + "	  set dwpfnombre = '" + obj.getDwpfnombre() + "' ";
		SQL = SQL + "	where dwpfperfilid = " + obj.getDwpfperfilid() + " ";
		int i = executeUpdate(conn, SQL, "actualizaPerfil: " + SQL);
		return i;			
	}
	
	public int eliminaPerfil(Connection conn, PerfilORM obj) throws SQLException {
		String SQL = " delete from dw_pf_perfil ";
		SQL = SQL + "	where dwpfperfilid = " + obj.getDwpfperfilid() + " ";
		int i = executeUpdate(conn, SQL, "eliminaPerfil: " + SQL);
		return i;			
	}
	
	
	public List<PermisoORM> obtienePermisos(Connection conn, String perfilid, String tipo) throws SQLException {
		String SQL = " select * from dw_pm_permiso ";
		if(tipo.equals("nuevo")){
			SQL = SQL + " where dwpmpermisoid not in ( ";
		}else if(tipo.equals("asig")){
			SQL = SQL + " where dwpmpermisoid in ( ";
		}
		SQL = SQL + "        select dwpmpermisoid  ";
		SQL = SQL + "          from dw_permiso_asoc  ";
		SQL = SQL + "         where dwpfperfilid = " + perfilid + ") ";
		return selectStatement(conn, SQL, PermisoORM.class, "obtienePermisoNew: " + SQL);
	}
	
	
	public List<PermisoORM> obtienePermisos(Connection conn, PermisoORM obj) throws SQLException {
		String SQL = "select perm.dwpmpermisoid, ";
		SQL = SQL + "        asoc.dwpfperfilid, ";
		SQL = SQL + "        perm.dwpmnombre, ";
		SQL = SQL + "        perm.dwpmnombretecnico  ";
		SQL = SQL + "   from dw_pm_permiso perm ";
		SQL = SQL + "   join dw_permiso_asoc asoc ";
		SQL = SQL + "     on perm.dwpmpermisoid = asoc.dwpmpermisoid ";
		SQL = SQL + "  where 1 = 1 ";
		
		if(obj != null){
			if(obj.getDwpfperfilid() != null){
				SQL = SQL + " and asoc.dwpfperfilid = " + obj.getDwpfperfilid() + " ";
			}
			if(obj.getDwpmnombre() != null){
				SQL = SQL + " and upper(perm.dwpmnombre) like upper('%" + obj.getDwpmnombre() + "%') ";
			}
			if(obj.getDwpmnombretecnico() != null){
				SQL = SQL + " and upper(perm.dwpmnombretecnico) like upper('%" + obj.getDwpmnombretecnico() + "%')";
			}
		}
		SQL = SQL + " order by asoc.dwpmpermisoid asc ";
		return selectStatement(conn, SQL, PermisoORM.class, "obtienePermisos: " + SQL);
	}
	
	public int insertaAsocPermiso(Connection conn, String perfilid, String permisoid) throws SQLException {
		String SQL = " insert into dw_permiso_asoc(dwpmpermisoid, dwpfperfilid) ";
		SQL = SQL + "values(" + permisoid + ", " + perfilid + ") ";
		return executeUpdate(conn, SQL, "insertaAsocPermiso: " + SQL);
	}
	
	public int eliminaAsocPermiso(Connection conn, String perfilid, String permisoid) throws SQLException {
		String SQL = "delete from dw_permiso_asoc ";
		SQL = SQL + "  where dwpfperfilid = " + perfilid + " ";
		if(permisoid != null){
			SQL = SQL + " and dwpmpermisoid = " + permisoid + " ";
		}
		return executeUpdate(conn, SQL, "eliminaAsocPermiso: " + SQL);
	}
	
	public int insertaPermiso(Connection conn, PermisoORM permiso) throws SQLException {
		String SQL = " insert into dw_pm_permiso(dwpmpermisoid, dwpmnombre, dwpmnombretecnico) ";
		SQL = SQL + "values(dw_pm_permiso_sq.nextval, "
				+ "'" + permiso.getDwpmnombre() + "', "
				+ "'" + permiso.getDwpmnombretecnico() + "') ";
		return executeUpdate(conn, SQL, "insertaPermiso: " + SQL);
	}
	
	public int actualizaPermiso(Connection conn, PermisoORM permiso) throws SQLException {
		String SQL = " update dw_pm_permiso set "
				+ "dwpmnombre = '" + permiso.getDwpmnombre() + "', "
				+ "dwpmnombretecnico = '" + permiso.getDwpmnombretecnico() + "' "
				+ "where dwpmpermisoid = " + permiso.getDwpmpermisoid() + " ";
		return executeUpdate(conn, SQL, "actualizaPermiso: " + SQL);
	}
	
	public int eliminaPermiso(Connection conn, String permisoid) throws SQLException {
		String SQL = "delete from dw_pm_permiso ";
		SQL = SQL + "  where dwpmpermisoid = " + permisoid + " ";
		return executeUpdate(conn, SQL, "eliminaPermiso: " + SQL);
	}
	
	public String conteoPermiso(Connection conn, String perfilid) throws SQLException {
		String SQL = "select count(1) from dw_permiso_asoc ";
		SQL = SQL + "  where dwpfperfilid = " + perfilid + " ";
		return executeQuery(conn, SQL, "conteoPermiso: " + SQL);
	}
	
}
