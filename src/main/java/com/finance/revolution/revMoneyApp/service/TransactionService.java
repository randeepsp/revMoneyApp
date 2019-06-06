package com.finance.revolution.revMoneyApp.service;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.database.AccountData;
import com.finance.revolution.revMoneyApp.model.Transaction;

public class TransactionService {
	private static final Logger LOGGER = Logger.getLogger(TransactionService.class);

	AccountData accountData = new AccountData();

	public boolean transfer(Transaction transaction) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String currencyType = transaction.getCurrencyType();
		try {
			accountData.transferAmount(transaction);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

}
