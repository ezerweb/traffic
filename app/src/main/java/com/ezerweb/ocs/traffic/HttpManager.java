package com.ezerweb.ocs.traffic;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            //uri = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyAlPPrkosomlrQZPYXxDwJVhSPSvaMI6-8";
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Host", "www.google.com");
//            con.setRequestProperty("Accept-Charset", "UTF-8");

            StringBuilder sb = new StringBuilder();

            //reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            InputStream inputStream;
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK)
                inputStream = con.getInputStream();
            else
                inputStream = con.getErrorStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
}
