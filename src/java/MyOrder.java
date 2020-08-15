
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


@WebServlet(urlPatterns = {"/MyOrder"})
public class MyOrder extends HttpServlet {
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out;
        
        System.out.print("got hit at my orders");
        
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String op="nodata";
        
        Connection c,d,e;
        Statement s,t,u;
        ResultSet rs,r,p;
        
        try {
            out = response.getWriter();
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            d = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            e = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            t = d.createStatement();
            u = e.createStatement();
            
            rs = s.executeQuery("select userid from users_token where token = "+token +" AND userid = "+userid);
            if(rs.next())
            {
                op = "{ \"orders\" : [";
                rs = s.executeQuery("select orderid, status, amount, date from users_order where userid = "+userid);
                if(rs.next())
                {
                    String orderid = rs.getString("orderid");
                    op += "{ \"orderid\" : \"" + rs.getString("orderid") + "\",";
                    op += "\"status\" : \"" + rs.getString("status") + "\",";
                    op += "\"date\" : \"" + rs.getString("date") + "\",";
                    op += "\"amount\" : \"" + rs.getString("amount") + "\",";
                    op += "\"items\" : [";
                    r = t.executeQuery("select quantity,p_id,price from product_order where orderid = "+orderid);
                    if(r.next())
                    {
                        String pid = r.getString("p_id");
                        p = u.executeQuery("select p_brand, p_model, p_img from product where p_id = "+pid);
                        if(p.next())
                        {
                            op += "{ \"p_id\" : \""+pid+"\", ";
                            op += "\"quantity\" : \""+r.getString("quantity")+"\", ";
                            op += "\"price\" : \""+r.getString("price")+"\", ";
                            op += "\"name\" : \""+p.getString("p_brand") + " " + p.getString("p_model") +"\", ";
                            op += "\"img\" : \""+p.getString("p_img")+"\" }";
                        }
                    }
                    while(r.next())
                    {
                        String pid = r.getString("p_id");
                        p = u.executeQuery("select p_brand, p_model, p_img from product where p_id = "+pid);
                        if(p.next())
                        {
                            op += ", { \"p_id\" : \""+pid+"\", ";
                            op += "\"quantity\" : \""+r.getString("quantity")+"\", ";
                            op += "\"price\" : \""+r.getString("price")+"\", ";
                            op += "\"name\" : \""+p.getString("p_brand") + " " + p.getString("p_model") +"\", ";
                            op += "\"img\" : \""+p.getString("p_img")+"\" }";
                        }
                    }
                    op += " ] }";
                }
                while(rs.next())
                {
                    String orderid = rs.getString("orderid");
                    op += ", { \"orderid\" : \"" + rs.getString("orderid") + "\",";
                    op += "\"status\" : \"" + rs.getString("status") + "\",";
                    op += "\"date\" : \"" + rs.getString("date") + "\",";
                    op += "\"amount\" : \"" + rs.getString("amount") + "\",";
                    op += "\"items\" : [";
                    r = t.executeQuery("select quantity,p_id,price from product_order where orderid = "+orderid);
                    if(r.next())
                    {
                        String pid = r.getString("p_id");
                        p = u.executeQuery("select p_brand, p_model, p_img from product where p_id = "+pid);
                        if(p.next())
                        {
                            op += "{ \"p_id\" : \""+pid+"\", ";
                            op += "\"quantity\" : \""+r.getString("quantity")+"\", ";
                            op += "\"price\" : \""+r.getString("price")+"\", ";
                            op += "\"name\" : \""+p.getString("p_brand") + " " + p.getString("p_model") +"\", ";
                            op += "\"img\" : \""+p.getString("p_img")+"\" }";
                        }
                    }
                    while(r.next())
                    {
                        String pid = r.getString("p_id");
                        p = u.executeQuery("select p_brand, p_model, p_img from product where p_id = "+pid);
                        if(p.next())
                        {
                            op += ", { \"p_id\" : \""+pid+"\", ";
                            op += "\"quantity\" : \""+r.getString("quantity")+"\", ";
                            op += "\"price\" : \""+r.getString("price")+"\", ";
                            op += "\"name\" : \""+p.getString("p_brand") + " " + p.getString("p_model") +"\", ";
                            op += "\"img\" : \""+p.getString("p_img")+"\" }";
                        }
                    }
                    op += " ] } ";
                    response.setContentType("application/json;charset=UTF-8");
                }
                
                op += " ] }";
                out.print(op);
            }
            s.close();
            t.close();
            u.close();
            c.close();
            d.close();
            e.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MyOrder.class.getName()).log(Level.SEVERE, null, ex);
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
