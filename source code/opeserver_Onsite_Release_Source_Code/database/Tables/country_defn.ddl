--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	       *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*												       *										 
* File Name                   :  ope_country_defn.ddl                                  	               *
* Author                      :  charles                                                          *
* Creation Date               :  March-2006                                                            *
* Description                 :  This file is to create ope_country_defn table  		       *
*				         			      				       *
* Modification History        :                                                                        *
*												       *										     
*   Version No.|Date        |Modified by    |       Brief description of change                        *
*  --------------------------------------------------------------------------------------------------  *
*  *					       			     
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--

--DROP TABLE ope_country_defn;

CREATE TABLE ope_country_defn(
	country_id   VARCHAR2(3 CHAR),
	country_name VARCHAR2(255 CHAR) NOT NULL,
	available    VARCHAR2(1 CHAR),
CONSTRAINT country_prk PRIMARY KEY(country_id));