package com.umg.cuentaws.orm;

import java.util.List;

public class ResponseNota {
	private List<Nota> documentos;
	private List<String> error;
	
	public List<Nota> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(List<Nota> documentos) {
		this.documentos = documentos;
	}
	public List<String> getError() {
		return error;
	}
	public void setError(List<String> error) {
		this.error = error;
	}
}
