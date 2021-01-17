package org.example;

/* Task of this class is to parse received data and get it prepared to be sent to the database
   all data is saved to the hashmap with appropriate keys and according values*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class InfoParser {

    private HashMap<String, String> data;

    //Conversion method for device id, port and hardware_serial number
    private void DeviceInfoConvert(JSONObject obj) {
        data.put("timestamp", Long.toString(System.currentTimeMillis()/1000));
        try {
            data.put("dev_id", String.valueOf(obj.getString("dev_id")));
            data.put("port", String.valueOf(obj.getInt("port")));
            data.put("hardware_serial", String.valueOf(obj.getString("hardware_serial")));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Conversion method for pressure, temperature and light
    //Also decoder included to decode and receive data in proper format
    private void DataInfoConvert(JSONObject obj) {
        try {
            String payload  = obj.getString("payload_raw");
            System.out.println("payload: " + payload);

            byte[] bytes = Base64.getDecoder().decode(payload);

            //getting the data
            float pressure = (float) Byte.toUnsignedInt(bytes[0])/2+950;
            float light = (float) Byte.toUnsignedInt(bytes[1]);
            float temperature = (((float) Byte.toUnsignedInt(bytes[2])-20)*10+bytes[3])/10;
            data.put("pressure", String.valueOf(pressure));
            data.put("temperature", String.valueOf(temperature));
            data.put("light", (String.valueOf(light)));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method for receiving information about each device location, such as gtw_id, latitude and longitude
    private void locationInfo(JSONObject obj) {

        JSONArray jsonArray = null;
        try {
            jsonArray = (obj.getJSONObject("metadata").getJSONArray("gateways"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        for(int index = 0; index < jsonArray.length(); index++ ) {
            try {
                JSONObject object = jsonArray.getJSONObject(index);

                data.put("gtw_id",   object.getString("gtw_id"));
                data.put("latitude", String.valueOf(object.getDouble("latitude")));
                data.put("longitude", String.valueOf(object.getDouble("longitude")));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //The goal of this method is to put all information to the hasmap and catch exceptions
    public void pushInfo(String json) {
        data = new HashMap<>();

        JSONObject obj = null;
        try {

            obj = new JSONObject(json);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        assert obj != null;
        DeviceInfoConvert(obj);
        DataInfoConvert(obj);
        //locationInfo(obj);
    }

    //The goal of this method is to optimize information savings in the database by changing dev_id string into number
    public  HashMap<String, String> checkData(HashMap<String, String> parsedData) {
        for (Map.Entry<String, String> en : parsedData.entrySet()) {
            if (parsedData.containsValue("pysaxion")) {
                parsedData.put("dev_id", "1");

            } else if (parsedData.containsValue("pywierden")) {
                parsedData.put("dev_id", "2");
            }
            else if (parsedData.containsValue("pygronau")) {
                parsedData.put("dev_id", "3");
            }
            else if (parsedData.containsValue("pygarage")) {
                parsedData.put("dev_id", "4");
            }
        }
        return parsedData;
    }


    //The goal of this method is to callect all saved information from the sensors
    public HashMap<String, String> getData()
    {
        return data;
    }
}