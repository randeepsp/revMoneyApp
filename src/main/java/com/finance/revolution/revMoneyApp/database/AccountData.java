package com.finance.revolution.revMoneyApp.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.model.Account;
import com.finance.revolution.revMoneyApp.model.Transaction;

public class AccountData {

	private static final Logger LOGGER = Logger.getLogger(AccountData.class);
	UserData userData = new UserData();
	static Account accountCL;
	static Account fromAccountCL;
	static Account toAccountCL;

	public List<Account> getAllAccounts() throws Exception {

		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		List<Account> accounts = new ArrayList<Account>();
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_GET_ALL);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Account account = new Account(rs.getLong("AccountId"), rs.getString("PhoneNumber"),
						rs.getBigDecimal("Balance"), rs.getString("Currency"));
				accounts.add(account);
				LOGGER.debug("Adding account " + account + " to accounts List");

			}
			return accounts;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed for accounts");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());

		}

	}

	public Account getAccountById(String phoneNumber) throws Exception {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		Account account = null;
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_GET_BYID);
			prepStmt.setString(1, phoneNumber);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				account = new Account(rs.getLong("accountId"), rs.getString("PhoneNumber"), rs.getBigDecimal("Balance"),
						rs.getString("Currency"));
			}
			LOGGER.debug("Got the account -> " + account);

			return account;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("fetch failed for Accountid");
		} finally {
			DbUtils.closeQuietly(conn, prepStmt, rs);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());

		}
	}

	public long insertAccount(Account account) throws Exception {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.debug("Attempting to insert " + account);
		accountCL = account;

		try {
			conn = DatabaseUtils.getConnection();
			int rowsChanged;
			prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_CREATE);
			synchronized (accountCL) {

				prepStmt.setString(1, account.getPhoneNumber());
				prepStmt.setBigDecimal(2, account.getBalance());
				prepStmt.setString(3, account.getCurrencyType());
				rowsChanged = prepStmt.executeUpdate();
				LOGGER.debug("No of rows changed -> " + rowsChanged);
			}
			return rowsChanged;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("account insert failed due to");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());

		}
	}

	public long deleteAccount(String phoneNumber) throws Exception {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.debug("Attempting to delete account with phone number -> " + phoneNumber);
		try {
			conn = DatabaseUtils.getConnection();
			prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_DELETE);
			prepStmt.setString(1, phoneNumber);
			int rowsChanged = prepStmt.executeUpdate();
			LOGGER.debug("No of rows changed -> " + rowsChanged);
			return rowsChanged;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("account delete failed due to unknown reason");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());

		}
	}

	public Account updateAccountBalance(String phoneNumber, BigDecimal amount, String operation) throws Exception {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.debug("Attempting to " + operation + " for amount:" + amount + " of account with phone number ->"
				+ phoneNumber);
		try {
			accountCL = getAccountById(phoneNumber);
			BigDecimal balance = accountCL.getBalance();
			synchronized (accountCL) {

				LOGGER.debug("Current balance ->" + balance);
				if (operation.equals("ADD")) {
					balance = balance.add(amount);
				}
				if (operation.equals("DEDUCT")) {
					balance = balance.subtract(amount);
				}
				if (balance.compareTo(BigDecimal.ZERO) < 0)
					throw new Exception("Failed due to insufficient balance");
				conn = DatabaseUtils.getConnection();
				prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_UPDATE_BALANCE);
				prepStmt.setString(2, phoneNumber);
				prepStmt.setBigDecimal(1, balance);
				int rowsChanged = prepStmt.executeUpdate();
				if (rowsChanged == 0) {
					LOGGER.error("updateAccountBalance(): " + operation + " failed");
					throw new Exception("updateAccountBalance(): " + operation + " failed");
				}
				return getAccountById(phoneNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("updateAccountBalance(): failed due to unknown reasons");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public Account updateAccountBalance(String phoneNumber, BigDecimal amount, String operation, Connection conn)
			throws Exception {
		PreparedStatement prepStmt = null;
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.debug("Attempting to " + operation + " for amount:" + amount + " of account with phone number ->"
				+ phoneNumber);
		try {
			accountCL = getAccountById(phoneNumber);
			BigDecimal balance = accountCL.getBalance();
			synchronized (accountCL) {
				LOGGER.debug("Current balance ->" + balance);
				if (operation.equals("ADD")) {
					balance = balance.add(amount);
				}
				if (operation.equals("DEDUCT")) {
					balance = balance.subtract(amount);
				}
				if (balance.compareTo(BigDecimal.ZERO) < 0)
					throw new Exception("Failed due to insufficient balance");
				conn = DatabaseUtils.getConnection();
				prepStmt = conn.prepareStatement(DatabaseElements.ACCOUNT_UPDATE_BALANCE);
				prepStmt.setString(2, phoneNumber);
				prepStmt.setBigDecimal(1, balance);
				int rowsChanged = prepStmt.executeUpdate();
				if (rowsChanged == 0) {
					LOGGER.error("updateAccountBalance(): " + operation + " failed");
					throw new Exception("updateAccountBalance(): " + operation + " failed");
				}
				return getAccountById(phoneNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("updateAccountBalance(): failed due to unknown reasons");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	public boolean transferAmount(Transaction transaction) throws Exception {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		Account fromAccount = null;
		Account toAccount = null;
		String fromCurrency = null;
		String toCurrency = null;
		BigDecimal withdrawAmount = null;
		BigDecimal depositAmount = null;

		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.debug("Transfer amount " + transaction.getAmount() + " from " + transaction.getFromAccount() + " to "
				+ transaction.getToAccount());
		try {
			fromAccount = getAccountById(transaction.getFromAccount());
			toAccount = getAccountById(transaction.getToAccount());
			// check if both account exists
			if (fromAccount == null || toAccount == null)
				throw new Exception("no such account");

			// get currency types
			fromCurrency = fromAccount.getCurrencyType();
			toCurrency = toAccount.getCurrencyType();

			// convert amount to fromAccount currency type and toAccount currencyType
			withdrawAmount = MyCurrencyConverter.convert(Currency.getInstance(transaction.getCurrencyType()),
					Currency.getInstance(fromCurrency), transaction.getAmount());
			depositAmount = MyCurrencyConverter.convert(Currency.getInstance(transaction.getCurrencyType()),
					Currency.getInstance(toCurrency), transaction.getAmount());

			if ((fromAccount.getBalance().subtract(withdrawAmount)).compareTo(new BigDecimal(0)) < 0)
				throw new Exception("Failed due to insufficient balance");

			fromAccountCL = fromAccount;
			toAccountCL = toAccount;
			conn = DatabaseUtils.getConnection();
			conn.setAutoCommit(false);

			synchronized (fromAccountCL) {
				updateAccountBalance(fromAccount.getPhoneNumber(), withdrawAmount, DatabaseElements.ACCOUNT_DEDUCT,
						conn);
			}
			synchronized (toAccountCL) {
				updateAccountBalance(toAccount.getPhoneNumber(), depositAmount, DatabaseElements.ACCOUNT_ADD, conn);
			}
			conn.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("transaction failed due to");
		} finally {
			DbUtils.closeQuietly(prepStmt);
			DbUtils.closeQuietly(conn);
			LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
}
