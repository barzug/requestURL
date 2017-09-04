package main;

import executor.DBService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllRequestsServlet;
import servlets.BinsServlet;
import servlets.RequestServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AllRequestsServlet(dbService)), "/");
        context.addServlet(new ServletHolder(new BinsServlet(dbService)), "/api/v1/bins/*");
        context.addServlet(new ServletHolder(new RequestServlet(dbService)), "/bins/*");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
