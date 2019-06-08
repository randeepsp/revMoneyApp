package com.finance.revolution.revMoneyApp.database;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MoneyDBInitializer implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(MoneyDBInitializer.class);

	public void contextInitialized() {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		InputStream is = getClass().getResourceAsStream("/log4j.properties");
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(is));
		PropertyConfigurator.configure(is);
		// PropertyConfigurator.configure("log4j.properties");
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " Starting up! ");
		DatabaseUtils DBUtil = new DatabaseUtils();
		DbUtils.loadDriver(DatabaseElements.H2_DRIVER);
		DBUtil.buildTables();
		LOGGER.debug("Exiting " + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " Shutting down! ");
	}

}