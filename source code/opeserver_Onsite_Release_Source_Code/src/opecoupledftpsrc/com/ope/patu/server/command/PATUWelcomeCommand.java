/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : PATUWelcomeCommand.java                                 	 *
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

package com.ope.patu.server.command;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.command.impl.AbstractCommand;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.ope.patu.server.constant.ServerConstants;

/**This class is used to implement the System Welcome command.
 * "Welcome" command.
 * System submits this command into a command processor when user's connection is accepted by a connector.
 * @author Debadatta Mishra
 */
public class PATUWelcomeCommand extends AbstractCommand {

	protected static Logger logger = Logger.getLogger( PATUWelcomeCommand.class );
  /* (non-Javadoc)
 * @see com.coldcore.coloradoftp.command.Command#execute()
 */
public Reply execute() {
    Reply reply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
    reply.setCode("220");
    reply.setText( ServerConstants.WELCOMEMESSAGE );
    return reply;
  }


  /* (non-Javadoc)
 * @see com.coldcore.coloradoftp.command.impl.AbstractCommand#getName()
 */
public String getName() {
    return "SYSTEM (WELCOME)";
  }
}
