connect &pm_usr/&pm_pwd@&pm_db;

SET TERMOUT ON
DEFINE line1='--------------------------------------------------------------------------------------'
DEFINE line2='======================================================================================'
DEFINE finished='*********************************  Finished  *********************************'
DEFINE ope='OPE'

PROMPT 
PROMPT Connected to &pm_usr on &pm_db
PROMPT 
PROMPT OPE Installation ...
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
select DECODE('&next_script','ope_install','I N S T A L L I N G  OPE ... ',
                 'ope_uninstall','U N I N S T A L L I N G  OPE ... ',
                 'ope_clean_open','C L E A N I N G  OPE Open Workflow Schema ...',
                 'ope_recompile', 'R E C O M P I L I N G OPE ...',
                 'ope_exit') col FROM dual;
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

