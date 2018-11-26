REM /****************************************************************************************************************************
REM * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	  			*
REM * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.        		        	*
REM * The Program or any portion thereof may not be reproduced in any form whatsoever except         		 	  	*
REM * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.     		 		*
REM *												   		  		*
REM * File Name                   : PMrelease.bat	                                      	             		  	*
REM * Author                      : charles                                                     			*
REM * Creation Date               : 04-Sep-2007                                                        			  	*
REM * Description                 : This file contains DOS script to generate SQL scripts for PM database setup			*
REM *																*
REM * Modification History        :                                                                       		  	*
REM *												      		 		*
REM *     Version No. | Date         |Modified by     |      Brief description of change                 			*
REM *  --------------------------------------------------------------------------------------------------------------		*
REM *                 | 	     |		      | 									*
REM *  -------------------------------------------------------------------------------------------------------------------------*
REM ****************************************************************************************************************************/

rd /s/q PM_TEMP
MKDIR PM_TEMP

cd..
SET WORKDIR=%CD%

cd %WORKDIR%\tables

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_tables.sql
ECHO -- TABLES  >> %WORKDIR%\set-up\pm_temp\ope_tables.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_tables.sql
ECHO OFF

for %%* in (*.ddl) do echo.@@ope_run %WORKDIR%\tables\%%*; >> %WORKDIR%\set-up\pm_temp\ope_tables.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_tables.sql
ECHO OFF



cd %WORKDIR%\types

ECHO ON
ECHO -- TYPES  >> %WORKDIR%\set-up\pm_temp\ope_types.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_types.sql
ECHO OFF

for %%* in (*.ddl) do echo.@@ope_run %WORKDIR%\types\%%*; >> %WORKDIR%\set-up\pm_temp\ope_types.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_types.sql
ECHO OFF

cd %WORKDIR%\sq

ECHO ON
ECHO -- SEQUENCES  >> %WORKDIR%\set-up\pm_temp\ope_sequences.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_sequences.sql
ECHO OFF

for %%* in (*.sq) do echo.@@ope_run %WORKDIR%\sq\%%*; >> %WORKDIR%\set-up\pm_temp\ope_sequences.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\ope_temp\ope_sequences.sql
ECHO OFF

cd %WORKDIR%\spc

ECHO ON
ECHO -- PACKAGE SPECIFICATIONS  >> %WORKDIR%\set-up\pm_temp\ope_packages.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_packages.sql
ECHO OFF

for %%* in (*.spc) do echo.@@ope_run %WORKDIR%\spc\%%*; >> %WORKDIR%\set-up\pm_temp\ope_packages.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_packages.sql
ECHO OFF

cd %WORKDIR%\sql

ECHO ON
ECHO -- PACKAGE BODIES  >> %WORKDIR%\set-up\pm_temp\ope_packages_b.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_packages_b.sql
ECHO OFF

for %%* in (*.sql) do echo.@@ope_run %WORKDIR%\sql\%%*; >> %WORKDIR%\set-up\pm_temp\ope_packages_b.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_packages_b.sql
ECHO OFF

cd %WORKDIR%\inc

ECHO ON
ECHO -- BASEPM Pre-Shipped Data >> %WORKDIR%\set-up\pm_temp\ope_inc.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_inc.sql
ECHO OFF

for %%* in (*.inc) do echo.@@ope_run %WORKDIR%\inc\%%*; >> %WORKDIR%\set-up\pm_temp\ope_inc.sql

ECHO ON
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_inc.sql
ECHO OFF

REM copy %WORKDIR%\set-up\opesetup.sql %WORKDIR%\set-up\pm_temp\opesetup.sql
copy %WORKDIR%\set-up\ope_install.sql %WORKDIR%\set-up\pm_temp\ope_install.sql
copy %WORKDIR%\set-up\drop_all_objects.sql %WORKDIR%\set-up\pm_temp\ope_uninstall.sql
copy %WORKDIR%\set-up\ope_run.sql %WORKDIR%\set-up\pm_temp\ope_run.sql
copy %WORKDIR%\set-up\ope_Recompile.sql %WORKDIR%\set-up\pm_temp\ope_Recompile.sql




ECHO ON
ECHO -- Clean Open Schema >> %WORKDIR%\set-up\pm_temp\ope_clean_open.sql
ECHO @@pmconn_open; >> %WORKDIR%\set-up\pm_temp\ope_clean_open.sql
ECHO -- >> %WORKDIR%\set-up\pm_temp\ope_clean_open.sql
ECHO OFF

ECHO ON
ECHO EXIT; >> %WORKDIR%\set-up\pm_temp\ope_exit.sql
ECHO OFF

copy %WORKDIR%\set-up\ope_Prompt.sql %WORKDIR%\set-up\pm_temp\ope_Prompt.sql
copy %WORKDIR%\set-up\opeconn_open.sql %WORKDIR%\set-up\pm_temp\opeconn_open.sql
copy %WORKDIR%\set-up\opeconn_closed.sql %WORKDIR%\set-up\pm_temp\opeconn_closed.sql

cd %WORKDIR%\set-up\pm_temp
cls
sqlplus/nolog @ope_prompt.sql
