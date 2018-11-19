package dna.server.app;

import static org.slf4j.LoggerFactory.getLogger;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import dna.server.config.WebConfiguration;

/**
 * startup application servlet
 */
public class ServerApp {

    private static final int MIN_THREADS = 10;
	private static final int MAX_THREADS = 200;

	private static final Logger LOGGER = getLogger(ServerApp.class);
	
	private static final String APP_SERVLET = "classpath:service-servlet.xml";

	
    public static void main(String[] args) throws Exception {
        int exitStatus = 0;
        Server server = null;

        try {
        	final int serverPort = 8080;
            final String webAppContextPath = "/*";
            
            final ServletContextHandler servletContextHandler = getAppContext(webAppContextPath);

            final QueuedThreadPool threadPool = new QueuedThreadPool(MAX_THREADS, MIN_THREADS);
            server = new Server(threadPool);
            final ServerConnector connector = new ServerConnector(server);
            connector.setPort(serverPort);
            server.setConnectors(new Connector[]{connector});
            server.setHandler(servletContextHandler);
            server.start();
            LOGGER.info("server started at port:{} on path:{}", serverPort, webAppContextPath);
            server.join();
        } catch (final InterruptedException e) {
            LOGGER.info("application server interrupted. Finish jvm with ok.");
        } catch (final Exception e) {
            LOGGER.error("application server raise exception. Finish jvm with error: ", e);
            exitStatus = 1;
        } finally {
            if (server != null) {
                server.stop();
                server.destroy();
            }
            LOGGER.info("application end");
        }
        System.exit(exitStatus);
    }

    private static ServletContextHandler getAppContext(String webAppContextPath) {

        final DispatcherServlet dispatcher = new DispatcherServlet();
        dispatcher.setContextConfigLocation(APP_SERVLET);
       
        final ServletHolder servlet = new ServletHolder(dispatcher);
        servlet.setName("dna Appplication");

        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS | ServletContextHandler.NO_SECURITY);
        context.addEventListener(new ContextLoaderListener());
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.setInitParameter("contextConfigLocation", WebConfiguration.class.getName());
        context.setCompactPath(true);;
        context.addServlet(servlet, webAppContextPath);
        LOGGER.info("application's path: {}", webAppContextPath);

        return context;
    }
}
