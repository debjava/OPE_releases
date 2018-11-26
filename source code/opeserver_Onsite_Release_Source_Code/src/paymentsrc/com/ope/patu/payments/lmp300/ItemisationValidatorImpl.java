//package com.ope.patu.payments.lmp300;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.ope.patu.payment.utility.ReferenceNumberValidation;
//import com.ope.patu.payments.lmp300.beans.PaymentServiceBeanRC1;
//import com.ope.patu.server.constant.ErrorConstants;
//import com.ope.patu.server.constant.PaymentConstants;
//
//
//public class ItemisationValidatorImpl implements PaymentValidator
//{
//	public Object getValidatedObject(Object... objects) 
//	{
//		Map item_map = new HashMap();
//		boolean flag=false;
//		PaymentServiceBeanRC1 pmtserbean_rc1 = ( PaymentServiceBeanRC1 )objects[0];
//		/**
//		 * Provide all the implementation for the validation
//		 */
//		int seq_no = pmtserbean_rc1.getSeq_no();
//		String payees_name = pmtserbean_rc1.getPayees_name();
//		String payees_acc_no = pmtserbean_rc1.getItemisation_acc_no();
//		String item_sum_rec = pmtserbean_rc1.getItemisation_sum();
//		String refrance_no = pmtserbean_rc1.getMessage();
//		String message_type = pmtserbean_rc1.getMessage_type();
//		String valid_ref_no="";
//		try{
//		      valid_ref_no = refrance_no.substring(0, 20);
//		  }catch(StringIndexOutOfBoundsException sie){
//			  System.out.println(sie.getMessage());
//		  }
//		  
//		if( !(pmtserbean_rc1.getItemisation_acc_no().equals(pmtserbean_rc1.getPayees_account_number()))){
//			pmtserbean_rc1.getItemisationErrorMsg().add(ErrorConstants.ITEM_ACC_NO + ",   "+ pmtserbean_rc1.getItemisation_acc_no());
//	    }
//		if(!(pmtserbean_rc1.getItemisation_msg_type().equals(pmtserbean_rc1.getMessage_type()))){
//			pmtserbean_rc1.getItemisationErrorMsg().add(ErrorConstants.ITEM_MSG_TYPE + ",   "+ pmtserbean_rc1.getItemisation_msg_type());
//		}
//		if(!(pmtserbean_rc1.getItemisation_sum().equals(pmtserbean_rc1.getSum()))){
//			pmtserbean_rc1.getItemisationErrorMsg().add(ErrorConstants.ITEM_SUM + ",   "+ pmtserbean_rc1.getItemisation_sum());
//		}
//		if(message_type.equals(PaymentConstants.ONE)|| message_type.equals(PaymentConstants.SEVEN)){
//			  //refrance number
//			  boolean ref_flag = new ReferenceNumberValidation().validateRefranceNumber(valid_ref_no);
//			  if(ref_flag==false){
//				  pmtserbean_rc1.getTransErrorMsg().add(ErrorConstants.REFERANCE_NO + ",   "+ valid_ref_no);
//			  }
//		  }
//		try{
//			if(pmtserbean_rc1.getItemisationErrorMsg().size()>0){
//				flag=true;
//			}else{
//				flag=false;
//			}
//		}catch(NullPointerException npe){
//			    flag = false;
//			npe.printStackTrace();
//		}
//		if(flag==true){
//			pmtserbean_rc1.setSeq_no(seq_no);
//			pmtserbean_rc1.setPayees_name(payees_name);
//			pmtserbean_rc1.setPayees_account_number(payees_acc_no);
//			pmtserbean_rc1.setSum(item_sum_rec);
//			pmtserbean_rc1.setRefrance_no(refrance_no);
//			item_map.put(ErrorConstants.ITEM_FAILED, pmtserbean_rc1);
//			//trans_list.add(trans_map);
//		}
//		return item_map;
//	}
//}
