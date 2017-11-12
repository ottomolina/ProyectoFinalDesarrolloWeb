package com.umg.cuentaws.ctrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.umg.cuentaws.dao.FinanzasDAO;
import com.umg.cuentaws.dao.GeneralDao;
import com.umg.cuentaws.orm.CreacionNota;
import com.umg.cuentaws.orm.DatosCuenta;
import com.umg.cuentaws.orm.Documento;
import com.umg.cuentaws.orm.DocumentoCredito;
import com.umg.cuentaws.orm.FiltroCuenta;
import com.umg.cuentaws.orm.Nota;
import com.umg.cuentaws.orm.RequestNota;
import com.umg.cuentaws.orm.ResponseCuenta;
import com.umg.cuentaws.orm.ResponseNota;
import com.umg.cuentaws.util.ExcepcionUsuario;
import com.umg.cuentaws.util.Util;

import javax.jws.WebService;

@WebService()
public class CuentaWS extends ControladorBase {
	private final Logger log = Logger.getLogger(CuentaWS.class);
	private FinanzasDAO dao;
	
	public ResponseCuenta obtieneDatosCuenta(FiltroCuenta params){
		ResponseCuenta response = new ResponseCuenta();
		List<String> error = new ArrayList<String>();
		
		// GOMR 02/11/2017 �rea de validaciones de datos ingresados en el filtro
		/*if("".equals(params.getNombres())){
			error.add("Debe ingresar un valor en el campo nombres.");
		}else */if(Util.sinComilla(params.getNombres())){
			error.add("El campo nombres contiene caracteres inv�lidos.");
		}
		if(Util.sinComilla(params.getApellidos())){
			error.add("El campo apellidos contiene caracteres inv�lidos.");
		}
		
		if(params.getTelefono() != null && Util.isNumero(params.getTelefono())){
			error.add("Debe ingresar un valor num�rico en el campo tel�fono.");
		}
		if(params.getNumCuenta() != null && Util.sinComilla(params.getNumCuenta())){
			error.add("El campo n�mero de cuenta contiene caracteres inv�lidos.");
		}
		if(!"".equals(params.getCategoriaCuenta()) && Util.sinComilla(params.getCategoriaCuenta())){
			error.add("El campo categor�a de cuenta contiene caracteres inv�lidos.");
		}
		if(!"".equals(params.getTipoCuenta()) && Util.sinComilla(params.getTipoCuenta())){
			error.add("El campo tipo de cuenta contiene caracteres inv�lidos.");
		}
		// GOMR 02/11/2017 Finaliza �rea de validaciones de datos ingresados en el filtro
		
		if(error.size() != 0){
			response.setErrores(error);
		}else{
			dao = new FinanzasDAO();
			List<DatosCuenta> datos = null;
			Connection conn = null;
			try{
				conn = obtieneConexion();
				String path = new GeneralDao().obtieneParam(conn, "PATHLOG_WS");
				cargaPropiedades(path);
				
				datos = dao.obtieneDatosCuenta(conn, params);
			}catch(IOException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}finally{
				cerrarConexion(conn);
			}
			if(error.size() != 0){
				response.setErrores(error);
			}
			response.setDatosCuenta(datos);
		}
		
		return response;
	}
	
	public ResponseNota obtieneNotasDebito(RequestNota filtro){
		dao = new FinanzasDAO();
		ResponseNota nota = new ResponseNota();
		List<Nota> listaDocumento = new ArrayList<Nota>();
		List<String> error = new ArrayList<String>();
		
		if(Util.sinComilla(filtro.getNit())){
			error.add("El campo nit contiene caracteres inv�lidos.");
		}
		if(error.size() != 0){
			nota.setError(error);
		}else{
			Connection conn = null;
			try{
				conn = obtieneConexion();

				String path = new GeneralDao().obtieneParam(conn, "PATHLOG_WS");
				cargaPropiedades(path);
				listaDocumento = dao.obtieneDocumentos(conn, "2", filtro.getNit());
				
			}catch(IOException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}finally{
				cerrarConexion(conn);
			}
			
			if(error.size() != 0){
				nota.setError(error);
			}else{
				nota.setDocumentos(listaDocumento);
			}
		}
		return nota;
	}
	
	public ResponseNota obtieneNotasCredito(RequestNota filtro){
		dao = new FinanzasDAO();
		ResponseNota nota = new ResponseNota();
		List<Nota> listaDocumento = new ArrayList<Nota>();
		List<String> error = new ArrayList<String>();
		
		if(Util.sinComilla(filtro.getNit())){
			error.add("El campo nit contiene caracteres inv�lidos.");
		}
		if(error.size() != 0){
			nota.setError(error);
		}else{
			Connection conn = null;
			try{
				conn = obtieneConexion();

				String path = new GeneralDao().obtieneParam(conn, "PATHLOG_WS");
				cargaPropiedades(path);
				listaDocumento = dao.obtieneDocumentos(conn, "1", filtro.getNit());
				
			}catch(IOException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}finally{
				cerrarConexion(conn);
			}
			
			if(error.size() != 0){
				nota.setError(error);
			}else{
				nota.setDocumentos(listaDocumento);
			}
		}
		return nota;
	}
	
	public CreacionNota creaNotaDebito(Documento documento) {
		CreacionNota respuesta = new CreacionNota();
		List<String> error = new ArrayList<String>();
		if(documento == null){
			error.add("Debe enviar una especificaci�n de la nota de d�bito.");
		}else{
			if(documento.getNit() == null || "".equals(documento.getNit())){
				error.add("Debe enviar el nit del cliente.");
			}else if(Util.sinComilla(documento.getNit())){
				error.add("El nit del cliente contiene caracteres inv�lidos.");
			}
			if(documento.getNum_facura() == null || "".equals(documento.getNum_facura())){
				error.add("Debe enviar el numero de factura para la nota de d�bito.");
			}
			if(documento.getDescripcion() == null || "".equals(documento.getDescripcion())){
				error.add("Debe enviar una descripci�n para la nota de d�bito.");
			}else if(Util.sinComilla(documento.getDescripcion())){
				error.add("La descripci�n de la nota de d�bito contiene caracteres inv�lidos");
			}
			int validacion = new BigDecimal(0).compareTo(documento.getMonto());
			if(documento.getMonto() == null){
				error.add("Debe enviar el monto de la nota de d�bito.");
			}else if(validacion == 1 || validacion == 0){
				error.add("El monto de la nota de d�bito debe ser mayor a cero.");
			}
		}
		if(error.size() != 0){
			respuesta.setError(error);
		}else{
			Connection conn = null;
			try{
				conn = obtieneConexion();

				String path = dao.obtieneParam(conn, "PATHLOG_WS");
				cargaPropiedades(path);
				String validaCliente = dao.verificaClientePoseeCuenta(conn, documento.getNit());
				if("0".equals(validaCliente)){
					throw new ExcepcionUsuario("El cliente no posee cuenta corriente.");
				}
				String validaFacturaCliente = dao.verificaFacturaCliente(conn, documento.getNit(), documento.getNum_facura());
				if("0".equals(validaFacturaCliente)){
					throw new ExcepcionUsuario("El factura ingresada no est� asociada al cliente.");
				}
				String saldoFactura = dao.obtieneSaldo(conn, documento.getNit(), documento.getNum_facura());
				int validaMontosFactura = new BigDecimal(0).compareTo(new BigDecimal(saldoFactura));
				if(validaMontosFactura == 1 || validaMontosFactura == 0){
					throw new ExcepcionUsuario("La factura ingresada no tiene saldo pendiente.");
				}
				int creacion = dao.creaNota(conn, documento, "2"); // Creando documento de nota de d�bito
				if(creacion == 0){
					error.add("Ocurri� un error al crear la nota de d�bito, consulte a su administrador.");
				}else{
					respuesta.setRespuesta("OK");
				}
				
			}catch(ExcepcionUsuario e){
				error.add(e.getMessage());
			}catch(IOException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}finally{
				cerrarConexion(conn);
			}
			if(error.size() != 0){
				respuesta.setError(error);
			}else{
			}
		}
		
		return respuesta;
	}
	
	public CreacionNota creaNotaCredito(DocumentoCredito documento) {
		CreacionNota respuesta = new CreacionNota();
		List<String> error = new ArrayList<String>();
		if(documento == null){
			error.add("Debe enviar una especificaci�n de la nota de credito.");
		}else{
			if(documento.getNit() == null || "".equals(documento.getNit())){
				error.add("Debe enviar el nit del cliente.");
			}else if(Util.sinComilla(documento.getNit())){
				error.add("El nit del cliente contiene caracteres inv�lido.");
			}
			if(documento.getNum_facura() == null || "".equals(documento.getNum_facura())){
				error.add("Debe enviar el numero de factura para la nota de credito.");
			}else if(Util.sinComilla(documento.getNum_facura())){
				error.add("El nit del cliente contiene caracteres inv�lido.");
			}
			if(documento.getDescripcion() == null || "".equals(documento.getDescripcion())){
				error.add("Debe enviar una descripci�n para la nota de cr�dito.");
			}else if(Util.sinComilla(documento.getDescripcion())){
				error.add("La descripci�n de la nota de cr�dito contiene caracteres inv�lidos");
			}
			int validacion = new BigDecimal(0).compareTo(documento.getMonto());
			if(documento.getMonto() == null){
				error.add("Debe enviar el monto de la nota de cr�dito.");
			}else if(validacion == 1 || validacion == 0){
				error.add("El monto de la nota de cr�dito debe ser mayor a cero.");
			}
			int validacionCuota = new BigDecimal(0).compareTo(documento.getNum_cuota());
			if(documento.getNum_cuota() == null){
				error.add("Debe enviar el n�mero de cuotas para realizar el desembolso del cr�dito.");
			}else if(validacionCuota == 1 || validacionCuota == 0){
				error.add("El n�mero de cuotas de la nota de cr�dito debe ser mayor a cero.");
			}
		}
		if(error.size() != 0){
			respuesta.setError(error);
		}else{
			Connection conn = null;
			try{
				conn = obtieneConexion();

				String path = dao.obtieneParam(conn, "PATHLOG_WS");
				cargaPropiedades(path);
				
				String validaCliente = dao.verificaClientePoseeCuenta(conn, documento.getNit());
				if("0".equals(validaCliente)){
					throw new ExcepcionUsuario("El cliente no posee cuenta corriente.");
				}
				
				String saldoPendiente = dao.obtieneSaldo(conn, documento.getNit(), null);
				String validaCreditoCliente = dao.verificaCreditoDisponibleCuenta(conn, documento, saldoPendiente);
				if("0".equals(validaCreditoCliente)){
					throw new ExcepcionUsuario("No es posible otorgar m�s cr�dito a este cliente.");
				}
				
				int creacionDesembolso = dao.creaDesembolso(conn, documento);
				int creacionNota = 0;
				if(creacionDesembolso != 0){
					creacionNota = dao.creaNota(conn, documento, "1");
				}else{
					error.add("Ocurri� un error al realizar el desembolso para la nota de cr�dito, consulte a su administrador.");
				}
				if(creacionNota == 0){
					error.add("Ocurri� un error al crear la nota de cr�dito, consulte a su administrador.");
				}else{
					respuesta.setRespuesta("OK");
				}
				
			}catch(ExcepcionUsuario e){
				error.add(e.getMessage());
			}catch(IOException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(SQLException e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}catch(Exception e){
				log.error(e.getMessage(), e);
				error.add("Ocurri� un error inesperado, consulte a su administrador.");
			}finally{
				cerrarConexion(conn);
			}
			if(error.size() != 0){
				respuesta.setError(error);
			}else{
			}
		}
		return respuesta;
	}
	
}
