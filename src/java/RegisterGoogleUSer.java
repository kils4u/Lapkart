/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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


@WebServlet(urlPatterns = {"/RegisterGoogleUSer"})
public class RegisterGoogleUSer extends HttpServlet {
    
    Random rand = new Random();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String userid;
        
        int token;
        
        Connection c;
        Statement s;
        ResultSet rs;
        
        String op;
        op = "{\"tokens\" : [{ \"status\" : \"fail\",";
        op += "\"token\" : \"null\",";
        op += "\"userid\" : \"null\"}]}";
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            
            rs = s.executeQuery("select userid from users_login where email = '"+email+"'");
            if(rs.next())
            {
                userid = rs.getString("userid");
                while(true)
                {
                    token = rand.nextInt();
                    if(token < 0)
                        token = -(token);
                    rs = s.executeQuery("select * from users_token where token = '" + token + "'");
                    if(!rs.next())
                        break;
                }
                op = "{\"tokens\" : [{ \"status\" : \"successfull\",";
                op += "\"token\" : \"" + token + "\",";
                op += "\"userid\" : \"" + userid + "\"}]}";
                s.executeUpdate("insert into users_token values("+token+","+userid+",'"+ email +"')");
            }
            else
            {
                String fname,lname;
                int uid = 1;
                String [] full_name = name.split(" ");
                if(full_name.length >= 2)
                {
                    fname = full_name[0];
                    lname = full_name[1];
                    
                    rs = s.executeQuery("select MAX(userid) from users");
                    if(rs.next() && rs.getString("max")!= null)
                        uid = rs.getInt("max");
                    
                    if(uid < 100)
                        uid = 100;
                    else
                        uid += 1;
                    
                    s.executeUpdate("insert into users (userid,fname,lname) values ("+uid+",'"+fname+"','"+lname+"')");
                    s.executeUpdate("insert into users_login (userid,email) values ("+uid+",'"+email+"')");
                    while(true)
                    {
                        token = rand.nextInt();
                        if(token < 0)
                            token = -(token);
                        rs = s.executeQuery("select * from users_token where token = '" + token + "'");
                        if(!rs.next())
                            break;
                    }
                    op = "{\"tokens\" : [{ \"status\" : \"successfull\",";
                    op += "\"token\" : \"" + token + "\",";
                    op += "\"userid\" : \"" + uid + "\"}]}";
                    s.executeUpdate("insert into users_token values("+token+","+uid+",'"+ email +"')");
                }
            }
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            s.close();
            c.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegisterGoogleUSer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterGoogleUSer.class.getName()).log(Level.SEVERE, null, ex);
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
