<%-- 
    Document   : login
    Created on : 9 Feb, 2018, 6:04:38 PM
    Author     : kundan 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login - Conference Room Booking System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="int-lib/images/lear-icon.png" />
        <link href="ext-lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
        <script type="text/javascript" src="ext-lib/jquery/js/jquery.js"></script>
        <script type="text/javascript" src="ext-lib/bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="int-lib/js/login.js"></script>
        <link href="int-lib/css/style.css" rel="stylesheet"/>
    </head>

    <body>
        <nav class="navbar navbar-inverse">
            <img class="navbar-left img-responsive" src="int-lib/images/lear-icon.png" style="width: 50px;"/>
            <div class="navbar-header">
                <span class="navbar-brand" style="color:white;">Conference Room Booking System</span>
            </div>
        </nav>
        
        <div class="container" style="margin-top: 9%;">
            <div class="row">
                <div class="col-md-4 col-sm-3 col-xs-2 mx-auto"></div>
                <div class="col-md-4 col-sm-6 col-xs-8 mx-auto loginBorder">
                    <center>
                        <span id="loginInfo" style="display:none;" >Error Info</span>
                        <form id="loginForm" class="form-horizontal form-signin" name="login" action="login" method="post" autocomplete="off">

                            <input id="inputEmail" name="login_id" class="form-control gap" placeholder="Username" required autofocus>
                            <input type="password" name="pass" id="inputPassword" class="form-control gap" placeholder="Password" required>

                            <div class="login-input" style="display: none;">
                                <select name="login_type" required style="width: 254px;padding: 10px;border: 2px solid skyblue;border-radius: 3px;" id="login_type_id">
                                    <option value="Normal user">User</option>
                                </select>
                            </div>
                            <div class="gap"></div>
                            <button class="btn btn-lg btn-primary btn-block" onclick="">Sign in</button>

                            <div class="checkbox mb-3">
                                <input type="button" value="Forgot Password ?" data-toggle="modal" data-target="#myModalNorm" style="border: none;background: none!important;color:#069;font-family:arial,sans-serif;cursor: pointer;float: left;" title="Get your Password">
                            </div>
                        </form>
                    </center>
                </div>
                <div class="col-md-4 col-sm-3 col-xs-2 mx-auto"></div>
            </div>
        </div>
        
        <!-- Forgot password - Modal -->
        <div class="modal fade" id="myModalNorm" tabindex="-1" role="dialog" data-backdrop="static"
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
                            Recover Your Password
                        </h4>
                    </div>

                    <!-- Modal Body -->
                    <div class="modal-body">
                        <form accept-charset="UTF-8" role="form" id="login-recordar" method="post" autocomplete="off">
                        <!--<form role="form">-->
                            <div class="form-group">
                                In order to receive your password by e-mail, please enter your Lear E-mail address.<br><br>
                                <label for="forget_pass_id">Email address</label>
                                <input type="email" class="form-control"
                                       id="forget_pass_id" placeholder="Enter email"/>
                                <div class="gap" id="errorInfo" style="color: red"></div>
                            </div>
                        </form>
                    </div>

                    <!-- Modal Footer -->
                    <div class="modal-footer">
                        <button type="button" id="forgotPwdBtn" class="btn btn-primary" onclick="forget_pass_ajax()">
                            Send
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Forgot password - Modal ends -->
    </body>
</html>