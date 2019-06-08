package com.finance.revolution.revMoneyApp;
 
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.finance.revolution.revMoneyApp.database.MoneyDBInitializer;
import com.finance.revolution.revMoneyApp.resource.AccountResource;
import com.finance.revolution.revMoneyApp.resource.TransactionResource;
import com.finance.revolution.revMoneyApp.resource.UserResource;

/**
 * Hello world!
 *
 */
public class App {
	private static Logger LOGGER = Logger.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Initializing the app .....");
		new MoneyDBInitializer().contextInitialized();
		startService();
		LOGGER.debug("Service started .....");
	}

	private static void startService() throws Exception {
		Server server = new Server(9080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames", UserResource.class.getCanonicalName()
				+ "," + AccountResource.class.getCanonicalName() + "," + TransactionResource.class.getCanonicalName());
				
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}
}
