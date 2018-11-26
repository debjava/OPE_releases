package com.ope.scheduler.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import com.ope.patu.util.FileUtil;
import com.ope.patu.util.MergeFile;
import com.ope.scheduler.db.OracleDmBatch;

public class RequestOperation {
	
	private static String opeConfigProPath = null;
	private static FilePermission filePermission = null;

	protected static Logger logger = Logger.getLogger(RequestOperation.class);

//	private static String opeConfigProPath = "conf" + File.separator + "ope-config.properties";

	public static final String UNPROCESSED_FILE_STATUS = "U";
	public static final String PROCESSED_FILE_STATUS = "P";
	public static final String NOT_COLLECTED_FILE_STATUS = "G";
	public static final String COLLECTED_FILE_STATUS = "C";
	public static final String CANCELED_FILE_STATUS = "X";

	public static final String TXT_FILE_EXTSN = ".txt";
	
	
	static {
		
		String confLoaction = null;
	    InputStream is = null;
	    
	    is = RequestOperation.class.getResourceAsStream("/ope-setup.properties");
		
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
		

	/**
	 * function <b>doTask()</b> <br>
	 * This function is used to perform following operation..
	 * <ol>
	 * <li> Pickup the unprocessed file from the PATU-AREA.
	 * <p>
	 * Unprocessed file ex - <b>ABC_U</b>
	 * <li> Move this file into the DM-AREA.
	 * <p>
	 * File Name in DM-PRO folder - <b>"Service-Code"_"Current-Date".txt</b>
	 * <li> After transferred the file successfully run the DM Batch for
	 * specific Service Id.
	 * <li> Rename the file of PATU-AREA.
	 * <p>
	 * Processed file ex - <b>ABC_P</b>
	 * <li> Show the errors in case any problem.
	 */
	
	public static void main(String[] args) {
		doTask();
	}
	
	public static void initDir() {
		try {
			logger.debug("Inside initDir to Create/Check the all dirs inside the DM-AREA");
			Properties properties = new Properties();
			properties.load(new FileInputStream(opeConfigProPath));
			// properties.load(new FileInputStream("D:/OPE/opeserver_Aug-19/opeserver_Dec-12_12-40/conf/ope-config.properties"));
			
			String opeDMRoot = properties.getProperty("dm.data.root").toString();
			String configuredServiceName = properties.getProperty("configure.service.name");
			
			if (configuredServiceName.length() > 0) {
				StringTokenizer token = new StringTokenizer(configuredServiceName, ",");
				while (token.hasMoreTokens()) {
					String serviceName = token.nextToken();
					String dmServiceDirPath = opeDMRoot + File.separator + serviceName;
					File dir = new File(dmServiceDirPath);
					File serviceDir = new File(dmServiceDirPath);
					// boolean flag = false;
					if(dir.exists())
						logger.debug("DIR ALREADY EXISTS For ->"+ serviceName);
					else {
						logger.debug("DIR DOES NOT EXISTS For ->"+ serviceName);
						if(serviceDir.mkdir())
							logger.debug("DIR CREATED For ->"+ serviceName);
					}
					//Added by Debadatta Mishra
					setRights(dir);
					setRights(serviceDir);
					checkSubDMdir(serviceDir,properties,serviceName);
				}
			}
			
		} catch(Exception e){
			logger.debug("Caught in an Exception->"+ e.getMessage());
		}
	}
	
	public static void checkSubDMdir(File serviceDir, Properties properties, String serviceType) {
		try {
			logger.debug("Inside checkSubDMdir method");
			String dmSubDirNames[] = new String[4];
			dmSubDirNames[0] = properties.getProperty("ope.dm.pro.dir.name").toString();
			dmSubDirNames[1] = properties.getProperty("ope.dm.res.dir.name").toString();
			dmSubDirNames[2] = properties.getProperty("ope.dm.wip.dir.name").toString();
			dmSubDirNames[3] = properties.getProperty("ope.dm.picked.dir.name").toString();
			
			String serviceDirPath = serviceDir.getAbsolutePath();
			for (int i = 0; i < dmSubDirNames.length; i++) {
				if( !(new File(serviceDirPath + File.separator + dmSubDirNames[i].trim()).exists()) )
				{	logger.debug("Create "+ dmSubDirNames[i] + " dir inside "+ serviceType +", because IT DOES NOT EXISTS");
//					new File(serviceDirPath + File.separator + dmSubDirNames[i].trim()).mkdir();
				//Added by Debadatta Mishra
				File tempFile = new File(serviceDirPath + File.separator + dmSubDirNames[i].trim());
				tempFile.mkdirs();
				setRights(tempFile);
				}
			}
		} catch(Exception e){
			logger.debug("Caught in an Exception->"+ e.getMessage());
		}
	}


	@SuppressWarnings("unchecked")
	public static void doTask() {

		logger.debug("Inside Request Operation : doTask");
		initDir();
		
		try {

			Properties properties = new Properties();
			properties.load(new FileInputStream(opeConfigProPath));
			// properties.load(new FileInputStream("D:/OPE/opeserver_Aug-19/opeserver_Dec-12_12-40/conf/ope-config.properties"));
			
			File dataDir = new File(properties.getProperty("patu.data.root"));
			
			String configuredServiceName = properties.getProperty("configure.service.name");
									
			logger.debug("Configured Service Names in OPE ->"+configuredServiceName);
			List prcessedFileList = (List) new ArrayList(); 
			if (configuredServiceName.length() > 0) {
				StringTokenizer token = new StringTokenizer(configuredServiceName, ",");
				while (token.hasMoreTokens()) {
					String serviceName = token.nextToken();
					logger.debug("Looking serviceName ->"+serviceName);
					String fileToSearch = new StringBuffer().append(serviceName).append("_").append(UNPROCESSED_FILE_STATUS).toString();
					List<File> list = (List) MergeFile.getFileList(dataDir, fileToSearch);
					// list.removeAll(list);
					String date = null; 
					date = getCurrentDate(properties.get("date.format").toString());
					prcessedFileList = getCurrentDateFolderList(list,properties,date);
					
					for (Iterator iter = prcessedFileList.iterator();  iter.hasNext();) {
						File file = (File) iter.next();
						 						
						if((transferFile(file, properties, serviceName, date)) && (new OracleDmBatch().runDmBatch(properties, serviceName)) ) {	
							logger.debug("----@ FILE TRANSFERRED & BATCH RUN :: SUCCESSFULLY @----");
							String renameFilePath = new StringBuffer().append(file.getParent()).append(File.separator).append(serviceName).append("_").append(PROCESSED_FILE_STATUS).toString();
							file.renameTo(new File (renameFilePath));
						} else
						{	logger.debug("----@ FILE TRANSFERRED & BATCH RUN :: FAILED @----");	}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Caught in Exception" + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private static List getCurrentDateFolderList(List fileList,Properties properties,String date) {
		// logger.debug("BEFORE fileList.size()-> "+fileList.size());
		Iterator<File> fileListIterator = fileList.iterator();
		while (fileListIterator.hasNext()) {
			File file = (File) fileListIterator.next();
			String absolutePath = file.getAbsolutePath();
			if ( ((date != null) && (!(absolutePath.contains(date)))) ) {
				// logger.debug("Remove the reference from the iterator");
				logger.debug("It is not contain the current date folder");
				fileListIterator.remove();
			}
		}
		// logger.debug("AFTER fileList.size()-> "+fileList.size());
		return fileList;
	}
	
	/**
	 * This function is used to transfer a file from PATU-AREA to DM-AREA.
	 * 
	 * @param File : unprocessedFile
	 * @param Properties : properties
	 * @param String : serviceType
	 * @param String : String date
	 *
	 * @return boolean
	 */

	@SuppressWarnings("unused")
	private static boolean transferFile(File unprocessedFile, Properties properties, String serviceType, String date) {
		boolean status = false;
	
		try {
			/* 
			 * File Name convention <ServiceType+PresentDate>.txt
			 * Ex -> LMP300081209.txt
			 * Reason for ".txt" -> DM can upload only ".txt" files.  
			 */
			String fileName = new StringBuffer().append(serviceType).append(date).append(TXT_FILE_EXTSN).toString();
		
			File dmUploadDir = new File(properties.getProperty("dm.data.root"));
			File dmUploadDirArray[] = dmUploadDir.listFiles();
	
			logger.debug("Scheduler is looking " + serviceType + " dir inside DM-AREA");
	
			for (int i = 0; i < dmUploadDirArray.length; i++) {
				if (dmUploadDirArray[i].isDirectory() && (dmUploadDirArray[i].getName().equals(serviceType))) {
					String destinationLocation = new StringBuffer().append(dmUploadDirArray[i].getAbsolutePath()).append(File.separatorChar).append(properties.getProperty("ope.dm.pro.dir.name")).append(File.separatorChar).append(fileName).toString();
					status = copyFile(unprocessedFile, destinationLocation);
				}
			}
		} catch (Exception e) {
			logger.debug("Caught in Exception :: transferFile :: "+e.getMessage());
			status = false;
		}

		if(status)
			logger.debug("----@ FILE TRANSFERRED SUCCESSFULLY @----");
		else 
			logger.debug("----@ FILE TRANSFERRED FAILED @----");
		
		return status;
	}
	
	
	/**
	 * This function is used to get current system date.
	 * 
	 * @param String :
	 *            dateFormat
	 * @return String
	 */

	private static String getCurrentDate(String dateFormat) {

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String currentDate = sdf.format(today);

		return currentDate;
	}

	/* Old method need to remove after the testing */

	/*
	 * private static boolean chkReceivedFileStatus(String fileName) {
	 * if(fileName.indexOf("_") != -1) { if( (fileName.substring(
	 * fileName.lastIndexOf('_')+1, fileName.length() ) ).equalsIgnoreCase(
	 * UNPROCESSED_FILE_STATUS+FILE_TYPE) ) return true; else return false; }
	 * else { return false; } }
	 */

	/**
	 * This function is used to check received file status.
	 * 
	 * @param String :
	 *            fileName
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private static boolean chkReceivedFileStatus(String fileName) {
		boolean status = false;
		logger.debug("chkReceivedFileStatus :: fileName->" + fileName);
		if (fileName.indexOf(".") == -1) {
			if ((fileName.substring(fileName.lastIndexOf('_') + 1, fileName
					.length())).equalsIgnoreCase(UNPROCESSED_FILE_STATUS))
				status = true;
			else
				status = false;
		} else if (fileName.indexOf(".") != -1) {
			if ((fileName.substring(fileName.lastIndexOf('_') + 1, fileName
					.lastIndexOf('.')))
					.equalsIgnoreCase(UNPROCESSED_FILE_STATUS))
				status = true;
			else
				status = false;
		}

		return status;
	}

	/**
	 * This function is used to copy one file from one location to another
	 * location.
	 * 
	 * @param File :
	 *            sourceFile
	 * @param String :
	 *            fileName
	 * @return void
	 */

	private static boolean copyFile(File sourceFile, String destinationLocation) {
		boolean status = false;
		/*
		 * Added by Debadatta Mishra to resolve the issue related to character
		 * encoding.
		 */
		try
		{
			FileUtil.copyFileInUTF8(sourceFile, destinationLocation);
			status = true;
			logger.debug("%%%%%%% -: C O P Y = D O N E :- %%%%%%%");
		}
		catch( Exception e )
		{
			logger.debug("Caught in an Exception to COPY the file :: "+e.getMessage());
			status = false;
		}
		
//		try {
//
//			if (destinationLocation != null) {
//				File destinationFile = new File(destinationLocation);
//
//				FileReader in = new FileReader(sourceFile);
//				FileWriter out = new FileWriter(destinationFile);
//				int c;
//
//				while ((c = in.read()) != -1)
//					out.write(c);
//
//				in.close();
//				out.close();
//				
//				status = true;
//				logger.debug("%%%%%%% -: C O P Y = D O N E :- %%%%%%%");
//			}
//		} catch (Exception e) {
//			logger.debug("Caught in an Exception to COPY the file :: "+e.getMessage());
//			status = false;
//		}
		
		return status;
	}

	/**
	 * This function is used to get actual file name means without any status.
	 * 
	 * @param File :
	 *            sourceFile
	 * @return String
	 */

	@SuppressWarnings("unused")
	private static String getActualFileName(File sourceFile) {

		String fileName = sourceFile.getName();

		if (fileName.indexOf("_") != -1) {
			return (fileName.substring(0, fileName.lastIndexOf('_')));
		} else {
			return null;
		}
	}
	
	/**Method used to provide read,write and execute permission
	 * in the directory structure.
	 * @param dir of type {@link File}
	 * @author Debadatta Mishra
	 */
	public static void setRights( File dir )
	{
		if( dir.isDirectory())
		{
			filePermission = new FilePermission( dir.getAbsolutePath()+File.separator+"*","read,write,execute,delete");
		}
		else
		{
			filePermission = new FilePermission( dir.getAbsolutePath(),"read,write,execute,delete");
		}
//		dir.setReadable(true, true);
//		dir.setWritable(true, true);
//		dir.setExecutable(true,true);
	}

}