--
/*******************************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                                   *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.                  *
* The Program or any portion thereof may not be reproduced in any form whatsoever except               *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.            *
*                                                                                                      *
* File Name                   : agre_defn.spc                                                       *
* Author                      : charles                                                            *
* Creation Date               : 27-Nov-2006                                                            *
* Description                 : This file has been created to perform new,search function              *
*                                                                                                      *
*                                                                                                      *
* Modification History        :                                                                        *
*                                                                                                      *
*     Version No.      Date       Modified By       Brief description of change                        *
*------------------------------------------------------------------------------------------------------*

*------------------------------------------------------------------------------------------------------*
********************************************************************************************************/
--
CREATE OR REPLACE PACKAGE agre_defn
AS

FUNCTION  fn_agre
                (
                        p_user_id               IN ope_user_info.user_id%TYPE
                ,       p_action                IN VARCHAR2
                ,       p_business_date         IN DATE
                ,       p_rec_entity            IN agre_defn_table
		,	p_rec_map		IN agre_map_defn_table
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
RETURN NUMBER;

FUNCTION fn_new_agre
                (
                        p_rec_entity            IN agre_defn_table
		,	p_rec_map		IN agre_map_defn_table
                ,       p_err_code              IN OUT  VARCHAR2
                ,       p_err_message           IN OUT  VARCHAR2
                )
RETURN NUMBER;


END edpk_channel_defn;
/
