/*
 *  Author: Kundan Lal 
	*This code is written for all the alert/error messages
 */

$(document).ready(function () {
    $("#loginForm").submit(function (event) {
        event.preventDefault();
        var username = document.forms["login"]["login_id"].value;
        var pass = document.forms["login"]["pass"].value;
        var login_type = "Normal user";

        if (login_type == 'admin')
        {
            username = username;
        } else {
            var new_changed = username.split("@")[0];
            username = new_changed + "@lear.com";
        }
        $.post('login', {
            login_id: username,
            pass: pass,
            login_type: login_type
        }, function (jsonResponse) {
            if (jsonResponse[0] == 1) //value is correct
            {
                window.location = jsonResponse[1];
            } else if (jsonResponse[0] == 0) //invalid login
            {
                document.getElementById("loginInfo").innerHTML = jsonResponse[1];
                document.getElementById("loginInfo").style.display = 'block';
            } else {
                document.getElementById("loginInfo").innerHTML = "Something went wrong.Please try again later.";
                document.getElementById("loginInfo").style.display = 'block';
            }
        });
    });
    
    //To reset the text field in forgot password modal popup
    $(".modal").on("hidden.bs.modal", function () {
        $("#forget_pass_id").val("");
        document.getElementById("errorInfo").innerHTML = "";
    });
});

/*
 * Function used to get the password if user forgot their password via e-mail.
 */
function forget_pass_ajax()
{
    var email_id = document.getElementById("forget_pass_id").value;
    var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
    if (email_id == "")
    {
        document.getElementById("errorInfo").innerHTML = "Please enter your Lear E-mail id.";
        document.getElementById("forget_pass_id").focus();
    } else if ((regMail.test(email_id) == false))
    {
        document.getElementById("errorInfo").innerHTML = "Please enter a valid E-mail address";
    } else
    {
        $("#forgotPwdBtn").attr("disabled","disabled");
        $.get('login', {
            email_id: email_id
        }, function (jsonresponse) {
            if (jsonresponse[0] == "Success")
            {
                var msg = "You will receive your password on provided Lear E-mail address soon."
                document.getElementById("errorInfo").innerHTML = msg;
            } else if (jsonresponse[0] == "Sorry, no account with that email address was found.")
            {
                var msg = "Sorry. No account information was found with this E-mail id."
                document.getElementById("errorInfo").innerHTML = msg;
            } else
            {
                document.getElementById("errorInfo").innerHTML = "Server Error. Please try again later or contact admin";
            }
            //To enable and change the Send button text to 'Resend'
            setTimeout(function(){
                $("#forgotPwdBtn").removeAttr("disabled");
                $("#forgotPwdBtn").text("Resend");
            },3000);
        });
    }
}