package com.ope.patu.payments.lmp300;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.core.PaymentProcessor;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;

/**
 * This is the main file for Bill Payment Service validation
 * @author anandkumar.b
 *
 */
public class LMP300Processor implements PaymentProcessor 
{
	public Object process(Object... objects) 
	{
		/**
		 * Provide the actual implementation for
		 * the specific bill payments
		 */
		String actualDataContents = ( String )objects[0];
		Session session = ( Session )objects[1];

		PaymentServiceValidator paymentValidator = new PaymentServiceValidator();
		List<String> contentsList = CommonUtil.getLinesList(actualDataContents);
		paymentValidator.billPaymentService(contentsList,	session);
		
		return null;
	}
	
public Map getServiceIdData(String dataContent, Session session){
		
		Map map = new HashMap<String, String>();
		String serviceId = null;
		String serviceIdString = null;
		String payersBusinessIdentityCode = null;
		List<String> contentsList = CommonUtil.getLinesList(dataContent);
		String batchRecord = (String) contentsList.get(0);
		serviceId =  batchRecord.substring(224,234);
		payersBusinessIdentityCode = batchRecord.substring(20,29);
		/* If Service is blank in the Payement file the use
		 * Payers Business Identity Code as a Service Id  
		 */
		if (serviceId.trim().length() == 0) {
			serviceIdString = payersBusinessIdentityCode;
		} else {
			serviceIdString = serviceId;
		}
		map.put(ServerConstants.SERVICEID, serviceIdString);
		map.put(ServerConstants.PAYERS_BUSINESS_ID_CODE, payersBusinessIdentityCode);
		
		return map;
	}
}
