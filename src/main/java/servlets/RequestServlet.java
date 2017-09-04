package servlets;

import com.google.gson.Gson;
import executor.DBException;
import executor.DBService;
import models.Bin;
import models.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class RequestServlet extends HttpServlet {
    private final DBService dbService;

    public RequestServlet(DBService dbService) {
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
        try {
            this.doAny(req, resp);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private void maybeSetLastModified(HttpServletResponse resp, long lastModified) {
        if(!resp.containsHeader("Last-Modified")) {
            if(lastModified >= 0L) {
                resp.setDateHeader("Last-Modified", lastModified);
            }

        }
    }

    private void doAny(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException, DBException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || Objects.equals(pathInfo, "/")) {
            String json = new Gson().toJson("error");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            return;

        }


        String[] pathParts = pathInfo.split("/");

        if (pathParts.length == 2) {
            String name = pathParts[1];
            Bin bin = null;
            try {
                bin = dbService.getOneBin(name);
            } catch (DBException e) {
                e.printStackTrace();
            }

            if (bin == null) {
                String json = new Gson().toJson("error");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

            } else {
                Request dbRequest = new Request(request, bin);
                dbService.addRequest(dbRequest);
                String json = new Gson().toJson("ok");
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            }
        }

    }

}