package com.ope.patu.payments.lum2;

import java.util.List;
import com.coldcore.coloradoftp.session.Session;
import com.ope.patu.payment.core.PaymentProcessor;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.CommonUtil;
import java.util.Map;
import java.util.HashMap;

public class LUM2Processor implements PaymentProcessor {

	public Object process(Object... objects) {
		/*
		 * Provide the actual implementation for
		 * the cross border payments.
		 */
		String actualDataContents = (String) objects[0];
		Session session = (Session) objects[1];
		List<String> contentsList = CommonUtil.getLinesList(actualDataContents);

		Map returnMap = (Map) new LUM2ServiceValidator().serviceValidator(
				session, contentsList);

		return returnMap;
	}

	public Map getServiceIdData(String dataContent, Session session) {

		Map map = new HashMap<String, String>();
		String serviceIdString = null;
		String payersBusinessIdentityCode = null;
		List<String> contentsList = CommonUtil.getLinesList(dataContent);
		String batchRecord = (String) contentsList.get(0);
		
		payersBusinessIdentityCode = batchRecord.substring(7,18);
		serviceIdString = payersBusinessIdentityCode.trim(); 
		
		map.put(ServerConstants.SERVICEID, serviceIdString);
		map.put(ServerConstants.PAYERS_BUSINESS_ID_CODE, payersBusinessIdentityCode);

		return map;
	}

}
