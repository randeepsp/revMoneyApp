package com.finance.revolution.revMoneyApp.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Transaction {
	
	//conversion of currency to be handled
	private String currencyType;
	private BigDecimal amount;
	private String fromAccount;
	private String toAccount;
	
	public Transaction(){
		
	}
	
	public Transaction(String currencyType,BigDecimal amount,String fromAccount,String toAccount){
		this.currencyType=currencyType;
		this.amount=amount;
		this.fromAccount=fromAccount;
		this.toAccount=toAccount;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	
	
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currencyType == null) ? 0 : currencyType.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((toAccount == null) ? 0 : toAccount.hashCode());
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
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currencyType == null) {
			if (other.currencyType != null)
				return false;
		} else if (!currencyType.equals(other.currencyType))
			return false;
		if (fromAccount == null) {
			if (other.fromAccount != null)
				return false;
		} else if (!fromAccount.equals(other.fromAccount))
			return false;
		if (toAccount == null) {
			if (other.toAccount != null)
				return false;
		} else if (!toAccount.equals(other.toAccount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [currencyType=" + currencyType + ", amount=" + amount + ", fromAccount=" + fromAccount
				+ ", toAccount=" + toAccount + "]";
	}


}
