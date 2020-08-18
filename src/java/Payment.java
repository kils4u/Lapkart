
import com.paytm.pg.merchant.CheckSumServiceHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Payment"})
public class Payment extends HttpServlet {
    
    public class PaymentConstants {
        public final static String MID="PjIsqQ54694102706408";
        public final static String MERCHANT_KEY="oSzuOn&cXIYWi#ZM";
        public final static String INDUSTRY_TYPE_ID="Retail";
        public final static String CHANNEL_ID="WEB";
        public final static String WEBSITE="WEBSTAGING";
        public final static String PAYTM_URL="https://securegw-stage.paytm.in/order/process";
        public final static String callback_url = "https://localhost:8443/Lapkart/PaymentRedirect";
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String orderid = request.getParameter("orderid");
        String token = request.getParameter("token");
        String userid = request.getParameter("userid");
        String mob = null;
        String email = null;
        String mobid;
        
        String op = "fail";
        
        int total_amnt = 10;
        
        Connection c,d;
        Statement s,t;
        ResultSet rs,r;
        
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
                    rs = s.executeQuery("select * from users_order where userid = "+userid+" AND orderid = "+orderid);
                    if(rs.next())
                    {
                        mobid = rs.getString("mobid");
                        r = t.executeQuery("select mob from users_mob where mobid = "+ mobid);
                        if(r.next())
                        {       
                            mob = r.getString("mob");
                        }
                        else
                        {
                            op = "mob Fail";
                        }
                        r = t.executeQuery("select email from users_login where userid = "+userid);
                        if(r.next())
                        {
                            email = r.getString("email");
                        }
                        else
                        {
                            op = "email fail";
                        }
                    }
                }
                else
                {
                    op = "user fail";
                }
            }
            else
            {
                op = "Token fail";
            }
            s.close();
            t.close();
            c.close();
            d.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            if(mob != null && email != null)
            {
                
                TreeMap<String, String> paytmParams = new TreeMap<String, String>();
                paytmParams.put("MID",PaymentConstants.MID);
                paytmParams.put("ORDER_ID",orderid);
                paytmParams.put("CHANNEL_ID",PaymentConstants.CHANNEL_ID);
                paytmParams.put("CUST_ID",userid);
                paytmParams.put("MOBILE_NO",mob);
                paytmParams.put("EMAIL",email);
                paytmParams.put("TXN_AMOUNT",String.valueOf(total_amnt));
                paytmParams.put("WEBSITE",PaymentConstants.WEBSITE);
                paytmParams.put("INDUSTRY_TYPE_ID",PaymentConstants.INDUSTRY_TYPE_ID);
                paytmParams.put("CALLBACK_URL", PaymentConstants.callback_url);
                
                String paytmChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(PaymentConstants.MERCHANT_KEY, paytmParams);
                
                op = "<html>";
                op += "<head>";
                op += "<title>Merchant Check Out Page</title>";
                op += "</head>";
                op += "<body>";
                op += "<center><h1>Please do not refresh this page...</h1></center>";
                op += "<form method=\"post\" action=\""+ PaymentConstants.PAYTM_URL+"?ORDER_ID="+orderid+"\" name=\"f1\">";
                op += "<table border=\"1\">";
                op += "<tbody>";
                op += "<input type=\"hidden\" name=\"MID\" value=\"" +PaymentConstants.MID+"\">";
                op += "<input type=\"hidden\" name=\"WEBSITE\" value=\""+PaymentConstants.WEBSITE+"\">";
                op += "<input type=\"hidden\" name=\"ORDER_ID\" value=\""+orderid+"\">";
                op += "<input type=\"hidden\" name=\"CUST_ID\" value=\""+userid+"\">";
                op += "<input type=\"hidden\" name=\"MOBILE_NO\" value=\""+mob+"\">";
                op += "<input type=\"hidden\" name=\"EMAIL\" value=\""+email+"\">";
                op += "<input type=\"hidden\" name=\"INDUSTRY_TYPE_ID\" value=\""+PaymentConstants.INDUSTRY_TYPE_ID+"\">";
                op += "<input type=\"hidden\" name=\"CHANNEL_ID\" value=\""+PaymentConstants.CHANNEL_ID+"\">";
                op += "<input type=\"hidden\" name=\"TXN_AMOUNT\" value=\""+total_amnt+"\">";
                op += "<input type=\"hidden\" name=\"CALLBACK_URL\" value=\""+PaymentConstants.callback_url+"\">";
                op += "<input type=\"hidden\" name=\"CHECKSUMHASH\" value=\""+paytmChecksum+"\">";
                op += "</tbody>";
                op += "</table>";
                op += "<script type=\"text/javascript\">";
                op += "document.f1.submit();";
                op += "</script>";
                op += "</form>";
                op += "</body>";
                op += "</html>";
                
            }
            else
            {
                op += " some thing Gone Wrong";
            }
            
            out.print(op);
        } catch (Exception ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
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
