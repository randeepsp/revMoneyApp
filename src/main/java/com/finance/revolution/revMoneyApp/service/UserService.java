package com.finance.revolution.revMoneyApp.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.database.UserData;
import com.finance.revolution.revMoneyApp.model.User;

public class UserService {
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class);

	UserData userData = new UserData();

	public User getUserByNo(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return userData.getUserByNo(phoneNumber);
	}

	public List<User> getAllUsers() throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return userData.getAllUsers();
	}

	public synchronized long createUser(User user) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());		User userExists = getUserByNo(user.getPhoneNumber());
		if(userExists!=null){
			throw new Exception("User already exists"); 
		}
		long id = userData.insertUser(user);
		return id;
	}

	public synchronized User updateUser(String phoneNumber, User user) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User userExists = getUserByNo(phoneNumber);
		if(userExists==null){
			throw new Exception("No such user exists"); 
		}
		return userData.updateUserById(userExists.getUserId(), user);
	}

	public synchronized boolean deleteUser(String phoneNumber) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User user = getUserByNo(phoneNumber);
		if(user==null){
			throw new Exception("No such user exists"); 
		}
		if(userData.deleteUser(phoneNumber)==0)
			return false;
		else
			return true;
	}

}
