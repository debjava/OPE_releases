--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : CUSTOMER_AGREEMENT_MAP_DEFN                                            *
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific Agreements map Details *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_CUSTOMER _AGREEMENT_MAP_DEFN;

CREATE TABLE OPE_CUST_AGREE_MAP_DEFN
				(
					version_no		NUMBER         NOT NULL
				,	customer_id		VARCHAR2(10)   NOT NULL
				,	customer_name		VARCHAR2(10)   NOT NULL
				,	address			VARCHAR2(40)
				,	agreement_id		VARCHAR2(10)   NOT NULL
				,	CONSTRAINT prk_CUST_AGREE_MAP_DEFN PRIMARY KEY(customer_id,version_no)
				);

