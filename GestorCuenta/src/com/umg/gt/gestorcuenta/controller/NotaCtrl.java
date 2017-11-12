package com.umg.gt.gestorcuenta.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.umg.gt.gestorcuenta.dao.DocumentoDAO;
import com.umg.gt.gestorcuenta.orm.DocumentoORM;
import com.umg.gt.gestorcuenta.orm.TipoDocumentoORM;

public class NotaCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CuentaCtrl.class);
	private DocumentoDAO dao;
	
	Window wdwFiltro;
	
	Listbox lbxDocumento;
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		dao = new DocumentoDAO();
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxDocumento(conn, null);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void cargaLbxDocumento(Connection conn, DocumentoORM filtro) throws SQLException {
		lbxDocumento.getItems().clear();
		lbxDocumento.setEmptyMessage("No existen documentos de crédito y débito en el sistema.");
		
		List<DocumentoORM> listaDocs = dao.obtieneDocumentos(conn, filtro);
		Iterator<DocumentoORM> iList = listaDocs.iterator();
		
		DocumentoORM obj;
		Listitem item;
		Listcell cell;
		while(iList.hasNext()){
			obj = iList.next();
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			
			cell = new Listcell(obj.getNombre_cuenta());
			cell.setParent(item);
			
			cell = new Listcell(obj.getNum_cuenta());
			cell.setParent(item);
			
			cell = new Listcell(obj.getMonto());
			cell.setParent(item);
			
			cell = new Listcell(obj.getDescripcion());
			cell.setParent(item);
			
			cell = new Listcell(obj.getTipo_documento());
			cell.setParent(item);
			
			cell = new Listcell(obj.getFecha());
			cell.setParent(item);
			
			item.setValue(obj);
			item.setParent(lbxDocumento);
		}
	}
	
	public void onClick$btnFiltro(){
		Combobox cmbTipoDocumento = (Combobox) wdwFiltro.getFellow("cmbTipoDocumento");
		Button btnBuscar = (Button) wdwFiltro.getFellow("btnBuscar");
		btnBuscar.removeEventListener("onClick", onClick_btnBuscar);
		btnBuscar.addEventListener("onClick", onClick_btnBuscar);
		
		cmbTipoDocumento.getItems().clear();
		Connection conn = null;
		try{
			conn = obtieneConexion();
			List<TipoDocumentoORM> tipo = dao.obtieneTipoDocumento(conn);
			Iterator<TipoDocumentoORM> iList = tipo.iterator();
			
			TipoDocumentoORM obj;
			Comboitem item;
			while(iList.hasNext()){
				obj = iList.next();
				item = new Comboitem();
				
				item.setLabel(obj.getDescripcion_documento());
				item.setParent(cmbTipoDocumento);
			}
			wdwFiltro.doHighlighted();
		}catch(SQLException e){
			log.error(e.getMessage(), e);
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$btnLimpiar(){
		Combobox cmbTipoDocumento = (Combobox) wdwFiltro.getFellow("cmbTipoDocumento");
		Textbox txtNomCliente = (Textbox) wdwFiltro.getFellow("txtNomCliente");
		cmbTipoDocumento.setSelectedItem(null);
		cmbTipoDocumento.setText("");
		txtNomCliente.setText("");
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxDocumento(conn, null);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	EventListener<Event> onClick_btnBuscar = new EventListener<Event>() {
		public void onEvent(Event event) {
			Combobox cmbTipoDocumento = (Combobox) wdwFiltro.getFellow("cmbTipoDocumento");
			Textbox txtNomCliente = (Textbox) wdwFiltro.getFellow("txtNomCliente");
			
			if("".equals(txtNomCliente.getText()) && cmbTipoDocumento.getSelectedItem() == null){
				mostrarMensaje("Debe ingresar valores en la búsqueda");
				return;
			}
			DocumentoORM filtro = new DocumentoORM();
			if(cmbTipoDocumento.getSelectedItem() != null){
				filtro.setTipo_documento(cmbTipoDocumento.getSelectedItem().getLabel());
			}
			filtro.setNombre_cuenta(txtNomCliente.getText());
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				cargaLbxDocumento(conn, filtro);
				wdwFiltro.setVisible(false);
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	}; 
	
}
