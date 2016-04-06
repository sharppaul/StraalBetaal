package nl.hr.project3_4.straalbetaal.server;

import java.net.URI;

import org.apache.log4j.Logger;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import nl.hr.project3_4.straalbetaal.server.resources.Resource;

public class Server {

	private static final Logger LOG = Logger.getLogger(Server.class.getName());

	public static void main(String[] args) throws Exception {
		new Server();
		LOG.info("Server started.");
	}

	public Server() throws Exception {
		HttpServer server = initWebserver();
		server.start();
		while (true) {
			Thread.sleep(1000);
		}
	}

	private HttpServer initWebserver() {
		ResourceConfig config = new ResourceConfig(Resource.class);
		config.register(JacksonJaxbJsonProvider.class);
		URI uri = URI.create("http://0.0.0.0:" + 8025);

		// NOT WORKING CODE!!!
		// SSLContextConfigurator sslConf = new SSLContextConfigurator();
		// sslConf.setKeyStoreFile("./keystore_server"); // contains server
		// keypair
		// sslConf.setKeyStorePass("asdfgh");
		// sslConf.setTrustStoreFile("./truststore_server"); // contains client
		// certificate
		// sslConf.setTrustStorePass("asdfgh");

		// return GrizzlyHttpServerFactory.createHttpServer(uri, config, true,
		// new SSLEngineConfigurator(sslConf));
		return GrizzlyHttpServerFactory.createHttpServer(uri, config, true);
	}

}
