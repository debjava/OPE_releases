package com.ope.scheduler.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ResponseOperation {
	
	protected static Logger logger = Logger.getLogger(ResponseOperation.class);
	
	private static String opeConfigProPath = null; 
	
	public static final String UNPROCESSED_FILE_STATUS = "U";
	public static final String PROCESSED_FILE_STATUS = "P";
	public static final String NOT_COLLECTED_FILE_STATUS = "G";
	public static final String COLLECTED_FILE_STATUS = "C";
	public static final String CANCELED_FILE_STATUS = "X";
	public static final String FILE_TYPE = ".txt";
	
	static {
		
		String confLoaction = null;
	    InputStream is = null;
	    
	    is = ResponseOperation.class.getResourceAsStream("/ope-setup.properties");
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		confLoaction  = prop.getProperty("ope.conf.location");
		opeConfigProPath = confLoaction + File.separator + "ope-config.properties";
		logger.debug("Ope-Config Properties Path ->"+opeConfigProPath);
		
	}
	
	
//	public static void main(String[] args) { doTask(); }
	
	public static void doTask() {
			try {
			Map map = null;
			Properties properties = new Properties();
			properties.load(new FileInputStream(opeConfigProPath));
			
			File file = new File(properties.getProperty("dm.data.root"));
			
			File serviceFileArray[]= file.listFiles();
			String serviceFileNames[]= file.list();
			
			logger.debug("-----Inside "+ file.getName()+" total number of Dir/Files are -> "+serviceFileNames.length);

			logger.debug("--> Now we are going inside service dir one by one --");
			for (int i = 0; i < serviceFileArray.length; i++) {
				if(serviceFileArray[i].isDirectory()) {
					logger.debug("--["+i+"]--> Service Dir --> "+ serviceFileArray[i].getName()+" ----");
					logger.debug("---- Now Inside "+ serviceFileArray[i].getName()+ ", We are going to find DM-RES -->");
					
					File responseFileArrayDir[] = serviceFileArray[i].listFiles();
													
					for (int j = 0; j < responseFileArrayDir.length; j++) {
						
						if( (responseFileArrayDir[j].isDirectory())&&(responseFileArrayDir[j].getName().equals(properties.getProperty("ope.dm.res.dir.name"))) )						{
							logger.debug("------ Now Inside "+serviceFileArray[i].getName()+" , We got "+ responseFileArrayDir[j].getName() +" Dir----");
							File responseFile[] = responseFileArrayDir[j].listFiles();
							logger.debug("--------- Now Inside DM-RES Folder we are having "+ responseFile.length+" Dirs/Files ---- ");
							for (int k = 0; k < responseFile.length; k++) {
								logger.debug("------------ Now Inside DM-RES Folder we are looking to find Response File ---- ");
								if(responseFile[k].isFile())	{
								    logger.debug("-------------- Now Inside DM-RES Folder we got the Response File with name "+responseFile[k].getName()+" ---- ");	
									map = new HashMap();
									map.put("responseFileObj", responseFile[k]);
									map.put("serviceCodeDirName", serviceFileArray[i].getName());
									map.put("configPropertyObj", properties);
									if(transferFile(map)) {
										String moveLocation = serviceFileArray[i].getCanonicalPath()+ File.separator + properties.getProperty("ope.dm.picked.dir.name")+ File.separator + responseFile[k].getName();
										logger.debug("--------------------- moveLocation-->"+moveLocation);
										if(copyFile(responseFile[k], moveLocation)) {
											responseFile[k].delete();
										}
									} else {
										logger.debug("-------------- Could not move the File =====================");
									}
								}
							}
						} else {
							// logger.debug("-------- "+ responseFileArrayDir[j].getName()+" is a Dir ----");
						}
					}
				}
				else {
					logger.debug("----"+serviceFileArray[i].getName()+" is Not a File..!!");
				}
			}
		}
		catch (Exception e) {
			logger.debug("doTask :: Caught in an Exception "+e.getMessage());
		}
	}
	
	private static boolean transferFile(Object ...objects) {
		
		logger.debug("---------------- Now We need to Transfer this File in to the PATU-AREA  ---- ");
		boolean status = false;
		
		Map dmFileMap = (Map) objects[0];
	
		File responseFile = (File)dmFileMap.get("responseFileObj");
		Properties properties = (Properties)dmFileMap.get("configPropertyObj");
				
		String resFileNameContent[] =  responseFile.getName().split("_");
		
		/*	Sample File -> SB001_AG23433_TITO_SAU23434_081129.txt
		 * 	1)  Service Bureau Id
		 *	2)  Agreement Id
		 *	3)  Service Type
		 *	4)  Service ID
		 *	5)	Date (in yymmdd ) format
		 */
				
		String serviceBureauId = null;
		String agreementId = null;
		String serviceType = null;
		String serviceId = null;
		String date = null;
		
		if(resFileNameContent.length == 5) {
			
			serviceBureauId = resFileNameContent[0];
			agreementId = resFileNameContent[1];
			serviceType = resFileNameContent[2];
			serviceId = resFileNameContent[3];
			
			if(resFileNameContent[4].indexOf('.')!= -1) {
				date = resFileNameContent[4].substring(0, resFileNameContent[4].indexOf('.'));	
			} else {
				date = resFileNameContent[4];
			}
		}
		
		try {

			logger.debug("-- Now We need to Transfer this File in to the PATU-AREA, which will have.... ");
			logger.debug("-## serviceBureauId :: " + serviceBureauId + "-## agreementId :: "+ agreementId);
			logger.debug("-## serviceType :: " + serviceType + "-## serviceId :: "+ serviceId);
			logger.debug("-## date :: " + date);
			logger.debug("-## ProcessedFile :: " +responseFile);
			
			/*
			 * Added by Debadatta Mishra
			 */
//			String fileName = serviceType+"_"+UNPROCESSED_FILE_STATUS;
			String fileName = serviceType+"_"+NOT_COLLECTED_FILE_STATUS;
			String resDirLocation = new StringBuffer().append(
					properties.getProperty("patu.data.root")).append(
					File.separator).append(serviceBureauId).append(
					File.separator).append(agreementId).append(File.separator)
					.append(serviceType).append(File.separator).append(
							serviceId).append(File.separator).append(date)
					.toString();
			String resFileLocation = new StringBuffer().append(resDirLocation).append(File.separator).append(fileName).toString();		
			
			if(!(new File(resDirLocation).exists()))	{
				logger.debug("------- Destination Location Does not exists ::::: ");
				new File(resDirLocation).mkdirs();
			}
			
			logger.debug("------- Res File Location:: "+resFileLocation);
	
			if(copyFile(responseFile,resFileLocation)) { 
				logger.debug("$$$$$$ Response Copy File :: DONE $$$$$$");
				status = true;
			} else	{ 
				logger.debug("%%%%%% Response Copy File :: FAILED %%%%%%");
				status = false;
			}
		
		} catch (Exception e) {
			logger.debug("handle exception ::" + e.getMessage());
		}
		return status;
	}
		
	private static boolean copyFile(File sourceFile, String destinationLocation)
	{	boolean status = true;
		try {
			if(destinationLocation!=null)	{
				File destinationFile = new File(destinationLocation);
	
			    FileReader in = new FileReader(sourceFile);
			    FileWriter out = new FileWriter(destinationFile);
			    int c;
	
			    while ((c = in.read()) != -1)
			      out.write(c);
	
			    in.close();
			    out.close();
			}
		} catch (FileNotFoundException e) {
			status = false;
//			e.printStackTrace();
			logger.debug("Caught in the FileNotFoundException : on Copying the File "+e.getMessage());
		} catch (Exception e) {
			status = false;
//			e.printStackTrace();
			logger.debug("Caught in the Exception : on Copying the File "+e.getMessage());
		}
		return status;
	}	
}
