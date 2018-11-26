/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : ServerDAO.java                                 	 	     *
 * Author                      : Debadatta Mishra                                            *
 * Creation Date               : July 18, 2008                                               *
 * Modification History        :                											 *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |      			|				 								 *
 *                       |                  |											 	 *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/
package com.ope.patu.server.db;

import java.util.Map;

import com.ope.patu.exception.DatabaseException;

/**This interface is used to provide the specification
 * for the database operation for the server.
 * @author Debadatta Mishra
 *
 */
public interface ServerDAO 
{
	/**This method is used to obtaint the user specific information
	 * from the database.
	 * @param userName of type String indicating the userID
	 * @return a Map containg the user specific information
	 */
	public Map<String, String> getUserInfo( String userName ) throws DatabaseException ;
	public void insertAuditLog( Object ... objects ) throws DatabaseException;
	public Object getESIAgmtDetails( Object...objects ) throws DatabaseException;
	public Object getServiceIdInfo( Object... objects ) throws DatabaseException;
	public Object getKeyGeneration( Object ...objects ) throws DatabaseException;
	public Object isMessageProtected( Object ...objects ) throws DatabaseException;
	public Object insertNewAukKey( Object ...objects ) throws DatabaseException;
	public Object getLanguage( Object ...objects ) throws DatabaseException;
	public Object isTimeStampUnique( Object ...objects ) throws DatabaseException;
	public Object getAgreementId( Object ...objects ) throws DatabaseException;
	public boolean isPatuIdExists(String patuId) throws DatabaseException;
	
}
