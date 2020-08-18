/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kils
 */
@WebServlet(urlPatterns = {"/top_brands"})
public class top_brands extends HttpServlet {
    
    String query = "select * from brand where bid In(select brand_id from top_brands)";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        String output = null;
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres","kils4u");
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next())
            {
                output = "{ \"top_brands\" : [ ";
                output += "{ \"bid\" : \"" + rs.getString("bid") + "\",";
                output += "\"bname\" : \"" + rs.getString("bname") + "\",";
                output += "\"blogo\" : \"" + rs.getString("blogo") + "\",";
                output += "\"brating\" : \"" + rs.getString("brating") + "\"}";
            }
            while(rs.next())
            {
                output += ",{ \"bid\" : \"" + rs.getString("bid") + "\",";
                output += "\"bname\" : \"" + rs.getString("bname") + "\",";
                output += "\"blogo\" : \"" + rs.getString("blogo") + "\",";
                output += "\"brating\" : \"" + rs.getString("brating") + "\"}";
            }
            if(output != null)
                output += "] }";
            
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println(output);
            
            c.close();
            stmt.close();
        }
        catch (Exception ex) {
            Logger.getLogger(deals_of_the_day.class.getName()).log(Level.SEVERE, null, ex);
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
