package com.umg.gt.gestorcuenta.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.umg.gt.gestorcuenta.dao.GeneralDAO;
import com.umg.gt.gestorcuenta.dao.SolicitudDAO;
import com.umg.gt.gestorcuenta.orm.SolicitudORM;
import com.umg.gt.gestorcuenta.util.Conf;

public class SolicitudCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(SolicitudCtrl.class);
	private SolicitudDAO dao;
	private SolicitudORM filtro;
	
	Window wdwSolicitud;
	Window wdwFiltro;
	Window wdwManSolicitud;
	
	Listbox lbxSolicitud;
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		dao = new SolicitudDAO();
		
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxSolicitud(conn);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
		}finally{
			cerrarConexion(conn);
		}
		
	}
	
	public void cargaLbxSolicitud(Connection conn) throws SQLException{
		lbxSolicitud.getItems().clear();
		
		List<SolicitudORM> listaSolicitud = dao.obtieneListaSolicitud(conn, filtro);
		Iterator<SolicitudORM> iList = listaSolicitud.iterator();
		SolicitudORM obj;
		Listitem item = null;
		Listcell cell = null;
		
		while(iList.hasNext()){
			obj = iList.next();
			
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			cell = new Listcell(obj.getDwreidentificacion());
			cell.setParent(item);
			cell = new Listcell(obj.getDwrenit());
			cell.setParent(item);
			cell = new Listcell("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(obj.getDwreingresomensual())));
			cell.setParent(item);
			cell = new Listcell("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(obj.getDwremonto())));
			cell.setParent(item);
			cell = new Listcell(obj.getDwsrdescripcion());
			cell.setParent(item);
			cell = new Listcell(obj.getDwususuarioid());
			cell.setParent(item);
			
			item.setValue(obj);
			item.setParent(lbxSolicitud);
		}
	}
	
	public void onClick$btnNuevo(){
		cargaWdwManSolicitud(null);
		wdwManSolicitud.setTitle("Nueva Solicitud");
		wdwManSolicitud.doHighlighted();
	}
	
	public void cargaLbxCliente(Connection conn) throws SQLException {
		Listbox lbxCliente = (Listbox) wdwManSolicitud.getFellow("lbxCliente");
		lbxCliente.getItems().clear();
		
		// Esta parte de codigo es para pruebas, mientras se espera el api del modulo de clientes
		SolicitudORM orm = new GeneralDAO().obtieneClientePrueba(conn);
		Listitem item = new Listitem();
		Listcell cell = new Listcell();
		cell.setParent(item);
		
		cell = new Listcell(orm.getDwrenombrecliente());
		cell.setParent(item);
		cell = new Listcell(orm.getDwreidentificacion());
		cell.setParent(item);
		item.setValue(orm);
		item.setParent(lbxCliente);
		// Cuando se tenga el api implementar con el mismo orm, solo se debe castear
		item.removeEventListener("onDoubleClick", onDoubleClick_lbxCliente);
		item.addEventListener("onDoubleClick", onDoubleClick_lbxCliente);
	}
	
	public void cargaCmbEstado(Connection conn, int var){
		List<SolicitudORM> listaEstados;
		Combobox cmbEstado = null;
		try{
			listaEstados = new GeneralDAO().obtieneEstadosSolicitud(conn);
			
			if(var == 0){ // Cargará el combobox de la ventana de búsqueda
				cmbEstado = (Combobox) wdwFiltro.getFellow("cmbEstado");
			}else if(var == 1){ // Cargará el combobox de la ventana de ingreso
				cmbEstado = (Combobox) wdwManSolicitud.getFellow("cmbEstado");
			}
			cmbEstado.getItems().clear();
			Iterator<SolicitudORM> iListEstado = listaEstados.iterator();
			Comboitem item;
			SolicitudORM orm;
			while(iListEstado.hasNext()){
				orm = iListEstado.next();
				item = new Comboitem();
				item.setValue(orm);
				item.setLabel(orm.getDwsrdescripcion());
				item.setParent(cmbEstado);
			}
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}
	}
	
	public void cargaWdwManSolicitud(SolicitudORM orm){
		wdwManSolicitud.removeAttribute(Conf.KEY_APERTURA);
		Bandbox bndCliente = (Bandbox) wdwManSolicitud.getFellow("bndCliente");
		Textbox txtCliente = (Textbox) wdwManSolicitud.getFellow("txtCliente");
		
		Textbox txtIdentificacion = (Textbox) wdwManSolicitud.getFellow("txtIdentificacion");
		Textbox txtNit = (Textbox) wdwManSolicitud.getFellow("txtNit");
		Decimalbox decIngresoMes = (Decimalbox) wdwManSolicitud.getFellow("decIngresoMes");
		Decimalbox decMontoSolicitado = (Decimalbox) wdwManSolicitud.getFellow("decMontoSolicitado");
		Combobox cmbEstado = (Combobox) wdwManSolicitud.getFellow("cmbEstado");
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaCmbEstado(conn, 1);
			cargaLbxCliente(conn);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
		}finally{
			cerrarConexion(conn);
		}
		txtCliente.setText("");
		if(orm == null){
			bndCliente.setDisabled(false);
			bndCliente.setText("");
			txtIdentificacion.setText("");
			txtNit.setText("");
			decIngresoMes.setText("");
			decMontoSolicitado.setText("");
			cmbEstado.setText("");
			cmbEstado.setSelectedItem(null);
			wdwManSolicitud.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
		}else{
			bndCliente.setDisabled(true);
			bndCliente.setText("Cliente Prueba");
			txtIdentificacion.setText(orm.getDwreidentificacion());
			txtNit.setText(orm.getDwrenit());
			decIngresoMes.setText(orm.getDwreingresomensual());
			decMontoSolicitado.setText(orm.getDwremonto());
			for(int i=0; i<cmbEstado.getItems().size(); i++){
				if(orm.getDwsrdescripcion().equals(cmbEstado.getItems().get(i).getLabel())){
					cmbEstado.setSelectedIndex(i);
					break;
				}
			}
			wdwManSolicitud.setAttribute(Conf.KEY_APERTURA, Conf.KEY_EDITA);
		}
		
	}
	
	public SolicitudORM obtieneSolicitud(){
		Listbox lbxCliente = (Listbox) wdwManSolicitud.getFellow("lbxCliente");
		Decimalbox decIngresoMes = (Decimalbox) wdwManSolicitud.getFellow("decIngresoMes");
		Decimalbox decMontoSolicitado = (Decimalbox) wdwManSolicitud.getFellow("decMontoSolicitado");
		Combobox cmbEstado = (Combobox) wdwManSolicitud.getFellow("cmbEstado");
		
		if("".equals(decIngresoMes.getText().trim())){
			mostrarMensaje("Debe ingresar el ingreso mensual.");
			return null;
		}else if(BigDecimal.ZERO.compareTo(decIngresoMes.getValue()) == 1){
			mostrarMensaje("El ingreso mensual es inválido.");
			return null;
		}
		if("".equals(decMontoSolicitado.getText().trim())){
			mostrarMensaje("Debe ingresar el monto solicitado.");
			return null;
		}else if(BigDecimal.ZERO.compareTo(decMontoSolicitado.getValue()) == 1){
			mostrarMensaje("El monto solicitado es inválido.");
			return null;
		}
		if(cmbEstado.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar el estado de la solicitud.");
			return null;
		}
		SolicitudORM orm;
		if(wdwManSolicitud.getAttribute(Conf.KEY_APERTURA).toString().equals(Conf.KEY_EDITA)){
			orm = lbxSolicitud.getSelectedItem().getValue();
		}else{
			if(lbxCliente.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar el cliente para la solicitud.");
				return null;
			}
			orm = lbxCliente.getSelectedItem().getValue();
		}
		
		orm.setDwreingresomensual(decIngresoMes.getText());
		orm.setDwremonto(decMontoSolicitado.getText());
		SolicitudORM estado = cmbEstado.getSelectedItem().getValue();
		orm.setDwsrestadoid(estado.getDwsrestadoid());
		orm.setDwuscodigo(getUsuario().getDwuscodigo());
		return orm;
	}
	
	EventListener<Event> onDoubleClick_lbxCliente = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listitem itemCliente = (Listitem) event.getTarget();
			SolicitudORM cliente = itemCliente.getValue();
			
			Bandbox bndCliente = (Bandbox) wdwManSolicitud.getFellow("bndCliente");
			Textbox txtIdentificacion = (Textbox) wdwManSolicitud.getFellow("txtIdentificacion");
			Textbox txtNit = (Textbox) wdwManSolicitud.getFellow("txtNit");
			
			bndCliente.setText(cliente.getDwrenombrecliente());
			txtIdentificacion.setText(cliente.getDwreidentificacion());
			txtNit.setText(cliente.getDwrenit());
			bndCliente.close();
		}
	};
	
	EventListener<Event> onClick_btnGuardarSolicitud = new EventListener<Event>() {
		public void onEvent(Event event) {
			SolicitudORM obj = obtieneSolicitud();
			if(obj == null){
				return;
			}
		}
	};
	
}
