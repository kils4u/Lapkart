

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/Search"})
public class Search extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String search = request.getParameter("search").toLowerCase().trim();
        
        String b_name = null;
        String processor = null;
        String m_name = null;
        String processor_gen = null;
        String p_id = null;
        List pids = new ArrayList();
        
        String op = null;
        
        Connection c;
        Statement s;
        ResultSet rs;
        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lapkart","postgres", "kils4u");
            s = c.createStatement();
            
            rs = s.executeQuery("select b_name from product_spec");
            while(rs.next())
            {
                String brand = rs.getString("b_name").toLowerCase().trim();
                int indx = search.indexOf(brand);
                if(indx != -1)
                {
                    b_name = brand;
                    search = search.replaceAll(brand,"").trim();
                    break;
                }
            }
            if(search != null){
                if(b_name != null)
                {
                    rs = s.executeQuery("select processor from product_spec where LOWER(b_name) = '"+b_name+"'");
                }
                else
                {
                    rs = s.executeQuery("select processor from product_spec");
                }
                while(rs.next())
                {
                    String proc = rs.getString("processor").toLowerCase().trim();
                    int indx = search.indexOf(proc);
                    if(indx != -1)
                    {
                        processor = proc;
                        search = search.replaceAll(proc,"").trim();
                        break;
                    }
                }
            }
            if(search != null)
            {
                if(b_name != null && processor != null)
                {
                    rs = s.executeQuery("select processor_gen from product_spec where LOWER(b_name) = '"+b_name + "' AND LOWER(processor) = '"+processor+"'");
                }
                else if(b_name != null)
                {
                    rs = s.executeQuery("select processor_gen from product_spec where LOWER(b_name) = '"+b_name+"'");
                }
                else if(processor != null)
                {
                    rs = s.executeQuery("select processor_gen from product_spec where LOWER(processor) = '"+processor+"'");
                }
                else
                {
                    rs = s.executeQuery("select processor_gen from product_spec");
                }
                while(rs.next())
                {
                    String proc_gen = rs.getString("processor_gen").toLowerCase();
                    int indx = search.indexOf(proc_gen);
                    if(indx != -1)
                    {
                        processor = proc_gen;
                        search = search.replaceAll(proc_gen,"").trim();
                        break;
                    }
                }
            }
            if(search != null)
            {
               if(b_name != null && processor != null && processor_gen != null )
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec where LOWER(b_name) = '"+b_name+"' AND LOWER(processor) = '"+processor +"' AND LOWER(processor_gen) = '"+ processor_gen+"'");
               }
               else if(b_name != null && processor != null)
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec where LOWER(b_name) = '"+b_name+ "' AND LOWER(processor) = '"+processor+"'");
               }
               else if(processor != null && processor_gen != null)
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec where LOWER(processor) = '"+processor+ "' AND LOWER(processor_gen) = '"+ processor_gen+"'");
               }
               else if(b_name != null)
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec where LOWER(b_name) = '"+b_name+"'");
               }
               else if(processor != null)
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec where LOWER(processor) = '"+processor+"'");                   
               }
               else
               {
                   rs = s.executeQuery("select m_name,p_id from product_spec");
               }
               while(rs.next())
               {
                   String model = rs.getString("m_name").toLowerCase();
                   if(model.length() == search.length() )
                   {
                       if(search.compareTo(model)==0)
                       {
                           p_id = rs.getString("p_id");
                           break;
                       }
                   }
                   else if(model.length() > search.length())
                   {
                       if(model.indexOf(search) != -1)
                       {
                           pids.add(new String(rs.getString("p_id")));
                           continue;
                       }
                   }
                   else if(model.length() < search.length())
                   {
                       if(search.indexOf(model) != -1)
                       {
                           pids.add(new String(rs.getString("p_id")));
                           continue;
                       }
                   }
                   else
                   {
                        String [] search_items = search.split(" ");
                        int len = search_items.length;
                        for(int i=0;i<len;i++)
                        {
                            if(model.indexOf(search_items[i]) != -1)
                            {
                                pids.add(new String(rs.getString("p_id")));
                                break;
                            }
                        }
                   }
                   
               }
            }
            
            if(p_id != null)
            {
                op = "{ \"Items\" : [ ";
                rs = s.executeQuery("select * from product where p_id = "+p_id);
                if(rs.next())
                {
                    op += "{ \"p_id\" : \"" + rs.getString("p_id") + "\",";
                    op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                    op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                    op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                    op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                    op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                    op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                    op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                    op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                }
            }
            
            if(!pids.isEmpty())
            {
                int len = pids.size();
                if(op != null)
                    op += ", ";
                else
                    op = "{ \"Items\" : [ ";
                for(int i=0;i<len;i++)
                {
                    rs = s.executeQuery("select * from product where p_id = "+pids.get(i));
                    if(rs.next())
                    {
                        op += "{ \"p_id\" : \"" + rs.getString("p_id") + "\",";
                        op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                        op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                        op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                        op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                        op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                        op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                        op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                        op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                    }
                    if(i!=len-1)
                        op += ", ";
                }
            }
            else if(processor != null)
            {
                if(op != null)
                    op += ", ";
                else
                    op = "{ \"Items\" : [ ";
                rs = s.executeQuery("select * from product where p_id IN (select p_id from product_spec where LOWER(processor) = '"+ processor +"' )");
                if(rs.next())
                {
                    op += "{ \"p_id\" : \"" + rs.getString("p_id") + "\",";
                    op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                    op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                    op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                    op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                    op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                    op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                    op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                    op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                }
                while(rs.next())
                {
                    op += ", { \"p_id\" : \"" + rs.getString("p_id") + "\",";
                    op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                    op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                    op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                    op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                    op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                    op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                    op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                    op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                }
            }
            else if(b_name != null)
            {
                if(op != null)
                    op += ", ";
                else
                    op = "{ \"Items\" : [ ";
                rs = s.executeQuery("select * from product where LOWER(p_brand) = '"+b_name+"'");
                if(rs.next())
                {
                    op += "{ \"p_id\" : \"" + rs.getString("p_id") + "\",";
                    op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                    op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                    op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                    op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                    op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                    op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                    op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                    op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                }
                while(rs.next())
                {
                    op += ", { \"p_id\" : \"" + rs.getString("p_id") + "\",";
                    op += "\"p_name\" : \"" + rs.getString("p_brand") + "\",";
                    op += "\"p_model\" : \"" + rs.getString("p_model") + "\",";
                    op += "\"p_img_link\" : \"" + rs.getString("p_img") + "\",";
                    op += "\"p_price\" : \"" + rs.getString("p_price") + "\",";
                    op += "\"p_processor\" : \"" + rs.getString("p_processor") + "\",";
                    op += "\"p_hdd_size\" : \"" + rs.getString("p_hdd") + "\",";
                    op += "\"p_ram_size\" : \"" + rs.getString("p_ram") + "\",";
                    op += "\"p_graphics_size\" : \"" + rs.getString("p_graphics") + "\"}";
                }
            }
            else
            {
                op = "No_Result";
            }
            if(op != "No_Result")
                    op += "] }";
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(op);
            
            s.close();
            c.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
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
