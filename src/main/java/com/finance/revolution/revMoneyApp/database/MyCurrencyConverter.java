package com.finance.revolution.revMoneyApp.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.apache.log4j.Logger;

public class MyCurrencyConverter {
	private static final Logger LOGGER = Logger.getLogger(MyCurrencyConverter.class);

	final static Currency USD = Currency.getInstance("USD");
	final static Currency EUR = Currency.getInstance("EUR");
	final static Currency INR = Currency.getInstance("INR");

	static BigDecimal convert(Currency input, Currency output, BigDecimal amount) throws Exception {
		// convertINR
		LOGGER.info("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		LOGGER.info("Request to convert " + amount + " from " + input + " to " + output);
		amount = amount.setScale(7, RoundingMode.HALF_UP);
		if (input.getCurrencyCode().equals(output.getCurrencyCode())) {
			return amount;
		} else if (input.getCurrencyCode().equals("INR")) {
			if (output.getCurrencyCode().equals("USD")) {
				LOGGER.debug("Convert INR to USD");
				return convertINR_to_USD(amount);
			} else if (output.getCurrencyCode().equals("EUR")) {
				LOGGER.debug("Convert INR to EUR");
				return convertINR_to_EUR(amount);
			} else
				throw new Exception("Currency Not supported");
		}
		// convert EUR
		else if (input.getCurrencyCode().equals("EUR")) {
			if (output.getCurrencyCode().equals("USD")) {
				LOGGER.debug("Convert EUR to USD");
				return convertEUR_to_USD(amount);
			} else if (output.getCurrencyCode().equals("INR")) {
				LOGGER.debug("Convert EUR to INR");
				return new BigDecimal(1).divide(convertINR_to_EUR(amount), 7, RoundingMode.HALF_UP);
			} else
				throw new Exception("Currency Not supported");
		}
		// convert USD
		else if (input.getCurrencyCode().equals("USD")) {
			if (output.getCurrencyCode().equals("INR")) {
				LOGGER.debug("Convert USD to INR");
				return new BigDecimal(1).divide(convertINR_to_USD(amount), 7, RoundingMode.HALF_UP);
			} else if (output.getCurrencyCode().equals("EUR")) {
				LOGGER.debug("Convert USD to EUR");
				return new BigDecimal(1).divide(convertEUR_to_USD(amount), 7, RoundingMode.HALF_UP);
			} else
				throw new Exception("Currency Not supported");
		} else
			throw new Exception("Currency Not supported");
	}

	static BigDecimal convertINR_to_USD(BigDecimal amount) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return new BigDecimal(1).multiply(new BigDecimal(0.0144184));
	}

	static BigDecimal convertEUR_to_USD(BigDecimal amount) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return new BigDecimal(1).multiply(new BigDecimal(1.13369));
	}

	static BigDecimal convertINR_to_EUR(BigDecimal amount) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		return new BigDecimal(1).multiply(new BigDecimal(0.0127200));
	}

}
