

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


@WebServlet(urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection c = null;
        Statement stmt = null;
        String output = null;
        ResultSet rs = null;
        
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String gender = request.getParameter("fname");
        String age = request.getParameter("age");
        String mob = request.getParameter("mob");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");
        String addr1 = request.getParameter("addr1");
        String addr2 = request.getParameter("addr2");
        String country = request.getParameter("country");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        int userid = 100;
        int mobid = 100;
        int addrid = 100;
        
        if(gender.compareTo("Male") == 0)
            gender = "M";
        else
            gender = "F";
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT MAX(userid) FROM users");
            if(rs.next())
            {
                userid = Integer.valueOf(rs.getString("max"));
                if(userid < 100)
                    userid = 100;
                else
                    userid++;
            }
            rs = stmt.executeQuery("SELECT MAX(mobid) FROM users_mob");
            if(rs.next())
            {
                mobid = Integer.valueOf(rs.getString("max"));
                if(mobid < 100)
                    mobid = 100;
                else
                    mobid++;
            }
            rs = stmt.executeQuery("SELECT MAX(addrid) FROM users_addr");
            if(rs.next())
            {
                addrid = Integer.valueOf(rs.getString("max"));
                if(addrid < 100)
                    addrid = 100;
                else
                    addrid++;
            }
            stmt.executeUpdate("insert into users values ("+userid+ ",'" +fname + "','" + lname + "',"+age+",'"+gender+"')" );
            stmt.executeUpdate("insert into users_login values("+userid+",'"+email+"','"+pass+"')" );
            stmt.executeUpdate("insert into users_addr values("+addrid+","+userid+",'"+addr1+"','"+addr2+"','"+country+"','"+state+"','"+zip+"')" );
            stmt.executeUpdate("insert into users_Mob values("+mobid+","+userid+","+mob+")" );
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("successful");
            stmt.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
    }

}
