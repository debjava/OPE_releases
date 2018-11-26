-- ******************** DROP ALL OBJECTS IN SCHEMA ****************************

SET TERMOUT OFF
SET ECHO OFF
SET VERIFY OFF
SET FEEDBACK OFF
SET TTITLE OFF
SET HEAD OFF
SET PAGESIZE 50000
SPOOL pm_drop_all_objects.sql

PROMPT SPOOL pm_drop_objects_logs.log
PROMPT SET FEEDBACK ON

SELECT 'DROP '||OBJECT_TYPE ||' '||OBJECT_NAME||';'
FROM USER_OBJECTS ORDER BY OBJECT_ID DESC;

PROMPT SET FEEDBACK OFF
PROMPT SPOOL OFF

SPOOL OFF

@@pm_drop_wf_objects.sql
PURGE RECYCLEBIN;

@@pm_drop_all_objects.sql
PURGE RECYCLEBIN;

COLUMN col NOPRINT NEW_VALUE total_objects
SELECT count(*) col FROM USER_OBJECTS;

CLS
SET TERMOUT ON
PROMPT 
PROMPT Finished UnInstalling PM. Please check pm_drop_objects_logs.log file for logs in pm_temp folder.
PROMPT 
PROMPT Total Objects In Schema : &total_objects
PROMPT
PROMPT <== OPTIONS ==>
PROMPT
PROMPT 1] Install 
PROMPT 2] UnInstall
PROMPT 3] Recompile
PROMPT 4] Clean Open Workflow Objects
PROMPT 6] Exit
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
SET TERMOUT OFF


