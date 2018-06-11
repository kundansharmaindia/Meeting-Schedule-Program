
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import com.kundan.app.db.connectionCredentials;

public class Req_form_team extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        Connection con = null;
        Statement stmt301 = null;
        Statement stmt_maanger = null;
        Statement stm_soft_detail = null;
        Statement stmt_os = null;
        Statement stmt302 = null;
        ResultSet rs3 = null;
        ResultSet rs_Manger = null;
        ResultSet rs_soft_det = null;
        ResultSet rs_os = null;
        ResultSet rs4 = null;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            HttpSession ses = request.getSession();
            String team_id = request.getParameter("teamNames");
            String soft_name = request.getParameter("soft_name");
            if (soft_name == null) {
                List<String> list = new ArrayList<String>();
                //List<String> list2 = new ArrayList<String>();
                String json = null;

                stmt301 = con.createStatement();
                stmt302 = con.createStatement();

                rs3 = stmt301.executeQuery("SELECT * FROM `19_user_team` where user_serial_no='" + ses.getAttribute("user_serial_no") + "' and team_id='" + team_id + "'");
                rs4 = stmt302.executeQuery("SELECT * FROM `2_team_table` where team_id='" + team_id + "'");

                while (rs3.next()) {
                    stmt_maanger = con.createStatement();
                    rs_Manger = stmt_maanger.executeQuery("SELECT * FROM `20_manager_table` where manager_id='" + rs3.getString(4) + "' and manager_status='Available'");
                    if (rs_Manger.next()) {
                        list.add(rs_Manger.getString(2));
                    }
                    else
                    {
                        list.add("No Manager Found");
                    }
                }

                if (rs4.next()) {
                    list.add(rs4.getString(2));
                }

                json = new Gson().toJson(list);
                response.setContentType("application/json");
                response.getWriter().write(json);
            } else {
                stm_soft_detail = con.createStatement();
                List<String> list_soft = new ArrayList<String>();
                String os = "";

                rs_soft_det = stm_soft_detail.executeQuery("SELECT sub_category,version,os_id FROM `4_software_detail_table` where soft_name='" + soft_name + "'");
                while (rs_soft_det.next()) {
                    stmt_os = con.createStatement();
                    rs_os = stmt_os.executeQuery("SELECT * FROM `project_lear`.`15_os_list_table` WHERE `os_id` = '" + rs_soft_det.getString(3) + "'");
                    while (rs_os.next()) {
                        os = rs_os.getString(2);
                    }
                    list_soft.add(rs_soft_det.getString(1));
                    list_soft.add(rs_soft_det.getString(2));
                    list_soft.add(os);
                }
                String json3 = null;
                json3 = new Gson().toJson(list_soft);
                response.setContentType("application/json");
                response.getWriter().write(json3);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Req_form_team.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Req_form_team.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stmt301 != null) {
                    stmt301.close();
                }
                if (stmt_maanger != null) {
                    stmt_maanger.close();
                }
                if (stm_soft_detail != null) {
                    stm_soft_detail.close();
                }
                if (stmt_os != null) {
                    stmt_os.close();
                }
                if (stmt302 != null) {
                    stmt302.close();
                }
                if (rs3 != null) {
                    rs3.close();
                }
                if (rs_Manger != null) {
                    rs_Manger.close();
                }
                if (rs_soft_det != null) {
                    rs_soft_det.close();
                }
                if (rs_os != null) {
                    rs_os.close();
                }
                if (rs4 != null) {
                    rs4.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FetchPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection con = null;
        Statement stmt_soft_name = null;
        Statement stmt_system = null;
        Statement stmt_hard_type = null;
        Statement stmt_os = null;
        ResultSet rs_soft = null;
        ResultSet rs_system = null;
        ResultSet rs_hard_type = null;
        ResultSet rs_os = null;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            String soft_cata = request.getParameter("softCategory");
            String json = "";
            String system_id = request.getParameter("system_serial");
            String request_type = request.getParameter("req_type");
            String system_id_for_software = request.getParameter("system_id_for_software"); //this system id is for checking if the software is install or uninstall in perticular system or not depend on request type(install or uninstall)

            //if id== null, means for software category return software name
            if (system_id == null) {
                List<String> list = new ArrayList<String>();
                stmt_soft_name = con.createStatement();
//                stmt_system_OS=con.createStatement();
//                String system_os="";
//                rs_system_OS=stmt_system_OS.executeQuery("select * from `5_system_details_table` where system_id='"+system_id_for_software+"'");
//                if(rs_system_OS.next())
//                {
//                    system_os=rs_system_OS.getString(6);
//                }
                if (request_type.equals("Install")) {
                    rs_soft = stmt_soft_name.executeQuery("SELECT DISTINCT(soft_name) FROM `4_software_detail_table` WHERE category='" + soft_cata + "' and soft_id NOT IN (select soft_id from 7_software_installed_detail where system_id='" + system_id_for_software + "') and os_id = (select os_id FROM 5_system_details_table where system_id='" + system_id_for_software + "')");
                } else if (request_type.equals("Uninstall")) {
                    rs_soft = stmt_soft_name.executeQuery("SELECT DISTINCT(soft_name) FROM `4_software_detail_table` WHERE category='" + soft_cata + "' and soft_id IN (select soft_id from 7_software_installed_detail where system_id='" + system_id_for_software + "') and os_id = (select os_id FROM 5_system_details_table where system_id='" + system_id_for_software + "')");
                } else if (request_type.equals("Upgrade")) {
                    rs_soft = stmt_soft_name.executeQuery("SELECT DISTINCT(soft_name) FROM `4_software_detail_table` WHERE category='" + soft_cata + "' and soft_id IN (select soft_id from 7_software_installed_detail where system_id='" + system_id_for_software + "') and os_id = (select os_id FROM 5_system_details_table where system_id='" + system_id_for_software + "')");
                }

//                 rs_soft=stmt_soft_name.executeQuery("SELECT DISTINCT(soft_name) FROM `4_software_detail_table` WHERE category='"+soft_cata+"'");
                while (rs_soft.next()) {
                    list.add(rs_soft.getString(1));

                }
                list.add(0, "Select Software");
                json = new Gson().toJson(list);
                response.setContentType("application/json");
                response.getWriter().write(json);
            } //else if system id != null means this code is for system and return os, type and description of system
            else {
                List<String> list2 = new ArrayList<String>();
                String hardware_type = "", os = "";
                stmt_system = con.createStatement();
                rs_system = stmt_system.executeQuery("SELECT hardware_type_id,os_id,description,hardware_id FROM `5_system_details_table` where system_id='" + system_id + "'");
                while (rs_system.next()) {
                    stmt_hard_type = con.createStatement();
                    rs_hard_type = stmt_hard_type.executeQuery("SELECT type FROM `project_lear`.`6_hardware_type_table` WHERE `hardware_type_id` = '" + rs_system.getString(1) + "'");
                    while (rs_hard_type.next()) {
                        hardware_type = rs_hard_type.getString(1);
                    }
                    stmt_os = con.createStatement();
                    rs_os = stmt_os.executeQuery("SELECT os_name,version FROM `project_lear`.`15_os_list_table` WHERE `os_id` = '" + rs_system.getString(2) + "'");
                    while (rs_os.next()) {
                        os = rs_os.getString(1) + "/" + rs_os.getString(2);
                    }
                    list2.add(hardware_type);
                    list2.add(os);
                    list2.add(rs_system.getString(3));
                    list2.add(rs_system.getString(4));
                }
                String json2 = null;
                json2 = new Gson().toJson(list2);
                response.setContentType("application/json");
                response.getWriter().write(json2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (stmt_soft_name != null) {
                    stmt_soft_name.close();
                }
                if (stmt_system != null) {
                    stmt_system.close();
                }
                if (stmt_hard_type != null) {
                    stmt_hard_type.close();
                }
                if (stmt_os != null) {
                    stmt_os.close();
                }
                if (rs_soft != null) {
                    rs_soft.close();
                }
                if (rs_system != null) {
                    rs_system.close();
                }
                if (rs_hard_type != null) {
                    rs_hard_type.close();
                }
                if (rs_os != null) {
                    rs_os.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FetchPdf.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }
}
