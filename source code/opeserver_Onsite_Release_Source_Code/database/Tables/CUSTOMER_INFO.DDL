--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : CUSTOMER_INFO.DDL						*
* Author                      : charles                                                     	       *
* Creation Date               : 15-Jul-2007                                                            *
* Description                 : This file is to create tables for storing  CUSTOMER IFORMATION	       *					       *
*                                                                                                      *
* Modification History        :    												 *
*																	 *
*   Version No.|   Date       | Modified by    |       Brief description of change                     *
*  --------------------------------------------------------------------------------------------------  *
        
*  --------------------------------------------------------------------------------------------------  *
******************************************************************************************************/
--
--DROP TABLE OPE_CUSTOMER_INFO;

CREATE TABLE OPE_CUST_INFO
				(
					version_no		NUMBER
				,	customer_id		VARCHAR2(10 CHAR)
				,	customer_name		VARCHAR2(10 CHAR)
				,	address			VARCHAR2(40 CHAR)
				,	cust_acc_no		NUMBER(14)
				,	address_line_1		VARCHAR2(50 CHAR)
				,	address_line_2		VARCHAR2(50 CHAR)
				,	address_line_3		VARCHAR2(50 CHAR)
				,	address_line_4		VARCHAR2(50 CHAR)
				,	post_address1		VARCHAR2(50 CHAR)	
				,	post_address2		VARCHAR2(50 CHAR)
				,	post_address3		VARCHAR2(50 CHAR)
				,	post_address4		VARCHAR2(50 CHAR)
				,	language_code		VARCHAR2(3 CHAR)
				,	CONSTRAINT prk_OPE_CUST_INFO PRIMARY KEY(customer_id,version_no)
				);
