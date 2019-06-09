package com.finance.revolution.revMoneyApp.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.database.AccountData;
import com.finance.revolution.revMoneyApp.model.Transaction;

public class TransactionService {
	private static final Logger LOGGER = Logger.getLogger(TransactionService.class);

	AccountData accountData = new AccountData();

	public boolean transfer(Transaction transaction) throws Exception {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		String currencyType = transaction.getCurrencyType();
		if (transaction.getAmount().compareTo(new BigDecimal(0))<0) {
			throw new Exception("Amount cannot be less than 0");
		}
		if(transaction.getFromAccount().equals(transaction.getToAccount())) {
			throw new Exception("From and To account cannot be same");
		}
		try {
			synchronized (transaction) {
				accountData.transferAmount(transaction);
				return true;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
