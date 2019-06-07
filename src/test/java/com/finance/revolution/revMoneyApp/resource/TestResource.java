package com.finance.revolution.revMoneyApp.resource;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.revolution.revMoneyApp.database.DatabaseSetup;

public class TestResource {

	static Server server = null;
	static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
	static HttpClient client;
	ObjectMapper mapper = new ObjectMapper();
	URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:9080");

	@BeforeAll
	public static void setUp() throws Exception {

		DatabaseSetup.createDBforTests();
		DatabaseSetup.prepareDBforTests();
		startServer();
		connManager.setDefaultMaxPerRoute(30);
		connManager.setMaxTotal(200);
		client = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();

	}

	static void startServer() throws Exception {
		if (server == null) {
			server = new Server(9080);
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
			servletHolder.setInitParameter("jersey.config.server.provider.classnames",
					UserResource.class.getCanonicalName() + "," + AccountResource.class.getCanonicalName() + ","
							+ TransactionResource.class.getCanonicalName());
			server.start();
		}
	}

	@AfterAll
	static void closeClient() throws Exception {
		// server.stop();
		HttpClientUtils.closeQuietly(client);
	}

}
