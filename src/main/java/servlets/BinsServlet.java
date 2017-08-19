package servlets;

import com.google.gson.Gson;
import models.Bin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class BinsServlet extends HttpServlet {

    private HashMap<String, Bin> bins = new HashMap<String, Bin>();
    private Vector<String> history = new Vector<String>();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");

        if ( name == null ) {
            Vector<Bin> filteredBins = new Vector<Bin>();
            for (String aHistory : history) {
                Bin bin = bins.get(aHistory);
                if (bin != null) {
                    filteredBins.add(bin);
                }
            }

            String json = new Gson().toJson(filteredBins);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_OK);

        } else {
            Bin bin = bins.get(name);

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

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Bin bin = new Bin();
        bins.put(bin.getName(), bin);
        history.add(bin.getName());

        String json = new Gson().toJson(bin);
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
