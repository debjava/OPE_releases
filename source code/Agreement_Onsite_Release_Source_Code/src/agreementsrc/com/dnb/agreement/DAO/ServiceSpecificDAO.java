/********************************************************************************************
* Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
* IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
* The Program or any portion thereof may not be reproduced in any form whatsoever except    *
* as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
*																							*
* File Name                   : ServiceSpecificDAO.java                                     *
* Author                      : Anantaraj S                                                 *
* Creation Date               : 08-Aug-2008                                                 *
* Description                 : This file serves as the Interface Class, which contains     *
*                               all the methods which are used for Service specification.   *
* Modification History        :                                                             *
*																						    *
*     Version No.               Date               Brief description of change              *
*  ---------------------------------------------------------------------------------------  *
*                       |                  |											    *
*                       |                  |											    *
*  ---------------------------------------------------------------------------------------  *
*********************************************************************************************/
package com.dnb.agreement.DAO;

import java.util.Date;
import java.util.List;

import com.dnb.agreement.bean.ServiceSpecificAccountBean;
import com.dnb.agreement.bean.ServiceSpecificCommonBean;
import com.dnb.agreement.bean.ServiceSpecificCustomerBean;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;

public interface ServiceSpecificDAO {
    /**
     * This method for inserting record for new service specifications
     */
	public int insert(ServiceSpecificAccountBean ssab[],ServiceSpecificCustomerBean [] ssCustomerBean,ServiceSpecificCommonBean sscb,String userId,Date businessDate,String Action)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for editing record for service specifications
     */
	public int edit(ServiceSpecificAccountBean ssab[],ServiceSpecificCustomerBean [] ssCustomerBean,ServiceSpecificCommonBean sscb,String userId,Date businessDate,String Action)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for getting bureau name based on bureau id
     */
	public List getBureauName(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for searching service id  for specific service type
     */
	public List search(ServiceSpecificCommonBean sscb,String action)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for searching service specifications
     */
	public List getService(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for deleting service specifications from temp table
     */
	public int deleteService(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for inserting service ids into temp table
     */
	public boolean insertServiceId(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method for editing  service ids
     */
	public boolean editServiceId(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	/**
     * This method checks, service id is already existing or not in table
     */
	public int checkDuplicateServiceId(ServiceSpecificCommonBean sscb)throws AgreementSystemException,AgreementBusinessException;
	
	/**
     * This method clears the temporary table
     */
	public int clearTemporaryTable(ServiceSpecificCommonBean sscb)throws AgreementSystemException ;
	
}
