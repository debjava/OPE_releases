package com.ope.scheduler.db;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.ope.patu.security.EncryptionUtility;
import com.ope.scheduler.utilities.DescriptorUtility;

import oracle.jdbc.OracleTypes;

public class OracleDmBatch {
	Array arrInterface;

	/** Method is used to run the DM Batch */
	protected static Logger logger = Logger.getLogger(OracleDmBatch.class);

	public boolean runDmBatch(Properties properties, String serviceType) {
		String userId = null;
		String password = null;
		String branchId = null;
		String batchId = null;
		String xRef = null;
		String validationCode = null;
		String data1 = null;
		String data2 = null;
		String data3 = null;
		String data4 = null;
		String data5 = null;

		Connection con = null;
		CallableStatement ocs = null;
		Struct calStruct = null;

		int res;
		boolean ret = false;

		Date dateOfExecution = null;

		userId = properties.getProperty("dm.userId");
		password = properties.getProperty("dm.password");
		branchId = properties.getProperty("dm.branchId");
		batchId = properties.getProperty("dm." + serviceType + ".batchId");

		xRef = userId + "-" + batchId + "-"
				+ getCurrentDate("yy-MM-dd-HH:mm:ss");

		logger.debug("IN Parameter UserId->" + userId + ",BranchId->"
				+ branchId + ",BatchId->" + batchId + ",XREF->" + xRef);

		try {
			Class.forName(properties.getProperty("driverclass"));
			con = DriverManager.getConnection(properties
					.getProperty("dbcp_dburl"), properties
					.getProperty("dbcp_dbuser"), properties
					.getProperty("dbcp_dbpassword"));

		} catch (SQLException sq) {
			logger.debug("Could not obtain OracleConnection-->"
					+ sq.getMessage());
		} catch (ClassNotFoundException e) {
			logger.debug("ClassNotFoundException -->" + e.getMessage());
		} catch (Exception e) {
			logger.debug("Exception -->" + e.getMessage());
		}

		/**
		 * Encryption method to encrypt the Password.
		 */

		logger.debug(" Calling EncryptionUtility Classes for Encryption");
		EncryptionUtility eu = new EncryptionUtility();
		password = eu.encrypt(password);

		try {

			/*	Make the object from the bean class */

			Object[] p1recobj = { userId, password, branchId, batchId, xRef,
					dateOfExecution, validationCode, data1, data2, data3,
					data4, data5 };

			/*	Create Descriptors for OracleTypes */

			calStruct = DescriptorUtility.getStruct("EXTERNAL_MESSAGE_TYPE",
					con, p1recobj);
			Object[] p1arrobj = new Object[] { calStruct };
			arrInterface = DescriptorUtility.getArray(
					"EXTERNAL_MESSAGE_TYPE_TB", con, p1arrobj);

		} catch (SQLException t) {
			t.getCause();
			t.getErrorCode();
			t.printStackTrace();
		}

		try {

			/*	Prepare the OracleCallableStatement */
			ocs = (CallableStatement) con

					.prepareCall("{? = call dmpk_interface_insert.fn_process_ext_req(?,?,?)}");

			/*	Bind inputs and register outputs */
			ocs.registerOutParameter(1, OracleTypes.NUMERIC);
			ocs.setArray(2, arrInterface);
			ocs.registerOutParameter(3, OracleTypes.VARCHAR);
			ocs.registerOutParameter(4, OracleTypes.VARCHAR);

			/*	Execute the procedure */
			ocs.execute();

			/*	Log the result */
			res = ocs.getInt(1);

			/*	Return the result to the called location */
			if (res == 0) {
				logger.debug("Insertion Failed:Return" + res);
				con.rollback();
				con.close();
			} else if (res == 1) {
				logger.debug("Insertion Successful:Return" + res);
				con.commit();
				con.close();
				ret = true;
			} else {
				con.rollback();
				con.close();
				ret = false;
			}
			return ret;
		} catch (SQLException t) {
			t.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return ret;
	}

	/**
	 * Parses a string date and returns java.sql.Date in the 'yyyy-MM-dd' format
	 * @param format : format of the stringdate
	 * @param date
	 * @return Date
	 */
	public Date parseDate(String date) {
		java.util.Date utilDate = null;
		Date sqlDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			utilDate = sdf.parse(date);
			sqlDate = new Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}

	/**
	 * Get a current date as a String in the specified format - 'yy-MM-dd-HH:mm:ss'
	 * @return String
	 */
	private String getCurrentDate(String format) {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateFormat = sdf.format(today);
		logger.debug(dateFormat);
		return dateFormat;
	}
}