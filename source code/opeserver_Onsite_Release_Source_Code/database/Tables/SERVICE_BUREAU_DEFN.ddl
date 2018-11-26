--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : SERVICE_BUREAU_DEFN.DDL								*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific	       *
				Service Bureau Details							       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_SERVICE_BUREAU_DEFN;

CREATE TABLE OPE_SERVICE_BUREAU_DEFN
				(
					version_no		NUMBER
				,	bureau_id		VARCHAR2(10 CHAR)
				,	bureau_name		VARCHAR2(40 CHAR)
				,	bureau_desc		VARCHAR2(255 CHAR)
				,	city			VARCHAR2(40 CHAR)
				,	address_line1		VARCHAR2(32 CHAR)
				,	address_line2		VARCHAR2(32 CHAR)
				,	address_line3		VARCHAR2(32 CHAR)
				,	address_line4		VARCHAR2(32 CHAR)
				,	tel_no1			VARCHAR2(40 CHAR)
				,	tel_no2			VARCHAR2(40 CHAR)
				,	pin_no			NUMBER
				,	country_id		VARCHAR2(3 CHAR)
				,	CONSTRAINT prk_SERVICE_BUREAU_DEFN PRIMARY KEY(BUREAU_ID,version_no)
				);
