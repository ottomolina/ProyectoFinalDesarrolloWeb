package com.umg.gt.gestorcuenta.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Messagebox;

import com.umg.gt.gestorcuenta.orm.ViewUser;
import com.umg.gt.gestorcuenta.util.Conf;

public class ControladorBase extends GenericForwardComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ControladorBase.class);
	
	private final String CONN = "LOCAL";
	
	public void doAfterCompose(Component comp){
		try{
			super.doAfterCompose(comp);
			cargaPropiedades(Conf.PATH_LOG);
		}catch(IOException e){
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public ViewUser getUsuario(){
		ViewUser user = null;
		if(CONN.equals("LOCAL")){
			user = new ViewUser();
			user.setDwususuarioid("admin");
			user.setDwpfperfilid("1");
			user.setDwuscodigo("0");
			desktop.setAttribute("USER", user);
		}else{
			if(desktop.getAttribute("USER") != null){
				user = (ViewUser)desktop.getAttribute("USER");
			}
		}
		return user;
	}
	
	protected void validaLogin(){
		if(getUsuario() == null){
			Executions.sendRedirect("index.zul");
		}
	}
	
	public Connection obtieneConexion() throws SQLException {
		Connection conexion = null;
		try {
			conexion = obtenerDataSource(Conf.JNDI_CUENTA).getConnection();
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw new SQLException("Surgio un inconveniente con la conexión hacia base de datos. "
					+ "Recurso: " + Conf.JNDI_CUENTA);
		}
		return conexion;
	}
	
	private DataSource obtenerDataSource(String strJndi) throws NamingException {
		DataSource ret;
		if (desktop.getSession().getAttribute("dtsjndi"+strJndi) == null) {
			Context contexto= new InitialContext();
			ret = (DataSource)contexto.lookup(strJndi);
			desktop.getSession().setAttribute("dtsjndi"+strJndi, ret);
		} else {
			ret = (DataSource) desktop.getSession().getAttribute("dtsjndi"+strJndi);
		}
		return ret;
	}
	
	public void cerrarConexion(Connection conn){
		if(conn != null){
			DbUtils.closeQuietly(conn);
		}
	}
	
	public void mostrarMensaje(String p_mensaje){
		Messagebox.show(p_mensaje, "Información", Messagebox.OK, Messagebox.INFORMATION);
//		Clients.showNotification(p_mensaje, Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
	}
	
	public void mensajeInconveniente(){
		Messagebox.show("Ha ocurrido un inconveniente, si el problema persiste consulte a su administrador.", 
				"Información", Messagebox.OK, Messagebox.INFORMATION);
//		String p_mensaje = "Ha ocurrido un inconveniente, si el problema persiste consulte a su administrador.";
//		Clients.showNotification(p_mensaje, Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
	}
	
	public void mostrarConfirmacion(String mensaje, 
			org.zkoss.zk.ui.event.EventListener<org.zkoss.zk.ui.event.Event> evento){
		Messagebox.show(mensaje, "Confirmación", Messagebox.YES + Messagebox.NO, Messagebox.QUESTION, evento);
	}
	
	public void cargaPropiedades(String path) throws IOException {
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/com/umg/gt/gestorcuenta/log4j.properties");
		prop.load(in);
		prop.put("log4j.appender.FILE.File", path);
		PropertyConfigurator.configure(prop);
	}

}
