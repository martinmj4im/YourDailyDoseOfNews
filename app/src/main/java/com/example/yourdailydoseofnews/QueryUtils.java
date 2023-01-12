package com.example.yourdailydoseofnews;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QueryUtils {


    public static ArrayList<Story> fetchNewsData(String requestUrl)  {
        String jsonResponse = null;
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Story> Stories = new ArrayList<>();
        try {
            Stories = extractFeatureFromJson(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Stories;
    }

    public static String makeHttpRequest(URL url) throws IOException {

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                 inputStream = urlConnection.getInputStream();
                 jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            line = bufferedReader.readLine();
            while (line != null){
                output = output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList extractFeatureFromJson(String jsonResponse) throws JSONException {
       ArrayList<Story> Stories = new ArrayList<>();
       String author = "";


        if (jsonResponse == null){
            return Stories;
        }

        JSONObject root = new JSONObject(jsonResponse);
        JSONObject response = root.getJSONObject("response");
        JSONArray results = response.getJSONArray("results");
        for(int i = 0; i < results.length(); i++){
            JSONObject position = results.getJSONObject(i);
            String title = position.getString("webTitle");
            String section = position.getString("sectionName");
            String date = position.getString("webPublicationDate");
            String intentUrl = position.getString("webUrl");
            if(position.has("tags")){
            JSONArray tags = position.getJSONArray("tags");
            if(tags != null && tags.length() > 0){
            JSONObject first_tag = tags.getJSONObject(0);
            if(first_tag.has("webTitle")){
             author = first_tag.getString("webTitle");}
            }
            else {author = "";}
            }


            Stories.add(new Story(title,section,date,intentUrl,author));
        }

        return Stories;
    }

}
