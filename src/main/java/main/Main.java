package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllRequestsServlet;
import servlets.BinsServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();
        BinsServlet binsServlet = new BinsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(allRequestsServlet), "/");
        context.addServlet(new ServletHolder(binsServlet), "/bins/");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
