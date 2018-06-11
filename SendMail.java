
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.kundan.app.db.connectionCredentials;
import java.util.ArrayList;

public class SendMail {
 
    static String hostEmailID = "confroom@aradasystems.com";
    static String hostPassword = "Jg&kBaeokZ02";
    static String host = "ns1.arada.interactivedns.com";
    static String socketFactory_port = "465";
    static String socketFactory_class = "javax.net.ssl.SSLSocketFactory";
    static String auth = "true";
    static String port = "465";
//    static String Footer = "Click on the link to login to SHMS tool http://180.151.199.236:8080/shms\n"
//            + "\n"
//            + "\n"
//            + "Thanks and Regards,\n"
//            + "Help Desk";
//
//    static String footer_link_Only = "Click on the link to login to SHMS tool http://180.151.199.236:8080/shms\n";
//    static String URL_ForManagerMail_part1 = "http://180.151.199.236:8080/shms/shmsRequestMailAction.jsp";


    /**
     * Method used to send mail to the user and also selected participants while booking room.
     * 
     * @param msg
     * @param subject
     * @param to_email
     * @param CC_get
     * @return 
     */
    public  Integer sendMessage(String msg, String subject, String to_email, ArrayList<String> CC_get) {
        Integer result =0;
        try {

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.socketFactory.port", socketFactory_port);
            props.put("mail.smtp.socketFactory.class", socketFactory_class);
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.port", port);

            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(hostEmailID, hostPassword);
                }
            });
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(hostEmailID));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
            
            for (String cc_mail : CC_get) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc_mail)); // cc
            }
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
            result =2;
        }
        return result;
    }
    
    /**
     * 
     * Method used to send e-mail only to the user. This method will be used on 
     * Forgot password and Change password.
     * 
     * @param to_email
     * @param subject
     * @param msg
     * @return 
     */
    public  Integer sendMessageForPwdSettings(String to_email, String subject, String msg) {
        Integer result =0;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.socketFactory.port", socketFactory_port);
            props.put("mail.smtp.socketFactory.class", socketFactory_class);
            props.put("mail.smtp.auth", auth);
            props.put("mail.smtp.port", port);

            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(hostEmailID, hostPassword);
                }
            });
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(hostEmailID));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
            
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
            result =2;
        }
        return result;
    }
    
}
