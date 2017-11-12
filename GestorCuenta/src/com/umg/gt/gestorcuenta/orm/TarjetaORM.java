package com.umg.gt.gestorcuenta.orm;

public class TarjetaORM {
	private String numero_tarjeta;
	private String nombre_tarjeta;
	private String codigo_seguridad;
	private String mes_vencimiento;
	private String anio_vencimiento;
	private String nit_cliente;
	
	public String getNumero_tarjeta() {
		return numero_tarjeta;
	}
	public void setNumero_tarjeta(String numero_tarjeta) {
		this.numero_tarjeta = numero_tarjeta;
	}
	public String getNombre_tarjeta() {
		return nombre_tarjeta;
	}
	public void setNombre_tarjeta(String nombre_tarjeta) {
		this.nombre_tarjeta = nombre_tarjeta;
	}
	public String getCodigo_seguridad() {
		return codigo_seguridad;
	}
	public void setCodigo_seguridad(String codigo_seguridad) {
		this.codigo_seguridad = codigo_seguridad;
	}
	public String getMes_vencimiento() {
		return mes_vencimiento;
	}
	public void setMes_vencimiento(String mes_vencimiento) {
		this.mes_vencimiento = mes_vencimiento;
	}
	public String getAnio_vencimiento() {
		return anio_vencimiento;
	}
	public void setAnio_vencimiento(String anio_vencimiento) {
		this.anio_vencimiento = anio_vencimiento;
	}
	public String getNit_cliente() {
		return nit_cliente;
	}
	public void setNit_cliente(String nit_cliente) {
		this.nit_cliente = nit_cliente;
	}
}
