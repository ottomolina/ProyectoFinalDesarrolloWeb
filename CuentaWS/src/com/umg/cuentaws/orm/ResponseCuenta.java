package com.umg.cuentaws.orm;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ResponseCuentaType")
public class ResponseCuenta {
	private List<DatosCuenta> datosCuenta;
	private List<String> errores;
	
	public List<DatosCuenta> getDatosCuenta() {
		return datosCuenta;
	}
	public void setDatosCuenta(List<DatosCuenta> datosCuenta) {
		this.datosCuenta = datosCuenta;
	}
	public List<String> getErrores() {
		return errores;
	}
	public void setErrores(List<String> errores) {
		this.errores = errores;
	}
}
