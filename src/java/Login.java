import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    Random rand = new Random();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String mail = request.getParameter("email");
        String pass = request.getParameter("pass");
        
        Connection c;
        Statement stmt;
        ResultSet rs;
        
        String op;
        
        int token;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            rs = stmt.executeQuery("select * from users_login where email = '"+ mail +"'");
            if(rs.next())
            {
                String uid = rs.getString("userid");
                String email = rs.getString("email");
                if(rs.getString("pass").compareTo(pass)==0)
                {
                    while(true){
                        token = rand.nextInt();
                        if(token < 0)
                            token = -(token);
                        ResultSet s = stmt.executeQuery("select * from users_token where token = '" + token + "'");
                        if(!s.next())
                            break;
                    }
                    op = "{\"tokens\" : [{ \"status\" : \"successfull\",";
                    op += "\"token\" : \"" + token + "\",";
                    op += "\"userid\" : \"" + uid + "\"}]}";
                    stmt.executeUpdate("insert into users_token values("+token+",'"+uid+"','"+ email +"')");
                }
                else
                {
                    op = "{\"tokens\" : [{ \"status\" : \"fail\",";
                    op += "\"token\" : \"null\",";
                    op += "\"userid\" : \"null\"}]}";
                }
            }
            else
            {
                op = "{\"tokens\" : [{ \"status\" : \"fail\",";
                op += "\"token\" : \"null\",";
                op += "\"userid\" : \"null\"}]}";
            }
            
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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
