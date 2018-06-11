/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.kundan.app.db.connectionCredentials;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kundan
 */
public class crbookingParticipants extends HttpServlet {

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
            out.println("<title>Servlet crbookingParticipants</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet crbookingParticipants at " + request.getContextPath() + "</h1>");
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

//            String participant = request.getParameter("term");
//            System.out.println("pass= "+participant);
//            System.out.println("pass= ");
        Connection con = null;
        Statement crStmt = null;

        ResultSet userDetailResult = null;

        try {
            String ServerPath = connectionCredentials.serverAddress();
            String ServerLoginID = connectionCredentials.ServerID();
            String ServerPassword = connectionCredentials.ServerPassword();
            String DBDrivers = connectionCredentials.DBDriverName();

            Class.forName(DBDrivers);

            con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
            HttpSession ses = request.getSession();

            String userSerial = request.getParameter("uno");

            //participant = participant + "%";
            String json = null;
            
            crStmt = con.createStatement();
            
            HashMap<String, ArrayList<String>> userDetailsMap = new HashMap<>();
            HashMap<String, ArrayList<String>> teamMembersMap = new HashMap<>();
            
            ArrayList<String> usersList = new ArrayList<>();
            
            //To fetch all user details except logged-in user and admin
            userDetailResult = crStmt.executeQuery("SELECT user_serial_no,emp_name, email_id from 1_user_detail WHERE NOT user_serial_no=0 and NOT user_serial_no="+userSerial);
            
            //Iterating resultSet and creating 'userDetailsMap' hashmap with user_serial_no, name and email id.
            while(userDetailResult.next()){
                usersList = new ArrayList<>();
                String userSerialNo = userDetailResult.getString(1);
                usersList.add(userSerialNo);
                usersList.add(userDetailResult.getString(2));
                usersList.add(userDetailResult.getString(3));
                userDetailsMap.put(userSerialNo, usersList);
            }
            
            //To get the team id for the signed-in user.
            userDetailResult = crStmt.executeQuery("SELECT team_id FROM 19_user_team where user_serial_no="+userSerial);
            
            //Signed-in user may be related to one or more groups.So, iterated the resultSet and added to 'teamIdList' arraylist.
            ArrayList<String> teamIdList = new ArrayList<>();
            while(userDetailResult.next()){
                teamIdList.add(userDetailResult.getString(1));
            }
            
            //Iterating the signed-in user teamIdList and getting the own team members id.
            ArrayList<String> teamMembersList = new ArrayList<>();
            for (int i = 0; i < teamIdList.size(); i++) {
                String teamId = teamIdList.get(i);
                userDetailResult = crStmt.executeQuery("SELECT user_serial_no FROM 19_user_team WHERE team_id=" + teamId );
                while(userDetailResult.next()){
                    teamMembersList.add(userDetailResult.getString(1));
                }
                teamMembersMap.put(teamId,teamMembersList);
            }
            
            Iterator<String> usersIterator = userDetailsMap.keySet().iterator();
            HashMap<String,HashMap<String,ArrayList<String>>> finalHashMap = new HashMap<>();
            HashMap<String,ArrayList<String>> teamHashMap = new HashMap<>();
            HashMap<String,ArrayList<String>> othersHashMap = new HashMap<>();
            
            //Iterating all userDetails hashmap to find their own team members and other members
            while (usersIterator.hasNext()) {
                String key = usersIterator.next();
                
                //Condition to add the user details into separate hashmap for team members and other members.
                if (teamMembersList.contains(key)) {
                    teamHashMap.put(key, userDetailsMap.get(key));
                } else {
                    othersHashMap.put(key, userDetailsMap.get(key));
                }
            }
            
            //Adding the 2 different hashmaps into single hashmap to convert to JSON and send as response
            finalHashMap.put("team", teamHashMap);
            finalHashMap.put("others", othersHashMap);
            
            json = new Gson().toJson(finalHashMap);
            response.setContentType("application/json");
            response.getWriter().write(json);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Req_form_team.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Req_form_team.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (crStmt != null) {
                    crStmt.close();
                }

                if (userDetailResult != null) {
                    userDetailResult.close();
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
        
        
        
        String participant = request.getParameter("term");
        
        
        
        
//        processRequest(request, response);
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
