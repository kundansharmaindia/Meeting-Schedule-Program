///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.reflect.TypeToken;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URLEncoder;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import com.lear.app.db.connectionCredentials;
//
///**
// *
// * @author kundan
// */
//public class AdminMailRequestAction extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet AdmuinMailRequestAction</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet AdmuinMailRequestAction at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        /*
//         * this get method contail code for approval or rejection of requests by manager from manager paending requests page
//         */
//
//        Connection con = null;
//        Statement stmt_soft_req_id = null;
//        Statement stmt_soft_req_id_admin = null;
//        Statement stmt_table_9 = null;
//        Statement stmt_table_8 = null;
//        Statement stmt_req_id = null;
//        Statement stmt_table_22 = null;
//        Statement stmt_table_admin = null;
//        ResultSet rs_soft_req_id = null;
//        ResultSet rs_soft_req_id_admin = null;
//        ResultSet rs_req_id = null;
//        Statement stmt_hard_req_id = null;
//        Statement stmt_hard_req_id_admin = null;
//        Statement stmt_team = null;
//        Statement stmt_table_14 = null;
//        Statement stmt_table_13 = null;
//        ResultSet rs_hard_req_id = null;
//        ResultSet rs_table14 = null;
//        ResultSet rs_hard_req_id_admin = null;
//        Statement stmt_userDetail = null;
//        Statement stmt_managerDetail = null;
//        Statement stmt_req_type = null;
//        Statement stmt_manager_email = null;
//        Statement stmt_AdminDetail = null;
//        Statement stmt_hardType = null;
//        Statement stmt_System = null;
//        Statement stmt_Details = null;
//        ResultSet rs_manager_email = null;
//        ResultSet rs_managerDetail = null;
//        ResultSet rs_team = null;
//        ResultSet rs_req_type = null;
//        ResultSet rs_userDetail = null;
//        ResultSet rs_hardType = null;
//        ResultSet rs_Details = null;
//        ResultSet rs_System = null;
//        ResultSet rs_AdminDetail = null;
//
//        try {
//            String ServerPath = connectionCredentials.serverAddress();
//            String ServerLoginID = connectionCredentials.ServerID();
//            String ServerPassword = connectionCredentials.ServerPassword();
//            String DBDrivers = connectionCredentials.DBDriverName();
//
//            Class.forName(DBDrivers);
//
//            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
//
//            String RequestID = request.getParameter("RequestID");
//            String action_type = request.getParameter("action_type");
//            String RequestType = request.getParameter("requestTypeHidden");
//            String reason = request.getParameter("comment");
//            String manager_name = request.getParameter("ManagerName");
//            String manager_id = request.getParameter("ManagerID");
//
////            HistoryTableClass history = new HistoryTableClass();
//            List<String> list = new ArrayList<String>();
//
//            String URL = "", AdminEmailEnc = "", AdminPasEnc = "", reqIDEnc = "", reqtyp = "", AdminPass = "", AdminEmail = "";
//            EncryptionUtil encryptionUtil = new EncryptionUtil();
//
//            String request_status_id = "", request_status_id_admin = "", soft_req_id = "", hard_req_id = "", json = "", softDescription = "", HardID = "", soft_reqType = "", reasonManager = "";
//            String SoftwareStatus = "", softName = "", soft_version = "", softCatagory = "", teamName = "", user_name = "", UserEmail = "", Email_Manager = "", ReasonOfRequest = "";
//
//            java.util.Date dt = new java.util.Date();
//            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//            String date = sdf.format(dt);
//
//            stmt_managerDetail = con.createStatement();
//            rs_managerDetail = stmt_managerDetail.executeQuery("SELECT * FROM `20_manager_table` where manager_id='" + manager_id + "'");
//            if (rs_managerDetail.next()) {
//                stmt_manager_email = con.createStatement();
//                rs_manager_email = stmt_manager_email.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_managerDetail.getString(3) + "'");
//                if (rs_manager_email.next()) {
//                    Email_Manager = rs_manager_email.getString(8);
////                        senderPassword_Manager=rs_manager_email.getString(11);
//                }
//            }
//
//            stmt_AdminDetail = con.createStatement();
//            rs_AdminDetail = stmt_AdminDetail.executeQuery("SELECT * FROM `1_user_detail` where Designation='Admin'");
//            if (rs_AdminDetail.next()) {
//                AdminEmail = rs_AdminDetail.getString(14);
//                AdminPass = rs_AdminDetail.getString(11);
//            }
//
//            if (RequestType.equals("Software")) {
//                stmt_req_id = con.createStatement();
//                rs_req_id = stmt_req_id.executeQuery("SELECT * FROM `8_software_request_table` where sw_req_id='" + RequestID + "'");
//                if (rs_req_id.next()) {
//                    soft_req_id = rs_req_id.getString(1);
//
//                    SoftwareStatus = rs_req_id.getString(14);
//                    ReasonOfRequest = rs_req_id.getString(8);
//
//                    stmt_team = con.createStatement();
//                    rs_team = stmt_team.executeQuery("SELECT * FROM `project_lear`.`2_team_table` WHERE `team_id` = '" + rs_req_id.getString(3) + "'");
//                    if (rs_team.next()) {
//                        teamName = rs_team.getString(2);
//                    }
//
//                    stmt_System = con.createStatement();
//                    rs_System = stmt_System.executeQuery("SELECT * FROM `project_lear`.`5_system_details_table` WHERE `system_id` = '" + rs_req_id.getString(4) + "'");
//                    if (rs_System.next()) {
//                        HardID = rs_System.getString(2);
//                    }
//
//                    if (SoftwareStatus.equals("Available")) {
//                        stmt_Details = con.createStatement();
//                        rs_Details = stmt_Details.executeQuery("SELECT * FROM `4_software_detail_table` where soft_id='" + rs_req_id.getString(6) + "'");
//                        if (rs_Details.next()) {
//                            softName = rs_Details.getString(2);
//                            soft_version = rs_Details.getString(3);
//                            softCatagory = rs_Details.getString(4);
//                            softDescription = rs_Details.getString(7);
//
//                            stmt_req_type = con.createStatement();
//                            rs_req_type = stmt_req_type.executeQuery("SELECT * FROM `10_software_request_type` where sw_request_type_id='" + rs_req_id.getString(5) + "'");
//                            if (rs_req_type.next()) {
//                                soft_reqType = rs_req_type.getString(2);
//                            }
//                        }
//                    } else if (SoftwareStatus.equals("New Software Request")) {
//                        stmt_Details = con.createStatement();
//                        rs_Details = stmt_Details.executeQuery("SELECT * FROM `24_new_soft_request` where soft_request_id='" + rs_req_id.getString(1) + "'");
//                        if (rs_Details.next()) {
//                            softName = rs_Details.getString(3);
//                            soft_version = rs_Details.getString(4);
//                            softCatagory = rs_Details.getString(6);
//                            softDescription = rs_Details.getString(5);
//
//                            if (rs_Details.getString(7).equals("Other")) {
//                                soft_reqType = rs_Details.getString(8);
//                            } else {
//                                soft_reqType = rs_Details.getString(7);
//                            }
//                        }
//                    }
//
//                    stmt_userDetail = con.createStatement();
//                    rs_userDetail = stmt_userDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_req_id.getString(2) + "'");
//                    if (rs_userDetail.next()) {
//                        user_name = rs_userDetail.getString(3);
//                        UserEmail = rs_userDetail.getString(8);
//                    }
//
//                }
//
//                if (action_type.equals("Approve")) {
//                    stmt_soft_req_id = con.createStatement();
//                    rs_soft_req_id = stmt_soft_req_id.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Approved'");
//                    if (rs_soft_req_id.next()) {
//                        request_status_id = rs_soft_req_id.getString(1);
//                    }
//
//                    stmt_soft_req_id_admin = con.createStatement();
//                    rs_soft_req_id_admin = stmt_soft_req_id_admin.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Pending'");
//                    if (rs_soft_req_id_admin.next()) {
//                        request_status_id_admin = rs_soft_req_id_admin.getString(1);
//                    }
//
//                    stmt_table_9 = con.createStatement();
//                    stmt_table_8 = con.createStatement();
//                    stmt_table_22 = con.createStatement();
//
//                    int c = stmt_table_8.executeUpdate("UPDATE `8_software_request_table` SET request_status_id='" + request_status_id + "',final_status='Approved' where soft_request_id='" + soft_req_id + "'");
//                    if (c != 0) {
//                        int b = stmt_table_9.executeUpdate("update `22_admin_request_status` set date='" + date + "',request_status_id='" + request_status_id + "',remark='" + reason + "' where request_type='Software' and request_id='" + soft_req_id + "'");
//                        if (b != 0) {
////                            int a=stmt_table_22.executeUpdate("insert into `22_admin_request_status` (request_type,request_id,request_status_id) values('Software','"+soft_req_id+"','"+request_status_id_admin+"')");
//
////                            history.historyData(613, RequestID, "Admin", reason);
//                            list.add("Approved");
//
////                            AdminEmailEnc=encryptionUtil.finalEnc(AdminEmail);
////                            AdminPasEnc=encryptionUtil.finalEnc(AdminPass);
////                            reqIDEnc=encryptionUtil.finalEnc(RequestID);
////                            reqtyp=encryptionUtil.finalEnc("Software");
////                            userType=encryptionUtil.finalEnc("Admin");
////                            URL="?manemail="+URLEncoder.encode(AdminEmailEnc, "UTF-8")+"&secpas="+URLEncoder.encode(AdminPasEnc, "UTF-8")+"&usertyp=admn&reqid="+URLEncoder.encode(reqIDEnc, "UTF-8")+"&reqtyp="+URLEncoder.encode(reqtyp, "UTF-8");
////                            SendMail.sendMail_Soft_Hard_Admin(134, senderEmail_Manager, UserEmail_CC, user_name, soft_reqType, softName,soft_version,softCatagory,ReasonOfRequest,SoftwareStatus,URL,manager_name);
//                            SendMail.sendMail_to_SCustomer(138, UserEmail, Email_Manager, RequestID, user_name, teamName, softCatagory, softName, softDescription, HardID, "", "");
//
//                        }
//                    }
//                } else if (action_type.equals("Reject")) {
//                    stmt_soft_req_id = con.createStatement();
//                    rs_soft_req_id = stmt_soft_req_id.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Rejected'");
//                    if (rs_soft_req_id.next()) {
//                        request_status_id = rs_soft_req_id.getString(1);
//                    }
//
//                    stmt_table_9 = con.createStatement();
//                    stmt_table_8 = con.createStatement();
//
//                    int a = stmt_table_8.executeUpdate("UPDATE `8_software_request_table` SET request_status_id='" + request_status_id + "',final_date_approval='" + date + "',final_status='Rejected' where soft_request_id='" + soft_req_id + "'");
//                    int b = stmt_table_9.executeUpdate("update `22_admin_request_status` set request_status_id='" + request_status_id + "', date='" + date + "',remark='" + reason + "' where request_id='" + soft_req_id + "' and request_type='Software'");
//
//                    if (a != 0 && b != 0) {
////                        stmt_table_admin=con.createStatement();
////                        int c=stmt_table_admin.executeUpdate("delete from `22_admin_request_status` where request_type='Software' and request_id='"+soft_req_id+"'");
////                        history.historyData(615,RequestID ,manager_name , "Manager", "");
//
//                        history.historyData(617, RequestID, "Admin", reason);
//                        list.add("Rejected");
//
//                        stmt_table_14 = con.createStatement();
//                        rs_table14 = stmt_table_14.executeQuery("SELECT * FROM `9_software_request_approval` where soft_request_id='" + soft_req_id + "'");
//                        if (rs_table14.next()) {
//                            reasonManager = rs_table14.getString(8);
//                        }
//
//                        SendMail.sendMail_From_Admin(140, UserEmail, Email_Manager, user_name, soft_reqType, softName, soft_version, softCatagory, ReasonOfRequest, SoftwareStatus, reasonManager, reason);
//                    }
//                }
//            } else if (RequestType.equals("Hardware")) {
//                String Hard_desc = "", Hard_reqType = "", hardType = "", HardwareStatus = "", requestDate = "";
//                stmt_req_id = con.createStatement();
//                rs_req_id = stmt_req_id.executeQuery("SELECT * FROM `13_hardware_request_table` where hw_req_id='" + RequestID + "'");
//                if (rs_req_id.next()) {
//                    hard_req_id = rs_req_id.getString(1);
//                    ReasonOfRequest = rs_req_id.getString(8);
//                    HardwareStatus = rs_req_id.getString(14);
//                    requestDate = rs_req_id.getString(7);
//
//                    stmt_team = con.createStatement();
//                    rs_team = stmt_team.executeQuery("SELECT * FROM `project_lear`.`2_team_table` WHERE `team_id` = '" + rs_req_id.getString(3) + "'");
//                    if (rs_team.next()) {
//                        teamName = rs_team.getString(2);
//                    }
//
//                    stmt_userDetail = con.createStatement();
//                    rs_userDetail = stmt_userDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_req_id.getString(2) + "'");
//                    if (rs_userDetail.next()) {
//                        user_name = rs_userDetail.getString(3);
//                        UserEmail = rs_userDetail.getString(8);
//                    }
//
//                    Hard_desc = rs_req_id.getString(6);
//
//                    stmt_req_type = con.createStatement();
//                    rs_req_type = stmt_req_type.executeQuery("SELECT * FROM `18_hardware_request_type` where hw_request_type_id='" + rs_req_id.getString(4) + "'");
//                    if (rs_req_type.next()) {
//                        Hard_reqType = rs_req_type.getString(2);
//                    }
//
//                    if (HardwareStatus.equals("Available")) {
//                        stmt_hardType = con.createStatement();
//                        rs_hardType = stmt_hardType.executeQuery("SELECT * FROM `6_hardware_type_table` where hardware_type_id='" + rs_req_id.getString(5) + "'");
//                        if (rs_hardType.next()) {
//                            hardType = rs_hardType.getString(2);
//                        }
//                    } else if (HardwareStatus.equals("New Hardware Request")) {
//                        stmt_Details = con.createStatement();
//                        rs_Details = stmt_Details.executeQuery("SELECT * FROM `24_new_soft_request` where soft_request_id='" + rs_req_id.getString(1) + "'");
//                        if (rs_Details.next()) {
//                            hardType = rs_Details.getString(3);
//                        }
//                    }
//
//                }
//
//                if (action_type.equals("Approve")) {
//                    stmt_hard_req_id = con.createStatement();
//                    rs_hard_req_id = stmt_hard_req_id.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Approved'");
//                    if (rs_hard_req_id.next()) {
//                        request_status_id = rs_hard_req_id.getString(1);
//                    }
//
//                    stmt_hard_req_id_admin = con.createStatement();
//                    rs_hard_req_id_admin = stmt_hard_req_id_admin.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Pending'");
//                    if (rs_hard_req_id_admin.next()) {
//                        request_status_id_admin = rs_hard_req_id_admin.getString(1);
//                    }
//
//                    stmt_table_14 = con.createStatement();
//                    stmt_table_13 = con.createStatement();
//                    stmt_table_22 = con.createStatement();
//                    int c = stmt_table_13.executeUpdate("UPDATE `13_hardware_request_table` SET request_status_id='" + request_status_id + "',final_status='Approved' where hw_request_id='" + hard_req_id + "'");
//                    int b = stmt_table_14.executeUpdate("update `22_admin_request_status` set date='" + date + "',request_status_id='" + request_status_id + "',remark='" + reason + "' where request_type='Hardware' and request_id='" + hard_req_id + "'");
//                    if (b != 0) {
////                        int a=stmt_table_22.executeUpdate("insert into `22_admin_request_status` (request_type,request_id,request_status_id) values('Hardware','"+hard_req_id+"','"+request_status_id_admin+"')");
//                        history.historyData(613, RequestID, "Admin", reason);
//                        list.add("Approved");
//
////                        AdminEmailEnc=encryptionUtil.finalEnc(AdminEmail);
////                        AdminPasEnc=encryptionUtil.finalEnc(AdminPass);
////                        reqIDEnc=encryptionUtil.finalEnc(RequestID);
////                        reqtyp=encryptionUtil.finalEnc("Hardware");
//////                        userType=encryptionUtil.finalEnc("Admin");
////
////                        URL="?manemail="+URLEncoder.encode(AdminEmailEnc, "UTF-8")+"&secpas="+URLEncoder.encode(AdminPasEnc, "UTF-8")+"&usertyp=admn&reqid="+URLEncoder.encode(reqIDEnc, "UTF-8")+"&reqtyp="+URLEncoder.encode(reqtyp, "UTF-8");
////                        SendMail.sendMail_Soft_Hard_Admin(135, Email_Manager, UserEmail, user_name, Hard_reqType, hardType,Hard_desc,ReasonOfRequest,requestDate,HardwareStatus,URL,manager_name);
//                        SendMail.sendMail_to_SCustomer(139, UserEmail, Email_Manager, RequestID, user_name, teamName, hardType, Hard_desc, "", "", "", "");
////                        SendMail.sendMail_From_Admin (137, Email_Manager, UserEmail, user_name, Hard_reqType, hardType,Hard_desc,ReasonOfRequest,requestDate,HardwareStatus,reason,date);
//
//                    }
//                } else if (action_type.equals("Reject")) {
//                    stmt_hard_req_id = con.createStatement();
//                    rs_hard_req_id = stmt_hard_req_id.executeQuery("SELECT request_status_id FROM `11_request_status_table` WHERE `status_type` = 'Rejected'");
//                    if (rs_hard_req_id.next()) {
//                        request_status_id = rs_hard_req_id.getString(1);
//                    }
//
//                    stmt_table_admin = con.createStatement();
//                    stmt_table_13 = con.createStatement();
//
//                    int a = stmt_table_13.executeUpdate("UPDATE `13_hardware_request_table` SET request_status_id='" + request_status_id + "',final_date_approval='" + date + "',final_status='Rejected' where hw_request_id='" + hard_req_id + "'");
//                    int b = stmt_table_admin.executeUpdate("update `22_admin_request_status` set date='" + date + "',request_status_id='" + request_status_id + "',remark='" + reason + "' where request_type='Hardware' and request_id='" + hard_req_id + "'");
//                    if (a != 0 && b != 0) {
////                        stmt_table_admin=con.createStatement();
//
//                        stmt_table_14 = con.createStatement();
//                        rs_table14 = stmt_table_14.executeQuery("SELECT * FROM `14_hardware_request_approval` where hw_request_id='" + hard_req_id + "'");
//                        if (rs_table14.next()) {
//                            reasonManager = rs_table14.getString(8);
//                        }
//
//                        history.historyData(616, RequestID, "Admin", reason);
//                        list.add("Rejected");
//
//                        SendMail.sendMail_From_Admin(141, UserEmail, Email_Manager, user_name, Hard_reqType, hardType, Hard_desc, ReasonOfRequest, requestDate, HardwareStatus, reasonManager, reason);
//
//                    }
//                }
//            }
//
//            json = new Gson().toJson(list);
//            response.setContentType("application/json");
//            response.getWriter().write(json);
//        } catch (Exception e) {
//            System.out.println("Exception= " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            try {
//                if (con != null) {
//                    con.close();
//                }
//                if (stmt_soft_req_id != null) {
//                    stmt_soft_req_id.close();
//                }
//                if (stmt_soft_req_id_admin != null) {
//                    stmt_soft_req_id_admin.close();
//                }
//                if (stmt_table_9 != null) {
//                    stmt_table_9.close();
//                }
//                if (stmt_table_8 != null) {
//                    stmt_table_8.close();
//                }
//                if (stmt_req_id != null) {
//                    stmt_req_id.close();
//                }
//                if (stmt_table_22 != null) {
//                    stmt_table_22.close();
//                }
//                if (stmt_table_admin != null) {
//                    stmt_table_admin.close();
//                }
//                if (rs_soft_req_id != null) {
//                    rs_soft_req_id.close();
//                }
//                if (rs_soft_req_id_admin != null) {
//                    rs_soft_req_id_admin.close();
//                }
//                if (rs_req_id != null) {
//                    rs_req_id.close();
//                }
//                if (stmt_hard_req_id != null) {
//                    stmt_hard_req_id.close();
//                }
//                if (stmt_hard_req_id_admin != null) {
//                    stmt_hard_req_id_admin.close();
//                }
//                if (stmt_team != null) {
//                    stmt_team.close();
//                }
//                if (stmt_table_14 != null) {
//                    stmt_table_14.close();
//                }
//                if (stmt_table_13 != null) {
//                    stmt_table_13.close();
//                }
//                if (rs_hard_req_id != null) {
//                    rs_hard_req_id.close();
//                }
//                if (rs_table14 != null) {
//                    rs_table14.close();
//                }
//                if (rs_hard_req_id_admin != null) {
//                    rs_hard_req_id_admin.close();
//                }
//                if (stmt_userDetail != null) {
//                    stmt_userDetail.close();
//                }
//                if (stmt_managerDetail != null) {
//                    stmt_managerDetail.close();
//                }
//                if (stmt_req_type != null) {
//                    stmt_req_type.close();
//                }
//                if (stmt_manager_email != null) {
//                    stmt_manager_email.close();
//                }
//                if (stmt_AdminDetail != null) {
//                    stmt_AdminDetail.close();
//                }
//                if (stmt_hardType != null) {
//                    stmt_hardType.close();
//                }
//                if (stmt_System != null) {
//                    stmt_System.close();
//                }
//                if (stmt_Details != null) {
//                    stmt_Details.close();
//                }
//                if (rs_manager_email != null) {
//                    rs_manager_email.close();
//                }
//                if (rs_managerDetail != null) {
//                    rs_managerDetail.close();
//                }
//                if (rs_team != null) {
//                    rs_team.close();
//                }
//                if (rs_req_type != null) {
//                    rs_req_type.close();
//                }
//                if (rs_userDetail != null) {
//                    rs_userDetail.close();
//                }
//                if (rs_hardType != null) {
//                    rs_hardType.close();
//                }
//                if (rs_Details != null) {
//                    rs_Details.close();
//                }
//                if (rs_System != null) {
//                    rs_System.close();
//                }
//                if (rs_AdminDetail != null) {
//                    rs_AdminDetail.close();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        Connection con = null;
//        Statement stmt_loginCheck = null;
//        Statement stmt_hard_type = null;
//        Statement stmt_req_type = null;
//        Statement stmt_SoftwareTable = null;
//        Statement stmt_TeamDetail = null;
//        Statement stmt_useDetail = null;
//        Statement stmt_HardwareTable = null;
//        Statement stmt_ManagerDetail = null;
//        Statement stmt_systemDetails = null;
//        Statement stmt_os = null;
//        Statement stmt_ReqPendingID = null;
//        Statement stmt_Details = null;
//        Statement stmt_managerID = null;
//        Statement stmt_checkAdminStatus = null;
//        Statement stmt_managerActionStatus = null;
//        ResultSet rs_managerActionStatus = null;
//        ResultSet rs_loginCheck = null;
//        ResultSet rs_systemDetails = null;
//        ResultSet rs_req_type = null;
//        ResultSet rs_ReqPendingID = null;
//        ResultSet rs_hard_type = null;
//        ResultSet rs_ManagerDetail = null;
//        ResultSet rs_useDetail = null;
//        ResultSet rs_os = null;
//        ResultSet rs_Details = null;
//        ResultSet rs_checkAdminStatus = null;
//        ResultSet rs_TeamDetail = null;
//        ResultSet rs_SoftwareTable = null;
//        ResultSet rs_HardwareTable = null;
//        ResultSet rs_managerID = null;
//
//        try {
//            String ServerPath = connectionCredentials.serverAddress();
//            String ServerLoginID = connectionCredentials.ServerID();
//            String ServerPassword = connectionCredentials.ServerPassword();
//            String DBDrivers = connectionCredentials.DBDriverName();
//
//            Class.forName(DBDrivers);
//
//            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
//
//            String AdminEmail = request.getParameter("AdminEmail");
//            String AdminPass = request.getParameter("AdminPass");
//            String RequestIDEnc = request.getParameter("RequestID");
//            String RequestTypeEnc = request.getParameter("RequestType");
//
//            String AdminEmailDec = "", AdminPasswordDec = "", requestIDDec = "", reqTypedec = "", managerID = "", Req_PendingId = "", UserName = "", UserEmpId = "", User_Team_name = "", OS = "", reason = "", ReqDate = "", hard_reqType = "", SystemType_requestFor = "", SystemDescrib_RequestFor = "";
//            String softName = "", soft_version = "", softCatagory = "", soft_reqType = "", softDescription = "", managerName = "", managerActionDate = "", managerComment = "", ManagerEmailId = "";
//            String hardwareID = "", hardType = "", hardDescrib = "";
//            EncryptionUtil encryptionUtil = new EncryptionUtil();
//
//            ArrayList<PojoSoftReport> Data = new ArrayList<PojoSoftReport>();
//            PojoSoftReport pojoSoft = new PojoSoftReport();
//
//            AdminEmailDec = encryptionUtil.finalDec(AdminEmail);
//            AdminPasswordDec = encryptionUtil.finalDec(AdminPass);
//            requestIDDec = encryptionUtil.finalDec(RequestIDEnc);
//            reqTypedec = encryptionUtil.finalDec(RequestTypeEnc);
//
//            stmt_loginCheck = con.createStatement();
//            rs_loginCheck = stmt_loginCheck.executeQuery("SELECT * FROM `1_user_detail` where AdminEmail='" + AdminEmailDec + "' and password='" + AdminPasswordDec + "'");
//            if (rs_loginCheck.next()) {
////                stmt_managerID=con.createStatement();
////                rs_managerID=stmt_managerID.executeQuery("SELECT * FROM `20_manager_table` where user_serial_no='"+rs_loginCheck.getString(1)+"'");
////                if(rs_managerID.next())
////                {
////                    managerID=rs_managerID.getString(1);
////                    managerName=rs_managerID.getString(2);
////                    
////                    pojoSoft.setManagerID(managerID);
////                    pojoSoft.setManagerName(managerName);
//
//                stmt_ReqPendingID = con.createStatement();
//                rs_ReqPendingID = stmt_ReqPendingID.executeQuery("SELECT * FROM `11_request_status_table` WHERE status_type = 'Pending'");
//                if (rs_ReqPendingID.next()) {
//                    Req_PendingId = rs_ReqPendingID.getString(1);
//                }
//
//                if (reqTypedec.equals("Software")) {
//
//                    pojoSoft.setActionTypeMessage(reqTypedec);
//
//                    stmt_SoftwareTable = con.createStatement();
//                    rs_SoftwareTable = stmt_SoftwareTable.executeQuery("SELECT * FROM `8_software_request_table` where sw_req_id='" + requestIDDec + "'");
//                    if (rs_SoftwareTable.next()) {
//                        stmt_checkAdminStatus = con.createStatement();
//                        rs_checkAdminStatus = stmt_checkAdminStatus.executeQuery("SELECT * FROM `22_admin_request_status` where request_type='Software' and request_id='" + rs_SoftwareTable.getString(1) + "'");
//                        if (rs_checkAdminStatus.next()) {
//
//                            stmt_managerActionStatus = con.createStatement();
//                            rs_managerActionStatus = stmt_managerActionStatus.executeQuery("SELECT * FROM `9_software_request_approval` where soft_request_id='" + rs_SoftwareTable.getString(1) + "'");
//                            if (rs_managerActionStatus.next()) {
//
//                                managerActionDate = rs_managerActionStatus.getString(5);
//                                managerComment = rs_managerActionStatus.getString(8);
//
//                                stmt_managerID = con.createStatement();
//                                rs_managerID = stmt_managerID.executeQuery("SELECT * FROM `20_manager_table` where manager_id='" + rs_managerActionStatus.getString(6) + "'");
//                                if (rs_managerID.next()) {
//                                    managerID = rs_managerID.getString(1);
//
//                                    pojoSoft.setManagerID(managerID);
//
//                                    stmt_ManagerDetail = con.createStatement();
//                                    rs_ManagerDetail = stmt_ManagerDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_managerID.getString(3) + "'");
//                                    if (rs_ManagerDetail.next()) {
//                                        managerName = rs_ManagerDetail.getString(3);
//                                        ManagerEmailId = rs_ManagerDetail.getString(8);
//
//                                    }
//
//                                    pojoSoft.setManagerActionDate(managerActionDate);
//                                    pojoSoft.setManagerName(managerName);
//                                    pojoSoft.setManagerComment(managerComment);
//
//                                }
//                            }
//
//                            if (rs_checkAdminStatus.getString(4).equals(Req_PendingId)) {
//                                stmt_useDetail = con.createStatement();
//                                rs_useDetail = stmt_useDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_SoftwareTable.getString(2) + "'");
//                                if (rs_useDetail.next()) {
//                                    UserName = rs_useDetail.getString(3);
//                                    UserEmpId = rs_useDetail.getString(2);
//                                }
//
//                                stmt_TeamDetail = con.createStatement();
//                                rs_TeamDetail = stmt_TeamDetail.executeQuery("SELECT * FROM `2_team_table` where team_id='" + rs_SoftwareTable.getString(3) + "'");
//                                if (rs_TeamDetail.next()) {
//                                    User_Team_name = rs_TeamDetail.getString(2);
//                                }
//
//                                reason = rs_SoftwareTable.getString(8);
//                                ReqDate = rs_SoftwareTable.getString(7);
//
//                                stmt_systemDetails = con.createStatement();
//                                rs_systemDetails = stmt_systemDetails.executeQuery("SELECT * FROM `5_system_details_table` where system_id='" + rs_SoftwareTable.getString(4) + "'");
//                                if (rs_systemDetails.next()) {
//                                    stmt_os = con.createStatement();
//                                    rs_os = stmt_os.executeQuery("SELECT * FROM `15_os_list_table` where os_id='" + rs_systemDetails.getString(6) + "'");
//                                    if (rs_os.next()) {
//                                        OS = rs_os.getString(2);
//                                    }
//                                    hardwareID = rs_systemDetails.getString(2);
//                                    hardDescrib = rs_systemDetails.getString(5);
//
//                                    stmt_hard_type = con.createStatement();
//                                    rs_hard_type = stmt_hard_type.executeQuery("SELECT * FROM `6_hardware_type_table` where hardware_type_id='" + rs_systemDetails.getString(3) + "'");
//                                    if (rs_hard_type.next()) {
//                                        hardType = rs_hard_type.getString(2);
//                                    }
//                                }
//
//                                if (rs_SoftwareTable.getString(14).equals("Available")) {
//                                    stmt_Details = con.createStatement();
//                                    rs_Details = stmt_Details.executeQuery("SELECT * FROM `4_software_detail_table` where soft_id='" + rs_SoftwareTable.getString(6) + "'");
//                                    if (rs_Details.next()) {
//                                        softName = rs_Details.getString(2);
//                                        soft_version = rs_Details.getString(3);
//                                        softCatagory = rs_Details.getString(4);
//                                        softDescription = rs_Details.getString(7);
//
//                                        stmt_req_type = con.createStatement();
//                                        rs_req_type = stmt_req_type.executeQuery("SELECT * FROM `10_software_request_type` where sw_request_type_id='" + rs_SoftwareTable.getString(5) + "'");
//                                        if (rs_req_type.next()) {
//                                            soft_reqType = rs_req_type.getString(2);
//                                        }
//
//                                        pojoSoft.setSoftname(softName);
//                                        pojoSoft.setVer(soft_version);
//                                        pojoSoft.setCat(softCatagory);
//                                        pojoSoft.setDesc(softDescription);
//                                        pojoSoft.setRequestType(soft_reqType);
//                                        pojoSoft.setSoftwareStatus("Available");
//
//                                    }
//                                } else if (rs_SoftwareTable.getString(14).equals("New Software Request")) {
//                                    stmt_Details = con.createStatement();
//                                    rs_Details = stmt_Details.executeQuery("SELECT * FROM `24_new_soft_request` where soft_request_id='" + rs_SoftwareTable.getString(1) + "'");
//                                    if (rs_Details.next()) {
//                                        softName = rs_Details.getString(3);
//                                        soft_version = rs_Details.getString(4);
//                                        softCatagory = rs_Details.getString(6);
//                                        softDescription = rs_Details.getString(5);
//
//                                        if (rs_Details.getString(7).equals("Other")) {
//                                            soft_reqType = rs_Details.getString(8);
//                                        } else {
//                                            soft_reqType = rs_Details.getString(7);
//                                        }
//
//                                        pojoSoft.setSoftname(softName);
//                                        pojoSoft.setVer(soft_version);
//                                        pojoSoft.setCat(softCatagory);
//                                        pojoSoft.setDesc(softDescription);
//                                        pojoSoft.setRequestType(soft_reqType);
//                                        pojoSoft.setSoftwareStatus("Not Available. New Software Request");
//                                    }
//                                }
//// set data in pojo class
//                                pojoSoft.setEmployeeName(UserName);
//                                pojoSoft.setEmployeeID(UserEmpId);
//                                pojoSoft.setTeamName(User_Team_name);
//                                pojoSoft.setSoftReqID(requestIDDec);
//                                pojoSoft.setOs(OS);
//                                pojoSoft.setPur_date(ReqDate);
//                                pojoSoft.setReason(reason);
//                                pojoSoft.setHardwareType(hardType);
//                                pojoSoft.setHardwareID(hardwareID);
//                                pojoSoft.setHardwareDesc(hardDescrib);
//                            } else {
//                                pojoSoft.setActionTypeMessage("Action Already Taken. Please login to your SHMS account for more information.");
//                            }
//                        }
//                    } else {
//                        pojoSoft.setActionTypeMessage("Request Not Found");
//                    }
//                } else if (reqTypedec.equals("Hardware")) {
//                    pojoSoft.setActionTypeMessage(reqTypedec);
//                    stmt_HardwareTable = con.createStatement();
//                    rs_HardwareTable = stmt_HardwareTable.executeQuery("SELECT * FROM `13_hardware_request_table` where hw_req_id='" + requestIDDec + "'");
//                    if (rs_HardwareTable.next()) {
//                        stmt_checkAdminStatus = con.createStatement();
////                            rs_checkAdminStatus=stmt_checkAdminStatus.executeQuery("SELECT * FROM `14_hardware_request_approval` where hw_request_id='"+rs_HardwareTable.getString(1)+"'");
//                        rs_checkAdminStatus = stmt_checkAdminStatus.executeQuery("SELECT * FROM `22_admin_request_status` where request_type='Hardware' and request_id='" + rs_HardwareTable.getString(1) + "'");
//                        if (rs_checkAdminStatus.next()) {
//
//                            stmt_managerActionStatus = con.createStatement();
//                            rs_managerActionStatus = stmt_managerActionStatus.executeQuery("SELECT * FROM `14_hardware_request_approval` where hw_request_id='" + rs_HardwareTable.getString(1) + "'");
//                            if (rs_managerActionStatus.next()) {
//
//                                managerActionDate = rs_managerActionStatus.getString(5);
//                                managerComment = rs_managerActionStatus.getString(8);
//
//                                stmt_managerID = con.createStatement();
//                                rs_managerID = stmt_managerID.executeQuery("SELECT * FROM `20_manager_table` where manager_id='" + rs_managerActionStatus.getString(6) + "'");
//                                if (rs_managerID.next()) {
//                                    managerID = rs_managerID.getString(1);
//
//                                    stmt_ManagerDetail = con.createStatement();
//                                    rs_ManagerDetail = stmt_ManagerDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_managerID.getString(3) + "'");
//                                    if (rs_ManagerDetail.next()) {
//                                        managerName = rs_ManagerDetail.getString(3);
//                                        ManagerEmailId = rs_ManagerDetail.getString(8);
//
//                                    }
//
//                                    pojoSoft.setManagerID(managerID);
//                                    pojoSoft.setManagerActionDate(managerActionDate);
//                                    pojoSoft.setManagerName(managerName);
//                                    pojoSoft.setManagerComment(managerComment);
//
//                                }
//                            }
//
//                            if (rs_checkAdminStatus.getString(4).equals(Req_PendingId)) {
//                                stmt_useDetail = con.createStatement();
//                                rs_useDetail = stmt_useDetail.executeQuery("SELECT * FROM `1_user_detail` where user_serial_no='" + rs_HardwareTable.getString(2) + "'");
//                                if (rs_useDetail.next()) {
//                                    UserName = rs_useDetail.getString(3);
//                                    UserEmpId = rs_useDetail.getString(2);
//                                }
//
//                                stmt_TeamDetail = con.createStatement();
//                                rs_TeamDetail = stmt_TeamDetail.executeQuery("SELECT * FROM `2_team_table` where team_id='" + rs_HardwareTable.getString(3) + "'");
//                                if (rs_TeamDetail.next()) {
//                                    User_Team_name = rs_TeamDetail.getString(2);
//                                }
//
//                                reason = rs_HardwareTable.getString(8);
//                                ReqDate = rs_HardwareTable.getString(7);
//
//                                pojoSoft.setEmployeeName(UserName);
//                                pojoSoft.setEmployeeID(UserEmpId);
//                                pojoSoft.setTeamName(User_Team_name);
//                                pojoSoft.setPur_date(ReqDate);
//                                pojoSoft.setReason(reason);
//                                pojoSoft.setSoftReqID(requestIDDec);
//
//                                if (rs_HardwareTable.getString(14).equals("Available")) {
//                                    stmt_Details = con.createStatement();
//                                    rs_Details = stmt_Details.executeQuery("SELECT * FROM `6_hardware_type_table` where hardware_type_id='" + rs_HardwareTable.getString(5) + "'");
//                                    if (rs_Details.next()) {
//                                        hardType = rs_Details.getString(2);
//                                    }
//                                    hardDescrib = rs_HardwareTable.getString(6);
//                                    pojoSoft.setSoftwareStatus("Available");
//                                } else if (rs_HardwareTable.getString(14).equals("New Hardware Request")) {
//                                    stmt_Details = con.createStatement();
//                                    rs_Details = stmt_Details.executeQuery("SELECT * FROM `28_new_hardware_request` where hw_request_id='" + rs_HardwareTable.getString(1) + "'");
//                                    if (rs_Details.next()) {
//                                        hardType = rs_Details.getString(3);
//                                        hardDescrib = rs_Details.getString(4);
//                                    }
//                                    pojoSoft.setSoftwareStatus("Not Available. New Hardware Request");
//                                }
//
//                                stmt_req_type = con.createStatement();
//                                rs_req_type = stmt_req_type.executeQuery("SELECT * FROM `18_hardware_request_type` where hw_request_type_id='" + rs_HardwareTable.getString(4) + "'");
//                                if (rs_req_type.next()) {
//                                    hard_reqType = rs_req_type.getString(2);
//                                }
//
//                                if (rs_HardwareTable.getString(15).equals("User")) {
//                                    OS = "-";
//                                    SystemDescrib_RequestFor = "-";
//                                    SystemType_requestFor = "-";
//                                    pojoSoft.setRequestForType("User");
//                                } else {
//                                    stmt_systemDetails = con.createStatement();
//                                    rs_systemDetails = stmt_systemDetails.executeQuery("SELECT * FROM `5_system_details_table` where system_id='" + rs_HardwareTable.getString(15) + "'");
//                                    if (rs_systemDetails.next()) {
//                                        stmt_os = con.createStatement();
//                                        rs_os = stmt_os.executeQuery("SELECT * FROM `15_os_list_table` where os_id='" + rs_systemDetails.getString(6) + "'");
//                                        if (rs_os.next()) {
//                                            OS = rs_os.getString(2);
//                                        }
//
//                                        SystemDescrib_RequestFor = rs_systemDetails.getString(5);
//
//                                        stmt_hard_type = con.createStatement();
//                                        rs_hard_type = stmt_hard_type.executeQuery("SELECT * FROM `6_hardware_type_table` where hardware_type_id='" + rs_systemDetails.getString(3) + "'");
//                                        if (rs_hard_type.next()) {
//                                            SystemType_requestFor = rs_hard_type.getString(2);
//                                        }
//                                    }
//
//                                    pojoSoft.setRequestForType("System");
//
//                                }
//
//                                pojoSoft.setRequestType(hard_reqType);
//                                pojoSoft.setHardwareType(hardType);
//                                pojoSoft.setHardwareDesc(hardDescrib);
//                                pojoSoft.setSystemType(SystemType_requestFor);
//                                pojoSoft.setSystemDescrip(SystemDescrib_RequestFor);
//                                pojoSoft.setSystemOS(OS);
//// set data in pojo class
//                            } else {
//                                pojoSoft.setActionTypeMessage("Action Already Taken. Please login to your SHMS account for more information.");
//                            }
//                        }
//                    } else {
//                        pojoSoft.setActionTypeMessage("Request Not Found");
//                    }
//                } else {
//                    pojoSoft.setActionTypeMessage("Access Denied!!!");
//                }
////                }
////                else
////                {
////                    pojoSoft.setActionTypeMessage("Access Denied!!!");
////                }
//            } else {
//                pojoSoft.setActionTypeMessage("Access Denied!!!");
//            }
//
//            Data.add(pojoSoft);
//            Gson gson = new Gson();
//            JsonElement element = gson.toJsonTree(Data, new TypeToken<List<PojoSoftReq>>() {
//            }.getType());
//            JsonArray jsonArray = element.getAsJsonArray();
//            response.setContentType("application/json");
//            response.getWriter().print(jsonArray);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (con != null) {
//                    con.close();
//                }
//                if (stmt_loginCheck != null) {
//                    stmt_loginCheck.close();
//                }
//                if (stmt_hard_type != null) {
//                    stmt_hard_type.close();
//                }
//                if (stmt_req_type != null) {
//                    stmt_req_type.close();
//                }
//                if (stmt_SoftwareTable != null) {
//                    stmt_SoftwareTable.close();
//                }
//                if (stmt_TeamDetail != null) {
//                    stmt_TeamDetail.close();
//                }
//                if (stmt_useDetail != null) {
//                    stmt_useDetail.close();
//                }
//                if (stmt_HardwareTable != null) {
//                    stmt_HardwareTable.close();
//                }
//                if (stmt_ManagerDetail != null) {
//                    stmt_ManagerDetail.close();
//                }
//                if (stmt_systemDetails != null) {
//                    stmt_systemDetails.close();
//                }
//                if (stmt_os != null) {
//                    stmt_os.close();
//                }
//                if (stmt_ReqPendingID != null) {
//                    stmt_ReqPendingID.close();
//                }
//                if (stmt_Details != null) {
//                    stmt_Details.close();
//                }
//                if (stmt_managerID != null) {
//                    stmt_managerID.close();
//                }
//                if (stmt_checkAdminStatus != null) {
//                    stmt_checkAdminStatus.close();
//                }
//                if (stmt_managerActionStatus != null) {
//                    stmt_managerActionStatus.close();
//                }
//                if (rs_managerActionStatus != null) {
//                    rs_managerActionStatus.close();
//                }
//                if (rs_loginCheck != null) {
//                    rs_loginCheck.close();
//                }
//                if (rs_systemDetails != null) {
//                    rs_systemDetails.close();
//                }
//                if (rs_req_type != null) {
//                    rs_req_type.close();
//                }
//                if (rs_ReqPendingID != null) {
//                    rs_ReqPendingID.close();
//                }
//                if (rs_hard_type != null) {
//                    rs_hard_type.close();
//                }
//                if (rs_ManagerDetail != null) {
//                    rs_ManagerDetail.close();
//                }
//                if (rs_useDetail != null) {
//                    rs_useDetail.close();
//                }
//                if (rs_os != null) {
//                    rs_os.close();
//                }
//                if (rs_Details != null) {
//                    rs_Details.close();
//                }
//                if (rs_checkAdminStatus != null) {
//                    rs_checkAdminStatus.close();
//                }
//                if (rs_TeamDetail != null) {
//                    rs_TeamDetail.close();
//                }
//                if (rs_SoftwareTable != null) {
//                    rs_SoftwareTable.close();
//                }
//                if (rs_HardwareTable != null) {
//                    rs_HardwareTable.close();
//                }
//                if (rs_managerID != null) {
//                    rs_managerID.close();
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
