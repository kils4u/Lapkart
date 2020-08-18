

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Product"})
public class Product extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection c,d;
        Statement stmt;
        ResultSet rs,r;
        String pid = request.getParameter("pid");
        String op = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            d = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            Statement stmt2 = d.createStatement();
            rs = stmt.executeQuery("select * from product_spec where pid = " + pid);
            if(rs.next())
            {
                op = "{ \"product\" : [{";
                op += "\"pid\" : \""+rs.getString("pid")+"\",";
                op += "\"bname\" : \""+rs.getString("bname")+"\",";
                op += "\"mname\" : \""+rs.getString("mname")+"\",";
                op += "\"color\" : \""+rs.getString("color")+"\",";
                op += "\"weight\" : \""+rs.getString("weight")+"\",";
                op += "\"processor\" : \""+rs.getString("processor")+"\",";
                op += "\"processor_gen\" : \""+rs.getString("processor_gen")+"\",";
                op += "\"processor_speed\" : \""+rs.getString("processor_speed")+"\",";
                op += "\"ram\" : \""+rs.getString("ram")+"\",";
                op += "\"ram_size\" : \""+rs.getString("ram_size")+"\",";
                op += "\"graphics\" : \""+rs.getString("graphics_card")+"\",";
                op += "\"graphics_size\" : \""+rs.getString("graphics_size")+"\",";
                op += "\"ssd\" : \""+rs.getString("ssd")+"\",";
                op += "\"hdd\" : \""+rs.getString("hhd")+"\",";
                op += "\"display\" : \""+rs.getString("disp")+"\",";
                op += "\"disp_size\" : \""+rs.getString("disp_size")+"\",";
                op += "\"scr_res\" : \""+rs.getString("scr_res")+"\",";
                r = stmt2.executeQuery("select * from product where pid = " + pid);
                if(r.next())
                {
                    op += "\"price\" : \""+r.getString("pprice")+"\",";
                    op += "\"img\" : \""+r.getString("pimg")+"\"}]}";
                }
            }
            
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            stmt.close();
            c.close();
            d.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
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
