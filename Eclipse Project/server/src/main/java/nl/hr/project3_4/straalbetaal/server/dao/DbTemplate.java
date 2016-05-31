package nl.hr.project3_4.straalbetaal.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public abstract class DbTemplate {

	private static final Logger LOG = Logger.getLogger(DbTemplate.class.getName());

	protected Connection getConnection() {
		Connection con = null;
		String host = "jdbc:mysql://localhost:3306/straalbetaal";
		String uName = "straalbetaal";
		String uPass = "33fm3K";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(host, uName, uPass);
			LOG.info("Connection with mysql server made sucessfully.");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			LOG.error("CONNECTION WITH MYSQL SERVER FAILED!!!");
		}

		return con;
	}

	protected void closeResources(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			LOG.info("Sql Resources closed succesfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("ERROR CLOSING RESOURCES!!!");
		}
	}

	protected void closeResources(Connection con, PreparedStatement stmt, ResultSet rs) {
		try {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			LOG.info("Sql Resources closed succesfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("ERROR CLOSING RESOURCES!!!");
		}
	}

}
