package com.test;

import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.wide.preferences.bean.HostConfigurationSettings;

public class GsonTest {

    public static String getJsonStr() {

        Gson gson = new Gson();

        JsonArray jsonArray = new JsonArray();

        String windchillHost = "192.168.197.134";
        String hostUser = "admin";
        String hostUserPassword = "wcadmin";
        String windchillHostOS = "Windows";
        String windchillAdmin = "wcadmin";
        String windchillAdminPassword = "wcadmin";
        String windchillVersion = "Windchill 10.2";
        String httpServerHome = "D:/Desktop/picture/2019-09-06";
        String windchillHome = "D:/Desktop/picture/2019-09-06";
        String windchillDSHome = "D:/Desktop/picture/2019-09-06";

        for (int i = 0; i < 9; i++) {

            HostConfigurationSettings bean = new HostConfigurationSettings(windchillHost, hostUser, hostUserPassword, windchillHostOS, windchillAdmin, windchillAdminPassword, windchillVersion, httpServerHome,
                    windchillHome, windchillDSHome);
            jsonArray.add(gson.toJson(bean));

        }

        return jsonArray.toString();

    }

    public static void parserJson(String hostConfigurationStr) {

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(hostConfigurationStr);
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                JsonPrimitive primitive = (JsonPrimitive) it.next();

                String string = primitive.getAsString();

                HostConfigurationSettings bean = gson.fromJson(string, HostConfigurationSettings.class);

            }

        }

    }

    public static void main(String[] args) {
        String hostConfigurationStr = getJsonStr();
        parserJson(hostConfigurationStr);

        System.out.println("123".compareToIgnoreCase("123"));
        
    }

}
