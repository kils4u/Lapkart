

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


@WebServlet(urlPatterns = {"/big_offers"})
public class big_offers extends HttpServlet {
    
    String query = "select * from offer where off_id IN (select offer_id from big_offers)";
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection c = null;
        Statement stmt = null;
        String output = null;
        response.setContentType("application/json;charset=UTF-8");
        
        try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next())
                {
                    output = "{\"big_offer\" : [ ";
                    output += "{ \"off_id\" : \"" + rs.getString("off_id") + "\", ";
                    output += "\"off_img_link\" : \"" + rs.getString("off_img_link") + "\"}";
                }
                while(rs.next())
                {
                    output += ", { \"off_id\" : \"" + rs.getString("off_id") + "\", ";
                    output += "\"off_img_link\" : \"" + rs.getString("off_img_link") + "\"} ";
                }
                if(output != null)
                    output += "] }";
                stmt.close();
                c.close();
            }
        catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }
            PrintWriter out = response.getWriter();
            out.print(output);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
