/**
 * 
 */
package com.ope.patu.payment.core;
import java.util.Map;

import com.coldcore.coloradoftp.session.Session;
/**
 * @author Debadatta Mishra
 *
 */
public interface PaymentProcessor 
{
	public Object process( Object ...objects );
	
	/** This method is added to get Service Id from 
	 *  the payment file.
	 * @param String dataContent
	 * @param Session session
	 * @author Saurabh Thakkur
	 */
	public Map getServiceIdData(String dataContent, Session session);
}
