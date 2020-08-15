
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


@WebServlet(urlPatterns = {"/EditCart"})
public class EditCart extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String cartid = request.getParameter("cartid");
        String operation = request.getParameter("operation");
        String value = request.getParameter("value");
        
        String op = "";
        
        Connection c;
        ResultSet rs;
        Statement s;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            rs = s.executeQuery("select userid from users_token where token = "+token);
            if(rs.next())
            {
                if(userid.compareTo(rs.getString("userid"))==0)
                {
                    if(operation.compareTo("add")==0)
                    {
                        rs = s.executeQuery("select * from users_cart where cartid = "+cartid);
                        if(rs.next())
                        {
                            if(userid.compareTo(rs.getString("userid"))==0)
                            {
                                int q = rs.getInt("quantity");
                                q++;
                                s.executeUpdate("update users_cart set quantity ="+q+" where cartid = "+cartid);
                            }
                        }
                    }
                    else if(operation.compareTo("sub")==0)
                    {
                        rs = s.executeQuery("select * from users_cart where cartid = "+cartid);
                        if(rs.next())
                        {
                            if(userid.compareTo(rs.getString("userid"))==0)
                            {
                                int q = rs.getInt("quantity");
                                if(q > 1)
                                {
                                    q--;
                                    s.executeUpdate("update users_cart set quantity ="+q+" where cartid = "+cartid);
                                }
                            }
                        }
                    }
                    else if(operation.compareTo("change")==0)
                    {
                        rs = s.executeQuery("select * from users_cart where cartid = "+cartid);
                        if(rs.next())
                        {
                            if(userid.compareTo(rs.getString("userid"))==0)
                            {
                                if(Integer.valueOf(value) > 0)
                                {
                                    int q = Integer.valueOf(value);
                                    s.executeUpdate("update users_cart set quantity ="+q+" where cartid = "+cartid);
                                }
                            }
                        }
                    }
                }
            }
            rs = s.executeQuery("select quantity from users_cart where cartid = "+cartid);
            if(rs.next())
                op = rs.getString("quantity");
            System.out.print("\nquantity = "+rs.getString("quantity"));
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            s.close();
            rs.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditCart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EditCart.class.getName()).log(Level.SEVERE, null, ex);
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
