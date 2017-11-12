
CREATE TABLESPACE ACCOUNT_TBS
DATAFILE '/oraclexe/account_tbs.dbf'
SIZE 100M AUTOEXTEND ON NEXT 200K MAXSIZE 500M;

CREATE USER APP_CUENTA
IDENTIFIED BY APP_CUENTA_123;

GRANT CREATE TRIGGER TO APP_CUENTA;

GRANT CREATE PROCEDURE TO APP_CUENTA;

GRANT CREATE SEQUENCE TO APP_CUENTA;

GRANT CREATE SYNONYM TO APP_CUENTA;

GRANT CREATE TABLE TO APP_CUENTA;

GRANT CREATE TYPE TO APP_CUENTA;

GRANT CREATE SESSION TO APP_CUENTA;

GRANT CONNECT TO APP_CUENTA;

ALTER USER APP_CUENTA QUOTA 100M ON ACCOUNT_TBS;
 
GRANT UNLIMITED TABLESPACE TO APP_CUENTA;