
/**
 * ValidaMontoCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.umg.ws;

    /**
     *  ValidaMontoCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ValidaMontoCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ValidaMontoCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ValidaMontoCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for validarCheque method
            * override this method for handling normal response from validarCheque operation
            */
           public void receiveResultvalidarCheque(
                    com.umg.ws.ValidaMontoStub.ValidarChequeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validarCheque operation
           */
            public void receiveErrorvalidarCheque(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validaTC method
            * override this method for handling normal response from validaTC operation
            */
           public void receiveResultvalidaTC(
                    com.umg.ws.ValidaMontoStub.ValidaTCResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validaTC operation
           */
            public void receiveErrorvalidaTC(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for existeTC method
            * override this method for handling normal response from existeTC operation
            */
           public void receiveResultexisteTC(
                    com.umg.ws.ValidaMontoStub.ExisteTCResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from existeTC operation
           */
            public void receiveErrorexisteTC(java.lang.Exception e) {
            }
                


    }
    