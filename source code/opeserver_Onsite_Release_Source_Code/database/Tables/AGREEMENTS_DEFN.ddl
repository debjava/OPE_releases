--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : AGREEMENTS_DEFN.ddl                                             	 *
* Author                      : charles                                                     	 *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing client specific Agreements Details *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_AGREEMENTS_DEFN;

CREATE TABLE OPE_AGREE_DEFN
				(
					agreement_id		  VARCHAR2(10 CHAR)    NOT NULL
				,	version_no		  NUMBER	      NOT NULL	
				,       branch_id		  VARCHAR2(10 CHAR)   NOT NULL
				,	contact_person		  VARCHAR2(34 CHAR)   NOT NULL
				,	dnb_relationships_manager VARCHAR2(34 CHAR)   NOT NULL
				,	date_of_agreement	  DATE		      NOT NULL
				,	suffix			  VARCHAR2(4 CHAR) 
				,	patu_id			  VARCHAR2(10 CHAR)   NOT NULL
				,	customer_short_name	  VARCHAR2(4 CHAR)
				,	acceptance_routine	  VARCHAR2(10 CHAR)   NOT NULL
				,	security_method		  VARCHAR2(10 CHAR)   NOT NULL
				,	account_for_fees	  NUMBER	      NOT NULL
				,	valid_from		  DATE		      NOT NULL
				,	valid_to		  DATE		      NOT NULL
				,	address_line_1		  VARCHAR2(50 CHAR)   NOT NULL
				,	address_line_2		  VARCHAR2(50 CHAR)
				,	address_line_3		  VARCHAR2(50 CHAR)
				,	address_line_4		  VARCHAR2(50 CHAR)
				,	post_address1		  VARCHAR2(50 CHAR)   NOT NULL
				,	post_address2		  VARCHAR2(50 CHAR)
				,	post_address3		  VARCHAR2(50 CHAR)
				,	post_address4		  VARCHAR2(50 CHAR)
				,	language_code		  VARCHAR2(3 CHAR)   NOT NULL
				,	country_id		  VARCHAR2(3 CHAR)   NOT NULL
				,	status			  CHAR(1 CHAR)
				,	created_by		  VARCHAR2(15 CHAR)
				,	created_on		  DATE
				,	last_updated_by		  VARCHAR2(15 CHAR)
				,	lats_updated_on		  DATE
				,	CONSTRAINT prk_AGREE_DEFN PRIMARY KEY(agreement_id,version_no)
				);
