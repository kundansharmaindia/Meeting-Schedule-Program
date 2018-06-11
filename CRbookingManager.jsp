<%-- 
    Document   : CRbooking
    Created on : Sep 10, 2017, 1:56:23 PM
    Author     : kundan
--%>
<%@page import="java.sql.*"%>
<%@page import="com.lear.app.db.connectionCredentials"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width" />
        <title>Conference Room Booking System</title>

        <link rel="shortcut icon" href="int-lib/images/lear-icon.png" />
        <link href="ext-lib/jquery/css/timepicker.min.css" rel="stylesheet"/>
        <link href="ext-lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
       
        <script type="text/javascript" src="ext-lib/jquery/js/jquery.js"></script>
        <script type="text/javascript" src="ext-lib/bootstrap/js/bootstrap.min.js"></script>
        <link href="int-lib/css/style.css" rel="stylesheet"/>
        <script type="text/javascript" src="ext-lib/jquery/js/jquery-timepicker.min.js"></script>


        <%
            response.setHeader("Cache-Control", "no-cache");

            //Forces caches to obtain a new copy of the page from the origin server
            response.setHeader("Cache-Control", "no-store");

            //Directs caches not to store the page under any circumstance
            response.setDateHeader("Expires", 0);

            //Causes the proxy cache to see the page as "stale"
            response.setHeader("Pragma", "no-cache");
            //HTTP 1.0 backward enter code
            //request.setAttribute("Error", "Session has ended.  Please logenter code herein.");
            //out.println("Session has ended.  Please login.");
            String designation = (String) session.getAttribute("Designation");
            if (null == designation) {
                //request.setAttribute("Error", "Session has ended.  Please logenter code herein.");
                //            JOptionPane.showMessageDialog(null, "please login first");
                response.sendRedirect("login.jsp");
            } else {

//                System.out.println("designation is : = "+designation);
//                if (designation.equals("Manager")) {
        %>

        <%//           
            Connection con = null;
            Statement stmt_hw_type = null;
            Statement stmt_hw_brand = null;
            Statement stmt_hw_emp = null;
            Statement stmt_hw_warr_type = null;
            ResultSet rs_hw_type = null;
            ResultSet rs_hw_brand = null;
            ResultSet rs_hw_emp = null;
            ResultSet rs_hw_warr_type = null;

            try {
                String ServerPath = connectionCredentials.serverAddress();
                String ServerLoginID = connectionCredentials.ServerID();
                String ServerPassword = connectionCredentials.ServerPassword();
                String DBDrivers = connectionCredentials.DBDriverName();

                Class.forName(DBDrivers);

                con = DriverManager.getConnection(ServerPath, ServerLoginID, ServerPassword);
                String user_serial_no = (String) session.getAttribute("user_serial_no");
                String emp_name = (String) (session.getAttribute("emp_name").toString());
                String emp_email = (String) (session.getAttribute("email_id").toString());

//                System.out.println("after login name = "+user_serial_no);
//                String booker_id = emp_name.substring(0, 3);
//                booker_id = "BKCR" + booker_id;
%>
    </head>

    <body>
        <div id="loading" class="modal">
            <span class="loader"></span>
        </div>
        <!--To display the loading image before all the elements loaded-->
        <script> $("#loading").show(); </script>
        
        <nav class="navbar navbar-inverse">
            <img class="navbar-left img-responsive" src="int-lib/images/lear-icon.png" style="width: 50px;"/>
            <!--<div class="container-fluid">-->
            <div class="navbar-header">
                <span class="navbar-brand" style="color:white;">Conference Room Booking System</span>
            </div>
            <ul class="nav navbar-nav" style="float:right;">
                <li><a href="#">Hi! <%= emp_name%></a></li>
                <li><a href="#" data-toggle="modal" data-target="#changePwdModal">Change Password</a></li>
                <li><a href="#" onclick="logout()">Logout &nbsp;&nbsp;<span class="glyphicon glyphicon-log-out"></span></a></li>
            </ul>
        </nav>
        <span id="userSerialNo" style="display:none;"><%= user_serial_no%></span>
        <div class="container-fluid sideMargin">
            <div id="containerRow" class="row">
                <div class="col-md-3" id="leftSection">
                    <!--<form id="bookingForm" class="form-horizontal" name="bookingForm" method="post" action="BookingInfoAll">-->
                    <div class="form-group">
                        <label for="city">Select City</label>
                        <select class="form-control" name="city" id="city" onchange="onSelectRoomid(this)" >
                            <option>Select City</option>
                        </select>
                        
                        <label for="room_id" class="gap">Select Room 
                            <span id="roomInfoIcon" class="glyphicon glyphicon-info-sign" title="Select Room ID and See Room Info." style="cursor:pointer;margin-left: 10px;color:transparent;border-radius: 50%;" onclick="get_room_data()"></span>
                        </label>
                        <!--<select class="form-control" name="room_id" id="room_id" onchange="get_room_data()">-->
                        <select class="form-control" name="room_id" id="room_id">
                            <option >Select Room</option>
                        </select>

                        <label for="bdate" class="gap">Select Booking Date</label>
                        <div class="inner-addon right-addon">
                            <input type="text" class="form-control datepicker" name="creation_ts" value="" placeholder="Booking Date" id="bdate" style="padding-left: 12px;" readonly>
                            <span class="glyphicon glyphicon-calendar" style="right:10%;"></span>
                        </div>

                        <label for="timepicker1" class="gap">Select Meeting Start Time</label>
                        <div class="inner-addon right-addon">
                            <input type="text" class="form-control" name="meeting_start_ts" value="" placeholder="Meeting Start Time" id="timepicker1" data-time-format="H:i" data-step="15" data-min-time="6:00" data-max-time="23:45" data-show-2400="true" onchange="fillEndTime();" readonly>
                            <span class="glyphicon glyphicon-time" style="right:10%;"></span>
                        </div>

                        <label for="timepicker2" class="gap"> Select Meeting End Time</label>
                        <div class="inner-addon right-addon">
                            <input type="text" class="form-control" name="meeting_end_ts" value="" placeholder="Meeting End Time" id="timepicker2" data-time-format="H:i" data-step="15" data-min-time="6:00" data-max-time="24:00" data-show-2400="true" readonly>
                            <span class="glyphicon glyphicon-time" style="right:10%;"></span>
                        </div>

                        <label for="emailnoti" class="gap">Email Notification:</label>
                        <input type="checkbox" name="email_notification"  id="emailnoti" checked="checked" style="margin-left:10px;transform: scale(1.25);"/>
                        <br>
                        <label for="contacts" class="gap">Add Participants:</label>
                        <!--<input type="button" id="contacts" class="btn btn-sm" value="Select" style="margin-left:10px;" data-toggle="modal" data-target="#addressBookModal" /><br>-->
                        <!--<span id="contacts" class="glyphicon glyphicon-envelope"></span> <br>-->
                        <span style="margin-left: 10px;">
                            <img id="contacts" src="int-lib/images/address.png" height="25px" width="25px" data-toggle="modal" data-target="#addressBookModal" title="Address Book" style="cursor: pointer;" />
                        </span>
                        <div style="margin-top:10px;">
                            <!--<i class="glyphicon glyphicon-asterisk"></i>-->
                            <span id="participantInfo"></span>
                        </div>
                        
                        
                        <input type="text" name="booker_name" value="<%= emp_name%>" placeholder="" id="booker_name" style="display: none">
                        <input type="text" name="booker_email" value="<%= emp_email%>" placeholder="" id="booker_email" style="display: none">
                        <input type="text" name="user_type" value="<%= designation%>" placeholder="" id="designation" style="display: none">

                        <div class="gap"></div>
                        <button id="" class="btn btn-lg btn-primary btn-block" style="margin-top: 30px;" onclick="bookRoom();">Book</button>
                    </div>
                    <!--</form>-->
                </div>
                <div class="col-md-9" id="rightSection">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="panel panel-primary">
                                <!--                            <div class="panel panel-default">-->
                                <div class="panel-heading">Bookings :&nbsp;
                                    <span id="displayMeeting"></span>
                                </div>
                                <div class="panel-body" style="max-height: 10;overflow-y: auto;">

                                    <div class="form-group col-xs-2" style="padding-left:0px;">
                                        <div class="inner-addon right-addon">
                                            <input type="text" id="roomDetailsCal" class="form-control datepicker" style="width: 100%;cursor: pointer;" onchange="changeDateOnHeading()" readonly/>
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </div>
                                    </div>

                                    <div class='row' style="margin-left:0px; margin-right: 0px;">
                                        <table id='bookingByDate' class='table table-bordered' width='100%'>
                                            <thead>
                                                <tr>
                                                    <th>Booker Name</th>
                                                    <th>Room Name</th>
                                                    <th>Start Time</th>
                                                    <th>End Time</th>
                                                    <th>Status</th>
                                                    <th>Update Time</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--<div class="gap"></div>-->
                        <div class="row">
                            <div class="panel panel-primary">
                                <div class="panel-heading">Your's Booking</div>
                                <div class="panel-body" style="max-height: 10;overflow-y: auto;">
                                    <div class='row' style="margin-left:0px; margin-right: 0px;">
                                        <table id='CRbooking_ByUser' class='table table-bordered' width='100%'>
                                            <thead>
                                                <tr>
                                                    <th>Room Name</th>
                                                    <th data-orderable="false">Booked Date</th>
                                                    <th data-orderable="false">Start Time</th>
                                                    <th data-orderable="false">End Time</th>
                                                    <th>Status</th>
                                                    <th data-orderable="false">Update Time</th>
                                                    <th data-orderable="false">Actions</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="roomDetailsModal" tabindex="-1" role="dialog" data-backdrop="static"
             aria-labelledby="roomDetailsModal"  aria-hidden="true">
            <div class="modal-dialog" style="width:60%;">
                <div class="modal-content">
                    <!-- Modal Header -->
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Close</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            Room Details
                        </h4>
                    </div>

                    <!-- Modal Body -->
                    <div class="modal-body">
                        <table id="roomDetailsTbl" class="table table-condensed">
                            <thead>
                                <tr>
                                    <th style="width:40%;padding-left: 50px;">List of Facilities</th>
                                    <th style="padding-left: 75px;">Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr style="display: none;">
                                    <th>ID</th>
                                    <td id="roomId"></td>
                                </tr>
                                <tr>
                                    <th>Room Name</th>
                                    <td id="roomName"></td>
                                </tr>
                                <tr>
                                    <th>No.of Seat(s)</th>
                                    <td id="noOfSeat"></td>
                                </tr>
                                <tr>
                                    <th>Description</th>
                                    <td id="desc"></td>
                                </tr>
                                <tr>
                                    <th>Projector</th>
                                    <td id="projector"></td>
                                </tr>
                                <tr>
                                    <th>AC</th>
                                    <td id="ac"></td>
                                </tr>
                                <tr>
                                    <th>Speakers</th>
                                    <td id="speaker"></td>
                                </tr>
                                <tr>
                                    <th>Conf. Call Facility</th>
                                    <td id="confCall"></td>
                                </tr>
                                <tr>
                                    <th>White Board</th>
                                    <td id="whiteBoard"></td>
                                </tr>
                                <tr>
                                    <th>Ext.No</th>
                                    <td id="extNo"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div id="alertModal" class="modal fade" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="padding: 20px;border-bottom: 0px;">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <span id="info">Your booking was successful</span>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="changePwdModal" tabindex="-1" role="dialog" data-backdrop="static"
             aria-labelledby="myModalLabel"  aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!-- Modal Header -->
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Close</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            Change Password
                        </h4>
                    </div>

                    <!-- Modal Body -->
                    <form accept-charset="UTF-8" role="form" name="changepass" method="get" action="change_password">
                        <div class="modal-body">
                            <div id="changePwd" style="display: none;">
                                <i id="icon" class="glyphicon glyphicon-alert"></i>
                                <span id="changePwdInfo" style="padding-left: 5px;"></span>
                                <div class="gap"></div>
                            </div>

                            <!--                <form accept-charset="UTF-8" role="form" name="changepass" method="get" action="change_password">-->
                            <!--<form role="form">-->
                            <div class="form-group">
                                <label for="newPass">Enter new password</label>
                                <input type="password" class="form-control" id="newPass" name="pass" placeholder="New password" style="width:96%;"/>
                                <div class="gap"></div>
                                <label for="confirmPass">Confirm new password</label>
                                <input type="password" class="form-control" id="confirmPass" name="cpass" placeholder="Confirm new password"  style="width:96%;"/>
                            </div>
                        </div>

                        <!-- Modal Footer -->
                        <div class="modal-footer">
                            <button type="button" id="changePwdBtn" class="btn btn-primary" onclick="validate();">
                                Change Password
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <!--Address Book Modal popup - Starts-->
        <div class="modal fade" id="addressBookModal" tabindex="-1" role="dialog" data-backdrop="static"
             aria-labelledby="addressBookModal"  aria-hidden="true">
            <div class="modal-dialog" style="width:60%;">
                <div class="modal-content">
                    <!-- Modal Header -->
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Close</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            Address Book
                        </h4>
                    </div>

                    <!-- Modal Body -->
                    <div class="modal-body">
                        <div class='row' style="margin-left:0px; margin-right: 0px;">
                            <table id='addressBookTbl' class='table table-bordered' width='100%'>
                                <thead>
                                    <tr>
                                        <th><input type="checkbox" value="1" id="selectAll"/></th>
                                        <th style="width: 45%;text-align: center;">Name</th>
                                        <th style="text-align: center;">E-Mail Address</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                    <!-- Modal Footer -->
                    <div class="modal-footer">
                        <button id="reset" class="btn btn-primary" onclick="loadParticipantsAddressBook();">Reset</button>
                        <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="getSelectedParticipantsList();"> OK </button>
                    </div>
                </div>
            </div>
        </div>
        <!--Address Book Modal popup - Ends-->
        
        <!--Alert to display notification sent message-->
        <div id="mailInfoAlert" class="alert">
            <span class="alertMsg"></span>
            <span class="closebtn" onclick="this.parentElement.style.display = 'none';">&times;</span>
        </div>
        
        <form id="logoutForm" action="logout" method="post" style="display: none">
        </form>

        <script src="int-lib/js/crBookingjavaScript.js"></script>
        <script type="text/javascript" src="ext-lib/jquery/js/jquery-ui.min.js"></script>
        <link rel="stylesheet" href="ext-lib/bootstrap/css/bootstrap-datepicker.css" />
        <script src="ext-lib/bootstrap/js/bootstrap-datepicker.js"></script>
        <script type="text/javascript" src="ext-lib/jquery/js/jquery-datatable.js"></script>
        <script type="text/javascript" src="ext-lib/jquery/js/jquery-dataTable-bootstrap.js"></script>
        <link rel="stylesheet" href="ext-lib/jquery/css/dataTables-bootstrap.css"/>

    </body>
    <%             } catch (Exception e) {
                System.out.println("exception = " + e.getMessage());
            }
            if (con != null) {
                con.close();
            }
            if (stmt_hw_type != null) {
                stmt_hw_type.close();
            }
            if (stmt_hw_brand != null) {
                stmt_hw_brand.close();
            }
            if (stmt_hw_emp != null) {
                stmt_hw_emp.close();
            }
            if (stmt_hw_warr_type != null) {
                stmt_hw_warr_type.close();
            }
            if (rs_hw_type != null) {
                rs_hw_type.close();
            }
            if (rs_hw_brand != null) {
                rs_hw_brand.close();
            }
            if (rs_hw_emp != null) {
                rs_hw_emp.close();
            }
            if (rs_hw_warr_type != null) {
                rs_hw_warr_type.close();
            }
        }
    %>
</html>
