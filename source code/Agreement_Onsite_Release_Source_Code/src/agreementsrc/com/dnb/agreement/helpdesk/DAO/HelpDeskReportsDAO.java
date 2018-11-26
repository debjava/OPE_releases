
/********************************************************************************************
* Copyright 2008 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : HelpDeskReportsDAO.java                                     *
* Author                      : Manjunath N G                                               *
* Creation Date               : 07-Nov-2008                                                 *
* Description                 : This file serves as the Interface Class, which contains     *
*                               all the methods which are used for HelpDeskReports          *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/

/** 
 * Create or import Packages
 */

package com.dnb.agreement.helpdesk.DAO;

import com.dnb.agreement.helpdesk.DTO.PatuKeyAuditSearchDTO;
import com.dnb.agreement.helpdesk.DTO.FileTransferAuditDTO;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;

/** 
 * Interface that all HelpDeskReportsDAO methods must support
 */

public interface HelpDeskReportsDAO
{
  
	/** 
	 * Method is used to search records based on inputs given 
	 * for showPatuKeyAuditDetails Report
	 * Input Field(s) -  PatuKeyAuditSearchDTO Object,User Id, Date Format
	 */

	public String showPatuKeyAuditDetails(PatuKeyAuditSearchDTO e, String userId,String dateFormat) throws AgreementSystemException,AgreementBusinessException;
  
	/** 
	 * Method is used to search records based on inputs given 
	 * for showFileTransferAuditDetails Report
	 * Input Field(s) - FileTransferAuditDTO Object,User Id,Date Format
	 */
	public String showFileTransferAuditDetails(FileTransferAuditDTO e, String userId,String dateFormat) throws AgreementSystemException,AgreementBusinessException;
  
}