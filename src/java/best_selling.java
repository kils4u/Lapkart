
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/best_selling"})
public class best_selling extends HttpServlet {
    
    String query = "select * from product where p_id IN (select product_id from best_selling)";
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
                output += "{ \"p_id\" : \"" + rs.getString("p_id") + "\",";
                output += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                output += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                output += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                output += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                output += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                output += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                output += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                output += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
             }
            while(rs.next())
            {
                output += ", { \"p_id\" : \"" + rs.getString("p_id") + "\",";
                output += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                output += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                output += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                output += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                output += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                output += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                output += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                output += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
            }
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
