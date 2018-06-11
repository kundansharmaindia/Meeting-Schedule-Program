
import com.kundan.app.db.connectionCredentials;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONException;
import org.codehaus.jettison.json.JSONArray;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import javax.xml.crypto.Data;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author kundan
 */
public class MeetingRoomBooking {

    public static void main(String args[]) throws org.codehaus.jettison.json.JSONException, SQLException {
        MeetingRoomBooking mrb = new MeetingRoomBooking();
//        System.out.println(mrb.getBookingInfoDateWise(1,8).toString());

        JSONObject jobj = new JSONObject();
        jobj.put("booker_id", "vijay");
        jobj.put("booker_name", "vijay kumar");
        jobj.put("booker_email", "vijay@lear,com");
        jobj.put("meeting_start_ts", 9);
        jobj.put("meeting_end_ts", 10);
        jobj.put("status", 1);
        jobj.put("room_id", "BLR1");
//        System.out.println(mrb.createNewBooking(jobj));

        JSONObject jobj2 = new JSONObject();
        jobj2.put("status", 0);
        jobj2.put("id", 10);
//        System.out.println(mrb.UpdateBookingInfoData(jobj2));
        
        
        System.out.println(mrb.getRoomData().toString());
        
    }
    
    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : UpdateBookingInfoData
     *
     * Description : prepare dynamic query based on the input JSONObject and
     * run to meeting_room_booking_info table.
     * 
     * id is Mandatory key from input JSONObect
     *
     * @param insertBookingObject JSONObject (id along with new updated 
     * columns data)
     *
     * @return Integer
     *      1 = Success 
     *      0 = Failure
     *      2 = Exception
     */
    public Integer UpdateBookingInfoData(Integer updateBookingId) throws SQLException {
        //Declare variable 
        Connection con = null;
        Statement stmt = null;
        Integer modifyBookingResult = 0;

        try {
            if (updateBookingId != null) {
                String ServerPath = connectionCredentials.serverAddress();
                String ServerLoginID = connectionCredentials.ServerID();
                String ServerPassword = connectionCredentials.ServerPassword();
                String DBDrivers = connectionCredentials.DBDriverName();
                Class.forName(DBDrivers);
                //Get database connection
                con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
                stmt = con.createStatement();
                long updateTs = Math.abs(System.currentTimeMillis()/1000);
                
                //final insert query is created based on forms data
                String queryStri = "UPDATE meeting_room_booking_info SET status = 0,update_ts = " +updateTs  + " WHERE id = " + updateBookingId;
                /* update in meeting_room_booking_info table and store response in variable
                 1 = Success 
                 0 = Failure
                 */
                modifyBookingResult = stmt.executeUpdate(queryStri);
            }
        } catch (Exception e) {
            e.printStackTrace();
            modifyBookingResult = 2;
        } finally {
            //Close all connections
            stmt.close();
            con.close();
        }
        //Return result
        return modifyBookingResult;
    }
    
    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : getBookingInfoDateWiseWithUser
     *
     * Description : execute query based on the start or end day timestamp and
     * booker id. return row from meeting_room_booking_info table
     *
     * return row in JSONArray Formate
     *
     * @param dayStartTimestamp day start time stamp (00:00:00) in milliseconds
     * @param dayEndTimestamp day end time stamp (23:59:59) in milliseconds
     * @param booker_id booker unique user id refer to login table
     *
     * @return JSONArray of result set from meeting_room_booking_info table.
     */
    public JSONArray getBookingInfoDateWiseWithUser(long dayStartTimestamp, long dayEndTimestamp, String booker_id) throws SQLException {
        //Declare variable 
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        JSONArray jrrReturn = null;
        //Query String preparation  to select all row those booking duration is in between start and end time
        String queryStri = "SELECT * FROM meeting_room_booking_info where meeting_start_ts >=" + dayStartTimestamp;
        queryStri += " and meeting_end_ts <=" + dayEndTimestamp + " and booker_id ='" + booker_id +"'" + "ORDER by meeting_start_ts DESC";
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            //Get database connection
            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            // Execute statement and store in resultSet 
            rs = stmt.executeQuery(queryStri);
            MeetingRoomBooking mrb = new MeetingRoomBooking();
            //result set convert to JSONArray formate
            jrrReturn = convert(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close all connections
            rs.close();
            stmt.close();
            con.close();
        }
        //Return JSONArray 
        return jrrReturn;
    }

    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : getBookingInfoDateWise
     *
     * Description : execute query based on the start or end day timestamp
     *
     * return row in JSONArray Formate
     *
     * @param dayStartTimestamp day start time stamp (00:00:00) in milliseconds
     * @param dayEndTimestamp day end time stamp (23:59:59) in milliseconds
     *
     * @return JSONArray of result set from meeting_room_booking_info table.
     */
    public JSONArray getRoomData() throws SQLException {
        //Declare variable 
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        JSONArray jrrReturn = null;
        //Query String preparation  to select all row those booking duration is in between start and end time
        String queryStri = "SELECT * FROM meeting_room_details";
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            //Get database connection
            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            // Execute statement and store in resultSet 
            rs = stmt.executeQuery(queryStri);
            jrrReturn = convert(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close all connections
            rs.close();
            stmt.close();
            con.close();
        }
        //Return JSONArray 
        return jrrReturn;
    }
    
    
    
    
    
    
    
    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : getBookingInfoDateWise
     *
     * Description : execute query based on the start or end day timestamp
     *
     * return row in JSONArray Formate
     *
     * @param dayStartTimestamp day start time stamp (00:00:00) in milliseconds
     * @param dayEndTimestamp day end time stamp (23:59:59) in milliseconds
     *
     * @return JSONArray of result set from meeting_room_booking_info table.
     */
    public JSONArray getBookingInfoDateWise(long dayStartTimestamp, long dayEndTimestamp) throws SQLException {
        //Declare variable 
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        JSONArray jrrReturn = null;
        //Query String preparation  to select all row those booking duration is in between start and end time
        String queryStri = "SELECT * FROM meeting_room_booking_info where meeting_start_ts >=" + dayStartTimestamp;
        queryStri += " and meeting_end_ts <=" + dayEndTimestamp + " and status =1 ORDER by meeting_start_ts DESC" ;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            //Get database connection
            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            // Execute statement and store in resultSet 
            rs = stmt.executeQuery(queryStri);
            MeetingRoomBooking mrb = new MeetingRoomBooking();
            //result set convert to JSONArray formate
            jrrReturn = convert(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Close all connections
            rs.close();
            stmt.close();
            con.close();
        }
        //Return JSONArray 
        return jrrReturn;
    }

    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : createNewBooking
     *
     * Description : Method first check exist entry based on meeting start time
     * and meeting end time. if no entry is available then insert new entry. if
     * entry already exits on given timestamp then new booking entry is not
     * create.
     *
     * @param insertBookingObject It is JOSN object of booking forms
     *
     * @return Integer specific value determine the success or failure of
     * booking. 1 = success 0 = time slot already book 2 = exception
     */
    public Integer createNewBooking(JSONObject insertBookingObject) throws org.codehaus.jettison.json.JSONException {
        //declare  variable
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        Integer allowBookingResult = 1;
        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            try {
                //Get database connection
                con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
                stmt = con.createStatement();
                /* Query String preparation : to check exist entry based on meeting start time and 
                 meeting end time. if no entry available then return 0 else more then 0
                 */
                String validateQueryString = "SELECT id FROM meeting_room_booking_info  where " + "(((meeting_start_ts > " + insertBookingObject.getString("meeting_start_ts") + " and meeting_start_ts < " + insertBookingObject.getString("meeting_end_ts")
                        + ") or (meeting_end_ts > " + insertBookingObject.getString("meeting_start_ts") + " and meeting_end_ts < " + insertBookingObject.getString("meeting_end_ts")
                        + ") or (meeting_start_ts < " + insertBookingObject.getString("meeting_start_ts") + " and meeting_end_ts > " + insertBookingObject.getString("meeting_end_ts") + "))or " + insertBookingObject.getString("meeting_start_ts")
                        + ">=meeting_start_ts and " + insertBookingObject.getString("meeting_end_ts") + "<= meeting_end_ts) and status=1 and room_id = '" + insertBookingObject.get("room_id") + "'";
                //Execute query and store data in result set
                System.out.println(validateQueryString);
                rs = stmt.executeQuery(validateQueryString);
                //convert result set to JSONArray and count length
                int noOfRecords = convert(rs).length();
                /*  check available entry of meeting_room_booking_info table 
                 if number of record is 0 then process to insert booking
                 */
                if (noOfRecords == 0) {
                    ResultSet rsInsert = null;
                    Connection conInsert = null;
                    Statement stmtInsert = null;
                    try {
                        //add current timestamp (millisecond) in forms data
//                        insertBookingObject.put("creation_ts", System.currentTimeMillis());
                        Iterator<?> keys = insertBookingObject.keys();
                        String keyQeryString = "";
                        String valueQueryString = "";
                        //Iterate forms data and create dynamic query to insert record
                        while (keys.hasNext()) {
                            //get key and value from forms object and convert to query
                            String key = (String) keys.next();
                            keyQeryString += "`" + key + "`";
                            
                            //To avoid adding value for 'participate_email' column
                            if(!key.equalsIgnoreCase("participate_email")){
                                valueQueryString += "'" + insertBookingObject.get(key) + "'";
                            } else {
                                valueQueryString += "''";
                            }
                        }
                        keyQeryString = keyQeryString.replace("``", "`,`");
                        valueQueryString = valueQueryString.replace("''", "','");
                        //final insert query is created based on forms data
                        String queryStri = "INSERT INTO meeting_room_booking_info (" + keyQeryString + ") VALUES (" + valueQueryString + ")";
                        /* insert in meeting_room_booking_info table and store response in variable
                         1 = Success 
                         0 = Failure
                         */
                        allowBookingResult = stmt.executeUpdate(queryStri);
                    } catch (Exception e) {
                        e.printStackTrace();
                        allowBookingResult = 2;
                    }
                }else{
                    allowBookingResult = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                allowBookingResult = 2;
            } finally {
                rs.close();
                stmt.close();
                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            allowBookingResult = 2;
        }
        return allowBookingResult;
    }

    /**
     * @author PNiraj
     * @version 1.0
     * @since 27-06-2017
     *
     * Function : convert
     *
     * Description : Method check result SET metaData and based on the metaData
     * convert to JSONArray and return.
     *
     * @param rs Database ResultSet object
     *
     * @return JSONArray converted value from result set
     */
    public JSONArray convert(ResultSet rs)
            throws SQLException, JSONException, org.codehaus.jettison.json.JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();

        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnName(i);

                if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rs.getArray(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rs.getLong(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rs.getBlob(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                    obj.put(column_name, rs.getString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                    obj.put(column_name, rs.getDate(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rs.getTimestamp(column_name));
                } else {
                    obj.put(column_name, rs.getObject(column_name));
                }
            }

            json.put(obj);
        }

        return json;
    }
    
    /**
     * 
     * Method to reset the 'status' column in 'meeting_room_booking_info' table to '2',
     * if meeting time was expired.
     * 
     * @param updateBookingId
     * @return
     * @throws SQLException 
     */
    public Integer UpdateMeetingStatus() throws SQLException {
        //Declare variable 
        Connection con = null;
        Statement stmt = null;
        Integer updateMeetingStatus = 0;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            //Get database connection
            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            long updateTs = Math.abs(System.currentTimeMillis() / 1000);
            
            //status = 2 - represents meeting time was expired
            String queryStr = "UPDATE meeting_room_booking_info SET status = 2 WHERE status=1 and " + updateTs + " >= meeting_end_ts";
            updateMeetingStatus = stmt.executeUpdate(queryStr);
        } catch (Exception e) {
            e.printStackTrace();
            updateMeetingStatus = 2;
        } finally {
            //Close all connections
            stmt.close();
            con.close();
        }
        return updateMeetingStatus;
    }
    
    /**
     * Method to send notification email to participants via /sendNotificationEmail api
     * 
     * @param insertBookingObject - JSON data object received via api's POST data
     * @return
     * @throws SQLException 
     */
    public Integer sendEmail(JSONObject insertBookingObject) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        Integer mailSentStatus = 0;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();
            Class.forName(DBDrivers);
            //Get database connection
            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            stmt = con.createStatement();
            //long updateTs = Math.abs(System.currentTimeMillis() / 1000);

            String bookerEmail = insertBookingObject.getString("booker_email");
            ArrayList ccEmail = new ArrayList();
            if (insertBookingObject.getString("email_notification") != null && insertBookingObject.getString("email_notification").equals("1")) {
                String participantEmail = insertBookingObject.getString("participate_email");
                if (!participantEmail.equals("")) {
                    String toEmailString[] = participantEmail.split(",");
                    ccEmail = new ArrayList(Arrays.asList(toEmailString));
                }
            }
            Date metStTs = new Date(Long.parseLong(insertBookingObject.getString("meeting_start_ts")) * 1000);
            Date metEndTs = new Date(Long.parseLong(insertBookingObject.getString("meeting_end_ts")) * 1000);
            //String meetingSt = new SimpleDateFormat("dd.MM.yyyy HH.mm").format(metStTs);
            //String meetingEnd = new SimpleDateFormat("dd.MM.yyyy HH.mm").format(metEndTs);

            String meetingDate = new SimpleDateFormat("dd/MM/yyyy").format(metStTs);
            String startTime = new SimpleDateFormat("HH:mm").format(metStTs);
            String endTime = new SimpleDateFormat("HH:mm").format(metEndTs);
            
            String htmlLayout="<div style='background:#337ab7;width:80%;padding:10px'><b style='color:white'>Meeting Schedule</b></div>";
            String line1 = "Meeting has been scheduled by " + insertBookingObject.getString("booker_name") + " ( E-Mail ID: " + insertBookingObject.getString("booker_email") + " ).";
            String line2 = "Date : " + meetingDate;
            String line3 = "Start Time : " + startTime;
            String line4 = "End Time : " + endTime;
            String line5 = "Venue : " + insertBookingObject.getString("room_name");
            String msg = htmlLayout+"\n\n"+line1 + "\n\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5;

            SendMail sm = new SendMail();
            mailSentStatus = sm.sendMessage(msg, "Conference Room Booking", bookerEmail, ccEmail);

        } catch (Exception e) {
            e.printStackTrace();
            mailSentStatus = 2;
        } finally {
            //Close all connections
            stmt.close();
            con.close();
        }
        return mailSentStatus;
    }
    
}
