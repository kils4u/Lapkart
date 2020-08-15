
import com.paytm.pg.merchant.CheckSumServiceHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/PaymentRedirect"})
public class PaymentRedirect extends HttpServlet {

    
    public class PaymentConstants {
        public final static String MID="PjIsqQ54694102706408";
        public final static String MERCHANT_KEY="oSzuOn&cXIYWi#ZM";
        public final static String INDUSTRY_TYPE_ID="Retail";
        public final static String CHANNEL_ID="WEB";
        public final static String WEBSITE="WEBSTAGING";
        public final static String PAYTM_URL="https://securegw-stage.paytm.in/theia/processTransaction";
        public final static String callback_url = "https://localhost:8443/Lapkart/PaymentRedirect";
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String orderid = request.getParameter("ORDERID");
        String userid = request.getParameter("CUST_ID");
        String paytm_txnid = request.getParameter("TXNID");
        String bank_txnid = request.getParameter("BANKTXNID");
        String amount = request.getParameter("TXNAMOUNT");
        String status = request.getParameter("STATUS");
        String date = request.getParameter("TXNDATE");
        String payment_mode = request.getParameter("PAYMENTMODE");
        String bank_name = request.getParameter("BANKNAME");
        String respmsg = request.getParameter("RESPMSG");
        
        String paytmChecksum = "";
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	Date cdate;
        
        String heading = "";
        String message = "";
        String img = "";
        
        String op = "fail";
        
        int total_amnt = 10;
        
        Connection c;
        Statement s;
        ResultSet rs;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
                
            TreeMap<String, String> paytmParams = new TreeMap<String, String>();

            for (Entry<String, String[]> requestParamsEntry : request.getParameterMap().entrySet()) {
                    if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
                        paytmChecksum = requestParamsEntry.getValue()[0];
                    } else {
                        paytmParams.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
                    }
                }
            boolean isValidChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(PaymentConstants.MERCHANT_KEY, paytmParams, paytmChecksum);

            if(isValidChecksum)
            {
                if(status.compareTo("TXN_SUCCESS")==0)
                {
                    img = "Successfull.svg";
                    heading = "Payment Successful";
                    message = "Thank You for shopping with us...!";
                    s.executeUpdate("update users_order set status = 'Successfull' where orderid = "+orderid);
                }
                else if(status.compareTo("TXN_FAILURE")==0)
                {
                    img = "Fail.svg";
                    heading = "Transction Failed";
                    message = "Sorry..! for the inconvenient...";
                    cdate = new Date();
                    date = formatter.format(cdate);
                    s.executeUpdate("update users_order set status = 'Failed' where orderid = "+orderid);
                }
                else if(status.compareTo("PENDING")==0)
                {
                    img = "Pending.svg";
                    heading = "Transction Pending";
                    message = "Status will be updated within 48 hours...";
                    cdate = new Date();
                    date = formatter.format(cdate);
                }
                s.executeUpdate("insert into payment values("+orderid+",'"+paytm_txnid+"','"+bank_txnid+"',"+amount+",'"+status+"','"+date+"','"+payment_mode+"','"+bank_name+"','"+respmsg+"')");
            }
            else
            {
                img = "Pending.svg";
                heading = "Transction Pending";
                message = "Status will be updated within 48 hours...";
                cdate = new Date();
                date = formatter.format(cdate);
                s.executeUpdate("insert into payment values("+orderid+",'"+paytm_txnid+"','"+bank_txnid+"',"+amount+",'"+status+"','"+date+"','"+payment_mode+"','"+bank_name+"','"+respmsg+"')");
            }
            s.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewAddress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PaymentRedirect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            op = "<!doctype html>";
            op += "<html lang=\"en\">";
            op += "<head>";
            op += "<meta charset=\"utf-8\">";
            op += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";
            op += "<meta name=\"description\" content=\"Online Laptop Purchese\">";
            op += "<meta name=\"author\" content=\"Kailas Deshmukh, Bhushan Pagar, and Aniket Deshmukh\">";
            op += "<title> Payment Status </title>";
            op += "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\">";
            op += "<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" crossorigin=\"anonymous\"></script>";
            op += "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" crossorigin=\"anonymous\"></script>";
            op += "<script src=\"js/bootstrap.min.js\"></script>";
            op += "</head>";
            op += "<body>";
            op += "<div class=\"container\" align=\"center\">";
            op += "<div class=\"card col-md-6 m-5 p-5\" style=\"width: 25rem;\">";
            op += "<img src=\"Images/payment/"+img+"\" class=\"card-img-top mb-4\" alt=\"Card image cap\">";
            op += "<div class=\"card-body\">";
            op += "<h3 class=\"card-title\">"+heading+"</h3>";
            op += "<hr/>";
            op += "<p class=\"card-text mb-4\">"+message+"</p>";
            op += "<a href=\"Home.html\" class=\"btn btn-primary\">Continue Shooping</a>";
            op += "</div>";
            op += "</div>";
            op += "</div>";
            op += "</body>";
            op += "</html>";
            
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
