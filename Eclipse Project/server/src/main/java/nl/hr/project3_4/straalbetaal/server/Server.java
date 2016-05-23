package nl.hr.project3_4.straalbetaal.server;

import java.net.URI;

import org.apache.log4j.Logger;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import nl.hr.project3_4.straalbetaal.server.resources.Resource;

public class Server {

	private static final Logger LOG = Logger.getLogger(Server.class.getName());

	public Server() throws Exception {
		HttpServer server = initWebserver();
		server.start();
		while (true) {
			Thread.sleep(1000);
		}
	}

	public static void main(String[] args) throws Exception {
		new Server();
		LOG.info("Server started!");
	}

	private HttpServer initWebserver() {
		ResourceConfig config = new ResourceConfig(Resource.class);
		config.register(JacksonJaxbJsonProvider.class);
		URI uri = URI.create("http://0.0.0.0:" + 8025);

		// NOT WORKING CODE!!!
		SSLContextConfigurator sslConf = new SSLContextConfigurator();
		sslConf.setKeyStoreFile("./keystore_server"); // contains server keypair
		sslConf.setKeyStorePass("33fm3K");
		sslConf.setTrustStoreFile("./truststore_server"); // client cert.
		sslConf.setTrustStorePass("33fm3K");
		SSLEngineConfigurator sslEngine = new SSLEngineConfigurator(sslConf);

		// comment next three lines to enable client authentication (If it works
		// at all)
		sslEngine.setClientMode(false);
		sslEngine.setNeedClientAuth(false);
		sslEngine.setWantClientAuth(false);

		return GrizzlyHttpServerFactory.createHttpServer(uri, config, true, sslEngine);
		// return GrizzlyHttpServerFactory.createHttpServer(uri, config, true);
	}

}
