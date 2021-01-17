package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class Database {

    //Method to test the connection
    public static void connectionTest() throws SQLException {
        Connection c = null;
        try {
            // Class.forName("org.sqlite.JDBC");
            String urL = "jdbc:sqlite:/var/www/Weather_station.db";//jdbc:sqlite:DIRECTORY OF DATABASE
            c = DriverManager.getConnection(urL);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        c.close();
    }

    //Method to insert data in "Readings" through a HashMap

    public static void insertData(HashMap<String, String> map) {
        String url = "jdbc:sqlite:/var/www/Weather_station.db";//jdbc:sqlite:DIRECTORY OF DATABASE
        try {
            Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement();

            // s.executeUpdate("INSERT INTO 'Location'" + "VALUES ('"+map.get("location")+"','" + map.get("placement") + "' ',' '" + map.get("longitude") + "' ',' '" + map.get("latitude") + "' )");
            //System.out.println("Successfully updated table: Location");
            String timeStamp = TimeStepConverter.convertToRealTime(map.get("timestamp"));
            String sql = "INSERT INTO Readings " + "VALUES ('" + timeStamp + "','" + map.get("dev_id") + "','" + map.get("temperature") + "','" + map.get("light") + "','" + map.get("pressure") + "'  )";
            System.out.println(sql);
            s.executeUpdate(sql);
            System.out.println("Successfully updated table: Readings");
            //s.executeUpdate("INSERT INTO 'Weather_station'" + "VALUES ('"+map.get("location")+"','" + map.get("dev_id") + "')");
            //System.out.println("Successfully updated table: Weather_station");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    //Method to insert data into table "Location" through a HashMap

    public static void insertLocation(HashMap<String, String> map) {
        String url = "jdbc:sqlite:/var/www/Weather_station.db";//jdbc:sqlite:DIRECTORY OF DATABASE
        try {
            Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement();
            for (Map.Entry<String, String> next : map.entrySet()) { //Iterating through the HashMap and putting the values in the database
                s.executeUpdate("INSERT INTO 'location'" + "VALUES ('" + next.getKey() + "" + next.getValue() + "')");
            }
            System.out.println("Successfully updated table: Location");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    //Method to insert data into table "Weather_station" through a HashMap

    public static void insertWeather_station(HashMap<Object, Object> map) {
        String url = "jdbc:sqlite:/var/www/Weather_station.db";//jdbc:sqlite:DIRECTORY OF DATABASE
        try {
            Connection c = DriverManager.getConnection(url);
            Statement s = c.createStatement();
            for (Map.Entry<Object, Object> next : map.entrySet()) { //Iterating through the HashMap and putting the values in the database
                s.executeUpdate("INSERT INTO 'Weather_station'" + "VALUES ('" + next.getKey() + "" + next.getValue() + "',12,200)");
            }
            System.out.println("Successfully updated table: Weather_station");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }


    //Method to create tables in database

    public static void creatingTables() {
        String url = "jdbc:sqlite:/var/www/Weather_station.db";//jdbc:sqlite:DIRECTORY OF DATABASE
        String example = "bruh";
        String sql = "CREATE TABLE IF NOT EXISTS " + example + " (\n"
                + " column1 integer PRIMARY KEY,\n"
                + " column2  NOT NULL,\n"
                + " column3 integer\n"
                + ");";
        try {
            Connection c = DriverManager.getConnection(url);
            Statement stmt = c.createStatement();
            stmt.execute(sql);
            System.out.println("Successfully updated the Database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}