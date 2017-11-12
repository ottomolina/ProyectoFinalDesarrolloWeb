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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.umg.gt.gestorcuenta.dao.ManDAO;
import com.umg.gt.gestorcuenta.orm.CategoriaORM;
import com.umg.gt.gestorcuenta.orm.TipoCuentaORM;
import com.umg.gt.gestorcuenta.util.Conf;

public class ManCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(ManCtrl.class);
	private ManDAO dao;
	
	Window wdwTipoCuenta;
	Window wdwCategoria;
	
	Listbox lbxTipoCuenta;
	Listbox lbxCategoria;
	
	Tab tbTipoCuenta;
	Tab tbCategoria;
	
	public void doAfterCompose(Component comp) {
		Connection conn = null;
		dao = new ManDAO();
		try{
			super.doAfterCompose(comp);
			conn = obtieneConexion();
			cargaLbxCategoria(conn);
			cargaLbxTipoCuenta(conn);
			
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void cargaLbxTipoCuenta(Connection conn) throws SQLException {
		lbxTipoCuenta.getItems().clear();
		List<TipoCuentaORM> listaTipoCuenta = dao.obtieneTipoCuenta(conn);
		Iterator<TipoCuentaORM> iList = listaTipoCuenta.iterator();
		
		Listitem item;
		Listcell cell;
		
		while(iList.hasNext()){
			TipoCuentaORM obj = iList.next();
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			cell = new Listcell(obj.getDwatdescripcion());
			cell.setParent(item);
			cell = new Listcell(obj.getDwatinteres());
			cell.setParent(item);
			cell = new Listcell(obj.getDwatfechacorte() + " de cada mes");
			cell.setParent(item);
			cell = new Listcell(obj.getDwatfechalimite() + " de cada mes");
			cell.setParent(item);
			
			item.setValue(obj);
			item.setContext("popMenu");
			item.setParent(lbxTipoCuenta);
		}
	}
	
	public void cargaLbxCategoria(Connection conn) throws SQLException {
		lbxCategoria.getItems().clear();
		List<CategoriaORM> listaCategoria = dao.obtieneCategoria(conn);
		Iterator<CategoriaORM> iList = listaCategoria.iterator();
		
		Listitem item;
		Listcell cell;
		
		while(iList.hasNext()){
			CategoriaORM obj = iList.next();
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			cell = new Listcell(obj.getDwcadescripcion());
			cell.setParent(item);
			cell = new Listcell("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(obj.getDwcacreditominimo())));
			cell.setParent(item);
			cell = new Listcell("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(obj.getDwcacreditomaximo())));
			cell.setParent(item);
			
			item.setValue(obj);
			item.setContext("popMenu");
			item.setParent(lbxCategoria);
		}	
	}
	
	public void onClick$btnAgregar(){
		if(tbCategoria.isSelected()){
			cargaCategoria(null);
			wdwCategoria.setTitle("Nueva Categoría de Cuenta");;
			wdwCategoria.doHighlighted();
		}else if(tbTipoCuenta.isSelected()){
			cargaTipoCuenta(null);
			wdwTipoCuenta.setTitle("Nuevo Tipo de Cuenta");;
			wdwTipoCuenta.doHighlighted();
		}
	}
	
	public void onClick$mniEliminar(){
		if(tbTipoCuenta.isSelected()){
			mostrarConfirmacion("Está seguro de eliminar el tipo de cuenta?", new EventListener<Event>() {
				public void onEvent(Event event) {
					if(!event.getName().equals("onYes")){
						return;
					}
					TipoCuentaORM orm = lbxTipoCuenta.getSelectedItem().getValue();
					Connection conn = null;
					try{
						conn = obtieneConexion();
						String conteo = dao.verificaTipoCuenta(conn, orm.getDwattipocuentaid());
						if("0".equals(conteo)){
							dao.eliminaTipoCuenta(conn, orm.getDwattipocuentaid());
							cargaLbxTipoCuenta(conn);
							mostrarMensaje("Tipo de cuenta eliminado.");
						}else{
							mostrarMensaje("El tipo de cuenta está siendo usado actualmente.");
						}
					}catch(Exception e){
						log.error(e.getMessage(), e);
						mensajeInconveniente();
					}finally{
						cerrarConexion(conn);
					}
				}
			});
		}else if(tbCategoria.isSelected()){
			mostrarConfirmacion("Está seguro de eliminar categoría de cuenta?", new EventListener<Event>() {
				public void onEvent(Event event) {
					if(!event.getName().equals("onYes")){
						return;
					}
					CategoriaORM orm = lbxCategoria.getSelectedItem().getValue();
					Connection conn = null;
					try{
						conn = obtieneConexion();
						String conteo = dao.verificaCategoriaCuenta(conn, orm.getDwcacategoriaid());
						if("0".equals(conteo)){
							dao.eliminaCategoria(conn, orm.getDwcacategoriaid());
							cargaLbxCategoria(conn);
							mostrarMensaje("Categoría de cuenta eliminado.");
						}else{
							mostrarMensaje("La categoría está siendo usada actualmente.");
						}
					}catch(Exception e){
						log.error(e.getMessage(), e);
						mensajeInconveniente();
					}finally{
						cerrarConexion(conn);
					}
				}
			});
		}
	}
	
	public void onClick$mniModificar(){
		if(tbTipoCuenta.isSelected()){
			TipoCuentaORM orm = lbxTipoCuenta.getSelectedItem().getValue();
			cargaTipoCuenta(orm);
			wdwTipoCuenta.setTitle("Modificar Tipo de Cuenta");;
			wdwTipoCuenta.doHighlighted();
		}else if(tbCategoria.isSelected()){
			CategoriaORM orm = lbxCategoria.getSelectedItem().getValue();
			cargaCategoria(orm);
			wdwCategoria.setTitle("Modificar Categoría de Cuenta");;
			wdwCategoria.doHighlighted();
		}
	}
	
	public void cargaCategoria(CategoriaORM orm) {
		wdwCategoria.removeAttribute(Conf.KEY_APERTURA);
		Textbox txtDescripcion = (Textbox) wdwCategoria.getFellow("txtDescripcion");
		Decimalbox decCreditMin = (Decimalbox) wdwCategoria.getFellow("decCreditMin");
		Decimalbox decCreditMax = (Decimalbox) wdwCategoria.getFellow("decCreditMax");
		
		Button btnGuardar = (Button)wdwCategoria.getFellow("btnGuardar");
		btnGuardar.removeEventListener("onClick", onClik_btnGuardarCategoria);
		btnGuardar.addEventListener("onClick", onClik_btnGuardarCategoria);
		
		if(orm == null){
			txtDescripcion.setText("");
			decCreditMin.setText("");
			decCreditMax.setText("");
			wdwCategoria.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
		}else{
			txtDescripcion.setText(orm.getDwcadescripcion());
			decCreditMin.setText(orm.getDwcacreditominimo());
			decCreditMax.setText(orm.getDwcacreditomaximo());
			wdwCategoria.setAttribute(Conf.KEY_APERTURA, Conf.KEY_EDITA);
		}
	}
	
	public void cargaTipoCuenta(TipoCuentaORM orm){
		wdwTipoCuenta.removeAttribute(Conf.KEY_APERTURA);
		Textbox txtTipoCuenta = (Textbox) wdwTipoCuenta.getFellow("txtTipoCuenta");
		Decimalbox decInteres = (Decimalbox) wdwTipoCuenta.getFellow("decInteres");
		Combobox cmbDiaCorte = (Combobox) wdwTipoCuenta.getFellow("cmbDiaCorte");
		Combobox cmbDiaLimite = (Combobox) wdwTipoCuenta.getFellow("cmbDiaLimite");
		cmbDiaCorte.getItems().clear();
		cmbDiaLimite.getItems().clear();
		
		Button btnGuardar = (Button)wdwTipoCuenta.getFellow("btnGuardar");
		btnGuardar.removeEventListener("onClick", onClik_btnGuardarTipoCuenta);
		btnGuardar.addEventListener("onClick", onClik_btnGuardarTipoCuenta);
		
		Comboitem item;
		for(int i=1; i<=28; i++){
			item = new Comboitem();
			item.setLabel(String.valueOf(i));
			item.setParent(cmbDiaCorte);
			
			item = new Comboitem();
			item.setLabel(String.valueOf(i));
			item.setParent(cmbDiaLimite);
		}
		
		if(orm == null){
			txtTipoCuenta.setText("");
			decInteres.setText("");
			cmbDiaCorte.setSelectedItem(null);
			cmbDiaCorte.setText("");
			cmbDiaLimite.setSelectedItem(null);
			cmbDiaLimite.setText("");
			wdwTipoCuenta.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
		}else{
			txtTipoCuenta.setText(orm.getDwatdescripcion());
			decInteres.setText(orm.getDwatinteres());
			
			for(int j=0; j<cmbDiaCorte.getItemCount(); j++){
				if(cmbDiaCorte.getItems().get(j).getLabel().equals(orm.getDwatfechacorte())){
					cmbDiaCorte.setSelectedIndex(j);
					break;
				}
			}
			for(int j=0; j<cmbDiaLimite.getItemCount(); j++){
				if(cmbDiaLimite.getItems().get(j).getLabel().equals(orm.getDwatfechalimite())){
					cmbDiaLimite.setSelectedIndex(j);
					break;
				}
			}
			wdwTipoCuenta.setAttribute(Conf.KEY_APERTURA, Conf.KEY_EDITA);
		}
	}
	
	public CategoriaORM obtieneCategoria(){
		Textbox txtDescripcion = (Textbox) wdwCategoria.getFellow("txtDescripcion");
		Decimalbox decCreditMin = (Decimalbox) wdwCategoria.getFellow("decCreditMin");
		Decimalbox decCreditMax = (Decimalbox) wdwCategoria.getFellow("decCreditMax");
		
		if("".equals(txtDescripcion.getText().trim())){
			mostrarMensaje("Debe ingresar el nombre de la categoría.");
			return null;
		}else if(!Conf.sinComilla(txtDescripcion.getText())){
			mostrarMensaje("El nombre de la categoría contiene caracteres inválidos.");
			return null;
		}
		if(BigDecimal.ZERO.compareTo(decCreditMin.getValue()) == 1){
			mostrarMensaje("Debe ingresar el crédito mínimo correctamente.");
			return null;
		}
		if(BigDecimal.ZERO.compareTo(decCreditMax.getValue()) == 1){
			mostrarMensaje("Debe ingresar el crédito máximo correctamente.");
			return null;
		}
		CategoriaORM orm = new CategoriaORM();
		orm.setDwcadescripcion(txtDescripcion.getText());
		orm.setDwcacreditomaximo(decCreditMax.getText());
		orm.setDwcacreditominimo(decCreditMin.getText());
		return orm;
	}
	
	public TipoCuentaORM obtieneTipoCuenta() {
		Textbox txtTipoCuenta = (Textbox) wdwTipoCuenta.getFellow("txtTipoCuenta");
		Decimalbox decInteres = (Decimalbox) wdwTipoCuenta.getFellow("decInteres");
		Combobox cmbDiaCorte = (Combobox) wdwTipoCuenta.getFellow("cmbDiaCorte");
		Combobox cmbDiaLimite = (Combobox) wdwTipoCuenta.getFellow("cmbDiaLimite");
		
		if("".equals(txtTipoCuenta.getText().trim())){
			mostrarMensaje("Debe ingresar el nombre del tipo de cuenta.");
			return null;
		}else if(!Conf.sinComilla(txtTipoCuenta.getText())){
			mostrarMensaje("El nombre del tipo de cuenta contiene caracteres inválidos.");
			return null;
		}
		if(BigDecimal.ZERO.compareTo(decInteres.getValue()) == 1){
			mostrarMensaje("Debe ingresar la tasa de interés correctamente.");
			return null;
		}
		if(cmbDiaCorte.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar el día de corte.");
			return null;
		}
		if(cmbDiaLimite.getSelectedItem() == null){
			mostrarMensaje("Debe seleccionar el día límite de pago.");
			return null;
		}
		TipoCuentaORM orm = new TipoCuentaORM();
		orm.setDwatdescripcion(txtTipoCuenta.getText());
		orm.setDwatinteres(decInteres.getText());
		orm.setDwatfechacorte(cmbDiaCorte.getSelectedItem().getLabel());
		orm.setDwatfechalimite(cmbDiaLimite.getSelectedItem().getLabel());
		return orm;
	}
	
	EventListener<Event> onClik_btnGuardarCategoria = new EventListener<Event>() {
		public void onEvent(Event event) {
			CategoriaORM obj = obtieneCategoria();
			if(obj == null){
				return;
			}
			String apertura = wdwCategoria.getAttribute(Conf.KEY_APERTURA).toString();
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				if(apertura.equals(Conf.KEY_NUEVO)){
					dao.insertaCategoriaCuenta(conn, obj);
				}else if(apertura.equals(Conf.KEY_EDITA)){
					CategoriaORM orm = lbxCategoria.getSelectedItem().getValue();
					obj.setDwcacategoriaid(orm.getDwcacategoriaid());
					dao.actualizaCategoriaCuenta(conn, obj);
				}
				wdwCategoria.setVisible(false);
				cargaLbxCategoria(conn);
				mostrarMensaje("La categoría fue guardada correctamente.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClik_btnGuardarTipoCuenta = new EventListener<Event>() {
		public void onEvent(Event event) {
			TipoCuentaORM obj = obtieneTipoCuenta();
			if(obj == null){
				return;
			}
			String apertura = wdwTipoCuenta.getAttribute(Conf.KEY_APERTURA).toString();
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				if(apertura.equals(Conf.KEY_NUEVO)){
					dao.insertaTipoCuenta(conn, obj);
				}else if(apertura.equals(Conf.KEY_EDITA)){
					TipoCuentaORM orm = lbxTipoCuenta.getSelectedItem().getValue();
					obj.setDwattipocuentaid(orm.getDwattipocuentaid());
					dao.actualizaTipoCuenta(conn, obj);
				}
				wdwTipoCuenta.setVisible(false);
				cargaLbxTipoCuenta(conn);
				mostrarMensaje("El tipo de cuenta fue guardado correctamente.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
}
