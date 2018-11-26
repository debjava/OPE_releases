
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : AgreementDAO.java                                          *
 * Author                      : Anantaraj S                                               *
 * Creation Date               : 21-July-2008                                                *
 * Description                 : This file serves as the Interface Class, which contains     *
 *                               all the methods which are used for Agreement.              *
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
package com.dnb.agreement.DAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.dnb.agreement.DTO.AgreementSearchDTO;
import com.dnb.agreement.bean.AgreementCommonBean;
import com.dnb.agreement.bean.CustomerIdBean;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;

public interface AgreementDAO {
	/**
	 * This method is used to insert Agreement deatils, while creating new record
	 * returns true, if success else false to action class
	 */
	public boolean insert(AgreementCommonBean acb,String userId,Date businessDate,String Action)throws AgreementSystemException,AgreementBusinessException;  
	/**
	 * This method is used to search the Agreements based on agreement id
	 * returns list if success, else empty list to action class 
	 */
	public List search(AgreementSearchDTO e,String action) throws AgreementSystemException,AgreementBusinessException;
	/**
	 * This method is used to delete the agreement
	 * return true if success else false to action class
	 */
	public boolean delete(AgreementSearchDTO e,String userId, Date businessDate,String action,String deleteMode) throws AgreementSystemException,AgreementBusinessException;
	/**
	 * This method is used to edit the Agreements details
	 * returns true if success, else false to action class 
	 */
	public boolean edit(AgreementCommonBean acb,String userId, Date businessDate,String action) throws AgreementSystemException,AgreementBusinessException;
	/**
	 * This method is used to get the Agreements previous and next version based on version No
	 * returns list if success, else empty list to action class 
	 */
	public List showVersion(AgreementCommonBean acb,String action)throws AgreementSystemException,AgreementBusinessException;
	/***
	 * This method is used to print the agreement details
	 */
	public List print(AgreementCommonBean acb)throws AgreementSystemException,AgreementBusinessException;
	/**
	 * clear method is used to clear the agreements services ones user clicks agreement clear
	 * @param acb
	 * @return int
	 * @throws AgreementSystemException
	 * @throws AgreementBusinessException
	 */
	public int clear(AgreementCommonBean acb)throws AgreementSystemException,AgreementBusinessException;
	
	/*
	 * Added by Debadatta Mishra
	 * Method to execute the statement.
	 */
	/**This method is used to obtain the account details from the database.
	 * @author Debadatta Mishra
	 * @param accountNo of type String indicating the account No.
	 * @param queryString of type String indicating the query
	 * @return a String
	 * @throws SQLException
	 */
	public String getAccountDetails(String accountNo,String queryString) throws SQLException;
	/**This method is used to obtain the customer datails from the database.
	 * @author Debadatta Mishra
	 * @param custId of type String indicating the customer id
	 * @param queryString of type String indicating the database query
	 * @return an object of type CustomerIdBean
	 * @throws SQLException
	 */
	public CustomerIdBean getCustomerIdDetails( String custId , String queryString ) throws SQLException;
	}
	

