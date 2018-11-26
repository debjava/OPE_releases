--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : SERVICE_DEFN.DDL								*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific	       *
				Service Details							       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_SERVICE_DEFN;

CREATE TABLE OPE_SERVICE_DEFN
				(
					version_no		NUMBER
				,	service_id		VARCHAR2(10 CHAR)
				,	service_name		VARCHAR2(40 CHAR)
				,	service_description	VARCHAR2(255 CHAR)
				,	valid_from		DATE
				,	valid_to		DATE
				,	created_by		VARCHAR2(10 CHAR)
				,	created_on		DATE
				,	last_updated_by		VARCHAR2(10 CHAR)
				,	last_updated_on		DATE
				,	availability		CHAR(1 CHAR)
				,	status			CHAR(1 CHAR)
				,	send_to			CHAR(1 CHAR)
				,	CONSTRAINT prk_SERVICE_DEFN PRIMARY KEY(service_id,version_no)
				);
