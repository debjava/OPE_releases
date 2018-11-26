
/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							*
 * File Name                   : ServiceBureauDAO.java                                       *
 * Author                      : Manjunath N G                                               *
 * Creation Date               : 13-August-2008                                              *
 * Description                 : This file serves as the Interface Class, which contains     *
 *                               all the methods which are used for Request OPE.             *
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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import com.dnb.agreement.DTO.ServiceBureauDTO;
import com.dnb.agreement.DTO.ServiceBureauSearchDTO;
import com.dnb.agreement.bean.ServiceBureauBean;
import com.dnb.agreement.exception.AgreementBusinessException;
import com.dnb.agreement.exception.AgreementSystemException;

/** 
 * Interface that all ServiceBureauDAO methods must support
 */

public interface ServiceBureauDAO 
{
	public boolean insert(ServiceBureauDTO e,String userId, Date businessDate,String action) throws AgreementSystemException,AgreementBusinessException;

	public List search(ServiceBureauSearchDTO e,String action) throws AgreementSystemException,AgreementBusinessException;

	public boolean delete(ServiceBureauSearchDTO e,String userId, Date businessDate,String action,String deleteMode) throws AgreementSystemException,AgreementBusinessException;

	public boolean edit(ServiceBureauDTO e,String userId, Date businessDate,String action) throws AgreementSystemException,AgreementBusinessException;	

	public List showVersion(ServiceBureauDTO e,String action) throws AgreementSystemException,AgreementBusinessException;

	public String createPatuId(ServiceBureauDTO e) throws AgreementSystemException,AgreementBusinessException;

	/**
     * method is used to generate the kek and auk keys  
     */
	public List generateKeys(ServiceBureauBean e) throws AgreementSystemException,AgreementBusinessException;
	/**
     * method is used to inserts the kek and auk keys  
     */
	public boolean insertKeys(ServiceBureauBean e,Timestamp businessDate,String versionNo,String action) throws AgreementSystemException,AgreementBusinessException;
	/**
     * method is used to re set the kek and auk keys  
     */
	public boolean resetKeys(ServiceBureauBean e) throws AgreementSystemException,AgreementBusinessException;
	/**
     * method is used to display the kek and auk keys  
     */
	public List getKekAukDisplay(String intrnalBureauId) throws AgreementSystemException,AgreementBusinessException;

	/**
     * method is used to insert the kek and auk keys  
     */
	public boolean insertAuk(String internalBureauId,String generatedAuk, java.sql.Timestamp businessDate)throws AgreementSystemException,AgreementBusinessException;

	/**
     * method is used to print the kek and auk keys  
     */
	public List printKeys(String internalBureauId )throws AgreementSystemException,AgreementBusinessException;
}