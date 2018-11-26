--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : SERVICE_BUREAU_MAP_DEFN.DDL						*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific	       *
				Service Bureau Map Details					       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_SERVICE_BUREAU_MAP_DEFN;

CREATE TABLE OPE_SERVICE_BUREAU_MAP_DEFN
				(
					version_no		NUMBER
				,	service_id		VARCHAR2(10 CHAR)
				,	service_code		VARCHAR2(10 CHAR)
				,	bureau_id		VARCHAR2(10 CHAR)
				,	CONSTRAINT prk_SERVICE_BUREAU_MAP_DEFN PRIMARY KEY(BUREAU_ID,version_no)
				);

