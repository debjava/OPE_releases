--
/************************************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	  			  	*
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.        		        	  	*
* The Program or any portion thereof may not be reproduced in any form whatsoever except         		 	  	*
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.     		 		*
*												   		  		  				*
* File Name                   : pm_setup.sql                                       	             		  	  	*
* Author                      : charles                                                     			  	*
* Creation Date               : 04-Sep-2007                                                        			  	*
* Description                 : This file contains script to set up PM database							*
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

----------------------------------------------------
-- Parameter 1 Values:
--
-- Install   - PM full installation
-- Recompile - Recompile PM code base
-- Uninstall - Deinstall PM
----------------------------------------------------
SET TERMOUT ON
DEFINE line1='--------------------------------------------------------------------------------------'
DEFINE line2='======================================================================================'
DEFINE finished='*********************************  Finished  *********************************'
DEFINE ope='OPE'

COLUMN col NOPRINT NEW_VALUE pm_owner
select USER col from dual;

COLUMN col NOPRINT NEW_VALUE next_script
select DECODE(LOWER('&1'),'-i',	'ope_install',
                 		 '-r',	'ope_recompile',
                 		 '-u',	'ope_uninstall',
                 		 '-cln-o',	'ope_clean_open',
                 		 '-open',	'opeconn_open',
                 		 'ERROR') col FROM dual;

COLUMN col NOPRINT NEW_VALUE txt_prompt
select DECODE('&next_script','ope_install','I N S T A L L I N G  OPE',
                 'ope_recompile','R E C O M P I L I N G  OPE',
                 'ope_uninstall','U N I N S T A L L I N G  OPE',
                 'opeconn_open','C O N N E C T I N G  Open Schema...',
                 'ope_clean_open','C L E A N I N G  ProcessMate Open Workflow Schema',
                 'ERROR') col FROM dual;
------------------------------------------------------



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

PROMPT [ &txt_prompt ...]
PROMPT

@@&next_script
