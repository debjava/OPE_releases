The steps below are to set up a release schema

All files are folders mentioned refer to the set-up folder in DMDNBFI01\database unless specified otherwise

1) Create a folder LST in c: 

2) If Required - create schema using create_schema.sql (Please read the comments in the file before running it)

3) Login to the schema where set up needs to be done.

4) Run set-up\drop_all_objects.sql 

5) Run the command PURGE RECYCLEBIN;

6) Run Select count(*) from user_objects

7) Re-run steps 4-7 until the command from Step 7 returns 0 rows.

8) For a BasePm release use file set-up\release.bat and for an LH release, use set-up\release_lh.bat

9) Edit Release.bat or release_lh.bat and replace C:\FRESHCHECKOUT\ProcessMateSrc\database with the equivalent CVS path name 

10) Double click on Release.bat or release_lh.bat (whichever is applicable - refer to step 9).
	-This will create a file script.lst  in c:\LST (can open the file and check out the path)

11) Run workflow-set-up\create_workflow_objects.sql

12) Run @C:\LST\script.lst

14) Open file closed_workflow_set_up\closedworkflow_Moveclosed.sql and replace <open workflow schema name> with the current open workflow schema name

15) Following the set of instructions mentioned below ( I or II) accordingly.

16) Run closed_workflow_set_up\closedworkflow_Moveclosed in the open worflow schema.

17) Run set-up\list_invalid_objects.sql - This will print a list of Invalid objects in c:\LST\INVALID.LST.
	-Ideally this list should not have any INVALID objects 

18) Run set-up\inv This will compile all invalid objects.

19) Keep running step 17 and 18 till no objects are invalid
 
20) Edit run_inc.bat or run_inc_lh.bat depending on if it is a basePM release or LH release and replace 
	C:\idealdb\BasePM\database with equivalent CVS path 

21) Double click on run_inc.bat or run_inc_lh.bat depending on if it is a basePM release or LH release.
	--This will create a file data.lst  in c:\LST (can open the file and check out the path)


22) Run @c:\LST\data.lst



I]  To Set up a New closed workflow schema

1) Connect sys as sysdba and use the script closed_workflow_set_up\create_pmclwf_schema_script.sql
2) Login into PMCLWF schema
3) Run closed_workflow_set_up\closedworkflow_create_table.sql
4) Open closed_workflow_set_up\closedworkflow_grant_script.sql and replace <open workflow schema name> with the current open workflow schema name
5) Now Run closed_workflow_set_up\closedworkflow_grant_script.sql with the change made in step 4



II] To Set up an existing closed workflow schema

1) Login into PMCLWF schema
2) Run closed_workflow_set_up\closedworkflow_clean_data.sql
3) Open closed_workflow_set_up\closedworkflow_grant_script.sql and replace <open workflow schema name> with the current open workflow schema name
4) Now Run closed_workflow_set_up\closedworkflow_grant_script.sql
