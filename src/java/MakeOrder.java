

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/MakeOrder"})
public class MakeOrder extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String mobid = request.getParameter("mobid");
        String addrid = request.getParameter("addrid");
        
        String op="fail";
        
        int orderid;
        
        float Total_amount = 0;
        Connection c,d;
        Statement s,t;
        ResultSet rs,r;
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	Date date; 
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            d = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            t = d.createStatement();
            if(token != null && userid != null)
            {
                rs = s.executeQuery("select userid from users_token where token = "+token+ " AND userid = "+userid);
                if(rs.next())
                {
                    rs = s.executeQuery("select MAX(orderid) from users_order");
                    if(rs.next())
                    {
                        orderid = rs.getInt("max");
                        if(orderid < 2000)
                            orderid = 2000;
                        else
                            orderid++;
                        date = new Date();
                        s.executeUpdate("insert into users_order values ("+orderid+", "+userid+","+addrid+","+mobid+",'pending','"+formatter.format(date)+"')");
                        
                        rs = s.executeQuery("select * from users_cart where userid = "+userid);
                        while(rs.next())
                        {
                            r = t.executeQuery("select pprice from product where pid = "+rs.getString("pid"));
                            if(r.next())
                            {
                                int q = Integer.valueOf(rs.getString("quantity"));
                                float p = Float.valueOf(r.getString("pprice").replace("₹", "").replace(",", ""));
                                Total_amount += p * q;
                                t.executeUpdate("insert into product_order values ("+orderid+","+rs.getString("pid")+","+q+","+p+")");
                            }
                            r.close();
                        }
                        s.executeUpdate("update users_order set amount = "+Total_amount+" where orderid = "+orderid);
                        s.executeUpdate("delete from users_cart where userid="+userid);
                        op = String.valueOf(orderid);
                    }
                }
                else
                {
                    op = "fail";
                }
                rs.close();
            }
            else
            {
                op = "fail";
            }
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            s.close();
            t.close();
            c.close();
            d.close();
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
