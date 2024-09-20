package com.example.btlandroid;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection connection;
    String ConnectURL;
    String ip, port, db, user, pass;

    @SuppressLint("NewApi")
    public Connection conclass() {
        ip = "192.168.248.162"; //4g
//        ip = "192.168.1.203"; //home
//        ip = "192.168.137.39";
        port = "1433";
        db="QLTruyen";
        user="sa";
        pass="123";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ConnectURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectURL= "jdbc:jtds:sqlserver://"+ip+":"+port+";"+ "databasename="+ db+";user="+user+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectURL);
            Log.e("Call ", "Connection Called");
        }
        catch (Exception exception)
        {
            Log.e("Error: ", "Failed "+exception.getMessage());
        }
        return connection;
    }
}

