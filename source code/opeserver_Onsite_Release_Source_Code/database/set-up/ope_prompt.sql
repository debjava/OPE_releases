SET TERMOUT ON
PROMPT *** IDEAL INVENT Technologies Pvt. Ltd. ***
PROMPT 
PROMPT Please login to proceed installing OPE database objects.
PROMPT
ACCEPT pm_db  CHAR PROMPT 'Enter Database Name			:'
PROMPT
ACCEPT pm_usr CHAR PROMPT 'Enter Schema Name			:'
ACCEPT pm_pwd CHAR PROMPT 'Enter Password			        :' HIDE
PROMPT
PROMPT 1] Connect Open Schema
PROMPT 2] Exit
PROMPT
ACCEPT pm_inst_opt CHAR PROMPT 'Please Enter the Appropriate option -> '
PROMPT
SET TERMOUT OFF
conn &pm_usr/&pm_pwd@&pm_db

COLUMN col NOPRINT NEW_VALUE next_conn
select DECODE(LOWER('&pm_inst_opt'),'1',	'opeconn_open',
                 		 		'2',	'ope_exit',                		 		
                 		 		'ERROR') col FROM dual;

@@&next_conn
