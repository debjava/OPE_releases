--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *                                                                                
* File Name                   : agre_defn__object.ddl                                                  *
* Author                      : charles.A                                                              *
* Creation Date               : 21-July-2008                                                           *
* Description                 : This file will create object for Agreement info type variable          *        
*                                                                                                      *        
*                                                                                                      *
* Modification History        :                                                                        *
*                                                                                                      *                                                                                    
*    Version No.|    Date     |  Modified By  |       Brief description of change                      *
*  --------------------------------------------------------------------------------------------------  *
*                                                                                                      *
*                                                                                                      *
*  --------------------------------------------------------------------------------------------------  *
********************************************************************************************************/
--

CREATE OR REPLACE TYPE agre_defn_type AS OBJECT
        (       
					agreement_id		  VARCHAR2(10 CHAR)   
				,	version_no		  NUMBER	      
				,       branch_id		  VARCHAR2(10 CHAR)   
				,	contact_person		  VARCHAR2(34 CHAR)   
				,	dnb_relationships_manager  VARCHAR2(34 CHAR)  
				,	date_of_agreement	  DATE   
				,	suffix			  VARCHAR2(4 CHAR) 
				,	patu_id			  VARCHAR2(10 CHAR)   
				,	customer_short_name	  VARCHAR2(4 CHAR)
				,	acceptance_routine	  VARCHAR2(10 CHAR)   
				,	security_method		  VARCHAR2(10 CHAR)   
				,	account_for_fees	  NUMBER  
				,	valid_from		  DATE   
				,	valid_to		  DATE   
				,	address_line_1		  VARCHAR2(50 CHAR)   
				,	address_line_2		  VARCHAR2(50 CHAR)
				,	address_line_3		  VARCHAR2(50 CHAR)
				,	address_line_4		  VARCHAR2(50 CHAR)
				,	post_address1		  VARCHAR2(50 CHAR)   
				,	post_address2		  VARCHAR2(50 CHAR)
				,	post_address3		  VARCHAR2(50 CHAR)
				,	post_address4		  VARCHAR2(50 CHAR)
				,	language_code		  VARCHAR2(3 CHAR)   
				,	country_id		  VARCHAR2(3 CHAR)   
				,	status			  CHAR(1 CHAR)
				,	created_by		  VARCHAR2(15 CHAR)
				,	created_on		  DATE
				,	last_updated_by		  VARCHAR2(15 CHAR)
				,	lats_updated_on		  DATE
			  );
/
CREATE OR REPLACE TYPE agre_defn_table AS TABLE OF agre_defn_type;
/

CREATE OR REPLACE TYPE agre_map_defn_type AS OBJECT
			 (       
					version_no		NUMBER         
				,	customer_id		VARCHAR2(10)   
				,	customer_name		VARCHAR2(10)   
				,	address			VARCHAR2(40)
				,	agreement_id		VARCHAR2(10)   
			  );
/
CREATE OR REPLACE TYPE agre_map_defn_table AS TABLE OF agre_map_defn_type;
/