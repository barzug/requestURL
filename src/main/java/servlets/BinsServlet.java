package servlets;

import com.google.gson.Gson;
import executor.DBException;
import executor.DBService;
import models.Bin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class BinsServlet extends HttpServlet {

    private final DBService dbService;

    public BinsServlet(DBService dbService) {
        this.dbService = dbService;
    }

    private Vector<String> history = new Vector<>();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || Objects.equals(pathInfo, "/")) {
            Vector<Bin> filteredBins = null;
            try {
                filteredBins = dbService.getManyBins(history);
            } catch (DBException e) {
                e.printStackTrace();
            }

            String json = new Gson().toJson(filteredBins);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);
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
                String json = new Gson().toJson(bin);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

        if (!Objects.equals(pathParts[2], "requests") ) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (pathParts.length == 3) {

            return;
        }
        if (pathParts.length == 4) {

            return;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !Objects.equals(pathInfo, "/")) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Bin bin = new Bin();
        try {
            dbService.addBin(bin);
        } catch (DBException e) {
            e.printStackTrace();
        }
        history.add(bin.getName());

        String json = new Gson().toJson(bin);
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
