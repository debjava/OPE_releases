SET HEAD OFF
SET PAGES 0
SET FEEDBACK OFF
SPOOL C:\lst\DROP.LST

SELECT 'DROP '||OBJECT_TYPE ||' '||OBJECT_NAME||' ;'
FROM USER_OBJECTS where object_type=&Objectype

SPOOL OFF;
@c:\lst\DROP.LST