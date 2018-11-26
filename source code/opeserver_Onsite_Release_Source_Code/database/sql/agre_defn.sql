--
/********************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                    *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                   *
* The Program or any portion thereof may not be reproduced in any form whatsoever except                *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.             *
*                                                                                                       *
* File Name                   : agre_defn.sql                                                        *
* Author                      : charles                                                             *
* Creation Date               : 21-Jul-2008                                                             *
* Description                 : This file has been created to perform new,search function               *
*                                                                                                       *
* Modification History        :                                                                         *
*                                                                                                       *
*     Version No.      Date          Modified By       Brief description of change                      *
*------------------------------------------------------------------------------------------------------ *

*------------------------------------------------------------------------------------------------------ *
********************************************************************************************************/
--
CREATE OR REPLACE PACKAGE BODY agre_defn
AS
-- FORWARD DECLARATION starts
/****************************************************************************************************************/
FUNCTION fn_insert_record
                (
                        p_rec_entity            IN agre_defn_table
		,	p_rec_map		IN agre_map_defn_table
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
RETURN BOOLEAN;
/*
FUNCTION fn_agreement_exist
                (
                        p_channel_id            IN EDTM_CHANNEL_DEFN.CHANNEL_ID%TYPE
                ,       p_channel_code          IN EDTM_CHANNEL_DEFN.CHANNEL_CODE%TYPE
                ,       p_channel_name          IN EDTM_CHANNEL_DEFN.CHANNEL_NAME%TYPE
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
RETURN BOOLEAN;

*/
/****************************************************************************************************************/
-- FORWARD DECLARATION ends

FUNCTION  fn_agre
                (
                        p_user_id               IN ope_ope_user_info.user_id%TYPE
                ,       p_action                IN VARCHAR2
                ,       p_business_date         IN DATE
                ,       p_rec_entity            IN agre_defn_table
		,	p_rec_map		IN agre_map_defn_table
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
RETURN NUMBER
IS
        l_res                   INTEGER;
        l_last_version          VARCHAR2(1 CHAR);
        l_err_code              VARCHAR2(20 CHAR);
        l_err_message           VARCHAR2(1000);
       -- l_channel_data          channel_table := channel_table();
        --l_channel_prd           channel_prd_table := channel_prd_table();
      --  l_channel_plan          channel_plan_table := channel_plan_table();


BEGIN
        CASE    WHEN p_action = 'N' THEN l_res := fn_new_agre
                                                (

                                                        p_rec_entity
                                                ,       p_rec_map
                                                ,       l_err_code
                                                ,       l_err_message
                                                );
               
                ELSE
                        p_err_code      := 'ERR-CHL-001';
                        p_err_message   := 'Invalid Action,Pleae try later';
                        RETURN 0;
                END CASE;

        p_err_code      := l_err_code;
        p_err_message   := l_err_message;

        IF l_res != 1
        THEN
                RETURN 0;
        END IF;

        RETURN 1;
EXCEPTION
WHEN OTHERS
THEN
        p_err_code      := 'ERR-CHL-004';
        p_err_message   := 'Error in processing channel '||SQLERRM;
        RETURN 0;
END  fn_channel;




FUNCTION fn_new_agre
                (
                        p_rec_entity             IN agre_defn_table
		,	p_rec_map		 IN agre_map_defn_table
                ,       p_err_code               IN OUT  VARCHAR2
                ,       p_err_message            IN OUT  VARCHAR2
                )
RETURN NUMBER
IS
        l_count NUMBER;
        l_agre_data    agre_defn_table     := agre_defn_table();
        l_map_data     agre_map_defn_table := agre_map_defn_table();

BEGIN

        l_agre_data := agre_defn_table;
        l_map_data  := agre_map_defn_table;
        agre_defn_table(1).status := 'A';


        IF NOT fn_insert_record
                (
                        l_agre_data
                ,       l_map_data
                ,       p_err_code
                ,       p_err_message
                )
        THEN
                RETURN 0;
        END IF;

        p_err_code      := 'ERR-CHL-018';
        p_err_message   := 'Record created successfully';
RETURN 1;

EXCEPTION
WHEN OTHERS
THEN
        p_err_code    := 'ERR-CHL-007';
        p_err_message := 'Error in creation of New Channel Defn'||SQLERRM;
        RETURN 0;

END fn_new_channel;

FUNCTION fn_insert_record
                (
                        p_rec_entity            IN agre_defn_table
		,	p_rec_map		IN cust_agre_map_defn_table
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
                )
RETURN BOOLEAN
IS
l_version_no NUMBER;
BEGIN

        INSERT INTO OPE_AGREE_DEFN
                (
			agreement_id		  
		,	version_no		 
		,       branch_id		 
		,	contact_person		 
		,	dnb_relationships_manager
		,	date_of_agreement	 
		,	suffix			 
		,	patu_id			 
		,	customer_short_name	 
		,	acceptance_routine	 
		,	security_method		 
		,	account_for_fees	 
		,	valid_from		 
		,	valid_to		 
		,	address_line_1		 
		,	address_line_2		 
		,	address_line_3		 
		,	address_line_4		 
		,	post_address1		 
		,	post_address2		 
		,	post_address3		 
		,	post_address4		 
		,	language_code		 
		,	country_id		 
		,	status			 
		,	created_by		 
		,	created_on		 
		,	last_updated_by		 
		,	lats_updated_on		
			
                )
        VALUES
                (
                	p_rec_entity(1).agreement_id		  
		,	version_no		 
		,       p_rec_entity(1).branch_id		 
		,	p_rec_entity(1).contact_person		 
		,	p_rec_entity(1).dnb_relationships_manager
		,	p_rec_entity(1).date_of_agreement	 
		,	p_rec_entity(1).suffix			 
		,	p_rec_entity(1).patu_id			 
		,	p_rec_entity(1).customer_short_name	 
		,	p_rec_entity(1).acceptance_routine	 
		,	p_rec_entity(1).security_method		 
		,	p_rec_entity(1).account_for_fees	 
		,	p_rec_entity(1).valid_from		 
		,	p_rec_entity(1).valid_to		 
		,	p_rec_entity(1).address_line_1		 
		,	p_rec_entity(1).address_line_2		 
		,	p_rec_entity(1).address_line_3		 
		,	p_rec_entity(1).address_line_4		 
		,	p_rec_entity(1).post_address1		 
		,	p_rec_entity(1).post_address2		 
		,	p_rec_entity(1).post_address3		 
		,	p_rec_entity(1).post_address4		 
		,	p_rec_entity(1).language_code		 
		,	p_rec_entity(1).country_id		 
		,	p_rec_entity(1).status			 
		,	p_rec_entity(1).created_by		 
		,	p_rec_entity(1).created_on		 
		,	p_rec_entity(1).last_updated_by		 
		,	p_rec_entity(1).lats_updated_on	
                );
  l_version_no := p_channel_data(1).version_no;
  
  

        FOR j IN p_rec_map.FIRST..p_rec_map.LAST LOOP
 
                INSERT INTO OPE_CUST_AGREE_MAP_DEFN
                        (
                                version_no
                        ,       customer_id
                        ,       customer_name
                        ,       address
			,	agreement_id
                        )
                        VALUES
                        (
                                p_rec_map(j).version_no
                        ,       p_rec_map(j).customer_id
                        ,       p_rec_map(j).customer_name
                        ,       p_rec_map(j).address
			,	p_rec_map(j).agreement_id
                        );
        END LOOP;

        p_err_code     := 'ERR-CHL-000';
        p_err_message  := 'RECORD Inserted Sucessfully';

RETURN TRUE;

EXCEPTION
WHEN OTHERS
THEN
        p_err_code     := 'ERR-CHL-006';
        p_err_message  := 'Failed to insert record'||SQLERRM;
        RETURN FALSE;
END fn_insert_record;




END edpk_channel_defn;
/


