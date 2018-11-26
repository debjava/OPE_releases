package com.ope.patu.server.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.coldcore.coloradoftp.filesystem.FailedActionException;
import com.coldcore.coloradoftp.filesystem.FailedActionReason;
import com.coldcore.coloradoftp.filesystem.FileSystem;
import com.coldcore.coloradoftp.filesystem.ListingFile;
import com.coldcore.coloradoftp.filesystem.impl.ListingFileBean;
import com.coldcore.coloradoftp.plugin.xmlfs.User;
import com.coldcore.coloradoftp.plugin.xmlfs.VirtualFolder;
import com.coldcore.coloradoftp.plugin.xmlfs.XmlFS;
import com.coldcore.coloradoftp.plugin.xmlfs.parser.ParsingException;
import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;
import com.coldcore.misc5.CFile;
import com.ope.patu.filesystem.FileRetriever;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.util.FTPFileUtil;

public class PATUFileSystem implements FileSystem
{
	private static Logger log = Logger.getLogger( PATUFileSystem.class );
	protected XmlFS xmlFS;
	protected String filesOwner;
	protected String filesPermissions;
	protected String mlsxFileFacts;
	protected String mlsxDirFacts;
	
	private int counter = 1;


	public PATUFileSystem(String filename) throws FileNotFoundException, ParsingException {
		String confFilePath = System.getProperty("user.dir") + File.separator + "conf";
		confFilePath = confFilePath + File.separator +  filename;
		xmlFS = new XmlFS();
		xmlFS.initialize(confFilePath);
		filesOwner = "ftp";
		filesPermissions = "rwxrwxrwx";
		mlsxDirFacts = "cdeflp";
		mlsxFileFacts = "adfrw";
	}


	/** Default files owner when listing directory content
	 * @param owner Owner name
	 */
	public void setFilesOwner(String owner) {
		if (owner == null) throw new IllegalArgumentException("Invalid owner");
		filesOwner = owner;
	}


	/** Default files owner when listing directory content
	 * @return Owner name
	 */
	public String getFilesOwner() {
		return filesOwner;
	}


	/** Default files permissions when listing directory content
	 * @param permissions Permissions
	 */
	public void setFilesPermissions(String permissions) {
		if (permissions == null) throw new IllegalArgumentException("Invalid permissions");
		filesPermissions = permissions;
	}


	/** Default files permissions when listing directory content
	 * @return Permissions
	 */
	public String getFilesPermissions() {
		return filesPermissions;
	}


	public String getCurrentDirectory(Session userSession) 
	{
		log.info("PATUFileSystem : getCurrentDirectory-------"+getCurrentuserDir(userSession));
		return getCurrentuserDir(userSession);
//		return xmlFS.getVirtualPathResolver().getCurrentVirtualDirectory(userSession);
	}


	public String toAbsolute(String path, Session userSession) throws FailedActionException {
		return xmlFS.getVirtualPathResolver().virtualPathToAbsolute(path, userSession);
	}


	public String getParent(String path, Session userSession) throws FailedActionException {
		String apath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(path, userSession);
		if (apath == null)
			throw new FailedActionException(FailedActionReason.SYSTEM_ERROR, "Cannot convert path.");

		return xmlFS.getVirtualPathResolver().getVirtualParent(path);
	}


	public Set<ListingFile> listDirectory(String dir, Session userSession) {

		User user = xmlFS.findUser(userSession);
		if (user == null)
			throw new FailedActionException(FailedActionReason.OTHER, "No file system entry.");

		String apath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(dir, userSession);
		String rpath = xmlFS.getRealPathResolver().virtualPathToReal(apath, user.getHome());
		if (rpath == null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path not found.");

		//Permissions
		if (!xmlFS.getPermissionsManager().canAccessDirectory(rpath, user.getHome()))
			throw new FailedActionException(FailedActionReason.NO_PERMISSIONS, "Access denied.");

		
		File file = new File(rpath);
		if (!file.isDirectory())
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path is a file.");

		Set<ListingFile> set = new HashSet<ListingFile>();
		File[] list = file.listFiles();
		for (File f : list) {
			//Test if listing is allowed
			if (f.isDirectory() && !xmlFS.getPermissionsManager().canListDirectory(f.getAbsolutePath(), user.getHome())) continue;
			if (f.isFile() && !xmlFS.getPermissionsManager().canListFile(f.getAbsolutePath(), user.getHome())) continue;
			String fapath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(apath+"/"+f.getName(), userSession);
			String frpath = f.getAbsolutePath();
			ListingFile lf = createListingFile(fapath, frpath);
			if (lf != null) set.add(lf);
		}

		//Add virtual folders
		if (apath.equals("/")) {
			Set<VirtualFolder> vfolders = user.getHome().getVirtualFolders();
			for (VirtualFolder vf : vfolders) {
				ListingFile lf = createListingFile(apath, vf.getPath());
				if (lf != null) {
					lf.setName(vf.getName());
					lf.setAbsolutePath("/"+vf.getName());
					set.add(lf);
				}
			}
		}

		log.debug("Directory listed: "+rpath);

		return set;
	}


	public ListingFile getPath(String path, Session userSession) throws FailedActionException 
	{
		User user = xmlFS.findUser(userSession);
		if (user == null)
			throw new FailedActionException(FailedActionReason.OTHER, "No file system entry.");

		String apath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(path, userSession);
		String rpath = xmlFS.getRealPathResolver().virtualPathToReal(apath, user.getHome());
		if (rpath == null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path not found.");

		return createListingFile(apath, rpath);
	}


	/** Create listing file object
	 * @param apath Absolute virtual path
	 * @param rpath Absolute real path
	 * @return Listing file or NULL if does not exist or cannot be converted
	 */
	protected ListingFile createListingFile(String apath, String rpath) {
		File file = new File(rpath);
		if (!file.exists()) return null;

		ListingFile lf = new ListingFileBean();
		lf.setName(apath.equals("/")?"/":file.getName());
		lf.setAbsolutePath(apath);
		lf.setOwner(filesOwner);
		lf.setDirectory(file.isDirectory());
		lf.setPermissions(filesPermissions);
		if (file.isFile()) lf.setSize(file.length());
		lf.setLastModified(new Date(file.lastModified()));
		lf.setMlsxFacts(file.isDirectory()?mlsxDirFacts:mlsxFileFacts);

		return lf;
	}

	public String changeDirectory(String dir, Session userSession) {
	    User user = xmlFS.findUser(userSession);
	    if (user == null)
	      throw new FailedActionException(FailedActionReason.OTHER, "No file system entry.");

	    String apath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(dir, userSession);
	    String rpath = xmlFS.getRealPathResolver().virtualPathToReal(apath, user.getHome());
	    if (rpath == null)
	      throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path not found.");

	    //Permissions
	    if (!xmlFS.getPermissionsManager().canAccessDirectory(rpath, user.getHome()))
	      throw new FailedActionException(FailedActionReason.NO_PERMISSIONS, "Access denied.");

	    File file = new File(rpath);
	    if (!file.isDirectory())
	      throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path is a file.");

	    userSession.setAttribute(SessionAttributeName.CURRENT_DIRECTORY, apath);
	    log.debug("User switched to directory: "+rpath);

	    return apath;
	  }

	public void deletePath(String path, Session userSession) 
	{
		/*
		 * Custom implementation made by
		 * Debadatta Mishra
		 */
//		String rpath = getFileNamePath(userSession, path);
//		String rpath = FileUtil.getCurrentUserFilePath(userSession, path);
		
				
		Set set = userSession.getAttributeNames();
		Iterator it = set.iterator();
		
//		while (it.hasNext()){
//			String value = (String) it.next();
//			log.debug("SESSION ATTRIBUTE NAME ->	: "+ value);
//			log.debug("SESSION ATTRIBUTE VALUE ->	: "+ userSession.getAttribute(value));
//		}
		
		String rpath = FTPFileUtil.getSessionTempPath(userSession, path) ;
		
		File file = new File(rpath);
		new CFile(file).delete();
		log.debug("Path deleted: "+rpath);
	}


	public String createDirectory(String dir, Session userSession)
	{
		log.debug("createDirectory : dir--------->>>"+dir);
		User user = xmlFS.findUser(userSession);
		if (user == null)
			throw new FailedActionException(FailedActionReason.OTHER, "No file system entry.");

		if (dir.endsWith("/"))
			throw new FailedActionException(FailedActionReason.INVALID_INPUT, "Invalid directory name.");

		String apath = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(dir, userSession);
		log.debug("createDirectory : apath--------->>>"+apath);
		String rpath = xmlFS.getRealPathResolver().virtualPathToReal(apath, user.getHome());
		log.debug("createDirectory : rpath--------->>>"+rpath);
		if (rpath != null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path already exists.");

		if (xmlFS.getVirtualPathResolver().isVirtualFolder(apath, user.getHome()))
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path already exists.");

		String parent = getParent(apath, userSession);
		log.debug("createDirectory : parent--------->>>"+parent);
		rpath = xmlFS.getRealPathResolver().virtualPathToReal(parent, user.getHome());
		log.debug("createDirectory : rpath--------->>>"+rpath);
		if (rpath == null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Directory not found.");

		rpath = rpath+"/"+apath.substring(apath.lastIndexOf("/")+1);
		log.debug("createDirectory : rpath after--------->>>"+rpath);
		File file = new File(rpath);
		rpath = file.getAbsolutePath();

		//Permissions
		if (!xmlFS.getPermissionsManager().canCreateDirectory(rpath, user.getHome()))
			throw new FailedActionException(FailedActionReason.NO_PERMISSIONS, "Access denied.");

		file.mkdirs();
		log.debug("Directory created: "+rpath);

		return apath;
	}


	public String renamePath(String from, String to, Session userSession) 
	{
		User user = xmlFS.findUser(userSession);
		if (user == null)
			throw new FailedActionException(FailedActionReason.OTHER, "No file system entry.");

		if (from.endsWith("/"))
			throw new FailedActionException(FailedActionReason.INVALID_INPUT, "Invalid path name.");

		if (to.endsWith("/"))
			throw new FailedActionException(FailedActionReason.INVALID_INPUT, "Invalid path name.");

		String apathF = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(from, userSession);
		String rpathF = xmlFS.getRealPathResolver().virtualPathToReal(apathF, user.getHome());
		if (rpathF == null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path not found.");

		String apathT = xmlFS.getVirtualPathResolver().virtualPathToAbsolute(to, userSession);
		String rpathT = xmlFS.getRealPathResolver().virtualPathToReal(apathT, user.getHome());
		if (rpathT != null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path already exists.");

		if (xmlFS.getVirtualPathResolver().isVirtualFolder(apathF, user.getHome()))
			throw new FailedActionException(FailedActionReason.NO_PERMISSIONS, "Path is a virtual folder.");

		if (xmlFS.getVirtualPathResolver().isVirtualFolder(apathT, user.getHome()))
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path already exists.");

		File fileF = new File(rpathF);

		String parent = getParent(apathT, userSession);
		rpathT = xmlFS.getRealPathResolver().virtualPathToReal(parent, user.getHome());
		if (rpathT == null)
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Directory not found.");

		rpathT = rpathT+"/"+apathT.substring(apathT.lastIndexOf("/")+1);
		File fileT = new File(rpathT);
		rpathT = fileT.getAbsolutePath();

		//Permissions
		boolean allowed;
		allowed = fileF.isDirectory() ?
				xmlFS.getPermissionsManager().canRenameDirectory(rpathF, user.getHome()) && xmlFS.getPermissionsManager().canCreateDirectory(rpathT, user.getHome()) :
					xmlFS.getPermissionsManager().canRenameFile(rpathF, user.getHome()) && xmlFS.getPermissionsManager().canCreateFile(rpathT, user.getHome());
				if (!allowed)
					throw new FailedActionException(FailedActionReason.NO_PERMISSIONS, "Access denied.");

				fileF.renameTo(fileT);
				log.debug("Path ("+rpathF+") renamed: "+rpathT);

				return apathT;
	}

	

	/* (non-Javadoc)
	 * @see com.coldcore.coloradoftp.filesystem.FileSystem#readFile(java.lang.String, long, com.coldcore.coloradoftp.session.Session)
	 */
	public ReadableByteChannel readFile(String filename, long position, Session userSession) 
	{
	    String rpath = FileRetriever.getFilePath(filename, userSession);
//	    log.debug("Raptha---------"+rpath);

		File file = new File(rpath);
		if (file.exists() && file.isDirectory())
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path is a directory.");
		FileChannel fc;
		try {
			fc = new RandomAccessFile(file, "r").getChannel();
			fc.position(position);
		} catch (Throwable e) {
			log.error("Cannot create (r) file channel", e);
			throw new FailedActionException(FailedActionReason.SYSTEM_ERROR);
		}

		//Just in case, to be used by external components
		userSession.setAttribute("transferred.file", file);

		log.debug("File channel (r) mounted to: "+rpath);
		return fc;
	}
	
	private String getCurrentuserDir( Session userSession )
	{
		return ServerConstants.FILE_HOME_DIR+File.separator+(String) userSession
		.getAttribute(SessionAttributeName.USERNAME);
	}
	public WritableByteChannel saveFile(String filename, boolean append, Session userSession) 
	{
		log.debug("PATUFileSystem : filename ---->"+filename);
				
		Set set = userSession.getAttributeNames();
		Iterator it = set.iterator();
	
		/*
		 * Custom implementation provided by
		 * Debadatta Mishra
		 */
//		String rpath = getFileNamePath(userSession, filename);
//		String rpath = FileUtil.getCurrentUserFilePath(userSession, filename);
		String rpath = FTPFileUtil.getSessionTempPath( userSession, filename );
		log.debug("PATUFileSystem : rpath----------"+rpath);
		File file = new File(rpath);
		rpath = file.getAbsolutePath();
		if (file.exists() && file.isDirectory())
		{
			log.debug("Files may be present");
			throw new FailedActionException(FailedActionReason.PATH_ERROR, "Path already exists.");
		}
		FileChannel fc;
		try {
			fc = new RandomAccessFile(file, "rw").getChannel();
			if (append) fc.position(fc.size()); //Append
			else fc.truncate(0); //Overwrite
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			log.error("Cannot create (rw) file channel", e);
			throw new FailedActionException(FailedActionReason.SYSTEM_ERROR);
		}

		//Just in case, to be used by external components
		userSession.setAttribute("transferred.file", file);

		log.debug("File channel (rw) mounted to: "+rpath);
		log.debug("File channel (rw) mounted to: "+rpath);
		return fc;
	}

	public String getFileSeparator() 
	{
		return xmlFS.getVirtualPathResolver().getVirtualFileSeparator(); 
	}
}
