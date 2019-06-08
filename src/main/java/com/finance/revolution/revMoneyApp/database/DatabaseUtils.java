package com.finance.revolution.revMoneyApp.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

public class DatabaseUtils {
	private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class);

	DatabaseUtils() {
		
	}

	public static Connection getConnection() throws SQLException {
		LOGGER.debug("In " + Thread.currentThread().getStackTrace()[1].getMethodName());
		DbUtils.loadDriver(DatabaseElements.H2_DRIVER);
		return DriverManager.getConnection(DatabaseElements.H2_CONNECTION_URL, DatabaseElements.H2_USER,
				DatabaseElements.H2_PASSWORD);

	}

	public void buildTables() {
		LOGGER.info("Creating User and Account tables ");
		Connection conn = null;
		try {
			conn = getConnection();
			InputStream is = getClass().getResourceAsStream("/default.sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			RunScript.execute(conn, reader);
		} catch (SQLException e) {
			LOGGER.error("Error creating tables ", e);
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}