package com.ezerweb.ocs.traffic;

import org.json.JSONException;
import org.json.JSONObject;

public class DurationJSONParser {

    public static String parseFeed(String content) {

        try {

            JSONObject jsonRespRouteDistance = new JSONObject(content)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray ("elements")
                    .getJSONObject(0)
                    .getJSONObject("duration");

            String Duration = jsonRespRouteDistance.get("text").toString();

            return Duration;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
