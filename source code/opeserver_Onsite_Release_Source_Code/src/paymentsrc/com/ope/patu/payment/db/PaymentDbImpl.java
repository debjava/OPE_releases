package com.ope.patu.payment.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;

import com.ope.patu.payments.lmp300.BatchValidatorImpl;
import com.ope.patu.server.constant.ServerConstants;
import com.ope.patu.server.db.DatabaseUtil;
import com.ope.patu.util.DateUtil;

//com.ope.patu.payment.db.PaymentDbImpl
public class PaymentDbImpl 
{
	protected static Logger logger = Logger.getLogger(PaymentDbImpl.class);
	private static Connection getConnection() throws Exception
	{
		return DatabaseUtil.getInstance(ServerConstants.DB_PROPERTY)
		.getPooledDbConnection();
	}

	/* (non-Javadoc)
	 * @see com.ope.patu.server.db.ServerDAO#getUserInfo(java.lang.String)
	 */
	public static Map<String, String> getUserInfo(String userName)
	{
		Map< String, String > userInfoMap = new HashMap< String , String >();
		/*
		 * Write the business logic to populate data
		 */
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			Connection conn = getConnection();
			preparedStatement = conn
			.prepareStatement(ServerConstants.USER_INFO_QUERY);
			preparedStatement.setString( 1, userName );
			preparedStatement.setString( 2, userName );
			resultSet = preparedStatement.executeQuery();
			while( resultSet.next() )
			{
				userInfoMap.put("username",resultSet.getString(2));
				userInfoMap.put("password",resultSet.getString(4));
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtil.closeResultSet(resultSet);
			DatabaseUtil.closePreparedStatement(preparedStatement);
		}
		return userInfoMap;
	}

	public static void insertAuditLog(Object... objects) 
	{
		String auditLogQueryString = new StringBuilder("INSERT INTO ope_audit_log_tab")
		.append("(")
		.append("Customer_Name, File_name, File_Type, file_Date, Receiver, Sender, file_location,total_Records,")
		.append("Valid_Record_Count, Invalid_Record_count,In_Out, Session_ID, Error_Code, Error_Message , status )")
		.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
		.toString();
		PreparedStatement preparedStatement = null;
		try
		{
			Connection conn = getConnection();
			preparedStatement = conn
			.prepareStatement( auditLogQueryString );

			Map<String, String> auditMap = (Map)objects[0];
			int c = 1 ;
			Iterator itr = auditMap.entrySet().iterator();
			while( itr.hasNext() )
			{
				Map.Entry<String, String> me = (Map.Entry<String, String>)itr.next();
				preparedStatement.setString( c, me.getValue());
				c++;
			}
			preparedStatement.execute();
			conn.commit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtil.closePreparedStatement(preparedStatement);
		}

	}


	public static Object getESIAgmtDetails(Object... objects) 
	{
		//Senders Id is the Service Bureau Id
		String patuID = ( String )objects[0];
		java.sql.Timestamp timeStamp = ( java.sql.Timestamp )objects[1];
		Map<String , String> esiDataMap = new HashMap<String, String>();
		try
		{
			Connection conn = getConnection();
			final String storedProcname = "{? = call OPEPK_ESI_VALIDATION.FN_ESI_VALIDATION(?,?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(storedProcname);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, patuID);//PATU ID
			oraCallStmt.setTimestamp(3, timeStamp);


			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);

			logger.debug("status---------"+oraCallStmt.getString(4));
			logger.debug("errorcode--------"+oraCallStmt.getString(5));
			logger.debug("errormsg--------"+oraCallStmt.getString(6));
			if (res == 1) 
			{
				esiDataMap.put("status", oraCallStmt.getString(4));
			}
			else
			{
				esiDataMap.put("errorcode", oraCallStmt.getString(5));
				esiDataMap.put("errormsg", oraCallStmt.getString(6));
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return esiDataMap;
	}

	public static Object getServiceIdInfo( Object... objects )
	{
		String serviceId = ( String )objects[0];//SERVICEID123456789
		String serviceCode = ( String )objects[1];
		logger.debug("Service code-------->"+serviceCode);

		/*
		 * For testing
		 */
		serviceId = "SERVICEID123456789";
//		serviceCode = "LM03";
		Map<String , String> serviceInfoMap = new HashMap<String, String>();
		try
		{
			Connection conn = getConnection();
			final String storedProcname = "{? = call OPEPK_ESI_VALIDATION.fn_Transaction_request(?,?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = (OracleCallableStatement) conn
			.prepareCall(storedProcname);
			oraCallStmt.registerOutParameter(1, OracleTypes.NUMERIC);
			oraCallStmt.setString(2, serviceId);
			oraCallStmt.setString(3, serviceCode);
			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//agreement id
//			oraCallStmt.registerOutParameter(4, OracleTypes.VARCHAR);//Service code
			oraCallStmt.registerOutParameter(5, OracleTypes.VARCHAR);//Error code
			oraCallStmt.registerOutParameter(6, OracleTypes.VARCHAR);//Error message

			boolean executeFlag = oraCallStmt.execute();
			logger.debug("Agreement ID-----------"+oraCallStmt.getString(4));

			final int res = oraCallStmt.getInt(1);

			if (res == 1) 
			{
				serviceInfoMap.put("agreementid", oraCallStmt.getString(4));

			}
			else
			{
				serviceInfoMap.put("errorcode", oraCallStmt.getString(5));
				serviceInfoMap.put("errormsg", oraCallStmt.getString(6));
			}
			logger.debug("Total Map---------"+serviceInfoMap);

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return serviceInfoMap;
	}

	public static boolean checkBusinessIdentityCode(String businessIdentityCode)
	{
		boolean status = false;
		try {
			Connection con = getConnection();
			PreparedStatement selectStmt = con.prepareStatement("Select * from OPE_BUREAU_MAP_DEFN where SERVICE_ID = ?");
			selectStmt.setString(1, businessIdentityCode);
			ResultSet rs =  selectStmt.executeQuery();

			if(rs.next()) {
				logger.debug("Business Id is Exist");
				status = true;
			}
			else {
				logger.debug("Business Id is not Exist");
				status = false;
			}

		} catch (Exception e) {
			logger.debug("Caught in Exception"+e.getMessage());
		}
		return status;
	}

	public static boolean checkCurrencyCode(String currencyCode)
	{
		logger.debug("Currency Code------->"+currencyCode);
		boolean statusFlag = false;
		/*
		 * Added by Debadatta Mishra at onsite to fix the issue related to
		 * currency code validation. Actual query is given below.
		 * "select distinct status from dmtm_currency_defn where CURRENCY_CODE=?"
		 */
		String queryString = "SELECT DISTINCT STATUS FROM DMTM_CURRENCY_DEFN WHERE CURRENCY_CODE = ?";
		try 
		{
			Connection con = getConnection();
			// SELECT 1 FROM dmtm_currency_defn WHERE currency_code='EUR'
			//"select distinct status from dmtm_currency_defn where CURRENCY_CODE=?";
			//			PreparedStatement selectStmt = con.prepareStatement("SELECT 1 FROM dmtm_currency_defn WHERE currency_code = ?");
			PreparedStatement selectStmt = con.prepareStatement(queryString);
			selectStmt.setString(1, currencyCode);
			ResultSet rs =  selectStmt.executeQuery();
			/*
			 * Added by Debadatta Mishra at onsite to fix the issue related to currency code validation.
			 */
			if( rs != null )
			{
				while( rs.next() )
				{
					String status = rs.getString(1);
					statusFlag = status.equalsIgnoreCase("A") ? true : false;
				}
			}
			else
				statusFlag = false;
			/*
			 * Commented out by Debadatta Mishra at onsite to fix the issue
			 * related to currency validation.
			 */
//			if(rs.next()) {
//				logger.debug("Currency Code is Exist");
//				statusFlag = true;
//			}
//			else {
//				logger.debug("Currency Code is not Exist");
//				statusFlag = false;
//			}
		}
		catch( NullPointerException npe )
		{
			logger.error("NullPointer Exception thrown");
			statusFlag = false;
		}
		catch( SQLException se )
		{
			statusFlag = false;
			logger.error("SQLException thrown");
		}
		catch (Exception e) 
		{
			logger.debug("Caught in Exception :: checkCurrencyCode() :: "+e.getMessage());
			statusFlag = false;
		}
		return statusFlag;
	}
	/**
	 * Method is use to check the account number validation 
	 * @param accountNumber
	 * @param serviceId
	 * @param serviceType
	 * @param serviceBureau
	 * @return
	 */
	public static boolean checkOPEAccountNumber(String accountNumber, String serviceId, String serviceType, String serviceBureau)
	{
		serviceId = serviceId.trim();
		logger.debug("serviceId     -->"+serviceId+"----");
		logger.debug("serviceType   -->"+serviceType+"----");
		logger.debug("serviceBureau -->"+serviceBureau+"----");
		logger.debug("accountNumber -->"+accountNumber+"----");
		OracleConnection oraConRef = null;
		OracleCallableStatement ocs=null;
		Connection conn = null;
		int res;
		boolean flag = false;
		try {
			conn = getConnection();
			oraConRef = (OracleConnection) conn;	
			ocs = (OracleCallableStatement) oraConRef.prepareCall("{? = call opepk_esi_validation.fn_Account_validation(?,?,?,?,?,?)}");
			ocs.registerOutParameter(1,OracleTypes.NUMERIC);
			ocs.setString(2, serviceId);
			ocs.setString(3, serviceType);
			ocs.setString(4, serviceBureau);
			ocs.setString(5, accountNumber);
			ocs.registerOutParameter(6, OracleTypes.VARCHAR);
			ocs.registerOutParameter(7, OracleTypes.VARCHAR);
			ocs.execute();					
			res = ocs.getInt(1);
			logger.debug("RETURN     :" + res );			
			logger.debug("ERROR CODE :" + ocs.getString(6));
			logger.debug("ERROR DESC :" + ocs.getString(7));
			if(res==0) {
				flag = false;
				oraConRef.rollback();
				logger.debug("Could not get Rights : ");
			}else{
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("SQL Exception account number ::");
		} catch(Exception e){
			logger.error("Exception in in account validations ::");
			e.printStackTrace();
		}
		finally
		{
			ensureClosed(ocs, conn, oraConRef);
		}
		return flag;
	}
	public static boolean getDueDateValidations(String dueDate, String receivedDateFormat, String expectedDateFormat)
	{

		logger.debug("Inside getDueDateValidations......."+dueDate);
		logger.debug("Due Date -->"+dueDate);
		OracleConnection oraConRef = null;
		OracleCallableStatement ocs=null;
		Connection con = null;
		java.sql.Date sqlDate = null;

		int res;
		boolean flag = false;
		try
		{
			con = getConnection();
			try{
				if(expectedDateFormat != null){
					sqlDate = DateUtil.parseFormatDate(dueDate,receivedDateFormat,expectedDateFormat);
				}else{
					sqlDate = DateUtil.parseDate(dueDate);
				}
			}catch(NullPointerException npe){
				npe.printStackTrace();
			}
			
			oraConRef = (OracleConnection) con;		
			logger.debug("connected");
			logger.debug("sqlDate-->"+sqlDate);
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_bps_validations.fn_bps_duedate_validation(?,?,?)}");  
			ocs.registerOutParameter(1,OracleTypes.NUMERIC);
			ocs.setDate(2, sqlDate);
			ocs.registerOutParameter(3, OracleTypes.VARCHAR);
			ocs.registerOutParameter(4, OracleTypes.VARCHAR);
			ocs.execute();					
			res = ocs.getInt(1);
			logger.debug("RETURN     :" + res );			
			logger.debug("ERROR CODE :" + ocs.getString(3));
			logger.debug("ERROR DESC :" + ocs.getString(4));
			if(res==0) {
				flag = false;
				oraConRef.rollback();
				logger.debug("Could not get Rights : ");
			}else{
				flag = true;
			}
		}catch(SQLException e){
			logger.error("SQL Exception ...."+e.getMessage());
		}
		catch(NullPointerException npe){
			npe.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}finally{
		    ensureClosed(ocs, con, oraConRef);
		}
		
		return flag;
	}

	public static String getServiceCode(String serviceId,String serviceType, String bureauId){
		OracleConnection oraConRef = null;
		Connection con = null;			
		OracleCallableStatement ocs=null;		
		String accountNumber="";
		int res;

		try{
			con = getConnection();	
			oraConRef = (OracleConnection) con;	
			ocs = (OracleCallableStatement)
			oraConRef.prepareCall("{? = call opepk_esi_validation. fn_Account_out(?,?,?,?,?,?)}");  
			ocs.registerOutParameter(1,OracleTypes.NUMERIC);
			ocs.setString(2, serviceId);
			ocs.setString(3, serviceType);
			ocs.setString(4, bureauId);
			ocs.registerOutParameter(5, OracleTypes.VARCHAR);
			ocs.registerOutParameter(6, OracleTypes.VARCHAR);
			ocs.registerOutParameter(7, OracleTypes.VARCHAR);
			ocs.execute();					
			res = ocs.getInt(1);
			accountNumber = ocs.getString(5);
			logger.debug("Account Number --->"+accountNumber);
			logger.debug("res            --->"+res);
			logger.debug("RETURN         :" + res );			
			logger.debug("ACCOUNT NUMBER :" + ocs.getString(5));
			logger.debug("ERROR CODE     :" + ocs.getString(6));
			logger.debug("ERROR DESC     :" + ocs.getString(7));
			if(res==0) {
				accountNumber="0";
				oraConRef.rollback();
				logger.debug("Could not get rights : ");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
		    ensureClosed(ocs, con, oraConRef);
		}
		return accountNumber;
	}
	
	
	/**
	 * Closes Connection, swallows any exceptions that could occur.
	 * @param conn
	 * Connectoin reference.
	 */
	public static void ensureClosed(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				// Swallow.
			}
		}
	}

	/**
	 * Closes ResultSet, swallows any exceptions that could occur.
	 * 
	 * @param rslt
	 * ResultSet to close.
	 */
	public static void ensureClosed(ResultSet rslt) {
		if (rslt != null) {
			try {
				rslt.close();
				rslt = null;
			} catch (SQLException e) {
				// Swallow.
			}
		}
	}

	/**
	 * Closes Statement, swallows any exceptions that could occur.
	 * @param stmt Statement to close.
	 */
	public static void ensureClosed(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				// SWALLOW.
			}
		}
	}

	/**
	 * Method closes the Native connectionm, statement and nulls the reference
	 * connection, It also swallows any exceptions that could occur.
	 * @param stmt Statement to close.
	 * @param conn Statement to close.
	 * @param oraConRef Statement to close.
	 */
	public static void ensureClosed(Statement stmt, Connection conn,
			OracleConnection oraConRef) {
		try {
			ensureClosed(stmt);
			ensureClosed(conn);
			if (oraConRef != null)
				oraConRef = null;
		} 
		catch (Exception e) {
			// SWALLOW.
		}
	}

}
