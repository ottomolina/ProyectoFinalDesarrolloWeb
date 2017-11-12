package com.umg.gt.gestorcuenta.controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.*;

public class MenuCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	
	Div dvInicio;
	Div dvInvMenu;
	Div dvOrdMenu;
	
	Tabbox tabPrincipal;
	
	public void doAfterCompose(Component comp){
		try{
			super.doAfterCompose(comp);
			
//			validaLogin();
		}catch(Exception e){
			
		}
	}
	
/************************************/	
/** METODOS DE NAVEGACION DEL MENU **/
/************************************/
	/** Cuentas corrientes del módulo */
	public void onDoubleClick$mncAccount(){
		String titulo = "Historial de Cuentas";
		String url    = "/zul/cuenta.zul";
		String id  	  = "incAccount";
		String ico    = "/img/icons/cuenta.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	/** Opción de  */
	public void onDoubleClick$mncPago(){
		String titulo = "Historia de Pagos";
		String url    = "/zul/notas.zul";
		String id  	  = "incNotas";
		String ico    = "/img/icons/pagos.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	/** Ordenes de Entrada */
	public void onDoubleClick$mncOrdIn(){
		String titulo = "Transacción Entrada";
		String url    = "/zul/transaccion.zul";
		String id  	  = "incOrdIn";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	/** Ordenes de Salida */
	public void onDoubleClick$mncOrdOut(){
		String titulo = "Transacción Salida";
		String url    = "/zul/transaccion.zul";
		String id  	  = "incOrdOut";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	/** Producto */
	public void onDoubleClick$mncInfProduct(){
		String titulo = "Productos";
		String url    = "/zul/producto.zul";
		String id  	  = "incProducto";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	public void onDoubleClick$mncInfALmacen(){
		String titulo = "Almacen";
		String url    = "/zul/almacen.zul";
		String id  	  = "incAlmacen";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	public void onDoubleClick$mncConfUser(){
		String titulo = "Configuracion Usuarios";
		String url    = "/zul/usuario.zul";
		String id  	  = "incConfUser";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	public void onDoubleClick$mncConfUserAdmin(){
		String titulo = "Configuracion Usuarios";
		String url    = "/zul/admin_user.zul";
		String id  	  = "incConfUser";
		String ico    = "/img/icons/almacen_16.png";
		if (!existeTab(id)){
			creaTab(id, titulo, url, ico);
		}
	}
	
	public void onDoubleClick$mncSalir(){
		desktop.removeAttribute("USER");
		Executions.sendRedirect(null);
	}
	
	
	public boolean existeTab(String id) {
		List<Tab> tabs = tabPrincipal.getTabs().getChildren();
		for (Tab tab : tabs){
			if (tab.getId().equals(id)) {
				tab.setSelected(true);
				return true;
			}
		}
		return false;
	}
	
	public void creaTab(String id, String titulo, String zul) {
		creaTab(id, titulo, zul, null);
	}
	
	@SuppressWarnings("deprecation")
	public void creaTab(String id, String titulo, String zul, String ico) {
		Tab tab = null;
		Tabpanel tabpanel = null;
		Include inc = null;
		Borderlayout bl = null;
		Center ce = null;
		Div div = null;
		
		tab = new Tab();
		tab.setId(id);
		tab.setLabel(titulo);
		tab.setClosable(true);
		tab.setParent(tabPrincipal.getTabs());
		tab.setSelected(true);
		tab.setStyle("height:36px; border:none; background:rgba(255, 255, 255, 0.5);");
		
		if (ico != null)
			tab.setImage(ico);
		
		tabpanel = new Tabpanel();
		tabpanel.setWidth("99.2%");
		tabpanel.setHeight("100%");
		tabpanel.setParent(tabPrincipal.getTabpanels());
		
		bl = new Borderlayout();
		ce = new Center();
		
		bl.setParent(tabpanel);
		bl.setStyle("background: transparent;");
		
		ce.setStyle("background: transparent;");
		ce.setParent(bl);
		ce.setBorder("none");
		ce.setFlex(true);
		ce.setAutoscroll(true);
		
		div = new Div();
		div.setAlign("center");
		div.setParent(ce);
		
		inc = new Include();
		inc.setParent(div);
		inc.setStyle("margin-top: 0px;");
		inc.setWidth("100%");
		inc.setHeight("100%");
		inc.setSrc(zul);
	}
	
}
