package at.ac.univie.citybike;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class is creating a web connection
 * to get the data from the web.
 *
 */
public class JSONDownloader {
    /**
     * <code>getDataFromWeb</code> is creating a request to the
     * web page to get the json object
     * @param urls the url passed from the main page
     * @return a JSONObject
     */
    private static Response response;

    public static JSONObject getDataFromWeb (String... urls) {


        String url = urls[0];


        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error by request");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Sth happened when returning the JSON");
        }
        return null;
    }
}