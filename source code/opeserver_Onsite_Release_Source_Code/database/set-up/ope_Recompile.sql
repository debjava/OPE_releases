--
/************************************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	  			  	*
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.        		        	  	*
* The Program or any portion thereof may not be reproduced in any form whatsoever except         		 	  	*
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.     		 		*
*												   		  		  				*
* File Name                   : pm_recompile.sql                                       	             		  	*
* Author                      : charles                                                     			  	*
* Creation Date               : 04-Sep-2007                                                        			  	*
* Description                 : This file contains script to recompile Invalid PM database objects				*
*																				*
* Modification History        :                                                                       		  	*
*												      		 		  			*
*     Version No. | Date         |Modified by     |      Brief description of change                 				*
*  --------------------------------------------------------------------------------------------------------------		*
*                 | 		   |			  | 												*
*  ---------------------------------------------------------------------------------------------------------------------*
************************************************************************************************************************/
--

SET TERMOUT OFF
SET VERIFY OFF
SET PAGESIZE 0
SET FEEDBACK OFF
SET TRIMSPOOL ON

SET DEFINE ON
TTITLE OFF
SET SERVEROUTPUT ON SIZE 1000000 FORMAT WRAPPED

DEFINE uscript='pm_recompile_logs.log'

SET TERMOUT ON
PROMPT &line1
PROMPT RECOMPILING &ope OBJECTS
PROMPT &line1
SET TERMOUT OFF

SPOOL &uscript
SELECT 'PROMPT Recompiling...' FROM dual;
SELECT 'ALTER '|| DECODE (object_type, 'PACKAGE BODY', 'PACKAGE',object_type)|| ' ' || object_name|| ' ' ||
DECODE (object_type, 'PACKAGE BODY', 'COMPILE BODY;','COMPILE;')
FROM 	user_objects
WHERE status = 'INVALID'
ORDER BY last_ddl_time;
--SELECT 'PROMPT &finished' FROM dual;

SPOOL OFF
SET TERMOUT ON

@@&uscript

PROMPT
PROMPT 1] Install 
PROMPT 2] UnInstall
PROMPT 3] Recompile
PROMPT 4] Clean Open Workflow Objects
PROMPT 5] Exit
PROMPT
ACCEPT pm_op_inst_opt CHAR PROMPT 'Please Enter the Appropriate option -> '
PROMPT

COLUMN col NOPRINT NEW_VALUE pm_owner
select USER col from dual;

SET TERMOUT OFF
COLUMN col NOPRINT NEW_VALUE next_script
select DECODE(LOWER('&pm_op_inst_opt'),'4','ope_clean_open',
                 		 '2',	'ope_uninstall',
                 		 '1',	'ope_install',
                 		 '3',	'ope_recompile',
                 		 '5', 'ope_exit',
                 		 'ERROR') col FROM dual;

COLUMN col NOPRINT NEW_VALUE txt_prompt
select DECODE('&next_script','pm_install','I N S T A L L I N G  OPE ... ',
                 'pm_uninstall','U N I N S T A L L I N G  OPE ... ',
                 'pm_clean_open','C L E A N I N G  OPE Open Workflow Schema ...',
                 'pm_recompile', 'R E C O M P I L I N G OPE ...',
                 'pm_exit') col FROM dual;
------------------------------------------------------

CLS
SET TERMOUT ON
PROMPT &line2
PROMPT 
PROMPT Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.
PROMPT
PROMPT IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.
PROMPT The Program or any portion thereof may not be reproduced in any form whatsoever except
PROMPT as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.
PROMPT
PROMPT &line2
PROMPT

PROMPT &txt_prompt 
PROMPT

@@&next_script


