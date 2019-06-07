package com.finance.revolution.revMoneyApp.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;

public class DatabaseSetup {

	public static void createDBforTests() {
		Connection conn = null;
		try {
			conn = DatabaseUtils.getConnection();
			InputStream is = DatabaseSetup.class.getResourceAsStream("/default.sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			RunScript.execute(conn, reader);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public static void prepareDBforTests() {
		Connection conn = null;
		InputStream is;
		BufferedReader reader;
		try {
			conn = DatabaseUtils.getConnection();
			is = DatabaseSetup.class.getResourceAsStream("/defaultvalues.sql");
			reader = new BufferedReader(new InputStreamReader(is));
			RunScript.execute(conn, reader);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
