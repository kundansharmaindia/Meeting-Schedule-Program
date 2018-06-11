
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
public class emailStrings {

    public static int SetMessage(int id, String clentEmail, String userName, String email, String pass) {

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

            rs = stmt.executeQuery("select * from `26_email_strings` where ID='" + id + "'");
            if (rs.next()) {
                String result = String.format(rs.getString(3), userName, email, pass);
                String subject = rs.getString(2);

                String CCNo = rs.getString(4);
//                
//                if(CCNo.equals("0"))
//                {
//                    i=SendMail.sendMessageWithoutCC(clentEmail, result, subject);
//                }
//                else
//                {
//                    i = SendMail.sendMessage(clentEmail, result, subject,cc1);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
