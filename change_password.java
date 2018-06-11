/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;
import com.kundan.app.db.connectionCredentials;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kundan
 */
@WebServlet(urlPatterns = {"/change_password"})
public class change_password extends HttpServlet {

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
            out.println("<title>Servlet change_password</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet change_password at " + request.getContextPath() + "</h1>");
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

        Connection con = null;
        Statement stmt = null;
        Statement stmt_user = null;
        ResultSet rs_user = null;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);

            String password = request.getParameter("pass");
            String userSerialNo = request.getParameter("userSerialNo");
            System.out.println(""+password);

            stmt = con.createStatement();
            String userName = "", user_email = "", pwd = "";
            HistoryTableClass history = new HistoryTableClass();

            List<String> list = new ArrayList<String>();
            String json = "";

            int result = stmt.executeUpdate("update `1_user_detail` set password='" + password + "' where user_serial_no='" + userSerialNo + "'");
            if (result != 0) {

                stmt_user = con.createStatement();
                rs_user = stmt_user.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + userSerialNo + "'");
                if (rs_user.next()) {
                    userName = rs_user.getString(3);
                    user_email = rs_user.getString(8);
                    pwd = rs_user.getString(11);
                }
                list.add("Success");
                history.historyData(451, userName, "Admin", "");
                
                
                //Mail body generation
                String line1 = "Hi " + userName + ",";
                String line2 = "Your password has been changed successfully. Please find your login details below:";
                String line3 = "Username : " + user_email;
                String line4 = "Password : " + pwd;
                String msg = line1 + "\n\n" + line2 + "\n\n" + line3 + "\n" + line4;
                
                //To send new password after password reset done by the user
                SendMail sendMail = new SendMail();
                sendMail.sendMessageForPwdSettings(user_email, "Password Changed" , msg);
                //SendMail.onlyOneReciptent(127, user_email, userName, user_email, password, "", "", "");

            }
            json = new Gson().toJson(list);
            response.setContentType("application/json");
            response.getWriter().write(json);

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
                if (stmt_user != null) {
                    stmt_user.close();
                }
                if (rs_user != null) {
                    rs_user.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
        PrintWriter out = response.getWriter();
        HttpSession ses = request.getSession();
        String password = request.getParameter("pass");
        String Designation = (String) ses.getAttribute("Designation");

        Connection con = null;
        Statement stmt = null;
        Statement stmt_user = null;
        ResultSet rs_user = null;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            String userName = "", user_email = "";
            HistoryTableClass history = new HistoryTableClass();
            if (Designation.equals("Manager") || Designation.equals("Engineer")) {

                int result = stmt.executeUpdate("update `1_user_detail` set password='" + password + "' where Designation='" + Designation + "' and user_serial_no='" + ses.getAttribute("user_serial_no") + "' and email_id='" + ses.getAttribute("email_id") + "'");
                if (result != 0) {
                    stmt_user = con.createStatement();
                    rs_user = stmt_user.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + ses.getAttribute("user_serial_no") + "'");
                    if (rs_user.next()) {
                        userName = rs_user.getString(3);
                    }
                    history.historyData(451, userName, "Employee", "");
                    response.setContentType("text/html");
                    out.print("<script type=\"text/javascript\">");
                    out.print("alert('Password Succesfully Changed!!');");

                    if (Designation.equals("Manager")) {
                        out.print("location='menu_manager.jsp';");
                        out.print("</script>");
                    } else if (Designation.equals("Engineer")) {
                        out.print("location='menu_user.jsp';");
                        out.print("</script>");
                    }
                }
            } else if (Designation.equals("Admin")) {
               

                    int result = stmt.executeUpdate("update `1_user_detail` set password='" + password + "' where Designation='Admin'");
                    if (result != 0) {
                        history.historyData(451, "Admin", "Admin", "");
                        response.setContentType("text/html");
                        out.print("<script type=\"text/javascript\">");
                        out.print("alert('Password Succesfully Changed!!');");
                        out.print("location='menu_admin.jsp';");
                        out.print("</script>");
                    }
               

            }

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
                if (stmt_user != null) {
                    stmt_user.close();
                }
                if (rs_user != null) {
                    rs_user.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
