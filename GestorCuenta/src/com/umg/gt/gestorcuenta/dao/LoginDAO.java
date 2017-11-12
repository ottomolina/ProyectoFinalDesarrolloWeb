package com.umg.gt.gestorcuenta.dao;

import java.sql.Connection;
import java.util.List;

//import com.umg.gt.gestioncuenta.orm.UsuarioORM;

public class LoginDAO extends GeneralDAO {
	
	public String obtieneSeqUsuario(Connection conn, String p_bodega) throws Exception {
		String SQL = "select nvl(max(USU_CODIGO), 0) + 1 ";
		SQL = SQL + "   from gb_usuario ";
		SQL = SQL + "  where bod_codigo = " + p_bodega + " ";
		
		return executeQuery(conn, "obtieneSecuencia() " + SQL, SQL);
	}
	
//	public int insertaUsuario(Connection conn, UsuarioORM obj, String p_usuario) throws Exception {
//		String SQL = "insert into gb_usuario("
//				+ "bod_codigo,"
//				+ "usu_codigo,"
//				+ "usu_id_usuario,"
//				+ "usu_nombre_usuario,"
//				+ "usu_password,"
//				+ "usu_fch_creado,"
//				+ "usu_user_creado,"
//				+ "usu_fch_modificacion,"
//				+ "usu_user_modificacion,"
//				+ "usu_tipo)"
//				+ "values(" + obj.getBod_codigo() + ","
//						+ obj.getUsu_codigo() + ",'"
//						+ obj.getUsu_id_usuario() + "','"
//						+ obj.getUsu_nombre_usuario() + "'," +
//						"(select md5hash('" + obj.getUsu_password() + "') from dual),"
//						+ "sysdate,'"
//						+ p_usuario + "',"
//						+ "null,"
//						+ "null,'"
//						+ obj.getUsu_tipo() + "')";
//		
//		return executeUpdate(conn, "insertaUsuario() " + SQL, SQL);
//	}
	
//	public List<UsuarioORM> obtieneListaUsuario(Connection conn, UsuarioORM filtro) throws Exception {
//		String SQL = "select bod_codigo, "
//				+ "usu_codigo, "
//				+ "usu_id_usuario, "
//				+ "usu_nombre_usuario, "
//				+ "to_char(usu_fch_creado, 'dd/MM/yyyy') as usu_fch_creado, "
//				+ "usu_user_creado, "
//				+ "usu_fch_modificacion, "
//				+ "usu_user_modificacion, "
//				+ "usu_tipo "
//				+ "from gb_usuario "
//				+ "where 1 = 1 ";
//		
//		if(filtro != null){
//			if(filtro.getBod_codigo() != null){
//				SQL = SQL + " and bod_codigo = " + filtro.getBod_codigo() + " ";
//			}
//			if(filtro.getUsu_id_usuario() != null){
//				SQL = SQL + " and usu_id_usuario = '" + filtro.getUsu_id_usuario() + "' ";
//			}
//			if(filtro.getUsu_nombre_usuario() != null){
//				SQL = SQL + " and usu_nombre_usuario = '" + filtro.getUsu_nombre_usuario() + "' ";
//			}
//			if(filtro.getUsu_tipo() != null){
//				SQL = SQL + " and usu_tipo = '" + filtro.getUsu_tipo() + "' ";
//			}
//		}
//		
//		return selectStatement(conn, SQL, UsuarioORM.class, "obtieneListaUsuario() " + SQL);
//	}
	
//	public int actualizaUsuario(Connection conn, UsuarioORM obj, String p_usuario) throws Exception {
//		String SQL = " update gb_usuario "
//				+ " set usu_nombre_usuario = '" + obj.getUsu_nombre_usuario() + "'," 
////				+ " usu_password = '" + obj.getUsu_password() + "', "
//				+ " usu_fch_modificacion = sysdate, "
//				+ " usu_user_modificacion = '" + p_usuario + "', "
//				+ " usu_tipo = '" + obj.getUsu_tipo() + "' "
//				+ "where bod_codigo = " + obj.getBod_codigo() 
//				+ " and usu_codigo = '" + obj.getUsu_codigo() + "' ";
//								
//		return executeUpdate(conn, "actualizaUsuario() " + SQL, SQL);
//	}
	
	public int eliminaUsuario(Connection conn, String p_bodCodigo, String p_usuCodigo) throws Exception {
		String SQL = "delete from ga_usuario "
				+ " where bod_codigo = " + p_bodCodigo + " and usu_codigo = '" + p_usuCodigo + "' ";
		
		return executeUpdate(conn, "eliminaUsuario() " + SQL, SQL); 
	}
	
//	public int actualizaPassword(Connection conn, UsuarioORM obj, String p_usuario) throws Exception {
//		String SQL = " update gb_usuario set " 
//				+ " usu_password = (select md5hash('" + obj.getUsu_password() + "') from dual), "
//				+ " usu_fch_modificacion = sysdate, "
//				+ " usu_user_modificacion = '" + p_usuario + "' "
//				+ "where bod_codigo = " + obj.getBod_codigo() 
//				+ " and usu_codigo = '" + obj.getUsu_codigo() + "' ";
//								
//		return executeUpdate(conn, "actualizaUsuario() " + SQL, SQL);
//	}
	
	public String validaAutenticacion(Connection conn, String user, String pass){
		return "ok";
	}
	
}
