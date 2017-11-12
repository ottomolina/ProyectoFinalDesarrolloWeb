package com.umg.gt.gestorcuenta.orm;

public class DesembolsoORM {
	private String descripcion;
	private String pendiente;
	private String pagado;
	private String cuotas_pagadas;
	private String cuotas_pendientes;
	private String fecha;
	private String dwdidesembolsoid;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPendiente() {
		return pendiente;
	}
	public void setPendiente(String pendiente) {
		this.pendiente = pendiente;
	}
	public String getPagado() {
		return pagado;
	}
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}
	public String getCuotas_pagadas() {
		return cuotas_pagadas;
	}
	public void setCuotas_pagadas(String cuotas_pagadas) {
		this.cuotas_pagadas = cuotas_pagadas;
	}
	public String getCuotas_pendientes() {
		return cuotas_pendientes;
	}
	public void setCuotas_pendientes(String cuotas_pendientes) {
		this.cuotas_pendientes = cuotas_pendientes;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDwdidesembolsoid() {
		return dwdidesembolsoid;
	}
	public void setDwdidesembolsoid(String dwdidesembolsoid) {
		this.dwdidesembolsoid = dwdidesembolsoid;
	}
}
