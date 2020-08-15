
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


@WebServlet(urlPatterns = {"/TokenVerify"})
public class TokenVerify extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection c;
        Statement stmt;
        ResultSet rs;
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String name;
        String op = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            if(token != null)
            {
                rs = stmt.executeQuery("select * from users_token where token = '"+token+"'");
                if(rs.next())
                {
                    String uid = rs.getString("userid");
                    if(rs.getString("userid").compareTo(userid)==0)
                    {
                        ResultSet r = stmt.executeQuery("select * from users where userid = " + uid);
                        if(r.next())
                        {
                            name = r.getString("fname") + " " + r.getString("lname");
                            op = "{\"token_info\" : [{ \"status\" : \"successful\",";
                            op += "\"token\" : \""+ token +"\",";
                            op += "\"name\" : \"" + name + "\"";
                            ResultSet count = stmt.executeQuery("select COUNT(cartid) from users_cart where userid = "+uid);
                            if(count.next())
                            {
                                op += " ,\"cart_count\" : \"" + count.getString("count") + "\"";
                            }
                            op += " }]}";
                        }
                        else
                        {
                            System.out.println(" r is empty ");
                        }
                    }
                    else
                    {
                        op = "{\"token_info\" : [{ \"status\" : \"fail\",";
                        op += "\"token\" : \"\",";
                        op += "\"name\" : \"\" }]}";
                    }
                }
                else
                {
                    op = "{\"token_info\" : [{ \"status\" : \"fail\",";
                    op += "\"token\" : \"\",";
                    op += "\"name\" : \"\" }]}";
                }
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(op);
            }
            else
            {
                op = "{\"token\" : [{ \"status\" : \"fail\",";
                op += "\"token\" : \"\",";
                op += "\"name\" : \"\" }]}";
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(op);
            }
            stmt.close();
            c.close();
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(TokenVerify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TokenVerify.class.getName()).log(Level.SEVERE, null, ex);
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
