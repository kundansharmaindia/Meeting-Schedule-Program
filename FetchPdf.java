/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kundan.app.db.connectionCredentials;

/**
 *
 * @author kundan
 */
public class FetchPdf extends HttpServlet {

    public static byte[] InputStreamToByteArray(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    public String InputStreamToBase64(InputStream input) throws IOException {
        byte[] buffer = new byte[99999];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return Base64.encode(output.toByteArray());
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FetchPdf</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FetchPdf at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
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
        /*
        
         * this code is for deleting the uploaded file from view_system_detail.jsp page and view_all_software.jsp page.
         * using ajax we are taking Sr_No for `21_document_upload` table in database
        
         */
        Connection con = null;
        Statement stmt = null;
        try {
            String File_SrNo = request.getParameter("file_id");
            List<String> list = new ArrayList<String>();
            String json = null;

            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            int result = stmt.executeUpdate("Delete from `21_document_upload` where Sr_No='" + File_SrNo + "'");
            if (result != 0) {
                list.add("Saved");
            } else {
                list.add("Error Saving File");
            }
            json = new Gson().toJson(list);
            response.setContentType("application/json");
            response.getWriter().write(json);
        } catch (SQLException ex) {
            Logger.getLogger(FetchPdf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FetchPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

        /*       This page is for getting the id for displaying pdf file from view_system_detail.jsp page and also from view_all_software.jsp page
         *       
         */
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;

        try {

            List<String> list = new ArrayList<String>();
            String json = null;
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            String getFile = "";
            String file_id = request.getParameter("file_id");
            rs = stmt.executeQuery("select document from `21_document_upload` where Sr_No='" + file_id + "'");
            if (rs.next()) {
                getFile = InputStreamToBase64(rs.getBinaryStream(1));
                list.add(getFile);
            }

            json = new Gson().toJson(list);
            response.setContentType("application/json");
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FetchPdf.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

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
