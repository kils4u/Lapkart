
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/best_selling"})
public class best_selling extends HttpServlet {
    
    String query = "select * from product where pid IN (select product_id from best_selling)";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Statement stmt = null;
        Connection c = null;
        String output = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                output = "{ \"best_selling\" : [ ";
                output += "{ \"pid\" : \"" + rs.getString("pid") + "\",";
                output += "\"pbrand\" : \"" + rs.getString("pbrand") + "\",";
                output += "\"pmodel\" : \"" + rs.getString("pmodel") + "\",";
                output += "\"pimg_link\" : \"" + rs.getString("pimg") + "\",";
                output += "\"pprice\" : \"" + rs.getString("pprice") + "\",";
                output += "\"pprocessor\" : \"" + rs.getString("pprocessor") + "\",";
                output += "\"phdd_size\" : \"" + rs.getString("phdd") + "\",";
                output += "\"pram_size\" : \"" + rs.getString("pram") + "\",";
                output += "\"pgraphics_size\" : \"" + rs.getString("pgraphics") + "\"}";
             }
            while(rs.next())
            {
                output += ", { \"pid\" : \"" + rs.getString("pid") + "\",";
                output += "\"pbrand\" : \"" + rs.getString("pbrand") + "\",";
                output += "\"pmodel\" : \"" + rs.getString("pmodel") + "\",";
                output += "\"pimg_link\" : \"" + rs.getString("pimg") + "\",";
                output += "\"pprice\" : \"" + rs.getString("pprice") + "\",";
                output += "\"pprocessor\" : \"" + rs.getString("pprocessor") + "\",";
                output += "\"phdd_size\" : \"" + rs.getString("phdd") + "\",";
                output += "\"pram_size\" : \"" + rs.getString("pram") + "\",";
                output += "\"pgraphics_size\" : \"" + rs.getString("pgraphics") + "\"}";
            }
            if(output != null)
                output += "] }";
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(output);
            stmt.close();
            c.close();
        } catch (Exception ex) {
            Logger.getLogger(deals_of_the_day.class.getName()).log(Level.SEVERE, null, ex);
        }
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
