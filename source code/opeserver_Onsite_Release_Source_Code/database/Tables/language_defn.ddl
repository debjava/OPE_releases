--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	       *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*												       *										 
* File Name                   : ope_language_defn.ddl                               	               *
* Author                      : charles                                                        *
* Creation Date               :  February-2006                                                         *
* Description                 :  This file is to create ope_language_defn table  		       *
*				         			      				       *
* Modification History        :                                                                        *
*												       *										     
*     Version No.|  Date       | Modified by      |   Brief description of change                      *
*  --------------------------------------------------------------------------------------------------  *
			               *
*  --------------------------------------------------------------------------------------------------  *
********************************************************************************************************/
--

--DROP TABLE ope_language_defn;

CREATE TABLE ope_language_defn(
	language_id   VARCHAR2(9 CHAR),
	language_name VARCHAR2(255 CHAR) NOT NULL,
	country_id    varchar2(3 CHAR),
	available     VARCHAR2(1 CHAR),
CONSTRAINT language_prk PRIMARY KEY(language_id));
