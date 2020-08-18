

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


@WebServlet(urlPatterns = {"/Cart"})
public class Cart extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            String token = request.getParameter("token");
            String userid;
            
            String op = "noData";
            
            Connection c,d;
            Statement stmt1,stmt2;
            ResultSet rs,rs2;
            
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
                stmt1 = c.createStatement();
                
                d = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
                stmt2 = d.createStatement();
                
                rs = stmt1.executeQuery("select userid from users_token where token ="+token);
                if(rs.next())
                {
                    userid = rs.getString("userid");
                    rs = stmt1.executeQuery("select * from users_cart where userid = " + userid + " ORDER BY cartid ASC");
                    if(rs.next())
                    {
                        op = "{\"items\" : [ { ";
                        op += " \"cartid\" : \""+rs.getString("cartid")+"\", ";
                        op += " \"userid\" : \""+rs.getString("userid")+"\", ";
                        op += " \"pid\" : \""+rs.getString("pid")+"\", ";
                        op += " \"quantity\" : \""+rs.getString("quantity")+"\" ";
                        
                        rs2 = stmt2.executeQuery("select pid, pbrand, pmodel, pimg, pprice from product where pid = "+rs.getString("pid"));
                        if(rs2.next())
                        {
                            op += ", \"name\" : \"" + rs2.getString("pbrand") + " " + rs2.getString("pmodel") + "\", ";
                            op += " \"img\" : \"" +rs2.getString("pimg")+"\", ";
                            op += " \"price\" : \""+rs2.getString("pprice")+"\"";
                        }
                        op += "}";
                        rs2.close();
                    }
                    
                    while(rs.next())
                    {
                        op += ",{ ";
                        op += " \"cartid\" : \""+rs.getString("cartid")+"\", ";
                        op += " \"userid\" : \""+rs.getString("userid")+"\", ";
                        op += " \"pid\" : \""+rs.getString("pid")+"\", ";
                        op += " \"quantity\" : \""+rs.getString("quantity")+"\" ";
                        
                        rs2 = stmt2.executeQuery("select pid, pbrand, pmodel, pimg, pprice from product where pid = "+rs.getString("pid"));
                        if(rs2.next())
                        {
                            op += ", \"name\" : \"" + rs2.getString("pbrand") + " " + rs2.getString("pmodel") + "\", ";
                            op += " \"img\" : \""+rs2.getString("pimg")+"\", ";
                            op += " \"price\" : \""+rs2.getString("pprice")+"\"";
                        }
                        op += "}";
                        rs2.close();
                    }
                    if(op.compareTo("noData") != 0)
                        op += "]}";
                    rs.close();
                }
                
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                out.print(op);
                stmt1.close();
                stmt2.close();
                c.close();
                d.close();
                stmt1 = null;
                stmt2 = null;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
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
