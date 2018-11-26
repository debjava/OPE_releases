-- -- *********************************** CREATE USER ********************************************** --  --

-- ***** Create Tablespace ****** --



CREATE TABLESPACE &tablespace_name
DATAFILE &Path-datafile_name SIZE 4000m;

/****************************************************************************************************************
 *  To create tablespace connect as sysdba									*
 *  Tablespace_name 	: Different name which dosen't exist					`		*
 *  Path-datafile_name	: Path where datafile should be created and name of a datafile with extension DAT	*
 *				ex( 'D:\datafile\dataf1.dbf' ) should be in single cods				*
 ****************************************************************************************************************/
  





CREATE USER &user_name IDENTIFIED BY &user_password
DEFAULT TABLESPACE &tablespace_name
quota 3700m ON &tablespace_name;


/****************************************************************************************************************
 *  user_name		: New user name ( or Schema name) to be created (should no match the existing user name)*
 *  user_password	: Password given for the user								*
 *  Tablespace_name     : Tablespace name which we have created or(tablesspace name where space is avaliable)	*
 ****************************************************************************************************************/







GRANT CREATE TABLE, CREATE SESSION, CREATE PROCEDURE, CREATE SEQUENCE, CREATE TRIGGER, CREATE TYPE TO &user_name;


/****************************************************************************************************************
 * To grant privileges for new users										*
 * user_name 		: user name to which this privileges should be granted 					*
 ****************************************************************************************************************


