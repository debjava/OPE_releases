--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	       *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*												       *										 
* File Name                   : ope_country_code.ddl                                  	               *
* Author                      : charles                                                          *
* Creation Date               :  March-2006                                                            *
* Description                 :  This file is to create ope_country_code table  		       *
*				         			      				       *
* Modification History        :                                                                        *
*												       *										     
*   Version No.|Date        |Modified by    |       Brief description of change                        *
*  --------------------------------------------------------------------------------------------------  *
     *					       			     
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE ope_currency_code;

CREATE TABLE ope_currency_code(
       currency_id    VARCHAR2(10 CHAR),
       currency_code  VARCHAR2(3 CHAR)  NOT NULL,
       country_id     VARCHAR2(3 CHAR),
CONSTRAINT currency_prk PRIMARY KEY(currency_id));
