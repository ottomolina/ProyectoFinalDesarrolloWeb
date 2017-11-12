package com.umg.cuentaws.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
//	public static final String JNDI_CUENTA = "java:comp/env/conn_cuenta";
	public static final String JNDI_CUENTA = "conn_cuenta";
	
	// Variable que maneja la impresion en consola de la aplicacion
	public static boolean console = true;
	
	public static void mensajeConsola(String p_mensaje){
		if(console){
			System.out.println(p_mensaje);
		}
	}
	
	public static boolean sinComilla(String texto){
		if(texto == null){
			return false;
		}
		if(texto.contains("'")){
			return true;
		}
		return false;
	}
	
	public static boolean isEmailValido(String correo) {
		Pattern pat = Pattern.compile("^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$");
		Matcher mat = pat.matcher(correo);
		if (!mat.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean isNumero(String valor){
		if("".equals(valor.trim())){
			return false;
		}
		boolean ret = false;
		try{
			Integer.parseInt(valor);
		}catch(NumberFormatException e){
			ret = true;
		}
		return ret;
	}
	
}
