/*********************************************************************************************
 * Copyright 2006 IDEAL INVENT Technologies Pvt. Ltd.                                        *
 * IDEAL INVENT Technologies Pvt. Ltd reserves all rights in the Program as delivered.       *
 * The Program or any portion thereof may not be reproduced in any form whatsoever except    *
 * as provided by license without the written consent of IDEAL INVENT Technologies Pvt. Ltd. *
 *																							 *
 * File Name                   : OracleServerDAOImpl.java                                 	 *
 * Author                      : Debadatta Mishra                                            *
 * Creation Date               : July 18, 2008                                               *
 * Modification History        :                											 *
 *																						     *
 *     Version No.               Date               Brief description of change              *
 *  ---------------------------------------------------------------------------------------  *
 *                       |      			|				 								 *
 *                       |                  |											 	 *
 *  ---------------------------------------------------------------------------------------  *
 *********************************************************************************************/
package com.ope.patu.server.db;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;

import com.ope.patu.exception.DatabaseException;
import com.ope.patu.server.constant.ServerConstants;

/**This is the implementation class for {@link ServerDAO}
 * @author Debadatta Mishra
 *
 */
public class OracleServerDAOImpl implements ServerDAO {
	/**
	 * Object of type {@link DatabaseUtil}
	 */
	private DatabaseUtil dbUtil = null;

	protected static Logger logger = Logger
	.getLogger(OracleServerDAOImpl.class);

	/**
	 * Default {@link Constructor}
	 */
	public OracleServerDAOImpl() {
		super();
	}

	/**Constructor for database config file.
	 * @param dbConfigFileName of type String indicating the
	 * database config file.
	 */
	public OracleServerDAOImpl(String dbConfigFileName) {
		super();
		dbUtil = DatabaseUtil.getInstance(dbConfigFileName);
	}

	/* (non-Javadoc)
	 * @see com.ope.patu.server.db.ServerDAO#getUserInfo(java.lang.String)
	 */
	public Map<String, String> getUserInfo(String userName)
	throws DatabaseException {
		Map<String, String> userInfoMap = new HashMap<String, String>();
		/*
		 * Write the business logic to populate data
		 */
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Connection conn = dbUtil.getPooledDbConnection();
			preparedStatement = conn
			.prepareStatement(ServerConstants.USER_INFO_QUERY);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, userName);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userInfoMap.put("username", resultSet.getString(2));
				userInfoMap.put("password", resultSet.getString(4));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			DatabaseUtil.closeResultSet(resultSet);
			DatabaseUtil.closePreparedStatement(preparedStatement);
		}
		return userInfoMap;
	}

	public void insertAuditLog(Object... objects) throws DatabaseException {
		Connection conn=null;
		String auditLogQueryString = new StringBuilder(
		"INSERT INTO ope_audit_log_tab")
		.append("(")
		.append(
		"Patu_Id, Agreement_id, Customer_Name, File_name, File_Type, file_Date, Receiver, Sender, file_location,total_Records,")
		.append(
		"Valid_Record_Count, Invalid_Record_count,In_Out, Session_ID, Error_Code, Error_Message , status , SYSTEM_DATE )")
		.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
		.toString();
		PreparedStatement preparedStatement = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			preparedStatement = conn.prepareStatement(auditLogQueryString);

			Map<String, String> auditMap = (Map) objects[0];
			int c = 1;
			Iterator itr = auditMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry<String, String>) itr
				.next();
				preparedStatement.setString(c, me.getValue());
				c++;
			}
			preparedStatement.execute();
			conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			DatabaseUtil.closePreparedStatement(preparedStatement);
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object getESIAgmtDetails(Object... objects) throws DatabaseException {
		String patuID = (String) objects[0];
		java.sql.Timestamp timeStamp = (java.sql.Timestamp) objects[1];
		Map<String, String> esiDataMap = new HashMap<String, String>();
		Connection conn=null;
		OracleCallableStatement oraCallStmt = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			final String storedProcname = "{? = call OPEPK_ESI_VALIDATION.FN_ESI_VALIDATION(?,?,?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(storedProcname);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuID); // IN Parameter - PATU ID
			oraCallStmt.setTimestamp(3, timeStamp); // IN Parameter - TIME STAMP
			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR); // OUT Parameter - SERVICE BUREAU ID	
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR); // OUT Parameter - STATUS
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);
			oraCallStmt.registerOutParameter(7, OracleTypes.VARCHAR);
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);

			logger
			.debug("OracleServerDAOImpl : getESIAgmtDetails - SERVICE BUREAU ID -->"
					+ oraCallStmt.getString(4));
			logger.debug("OracleServerDAOImpl : getESIAgmtDetails - status -->"
					+ oraCallStmt.getString(5));

			if (res == 1) {
				esiDataMap.put("serviceBureauId", oraCallStmt.getString(4));
				esiDataMap.put("status", oraCallStmt.getString(5));
			} else {
				esiDataMap.put("errorcode", oraCallStmt.getString(6));
				esiDataMap.put("errormsg", oraCallStmt.getString(7));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();	
				if(conn!=null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return esiDataMap;
	}

	public Object getServiceIdInfo(Object... objects) throws DatabaseException {
		String serviceId = (String) objects[0];//SERVICEID123456789
		String serviceType = (String) objects[1];
		String serviceBureauId = (String) objects[2];
		Connection conn=null;
		OracleCallableStatement oraCallStmt = null;
		if(!(serviceId==null))
			serviceId = serviceId.trim();

		Map<String, String> serviceInfoMap = new HashMap<String, String>();
		try {
			conn = dbUtil.getPooledDbConnection();
			final String storedProcname = "{? = call OPEPK_ESI_VALIDATION.fn_Transaction_request(?,?,?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(storedProcname);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, serviceId);
			oraCallStmt.setString(3, serviceType);
			oraCallStmt.setString(4, serviceBureauId);
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//agreement id
			//			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//Service code
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);//Error code
			oraCallStmt.registerOutParameter(7, OracleTypes.VARCHAR);//Error message
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);

			if (res == 1) {
				logger.debug("Agreement ID -->" + oraCallStmt.getString(5));
				serviceInfoMap.put(ServerConstants.AGREEMENTTID, oraCallStmt
						.getString(5));
			} else {
				serviceInfoMap.put("errorcode", oraCallStmt.getString(6));
				serviceInfoMap.put("errormsg", oraCallStmt.getString(7));
			}
			logger
			.debug("OracleServerDAOImpl : getServiceIdInfo - errorcode-->"
					+ oraCallStmt.getString(6));
			logger
			.debug("OracleServerDAOImpl : getServiceIdInfo - errorMSG -->"
					+ oraCallStmt.getString(7));

		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return serviceInfoMap;
	}

	public Object getKeyGeneration(Object... objects) throws DatabaseException {
		Map<String, String> keyMap = new HashMap<String, String>();
		String patuId = (String) objects[0];
		String kekGenerationNo = (String) objects[1];
		String aukGenerationNo = (String) objects[2];
		logger.debug("getKeyGeneration--------KEKNO-----" + kekGenerationNo);
		logger.debug("getKeyGeneration--------AUKNO-----" + aukGenerationNo);
		Connection conn = null;
		OracleCallableStatement oraCallStmt = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			final String dbProcName = "{? = call opepk_esi_validation.fn_kek_auk_out(?,?,?,?,?,?,?,?,?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(dbProcName);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuId);
			oraCallStmt.setString(3, kekGenerationNo);//kek generation no
			oraCallStmt.setString(4, aukGenerationNo);//Auk generation no
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//KEK part1 5
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);//KEK part2 6 
			oraCallStmt.registerOutParameter(7, OracleTypes.VARCHAR);//KEK 7
			oraCallStmt.registerOutParameter(8, OracleTypes.VARCHAR);//AUK 8
			oraCallStmt.registerOutParameter(9, OracleTypes.VARCHAR);//New AUK key
			oraCallStmt.registerOutParameter(10, OracleTypes.VARCHAR);//New AUK generation no
			oraCallStmt.registerOutParameter(11, OracleTypes.VARCHAR);// Y/N Flag
			oraCallStmt.registerOutParameter(12, OracleTypes.VARCHAR);//Error code //11
			oraCallStmt.registerOutParameter(13, OracleTypes.VARCHAR);//Error mesg //12

			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if (res == 1) {
				keyMap.put(ServerConstants.KEK_PART1, oraCallStmt.getString(5));
				keyMap.put(ServerConstants.KEK_PART2, oraCallStmt.getString(6));
				keyMap.put(ServerConstants.KEK, oraCallStmt.getString(7));
				keyMap.put(ServerConstants.AUK, oraCallStmt.getString(8));
				keyMap.put(ServerConstants.NEW_AUK, oraCallStmt.getString(9));
				keyMap.put(ServerConstants.NEW_AUK_GEN_NO, oraCallStmt
						.getString(10));
			} else {
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt
						.getString(12));
				keyMap
				.put(ServerConstants.ERROR_MSG, oraCallStmt
						.getString(13));
			}
			logger.debug("Result-----" + res);
			logger.debug("Key MAP-------" + keyMap);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keyMap;
	}

	public Object isMessageProtected(Object... objects)
	throws DatabaseException {
		Boolean bool = Boolean.FALSE;
		Connection conn = null;
		OracleCallableStatement oraCallStmt = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			Map<String, String> keyMap = new HashMap<String, String>();
			String serviceBureauId = (String) objects[0];
			String serviceType = (String) objects[1];
			String serviceId = (String) objects[2];

			if(!(serviceId==null))
				serviceId = serviceId.trim();

			logger
			.debug("IN Parameter => serviceBureauId=" + serviceBureauId
					+ " serviceType=" + serviceType + " serviceId="
					+ serviceId);
			final String dbProcName = "{? = call opepk_esi_validation.fn_Protection_check(?,?,?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(dbProcName);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, serviceBureauId); // Service Bureau Id		
			oraCallStmt.setString(3, serviceType); // Service Type
			oraCallStmt.setString(4, serviceId); // Service Id
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR); // Message Protection Status
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);
			oraCallStmt.registerOutParameter(7, OracleTypes.VARCHAR);

			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if (res == 1) {
				logger.debug("Message Protection--------"
						+ oraCallStmt.getString(5));
				if (oraCallStmt.getString(5).equalsIgnoreCase("Y"))
					bool = Boolean.TRUE;
				else
					bool = Boolean.FALSE;
			} else {
				keyMap
				.put(ServerConstants.ERROR_CODE, oraCallStmt
						.getString(6));
				keyMap.put(ServerConstants.ERROR_MSG, oraCallStmt.getString(7));
				//				bool = Boolean.FALSE;
				logger.debug("Message Protection Error Code------"
						+ oraCallStmt.getString(6));
				logger.debug("Message Protection Error Message------"
						+ oraCallStmt.getString(7));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return bool;
	}

	public Object insertNewAukKey(Object... objects) throws DatabaseException {
		Map<String, String> dataMap = new HashMap<String, String>();
		String patuId = (String) objects[0];//PATU ID
		String newAukKey = (String) objects[1];//new AUK
		Connection conn =null;
		OracleCallableStatement oraCallStmt = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			final String dbProcName = "{? = call opepk_esi_validation.fn_auk_generation(?,?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(dbProcName);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuId);
			oraCallStmt.setString(3, newAukKey);//kek generation no
			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//New AUK Generation no
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//Error code
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);//Error message

			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if (res == 1) {
				dataMap.put(ServerConstants.NEW_AUK_GEN_NO, oraCallStmt
						.getString(4));
			} else {
				dataMap.put(ServerConstants.ERROR_CODE, oraCallStmt
						.getString(5));
				dataMap
				.put(ServerConstants.ERROR_MSG, oraCallStmt
						.getString(6));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

		}
		return dataMap;
	}

	public String getLanguage(Object... objects) throws DatabaseException {
		String patuId = (String) objects[0];
		Map<String, String> languageMap = new HashMap<String, String>();
		String language = null;
		OracleCallableStatement oraCallStmt = null;
		Connection conn=null;
		try {
			conn = dbUtil.getPooledDbConnection();
			final String dbProcName = "{? = call opepk_esi_validation.fn_language_out(?,?,?,?)}";
			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(dbProcName);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuId);
			oraCallStmt.registerOutParameter(3, OracleTypes.VARCHAR);
			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//Error code
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//Error message

			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if (res == 1) {
				logger.debug("Language------" + oraCallStmt.getString(3));
				languageMap.put(ServerConstants.LANGUAGE, oraCallStmt
						.getString(3));
			} else {
				languageMap.put(ServerConstants.ERROR_CODE, oraCallStmt
						.getString(4));
				languageMap.put(ServerConstants.ERROR_MSG, oraCallStmt
						.getString(5));
			}
			logger.debug("Result-----" + res);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return language;
	}

	public Object isTimeStampUnique(Object... objects) throws DatabaseException {
		boolean flag = false;
		String timeStamp = (String) objects[0];
		//		String queryString = "select file_date from ope_audit_log_tab where file_date=?";
		String queryString = "select count(*) from ope_audit_log_tab where file_date=?";
		//		String queryString = "select file_date from ope_audit_log_tab where file_date=?";
		Connection conn = null;
		PreparedStatement preparedStatement=null;
		try {
			conn = dbUtil.getPooledDbConnection();
			preparedStatement = conn.prepareStatement(queryString);
			preparedStatement.setString(1, timeStamp);
			//			preparedStatement.execute();
			ResultSet rs = preparedStatement.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count == 0 || count == 1)
				flag = true;
			logger.debug("Count---------------->>>" + count);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{
			try {
				if(preparedStatement!=null)
					preparedStatement.close();
				if(conn!=null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Boolean(flag);
	}

	public Object getAgreementId(Object... objects) throws DatabaseException {
		Map<String, String> dataMap = new HashMap<String, String>();
		String agmtId = null;
		String patuId = (String) objects[0];
		Connection conn=null;
		OracleCallableStatement oraCallStmt = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			final String dbProcName = "{? = call opepk_esi_validation.fn_Agrement_out(?,?,?,?)}";

			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(dbProcName);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuId);
			oraCallStmt.registerOutParameter(3, OracleTypes.VARCHAR);//Agmt id
			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//Error code
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//Error message

			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if (res == 1) {
				dataMap.put(ServerConstants.AGREEMENTTID, oraCallStmt
						.getString(3));
			} else {
				dataMap.put(ServerConstants.ERROR_CODE, oraCallStmt
						.getString(4));
				dataMap
				.put(ServerConstants.ERROR_MSG, oraCallStmt
						.getString(5));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error(se);
			throw new DatabaseException();
		} catch (DatabaseException de) {
			de.printStackTrace();
			logger.error(de);
			logger.error(de.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}finally{

			try {
				if(oraCallStmt!=null)
					oraCallStmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dataMap;
	}

	public boolean isPatuIdExists(String patuId) throws DatabaseException {
		boolean status = false;
		patuId = patuId.trim();

		//	String queryString = "SELECT COUNT(1) FROM ope_bureau_defn WHERE patu_id= ?";
		String queryString = "SELECT COUNT(1) FROM ope_bureau_defn WHERE patu_id= ?";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = dbUtil.getPooledDbConnection();
			preparedStatement = conn.prepareStatement(queryString);
			preparedStatement.setString(1, patuId);
			//	preparedStatement.execute();
			ResultSet rs = preparedStatement.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count>0)
				status = true;

			logger.debug("isPatuIdExists :: Count--> " + count + "Status--> " + status);

		} catch (SQLException se) {
			logger.error("isPatuIdExists :: SQLException " + se.getMessage());
			throw new DatabaseException();
		} catch (DatabaseException de) {
			logger.error("isPatuIdExists :: DatabaseException " + de.getMessage());
		} catch (Exception e) {
			logger.error("isPatuIdExists :: Exception " + e.getMessage());
		}finally{

			try {
				if(preparedStatement!=null)
					preparedStatement.close();
				if(conn!=null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;
	}
}
