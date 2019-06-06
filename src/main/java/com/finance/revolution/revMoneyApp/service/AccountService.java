package com.finance.revolution.revMoneyApp.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.database.AccountData;
import com.finance.revolution.revMoneyApp.model.Account;
import com.finance.revolution.revMoneyApp.model.User;

public class AccountService {

	private static final Logger LOGGER = Logger.getLogger(AccountService.class);
	AccountData accountData = new AccountData();
	UserService userService = new UserService();

	public List<Account> getAllAccounts() throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return accountData.getAllAccounts();
	};

	public Account get(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return accountData.getAccountById(phoneNumber);
	}

	public BigDecimal getAccountBalance(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return accountData.getAccountById(phoneNumber).getBalance();
	}

	public Account getAccountByNo(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return accountData.getAccountById(phoneNumber);
	}

	public long createAccount(Account account) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User user = userService.getUserByNo(account.getPhoneNumber());
		Account accountExists = getAccountByNo(account.getPhoneNumber());
		if(accountExists!=null){
			throw new Exception("Account already present");
		}
		if(user==null){
			throw new Exception("No such user to create account");
		}
		long id = accountData.insertAccount(account);
		return id;
	}

	public Account deposit(String phoneNumber, BigDecimal amount) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account = getAccountByNo(phoneNumber);
		if(account==null){
			throw new Exception("Account does not exist"); 
		}
		accountData.updateAccountBalance(phoneNumber, amount, "ADD");
		return accountData.getAccountById(phoneNumber);
	}

	public Account withdraw(String phoneNumber, BigDecimal amount) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account = getAccountByNo(phoneNumber);
		if(account==null){
			throw new Exception("Account does not exist"); 
		}
		if(account.getBalance().compareTo(amount)<0){
			throw new Exception("Insufficient funds"); 
		}
		return accountData.updateAccountBalance(phoneNumber, amount, "DEDUCT");		
	}

	public boolean delete(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User user = userService.getUserByNo(phoneNumber);
		if(user==null){
			throw new Exception("No such user to create account"); 
		}
		Account account = getAccountByNo(phoneNumber);
		if(account==null){
			throw new Exception("Account for this user "+ user.getUserName() +" does not exist"); 
		}
		if(accountData.deleteAccount(phoneNumber)==0)
			return false;
		else
			return true;
	}

}
