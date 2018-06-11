/**
 * Global variables used to store the JSON response values.
 */
var userBookingInfoRows = [];
var dateBookingInfoRows = [];
var roomvalue;
var participantsEmailIdAry = [];

/**
 * Function used to set auto height adjustments for "Bookings" info panel and "Your's Booking" info panel
 * based on height of the window screen.
 */
function setAutoHeightforBookingsPanel(){
    var navBarHeight = $(".navbar").height();
    var height = $(".container-fluid").innerHeight() - navBarHeight;
    var panelHeight = Math.floor(height / 2) + 10;
    $("#rightSection .panel").css({ "min-height" : panelHeight});
}

/**
 * Function used to display the date in the "Bookings" info panel when user selects date 
 * from "Select Booking Date" control from UI.
 */
function loadBookedInfoOnDateSelection(){
    var bookedDate = document.getElementById("bdate").value;
     $("#roomDetailsCal").datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        todayHighlight: true,
        //endDate : today
    }).datepicker("setDate", bookedDate);
}


$(document).ready(function ()
{
    var date = new Date();
    var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
    
    
    $('#bdate').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        todayHighlight: true,
        startDate: today,
        defaultDate: today
    }).datepicker("setDate", today);

    $("#roomDetailsCal").datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        todayHighlight: true,
        //startDate: today,
        //endDate : today
    }).datepicker("setDate", today);

    $("#timepicker1, #timepicker2").timepicker();
    
    /**
     * To fix the issue while opening the calendar and selecting the already selected 
     * date will display the date as empty.
     */
    $('.datepicker').on('show', function (e) {
        console.debug('show', e.date, $(this).data('stickyDate'));

        if (e.date) {
            $(this).data('stickyDate', e.date);
        } else {
            $(this).data('stickyDate', null);
        }
    });

    $('.datepicker').on('hide', function (e) {
        console.debug('hide', e.date, $(this).data('stickyDate'));
        var stickyDate = $(this).data('stickyDate');

        if (!e.date && stickyDate) {
            console.debug('restore stickyDate', stickyDate);
            $(this).datepicker('setDate', stickyDate);
            $(this).data('stickyDate', null);
        }
    });
    // Fix -- ends here.

    setAutoHeightforBookingsPanel();
    setDefaultStartAndEndTime();  //To set default start time and end time based on current system time
    getroomDls();   //To get the conference room details
    updateMeetingStatusOnLoad();    //To display the current status of meeting room
    loadParticipantsAddressBook();  //To load the participants name and email-id in address book popup


    //To set the date and time when user selects date from Booking section
    $("#bdate").on("changeDate", function () {
        setDefaultTimeOnDateSelection();
        //BookingInfoAllTodayDate();
        loadBookedInfoOnDateSelection();
    });
    
    //To load the booked details when user selects particular date in "Bookings" info panel.
    $("#roomDetailsCal").on("changeDate", function () {
        var selectedDate = document.getElementById("roomDetailsCal").value;
        BookingInfoAllTodayDate(selectedDate);
    });
    
    //To enable or disable 'Add Participants' button when Email notification checkbox is enabled/disabled
    $("#emailnoti").on("change",function(){
       if($("#emailnoti").is(":checked")){
           $("#contacts").removeAttr("disabled");
           $("#participantInfo").css({'color':''});
       }else{
           $("#contacts").attr("disabled","disabled");
           $("#participantInfo").css({'color':'transparent'});
       }
    });
    
    //To show/hide info icon when user selects room name
    $("#room_id").on("change", function () {
        var option = $("#room_id").val();
        if (option != "Select Room") {
            $("#roomInfoIcon").css({"color": "#337ab7"});
        } else {
            $("#roomInfoIcon").css({"color": "transparent"});
        }
    });
    setTimeout(function () {
        $("#loading").hide();        
    }, 1000);
    
    // To reset the text fields values in change password popup
    $("#changePwdModal").on("hidden.bs.modal", function () {
        $("#newPass").val("");
        $("#confirmPass").val("");
        $("#changePwd").css({"display": "none"});
    });
});


/*
 * Function used to get the list of selected participants e-mail id, when user 
 * clicks 'OK' in Address book popup
 */
function getSelectedParticipantsList() {

    participantsEmailIdAry = [];
    //To get all the rows (even in the next page) in datatable
    var table = $('#addressBookTbl').DataTable();
    var data = table.rows().nodes();
    
    //Iterate all the rows in datatable
    data.each(function (value, index) {
        //Getting the 0th columns(i.e. checkbox column) values
        var element = $($(value).find('td').eq(0)).find('input').prop('checked');
        
        //If checkbox is checked,then adding the 2nd column(i.e. email-id column) values to array
        if (element == true) {
            participantsEmailIdAry.push($($(value).find('td').eq(2)).text());
        } else {
            var el = $('#selectAll').get(0);
            el.checked = false;
        }
    });

    //$("#addressBookModal").modal("hide");
    var len = participantsEmailIdAry.length;
    $("#participantInfo").html("<b>" + len + " participant(s) will receive notification</b>");
}

/*
 * Function used to create and generate Participants details response into an array 
 * to display in Address book datatable in popup.
 */
function getParticipantsRow(responseJson) {

    var row = [];
    var participantsRowAry = [];
    var teamJson = responseJson.team;
    
    // To display team members list at first, iterating and pushing the 'responseJson.team' json
    // to 'participantsRowAry'
    for (var i in teamJson) {
        row = [];
        row.push(teamJson[i][0]);
        row.push(teamJson[i][1]);
        row.push(teamJson[i][2]);
        row.push("1");      //To check the team members by default in address book
        participantsRowAry.push(row);
    }

    // To display all other team members list, iterating and pushing the 'responseJson.other' json
    // to 'participantsRowAry'
    var othersJson = responseJson.others;
    for (var i in othersJson) {
        row = [];
        row.push(othersJson[i][0]);
        row.push(othersJson[i][1]);
        row.push(othersJson[i][2]);
        row.push("0");      //To uncheck the other team members by default in address book
        participantsRowAry.push(row);
    }
    return participantsRowAry;
}

/*
 * Function used to get the participants name and e-mail id via 'crbookingParticipants' api.
 */
function loadParticipantsAddressBook() {
    var serialNo = $("#userSerialNo").html();
    $.ajax({
        url: 'crbookingParticipants?uno=' + serialNo,
        type: 'GET',
        success: function (responseJson) {

            var participants = [];
            participants = getParticipantsRow(responseJson);
            
            //Initialisation of Address book datatable
            var ex = document.getElementById('addressBookTbl');
            if ($.fn.DataTable.fnIsDataTable(ex)) {
                var oTable = $('#addressBookTbl').dataTable();
                oTable.fnDestroy();
            }
            
            var table = $('#addressBookTbl').DataTable({
                "aaData": participants,
                "bPaginate": true,
                //"iDisplayLength": 10,
                "bLengthChange": false,
                "bFilter": true,
                "bInfo": true,
                "bAutoWidth": false,
                "aaSorting": [],
                columnDefs: [{
                        'targets': 0,
                        'searchable': false,
                        'orderable': false,
                        'className': 'dt-body-center',
                        'render': function (data, val, row) {
                            if (row[3] == '1') {    //row[3] = 0 - represents other team member, 1 - represents own team member
                                return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '" checked>';
                            } else {
                                return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                            }
                        }
                    }]
            });

            // To check/uncheck all checkboxes in the table when "Select all" is clicked
            $('#selectAll').on('click', function () {
                var data = table.rows({'search': 'applied'}).nodes();
                $('input[type="checkbox"]', data).prop('checked', this.checked);
            });

            //To check/uncheck "Select all" checkbox, when table rows are checked/unchecked
            $('#addressBookTbl tbody').on('change', 'input[type="checkbox"]', function () {
                
                /* counter - variable used to identify any row is unchecked or not.
                 * If atleast 1 row is unchecked then, counter will be incremented and "Select all" checkbox will be unchecked.
                 * otherwise, "Select all" checkbox will be checked.
                 */
                var counter = 0;    
                var data = table.rows({'search': 'applied'}).nodes();
                data.each(function (value, index) {
                    var ele = $($(value).find('td').eq(0)).find('input').prop('checked');
                    if (ele == false) {
                        counter += 1;
                    }
                });
                //To check/uncheck "Select All" checkbox
                var el = $('#selectAll').get(0);
                if (counter != 0) {
                    el.checked = false;
                } else {
                    el.checked = true;
                }
            });
            getSelectedParticipantsList();
        }
    });
}

/**
 * Function used to update meeting status from '1'(active) to '0'(inactive).
 * This function will reset the status while user refresh the page.
 * 
 *  Author: Kundan Lal 
 */
function updateMeetingStatusOnLoad() {
    $.ajax({
        url: 'UpdateMeetingStatus',
        type: 'GET',
        datatype: 'json',
        success: function (result) {
            if (result != '2') {
                var selectedDate = "";
                selectedDate = document.getElementById('bdate').value;
                BookingInfoByUser();
                BookingInfoAllTodayDate(selectedDate);
            } else {
                $("#alertModal").modal("show");
                $("#alertModal #info").text("Something went wrong. Please try again later.");
            }
        }
    });
}

/*
 * Function used to generate POST json data and send via /BookingInfoSaveUpdateDeactivate api.
 * This api is responsible for booking meeting room. 
 * 
 * Author : Kundan Lal
 */
function bookRoom() {

    var city = document.getElementById('city').value;
    var room_id = document.getElementById('room_id').value;

    var roomNameElement = document.getElementById("room_id");
    var roomName = roomNameElement.options[roomNameElement.selectedIndex].text;

    var creation_ts = document.getElementById('bdate').value;
    var meeting_start_ts = document.getElementById('timepicker1').value;
    var meeting_end_ts = document.getElementById('timepicker2').value;
    var email_notification = document.getElementById('emailnoti').checked;

    var booker_name = document.getElementById('booker_name').value;
    var booker_email = document.getElementById('booker_email').value;
    var designation = document.getElementById('designation').value;

    var selectedDate = creation_ts.split("-");
    var startTime = meeting_start_ts.split(":");
    startTime = (new Date(selectedDate[2], selectedDate[1] - 1, selectedDate[0], startTime[0], startTime[1], "00", "000").getTime() / 1000);
    
    var endTime = meeting_end_ts.split(":");
    endTime = (new Date(selectedDate[2], selectedDate[1] - 1, selectedDate[0], endTime[0], endTime[1], "00", "000").getTime() / 1000);

    if (email_notification == false)
    {
        email_notification = 0;
    } else {
        email_notification = 1;
    }

    var alertMsg = "";
    if (city == "Select City") {
        alertMsg = "Please select city";
    } else if (room_id == "Select Room") {
        alertMsg = "Please select room";
    } else if (creation_ts == "") {
        alertMsg = "Please select date";
    } else if (meeting_start_ts == "") {
        alertMsg = "Please select meeting start time";
    } else if (meeting_end_ts == "") {
        alertMsg = "Please select meeting end time";
    } else if ( endTime <= startTime) {
        alertMsg = "Start Time must be less than end time";
    }

    if (alertMsg != "") {
        $("#alertModal").modal("show");
        $("#alertModal #info").text(alertMsg);
    } else {
        var selectedDate = creation_ts.split('-');

        var selectYear = selectedDate[2];
        var selectMonth = selectedDate[1] - 1;
        var selectDate = selectedDate[0];

        var selectStartTime = meeting_start_ts.split(':');
        var selectStartHour = selectStartTime[0];
        var selectStartMin = selectStartTime[1];

        var selectEndTime = meeting_end_ts.split(':');
        var selectEndHour = selectEndTime[0];
        var selectEndtMin = selectEndTime[1];

        var data = {};

        data["city"] = city;
        data["room_id"] = room_id;
        data["creation_ts"] = Math.floor(new Date().getTime() / 1000);
        data["meeting_start_ts"] = (new Date(selectYear, selectMonth, selectDate, selectStartHour, selectStartMin, "00", "000").getTime() / 1000);
        data["meeting_end_ts"] = (new Date(selectYear, selectMonth, selectDate, selectEndHour, selectEndtMin, "00", "000").getTime() / 1000);
        data["email_notification"] = email_notification;
        
        //To send participants email-id,only if email-notification is enabled
        if($("#emailnoti").is(":checked")){
            data["participate_email"] = participantsEmailIdAry.toString();
        } else {
            data["participate_email"] = "";
        }
        
        data["booker_name"] = booker_name;
        data["booker_email"] = booker_email;
        data["user_type"] = designation;
        data["room_name"] = roomName;
        data["country"] = "India";
        data["status"] = 1;
        data["update_ts"] = -1;
        data["booker_id"] = booker_email;
        
        data = JSON.stringify(data);
        
        var status = "";
        
        $.ajax({
            url: 'BookingInfoSaveUpdateDeactivate',
            type: 'post',
            datatype: 'json',
            data: data,
            beforeSend: function(){
              $("#loading").show();  
            },
            success: function (result) {
                
                var msg = "";
                status = result.toString();
                switch (status) {
                    case '0':
                        msg = "Room was already booked for the selected time. Please change the time.";
                        break;
                    case '1':
                        msg = "Your booking was scheduled successfully.";
                        break;
                    case '2':
                        msg = "Something went wrong. Please try again later.";
                        break;
                }
                $("#alertModal").modal("show");
                $("#alertModal #info").text(msg);
                
                BookingInfoByUser();
                BookingInfoAllTodayDate(creation_ts);
                $("#loading").hide();
                
                /* 'status' = 1 - represents the success state of creating meeting in database.
                 * If meeting is created successfully, then notification email will be sent.
                 */
                if (status == '1') {
                    sendEmail(data);
                }
            }
        });
    }
}

/**
 * Function used to send email notification via /sendNotificationEmail api.
 * Based on the response, email notification alert will be displayed in UI.
 */
function sendEmail(jsonData) {
    $.ajax({
        url: 'sendNotificationEmail',
        type: 'post',
        datatype: 'json',
        data: jsonData,
        complete: function (result) {
            var msg = "";
            $("#mailInfoAlert .alertMsg").html("");
            if (result.responseText == '1') {
                msg = "Notification E-Mail sent successfully to all participant(s)...";
            } else {
                msg = "An error occurred while sending notification E-Mail...";
            }
            $("#mailInfoAlert .alertMsg").html(msg);
            $("#mailInfoAlert").fadeIn(1000);
            $("#mailInfoAlert").css({"display": "block"});
            //$("#mailInfoAlert").fadeOut(3000);
        }
    });
}

/*
 * Function used to display all the booked meetings for particular user selected date.
 */
function BookingInfoAllTodayDate(selectedDate)
{
    //var selectedDate = document.getElementById('bdate').value;
    document.getElementById('displayMeeting').innerHTML = selectedDate;
    selectedDate = selectedDate.split("-");

    var startTime = (new Date(selectedDate[2], selectedDate[1] - 1, selectedDate[0], "0", "0", "00", "000").getTime() / 1000);
    var endTime = (new Date(selectedDate[2], selectedDate[1] - 1, selectedDate[0], "23", "59", "59", "000").getTime() / 1000);

    var start = new Date();
    start.setHours(0, 0, 0, 0) / 1000;

    var end = new Date();
    end.setHours(23, 59, 59, 999);

    $.get('BookingInfoAll', {
        stTs: startTime,
        endTs: endTime,
    }, function (responseJson) {

        getBookedDetailsRowsForDate(responseJson);
        
        var ex = document.getElementById('bookingByDate');
        if ($.fn.DataTable.fnIsDataTable(ex)) {
            var oTable = $('#bookingByDate').dataTable();
            oTable.fnDestroy();
        }
        if (dateBookingInfoRows.length != 0) {
            var status = true;
            if(dateBookingInfoRows.length > 5){
                $("#bookingByDate").parent().css({"min-height": "230px"});
            } else {
                $("#bookingByDate").parent().css({"min-height": ""});
            }
        } else {
            var status = false;
            $("#bookingByDate").parent().css({"min-height": ""});
            $("#rightSection .panel").css({ "min-height" : ""});
        }


        $('#bookingByDate').dataTable({
            "aaData": dateBookingInfoRows,
            "bPaginate": status,
            "iDisplayLength": 5,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": true,
            "bAutoWidth": false,
            "aaSorting": []
            //"order": [[2, "desc"]]
        });
    });
}

/*
 * Function used to read the responseJSON from /BookingInfoAll api and generate
 * rows to dispay in UI.
 */
function getBookedDetailsRowsForDate(responseJson) {
    var row = [];
    dateBookingInfoRows = [];
    if (responseJson != null || responseJson.length != 0) {
       
        $.each(responseJson, function (key, value) {
            var status;
            var update_ts;
            // Unixtimestamp
            var unixtimestamp = value['creation_ts'];
            var Mstarttime = value['meeting_start_ts'];
            var Mendtime = value['meeting_end_ts'];

            // Months array
            var months_arr = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

            // Convert timestamp to milliseconds
            var date = new Date(unixtimestamp * 1000);
            var mstime = new Date(Mstarttime * 1000);
            var metime = new Date(Mendtime * 1000);

            // Year
            var year = date.getFullYear();

            // Month
            var month = months_arr[date.getMonth()];

            // Day
            var day = date.getDate();

            // Hours
            var hours = date.getHours();
            var Mshours = mstime.getHours();
            var Mehours = metime.getHours();


            // Minutes
            var minutes = "0" + date.getMinutes();
            var Msmin = "0" + mstime.getMinutes();
            var Memin = "0" + metime.getMinutes();
            // Seconds
            var seconds = "0" + date.getSeconds();

            // Display date time in MM-dd-yyyy h:m:s format
            var creation_ts = month + '-' + day + '-' + year + ' ' + hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
            var meeting_start_ts = Mshours + ':' + Msmin.substr(-2);
            var meeting_end_ts = Mehours + ':' + Memin.substr(-2);

            if (value["status"] == 1)
            {
                status = "Active";
            }
            if (value["update_ts"] == -1)
            {
                update_ts = meeting_start_ts;
            } else
            {
                update_ts = new Date(value['update_ts']);
            }
            
            row = [];
            row.push(value['booker_name']);
            row.push(value['room_name']);
            row.push(meeting_start_ts);
            row.push(meeting_end_ts);
            row.push(status);
            row.push(update_ts);
            row.push(value['']);
            dateBookingInfoRows.push(row);
        });
    }
}

/*
 * Function used to display all the booked history details based on user.
 */
function BookingInfoByUser()
{
    var bookerMailId = document.getElementById('booker_email').value;
    $.get('BookingInfoByUser', {
        stTs: 129301433,
        endTs: 1924166332,
        user: bookerMailId
    }, function (responseJson) {
        getBookedDetailsRowsForUser(responseJson);
        var ex = document.getElementById('CRbooking_ByUser');
        if ($.fn.DataTable.fnIsDataTable(ex)) {
            var oTable = $('#CRbooking_ByUser').dataTable();
            oTable.fnDestroy();
        }

        if (userBookingInfoRows.length != 0) {
            var status = true;
        } else {
            var status = false;
            $("#bookingByDate").parent().css({"min-height": "230px"});
            $("#CRbooking_ByUser").parent().css({"min-height": "240px"});
        }

        $('#CRbooking_ByUser').dataTable({
            "aaData": userBookingInfoRows,
            "bPaginate": status,
            "iDisplayLength": 5,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": true,
            "bAutoWidth": false,
            //"order": [[1, "desc"]],
            "aaSorting": [],
            "aoColumnDefs": [
                {
                    "mRender": function (data, val, row) {
                        var links = '';
                        if (row[7] == '1') {
                            links = links
                                    + ' '
                                    + '<center><a title="Cancel Booking" style="color: black;" href="JavaScript:changeStatus('
                                    + row[6]
                                    + ')"><span class="glyphicon glyphicon-remove-circle"></span>'
                                    + '</a></center>';
                        } else {
                            links = links
                                    + ' '
                                    + '<center><a style="color: #c5b9b9;" href="JavaScript:void(0);">'
                                    + '<span class="glyphicon glyphicon-remove-circle"></span>'
                                    + '</a></center>';
                        }
                        return links;
                    },
                    "aTargets": [6]
                }
            ]
        });
    });
}

/*
 * Function used to read the responseJSON from /BookingInfoByUser api and generate
 * rows to dispay in UI.
 */
function getBookedDetailsRowsForUser(responseJson) {

    var row = [];
    userBookingInfoRows = [];
    if (responseJson != null || responseJson.length != 0) {
        $.each(responseJson, function (key, value) {

            //To convert timestamp into user format
            var unixtimestamp = value['creation_ts'];
            var update_ts = value['update_ts'];

            var Mstarttime = value['meeting_start_ts'];
            var Mendtime = value['meeting_end_ts'];

            // Months array
            var months_arr = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

            // Convert timestamp to milliseconds
            var date = new Date(unixtimestamp * 1000);
            var update_ts = new Date(update_ts * 1000);

            var mstime = new Date(Mstarttime * 1000);
            var metime = new Date(Mendtime * 1000);

            // Year
            var year = date.getFullYear();
            var update_ts_year = update_ts.getFullYear();
            var Mstyear = mstime.getFullYear();
            var Metyear = metime.getFullYear();

            // Month
            var month = months_arr[date.getMonth()];
            var update_ts_month = months_arr[update_ts.getMonth()];
            var Mstmonth = months_arr[date.getMonth()];

            /**
             * Issue: Month in Meeting end date is displayed wrongly because of update_ts is -1
             * month will be displayed as Jan always.
             * Fix: Added condition to check whether updateTime value is -1. If yes, endTime is 
             * used otherwise updateTime is used.
             */
            if (value["update_ts"] == -1) {
                var Metmonth = months_arr[metime.getMonth()];
            } else {
                var Metmonth = months_arr[update_ts.getMonth()];
            }

            // Day
            var day = date.getDate();
            var update_ts_day = update_ts.getDate();
            var Mstday = mstime.getDate();
            var Metday = metime.getDate();
            // Hours
            var hours = date.getHours();
            var update_ts_hours = update_ts.getHours();
            var Mshours = mstime.getHours();
            var Mehours = metime.getHours();

            // Minutes
            var minutes = "0" + date.getMinutes();
            var update_ts_miniuts = "0" + update_ts.getMinutes();
            var Msmin = "0" + mstime.getMinutes();
            var Memin = "0" + metime.getMinutes();
            // Seconds
            var seconds = "0" + date.getSeconds();

            // Display date time in MM-dd-yyyy h:m:s format
            var creation_ts = month + '-' + day + '-' + year + ' ' + hours + ':' + minutes.substr(-2);
            var update_ts_update = update_ts_month + '-' + update_ts_day + '-' + update_ts_year + ' ' + update_ts_hours + ':' + update_ts_miniuts.substr(-2);

            var meeting_start_ts = Mstmonth + '-' + Mstday + '-' + Mstyear + ' ' + Mshours + ':' + Msmin.substr(-2);
            var meeting_end_ts = Metmonth + '-' + Metday + '-' + Metyear + ' ' + Mehours + ':' + Memin.substr(-2);


            if (value["update_ts"] == -1)
            {
                update_ts_update = creation_ts;

            } else
            {
                update_ts_update = update_ts_update;
            }

            if (value['status'] == 0)
            {
                var status = "<span style='color:red'>Cancelled</span>"
            } else if (value['status'] == 2) {
                var status = "<span>Expired</span>"
            } else
            {
                var status = "<span style='color:green'>Active</span>"
            }

            var id = value['id'];
            row = [];

            row.push(value['room_name']);
            row.push(creation_ts);
            row.push(meeting_start_ts);
            row.push(meeting_end_ts);
            row.push(status);
            row.push(update_ts_update);
            row.push(id);
            row.push(value['status']);
            userBookingInfoRows.push(row);
        });
    }
}

/*
 * Function used to get the room details via '/RoomData' api
 */
function getroomDls()
{
    $.get('RoomData', {
    }, function (responseJson) {
        if (responseJson != null) {
            roomvalue = "";
            roomvalue = responseJson;
            dropdownCity();
        }
    });
}

/*
 * Function used to iterate and fill room details to the 'Select City' drpodownlist in UI.
 */
function dropdownCity()
{
    $.each(roomvalue, function (key, value) {
        if (value['city'] != null && value['city'] != undefined && value['city'] != "") {
            $('#city').append($('<option>', {
                value: value['city'],
                text: value['city'],
            }));
        }
    });
    
    // To remove duplicate values from dropdown
    var map = {};
    $('select option').each(function () {
        if (map[this.value]) {
            $(this).remove()
        }
        map[this.value] = true;
    });
    //To set default city as 'Bangalore'
    $("#city").val("Bangalore");
    onSelectRoomid();
}

/*
 * Function used to bind the room details based on the selected city.
 */
function onSelectRoomid()
{
    var selval = $('#city').find(":selected").val();
    var selectOpt = $('#room_id');
    selectOpt.find('option').remove();
    $('#room_id').append("<option>Select Room</option>");
    $.each(roomvalue, function (key, value) {
        if (value['room_id'] != null && value['room_id'] != undefined && value['room_id'] != "") {
            if (selval == value['city']) {
                $('#room_id').append($('<option>', {
                    value: value['room_id'],
                    text: value['room_name'],
                }));
            }
        }
    });
}

/*
 * Function used to bind all the details of the selected room from dropdownlist in a popup.
 */
function get_room_data()
{
    var selectOpt = $('#room_id').val();
    if (roomvalue != null) {
        $.each(roomvalue, function (key, value) {
            if (selectOpt == value['room_id']) {
                $("#roomDetailsModal").modal("show");
                $("#roomId").text(value['id']);
                $("#roomName").text(value['room_name']);
                $("#noOfSeat").text(value['seating_capacity']);
                $("#desc").text(value['meeting_room_detail']);
                $("#projector").text(value['projector']);
                $("#ac").text(value['ac']);
                $("#speaker").text(value['speaker']);
                $("#confCall").text(value['confrence_call']);
                $("#whiteBoard").text(value['white_board']);
                $("#extNo").text(value['extension']);
            }
        });
    }
}

/**
 * Function used to cancel the booked room and refresh the UI page
 */
function changeStatus(userId)
{
    $('.loader').show();
    $.post('UpdateBookingInfoData', {
        id: userId,
    }, function (response) {
        response = response.toString();
        if (response == '1')
        {
            $('.loader').hide();

            $("#alertModal").modal("show");
            $("#alertModal #info").text("Scehduled Meeting was cancelled successfully");

            var selectedDate = document.getElementById('bdate').value;
            BookingInfoByUser();
            BookingInfoAllTodayDate(selectedDate);
        } else {
            $("#alertModal").modal("show");
            $("#alertModal #info").text("Something went wrong. Please try again later.");
        }
    });
}

/**
 * Function used to logout the current session
 */
function logout()
{
    document.getElementById('logoutForm').submit();
}

/**
 * Function used to set default start time and end time in UI
 * Author: Kundan Lal
 */
function setDefaultStartAndEndTime() {
    var startTime = getSystemTimeInfo();
    $('#timepicker1').timepicker().val(startTime);
    fillEndTime();
}

/**
 * Function used to calculate end time based on the start time and fill in
 * the end time in UI automatically when user changes start time.
 * 
 * Author: Kundan Lal
 */
function fillEndTime() {
    var endTime = $("#timepicker2").val();
    var splittedEndTime = endTime.split(":");
    var startTime = $("#timepicker1").val();
    var splittedStartTime = startTime.split(":");

    /**
     * splittedStartTime[0] and splittedStartTime[1] represents hours and minutes.
     * Added condition to check the minutes is 45 or not. If true, then add '1' to hours
     * and '00' to minutes. Otherwise, add only 15 minutes to the start time.
     */
    if (splittedStartTime[1] == '45') {
        var intSplittedStartTime = splittedStartTime[0] * 1;
        //To display '00:00' in end time if '23:45' is selected in start time
        (intSplittedStartTime == 23) ? (splittedEndTime[0] = '00') : (splittedEndTime[0] = intSplittedStartTime + 1);
        splittedEndTime[1] = '00';
    } else {
        splittedEndTime[0] = (splittedStartTime[0] * 1);
        splittedEndTime[1] = (splittedStartTime[1] * 1) + 15;
    }

    var newEndTime = splittedEndTime[0] + ":" + splittedEndTime[1];
    $('#timepicker2').timepicker().val(newEndTime);
}

/**
 * Function used to get the system time and calculate start time and end time automatically
 * and displays the default start time and end time in UI based on the current time.
 * 
 * Author: Kundan Lal
 */
function getSystemTimeInfo() {

    //To get the system time
    var date = new Date();
    var hour = date.getHours();
    var min = date.getMinutes();

    var timeStr = "";
    var roundedMin = "";

    //'interval' represents the minutes with 15min difference
    var interval = [0, 15, 30, 45, 60];
    for (var i = 0; i < 5; i++) {
        //To find the minutes difference between system time and meeting time
        var m = min - interval[i];

        /**
         * If difference is less than 5 min, then it will take the previous interval.
         * eg: if system time is 11:02, then start time will be displayed as 11:00.
         * if system time is 11:10, then start time will be displyed as 11:15.
         */
        if (m <= 5) {
            roundedMin = interval[i];
            break;
        } else {
            if (interval[i] >= min) {
                roundedMin = interval[i];
            }
        }
    }

    (roundedMin == 0) ? (roundedMin = '00') : roundedMin;
    if (roundedMin == '60') {
        hour = hour * 1 + 1;
        min = '00';
    } else {
        min = roundedMin;
    }

    timeStr = hour + ":" + min;
    return timeStr;
}

/**
 * Function used to set default start time and end time on selecting particular date.
 * If current date is selected, then current start time and end time will be displayed.
 * Otherwise, default start time(09:00) and end time(09:15) will be displayed.
 * 
 * Author: Kundan Lal
 */
function setDefaultTimeOnDateSelection() {

    //To get the selected date
    var selectedDate = document.getElementById('bdate').value;
    selectedDate = selectedDate.replace(/-/g, '');

    //To get the system date
    var date = new Date();
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    if (day <= 9) {
        day = '0' + day;
    }
    if (month <= 9) {
        month = '0' + month;
    }
    var systemDate = "" + day + month + year;

    //Added condition to check systemDate and selected date is same or not
    if (selectedDate == systemDate) {
        setDefaultStartAndEndTime();
    } else {
        $('#timepicker1').timepicker().val('09:00');
        $('#timepicker2').timepicker().val('09:15');
    }
}

/**
 * Function used to display the current date selected in panel heading when user
 * selects date from calendar control inside panel.
 */
function changeDateOnHeading() {
    var selectedDate = document.getElementById('roomDetailsCal').value;
    document.getElementById('displayMeeting').innerHTML = selectedDate;
}

/**
 * Function used to validate the new password and confirm password in change password popup.
 */
function validate() {

    var pass = document.forms["changepass"]["pass"].value;
    var cpass = document.forms["changepass"]["cpass"].value;
    var password = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;

    if (password.test(pass) == false)
    {
        $("#changePwd").css({"display": "block", "color": "red"});
        $("#changePwdInfo").html("Password must be contains atleast 8 characters with atleast 1 number, 1 upper and lowercase letters and 1 special characters")
        document.forms["changepass"]["pass"].focus();
    } else
    if (pass == "" || pass != cpass)
    {
        $("#changePwd").css({"display": "block", "color": "red"});
        $("#changePwdInfo").html("Password does not Match! Confirm Password Again");
        document.forms["changepass"]["cpass"].focus();
    } else
    {
        $("#changePwdBtn").attr("disabled","disabled");
        var userSerialNo = document.getElementById("userSerialNo").innerHTML;
        $.get('change_password', {
            pass: pass,
            userSerialNo: userSerialNo
        }, function (jsonResponse) {
            if (jsonResponse[0] == 'Success')
            {
                $("#changePwdModal").modal('hide');
                $("#alertModal").modal("show");
                $("#alertModal #info").text("Your password has been changed successfully");
            } else
            {
                $("#changePwd").css({"display": "block", "color": "red"});
                $("#changePwdInfo").html("Something Went wrong! Please try again later.");
            }
            $("#changePwdBtn").removeAttr("disabled");
        });
    }
}