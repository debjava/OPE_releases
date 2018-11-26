/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                         	       *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*												       				 *
* File Name                   : currency.ddl                                                           *
* Author                      : charles                                                          *
* Creation Date               : 14-June-2006                                                           *
* Description                 : This file is to create ope_currency_defn table		             *		         			                        
* Modification History        :                                                                        *
*												       				 *                          										    										  
*     Version No.| Date     |Modified by   |            Brief description of change         		 *
*  --------------------------------------------------------------------------------------------------  *
					 *
*  --------------------------------------------------------------------------------------------------  *
********************************************************************************************************/

--DROP TABLE ope_currency_defn;

CREATE TABLE ope_currency_defn 
			(	
				version_no 			NUMBER
			,	currency_id			VARCHAR2(10 CHAR) -- New Column
			,	currency_code 		VARCHAR2(3 CHAR)
			,	currency_name 		VARCHAR2(40 CHAR)
			,	decimal_digits 		NUMBER
			,	rounding_method		VARCHAR2(16 CHAR)
			,	decimal_separator		VARCHAR2(1 CHAR)
			,	thousand_separator 	VARCHAR2(1 CHAR)
			,	CONSTRAINT currency1_prk PRIMARY KEY(version_no,currency_id)
			);

