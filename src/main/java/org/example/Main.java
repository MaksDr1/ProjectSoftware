package org.example;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args){
        String broker = "tcp://eu.thethings.network:1883";
        String clientId = MqttAsyncClient.generateClientId(); //done so to prevent time connection exceptions
        String password = "ttn-account-v2.OC-mb7b1C5rDmos7-XTSoNE5T85V3c20jnrE8uN4jS0";
        String user = "project-software-engineering";

        //starts connection and will show if some errors with connection has occurred
        ConnectionToMQTT toMQTT = new ConnectionToMQTT(broker, password, user, clientId);
        toMQTT.startConnToMQTT();
        //initializing parser
        InfoParser parser = new InfoParser();

        //establish connection to the database
        try {
            Database.connectionTest();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        boolean flag = true;

        while (flag)
        {
            //Hashmap containing parsed data
            HashMap<String, String> parsedData;

            String unparsedData = toMQTT.getData();

            //delay section to prevent read or write  errors
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //only if there is dаta, call the parser
            if (unparsedData != null)
            {
                //convert and put data to the  hashmap

                parser.pushInfo(unparsedData);

                //Returning parsed data
                parsedData = parser.getData();
                //Method for conversion dev_id of sensors to numbers
                HashMap<String, String> newData = parser.checkData(parsedData);
                Database.insertData(newData);
                System.out.println(newData);
            }
        }
    }
}