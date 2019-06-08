package com.finance.revolution.revMoneyApp.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement
public class Account {

	private long accountId;
	private String PhoneNumber;
	private BigDecimal balance;
	//conversion of currency to be handled
	private String currencyType;

	public Account() {

	}

	public Account(String PhoneNumber, BigDecimal balance, String currencyType) {
		this.PhoneNumber = PhoneNumber;
		this.balance = balance;
		this.currencyType = currencyType;
	}

	public Account(long accountId, String PhoneNumber, BigDecimal balance, String currencyType) {
		this.accountId = accountId;
		this.PhoneNumber = PhoneNumber;
		this.balance = balance;
		this.currencyType = currencyType;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PhoneNumber == null) ? 0 : PhoneNumber.hashCode());
		result = prime * result + (int) (accountId ^ (accountId >>> 32));
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((currencyType == null) ? 0 : currencyType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (PhoneNumber == null) {
			if (other.PhoneNumber != null)
				return false;
		} else if (!PhoneNumber.equals(other.PhoneNumber))
			return false;
		if (accountId != other.accountId)
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (currencyType == null) {
			if (other.currencyType != null)
				return false;
		} else if (!currencyType.equals(other.currencyType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", PhoneNumber=" + PhoneNumber + ", balance=" + balance
				+ ", currencyType=" + currencyType + "]";
	}

}
