package com.ope.scheduler.utilities;

import com.ope.scheduler.exception.OPERuntimeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * Utility class which gets the OracleConnection object from underlying
 * connection objects and has utility methods to get the ARRAY and STRUCT
 * descriptor
*/

public class DescriptorUtility {
	
	/**
	 * Cache for the native connection extraction
	 */
	private static Method extractionMethod;
	private static boolean passConnection = false;
	private static boolean loaded = false;
	/**
	 * Make sure a native connection object is passed
	 * @param structName
	 * @param conn
	 * @param structContentArray
	 * @return
	 * @throws SQLException
	 */
	public static Struct getStruct(String structName, Connection conn, 
			Object[] structContentArray) throws SQLException {
		StructDescriptor descriptor = StructDescriptor.createDescriptor(structName, conn);
		return new STRUCT(descriptor, conn, structContentArray);
	}
	/**
	 * Make sure a native connection object is passed
	 * @param arrayTypeName
	 * @param conn
	 * @param arrayContents
	 * @return
	 * @throws SQLException
	 */
	public static Array getArray(String arrayTypeName, Connection conn, 
			Object[] arrayContents) throws SQLException {
		ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(arrayTypeName, conn);
		return new ARRAY(descriptor, conn, arrayContents);
	}
	/**
	 * Make sure a native connection object is passed
	 * @param arrayTypeName
	 * @param structName
	 * @param conn
	 * @param arrayContents
	 * @return
	 * @throws SQLException
	 */
	public static Array getArrayOfStructs(String arrayTypeName, String structName, 
			Connection conn, List arrayContents) throws SQLException {
		Array array = null;
		StructDescriptor descriptor = StructDescriptor.createDescriptor(structName, conn);
		Object[] structArr = new Object[arrayContents.size()];
		for (int i = 0; i < arrayContents.size(); i++) {
			structArr[i] = new STRUCT(descriptor, conn, (Object[]) arrayContents.get(i));
		} // endfor i
		array = getArray(arrayTypeName, conn, structArr);
		return array;
	}
	/**
	 * Get a struct descriptor for the Oracle type of the given name
	 * 
	 * @param name
	 *            STRUCT type name
	 * @param conn
	 *            Connection to use to fetch the type descriptor
	 * @return A StructDescriptor that can be used to create STRUCTS
	 * @throws SQLException
	 *             In case of DB error
	 */
	public static StructDescriptor getStructDescriptor(String name, Connection conn)
			throws SQLException {
		OracleConnection oConn = getOracleConnection(conn);
		StructDescriptor descr = StructDescriptor.createDescriptor(name, oConn);
		return descr;
	}

	/**
	 * Get an array descriptor for the Oracle type of the given name
	 * 
	 * @param name
	 *            ARRAY type name
	 * @param conn
	 *            Connection to use to fetch the type descriptor
	 * @return An ArrayDescriptor that can be used to create ARRAYs
	 * @throws SQLException
	 *             In case of DB error
	 */
	public static ArrayDescriptor getArrayDescriptor(String name, Connection conn)
			throws SQLException {
		OracleConnection oConn = getOracleConnection(conn);
		ArrayDescriptor descr = ArrayDescriptor.createDescriptor(name, oConn);
		return descr;
	}
  public static Blob createBlob(Connection conn, byte[] bytes) throws SQLException {
    OracleConnection oConn = getOracleConnection(conn);
    BLOB blob = BLOB.createTemporary(oConn, false, BLOB.DURATION_SESSION);
    blob.setBytes(1, bytes);
    return blob;
  }
  public static Clob createClob(Connection conn, String string) throws SQLException {
    OracleConnection oConn = getOracleConnection(conn);
    CLOB clob = CLOB.createTemporary(oConn, false, CLOB.DURATION_SESSION);
    clob.setString(1, string);
    return clob;
  }

	/**
	 * Get the underlying Oracle connection object (required to work with Oracle
	 * types)
	 * 
	 * @param conn
	 *            Base connection object
	 * @return The corresponding Oracle Connection
	 * @throws SQLException
	 *             If unable to read the underlying connection
	 */
	public static OracleConnection getOracleConnection(Connection conn)
			throws SQLException {
		OracleConnection oConn = null;
		if ( loaded ) {
			// try the cached extraction method
			try {
				oConn = (OracleConnection) extractionMethod.invoke(conn, 
						passConnection ? new Object[] { conn } : new Object[] {});
			} catch (IllegalArgumentException e) {
				System.out.println("[DescriptorUtility] " + e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println("[DescriptorUtility] " + e);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				System.out.println("[DescriptorUtility] " + e);
				e.printStackTrace();
			} finally {
				loaded = oConn != null; // regularly update status :D
			}
		} else  {
			// if cache-miss, try the hard way
			// this can throw the infamous ope-runtime-exception
			oConn = getNativeConnection(conn);
		}
		return oConn;
	}
	private static OracleConnection getNativeConnection(Connection conn)
			throws SQLException {
		System.out.println("Trying to get OracleConnection the hard way");
		// try the easiest first
		if (conn instanceof OracleConnection)
			return (OracleConnection) conn;
		Class tryOut = null;
		/* WEBSPHERE */
		try {
			// websphere 6
			tryOut = Class.forName("com.ibm.ws.rsadapter.jdbc.WSJdbcConnection");
			if ( tryOut.isInstance(conn) ) {
				System.out.println("Got a websphere here !!!");
				return getWebSphereNative(conn);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Caught \"" + e + "\".");
			System.out.println("Not Websphere !!");
		}
		/* JBOSS */
		try {
			// Jboss connections are of type WrappedConnection
			tryOut = Class.forName("org.jboss.resource.adapter.jdbc.WrappedConnection");
			if ( tryOut.isInstance(conn) ) {
				System.out.println("Got a jboss here !!!");
				return getJbossNative(conn, tryOut);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Caught \"" + e + "\".");
			System.out.println("Not Jboss!!");
		}
		/* DBCP */
		try {
			// Dbcp Connections are of type DelegationConnection
			tryOut = Class.forName("org.apache.commons.dbcp.DelegatingConnection");
			if ( tryOut.isInstance(conn) ) {
				System.out.println("Got a dbcp here !!!");
				return getDbcpNative(conn, tryOut);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Caught \"" + e + "\".");
			System.out.println("Not Dbcp!!");
		}
		// when hope truly deserts you give up :(
		throw new OPERuntimeException("", "Unknown connection type");
	}
	private static OracleConnection getDbcpNative(Connection conn, Class wrapperClass) {
		OracleConnection result = null;
		try {
			Method m = wrapperClass.getMethod("getInnermostDelegate", new Class[]{});
			result = (OracleConnection) m.invoke(conn, new Object[]{});
			init(m, false, true);
		} catch (SecurityException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		}
		return result;
	}
	private static OracleConnection getJbossNative(Connection conn, Class wrapperClass) {
		OracleConnection result = null;
		try {
			Method m = wrapperClass.getMethod("getUnderlyingConnection", new Class[]{});
			result = (OracleConnection) m.invoke(conn, new Object[]{});
			init(m, false, true);
		} catch (SecurityException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		}
		return result;
	}
	private static OracleConnection getWebSphereNative(Connection conn) {
		OracleConnection result = null;
		try {
			Class util = Class.forName("com.ibm.ws.rsadapter.jdbc.WSJdbcUtil");
			Method m = util.getMethod("getNativeConnection", new Class[] { conn.getClass() });
			if ( m != null ) {
				result = (OracleConnection) m.invoke(null, new Object[] { conn });
			}
			init(m, true, true);
		} catch (ClassNotFoundException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("[DescriptorUtility] " + e);
			e.printStackTrace();
		}
		return result;
	}
	private static void init(Method eMethod, boolean passCon, 
			boolean cLoaded) {
		extractionMethod = eMethod;
		passConnection = passCon;
		loaded = cLoaded;
	}

	/**
	 * This method will return an OracleCallableStatement, which has support for
	 * ARRAY and STRUCT types
	 * 
	 * @param conn
	 *            Connection to base this statement off of
	 * @param sql
	 *            SQL Statement to prepare
	 * @return An OracleCallableStatement
	 * @throws SQLException
	 *             In case we can't prepare the call
	 */
	static OracleCallableStatement prepareOracleCall(Connection conn, String sql)
			throws SQLException {
		OracleConnection oConn = getOracleConnection(conn);
		return (OracleCallableStatement) oConn.prepareCall(sql);
	}
}
