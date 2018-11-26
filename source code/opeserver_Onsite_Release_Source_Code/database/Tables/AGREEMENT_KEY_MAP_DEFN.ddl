--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : AGREEMENT_KEY_MAP_DEFN.DDL                                             *
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific Agreements   *
				KEY map Details							       *
*                                                                                                      *
* Modification History        :    								       *
*												       *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_AGREEMENT_KEY_MAP_DEFN;

CREATE TABLE OPE_AGREE_KEY_MAP_DEFN
				(
					version_no		NUMBER
				,	agreement_id		VARCHAR2(10 CHAR)
				,	key_part_1		VARCHAR2(40 CHAR)
				,	key_part_2		VARCHAR2(40 CHAR)
				,	CONSTRAINT prk_AGREE_KEY_MAP_DEFN PRIMARY KEY(AGREEMENT_ID,version_no)
				);

