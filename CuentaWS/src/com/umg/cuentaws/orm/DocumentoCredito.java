package com.umg.cuentaws.orm;

import java.math.BigDecimal;

public class DocumentoCredito extends Documento {
	private BigDecimal num_cuota;

	public BigDecimal getNum_cuota() {
		return num_cuota;
	}
	public void setNum_cuota(BigDecimal num_cuota) {
		this.num_cuota = num_cuota;
	}
}
