package org.example;
// Task of this class is to establish a connection with The Things Network and continuously receive data from sensors
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ConnectionToMQTT {
    private MqttClient sampleClient;
    private String loPyData;
    private String prev_loPyData = " ";

    //The goal of this method is to establish connection with the server
    ConnectionToMQTT(String broker, String pass, String user, String clientId) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);

            /*setting connection options, such as:
            //discard unsent messages from a previous run
            library will automatically reconnect ot the server in case of network failure*/

            MqttConnectOptions options = new MqttConnectOptions();
            options.setPassword(pass.toCharArray());
            options.setUserName(user);
            options.setConnectionTimeout(15);//Connection timeout
            options.setKeepAliveInterval(30);//Returns the "keep alive" interval.
            options.setCleanSession(true);//Returns whether the client and server should remember state for the client across reconnects.

            //connecting
            sampleClient.connect(options);
            System.out.println("Connection with MQTTBroker established.");

            //subscribe to the  topic
            sampleClient.subscribe("+/devices/+/up");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }


    //The goal of this method is to connect and receive data from the broker
    public void startConnToMQTT() {
        sampleClient.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) {
            }
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                loPyData = message.toString();
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }


    //Checks if the information is not the same as the previously received
    public String getData(){

        if(loPyData == null){
            loPyData = " ";
        }
        if(!loPyData.equals(prev_loPyData)){
            prev_loPyData = loPyData;
            return loPyData;
        }
        prev_loPyData = loPyData;
        return null;
    }
}