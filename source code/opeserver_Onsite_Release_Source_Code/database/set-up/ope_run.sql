SET TERMOUT OFF

COLUMN col NOPRINT NEW_VALUE run_object 
SELECT SUBSTR('&1', INSTR('&1', '\', - 1) + 1, (INSTR('&1', '.', - 1) - 1) - (INSTR('&1', '\', - 1) + 1) + 1) col FROM dual;

COLUMN col NOPRINT NEW_VALUE pm_run_object
SELECT '&1' col FROM dual;

SET TERMOUT ON

PROMPT &prompt_text &run_object

@@&pm_run_object

REM PROMPT &line1

SET TERMOUT OFF
