package servlets;

import executor.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;


public class AllRequestsServlet extends HttpServlet {
    private final DBService dbService;

    public AllRequestsServlet(DBService dbService) {
        this.dbService = dbService;
    }


    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        long lastModified;
        if(method.equals("GET")) {
            lastModified = this.getLastModified(req);
            if (lastModified != -1L) {
                long ifModifiedSince = req.getDateHeader("If-Modified-Since");
                if(ifModifiedSince < lastModified) {
                    this.maybeSetLastModified(resp, lastModified);
                } else {
                    resp.setStatus(304);
                }
            }
        } else if(method.equals("HEAD")) {
            lastModified = this.getLastModified(req);
            this.maybeSetLastModified(resp, lastModified);
        }
        this.doAny(req, resp);
    }

    private void maybeSetLastModified(HttpServletResponse resp, long lastModified) {
        if(!resp.containsHeader("Last-Modified")) {
            if(lastModified >= 0L) {
                resp.setDateHeader("Last-Modified", lastModified);
            }

        }
    }

    private void doAny(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.getWriter().println(request.getQueryString());

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

}