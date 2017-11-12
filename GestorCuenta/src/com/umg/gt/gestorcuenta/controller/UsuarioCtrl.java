package com.umg.gt.gestorcuenta.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import com.umg.gt.gestorcuenta.dao.PerfilDAO;
import com.umg.gt.gestorcuenta.dao.UsuarioDAO;
import com.umg.gt.gestorcuenta.orm.PerfilORM;
import com.umg.gt.gestorcuenta.orm.ViewUser;
import com.umg.gt.gestorcuenta.util.Conf;

public class UsuarioCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(UsuarioCtrl.class);
	private UsuarioDAO dao;
	private ViewUser filtro;
	
	Window wdwAdminUsuario;
	Window wdwUsuario;
	Window wdwCambioPass;
	
	Listbox lbxUsuario;
	Listbox lbxSearch;
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		dao = new UsuarioDAO();
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxUsuario(conn, filtro);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void cargaLbxUsuario(Connection conn, ViewUser filtro) throws SQLException {
		lbxUsuario.getItems().clear();
		
		List<ViewUser> listaUsuario = dao.obtieneListaUsuario(conn, filtro);
		
		Iterator<ViewUser> iList = listaUsuario.iterator();
		Listitem item;
		Listcell cell;
		ViewUser user;
		while(iList.hasNext()){
			user = iList.next();
			
			item = new Listitem();
			cell = new Listcell();
			cell.setParent(item);
			
			cell = new Listcell(user.getNombres());
			cell.setParent(item);
			cell = new Listcell(user.getApellidos());
			cell.setParent(item);
			cell = new Listcell(user.getDwususuarioid());
			cell.setParent(item);
			cell = new Listcell(user.getPerfil());
			cell.setParent(item);
			cell = new Listcell(user.getDwpecorreo());
			cell.setParent(item);
			cell = new Listcell(user.getDwpetelefono());
			cell.setParent(item);
			cell = new Listcell(user.getDwpefechacreado());
			cell.setParent(item);
			
			item.setValue(user);
			item.setContext("popMenu");
			item.setParent(lbxUsuario);
		}
	}
	
	public void onClick$imgBuscar(){
		boolean estado = lbxSearch.isVisible();
		lbxSearch.setVisible(!estado);
	}
	
	public void onClick$btnAgregarUsuario() {
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaConfUsuario(conn, null);
			wdwUsuario.setTitle("Agregar Usuario");
			wdwUsuario.doHighlighted();
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$btnModificarUsuario(){
		if(lbxUsuario.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar un usuario.");
			return;
		}
		Connection conn = null;
		ViewUser usuario = lbxUsuario.getSelectedItem().getValue();
		try{
			conn = obtieneConexion();
			cargaConfUsuario(conn, usuario);
			wdwUsuario.setTitle("Modificar Usuario");
			wdwUsuario.doHighlighted();
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$mniChangePass(){
		if(lbxUsuario.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar un usuario");
			return;
		}
		Textbox txtPassword = (Textbox) wdwCambioPass.getFellow("txtPassword");
		Textbox txtConfirmPass = (Textbox) wdwCambioPass.getFellow("txtConfirmPass");
		Button btnGuardarPass = (Button) wdwCambioPass.getFellow("btnGuardarPass");
		btnGuardarPass.removeEventListener("onClick", onClick_btnGuardarPass);
		btnGuardarPass.addEventListener("onClick", onClick_btnGuardarPass);
		txtPassword.setText("");
		txtConfirmPass.setText("");
		wdwCambioPass.doHighlighted();
	}
	
	public void cargaConfUsuario(Connection conn, ViewUser usuario) throws SQLException {
		Textbox txtFirstName = (Textbox)wdwUsuario.getFellow("txtFirstName");
		Textbox txtSecondName = (Textbox)wdwUsuario.getFellow("txtSecondName");
		Textbox txtLastName = (Textbox)wdwUsuario.getFellow("txtLastName");
		Textbox txtLastName1 = (Textbox)wdwUsuario.getFellow("txtLastName1");
		Decimalbox decTelefono = (Decimalbox)wdwUsuario.getFellow("decTelefono");
		Textbox txtCorreo = (Textbox)wdwUsuario.getFellow("txtCorreo");
		
		Textbox txtUsuario = (Textbox)wdwUsuario.getFellow("txtUsuario");
		Textbox txtPassword = (Textbox)wdwUsuario.getFellow("txtPassword");
		Combobox cmbPerfil = (Combobox)wdwUsuario.getFellow("cmbPerfil");
		
		Button btnGuardarUsuario = (Button)wdwUsuario.getFellow("btnGuardarUsuario");
		btnGuardarUsuario.removeEventListener("onClick", onClick_btnGuardarUsuario);
		btnGuardarUsuario.addEventListener("onClick", onClick_btnGuardarUsuario);
		
		wdwUsuario.removeAttribute(Conf.KEY_APERTURA);
		
		List<PerfilORM> perfiles = new PerfilDAO().obtieneListaPerfil(conn, null);
		Iterator<PerfilORM> iPerfil = perfiles.iterator();
		PerfilORM perfil;
		Comboitem item;
		while(iPerfil.hasNext()){
			perfil = iPerfil.next();
			item = new Comboitem();
			item.setValue(perfil);
			item.setLabel(perfil.getDwpfnombre());
			item.setParent(cmbPerfil);
		}
		
		if(usuario == null){
			txtFirstName.setText("");
			txtSecondName.setText("");
			txtLastName.setText("");
			txtLastName1.setText("");
			decTelefono.setText("");
			txtCorreo.setText("");
			txtUsuario.setText("");
			txtPassword.setText("");
			cmbPerfil.setText("");
			cmbPerfil.setSelectedItem(null);
			txtUsuario.setReadonly(false);
			txtPassword.setReadonly(false);
			wdwUsuario.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
		}else{
			String segundoNombre = "";
			if(usuario.getDwpesegundonombre() != null){
				segundoNombre = usuario.getDwpesegundonombre();
			}
			if(usuario.getDwpetercernombre() != null){
				segundoNombre = segundoNombre.concat(" ").concat(usuario.getDwpetercernombre());
			}
			String apellido2 = "";
			if(usuario.getDwpesegundoapellido() != null){
				apellido2 = usuario.getDwpesegundoapellido();
			}
			if(usuario.getDwpeapellidocasada() != null){
				apellido2 = apellido2.concat(" ").concat(usuario.getDwpeapellidocasada());
			}
			
			txtFirstName.setText(usuario.getDwpeprimernombre());
			txtSecondName.setText(segundoNombre);
			txtLastName.setText(usuario.getDwpeprimerapellido());
			txtLastName1.setText(apellido2);
			decTelefono.setText(usuario.getDwpetelefono());
			txtCorreo.setText(usuario.getDwpecorreo());
			txtUsuario.setText(usuario.getDwususuarioid());
			txtPassword.setText(usuario.getDwuspassword());
			for(int i=0; i<cmbPerfil.getItemCount(); i++){
				PerfilORM orm = (PerfilORM) cmbPerfil.getItemAtIndex(i).getValue();
				if(orm.getDwpfperfilid().equals(usuario.getDwpfperfilid())){
					cmbPerfil.setSelectedIndex(i);
					break;
				}
			}
			txtUsuario.setReadonly(true);
			txtPassword.setReadonly(true);
			wdwUsuario.setAttribute(Conf.KEY_APERTURA, Conf.KEY_EDITA);
		}
		wdwUsuario.doHighlighted();
	}
	
	public ViewUser obtieneUsuario(){
		Textbox txtFirstName = (Textbox)wdwUsuario.getFellow("txtFirstName");
		Textbox txtSecondName = (Textbox)wdwUsuario.getFellow("txtSecondName");
		Textbox txtLastName = (Textbox)wdwUsuario.getFellow("txtLastName");
		Textbox txtLastName1 = (Textbox)wdwUsuario.getFellow("txtLastName1");
		Decimalbox decTelefono = (Decimalbox)wdwUsuario.getFellow("decTelefono");
		Textbox txtCorreo = (Textbox)wdwUsuario.getFellow("txtCorreo");
		
		Textbox txtUsuario = (Textbox)wdwUsuario.getFellow("txtUsuario");
		Textbox txtPassword = (Textbox)wdwUsuario.getFellow("txtPassword");
		Combobox cmbPerfil = (Combobox)wdwUsuario.getFellow("cmbPerfil");
		
		if(txtFirstName.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar al menos el primer nombre para el usuario.");
			return null;
		}else if(!Conf.sinComilla(txtFirstName.getText())){
			mostrarMensaje("El primer nombre contiene caracteres no permitidos.");
			return null;
		}
		
		if(!Conf.sinComilla(txtSecondName.getText().trim())){
			mostrarMensaje("El segundo nombre contiene caracteres no permitidos.");
			return null;
		}
		
		if(txtLastName.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar al menos el primer apellido para el usuario.");
			return null;
		}else if(!Conf.sinComilla(txtLastName.getText())){
			mostrarMensaje("El primer apellido contiene caracteres no permitidos.");
			return null;
		}
		if(!Conf.sinComilla(txtLastName1.getText().trim())){
			mostrarMensaje("El segundo apellido contiene caracteres no permitidos.");
			return null;
		}
		
		if(decTelefono.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar número de teléfono para el usuario.");
			return null;
		}else if(decTelefono.getText().trim().contains(".")){
			mostrarMensaje("El número de teléfono contiene caracteres no permitidos.");
			return null;
		}
		
		if(txtCorreo.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar el correo electrónico.");
			return null;
		}else if(!Conf.sinComilla(txtCorreo.getText())){
			mostrarMensaje("El correo contiene caracteres no permitidos.");
			return null;
		}else if(!Conf.isEmailValido(txtCorreo.getText())){
			mostrarMensaje("El formato del correo es inválido.");
			return null;
		}
		
		if(txtUsuario.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar el usuario.");
			return null;
		}else if(!Conf.sinComilla(txtUsuario.getText())){
			mostrarMensaje("El usuario contiene caracteres no permitidos.");
			return null;
		}
		if(txtPassword.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar la contraseña para este usuario.");
			return null;
		}
		if(cmbPerfil.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar un perfil para el usuario.");
			return null;
		}
		
		String segundoNombre = txtSecondName.getText().trim();
		String tercerNombre = "";
		if(segundoNombre.split(" ").length > 1){
			tercerNombre = segundoNombre.substring(segundoNombre.indexOf(" ") + 1, segundoNombre.length());
			segundoNombre = segundoNombre.substring(0, segundoNombre.indexOf(" "));
		}
		String segundoApellido = txtLastName1.getText().trim();
		String apellidoCasada = "";
		if(segundoApellido.split(" ").length > 1){
			apellidoCasada = segundoApellido.substring(segundoApellido.indexOf(" ") + 1, segundoApellido.length());
			segundoApellido = segundoApellido.substring(0, segundoApellido.indexOf(" "));
		}
		
		ViewUser obj = new ViewUser();
		obj.setDwpeprimernombre(txtFirstName.getText());
		obj.setDwpesegundonombre(segundoNombre);
		obj.setDwpetercernombre(tercerNombre);
		obj.setDwpeprimerapellido(txtLastName.getText().trim());
		obj.setDwpesegundoapellido(segundoApellido);
		obj.setDwpeapellidocasada(apellidoCasada);
		obj.setDwpetelefono(decTelefono.getText());
		obj.setDwpecorreo(txtCorreo.getText().trim());
		obj.setDwpfperfilid(((PerfilORM) cmbPerfil.getSelectedItem().getValue()).getDwpfperfilid());
		
		obj.setDwususuarioid(txtUsuario.getText().trim());
		obj.setDwuspassword(DigestUtils.md5Hex( txtPassword.getText() ));
		return obj;
	}
	
	EventListener<Event> onClick_btnGuardarPass = new EventListener<Event>() {
		public void onEvent(Event event) {
			Textbox txtPassword = (Textbox) wdwCambioPass.getFellow("txtPassword");
			Textbox txtConfirmPass = (Textbox) wdwCambioPass.getFellow("txtConfirmPass");
			
			if("".equals(txtPassword.getText().trim())){
				mostrarMensaje("Ingrese contraseña");
				return;
			}
			if("".equals(txtConfirmPass.getText().trim())){
				mostrarMensaje("Confirme contraseña");
				return;
			}
			
			if(!txtPassword.getText().equals(txtConfirmPass.getText())){
				mostrarMensaje("Las contraseñas no coinciden.");
				return;
			}
			ViewUser usuario = lbxUsuario.getSelectedItem().getValue();
			usuario.setDwuspassword(DigestUtils.md5Hex(txtPassword.getText()));
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				dao.actualizaPassword(conn, usuario);
				
				mostrarMensaje("La contraseña ha sido actualizada.");
				cargaLbxUsuario(conn, filtro);
				wdwCambioPass.setVisible(false);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClick_btnGuardarUsuario = new EventListener<Event>() {
		public void onEvent(Event event) {
			ViewUser obj = obtieneUsuario();
			if(obj == null){
				return;
			}
			
			Connection conn = null;
			String k_ap = wdwUsuario.getAttribute(Conf.KEY_APERTURA).toString();
			try{
				conn = obtieneConexion();
				if(k_ap.equals(Conf.KEY_NUEVO)){
					String personaid = dao.obtieneIdPersona(conn);
					obj.setDwpepersonaid(personaid);
					dao.insertaPersona(conn, obj);
					dao.insertaUsuario(conn, obj);
				}else if(k_ap.equals(Conf.KEY_EDITA)){
					ViewUser usuarioSeleccionado = lbxUsuario.getSelectedItem().getValue();
					obj.setDwpepersonaid(usuarioSeleccionado.getDwpepersonaid());
					obj.setDwuscodigo(usuarioSeleccionado.getDwuscodigo());
					dao.actualizaPersona(conn, obj);
					dao.actualizaUsuario(conn, obj);
				}
				cargaLbxUsuario(conn, filtro);
				wdwUsuario.setVisible(false);
				mostrarMensaje("Los datos fueron guardados correctamente.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
}
