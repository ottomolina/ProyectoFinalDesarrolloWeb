package com.umg.gt.gestorcuenta.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

public class LoginCtrl extends ControladorBase {
	private static final long serialVersionUID = 1L;
	
	Window wdwCredenciales;
	Textbox txtUsuario;
	Textbox txtPass;
	
	@Override
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		
		wdwCredenciales.doHighlighted();
		
		Button btnIniciar = (Button) wdwCredenciales.getFellow("btnIniciar");
		Button btnRecordar = (Button) wdwCredenciales.getFellow("btnRecordar");
		
		txtUsuario = (Textbox) wdwCredenciales.getFellow("txtUsuario");
		txtPass = (Textbox) wdwCredenciales.getFellow("txtPass");
		
		btnIniciar.removeEventListener("onClick", onClick_btnIniciar);
		btnRecordar.removeEventListener("onClick", onClick_btnRecordar);
		
		btnIniciar.addEventListener("onClick", onClick_btnIniciar);
		btnRecordar.addEventListener("onClick", onClick_btnRecordar);
	}
	
	public void autentica(){
		/* GOMR despues de todo el vergueo de la autenticacion debe ejecutar esto  */
		/* onClick="Executions.sendRedirect(null)" */// para refrescar la pagina y que muestre el menu de la aplicacion
	}
	
	EventListener<Event> onClick_btnIniciar = new EventListener<Event>() {
		public void onEvent(Event event) {
			if(txtUsuario.getText().trim().equals("")){
				mostrarMensaje("Debe ingresar su usuario.");
				return;
			}
			if(txtPass.getText().trim().equals("")){
				mostrarMensaje("Debe ingresar la contraseña de su usuario.");
				return;
			}
			
		}
	};
	
	EventListener<Event> onClick_btnRecordar = new EventListener<Event>() {
		public void onEvent(Event event) {
			mostrarMensaje("Aun no definido...");
		}
	};
	
}

