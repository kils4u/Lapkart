
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


@WebServlet(urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String uid = request.getParameter("userid");
        String token = request.getParameter("token");
        String pid = request.getParameter("p_id");
        int cartid = 1000;
        int quantity = 1;
        
        String op = null;
        
        Connection c;
        Statement stmt;
        ResultSet rs;
        
        try {
            Class.forName("org.postgresql.Driver");
            
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            
            rs = stmt.executeQuery("select userid from users_token where token = "+token+" AND userid = "+uid);
            if(rs.next())
            {
            
                rs = stmt.executeQuery("select * from users_cart where userid = "+uid+" AND p_id = "+pid);
                if(rs.next())
                {
                    cartid = rs.getInt("cartid");
                    quantity += rs.getInt("quantity");
                    int n = stmt.executeUpdate("update users_cart set quantity = "+quantity+" where cartid = "+cartid);
                    if(n>0)
                        op = "successful";
                    else
                        op = "fail";
                }
                else
                {
                    rs = stmt.executeQuery("select MAX(cartid) from users_cart");
                    if(rs.next())
                    {
                        cartid = rs.getInt("max");
                        if(cartid < 1000)
                        {
                            cartid = 1000;
                        }
                        else
                        {
                            cartid += 1;
                        }
                        int n = stmt.executeUpdate("insert into users_cart values("+cartid+","+uid+","+pid+","+quantity+")");
                        if(n>0)
                            op = "successful";
                        else
                            op = "fail";
                    }
                    else
                    {
                        int n = stmt.executeUpdate("insert into users_cart values("+cartid+","+uid+","+pid+","+quantity+")");
                        if(n>0)
                            op = "successful";
                        else
                            op = "fail";
                    }
                }
                rs.close();
            }
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            stmt.close();
            c.close();
            stmt = null;
            c = null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
