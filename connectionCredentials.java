package com.kundan.app.db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author abhardwaj
 */
public class connectionCredentials {

    public static String DBDriverName() {
        String Divers = "com.mysql.jdbc.Driver";
        return Divers;
    }

    public static String serverAddress() {
//        String serverAddress = "jdbc:mysql://180.151.199.236:3306/project_meeting";
        //String serverAddress = "jdbc:mysql://115.249.231.198:3306/project_meeting";
		String serverAddress = "jdbc:mysql://localhost:3306/project_meeting";
        return serverAddress;
    }

    public static String ServerID() {
//        String LoginID = "";
	String LoginID = "root";
        return LoginID;
    }

    public static String ServerPassword() {
//        String password = "";
	String password = "root";
        return password;
    }
}
