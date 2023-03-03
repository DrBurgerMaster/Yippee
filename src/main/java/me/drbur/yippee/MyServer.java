package me.drbur.yippee;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.logging.Logger;

public class MyServer {
    private Server server;
    private int port;

    public MyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        server = new Server(port);

        // Set up a context handler for the servlets
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/app");
        servletContextHandler.addServlet(new ServletHolder(new MyServlet()), "/*");
        servletContextHandler.setResourceBase("static/build");

        // Set up a default servlet for static files
        ServletHolder defaultServletHolder = new ServletHolder(new DefaultServlet());
        defaultServletHolder.setInitParameter("resourceBase", "static/build");
        defaultServletHolder.setInitParameter("dirAllowed", "false");
        defaultServletHolder.setInitParameter("pathInfoOnly", "true");

        ServletContextHandler staticContextHandler = new ServletContextHandler();
        staticContextHandler.setContextPath("/");
        staticContextHandler.addServlet(defaultServletHolder, "/*");

        // Set up a request log
        RequestLog requestLog = new MyRequestLog();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);

        // Add the handlers to the server
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(servletContextHandler);
        handlers.addHandler(staticContextHandler);
        handlers.addHandler(requestLogHandler);
        server.setHandler(handlers);

        server.start();
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }

    public static class MyRequestLog implements RequestLog {
        private static final Logger logger = Logger.getLogger(MyRequestLog.class.getName());

        @Override
        public void log(Request request, Response response) {
            logger.info(request.getMethod() + " " + request.getRequestURI() + " " + response.getStatus());
        }
    }
}