package com.umg.gt.gestorcuenta.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import com.umg.gt.gestorcuenta.dao.CuentaDAO;
import com.umg.gt.gestorcuenta.orm.ClienteORM;
import com.umg.gt.gestorcuenta.orm.CuentaORM;
import com.umg.gt.gestorcuenta.orm.DesembolsoORM;
import com.umg.gt.gestorcuenta.orm.TarjetaORM;
import com.umg.gt.gestorcuenta.util.Conf;
import com.umg.gt.gestorcuenta.util.ExcepcionUsuario;
import com.umg.ws.TarjetaWS;
import com.umg.ws.ValidaMontoStub.ExisteTC;
import com.umg.ws.ValidaMontoStub.ValidaTC;

public class CuentaCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(CuentaCtrl.class);
	private CuentaDAO dao;
	private ExisteTC dato;
	
	Window wdwCuenta;
	Window wdwAgregar;
	Window wdwFiltro;
	Window wdwInfo;
	Window wdwPagos;
	Window wdwPagar;
	
	Listbox lbxCuenta;
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		dao = new CuentaDAO();
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxCuenta(conn, null);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$btnAgregar(){
		Decimalbox decTarjeta 	 = (Decimalbox) wdwAgregar.getFellow("decTarjeta");
		Textbox txtNombre 	 	 = (Textbox) wdwAgregar.getFellow("txtNombre");
		Decimalbox decCcv 	 	 = (Decimalbox) wdwAgregar.getFellow("decCcv");
		Spinner spnMes 		 	 = (Spinner) wdwAgregar.getFellow("spnMes");
		Spinner spnAnio		 	 = (Spinner) wdwAgregar.getFellow("spnAnio");
		Decimalbox decIngresoMes = (Decimalbox) wdwAgregar.getFellow("decIngresoMes");
		Textbox txtQuestNombre   = (Textbox) wdwAgregar.getFellow("txtQuestNombre");
		Textbox txtQuestNit  	 = (Textbox) wdwAgregar.getFellow("txtQuestNit");
		Tab tabInfoCuenta		 = (Tab) wdwAgregar.getFellow("tabInfoCuenta");
		
		Label lblCodCliente = (Label) wdwAgregar.getFellow("lblCodCliente");
		Label lblNomCliente = (Label) wdwAgregar.getFellow("lblNomCliente");
		Label lblNitCliente = (Label) wdwAgregar.getFellow("lblNitCliente");
		Label lblDireccion = (Label) wdwAgregar.getFellow("lblDireccion");
		Bandbox bndCliente = (Bandbox)wdwAgregar.getFellow("bndCliente");
		
		bndCliente.setText("");
		lblCodCliente.setValue("XXXXXXXXXXX");
		lblNomCliente.setValue("XXXXXXXXXXX");
		lblNitCliente.setValue("XXXXXXXXXXX");
		lblDireccion.setValue("XXXXXXXXXXX");
		
		
		tabInfoCuenta.setVisible(false);
		Button btnCrear 		 = (Button) wdwAgregar.getFellow("btnCrear");
		Button btnValidaTarjeta	 = (Button) wdwAgregar.getFellow("btnValidaTarjeta");
		
		txtQuestNombre.setText("");
		txtQuestNit.setText("");
		decTarjeta.setText("");
		txtNombre.setText("");
		decCcv.setText("");
		spnMes.setText("");
		spnAnio.setText("");
		decIngresoMes.setText("");
		
		decTarjeta.setReadonly(false);
		txtNombre.setReadonly(false);
		decCcv.setReadonly(false);
		spnMes.setDisabled(false);
		spnAnio.setDisabled(false);
		
		btnCrear.setDisabled(false);
		btnCrear.removeEventListener("onClick", onClick_btnCrearCuenta);
		btnCrear.addEventListener("onClick", onClick_btnCrearCuenta);
		
		btnValidaTarjeta.removeEventListener("onClick", onClick_btnValidaTarjeta);
		btnValidaTarjeta.addEventListener("onClick", onClick_btnValidaTarjeta);
		
		dato = null;
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxCliente(conn, null);
			
			wdwAgregar.removeAttribute(Conf.KEY_APERTURA);
			wdwAgregar.setAttribute(Conf.KEY_APERTURA, Conf.KEY_NUEVO);
			wdwAgregar.doHighlighted();
			
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$btnFiltro(){
		Button btnBuscar = (Button) wdwFiltro.getFellow("btnBuscar");
		
		btnBuscar.removeEventListener("onClick", onClick_btnBuscar);
		btnBuscar.addEventListener("onClick", onClick_btnBuscar);
		wdwFiltro.doHighlighted();
	}
	
	public void onClick$btnLimpiar(){
		Textbox txtNomCliente = (Textbox) wdwFiltro.getFellow("txtNomCliente");
		Textbox txtNitCliente = (Textbox) wdwFiltro.getFellow("txtNitCliente");
		txtNomCliente.setText("");
		txtNitCliente.setText("");
		
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxCuenta(conn, null);
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void onClick$mniDesembolsos(){
		Button btnPago = (Button) wdwPagos.getFellow("btnPago");
		btnPago.removeEventListener("onClick", onClick_btnPago);
		btnPago.addEventListener("onClick", onClick_btnPago);
		
		CuentaORM orm = lbxCuenta.getSelectedItem().getValue();
		
		Connection conn = null;
		try{
			conn = obtieneConexion();
			cargaLbxDesembolso(conn, orm.getDwaccuentaid());

			wdwPagos.doHighlighted();
		}catch(SQLException e){
			log.error(e.getMessage(), e);
			mensajeInconveniente();
		}finally{
			cerrarConexion(conn);
		}
	}
	
	public void cargaLbxDesembolso(Connection conn, String cuentaid) throws SQLException {
		Listbox lbxDesembolso = (Listbox)wdwPagos.getFellow("lbxDesembolso");
		lbxDesembolso.setEmptyMessage("No existen créditos realizados para esta cuenta.");
		lbxDesembolso.getItems().clear();
		
		List<DesembolsoORM> lista = dao.obtieneListaDesembolso(conn, cuentaid);
		Iterator<DesembolsoORM> iList = lista.iterator();
		
		Listitem item;
		Listcell cell;
		DesembolsoORM obj;
		while(iList.hasNext()){
			obj = iList.next();
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			
			cell = new Listcell(obj.getDescripcion());
			cell.setParent(item);
			
			cell = new Listcell(obj.getPendiente());
			cell.setParent(item);
			
			cell = new Listcell(obj.getPagado());
			cell.setParent(item);
			
			cell = new Listcell(obj.getCuotas_pendientes());
			cell.setParent(item);
			
			cell = new Listcell(obj.getCuotas_pagadas());
			cell.setParent(item);
			
			cell = new Listcell(obj.getFecha());
			cell.setParent(item);
			
			item.setValue(obj);
			item.setParent(lbxDesembolso);
		}	
	}
	
	public void cargaLbxCliente(Connection conn, ClienteORM filtro) throws SQLException {
		Listbox lbxCliente = (Listbox)wdwAgregar.getFellow("lbxCliente");
		lbxCliente.setEmptyMessage("No existen clientes creados.");
		lbxCliente.getItems().clear();
		
		List<ClienteORM> lista = dao.obtieneClientes(conn, filtro);
		Iterator<ClienteORM> iList = lista.iterator();
		
		Listitem item;
		Listcell cell;
		while(iList.hasNext()){
			ClienteORM obj = iList.next();
			item = new Listitem();
			
			cell = new Listcell("");
			cell.setParent(item);
			
			cell = new Listcell(obj.getCodigo());
			cell.setParent(item);
			
			cell = new Listcell(obj.getNombres().concat(obj.getApellidos()));
			cell.setParent(item);
			
			cell = new Listcell(obj.getNit());
			cell.setParent(item);
			
			item.setValue(obj);
			item.removeEventListener("onDoubleClick", onDoubleClick_lbxSelectCliente);
			item.addEventListener("onDoubleClick", onDoubleClick_lbxSelectCliente);
			item.setParent(lbxCliente);
		}	
	}
	
	public void cargaLbxCuenta(Connection conn, CuentaORM filtro) throws SQLException {
		lbxCuenta.setEmptyMessage("No existen cuentas en el sistema.");
		lbxCuenta.getItems().clear();
		
		List<CuentaORM> listaCuenta = dao.obtieneDatosCuenta(conn, filtro, 2);
		Iterator<CuentaORM> iList = listaCuenta.iterator();
		CuentaORM obj;
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
			
			cell = new Listcell(obj.getTipo_cuenta());
			cell.setParent(item);
			
			cell = new Listcell(obj.getCategoria());
			cell.setParent(item);
			
			cell = new Listcell(obj.getFecha_creado());
			cell.setParent(item);
			
			item.setValue(obj);
			item.setContext("popMenu");
			item.setTooltiptext("Doble click para acceder a la información y click derecho para mas opciones");
			item.removeEventListener("onDoubleClick", onDoubleClick_lbxCuenta);
			item.addEventListener("onDoubleClick", onDoubleClick_lbxCuenta);
			item.setParent(lbxCuenta);
		}
	}
	
	EventListener<Event> onClick_btnPago = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxDesembolso = (Listbox) wdwPagos.getFellow("lbxDesembolso");
			if(lbxDesembolso.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar un crédito para aplicar pago");
				return;
			}
			Textbox txtDescripcion = (Textbox) wdwPagar.getFellow("txtDescripcion");
			Decimalbox txtMonto = (Decimalbox) wdwPagar.getFellow("txtMonto");
			Combobox cmbTarjeta = (Combobox) wdwPagar.getFellow("cmbTarjeta");
			CuentaORM cuenta = lbxCuenta.getSelectedItem().getValue();
			Radiogroup rdMetodoPago = (Radiogroup) wdwPagar.getFellow("rdMetodoPago");
			
			Button btnAceptar = (Button)wdwPagar.getFellow("btnAceptar");
			
			btnAceptar.removeEventListener("onClick", onClick_btnAceptar);
			btnAceptar.addEventListener("onClick", onClick_btnAceptar);
			
			DesembolsoORM orm = lbxDesembolso.getSelectedItem().getValue();
			
			txtDescripcion.setText("");
			BigDecimal pendiente = new BigDecimal(orm.getPendiente());
			BigDecimal pagado = new BigDecimal(orm.getPagado());
			BigDecimal cuotasPe = new BigDecimal(orm.getCuotas_pendientes());
			BigDecimal cuotasPa = new BigDecimal(orm.getCuotas_pagadas());
			BigDecimal cuota = cuotasPe.add(cuotasPa);
			cuota = cuota.setScale(2, RoundingMode.HALF_UP);
			BigDecimal total = pendiente.add(pagado);
			total = total.setScale(2, RoundingMode.HALF_UP);
			BigDecimal cuotaPagar = total.divide(cuota);
			cuotaPagar = cuotaPagar.setScale(2, RoundingMode.HALF_UP);
			txtMonto.setText(String.valueOf(cuotaPagar));
			cmbTarjeta.setSelectedItem(null);
			cmbTarjeta.setText("");
			cmbTarjeta.getItems().clear();

			rdMetodoPago.removeEventListener("onCheck", onCheck_metodoPago);
			rdMetodoPago.addEventListener("onCheck", onCheck_metodoPago);
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				List<TarjetaORM> tarjeta = dao.obtieneTarjetaCliente(conn, cuenta.getNit());
				if(!tarjeta.isEmpty()){
					TarjetaORM ormTarjeta = tarjeta.get(0);
					Comboitem item = new Comboitem();
					item.setLabel(ormTarjeta.getNombre_tarjeta().concat(" - ".concat(ormTarjeta.getNumero_tarjeta())));
					item.setValue(ormTarjeta);
					item.setParent(cmbTarjeta);
				}
				wdwPagar.doHighlighted();
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onCheck_metodoPago = new EventListener<Event>() {
		public void onEvent(Event event) {
			Radiogroup rdMetodoPago = (Radiogroup) wdwPagar.getFellow("rdMetodoPago");
			Combobox cmbTarjeta = (Combobox) wdwPagar.getFellow("cmbTarjeta");
			boolean estado = false;
			
			if(rdMetodoPago.getSelectedItem().getLabel().equals("Efectivo")){
				estado = true;
				cmbTarjeta.setSelectedItem(null);
				cmbTarjeta.setText("");
			}
			cmbTarjeta.setDisabled(estado);
		}
	};
	
	EventListener<Event> onClick_btnAceptar = new EventListener<Event>() {
		public void onEvent(Event event) {
			Textbox txtDescripcion = (Textbox) wdwPagar.getFellow("txtDescripcion");
			Decimalbox txtMonto = (Decimalbox) wdwPagar.getFellow("txtMonto");
			Combobox cmbTarjeta = (Combobox) wdwPagar.getFellow("cmbTarjeta");
			Radiogroup rdMetodoPago = (Radiogroup)wdwPagar.getFellow("rdMetodoPago");
			Listbox lbxDesembolso = (Listbox) wdwPagos.getFellow("lbxDesembolso");
			
			if("".equals(txtDescripcion.getText())){
				mostrarMensaje("Debe ingresar una descripción.");
				return;
			}
			if(rdMetodoPago.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar el método de pago.");
				return;
			}
			if(!rdMetodoPago.getSelectedItem().getLabel().equals("Efectivo") &&
					cmbTarjeta.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar la tarjeta de crédito para realizar el pago.");
				return;
			}
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				String cobro = "PAGO EXITOSO";
				if(!rdMetodoPago.getSelectedItem().getLabel().equals("Efectivo")){
					cobro = cobroTarjeta(conn, 
							cmbTarjeta.getSelectedItem().getValue(), 
							txtMonto.getValue().intValue());
				}
				if("PAGO EXITOSO".equals(cobro)){
					
					dao.creaNotaDebito(conn, 
							txtMonto.getText(),
							txtDescripcion.getText(),
							((DesembolsoORM) lbxDesembolso.getSelectedItem().getValue()).getDwdidesembolsoid(),
							((CuentaORM) lbxCuenta.getSelectedItem().getValue()).getDwaccuentaid());
					mostrarMensaje("La nota de débito fue realizada correctamente");
				}
				cargaLbxDesembolso(conn, ((CuentaORM) lbxCuenta.getSelectedItem().getValue()).getDwaccuentaid());
				wdwPagar.setVisible(false);
				
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			} catch (AxisFault e) {
				log.error(e.getMessage(), e);
				mostrarMensaje("Ocurrió un inconveniente al realizar la validación de la tarjeta de crédito, "
						+ "consulte a su administrador para más detalles.");
			} catch (RemoteException e) {
				log.error(e.getMessage(), e);
				mostrarMensaje("Ocurrió un inconveniente al realizar la validación de la tarjeta de crédito, "
						+ "consulte a su administrador para más detalles.");
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	public String cobroTarjeta(Connection conn, TarjetaORM orm, Integer monto) throws SQLException, AxisFault, RemoteException {
		ValidaTC dato = new ValidaTC();
		dato.setArg0(orm.getNumero_tarjeta());
		dato.setArg1(orm.getNombre_tarjeta());
		dato.setArg2(orm.getCodigo_seguridad());
		dato.setArg3(Integer.parseInt(orm.getMes_vencimiento()));
		dato.setArg4(Integer.parseInt(orm.getAnio_vencimiento()));
		dato.setArg5(monto);
		
		String endpoint = dao.obtieneParametro(conn, "ENDPOINT_VALIDATARJETA");
		TarjetaWS validaTarjeta = new TarjetaWS();
		String validacion = validaTarjeta.realizaCobro(endpoint, dato);
		
		mostrarMensaje("La validación de tarjeta devolvió el siguiente mensaje: " + validacion);
		
		return validacion;
	}
	
	EventListener<Event> onClick_btnBuscar = new EventListener<Event>() {
		public void onEvent(Event event) {
			Textbox txtNomCliente = (Textbox) wdwFiltro.getFellow("txtNomCliente");
			Textbox txtNitCliente = (Textbox) wdwFiltro.getFellow("txtNitCliente");
			
			if("".equals(txtNomCliente.getText()) && "".equals(txtNitCliente.getText())){
				mostrarMensaje("No ha ingresado ningun filtro.");
				return;
			}
			CuentaORM filtro = new CuentaORM();
			filtro.setNombre_cuenta(txtNomCliente.getText());
			filtro.setNit(txtNitCliente.getText());
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				cargaLbxCuenta(conn, filtro);
				wdwFiltro.setVisible(false);
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onDoubleClick_lbxCuenta = new EventListener<Event>() {
		public void onEvent(Event event) {
			CuentaORM orm = lbxCuenta.getSelectedItem().getValue();
			Tab tabInfoCliente = (Tab) wdwInfo.getFellow("tabInfoCliente");
			
			Label lblNomCliente = (Label) wdwInfo.getFellow("lblNomCliente");
			Label lblNitCliente = (Label) wdwInfo.getFellow("lblNitCliente");
			
			Label lblCuenta = (Label) wdwInfo.getFellow("lblCuenta");
			Label lblTipoCuenta = (Label) wdwInfo.getFellow("lblTipoCuenta");
			Label lblCategoría = (Label) wdwInfo.getFellow("lblCategoría");
			Label lblInteres = (Label) wdwInfo.getFellow("lblInteres");
			Label lblDisponible = (Label) wdwInfo.getFellow("lblDisponible");
			Label lblFecCorte = (Label) wdwInfo.getFellow("lblFecCorte");
			Label lblFecPago = (Label) wdwInfo.getFellow("lblFecPago");
			
			tabInfoCliente.setSelected(true);
			lblNomCliente.setValue(orm.getNombre_cuenta());
			lblNitCliente.setValue(orm.getNit());
			
			lblCuenta.setValue(orm.getNum_cuenta());
			lblTipoCuenta.setValue(orm.getTipo_cuenta());
			lblCategoría.setValue(orm.getCategoria());
			lblInteres.setValue(orm.getTasa_interes() + "%");
			lblDisponible.setValue("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(orm.getDisponible())));
			lblFecCorte.setValue(orm.getFecha_corte() + " de cada mes");
			lblFecPago.setValue(orm.getFecha_pago() + " de cada mes");
			wdwInfo.doHighlighted();
		}
	};
	
	EventListener<Event> onClick_btnCrearCuenta = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxCliente = (Listbox) wdwAgregar.getFellow("lbxCliente");
			Decimalbox decIngresoMes = (Decimalbox) wdwAgregar.getFellow("decIngresoMes");
			
			if(lbxCliente.getSelectedItem() == null){
				mostrarMensaje("Debe seleccionar un cliente.");
				return;
			}
			if(decIngresoMes.getValue() == null){
				mostrarMensaje("Debe ingresar el monto de ingresos mensuales.");
				return;
			}
			Object valida = wdwAgregar.getAttribute("tarjeta");
			if(valida == null){
				mostrarMensaje("Aun no ha relizado la validación de la tarjeta de crédito.");
				return;
			}else if(!"TARJETA EXISTENTE".equals(valida.toString())){
				mostrarMensaje("La tarjeta de crédito es inválida.");
				return;
			}
			ClienteORM cliente = lbxCliente.getSelectedItem().getValue();
			Label lblCuenta = (Label)wdwAgregar.getFellow("lblCuenta");
			Label lblTipoCuenta = (Label)wdwAgregar.getFellow("lblTipoCuenta");
			Label lblCategoría = (Label)wdwAgregar.getFellow("lblCategoría");
			Label lblInteres = (Label)wdwAgregar.getFellow("lblInteres");
			Label lblDisponible = (Label)wdwAgregar.getFellow("lblDisponible");
			Label lblFecCorte = (Label)wdwAgregar.getFellow("lblFecCorte");
			Label lblFecPago = (Label)wdwAgregar.getFellow("lblFecPago");
			Tab tabInfoCuenta = (Tab)wdwAgregar.getFellow("tabInfoCuenta");
			Button btnCrear = (Button)wdwAgregar.getFellow("btnCrear");
			
			Connection conn = null;
			try{
				conn = obtieneConexion();
				String solicitudid = dao.insertaSolicitud(conn, cliente, decIngresoMes.getValue(), "0");
				
				if(dato != null){
					dao.insertaDatosTarjeta(conn, dato, cliente.getNit());
				}
				
				String creaCuenta = dao.creaCuenta(conn, solicitudid);
				if("OK".equals(creaCuenta)){
					mostrarMensaje("La cuenta ha sido creada, puede visualizar su información.");
					
					CuentaORM datoCuenta = dao.obtieneDatosCuenta(conn, null, 1).get(0);
					if(datoCuenta != null){
						lblCuenta.setValue(datoCuenta.getNum_cuenta());
						lblTipoCuenta.setValue(datoCuenta.getTipo_cuenta());
						lblCategoría.setValue(datoCuenta.getCategoria());
						lblInteres.setValue(datoCuenta.getTasa_interes() + "%");
						lblDisponible.setValue("Q " + Conf.DECIMAL_FORMAT.format(new BigDecimal(datoCuenta.getDisponible())));
						lblFecCorte.setValue(datoCuenta.getFecha_corte() + " de cada mes");
						lblFecPago.setValue(datoCuenta.getFecha_pago() + " de cada mes");
						tabInfoCuenta.setVisible(true);
						btnCrear.setDisabled(true);
					}
					cargaLbxCuenta(conn, null);
				}else{
					mostrarMensaje(creaCuenta);
				}
				
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onClick_btnValidaTarjeta = new EventListener<Event>() {
		public void onEvent(Event event) {
			Decimalbox decTarjeta = (Decimalbox) wdwAgregar.getFellow("decTarjeta");
			Textbox txtNombre = (Textbox) wdwAgregar.getFellow("txtNombre");
			Decimalbox decCcv =	(Decimalbox) wdwAgregar.getFellow("decCcv");
			Spinner spnMes = (Spinner) wdwAgregar.getFellow("spnMes");
			Spinner spnAnio = (Spinner) wdwAgregar.getFellow("spnAnio");
			
			if(decTarjeta.getValue() == null){
				mostrarMensaje("Ingrese el número de tarjeta.");
				return;
			}else if(new BigDecimal(0).compareTo(decTarjeta.getValue()) == 1){
				mostrarMensaje("El número de tarjeta es inválido.");
				return;
			}
			if("".equals(txtNombre.getText().trim())){
				mostrarMensaje("Debe ingresar el nombre que aparece en la tarjeta.");
				return;
			}
			if(decCcv.getValue() == null){
				mostrarMensaje("Ingrese el código de seguridad.");
				return;
			}else if(new BigDecimal(0).compareTo(decCcv.getValue()) == 1){
				mostrarMensaje("El código de seguridad es inválido.");
				return;
			}
			if(spnMes.getValue() == null){
				mostrarMensaje("Ingrese el mes de vencimiento de la tarjeta.");
				return;
			}
			if(spnAnio.getValue() == null){
				mostrarMensaje("Ingrese el año de vencimiento de la tarjeta.");
				return;
			}
			
			dato = new ExisteTC();
			dato.setArg0(decTarjeta.getText());
			dato.setArg1(txtNombre.getText());
			dato.setArg2(decCcv.getText());
			dato.setArg3(spnMes.getValue());
			dato.setArg4(spnAnio.getValue());
			
			Connection conn = null;
			
			try{
				conn = obtieneConexion();
				
				String validaExistencia = dao.validaTarjeta(conn, decTarjeta.getText());
				if(!"0".equals(validaExistencia)){
					throw new ExcepcionUsuario("La tarjeta ya existe en el sistema.");
				}
				
				String endpoint = dao.obtieneParametro(conn, "ENDPOINT_VALIDATARJETA");
				TarjetaWS validaTarjeta = new TarjetaWS();
				String validacion = validaTarjeta.validaTarjeta(endpoint, dato);
				
				mostrarMensaje("La validación de tarjeta devolvió el siguiente mensaje: " + validacion);
				
				wdwAgregar.removeAttribute("tarjeta");
				wdwAgregar.setAttribute("tarjeta", validacion);
				if(!"TARJETA EXISTENTE".equals(validacion.toUpperCase())){
					dato = null;
				}else{
					decTarjeta.setReadonly(true);
					txtNombre.setReadonly(true);
					decCcv.setReadonly(true);
					spnMes.setDisabled(true);
					spnAnio.setDisabled(true);
				}
				
			}catch(ExcepcionUsuario e){
				mostrarMensaje(e.getMessage());
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				mensajeInconveniente();
			} catch (AxisFault e) {
				log.error(e.getMessage(), e);
				mostrarMensaje("Ocurrió un inconveniente al realizar la validación de la tarjeta de crédito, "
						+ "consulte a su administrador para más detalles.");
			} catch (RemoteException e) {
				log.error(e.getMessage(), e);
				mostrarMensaje("Ocurrió un inconveniente al realizar la validación de la tarjeta de crédito, "
						+ "consulte a su administrador para más detalles.");
			}finally{
				cerrarConexion(conn);
			}
		}
	};
	
	EventListener<Event> onDoubleClick_lbxSelectCliente = new EventListener<Event>() {
		public void onEvent(Event event) {
			Listbox lbxCliente  = (Listbox) wdwAgregar.getFellow("lbxCliente");
			Bandbox bndCliente  = (Bandbox) wdwAgregar.getFellow("bndCliente");
			Label lblCodCliente = (Label) wdwAgregar.getFellow("lblCodCliente");
			Label lblNomCliente = (Label) wdwAgregar.getFellow("lblNomCliente");
			Label lblNitCliente = (Label) wdwAgregar.getFellow("lblNitCliente");
			Label lblDireccion  = (Label) wdwAgregar.getFellow("lblDireccion");
			
			ClienteORM cliente = lbxCliente.getSelectedItem().getValue();
			lblCodCliente.setValue(cliente.getCodigo());
			lblNomCliente.setValue(cliente.getNombres().concat(cliente.getApellidos()));
			lblNitCliente.setValue(cliente.getNit());
			lblDireccion.setValue(cliente.getDireccion());
			bndCliente.setText(cliente.getNombres().concat(cliente.getApellidos()));
			bndCliente.close();
		}
	};
	
}
