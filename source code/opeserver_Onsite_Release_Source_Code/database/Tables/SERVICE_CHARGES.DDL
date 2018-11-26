--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : SERVICE_CHARGES.DDL						*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing Log in sERVICE CHARCHES details *					       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_SERVICE_CHARGES;

CREATE TABLE OPE_SERVICE_CHARGES
				(
					service_id		VARCHAR2(10 CHAR)
				,	version			NUMBER
				,	service_name		VARCHAR2(60 CHAR)
				,	channel			VARCHAR2(3 CHAR)
				,	fee_pertem_currency	NUMBER
				,	volume			NUMBER
				,	total_currency		NUMBER
				,	CONSTRAINT prk_SERVICE_CHARGES PRIMARY KEY(service_id,version)
				);

