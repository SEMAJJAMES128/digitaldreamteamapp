package com.example.digitaldreamteamapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, database;
    @SuppressLint("NewApi")

    public Connection connectionclass()
    {
        // PostgreSQL database connection details
        ip = "172.1.1.0";
        database = "DigitalDream";
        uname = "ddt";
        pass = "test123";
        port = "5432"; // Default PostgreSQL port

        // Allow network operation on the main thread (Not recommended for production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            // Use PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            // Update the connection URL format for PostgreSQL
            ConnectionURL = "jdbc:postgresql://" + ip + ":" + port + "/" + database + "?user=" + uname + "&password=" + pass;
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return connection;
    }
}
