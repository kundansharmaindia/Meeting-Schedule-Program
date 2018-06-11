
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.kundan.app.db.connectionCredentials;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kundan
 */

/*

 * ID starting from 111 to 199 define for Creation or registration or new request
 * ID starting from 211 to 299 define for Deletion or Remove operation
 * ID starting from 311 to 399 define for Assign any hardware or software or team
 * ID starting from 411 to 499 define for Roles and designation or edit of any user value
 * ID starting from 511 to 599 define for Teams or user have no manager 

 */
public class HistoryTableClass {

    public int historyData(int id, String Value1, String User, String Remark) // for one arguments
    {
//        java.util.Date dt = new java.util.Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
//                String date_request = sdf.format(dt);

        Connection con = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        int i = 0;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            stmt2 = con.createStatement();
//            String Action="";
//            for(int a=0;a<id.length;a++)
//            {
            rs = stmt.executeQuery("select * from `25_history_strings` where StringID='" + id + "'");
            if (rs.next()) {
                String result = String.format(rs.getString(2), Value1);
                String Action = rs.getString(4);
                i = stmt2.executeUpdate("insert into `16_history_table` (User,ActionType,Data,Remark) values ('" + User + "','" + Action + "','" + result + "','" + Remark + "')");
            }
//            }
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
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return i;
    }

    public int historyData(int id, String Value1, String Value2, String User, String Remark) // for two arguments
    {
//        java.util.Date dt = new java.util.Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
//                String date_request = sdf.format(dt);

        Connection con = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        int i = 0;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            stmt2 = con.createStatement();
//            String Action="";
//            for(int a=0;a<id.length;a++)
//            {
            rs = stmt.executeQuery("select * from `25_history_strings` where StringID='" + id + "'");
            if (rs.next()) {
                String result = String.format(rs.getString(2), Value1, Value2);
                String Action = rs.getString(4);
                i = stmt2.executeUpdate("insert into `16_history_table` (User,ActionType,Data,Remark) values ('" + User + "','" + Action + "','" + result + "','" + Remark + "')");
            }
//            }
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
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return i;
    }

    // for 3 arguments
    public int historyData(int id, String Value1, String Value2, String Value3, String User, String Remark) // for 3 arguments
    {
//        java.util.Date dt = new java.util.Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
//                String date_request = sdf.format(dt);

        Connection con = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        int i = 0;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            stmt2 = con.createStatement();
//            for(int a=0;a<id.length;a++)
//            {
            rs = stmt.executeQuery("select * from `25_history_strings` where StringID='" + id + "'");
            if (rs.next()) {
                String result = String.format(rs.getString(2), Value1, Value2, Value3);
                String Action = rs.getString(4);
                i = stmt2.executeUpdate("insert into `16_history_table` (User,ActionType,Data,Remark) values ('" + User + "','" + Action + "','" + result + "','" + Remark + "')");
//                }
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
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return i;
    }

    public int historyData(int id, String Value1, String Value2, String Value3, String value4, String User, String Remark) // for 4 arguments
    {
//        java.util.Date dt = new java.util.Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
//                String date_request = sdf.format(dt);

        Connection con = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        int i = 0;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            stmt2 = con.createStatement();
//            for(int a=0;a<id.length;a++)
//            {
            rs = stmt.executeQuery("select * from `25_history_strings` where StringID='" + id + "'");
            if (rs.next()) {
                String result = String.format(rs.getString(2), Value1, Value2, Value3, value4);
                String Action = rs.getString(4);
                i = stmt2.executeUpdate("insert into `16_history_table` (User,ActionType,Data,Remark) values ('" + User + "','" + Action + "','" + result + "','" + Remark + "')");
//                }
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
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return i;
    }

}
