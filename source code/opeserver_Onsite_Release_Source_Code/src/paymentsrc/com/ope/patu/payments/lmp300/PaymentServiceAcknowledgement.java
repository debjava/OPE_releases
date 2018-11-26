//package com.ope.patu.payments.lmp300;
//import java.io.File;
//import java.util.*;
//
//import com.coldcore.coloradoftp.session.Session;
//import com.ope.patu.server.constant.ServerConstants;
//import com.ope.patu.util.CommonUtil;
//import com.ope.patu.util.FileUtil;
//public class PaymentServiceAcknowledgement {
//
//	public static void paymentServiceAcknowledgement( Object ... objects )
//	{
//		Map ackMap = ( Map )objects[0];
//		Session session = ( Session )objects[1];
//		StringBuilder datas = new StringBuilder();
//		String totalData="";
//		Iterator iter = ackMap.entrySet().iterator();
//		while(iter.hasNext())
//		{
//			Map.Entry me = ( Map.Entry )iter.next();
//			   try{
//			    if(me.getValue().equals("rejected")){
//				  if(me.getKey().equals("no_of_trans"))
//				  {
//					String no_of_trans = CommonUtil.pad("0", 6, " ");
//					datas.append(no_of_trans); 
//				  }
//				  else if(me.getKey().equals("sum_trans"))
//				  {
//					String sum_trans = CommonUtil.pad("0", 16, " ");
//					datas.append(sum_trans); 
//				  }
//				  else if(me.getKey().equals("rejected"))
//				  {
//					
//				  }
//			   }else{
//					datas.append(me.getValue());
//					totalData = datas.toString();
//			   }
//			}catch(NullPointerException npe){
//				npe.printStackTrace();
//			}
//		}
//		String agmtServicePath = ( String )session.getAttribute(ServerConstants.AGMTSERVICECODEPATH);
//		String filePath = agmtServicePath+File.separator+"U_KUITTAUS";
//		FileUtil.writeContents(filePath, totalData);
//	}
//}
//
