/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;
import com.kundan.app.db.connectionCredentials;

/**
 *
 * @author kundan
 */
@WebServlet(urlPatterns = {"/login"})
public class login extends HttpServlet {

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
            out.println("<title>Servlet login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet login at " + request.getContextPath() + "</h1>");
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
        Statement stmt_validate_email = null;
        ResultSet rs_validate_email = null;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            String email_id = request.getParameter("email_id");
            List<String> list = new ArrayList<String>();
            String json = null;
            stmt_validate_email = con.createStatement();
            rs_validate_email = stmt_validate_email.executeQuery("SELECT emp_name,email_id,password FROM `1_user_detail` where email_id='" + email_id + "'");
            if (rs_validate_email.next()) {

                list.add("Success");
                
                String empName = rs_validate_email.getString(1);
                String username = rs_validate_email.getString(2);
                String pwd = rs_validate_email.getString(3);
                
                //Mail body generation
                String line1 = "Hi " + empName + ",";
                String line2 = "Your password has been recovered successfully. Please find your login details below:";
                String line3 = "Username : " + username;
                String line4 = "Password : " + pwd;
                String msg = line1 + "\n\n" + line2 + "\n\n" + line3 + "\n" + line4;
                
                //To send password recovery mail to the user
                SendMail sendMail = new SendMail();
                int i = sendMail.sendMessageForPwdSettings(email_id, "Password Recovery", msg);
                //int i = sendMail.sendMessageWithoutCC(111, rs_validate_email.getString(8), rs_validate_email.getString(3), email_id, rs_validate_email.getString(11), "");
                if (i == 1) {
                    list.add("Success");
                }
            } else {
                list.add("Sorry, no account with that email address was found.");
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
                if (stmt_validate_email != null) {
                    stmt_validate_email.close();
                }
                if (rs_validate_email != null) {
                    rs_validate_email.close();
                }
            } catch (SQLException ex) {
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
        //processRequest(request, response);

//        JOptionPane.showMessageDialog(null,"assa");
        PrintWriter out = response.getWriter();
        String email_id = request.getParameter("login_id");
        String password = request.getParameter("pass");
        String login_type = request.getParameter("login_type");
        List<String> list = new ArrayList<String>();
        String json = null;

//        System.out.println("id is = "+email_id+" and pass= "+password+" and type is = "+login_type);
        HttpSession ses = request.getSession();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);

            stmt = con.createStatement();

            rs = stmt.executeQuery("select * from `1_user_detail` where email_id='" + email_id + "' and BINARY password='" + password + "' and user_status='Active'");

            if (rs.next()) {

                if (login_type.equals("Normal user") && !rs.getString(4).equals("Admin")) {

                    ses.setAttribute("user_serial_no", rs.getString(1));
                    ses.setAttribute("emp_id", rs.getString(2));
                    ses.setAttribute("emp_name", rs.getString(3));
                    ses.setAttribute("Designation", rs.getString(4));
                    ses.setAttribute("joining_date", rs.getString(6));
                    ses.setAttribute("email_id", rs.getString(8));
                    ses.setAttribute("role_id", rs.getString(10));
                    ses.setAttribute("ph_no", rs.getString(12));

                    list.add("1");
                        list.add("CRbookingManager.jsp");
                    
                    
//                    if (rs.getString(4).equalsIgnoreCase("Manager")) {
//                        //redirect to the page associate to manager
////                        response.sendRedirect("menu_manager.jsp");
//                        list.add("1");
//                        list.add("menu_manager.jsp");
//                    } else if (rs.getString(4).equalsIgnoreCase("Engineer")) {
//
//                        list.add("1");
//                        list.add("menu_user.jsp");
//
//                        //redirect to the page associate to engineer
////                        response.sendRedirect("menu_user.jsp");
//                    } else {
//
//                        response.setContentType("text/html");
//                        out.println("<script type=\"text/javascript\">");
//                        out.println("alert('User Account already Exist !!');");
//                        out.println("</script>");
////                        JOptionPane.showMessageDialog(null,"Invalid login!! contact Admin", "Database Error", 2);
//                        ses.invalidate();
//
//                        list.add("0");
//                        list.add("Invalid login!!");
//
////                        response.sendRedirect("login.jsp");
//                    }

                } 
//                else if (login_type.equals("admin") && rs.getString(4).equals("Admin")) {
//                    ses.setAttribute("Designation", rs.getString(4));
//                    ses.setAttribute("emp_name", "Admin");
//
//                    list.add("1");
//                    list.add("menu_admin.jsp");
//
////                    response.sendRedirect("menu_admin.jsp");
//                }
                else {
//                 response.setContentType("text/html");
//        out.println("<script type=\"text/javascript\">");
//        out.println("alert('User Account already Exist !!');");
//        out.println("</script>");
//                JOptionPane.showMessageDialog(null,"Invalid login!!", "Error", 2);
//                ses.invalidate();
                    list.add("0");
                    list.add("Invalid login!!");

//                response.sendRedirect("login.jsp");
                }

                ses.setAttribute("logined", "logined");

            } else {
//                 response.setContentType("text/html");
//        out.println("<script type=\"text/javascript\">");
//        out.println("alert('User Account already Exist !!');");
//        out.println("</script>");

//                JOptionPane.showMessageDialog(null,"Invalid login!!", "Error", 2);
                list.add("0");
                list.add("Invalid login!!");

//                ses.invalidate();
//               response.sendRedirect("login.jsp");
            }
            json = new Gson().toJson(list);
            response.setContentType("application/json");
            response.getWriter().write(json);
        } catch (Exception e) {
            out.println("exeption in login.java: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
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
