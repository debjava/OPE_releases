package com.ope.patu.server.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.ope.patu.key.KeyGenerator;
import com.ope.patu.server.constant.ServerConstants;
import com.sun.corba.se.spi.activation.Server;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class TestDatabaseConnection 
{
	private Properties dbProp = new Properties();
	private static Connection conn = null;
	
	public Object getKeyGeneration(Object... objects) 
	{
		Map<String , String > keyMap = new HashMap<String, String>();
		String patuId = ( String )objects[0];
		String kekGenerationNo = ( String )objects[1];
		String aukGenerationNo = ( String )objects[2];
		try
		{
//			Connection conn = dbUtil.getPooledDbConnection();
			final String dbProcName =  "{? = call opepk_esi_validation.fn_kek_auk_out(?,?,?,?,?,?,?,?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = 
				( OracleCallableStatement )conn.prepareCall( dbProcName );
			oraCallStmt.registerOutParameter( 1, OracleTypes.NUMERIC );
			oraCallStmt.setString( 2 , patuId );
			oraCallStmt.setString( 3 , kekGenerationNo );//kek generation no
			oraCallStmt.setString( 4 , aukGenerationNo );//Auk generation no
			oraCallStmt.registerOutParameter( 5 , OracleTypes.VARCHAR );//KEK part1 5
			oraCallStmt.registerOutParameter( 6 , OracleTypes.VARCHAR );//KEK part2 6 
			oraCallStmt.registerOutParameter( 7 , OracleTypes.VARCHAR );//KEK 7
			oraCallStmt.registerOutParameter( 8 , OracleTypes.VARCHAR );//AUK 8
			oraCallStmt.registerOutParameter( 9 , OracleTypes.VARCHAR );//New AUK key
			oraCallStmt.registerOutParameter( 10 , OracleTypes.VARCHAR );//New AUK generation no
			oraCallStmt.registerOutParameter( 11 , OracleTypes.VARCHAR );//Error code //11
			oraCallStmt.registerOutParameter( 12 , OracleTypes.VARCHAR );//Error mesg //12
			
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if( res == 1 )
			{
				keyMap.put(ServerConstants.KEK_PART1, oraCallStmt.getString(5) );
				keyMap.put(ServerConstants.KEK_PART2, oraCallStmt.getString(6) );
				keyMap.put(ServerConstants.KEK, oraCallStmt.getString(7) );
				keyMap.put(ServerConstants.AUK, oraCallStmt.getString(8) );
				keyMap.put(ServerConstants.NEW_AUK, oraCallStmt.getString(9) );
				keyMap.put(ServerConstants.NEW_AUK_GEN_NO, oraCallStmt.getString(10) );
			}
			else
			{
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(11) );
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(12) );
			}
			System.out.println("Result-----"+res);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return keyMap;
	}
	public TestDatabaseConnection()
	{
		super();
		try
		{
			dbProp.load(new FileInputStream(System.getProperty("user.dir")
					+ File.separator + "conf" + File.separator
					+ "db.properties"));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
//	public Connection getConnection() throws Exception
//	{
//		String dbURL = new StringBuffer("jdbc:oracle:thin:@").
//		append(dbProp.getProperty("DATABASE_SERVER_IP"))
//		.append(":").append(dbProp.getProperty("DATABASE_SERVER_PORT")).
//		append(":").append(dbProp.getProperty("DATABASE_SID"))
//		.toString();
//		System.out.println(dbProp.getProperty("USERNAME")+"---"+dbProp.getProperty("PASSWORD")+"---"+dbURL);
//		Connection conn = DatabaseUtil
//				.getPooledDbConnection(dbProp.getProperty("USERNAME"), dbProp
//						.getProperty("PASSWORD"), dbURL);
//		return conn;
//	}
	
	public Connection getConnection() throws Exception
	{
		String dbURL = new StringBuffer("jdbc:oracle:thin:@").
		append(dbProp.getProperty("DATABASE_SERVER_IP"))
		.append(":").append(dbProp.getProperty("DATABASE_SERVER_PORT")).
		append(":").append(dbProp.getProperty("DATABASE_SID"))
		.toString();
		System.out.println(dbProp.getProperty("USERNAME")+"---"+dbProp.getProperty("PASSWORD")+"---"+dbURL);
		Connection conn = DatabaseUtil
				.getPooledDbConnection(dbProp.getProperty("USERNAME"), dbProp
						.getProperty("PASSWORD"), dbURL);
		return conn;
	}
	
	private void executeProc( String proceName )
	{
		try
		{
			Connection conn = getConnection();
			final String storedProcname = "{? = call OPEPK_ESI_VALIDATION.FN_ESI_VALIDATION(?,?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = 
				( OracleCallableStatement )conn.prepareCall( storedProcname );
			oraCallStmt.registerOutParameter( 1, OracleTypes.NUMERIC );
			oraCallStmt.setString( 2 , "MISCROSOFT2244667" );
//			oraCallStmt.setString( 2 , "dsfdsf" );
			oraCallStmt.registerOutParameter( 3 , OracleTypes.VARCHAR );
			oraCallStmt.registerOutParameter( 4 , OracleTypes.VARCHAR );
			oraCallStmt.registerOutParameter( 5 , OracleTypes.VARCHAR );
			oraCallStmt.registerOutParameter( 6 , OracleTypes.VARCHAR );
			
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			System.out.println("Result-----"+res);
			System.out.println("Date--------"+oraCallStmt.getString(3));
			System.out.println("Patu ID--------"+oraCallStmt.getString(4));
			
			System.out.println("Error Code--------"+oraCallStmt.getString(5));
			System.out.println("Error Msg--------"+oraCallStmt.getString(6));
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void insertnewAuk( Object...objects )
	{
		Map<String , String > keyMap = new HashMap<String, String>();
		String patuId = ( String )objects[0];
		String newAukKey = ( String )objects[1];
		try
		{
			final String dbProcName =  "{? = call opepk_esi_validation.fn_auk_generation(?,?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = 
				( OracleCallableStatement )conn.prepareCall( dbProcName );
			oraCallStmt.registerOutParameter( 1, OracleTypes.NUMERIC );
			oraCallStmt.setString( 2 , patuId );
			oraCallStmt.setString( 3 , newAukKey );//kek generation no
			oraCallStmt.registerOutParameter( 4 , OracleTypes.VARCHAR );//New AUK Generation no
			oraCallStmt.registerOutParameter( 5 , OracleTypes.VARCHAR );//Error code
			oraCallStmt.registerOutParameter( 6 , OracleTypes.VARCHAR );//Error message
			
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if( res == 1 )
			{
				System.out.println("New AUK Generation No------"+oraCallStmt.getString(4));
				keyMap.put(ServerConstants.NEW_AUK_GEN_NO, oraCallStmt.getString(4) );
			}
			else
			{
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(5) );
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(6) );
			}
			System.out.println("Result-----"+res);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	private static String getLanguage( String patuId , Connection conn )
	{
		Map<String , String > keyMap = new HashMap<String, String>();
		String language = null;
		try
		{
			final String dbProcName =  "{? = call opepk_esi_validation.fn_language_out(?,?,?,?)}";
			OracleCallableStatement oraCallStmt = null;
			oraCallStmt = 
				( OracleCallableStatement )conn.prepareCall( dbProcName );
			oraCallStmt.registerOutParameter( 1, OracleTypes.NUMERIC );
			oraCallStmt.setString( 2 , patuId );
			oraCallStmt.registerOutParameter( 3 , OracleTypes.VARCHAR );
			oraCallStmt.registerOutParameter( 4 , OracleTypes.VARCHAR );//Error code
			oraCallStmt.registerOutParameter( 5 , OracleTypes.VARCHAR );//Error message
			
			boolean executeFlag = oraCallStmt.execute();
			final int res = oraCallStmt.getInt(1);
			if( res == 1 )
			{
				System.out.println("Language------"+oraCallStmt.getString(3));
				
			}
			else
			{
				System.out.println("Language------"+oraCallStmt.getString(3));
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(4) );
				keyMap.put(ServerConstants.ERROR_CODE, oraCallStmt.getString(5) );
			}
			System.out.println("Result-----"+res);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		return language;
	}
	
	public static void main(String[] args) 
	{
		TestDatabaseConnection testDb = new TestDatabaseConnection();
		try
		{
			Connection conn1 = testDb.getConnection();
			conn = conn1;
			System.out.println("Connection--------"+conn);
//			String patuId = "99910000555552103";//99910000011111111
			String patuId = "99910000011111111";//99910000011111111
//			String kekGenNo = "0";
//			String aukGenN0 = "0";
//			Map<String, String> dataMap = (Map)testDb.getKeyGeneration(patuId,kekGenNo,aukGenN0);
//			System.out.println("KEK part1 ------"+dataMap.get(ServerConstants.KEK_PART1));
//			System.out.println("KEK part2 ------"+dataMap.get(ServerConstants.KEK_PART2));
//			System.out.println("KEK  ------"+dataMap.get(ServerConstants.KEK));
//			System.out.println("AUK  ------"+dataMap.get(ServerConstants.AUK));
//			System.out.println("NEW_AUK  ------"+dataMap.get(ServerConstants.NEW_AUK));
//			System.out.println("NEW_AUK_GEN_NO  ------"+dataMap.get(ServerConstants.NEW_AUK_GEN_NO));
			
			
//			String newAukString = KeyGenerator.getNewAuk();
//			testDb.insertnewAuk(patuId,newAukString);
			
			
//			getLanguage(patuId, conn);
			
			String timeStamp = "080930202723000";//"ppppppppppp";//"080930202723000";
//			String queryString = "select distinct file_date from ope_audit_log_tab where file_date=?";
//			String queryString = "select count(*)-1 from ope_audit_log_tab where file_date=?";
//			try
//			{
//				PreparedStatement preparedStatement = conn.prepareStatement( queryString );
//				preparedStatement.setString(1, timeStamp);
////				preparedStatement.execute();
//				ResultSet rs = preparedStatement.executeQuery();
//				System.out.println("--------"+rs);
//				while( rs.next() )
//				{
//					System.out.println("----------->>>"+rs.getString(1));
//				}
//			}
//			catch( Exception e )
//			{
//				e.printStackTrace();
//			}
			
			
			
			
			ServerDAO serverDAO = AbstractDAOFactory.getDefaultDAOFactory()
			.getServerDAO();
			Map<String, String> map = (Map<String,String>)serverDAO.getAgreementId("9991000000DTE2038");
			System.out.println(map);

			
			
			
			
			
			
			
			
			
//			System.out.println("Conn------"+conn);
//			final String dbProcName =  "{? = call opepk_esi_validation.fn_Protection_check(?,?,?,?,?)}";
//			OracleCallableStatement oraCallStmt = null;
//			oraCallStmt = 
//				( OracleCallableStatement )conn.prepareCall( dbProcName );
//			oraCallStmt.registerOutParameter( 1, OracleTypes.NUMERIC );
//			oraCallStmt.setString( 2 , "99910000WMDAT2087" );
//			oraCallStmt.setString( 3 , "LM03" );
//			oraCallStmt.registerOutParameter( 4 , OracleTypes.VARCHAR );//KEK part1
//			oraCallStmt.registerOutParameter( 5 , OracleTypes.VARCHAR );//KEK part2
//			oraCallStmt.registerOutParameter( 6 , OracleTypes.VARCHAR );//KEK
////			oraCallStmt.registerOutParameter( 7 , OracleTypes.VARCHAR );//AUK
////			oraCallStmt.registerOutParameter( 8 , OracleTypes.VARCHAR );//Error code
////			oraCallStmt.registerOutParameter( 9 , OracleTypes.VARCHAR );//Error mesg
//			
//			boolean executeFlag = oraCallStmt.execute();
//			final int res = oraCallStmt.getInt(1);
//			System.out.println("Protection status------"+oraCallStmt.getString(4));
//			System.out.println("Result-----"+res);
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
