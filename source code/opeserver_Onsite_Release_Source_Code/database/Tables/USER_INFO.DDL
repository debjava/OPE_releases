--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : USER_INFO.DDL						*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing Log in user details	       *					       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_USER_INFO;

CREATE TABLE OPE_USER_INFO
				(
					version_no		NUMBER
				,	user_id			VARCHAR2(15 CHAR)
				,	user_name		VARCHAR2(30 CHAR)
				,	user_password		VARCHAR2(50 CHAR)
				,	status			CHAR(1)
				,	login_status		CHAR(1)
				,	debug_status		CHAR(1)
				,	CONSTRAINT prk_USER_INFO PRIMARY KEY(user_id,version_no)
				);
