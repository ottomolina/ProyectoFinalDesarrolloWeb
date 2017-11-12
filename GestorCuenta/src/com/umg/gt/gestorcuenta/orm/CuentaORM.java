package com.umg.gt.gestorcuenta.orm;

public class CuentaORM {
	private String dwaccuentaid;
	private String num_cuenta;
	private String tipo_cuenta;
	private String categoria;
	private String tasa_interes;
	private String disponible;
	private String fecha_corte;
	private String fecha_pago;
	private String fecha_creado;
	private String nombre_cuenta;
	private String nit;
	
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getDwaccuentaid() {
		return dwaccuentaid;
	}
	public void setDwaccuentaid(String dwaccuentaid) {
		this.dwaccuentaid = dwaccuentaid;
	}
	public String getNum_cuenta() {
		return num_cuenta;
	}
	public void setNum_cuenta(String num_cuenta) {
		this.num_cuenta = num_cuenta;
	}
	public String getTipo_cuenta() {
		return tipo_cuenta;
	}
	public void setTipo_cuenta(String tipo_cuenta) {
		this.tipo_cuenta = tipo_cuenta;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getTasa_interes() {
		return tasa_interes;
	}
	public void setTasa_interes(String tasa_interes) {
		this.tasa_interes = tasa_interes;
	}
	public String getDisponible() {
		return disponible;
	}
	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}
	public String getFecha_corte() {
		return fecha_corte;
	}
	public void setFecha_corte(String fecha_corte) {
		this.fecha_corte = fecha_corte;
	}
	public String getFecha_pago() {
		return fecha_pago;
	}
	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}
	public String getFecha_creado() {
		return fecha_creado;
	}
	public void setFecha_creado(String fecha_creado) {
		this.fecha_creado = fecha_creado;
	}
	public String getNombre_cuenta() {
		return nombre_cuenta;
	}
	public void setNombre_cuenta(String nombre_cuenta) {
		this.nombre_cuenta = nombre_cuenta;
	}
}
