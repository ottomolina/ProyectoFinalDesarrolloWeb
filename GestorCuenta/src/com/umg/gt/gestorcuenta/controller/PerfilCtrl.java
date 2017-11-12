package com.umg.gt.gestorcuenta.controller;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import com.umg.gt.gestorcuenta.dao.PerfilDAO;
import com.umg.gt.gestorcuenta.orm.PerfilORM;
import com.umg.gt.gestorcuenta.orm.PermisoORM;
import com.umg.gt.gestorcuenta.util.Conf;

public class PerfilCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PerfilCtrl.class);
	private PerfilDAO dao;
	private List<PermisoORM> listaNuevo;
	private List<PermisoORM> listaAgregado;
	
	Window wdwPerfil;
	Window wdwPerfilMant;
	Window wdwConfPerfil;
	Window wdwPermiso;
	
	Listbox lbxPerfilBuscar;
	Listbox lbxPerfil;
	Listbox lbxPermiso;
	
	Image imgBuscar;
	Textbox txtPerfil;
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		dao = new PerfilDAO();
		
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxPerfil(conn, null);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$imgBuscar(){
		if(lbxPerfilBuscar.isVisible()){
			lbxPerfilBuscar.setVisible(false);
		}else{
			lbxPerfilBuscar.setVisible(true);
		}
	}
	
	public void cargaLbxPerfil(Connection conn, PerfilORM perfilOrm) throws Exception{
		lbxPerfil.getItems().clear();
		lbxPermiso.getItems().clear();
		List<PerfilORM> listaPerfil = dao.obtieneListaPerfil(conn, perfilOrm);
		Iterator<PerfilORM> iList = listaPerfil.iterator();
		PerfilORM perfil;
		Listitem item;
		Listcell cell;
		
		while(iList.hasNext()){
			perfil = iList.next();
			item = new Listitem();
			cell = new Listcell("");
			cell.setParent(item);
			cell = new Listcell(perfil.getDwpfnombre());
			cell.setParent(item);
			item.setValue(perfil);
			item.removeEventListener("onClick", onClick_lbxPerfil);
			item.addEventListener("onClick", onClick_lbxPerfil);
			item.setParent(lbxPerfil);
		}
	}
	
	public void cargaLbxPermiso(Connection conn, PermisoORM permisoOrm) throws Exception {
		lbxPermiso.getItems().clear();
		
		List<PermisoORM> listaPermiso = dao.obtienePermisos(conn, permisoOrm);
		
		Iterator<PermisoORM> iList = listaPermiso.iterator();
		PermisoORM permiso;
		Listitem item;
		Listcell cell;
		
		while(iList.hasNext()){
			permiso = iList.next();
			
			item = new Listitem();
			
			cell = new Listcell();
			cell.setParent(item);
			cell = new Listcell(permiso.getDwpmnombre());
			cell.setParent(item);
			cell = new Listcell(permiso.getDwpmnombretecnico());
			cell.setParent(item);
			
			item.setValue(permiso);
			item.setParent(lbxPermiso);
		}
	}
	
	public void onClick$btnAgregarPerfil(){
		cargaVentanaPerfil("Nuevo Perfil", Conf.KEY_NUEVO, "");
	}
	
	public void onOK$txtPerfil(){
		PerfilORM perfil = null;
		if(!txtPerfil.getText().trim().equals("")){
			perfil = new PerfilORM();
			perfil.setDwpfnombre(txtPerfil.getText().trim());
		}
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxPerfil(conn, perfil);
		}catch(Exception e){
			mensajeInconveniente();
			log.error(e.getMessage(), e);
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$btnModificarPerfil(){
		if(lbxPerfil.getSelectedCount() == 0){
			mostrarMensaje("Debe seleccionar un perfil.");
			return;
		}
		PerfilORM perfil = (PerfilORM) lbxPerfil.getSelectedItem().getValue();
		Textbox txtPerfil = (Textbox) wdwConfPerfil.getFellow("txtPerfil");
		txtPerfil.setText(perfil.getDwpfnombre());
		
		Button btnGuardar = (Button) wdwConfPerfil.getFellow("btnGuardar");
		Button btnAddPermiso = (Button) wdwConfPerfil.getFellow("btnAddPermiso");
		Button btnModPermiso = (Button) wdwConfPerfil.getFellow("btnModPermiso");
		Button btnDelPermiso = (Button) wdwConfPerfil.getFellow("btnDelPermiso");
		
		Button btnMoveRight = (Button) wdwConfPerfil.getFellow("btnMoveRight");
		Button btnMoveLeft = (Button) wdwConfPerfil.getFellow("btnMoveLeft");
		Button btnMoveRightAll = (Button) wdwConfPerfil.getFellow("btnMoveRightAll");
		Button btnMoveLeftAll = (Button) wdwConfPerfil.getFellow("btnMoveLeftAll");
		btnGuardar.removeEventListener("onClick", onClick_btnGuardarConfPerfil);
		btnGuardar.addEventListener("onClick", onClick_btnGuardarConfPerfil);
		btnAddPermiso.removeEventListener("onClick", onClick_btnAddPermiso);
		btnAddPermiso.addEventListener("onClick", onClick_btnAddPermiso);
		btnModPermiso.removeEventListener("onClick", onClick_btnModPermiso);
		btnModPermiso.addEventListener("onClick", onClick_btnModPermiso);
		btnDelPermiso.removeEventListener("onClick", onClick_btnAddPermiso);
		btnDelPermiso.addEventListener("onClick", onClick_btnAddPermiso);
		
		btnMoveRight.removeEventListener("onClick", onClick_btnMoveRight);
		btnMoveRight.addEventListener("onClick", onClick_btnMoveRight);
		btnMoveLeft.removeEventListener("onClick", onClick_btnMoveLeft);
		btnMoveLeft.addEventListener("onClick", onClick_btnMoveLeft);
		
		btnMoveRightAll.removeEventListener("onClick", onClick_btnMoveRightAll);
		btnMoveRightAll.addEventListener("onClick", onClick_btnMoveRightAll);
		btnMoveLeftAll.removeEventListener("onClick", onClick_btnMoveLeftAll);
		btnMoveLeftAll.addEventListener("onClick", onClick_btnMoveLeftAll);
		
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxPermisoConfPerfil(conn, perfil.getDwpfperfilid(), "nuevo");
			cargaLbxPermisoConfPerfil(conn, perfil.getDwpfperfilid(), "asig");
			
			wdwConfPerfil.doHighlighted();
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}
	}
	
	public void onClick$btnEliminarPerfil(){
		if(lbxPerfil.getSelectedCount() == 0){
			mostrarMensaje("Debe seleccionar un perfil.");
			return;
		}
		mostrarConfirmacion("Está seguro de elimninar el perfil seleccionado?", new EventListener<Event>(){
			public void onEvent(Event event){
				if(!event.getName().equals("onYes")){
					return;
				}
				PerfilORM perfil = (PerfilORM) lbxPerfil.getSelectedItem().getValue();
				
				Connection conn = null;
				try{
					conn = obtieneConexion();
					dao.eliminaAsocPermiso(conn, perfil.getDwpfperfilid(), null);
					dao.eliminaPerfil(conn, perfil);
					cargaLbxPerfil(conn, null);
				}catch(Exception e){
					log.error(e.getMessage(), e);
					mensajeInconveniente();
				}finally{
					cerrarConexion(conn);
				}
			}
		});
	}
	
	private void cargaVentanaPerfil(String titulo, String modoApertura, String perfil){
		Button btnGuardar = (Button) wdwPerfilMant.getFellow("btnGuardarPerfil");
		Textbox txtNombrePerfil = (Textbox) wdwPerfilMant.getFellow("txtNombrePerfil");
		
		txtNombrePerfil.setText(perfil);
		
		btnGuardar.removeEventListener("onClick", onClick_btnGuardar);
		btnGuardar.addEventListener("onClick", onClick_btnGuardar);
		
		wdwPerfilMant.setTitle(titulo);
		wdwPerfilMant.removeAttribute(Conf.KEY_APERTURA);
		wdwPerfilMant.setAttribute(Conf.KEY_APERTURA, modoApertura);
		wdwPerfilMant.doHighlighted();
	}
	
	private void cargaLbxPermisoConfPerfil(Connection conn, String perfilid, String tipo) throws Exception {
		List<PermisoORM> listaPermiso = dao.obtienePermisos(conn, perfilid, tipo);
		Listbox lbxPermiso = null;
		if(tipo.equals("nuevo")){
			this.listaNuevo = listaPermiso;
			lbxPermiso = (Listbox) wdwConfPerfil.getFellow("lbxPermisoNuevo");
		}else if(tipo.equals("asig")){
			this.listaAgregado = listaPermiso;
			lbxPermiso = (Listbox) wdwConfPerfil.getFellow("lbxPermisoAdded");
		}
		lbxPermiso.getItems().clear();
		if(listaPermiso.size() == 0){
			return;
		}
		Iterator<PermisoORM> iListaPermiso = listaPermiso.iterator();
		PermisoORM obj;
		Listitem item;
		Listcell cell;
		
		while(iListaPermiso.hasNext()){
			obj = iListaPermiso.next();
			item = new Listitem();
			
			cell = new Listcell();
			cell.setParent(item);
			cell = new Listcell(obj.getDwpmnombre());
			cell.setParent(item);
			cell = new Listcell(obj.getDwpmnombretecnico());
			cell.setParent(item);
			item.setValue(obj);
			item.setParent(lbxPermiso);
		}
	}
	
	EventListener<Event> onClick_btnGuardar = new EventListener<Event>() {
		public void onEvent(Event event) {
			PerfilORM obj = obtienePerfil();
			if(obj == null){
				return;
			}
			
			Connection conn = null;
			String k_ap = wdwPerfilMant.getAttribute(Conf.KEY_APERTURA).toString();
			try{
				conn = obtieneConexion();
				int resp = 0;
				String perfilid = null;
				if(k_ap.equals(Conf.KEY_NUEVO)){
					perfilid = dao.obtieneSecuencia(conn);
					obj.setDwpfperfilid(perfilid);
					resp = dao.insertaPerfil(conn, obj);
				}else if(k_ap.equals(Conf.KEY_EDITA)){
					perfilid = ((PerfilORM)lbxPerfil.getSelectedItem().getValue()).getDwpfperfilid();
					obj.setDwpfperfilid(perfilid);
					resp = dao.actualizaPerfil(conn, obj);
				}
				if(resp == 0){
					log.error("El perfil no ha sido guardado.");
					mensajeInconveniente();
				}else{
					cargaLbxPerfil(conn, null);
					wdwPerfilMant.setVisible(false);
					mostrarMensaje("El perfil ha sido guardado");
				}
			}catch(Exception e){
				mensajeInconveniente();
				log.error(e.getMessage(), e);
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClick_lbxPerfil = new EventListener<Event>() {
		public void onEvent(Event event) {
			Connection conn = null;
			try{
				conn = obtieneConexion();
				PermisoORM permiso = new PermisoORM();
				PerfilORM perfil = lbxPerfil.getSelectedItem().getValue();
				permiso.setDwpfperfilid(perfil.getDwpfperfilid());
				cargaLbxPermiso(conn, permiso);
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClick_btnAddPermiso = new EventListener<Event>(){
		public void onEvent(Event event){
			cargaWdwPermiso(null);
		}
	};
	
	EventListener<Event> onClick_btnModPermiso = new EventListener<Event>(){
		public void onEvent(Event event){
			Listbox lbxPermisoNuevo = (Listbox)wdwConfPerfil.getFellow("lbxPermisoNuevo");
			if(lbxPermisoNuevo.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar un permiso de la lista.");
				return;
			}
			PermisoORM permiso = lbxPermisoNuevo.getSelectedItem().getValue();
			cargaWdwPermiso(permiso);
		}
	};
	
	EventListener<Event> onClick_btnDelPermiso = new EventListener<Event>(){
		public void onEvent(Event event){
			
		}
	};
	
	EventListener<Event> onClick_btnGuardarConfPerfil = new EventListener<Event>() {
		public void onEvent(Event event) {
			Textbox txtPerfil = (Textbox) wdwConfPerfil.getFellow("txtPerfil");
			if(txtPerfil.getText().trim().equals("")){
				mostrarMensaje("Debe ingresar el nombre del perfil.");
				return;
			}
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				PerfilORM perfil = (PerfilORM) lbxPerfil.getSelectedItem().getValue();
				perfil.setDwpfnombre(txtPerfil.getText().trim());
				dao.actualizaPerfil(conn, perfil);
				dao.eliminaAsocPermiso(conn, perfil.getDwpfperfilid(), null);
				Iterator<PermisoORM> iteradorPermiso = listaAgregado.iterator();
				PermisoORM permiso = null;
				while(iteradorPermiso.hasNext()){
					permiso = iteradorPermiso.next();
					dao.insertaAsocPermiso(conn, perfil.getDwpfperfilid(), permiso.getDwpmpermisoid());
				}
				wdwConfPerfil.setVisible(false);
				cargaLbxPerfil(conn, null);
				mostrarMensaje("El perfil fue guardado exitosamente.");
				
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClick_btnGuardarPermiso = new EventListener<Event>(){
		public void onEvent(Event event){
			Textbox txtPermiso = (Textbox) wdwPermiso.getFellow("txtPermiso");
			Textbox txtComponente = (Textbox) wdwPermiso.getFellow("txtComponente");
			if(txtPermiso.getText().trim().equals("")){
				mostrarMensaje("Debe ingresar el nombre del permiso.");
				return;
			}
			if(!Conf.sinComilla(txtPermiso.getText().trim())){
				mostrarMensaje("El nombre del permiso no debe contener comillas.");
				return;
			}
			if(txtComponente.getText().trim().equals("")){
				mostrarMensaje("Debe ingresar el componente del permiso.");
				return;
			}
			if(!Conf.sinComilla(txtComponente.getText().trim())){
				mostrarMensaje("El componente no debe contener comillas.");
				return;
			}
			PermisoORM permiso = new PermisoORM();
			permiso.setDwpmnombre(txtPermiso.getText().trim());
			permiso.setDwpmnombretecnico(txtComponente.getText().trim());
			
			int resultado = 0;
			Connection conn = null;
			try{
				conn = obtieneConexion();
				if(wdwPermiso.getAttribute(Conf.KEY_APERTURA).toString().equals(Conf.KEY_NUEVO)){
					resultado = dao.insertaPermiso(conn, permiso);
				}else if(wdwPermiso.getAttribute(Conf.KEY_APERTURA).toString().equals(Conf.KEY_EDITA)){
					resultado = dao.actualizaPermiso(conn, permiso);
				}
				
				if(resultado > 0){
					wdwPermiso.setVisible(false);
					PerfilORM perfil = (PerfilORM) lbxPerfil.getSelectedItem().getValue();
					cargaLbxPermisoConfPerfil(conn, perfil.getDwpfperfilid(), "nuevo");
				}else{
					log.error("El permiso no ha sido guardado.");
					mensajeInconveniente();
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	public void cargaWdwPermiso(PermisoORM permiso){
		Textbox txtPermiso = (Textbox) wdwPermiso.getFellow("txtPermiso");
		Textbox txtComponente = (Textbox) wdwPermiso.getFellow("txtComponente");
		Button btnGuardarPermiso = (Button) wdwPermiso.getFellow("btnGuardarPermiso");
		wdwPermiso.removeAttribute(Conf.KEY_APERTURA);
		if(permiso == null){
			txtPermiso.setText("");
			txtComponente.setText("");
			wdwPermiso.setTitle("Agregar Permiso");
			wdwPermiso.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
		}else{
			txtPermiso.setText(permiso.getDwpmnombre());
			txtComponente.setText(permiso.getDwpmnombretecnico());
			wdwPermiso.setTitle("Modificar Permiso");
			wdwPermiso.setAttribute(Conf.KEY_APERTURA, Conf.KEY_EDITA);
		}
		btnGuardarPermiso.removeEventListener("onClick", onClick_btnGuardarPermiso);
		btnGuardarPermiso.addEventListener("onClick", onClick_btnGuardarPermiso);
		wdwPermiso.doHighlighted();
	}
	
	EventListener<Event> onClick_btnMoveRight = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxPermisoNuevo = (Listbox) wdwConfPerfil.getFellow("lbxPermisoNuevo");
			if(lbxPermisoNuevo.getSelectedItem() == null){
				return;
			}
			Listbox lbxPermisoAdded = (Listbox) wdwConfPerfil.getFellow("lbxPermisoAdded");
			Listitem item = lbxPermisoNuevo.getSelectedItem();
			item.setParent(lbxPermisoAdded);
			lbxPermisoAdded.clearSelection();
			PermisoORM permiso = (PermisoORM) item.getValue();
			listaAgregado.add(permiso);
			listaNuevo.remove(permiso);
		}
	};
	
	EventListener<Event> onClick_btnMoveLeft = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxPermisoAdded = (Listbox) wdwConfPerfil.getFellow("lbxPermisoAdded");
			if(lbxPermisoAdded.getSelectedItem() == null){
				return;
			}
			Listbox lbxPermisoNuevo = (Listbox) wdwConfPerfil.getFellow("lbxPermisoNuevo");
			Listitem item = lbxPermisoAdded.getSelectedItem();
			item.setParent(lbxPermisoNuevo);
			lbxPermisoAdded.clearSelection();
			PermisoORM permiso = (PermisoORM) item.getValue();
			listaNuevo.add(permiso);
			listaAgregado.remove(permiso);
		}
	};
	
	EventListener<Event> onClick_btnMoveRightAll = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxPermisoNuevo = (Listbox) wdwConfPerfil.getFellow("lbxPermisoNuevo");
			Listbox lbxPermisoAdded = (Listbox) wdwConfPerfil.getFellow("lbxPermisoAdded");
			Listitem item = null;
			while(lbxPermisoNuevo.getItemCount() != 0){
				item = lbxPermisoNuevo.getItems().get(0);
				item.setParent(lbxPermisoAdded);
				PermisoORM permiso = (PermisoORM) item.getValue();
				listaAgregado.add(permiso);
				listaNuevo.remove(permiso);
			}
		}
	};
	
	EventListener<Event> onClick_btnMoveLeftAll = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxPermisoNuevo = (Listbox) wdwConfPerfil.getFellow("lbxPermisoNuevo");
			Listbox lbxPermisoAdded = (Listbox) wdwConfPerfil.getFellow("lbxPermisoAdded");
			Listitem item = null;
			while(lbxPermisoAdded.getItemCount() != 0){
				item = lbxPermisoAdded.getItems().get(0);
				item.setParent(lbxPermisoNuevo);
				PermisoORM permiso = (PermisoORM) item.getValue();
				listaAgregado.add(permiso);
				listaNuevo.remove(permiso);
			}
		}
	};
	
	public PerfilORM obtienePerfil(){
		
		Textbox txtNombrePerfil = (Textbox)wdwPerfilMant.getFellow("txtNombrePerfil");
		
		if(txtNombrePerfil.getText().trim().equals("")){
			mostrarMensaje("Debe ingresar el nombre del perfil.");
			return null;
		}
		if(!Conf.sinComilla(txtNombrePerfil.getText())){
			mostrarMensaje("El nombre del perfil contiene caracteres inválidos.");
			return null;
		}
		
		PerfilORM orm = new PerfilORM();
		orm.setDwpfnombre(txtNombrePerfil.getText());
		return orm;
		
	}

}
