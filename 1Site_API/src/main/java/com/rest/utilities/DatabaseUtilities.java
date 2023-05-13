package com.rest.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rest.main.Base_Class_API;

public class DatabaseUtilities {

	private static Connection conn = null;
	String env = Base_Class_API.environment.toLowerCase();
	
	public DatabaseUtilities() {
		if(env.equals("sit")||env.equals("b2b")||env.equals("bluedot")) {
		SIT_Initializer();
		}else if(env.equals("preprod")) {
			PreProd_Initializer();
		}
	}

	public DatabaseUtilities(String dbName) {
		if(dbName.equalsIgnoreCase("DM")) {
			if(env.equals("sit")||env.equals("b2b")||env.equals("bluedot")) {
				DM_Initializer_SIT();
				}else if(env.equals("preprod")) {
					DM_Initializer_PreProd();
				}
			
		} else if(dbName.equalsIgnoreCase("SIT")) {
			if(env.equals("sit")||env.equals("b2b")||env.equals("bluedot")) {
				SIT_Initializer();
				}else if(env.equals("preprod")) {
					PreProd_Initializer();
				}
		}
		else if(dbName.equalsIgnoreCase("COL")) {
			if(env.equals("sit")||env.equals("b2b")||env.equals("bluedot")) {
				COL_Initializer_SIT();
				}else if(env.equals("preprod")) {
					COL_Initializer_PreProd();
				}
			
		}
		else if(dbName.equalsIgnoreCase("COLMedia")) {
			COLMedia_Initializer_SIT();
		}

	}
	
	private void COLMedia_Initializer_SIT() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_COLMedia") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_COLMedia");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn != null) {
				System.out.println("Database Connected Successfully...");
			} else {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	private void COL_Initializer_SIT() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_COL") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_COL");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn != null) {
				System.out.println("Database Connected Successfully...");
			} else {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	private void COL_Initializer_PreProd() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_PreProd") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_COLServices_PreProd");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn != null) {
				System.out.println("Database Connected Successfully...");
			} else {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	private void DM_Initializer_SIT() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_DM") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_DM");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn != null) {
				System.out.println("Database Connected Successfully...");
			} else {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	private void DM_Initializer_PreProd() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_PreProd") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_DM_PreProd");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn != null) {
				System.out.println("Database Connected Successfully...");
			} else {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}
	
	private void SIT_Initializer() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_SIT") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_SIT");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn == null) {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	private void PreProd_Initializer() {
		try {
			String dbURL = "jdbc:db2://" + System.getProperty("HostName_PreProd") + ":" + System.getProperty("Port") + "/" + System.getProperty("DBName_PreProd");
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			conn = DriverManager.getConnection(dbURL, System.getProperty("UserName"), System.getProperty("Password"));
			if(conn == null) {
				System.out.println("Database connection Failed!!");
			}
		} catch (Exception e) {
			System.out.println("Error while connecting to the Database -" + e.getMessage());
		}
	}

	public String getValues(String tableName, String colName, String condition) {
		final String sql = "select " + colName + " from " + tableName + " where " + condition;
		String data = "";
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			final ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				data = "No Record Found";
			} else {
				do {
					data = rs.getString(1);
				} while (rs.next());
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while getting the " + colName + ". Error:" + e.getMessage());
		}
		return data;
	}
	public int getRecordCount(String tableName, String condition) throws SQLException {
		final String sql = "select count(*) from "+ tableName + " where " + condition;
		final PreparedStatement ps = conn.prepareStatement(sql);
		final ResultSet rs = ps.executeQuery();
		rs.next();
		int count= rs.getInt("1");
		return count;
			
		} 

	public List<String> getMultipleValuesList(String tableName, String colName, String condition) {
		final String sql = "select " + colName + " from " + tableName + " where " + condition;
		List<String> data = new ArrayList<String>();
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				data.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while getting the " + colName + ". Error:" + e.getMessage());
		}
		return data;
	}

	// This method will return the OTP for stepUp authentication from database
	public String getOTP(final String emailId) throws Exception {
		String otp = "";
		final String sql = "select stringvalue from mbrattrval where mbrattr_id=800 and " + "member_id = (select users_id from users where lower(field1)=?)";
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, emailId.toLowerCase());
			final ResultSet rs = ps.executeQuery();
			// wait(3000);
			while (rs.next()) {
				otp = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while fetching OTP: " + e.getMessage());
		}
		return otp;
	}

	// This method will return the user id
	public String getUsersID(String userLoginID) {
		String usersid = "";
		final String sql = "select users_id from users where field1='" + userLoginID.toLowerCase() + "'";
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			final ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				usersid = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while fetching usersid: " + e.getMessage());
		}
		return usersid;
	}

	// Function closing the database connection
	public void closeDBConnection() {
		try {
			if(conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// method for update values in db (ST-3510) // changes V.N Nagarajan

	public void updateValues(String tableName, String colName, String condition, String value) {
		final String sql = "UPDATE " + tableName + " SET " + colName + "=" + value + " where " + condition;
		String data = "";
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Something went wrong while fetching usersid: " + e.getMessage());
		}
		// return data;
	}
	public void wcsSchemaRun()
	{
		final String sql= "SET SCHEMA WCSOWNER";
		try {
			final PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Something went wrong while running schema: " + e.getMessage());
		}
		
	}

}
