package com.ope.patu.payments.ts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.core.PaymentProcessor;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;

public class TSProcessor implements PaymentProcessor 
{
	public Object process(Object... objects) 
	{
		/**
		 * Provide the implementation for the
		 * salary payment services..
		 */
		String actualDataContents = ( String )objects[0];
		Session session = ( Session )objects[1];
		List<String> contentsList = CommonUtil.getLinesList(actualDataContents);
		SalaryServiceValidator salaryServiceValidator = new SalaryServiceValidator();
		salaryServiceValidator.getSalaryService(contentsList, session);
		return null;
	}

	public Map getServiceIdData(String dataContent, Session session) {
		// TODO Auto-generated method stub
		
		Map map = new HashMap<String, String>();
		String serviceId = null;
		String serviceIdString = null;
		String payersBusinessIdentityCode = null;
		List<String> contentsList = CommonUtil.getLinesList(dataContent);
		String transdRecord = (String) contentsList.get(0);
		serviceId = transdRecord.substring(23, 32);
		payersBusinessIdentityCode = transdRecord.substring(64, 75);
		
		/* If Service is blank in the Payement file the use
		 * Payers Business Identity Code as a Service Id  
		 */
		if (serviceId.trim().length() == 0) {
			serviceIdString = payersBusinessIdentityCode;
		} else {
			serviceIdString = serviceId;
		}
		
		System.out.println("----[ serviceIdString             ]----->"+serviceIdString);
		System.out.println("----[ payersBusinessIdentityCode  ]----->"+payersBusinessIdentityCode);
		map.put(ServerConstants.SERVICEID, serviceIdString);
		map.put(ServerConstants.PAYERS_BUSINESS_ID_CODE, payersBusinessIdentityCode);

		return map;
	}
}
