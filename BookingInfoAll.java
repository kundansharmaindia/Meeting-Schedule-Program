/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kundan.app.db.connectionCredentials;
import static com.mchange.v2.c3p0.impl.C3P0Defaults.user;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import static oracle.sql.NUMBER.e;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author kundan
 */

@WebServlet("/BookingInfoAll")
public class BookingInfoAll extends HttpServlet {

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
            out.println("<title>Servlet RoomData</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoomData at " + request.getContextPath() + "</h1>");
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
       
//        System.out.print("hello room booking ");
        MeetingRoomBooking room=new MeetingRoomBooking();
        try {
            long stTs=Long.parseLong(request.getParameter("stTs")+"");
            long endTs=Long.parseLong(request.getParameter("endTs")+"");
  
            JSONArray jr = room.getBookingInfoDateWise(stTs, endTs);     
           
            
             response.setContentType("application/json");
             response.getWriter().print(jr);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(RoomData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
      
        
        
//        processRequest(request, response);
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
        
PrintWriter out=response.getWriter(); 
               try {
                        Connection con = null;
                        Statement stmt = null;  
       
String city=request.getParameter("city");
String room_id=request.getParameter("room_id");
String creation_ts="hello";
String meeting_start_ts=request.getParameter("meeting_start_ts");
String meeting_end_ts=request.getParameter("meeting_end_ts");
String email_notification=request.getParameter("email_notification");
//String participate_email[]=request.getParameterValues("participate_email[]");
String participate_email=request.getParameter("participate_email");
String booker_id="bk1";
String booker_name="sumit";
String booker_email="kk@lear.com";
String user_type="manager";
String country="india";
int status=1;
String update_ts="hello";
String room_name="roomnae";
 System.out.println("result here: "+city);
         System.out.println("result here: "+room_id);
          System.out.println("result here: "+creation_ts);
           System.out.println("result here: "+meeting_start_ts);
            System.out.println("result here: "+meeting_end_ts);
             System.out.println("result here: "+email_notification);
             System.out.println("result here part: "+participate_email);

            List<String> list = new ArrayList<String>();
            String json = null;
            

            
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
                int i = stmt.executeUpdate("insert into `meeting_room_booking_info` (`booker_id`,`booker_name`,`booker_email`,`creation_ts`,`meeting_start_ts`,`meeting_end_ts`,`user_type`,`email_notification`,`participate_email`,`room_name`,`room_id`,`country`,`status`,`update_ts`,`city`) VALUES ('" +booker_id+ "','" + booker_name+"','" + booker_email + "','" + creation_ts+ "','" + meeting_start_ts + "','" + meeting_end_ts + "','" + user_type + "','" + email_notification + "','" + participate_email+  "','" +room_name+ "','" + room_id + "','" + country + "','" + status + "','" + update_ts + "','" + city + "')");
//            int i=stmt.executeUpdate("INSERT INTO `meeting_room_booking_info` (`booker_id`, `booker_name`, `booker_email`, `creation_ts`, `meeting_start_ts`, `meeting_end_ts`, `user_type`, `email_notification`, `participate_email`, `room_name`, `room_id`, `country`, `status`, `update_ts`, `city`) VALUES ( 'b2', 'vijay', 'vij@lear.com', '2017-12-13', '1293014332', '1924166332', 'f', '1', 'CloudTeam', 'Testlab', '3', 'India', '1', '1', 'pune');");
//            int i = stmt.executeUpdate("INSERT INTO `skills`(`name`) VALUES ('" + city + "')");
                              
                    if(i>0)                
out.println("Inserted Successfully");              
else                
out.println("Insert Unsuccessful");        
}        
catch(Exception e)        
{          out.println(e);               
 }
        
    
           
             
             
             
             
             
             
//             for (String values : participate_email)
//             {
//                 System.out.println("values pt: "+values);
//             }

//        JSONObject jObj    = new JSONObject();
//        try {
//            JSONObject crInsertJson = jObj.getJSONObject(request.getParameter("result"));
//            System.out.println("data "+crInsertJson);
//          
//            Iterator iterKey = crInsertJson.keys(); // create the iterator for the json object.
//            while(iterKey.hasNext()) {
//                String jsonKey = (String)iterKey.next(); //retrieve every key ex: name, age
//                String jsonValue = crInsertJson.getString(jsonKey); //use key to retrieve value from 
//
//                //This is a json object and will display the key value pair.
//
//                System.out.println(jsonKey  + " --> " + jsonValue  );
//            }
//            
////        System.out.print("result"+city);
//
//            
////        processRequest(request, response);
//        } catch (JSONException ex) {
//            Logger.getLogger(BookingInfoAll.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        } catch (JSONException ex) {
//            Logger.getLogger(BookingInfoAll.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
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
