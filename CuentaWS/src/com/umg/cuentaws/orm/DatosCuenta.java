package com.umg.cuentaws.orm;

public class DatosCuenta {
	private String codigoCliente;
	private String nombres;
	private String apellidos;
	private String direccion;
	private Integer telefono;
	private String numCuenta;
	private String categoriaCuenta;
	private String tipoCuenta;
	private String fechaCreado;
	
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Integer getTelefono() {
		return telefono;
	}
	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}
	public String getNumCuenta() {
		return numCuenta;
	}
	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}
	public String getCategoriaCuenta() {
		return categoriaCuenta;
	}
	public void setCategoriaCuenta(String categoriaCuenta) {
		this.categoriaCuenta = categoriaCuenta;
	}
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public String getFechaCreado() {
		return fechaCreado;
	}
	public void setFechaCreado(String fechaCreado) {
		this.fechaCreado = fechaCreado;
	}
}
