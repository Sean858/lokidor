package com.hackthon.demo.helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hackthon.demo.clzs.Loc;

import javax.json.Json;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteHelper {
    private final static String USER_AGENT = "Mozilla/5.0";

    public static boolean isNormal(String timeStamp, Loc loc){
        // construct the http get request
        try {
            URL url = new URL("http://10.215.40.108:8080/check_location");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            // construct get parameters
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("time", timeStamp);
            parameterMap.put("lat", loc.getLat() + "");
            parameterMap.put("lon", loc.getLon() + "");
            Gson gson = new Gson();
            String jsonString = gson.toJson(parameterMap);
            con.setRequestProperty("User-Agent", RemoteHelper.USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("charset", "utf-8");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(jsonString);
            out.flush();
            out.close();
            int status = con.getResponseCode();
            System.out.println("Response Status: " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null){
                content.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + content.toString());

            String contentJson = content.toString();
            JsonElement element = gson.fromJson (contentJson, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            String st = jsonObj.get("status").toString();
            if (st == "true") return true;
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
