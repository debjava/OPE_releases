package com.ope.patu.payment.core;

import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import com.ope.patu.payments.lmp300.PaymentConstants;
import com.ope.patu.payments.ts.SalaryConstants;

import java.util.List;
import java.util.Properties;

/**
 * 
 * @author anandkumar.b
 * Generic file for generating the bill payment feed back and acknowledgment
 */
public class VelocityTemplate {

	protected static Logger logger = Logger.getLogger(VelocityTemplate.class);

	/** Generates the feed back file and acknowledgment for bill payment service 
	 * This method
	 * @param list
	 * @param feedbackPath
	 * @param serviceName
	 * @return 
	 */
	public String callVelocityTemplate(List list, String feedbackPath,
			String serviceName) {
		String rootFilePath = PaymentConstants.CONFIG_FILE + File.separator + PaymentConstants.VELOCITY + File.separator;
		String textcontents = "";
		logger.debug("Root File Path ::::::" + rootFilePath);
		String templateFile = "";
		
		/*if (serviceName.equals(PaymentConstants.BPS)) {
			templateFile = rootFilePath + PaymentConstants.BillPayemntsFeedbackTemplate;
		} else if (serviceName.equals(PaymentConstants.SPS)) {
			templateFile = rootFilePath
			+ PaymentConstants.SalaryPayemntsFeedbackTemplate;
		} else if (serviceName.equals(PaymentConstants.ACK)) {
			templateFile = rootFilePath + PaymentConstants.PaymentsAcknowledgement;
		} else if (serviceName.equals(PaymentConstants.ACK_CBP)) {
			templateFile = rootFilePath + PaymentConstants.PaymentsAcknowledgement;
		}*/
		
		if (serviceName.equals(PaymentConstants.BPS)) {
			templateFile =  PaymentConstants.BillPayemntsFeedbackTemplate;
		} else if (serviceName.equals(PaymentConstants.SPS)) {
			templateFile = PaymentConstants.SalaryPayemntsFeedbackTemplate;
		} else if (serviceName.equals(PaymentConstants.ACK)) {
			templateFile =PaymentConstants.PaymentsAcknowledgement;
		} else if (serviceName.equals(PaymentConstants.ACK_CBP)) {
			templateFile = PaymentConstants.PaymentsAcknowledgement;
		}
		logger.debug("Template FileName : " + templateFile);
		//Velocity init method calls here and read the properties file
		VelocityEngine ve=null;
		try {
			Properties velocityProperties = new Properties();  
			velocityProperties.setProperty("file.resource.loader.path",  
			    rootFilePath);  
			ve = new VelocityEngine();
			ve.init(velocityProperties);
//			ve.init(rootFilePath + "velocity.properties");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//creates the context object for transformation
		VelocityContext context = new VelocityContext();
		context.put("list", list);
		Template template = null;
		try {
			
			System.out.println("LOADING THE TEMPLATE .. "+templateFile);
			System.out.println("FROM PATH : "+ rootFilePath);
			
			template = ve.getTemplate(templateFile);
		} catch (ResourceNotFoundException rnfe) {
			rnfe.printStackTrace();
			logger.error("Example : error : cannot find template "
					+ templateFile);
		} catch (ParseErrorException pee) {
			logger.error("Example : Syntax error in template "
					+ templateFile + ":" + pee);
		}catch(Exception e){
			logger.error("Error in velocity template :::"+e.getMessage());
		}
		logger.debug("Feed back path ::::::::" + feedbackPath);

		try {
			if (template != null)
				if (serviceName.equals(PaymentConstants.BPS) || serviceName.equals(SalaryConstants.FB_SPS)) {
					StringWriter writer = new StringWriter();
					template.merge(context, writer);
					textcontents = writer.toString();
					writer.flush();
					writer.close();
				}else if (serviceName.equals(PaymentConstants.ACK_CBP)) {
					StringWriter writer = new StringWriter();
					template.merge(context, writer);
					textcontents = writer.toString();
					writer.flush();
					writer.close();
				} 
				else {
					Writer fos = new FileWriter(feedbackPath);
					BufferedWriter writer = writer = new BufferedWriter(fos);
					template.merge(context, writer);
					writer.flush();
					writer.close();
				}
		} catch (NullPointerException npe) {
			logger.error("Null value in velocity template ::..."+npe.getMessage());
		}catch(Exception e){
			logger.error("Exception in velocity template ::::"+e.getMessage());
		}

		return textcontents;
	}

}
