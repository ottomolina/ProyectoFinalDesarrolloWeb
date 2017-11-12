package com.umg.ws;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.umg.ws.ValidaMontoStub.ExisteTC;
import com.umg.ws.ValidaMontoStub.ExisteTCE;
import com.umg.ws.ValidaMontoStub.ExisteTCResponse;
import com.umg.ws.ValidaMontoStub.ValidaTC;
import com.umg.ws.ValidaMontoStub.ValidaTCE;
import com.umg.ws.ValidaMontoStub.ValidaTCResponse;

public class TarjetaWS {
	
	public String validaTarjeta(String endpoint, ExisteTC datosTarjeta) 
			throws AxisFault, RemoteException{
		
		ValidaMontoStub servicioTarjeta = new ValidaMontoStub(endpoint);
		ExisteTCE encapsulamiento = new ExisteTCE();
		encapsulamiento.setExisteTC(datosTarjeta);
		ExisteTCResponse response = servicioTarjeta.existeTC(encapsulamiento).getExisteTCResponse();
		return response.get_return();
	}
	
	public String realizaCobro(String endpoint, ValidaTC datosTarjeta)
		throws AxisFault, RemoteException{
	
		ValidaMontoStub servicioTarjeta = new ValidaMontoStub(endpoint);
		ValidaTCE encapsulamiento = new ValidaTCE();
		encapsulamiento.setValidaTC(datosTarjeta);
		ValidaTCResponse response = servicioTarjeta.validaTC(encapsulamiento).getValidaTCResponse();
		return response.get_return();
	}
	
}
