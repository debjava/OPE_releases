--
/************************************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	  			  	*
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.        		        	  	*
* The Program or any portion thereof may not be reproduced in any form whatsoever except         		 	  	*
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.     		 		*
*												   		  		  				*
* File Name                   : pm_install.sql                                       	             		  	*
* Author                      :  charles                                                     			  	*
* Creation Date               : 04-Sep-2007                                                        			  	*
* Description                 : This file contains script to compile PM database objects						*
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
SET ECHO OFF
SET VERIFY OFF
SET FEEDBACK OFF
SET TTITLE OFF

SET SERVEROUTPUT ON SIZE 1000000 FORMAT WRAPPED
SET DEFINE ON
SPOOL ope_install_logs.log

----------------------------------------------------TABLES
SET TERMOUT ON
PROMPT &line1
PROMPT CREATING &ope TABLES
PROMPT &line1

DEFINE prompt_text='Creating &ope table '

@@ope_tables


----------------------------------------------------SEQUENCES
SET TERMOUT ON
PROMPT &line1
PROMPT CREATING &ope SEQUENCES
PROMPT &line1

DEFINE prompt_text='Creating &ope sequence '

@@ope_sequences

----------------------------------------------------TYPES
SET TERMOUT ON
PROMPT &line1
PROMPT CREATING &ope TYPES
PROMPT &line1

DEFINE prompt_text='Creating &ope type '

@@ope_types

----------------------------------------------------PACKAGE HEADERS
SET TERMOUT ON
PROMPT &line1
PROMPT CREATING &ope PACKAGE SPECIFICATIONS
PROMPT &line1

DEFINE prompt_text='Creating &ope package specification '

@@ope_packages

----------------------------------------------------PACKAGE BODIES
SET TERMOUT ON
PROMPT &line1
PROMPT CREATING &ope PACKAGE BODIES
PROMPT &line1

DEFINE prompt_text='Creating &ope package body '

@@ope_packages_b

----------------------------------------------------INC
SET TERMOUT ON
PROMPT &line1
PROMPT INSERTING &ope PRE-SHIPPED DATA
PROMPT &line1

DEFINE prompt_text='Inserting &ope Pre-Shipped Data '

@@ope_inc

SET TERMOUT OFF
SPOOL OFF

CLS
SET TERMOUT ON
PROMPT 
PROMPT Finished Installing PM. Please check pm_install_logs.log file for logs in pm_temp folder.
PROMPT 
PROMPT <== OPTIONS ==>
PROMPT
PROMPT 1] UnInstall
PROMPT 2] Recompile
PROMPT 3] To Clean Up tables
PROMPT 4] Exit
PROMPT
ACCEPT pm_op_inst_opt CHAR PROMPT 'Please Enter the Appropriate option -> '
PROMPT

COLUMN col NOPRINT NEW_VALUE pm_owner
select USER col from dual;

SET TERMOUT OFF
COLUMN col NOPRINT NEW_VALUE next_script
select DECODE(LOWER('&pm_op_inst_opt'),'3','ope_clean_open',
                 		 '2',	'ope_recompile',
                 		 '1',	'ope_uninstall',
                 		 '4', 'ope_exit',
                 		 'ERROR') col FROM dual;

COLUMN col NOPRINT NEW_VALUE txt_prompt
select DECODE('&next_script','ope_recompile','R E C O M P I L I N G  OPE ... ',
                 'ope_uninstall','U N I N S T A L L I N G  OPE ... ',
                 'ope_clean_open','C L E A N I N G  OPE  Schema ...',
                 'ope_exit') col FROM dual;
------------------------------------------------------

SET TERMOUT ON

PROMPT &txt_prompt 
PROMPT

@@&next_script
SET TERMOUT OFF

