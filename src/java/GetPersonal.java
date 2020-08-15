
import java.io.Console;
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

@WebServlet(urlPatterns = {"/GetPersonal"})
public class GetPersonal extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        
        String op="fail";
        
        Connection c;
        Statement s;
        ResultSet rs;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            
            System.out.print("select userid from users_token where token = "+token+" AND userid = "+userid);
            
            rs = s.executeQuery("select userid from users_token where token = "+token+" AND userid = "+userid );
            if(rs.next())
            {
                op = "{ \"user\" : { ";
                rs = s.executeQuery("select fname, lname from users where userid = "+userid);
                if(rs.next())
                {
                    op += "\"personal\" : { ";
                    op += "\"name\" : \"" + rs.getString("fname") + " " + rs.getString("lname") +"\" , ";
                    
                    rs = s.executeQuery("select email from users_login where userid = "+userid);
                    if(rs.next())
                    {
                        op += "\"email\" : \"" + rs.getString("email") + "\" }";
                    }
                    rs = s.executeQuery("select mobid, mob from users_mob where userid = "+userid);
                    if(rs.next())
                    {
                        op += ",\"contact\" : [ { ";
                        op += "\"mobid\" : \""+rs.getString("mobid")+"\", ";
                        op += "\"mob\" : \""+rs.getString("mob")+"\" ";
                        op += "}";
                        while(rs.next())
                        {
                            op += ", {";
                            op += "\"mobid\" : \""+rs.getString("mobid")+"\", ";
                            op += "\"mob\" : \""+rs.getString("mob")+"\" ";
                            op += "}";
                        }
                        op += "], ";
                    }
                    rs = s.executeQuery("select * from users_addr where userid = "+userid);
                    if(rs.next())
                    {
                        op += "\"address\" : [ { ";
                        op += "\"addrid\" : \"" + rs.getString("addrid") + "\", ";
                        op += "\"addr1\" : \"" + rs.getString("addr1") + "\", ";
                        op += "\"addr2\" : \"" + rs.getString("addr2") + "\", ";
                        op += "\"country\" : \"" + rs.getString("country") + "\", ";
                        op += "\"state\" : \"" + rs.getString("state") + "\", ";
                        op += "\"zip\" : \"" + rs.getString("zip") + "\" ";
                        op += "}";
                        while(rs.next())
                        {
                            op += ", {";
                            op += "\"addrid\" : \"" + rs.getString("addrid") + "\", ";
                            op += "\"addr1\" : \"" + rs.getString("addr1") + "\", ";
                            op += "\"addr2\" : \"" + rs.getString("addr2") + "\", ";
                            op += "\"country\" : \"" + rs.getString("country") + "\", ";
                            op += "\"state\" : \"" + rs.getString("state") + "\", ";
                            op += "\"zip\" : \"" + rs.getString("zip") + "\" ";
                            op += "}";
                        }
                        op += "]";
                    }
                    op += "} }";
                }
                
            }
            else
            {
                System.out.print("token = "+token);
                op = "Invalid_user";
            }
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            rs.close();
            s.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetPersonal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetPersonal.class.getName()).log(Level.SEVERE, null, ex);
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
