package com.finance.revolution.revMoneyApp.database;

public interface DatabaseElements {

	static final String H2_DRIVER = "org.h2.Driver";
	static final String H2_CONNECTION_URL = "jdbc:h2:mem:moneyApp;TRACE_LEVEL_FILE=2;DB_CLOSE_DELAY=-1";
	static final String H2_USER = "sa";
	static final String H2_PASSWORD = "";

	static final String USER_GET_BY_PHONE = "SELECT * FROM MoneyUser WHERE PhoneNumber=?";
	static final String USER_GET_BYID = "SELECT * FROM MoneyUser WHERE UserId=?";
	static final String USER_GET_BYUSERNAME = "SELECT * FROM MoneyUser WHERE UserName=?";
	static final String USER_GET_BYEMAILID = "SELECT * FROM MoneyUser WHERE EmailId=?";
	static final String USER_GET_ALL = "SELECT * FROM MoneyUser";
	static final String USER_INSERT = "INSERT INTO MoneyUser (PhoneNumber,UserName,EmailId) VALUES (?,?,?)";
	static final String USER_UPDATE = "UPDATE MoneyUser SET PhoneNumber=?,UserName=?,EmailId=? WHERE PhoneNumber=?";
	static final String USER_UPDATE_BY_ID = "UPDATE MoneyUser SET PhoneNumber = ? , UserName = ? , EmailId = ? WHERE UserId = ? ";
	static final String USER_DELETE = "DELETE FROM MoneyUser WHERE PhoneNumber=?";

	static final String ACCOUNT_GET_BYID = "SELECT * FROM MoneyAccount WHERE PhoneNumber = ? ";
	static final String ACCOUNT_CREATE = "INSERT INTO MoneyAccount (PhoneNumber, Balance, Currency) VALUES (?, ?, ?)";
	static final String ACCOUNT_UPDATE_BALANCE = "UPDATE MoneyAccount SET Balance = ? WHERE PhoneNumber = ? ";
	static final String ACCOUNT_GET_ALL = "SELECT * FROM MoneyAccount";
	static final String ACCOUNT_DELETE = "DELETE FROM MoneyAccount WHERE PhoneNumber = ?";

}
