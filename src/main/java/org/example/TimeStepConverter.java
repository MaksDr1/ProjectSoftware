package org.example;

public class TimeStepConverter {

    // class that converts time from sensors to readable date and time for Database

    static public String convertToRealTime(String EpochtimeStamp) {
        long epoch = Long.parseLong(EpochtimeStamp);

        return new java.text.SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(new java.util.Date (epoch*1000));
    }
}
