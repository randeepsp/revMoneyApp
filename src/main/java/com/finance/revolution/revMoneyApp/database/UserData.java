package com.finance.revolution.revMoneyApp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.model.User;

public class UserData {
	private static final Logger LOGGER = Logger.getLogger(UserData.class);
	static User userCL;

	public List<User> getAllUsers() throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<User>();

		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.USER_GET_ALL);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getLong("UserId"), rs.getString("PhoneNumber"), rs.getString("UserName"),
						rs.getString("EmailId"));
				users.add(user);
				LOGGER.debug("Adding user " + user + " to users List");
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}

	}

	public User getUserByNo(String PhoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		LOGGER.debug("Attempting to fetch user with phonenumber ->" + PhoneNumber);
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.USER_GET_BY_PHONE);
			prepStmt.setString(1, PhoneNumber);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getLong("UserId"), rs.getString("PhoneNumber"), rs.getString("UserName"),
						rs.getString("EmailId"));
			}
			LOGGER.debug("Returning user " + user);
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed for userid");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public User getUserByName(String userName) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.USER_GET_BYUSERNAME);
			prepStmt.setString(1, userName);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getLong("UserId"), rs.getString("PhoneNumber"), rs.getString("userName"),
						rs.getString("EmailId"));
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed for user name");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public User getUserById(long userId) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		LOGGER.debug("Fetching user for userid " + userId);
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.USER_GET_BYID);
			prepStmt.setLong(1, userId);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				user = new User(rs.getLong("UserId"), rs.getString("PhoneNumber"), rs.getString("UserName"),
						rs.getString("EmailId"));
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed for userId");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public long insertUser(User user) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.info("UD - creating user with " + user.getPhoneNumber() + " " + user.getUserName() + " "
				+ user.getEmailId());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		userCL = user;
		try {
			conn = DatabaseUtils.getConnection();
			synchronized (userCL) {
				prepStmt = conn.prepareStatement(DatabaseElements.USER_INSERT);
				prepStmt.setString(1, user.getPhoneNumber());
				prepStmt.setString(2, user.getUserName());
				prepStmt.setString(3, user.getEmailId());

				int rowsChanged = prepStmt.executeUpdate();
				LOGGER.debug("rowsChanged" + rowsChanged);
				return rowsChanged;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("insert failed due to");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public long deleteUser(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		LOGGER.info("delete user with phonenumber ->" + phoneNumber);
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.USER_DELETE);
			prepStmt.setString(1, phoneNumber);
			int rowsChanged = prepStmt.executeUpdate();
			LOGGER.debug("rowsChanged" + rowsChanged);
			return rowsChanged;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("user delete failed due to unknown reason");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public User updateUser(String phoneNumber, User user) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String errorMsg = "";
		LOGGER.info("UD - updating user with " + user.getPhoneNumber() + " " + user.getUserName() + " "
				+ user.getEmailId());
		userCL = user;
		try {
			conn = DatabaseUtils.getConnection();
			synchronized (userCL) {

				prepStmt = conn.prepareStatement(DatabaseElements.USER_UPDATE_BY_ID);
				prepStmt.setString(1, user.getPhoneNumber());
				prepStmt.setString(2, user.getUserName());
				prepStmt.setString(3, user.getEmailId());
				prepStmt.setLong(4, user.getUserId());
				int rowsChanged = prepStmt.executeUpdate();
				LOGGER.debug("rowsChanged" + rowsChanged);
				return getUserByNo(phoneNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("UPDATE failed due to");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public User updateUserById(long id, User user) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String errorMsg = "";
		userCL = user;
		try {
			conn = DatabaseUtils.getConnection();
			synchronized (userCL) {
				prepStmt = conn.prepareStatement(DatabaseElements.USER_UPDATE_BY_ID);
				prepStmt.setString(1, user.getPhoneNumber());
				prepStmt.setString(2, user.getUserName());
				prepStmt.setString(3, user.getEmailId());
				prepStmt.setLong(4, id);
				int rowsChanged = prepStmt.executeUpdate();
				LOGGER.debug("rowsChanged" + rowsChanged);
				return getUserById(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("UPDATE failed due to");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

}
