

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


@WebServlet(urlPatterns = {"/AddNewAddress"})
public class AddNewAddress extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String addr1 = request.getParameter("addr1");
        String addr2 = request.getParameter("addr2");
        String country = request.getParameter("country");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        
        int addrid;
        
        String op="";
        
        Connection c;
        Statement s;
        ResultSet rs = null;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            if(token != null && userid != null)
            {
                rs = s.executeQuery("select userid from users_token where token = "+token+ " AND userid = "+userid);
                if(rs.next())
                {
                    rs = s.executeQuery("select MAX(addrid) from users_addr");
                    if(rs.next())
                    {
                        addrid = rs.getInt("max");
                        if(addrid < 0)
                            addrid = 100;
                        else
                            addrid++;
                        s.executeUpdate("insert into users_addr values ("+addrid+", "+userid+", '"+addr1+"', '"+addr2+"','"+country+"','"+state+"',"+zip+")");
                        op = "successful";
                    }
                }
                else
                {
                    op = "Invalid user";
                }
                rs.close();
            }
            else
            {
                op = "Invalid token";
            }
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            rs = null;
            s.close();
            c.close();
            s = null;
            c = null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
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
