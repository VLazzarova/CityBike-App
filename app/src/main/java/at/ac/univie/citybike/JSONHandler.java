package at.ac.univie.citybike;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Objects;

public class JSONHandler {

    private static String city;
    private static String url = "https://api.citybik.es";
    private static int bikesPerCity = 0;
    private static int free_bikes = 0; // counts all the free bikes
    private static int empty_slots = 0; // counts all the empty slots
    private static int stations = 0;


    public static ArrayList<String> jsonMagic(String jsonBodyStr, String chosenCity) throws JSONException {
        System.out.println("WE ARE GONNA FO SOME MAGIC");
        setCity(chosenCity);

        JSONObject jsonObject = new JSONObject(jsonBodyStr);
        JSONArray networks = getNetworks(jsonObject);
        JSONObject network = getNetwork(networks, chosenCity);

        return null;

    }

    public static JSONArray getNetworks(JSONObject jsonObject) {

            try {
                JSONArray networks = jsonObject.getJSONArray("networks");
                System.out.println("GOT THE NETWORKS JSON");
                return networks;

            } catch (JSONException e) {
                System.out.println("Couldn't get networks array from JSON");
                e.printStackTrace();
            }
        return null;
    }

    public static JSONObject getNetwork(JSONArray networks, String city) throws JSONException {

        System.out.println("getNetwork(), for getting the specific city network");

        for (int i = 0; i < networks.length(); i++) {
            JSONObject networkObj = networks.getJSONObject(i);
            JSONObject location = networkObj.getJSONObject("location");
            String currentCity = location.getString("city");

            if(Objects.equals(currentCity, city)) {
                System.out.println("WOHOOO got the city:"+currentCity);
                String url = JSONHandler.url + networkObj.getString("href");
                GetSecondJson task = new GetSecondJson();
                task.execute(url);
            }
        }
        return null;
    }

    public static int countBikes(int numberOfBikes) {
        return 0;
    }

    public static int countSlots(int numberOfSlots) {
        return 0;
    }

    public static int countAllBikes(int allBikes) {
        return 0;
    }

    public static void setCity(String city) {
        JSONHandler.city = city;
    }

    public static void setBikesPerCity(int bikesPerCity) {JSONHandler.bikesPerCity += bikesPerCity;}

    public static void setEmpty_slots(int empty_slots) {
        JSONHandler.empty_slots += empty_slots;
    }

    public static void setFree_bikes(int free_bikes) {
        JSONHandler.free_bikes = free_bikes;
    }

    public static void setValuesToNull() {JSONHandler.empty_slots = 0; JSONHandler.free_bikes = 0; JSONHandler.bikesPerCity = 0; }

    public static int getEmpty_slots() {
        return empty_slots;
    }

    public static int getFree_bikes() {return free_bikes;}

    public static int getBikesPerCity() {
        return bikesPerCity;
    }
}

class GetSecondJson extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        System.out.println("WILL GET THE SECOND JSON");
        String url = params[0];

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            //System.out.println(response.body().string());
            return response.body().string();

        } catch (IOException e) {
            System.out.println("Could't execute response");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //System.out.println("Printing the response:  "+ s);


        System.out.println("IN POST EXECUTE");
        try {
            JSONObject json    = new JSONObject(s);
            JSONObject network = json.getJSONObject("network");
            JSONArray stations = network.getJSONArray("stations");

            for (int i = 0; i < stations.length(); i++) {
                JSONObject station    = stations.getJSONObject(i);
                String empty_slotsStr = station.getString("empty_slots");
                String free_bikesStr  = station.getString("free_bikes");
                int slots;
                int bikes;

                System.out.println("EMPTY_SLOTS: " + empty_slotsStr + " FREE_BIKES: " + free_bikesStr);

                if(empty_slotsStr.equals("null")) {
                    slots = 0;
                    bikes  = Integer.parseInt(station.getString("free_bikes"));
                } else {
                    slots = Integer.parseInt(station.getString("empty_slots"));
                    bikes  = Integer.parseInt(station.getString("free_bikes"));
                }
                JSONHandler.setEmpty_slots(slots);
                JSONHandler.setFree_bikes(bikes);
                JSONHandler.setBikesPerCity(slots+bikes);

                Log.i("Slots: ", String.valueOf(JSONHandler.getEmpty_slots()));

                float result = calculate();

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("RESULT", result.toString());

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public static float calculate() {

        float result;
        int slots = JSONHandler.getEmpty_slots();
        int all = JSONHandler.getBikesPerCity();

        result = slots/all;

        return result;
    }




