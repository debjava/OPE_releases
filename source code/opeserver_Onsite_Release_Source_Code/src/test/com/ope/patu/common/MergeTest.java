package com.ope.patu.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import com.ope.patu.exception.FileRetrievalException;

public class MergeTest {

	/**
	 * Merges all the files provided in the list and stores the file in the
	 * provided destination
	 * 
	 * @param fileList
	 * @param dst
	 * @throws IOException
	 */
	static void merge(List<File> fileList, File dst) throws IOException {

		OutputStream out = new FileOutputStream(dst);
		Iterator<File> fileIter = fileList.iterator();
		while (fileIter.hasNext()) {
			File inFile = (File) fileIter.next();
			InputStream in = new FileInputStream(inFile);
			System.out.println("Reading file::" + inFile.getAbsolutePath());

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];

			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
		}
		out.close();
	}

	/**
	 * Method use to merge the files which are satisfying the search criteria
	 * 
	 * @param rootPath -
	 *            Root Path of the files
	 * @param serviceType -
	 *            Service Type for search
	 * @param serviceBureauId -
	 *            Service bureau id from ESI message
	 * @param serviceId -
	 *            Service id for narrowing down search
	 * @param date -
	 *            Date for files on particular date
	 * @param retrievalFlag -
	 *            8 for normal retrieval and 9 for redo retrieval
	 * @throws FileRetrievalException -
	 *             In case if service type/rootpath/sb id is not provided
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void getFile(String rootPath, String serviceType,
			String serviceBureauId, String serviceId, String date,
			int retrievalFlag) throws FileRetrievalException, IOException {
		if (rootPath.equals(null) || serviceType.equals(null))
			throw new FileRetrievalException("R001",
					"ServiceType or RootPath is not available");
		else if (serviceBureauId.equals(null))
			throw new FileRetrievalException("R002",
					"Service Bureau Id not available");

		String fileToSearch = null;
		if (retrievalFlag == 9) {
			// If the file is appended with "P" it is for re retrieval
			fileToSearch = new StringBuffer().append(serviceType).append("_")
					.append("P").toString();
		} else {
			// If the file is appended with "U" it indicates that the file has
			// not yet been retrieved
			fileToSearch = new StringBuffer().append(serviceType).append("_")
					.append("U").toString();
		}

		String destination = new StringBuffer().append(rootPath).append(
				File.separator).append(serviceBureauId).append(serviceType)
				.append("merge").toString();

		String bureauPath = new StringBuffer().append(rootPath).append(
				File.separator).append(serviceBureauId).append(File.separator)
				.toString();
		List<File> fileList = (List<File>) getFileList(new File(bureauPath),
				fileToSearch);

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
				System.out.println("Remove the reference from the iterator");
				fileListIterator.remove();
			}
		}
		// Merge all the files by passing in the fileList to the mere method
		merge(fileList, new File(destination));
	}

	/**
	 * Gets all the files in the directory(drills down recursively) which
	 * satisfy the search criteria
	 * 
	 * @param dir
	 *            Directory search
	 * @param fileToSearch
	 *            File name to search for
	 * @return Collection of file objects
	 * @throws IOException
	 *             In case of IO issues
	 */
	@SuppressWarnings("unchecked")
	public static Collection getFileList(File dir, String fileToSearch)
			throws IOException {

		// IOFileFilter dirFilter = new WildcardFileFilter("*");
		IOFileFilter dirFilter = FileFilterUtils.makeDirectoryOnly(null);
		IOFileFilter fileFilter = new RegexFileFilter(fileToSearch);
		Collection<File> fileList = FileUtils.listFiles(dir, fileFilter,
				dirFilter);

		// TrueFileFilter.INSTANCE;
		return fileList;
	}

	public static void main(String args[]) {

		String rootPath = "D:/DATA";
		String serviceBureauId = "IDEAL003";
		String serviceType = "LMP300";

		// Sample service id and Date to test
		String serviceId = "SAUTEST001";
		String date = "081118";

		ArrayList<File> fileList = new ArrayList<File>();
		File first = new File(
				"D:/DATA/IDEAL003/DNB003001/LMP300/SAUTEST001/081118/LMP300_U");
		File second = new File(
				"D:/DATA/IDEAL003/DNB003001/LMP300/SAUTEST001/081119/LMP300_U");
		fileList.add(first);
		fileList.add(second);
		try {
			// merge(fileList, dst);
			// getFileList(new File("D:/DATA/IDEAL003"));
			// Test Case 1 - Both Service id and date is null
			getFile(rootPath, serviceType, serviceBureauId, null, null, 8);

			// Test Case 2 - Service id is provided and date is null
			// getFile(rootPath, serviceType, serviceBureauId, serviceId, null,
			// 8);

			// Test Case 3 - Service id is null and date is provided
			// getFile(rootPath, serviceType, serviceBureauId, null, date, 8);

			// Test case 4 - Service id and date both are provided
			// getFile(rootPath, serviceType, serviceBureauId, serviceId, date,
			// 8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileRetrievalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}