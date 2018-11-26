/********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                       *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.      *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except   *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd.*
 *																							*
 * File Name                   : ServiceSpecificAccountBean.java	                        *
 * Author                      : Anantaraj							                        *
 * Creation Date               : 05-Aug-08                                                  *
 * Description                 : Bean for Service specification  							* 								 									                 
 * Modification History        :                                                            *																						    
 * Version No.               Date               Brief description of change                 *
 *----------------------------------------------------------------------------------------- *
 *                       |                  |											    *
 *                       |                  |											    *
 *  --------------------------------------------------------------------------------------- *
 ********************************************************************************************/

package com.dnb.agreement.bean;

public class ServiceSpecificAccountBean {
private String accountNum;
private String accountName;

public String getAccountName() {
	
	return accountName;
}

public void setAccountName(String accountName) {
	
	this.accountName = accountName;
}

public String getAccountNum() {
	return accountNum;
}

public void setAccountNum(String accountNum) {
	this.accountNum = accountNum;
}

}
