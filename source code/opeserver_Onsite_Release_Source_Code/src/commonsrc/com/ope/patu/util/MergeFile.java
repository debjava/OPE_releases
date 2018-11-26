package com.ope.patu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;

import com.ope.patu.exception.FileRetrievalException;
import com.ope.patu.server.beans.TransferRequestBean;
import com.ope.patu.server.constant.ServerConstants;

/**
 * Class used for merging the files.
 * @author Raja Mohan
 */

public class MergeFile {
	
	public static final String UNPROCESSED_FILE_STATUS = "U";
	public static final String PROCESSED_FILE_STATUS = "P";
	public static final String NOT_COLLECTED_FILE_STATUS = "G";
	public static final String COLLECTED_FILE_STATUS = "C";
	public static final String CANCELED_FILE_STATUS = "X";

	protected static Logger logger = Logger.getLogger(MergeFile.class);
	
	/**
	 * Merges all the files provided in the list and stores the file in the
	 * provided destination
	 * 
	 * @param fileList
	 * @param dst
	 * @throws IOException
	 */
	static boolean merge(List<File> fileList, File dst) {
		boolean status = true;
		try {
		OutputStream out = new FileOutputStream(dst);
		Iterator<File> fileIter = fileList.iterator();
		while (fileIter.hasNext()) {
			File inFile = (File) fileIter.next();
			InputStream in = new FileInputStream(inFile);
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];

			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
		}
		out.close();
		} catch (IOException e) {
			// Facing problem on READ/WRITE
			status = false;
		}
		return status;
	}

	/**
	 * @param rootPath
	 * @param serviceType
	 * @param serviceBureauId
	 * @param serviceId
	 * @param date
	 * @param retrievalFlag
	 * @throws FileRetrievalException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map getFile(String rootPath, String serviceType,
			String serviceBureauId, String serviceId, String date,
			int retrievalFlag) throws FileRetrievalException, IOException {
		Map map = (HashMap) new HashMap();
		
		logger.debug("@ - getFile :: rootPath->"+rootPath);
		logger.debug("@ - getFile :: serviceType->"+serviceType+" & serviceBureauId->"+serviceBureauId);
		logger.debug("@ - getFile :: serviceId->"+serviceId + " & date->"+date+" & retrievalFlag->"+retrievalFlag);
				
		if (rootPath==null || serviceType==null)
			throw new FileRetrievalException(" ","ServiceType or RootPath is not available");
		else if (serviceBureauId==null)
			throw new FileRetrievalException(" ","Service Bureau Id not available");

		/* 
		 * If the file is appended with "G" it indicates that 
		 * the file has not yet been retrieved where as 
		 * "C" indicates to processed/collected. 
		 */
		
		String fileToSearch = null;
		if (retrievalFlag == ServerConstants.REDO_MERGE_CONSTANT) {
			// If the file is appended with "C" it is for re retrieval
			fileToSearch = new StringBuffer().append(serviceType).append("_")
					.append(COLLECTED_FILE_STATUS).toString();
		} else {
			// If the file is appended with "U" it indicates that the file has
			// not yet been retrieved
			fileToSearch = new StringBuffer().append(serviceType).append("_")
					.append(NOT_COLLECTED_FILE_STATUS).toString();
		}
		
		/* 
		 * Making the path of complied merge file.  
		 * Appending "M" to indicate a merge file.
		 */
		String destination = new StringBuffer().append(rootPath).append(
				File.separator).append(serviceBureauId).append(serviceType).append("_")
				.append(ServerConstants.STATUS_M).toString();
		
//		String ftpFileDestination = new StringBuffer().append(FileUtil.getFTPSharedLocation()).append(
//				File.separator).append(ServerConstants.DATA_DIR_NAME).toString();
		
		String ftpFileName = new StringBuffer().append(serviceBureauId).append("_").append(serviceType).append("_")
				.append(ServerConstants.STATUS_M).toString();
		

		String bureauPath = new StringBuffer().append(rootPath).append(
				File.separator).append(serviceBureauId).append(File.separator)
				.toString();
		List<File> fileList = (List<File>) getFileList(new File(bureauPath),
				fileToSearch);
		
		
		if(!(fileList.size()==0)) {
		map.put(ServerConstants.MERGED_FILE_LIST, fileList);

		// Remove the files in case of the search criteria
		Iterator<File> fileListIterator = fileList.iterator();
			while (fileListIterator.hasNext()) {
				File file = (File) fileListIterator.next();
				String absolutePath = file.getAbsolutePath();
				if (((serviceId != null) && (!(absolutePath.contains(serviceId))))
						|| ((date != null) && (!(absolutePath.contains(date))))
						|| ((serviceId != null) && (date != null)
								&& (!(absolutePath.contains(serviceId))) && (!(absolutePath
								.contains(date))))) {
						fileListIterator.remove();
				}
			}
			
			// Merge all the files by passing in the fileList to the merge method
			if(merge(fileList, new File(destination)))
				doRenameFileList(fileList, serviceType);	
			
		} else {
		
			TransferRequestBean trBean = new TransferRequestBean();
			trBean.setFileType(serviceType);
			String errorMsg = getErrorMessage(trBean);
			logger.info("Error Message-----" + errorMsg);
			logger.debug("Error Message in FileRetriever-----"+ errorMsg);
			FileUtil.writeContents(destination,	errorMsg);
			
		}
		
		logger.debug("@ - getFile :: destination->"+destination);
		
		String ftpFileContent  = FileUtil.getLineByLineFileContents(destination);
		map.put(ServerConstants.MERGED_FILE_CONTENT, ftpFileContent);
		map.put(ServerConstants.MERGED_FILE_PATH, destination);
		map.put(ServerConstants.MERGED_FILE_NAME, ftpFileName);
//		map.put(ServerConstants.MERGED_FILE_FTP_PATH, ftpFileDestination);
		
		
		return map;
	}

	
	/**
	 * @param fileList
	 * @param serviceType
	 * @throws FileRetrievalException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void doRenameFileList(List fileList, String serviceType) throws FileRetrievalException, IOException {
		logger.debug("@- doRenameFileList :: serviceType->"+serviceType);
		logger.debug("@- doRenameFileList :: fileList Size->"+fileList.size());
		Iterator<File> fileListIterator = fileList.iterator();
		while (fileListIterator.hasNext()) {
			File file = (File) fileListIterator.next();
			// String absolutePath = file.getAbsolutePath();
			String parentPath = file.getParent();
			String renamePath = new StringBuffer().append(parentPath).append(
					File.separator).append(serviceType).append("_").append(COLLECTED_FILE_STATUS).toString(); 
				
			file.renameTo(new File(renamePath));
		}
	}
	
	/**
	 * @param dir
	 * @param fileToSearch
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Collection getFileList(File dir, String fileToSearch)
			throws IOException {
		IOFileFilter dirFilter = FileFilterUtils.makeDirectoryOnly(null);
		IOFileFilter fileFilter = new RegexFileFilter(fileToSearch);
		Collection<File> fileList = FileUtils.listFiles(dir, fileFilter,
				dirFilter);

		return fileList;
	}
	
	public static String getErrorMessage(TransferRequestBean trBean) {
		String errorMsg = null;
		try {
			String recordId = "*";
			String blank1 = " ";
			String date = DateUtil.getDate("dd.MM");
			logger.debug("In case of Error message, File Retriever Date-->"+ date);
			String blank2 = " ";
			String time = DateUtil.getDate("HH:mm");
			logger.debug("In case of Error message, File Retriever Time-->"+ time);
			String blank3 = " ";
			//Modified by Debadatta Mishra at onsite to fix the issue related to error message
			String fileType = CommonUtil.pad(trBean.getFileType(), 10, " ");//trBean.getFileType();
			String blank4 = " ";
			String notificationCode = "003";
			String blank5 = " ";
//			String notificationText = CommonUtil.pad("Requested file is no longer available", 50, " ");
			
			String notificationText = CommonUtil.pad("Requested material not available", 50, " ");
			
			logger.debug("NotificationText------>>>"+notificationText);

			errorMsg = new StringBuilder().append(recordId).append(blank1)
					.append(date).append(blank2).append(time).append(blank3)
					.append(fileType).append(blank4).append(notificationCode)
					.append(blank5).append(notificationText).toString();
		} catch (NullPointerException npe) {
			logger.error("NullPointerException thrown" + npe.getMessage());
		} catch (Exception e) {
			logger.error("Other Exception thrown" + e.getMessage());
		}
		logger.debug("errorMsg------->>>"+errorMsg);
		return errorMsg;
	}
}