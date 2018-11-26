package com.ope.patu.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;

import com.ope.patu.server.constant.ServerConstants;

public class FileUtil {
	private static FilePermission filePermission = null;
	

//	private static String setUpFile = "conf" + File.separator + "ope-setup.properties";
	private static String rootDir = "";
	private static String homeDirPath = "";
	 
	
//	private static String homeDirPath = System.getProperty("user.dir") + File.separator + "home";
	
//	private static String rootDir = System.getProperty("user.dir");
	
	private static String dataPath = "data";

	protected static Logger logger = Logger.getLogger(FileUtil.class);

	static {
		
		callOPESetUp();
		logger.debug("In FileUtil HomeDirPath------------" + homeDirPath);
		logger.debug("In FileUtil user root path------------" + rootDir);
		File homeDirfile = new File(homeDirPath);
		if (!homeDirfile.exists())
			homeDirfile.mkdirs();
		setRights(homeDirfile);
	}

	public static void callOPESetUp() {
		
		try {
			
		InputStream is = null;
		is = FileUtil.class.getResourceAsStream("/ope-setup.properties");
			
		Properties properties = new Properties();
		properties.load(is);
		String sharedLocationPath = properties.getProperty(ServerConstants.OPE_SHARED_LOCATION_ROOT);
		rootDir = sharedLocationPath;
		homeDirPath = rootDir + File.separator + "home";
		
		} catch (Exception e) {
			e.printStackTrace();	
			logger.debug("callOPESetUp() :: Caught in an Exception ---->" + e.getMessage());
		}
		
	}

	public static String getHomeDirPath() {
		
		return homeDirPath;
	}
	
	public static String getSessionTempPath(Session userSession, String fileName) {
		String sessionTempPath = null;
		String userDirTempPath = getSessionUserPath(userSession)
				+ File.separator + "temp" + File.separator
				+ new Integer(userSession.getSessionId()).toString();
		logger.debug("userDirTempPath-----------" + userDirTempPath);
		File tempFile = new File(userDirTempPath);
		if (!tempFile.exists())
			tempFile.mkdirs();
		setRights(tempFile);
		sessionTempPath = userDirTempPath + File.separator + fileName;
		logger.debug("sessionTempPath-----------" + sessionTempPath);
		return sessionTempPath;
	}

	public static String getSessionUserPath(Session userSession) {
		String userName = (String) userSession
				.getAttribute(SessionAttributeName.USERNAME);
		userName = userName == null ? "anonymous" : userName;
		String sessionUserPath = homeDirPath + File.separator + userName;
		return sessionUserPath;
	}

	
	/**
	 * @param userSession
	 * @param fileName
	 * @param agmtId
	 * @param serviceId
	 * @return
	 * @deprecated
	 */
	public static String getAgmtServiceIdFilePath(Session userSession,
			String fileName, String agmtId, String serviceId) {
		String agmtIdServiceIdFilePath = getSessionUserPath(userSession)
				+ File.separator + agmtId + File.separator + serviceId;
		File tempFile = new File(agmtIdServiceIdFilePath);
		if (!tempFile.exists())
		{
			tempFile.mkdirs();
			setRights(tempFile);
		}
		return agmtIdServiceIdFilePath + File.separator + fileName;
	}

	public static String getAgmtServiceFilePath(String agmtId,
			String serviceCode) {
		String agmtServiceCodePath = new StringBuilder(getDataDirPath()
				+ File.separator + agmtId + File.separator + serviceCode
				+ File.separator + DateUtil.getDate()).toString();
		File dirFile = new File(agmtServiceCodePath);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
			setRights(dirFile);
		}
		return agmtServiceCodePath;
	}

	/**
	 * The new version of getAgmtServiceFilePath( String agmtId , String
	 * serviceCode )
	 * @author Saurabh Thakkur
	 * @param Map dataMap
	 * @return String
	 */

	public static String getPatuAgmtServiceFilePath(Map dataMap) {
		logger.debug("Inside getPatuAgmtServiceFilePath ==== ");

		HashMap map = (HashMap) dataMap;
		String serviceBureauId = (String) map
				.get(ServerConstants.SERVICEBUREAUID);
		String agreementId = (String) map.get(ServerConstants.AGREEMENTTID);
		String serviceType = (String) map.get(ServerConstants.SERVICECODE);
		String serviceId = (String) map.get(ServerConstants.SERVICEID);

		logger.debug("serviceBureauId=>" + serviceBureauId);
		logger.debug("agreementId=>" + agreementId);
		logger.debug("serviceType=>" + serviceType);
		logger.debug("serviceId=>" + serviceId);

		String patuAgmtServiceCodePath = new StringBuilder(getDataDirPath()
				+ File.separator + serviceBureauId + File.separator
				+ agreementId + File.separator + serviceType + File.separator
				+ serviceId + File.separator + DateUtil.getDate()).toString();
		File dirFile = new File(patuAgmtServiceCodePath);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
			setRights(dirFile);
		}
		logger.debug("patuAgmtServiceCodePath==>" + patuAgmtServiceCodePath);
		logger.debug("patuAgmtServiceCodePath==>" + patuAgmtServiceCodePath);
		
		return patuAgmtServiceCodePath;
	}

	public static String getAgmtServiceIdDir(Session userSession,
			String agmtId, String serviceId, String currentDate) {
		String agmtIdServiceIdFilePath = getSessionUserPath(userSession)
				+ File.separator + agmtId + File.separator + serviceId
				+ File.separator + currentDate;
		File tempFile = new File(agmtIdServiceIdFilePath);
		if (!tempFile.exists())
		{
			tempFile.mkdirs();
			setRights(tempFile);
		}
		return agmtIdServiceIdFilePath;
	}

	public static String getFilePath(Session userSession, String fileName) {
		String userName = (String) userSession
				.getAttribute(SessionAttributeName.USERNAME);
		userName = userName == null ? "anonymous" : userName;
		String fileNamePath = null;
		try {
			String userDir = homeDirPath + File.separator + userName;
			File file = new File(userDir);
			if (!file.exists())
			{
				file.mkdirs();
				setRights(file);
			}
			fileNamePath = userDir + File.separator + fileName;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return fileNamePath;
	}

	public static String getFileContetns(String filePath) {
		String fileContents = null;
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		try {
			fileReader = new FileReader(filePath);
			buffReader = new BufferedReader(fileReader);
			String tempString;
			StringBuffer buffer = new StringBuffer();
			while ((tempString = buffReader.readLine()) != null) {
				buffer.append(tempString);
			}
			fileContents = buffer.toString();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			logger.error(npe);
		} catch (Exception e) {
			e.printStackTrace();
			fileContents = null;
		} finally {
			try {
				buffReader.close();
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileContents;
	}

	public static void writeContents(String filePath, String fileContents) {
		
		logger.debug("FileUtil :: writeContents() : filePath ->" + filePath);
		logger.debug("FileUtil :: writeContents() : fileContents ->" + fileContents);
		FileWriter writer = null;
		BufferedWriter bf = null;
		try {
			writer = new FileWriter(filePath);
			bf = new BufferedWriter(writer);
			bf.write(fileContents);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("FileUtil :: writeContents() : Exception ->" + e.getMessage());
		} finally {
			try {
				bf.close();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("FileUtil :: writeContents() : Exception ->" +  e.getMessage());
			}
		}
	}

	public static Properties getServerConfig() {
		Properties serverProp = new Properties();
		try {
			InputStream inStream = new FileInputStream(
					ServerConstants.CONFIG_DIR + File.separator	+ "serverconfig.properties");
			serverProp.load(inStream);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger
					.error("Server config not found, please provide the configuration for server");
			logger.error(fnfe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unexpected error in getServerConfig method");
			logger.error(e);
		}
		return serverProp;
	}

	public static Properties getSecurityProperties() {
		Properties securityProp = new Properties();
		try {
			InputStream inStream = new FileInputStream(
					ServerConstants.CONFIG_DIR + File.separator
							+ "securityconstants.properties");
			securityProp.load(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return securityProp;
	}

	public static Properties getFileTypes() {
		Properties fileProp = new Properties();
		String filePath = ServerConstants.CONFIG_DIR + File.separator
				+ "filetypes.properties";
		try {
			InputStream in = new FileInputStream(filePath);
			fileProp.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileProp;
	}

	//	public static List<String> getFileTypesList()
	//	{
	//	List<String> fileTypes = new LinkedList<String>();
	//	try
	//	{
	//	String filePath = ServerConstants.CONFIG_DIR + File.separator
	//	+ "paymentfiletypes.txt";
	//	File file = new File( filePath );
	//	Scanner fileScanner = new Scanner( file );
	//	while( fileScanner.hasNext() )
	//	{
	//	fileTypes.add( fileScanner.nextLine() );
	//	}
	//	}
	//	catch( Exception e )
	//	{
	//	e.printStackTrace();
	//	}
	//	return fileTypes;
	//	}

	/**
	 * @param String filePath
	 * @return Map
	 * @deprecated instead of this use generic method 
	 * Map<String, String> getGenericMsg(String filePath)
	 */

	public static Map<String, String> getUnprotectedMsg(String filePath) {
		Map<String, String> protectedMsgMap = new HashMap<String, String>();
		try {
			File file = new File(filePath);
			StringBuilder sb = new StringBuilder();
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				sb.append(msg).append("\n");
			}
			protectedMsgMap.put("DATA_MSG", sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return protectedMsgMap;
	}

	/**
	 * @param String filePath
	 * @return Map
	 * @deprecated instead of this use generic method 
	 * Map<String, String> getGenericMsg(String filePath)
	 */
	public static Map<String, String> getProtectedMsg(String filePath) {
		Map<String, String> protectedMsgMap = new HashMap<String, String>();
		try {
			File file = new File(filePath);
			StringBuilder sb = new StringBuilder();
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				if (msg.startsWith(">>SUO")) {
					protectedMsgMap.put("SUO_MSG", msg);
				} else if (msg.startsWith(">>VAR")) {
					protectedMsgMap.put("VAR_MSG", msg);
				} else
					sb.append(msg).append("\n");

				protectedMsgMap.put("DATA_MSG", sb.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return protectedMsgMap;
	}

	/**
	 * Generic method to get data content
	 * @param String filePath
	 * @return Map
	 * @author Saurabh Thakkur
	 */

	public static Map<String, String> getGenericMsg(String filePath) {
		Map<String, String> msgMap = new HashMap<String, String>();
		try {
			File file = new File(filePath);
			StringBuilder sb = new StringBuilder();
			Scanner scanner = new Scanner(file);
			String status = ServerConstants.STATUS_NO;
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				if (msg.startsWith(">>SUO")) {
					msgMap.put("SUO_MSG", msg);
					status = ServerConstants.STATUS_YES;
				} else if (msg.startsWith(">>VAR")) {
					msgMap.put("VAR_MSG", msg);
					status = ServerConstants.STATUS_YES;
				} else
					sb.append(msg).append("\n");

				msgMap.put("MSG_STATUS", status);
				msgMap.put("DATA_MSG", sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgMap;
	}

	public static String getLineByLineFileContents(String filePath) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String msg = scanner.nextLine();
				sb.append(msg).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getDataDirPath() {
		String dataDirPath = null;
		File dataDir = new File(rootDir + File.separator + dataPath);
		if (!dataDir.exists())
		{
			dataDir.mkdirs();
			setRights(dataDir);
		}
		dataDirPath = dataDir.getAbsolutePath();
		logger.debug(dataDirPath);
		return dataDirPath;
	}

	public static void copyFile(String srcLocation, String destnLocation) {
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		FileChannel fromChannel = null;
		FileChannel toChannel = null;
		try {
			inStream = new FileInputStream(srcLocation);
			outStream = new FileOutputStream(destnLocation);
			fromChannel = inStream.getChannel();
			toChannel = outStream.getChannel();
			fromChannel.transferTo(0, fromChannel.size(), toChannel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fromChannel != null)
					fromChannel.close();
				if (toChannel != null)
					toChannel.close();
				if (inStream != null)
					inStream.close();
				if (outStream != null)
					outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**anandkumar b
	 * This method is for delete the LMP300 if bill payment service returns as rejected status  
	 * @param path
	 */
	public static void delete(String path) {
		File file = new File(path);
		file.delete();
	}

	public static String getPaymentDetailsFromTR(String filePath) {
		String paymentData = null;
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader buffReader = new BufferedReader(reader);
			buffReader.readLine();
			String tempString = null;
			StringBuffer buffer = new StringBuffer();
			while ((tempString = buffReader.readLine()) != null) {
				buffer.append(tempString);
			}
			paymentData = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paymentData;
	}

	public static int getLineNo(String filePath) {
		int count = 0;
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			LineNumberReader ln = new LineNumberReader(br);
			while (ln.readLine() != null) {
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**anandkumar b
	 * This method is use to write the LMP300_U file for bill payment service
	 * @param filePath
	 * @param contents
	 */
	public static void writePaymentFile(String filePath, List<String> contents) {
		try {
			delete(filePath);
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath,
					true));
			for (String record : contents) {
				out.write(record);
				out.write("\n");
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties getlanguage(Object... objects) {
		Properties properties = new Properties();
		String language = (String) objects[0];
		String location = null;

		try {

			location = ServerConstants.CONFIG_DIR + File.separator
					+ "PATUMessageResource_" + language + ".properties";
			properties.load(new FileInputStream(location));
			//			if(language.equals("en_GB"))
			//			{
			//			location = ServerConstants.CONFIG_DIR + File.separator+"PATUMessageResource_en.properties";
			//			location = location + File.separator + LanguageConstant.ENGLISH_FILE;
			//			logger.debug("Language-"+language);
			//			logger.debug("Location-"+location);
			//			properties.load(new FileInputStream(location));
			//			} else if(language.equals(LanguageConstant.FRANCH))
			//			{
			//			location = location + File.separator + LanguageConstant.FRANCH_FILE; 
			//			properties.load(new FileInputStream(location));
			//			logger.debug("Language-"+language);
			//			logger.debug("Location-"+location);
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;

	}
	
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

	public static String getSharedConfLocation() 
	{
		String confDirPath = ""; 
		
		InputStream is = null;
		is = FileUtil.class.getResourceAsStream("/ope-setup.properties");
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		confDirPath = prop.getProperty("ope.conf.location");
			
		return confDirPath;
	}
	
	public static String getPATUSharedLocation() 
	{
		String rootDirPath = ""; 
		
		InputStream is = null;
		is = FileUtil.class.getResourceAsStream("/ope-setup.properties");
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rootDirPath = prop.getProperty("ope.shared.location.root");
			
		return rootDirPath;
	}
	
	public static String getFTPSharedLocation() 
	{
		String rootDirPath = ""; 
		
		InputStream is = null;
		is = FileUtil.class.getResourceAsStream("/ope-setup.properties");
		
		Properties prop = new Properties();
		
		try {
			prop.load(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rootDirPath = prop.getProperty("ftp.shared.location.root");
			
		return rootDirPath;
	}
	
	public static String getFTPDataDirPath(String agmtServiceDirPath) 
	{
		String ftpDataDirPath = null;
		String ftpRootDirPath = getFTPSharedLocation();
		
		if (agmtServiceDirPath != null) {
			if(agmtServiceDirPath.lastIndexOf(ServerConstants.DATA_DIR_NAME)!= -1)
				agmtServiceDirPath = agmtServiceDirPath.substring(agmtServiceDirPath.lastIndexOf(ServerConstants.DATA_DIR_NAME));
			ftpDataDirPath = ftpRootDirPath + File.separator + agmtServiceDirPath;
		} else {
			ftpDataDirPath = ftpRootDirPath + File.separator + ServerConstants.DATA_DIR_NAME;
		}
		
		
		return ftpDataDirPath;
	}
	
	/**Method used to change the encoding of file to UTF-8
	 * @param sourceFile
	 * @param destnLocation
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 * @author Debadatta Mishra
	 */
	public static void copyFileInUTF8( File sourceFile, String destnLocation ) throws FileNotFoundException,IOException,Exception
	{
		try
		{
			InputStream inStream = new FileInputStream( sourceFile );
			OutputStream outStream = new FileOutputStream( destnLocation );
			String fromEncoding = System.getProperty("file.encoding");
			String toEncoding = "UTF-8";
			Reader reader = new BufferedReader(new InputStreamReader(inStream, fromEncoding));
			Writer writer = new BufferedWriter(new OutputStreamWriter(outStream, toEncoding));
			char[] buffer = new char[4096];
			int len;
			while ((len = reader.read(buffer)) != -1)
				writer.write(buffer, 0, len); 
			reader.close(); 
			writer.close(); 
		}
		catch( FileNotFoundException fnfe )
		{
			fnfe.printStackTrace();
			logger.error(fnfe.getMessage());
			throw fnfe;
		}
		catch( IOException ie )
		{
			ie.printStackTrace();
			logger.error(ie.getMessage());
			throw ie;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			throw e;
		}
	}
}
