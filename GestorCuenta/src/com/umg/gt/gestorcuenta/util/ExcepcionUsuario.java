package com.umg.gt.gestorcuenta.util;

public class ExcepcionUsuario extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ExcepcionUsuario(){
		super();
	}
	
	public ExcepcionUsuario(String mensaje, Throwable causa){
		super(mensaje, causa);
	};		
	
	public ExcepcionUsuario(String mensaje){
		super(mensaje);
	};
}
