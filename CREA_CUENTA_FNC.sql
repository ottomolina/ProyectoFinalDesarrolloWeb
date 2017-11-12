
CREATE OR REPLACE FUNCTION DW_CREA_CUENTA_FNC(SOLICITUD_ID NUMBER)
RETURN VARCHAR2
IS
    CLIENTEID           DW_AL_CUENTA.DWACCLIENTEID%TYPE;
    CATEGORIAID         DW_AL_CUENTA.DWCACATEGORIAID%TYPE;
    TIPOCUENTAID        DW_AL_CUENTA.DWATTIPOCUENTAID%TYPE;
    V_NUM_CUENTA        DW_AL_CUENTA.DWACNUMCUENTA%TYPE;
    INGRESO_MENSUAL     DW_AL_SOLICITUD.DWREINGRESOMENSUAL%TYPE;
    CREDITO_MAXIMO      DW_CAT_CATEGORIA.DWCACREDITOMAXIMO%TYPE;
    
    CUENTAID            NUMBER;
    INTERES             NUMBER;
    V_CONTEO            NUMBER;
    
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    
    BEGIN
       SELECT COUNT (1), DWCACATEGORIAID, MAX (DWCACREDITOMAXIMO)
         INTO V_CONTEO, CATEGORIAID, CREDITO_MAXIMO
         FROM DW_CAT_CATEGORIA
       HAVING (SELECT (DWREINGRESOMENSUAL)
                 FROM DW_AL_SOLICITUD
                WHERE DWRESOLICITUDID = 2) BETWEEN MAX (DWCACREDITOMAXIMO) - 1000
                                              AND MAX (DWCACREDITOMAXIMO)
     GROUP BY DWCACATEGORIAID;
        
        IF(V_CONTEO = 0) THEN
            RETURN 'El cliente no aplica a ninguna categoría en el sistema.';
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            RETURN 'El cliente no aplica a ninguna categoría en el sistema.';
    END;
    
    BEGIN
        SELECT '4'||
               trunc(dbms_random.value(0,9))||''|| trunc(dbms_random.value(0,9))||
               trunc(dbms_random.value(0,9))||''|| trunc(dbms_random.value(0,9))||
               trunc(dbms_random.value(0,9))||''|| trunc(dbms_random.value(0,9))||
               trunc(dbms_random.value(0,9))||''|| trunc(dbms_random.value(0,9))||
               trunc(dbms_random.value(0,9))||''|| trunc(dbms_random.value(0,9))
          INTO V_NUM_CUENTA
          FROM DUAL;
        
        SELECT NVL(MAX(DWACCUENTAID), 0) + 1
          INTO CUENTAID
          FROM DW_AL_CUENTA;
        
        INSERT INTO DW_AL_CUENTA(DWACCUENTAID,
                                 DWACNUMCUENTA,
                                 DWACFECHACREADO,
                                 DWACCLIENTEID,
                                 DWCACATEGORIAID,
                                 DWATTIPOCUENTAID)
        VALUES(    CUENTAID,
                V_NUM_CUENTA,
                SYSDATE,
                (SELECT DWRENIT FROM DW_AL_SOLICITUD WHERE DWRESOLICITUDID = SOLICITUD_ID),
                CATEGORIAID,
                2);
        COMMIT;
        RETURN 'OK';
    EXCEPTION
        WHEN OTHERS THEN
            RETURN 'No fue posible crear la cuenta, consulte a su administrador. '||sqlerrm;
    END;
   
    
EXCEPTION
    WHEN OTHERS THEN
    RETURN 'Ocurrió un error ' || SQLERRM;
END DW_CREA_CUENTA_FNC;