package at.ac.univie.citybike;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String i = "hiiii";
        Log.i("LOG", i);

        DownloadAPITask task = new DownloadAPITask();
        task.execute("https://api.citybik.es/v2/networks");
    }

    public class DownloadAPITask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            JSONObject json = JSONDownloader.getDataFromWeb(urls[0]);

            if((json != null) && (json.length() > 0)) {
                try {
                    JSONArray networks = json.getJSONArray("networks");

                    Log.i("Networks", networks.toString());

                    int length = networks.length();
                    for(int i = 0; i < length; i++) {
                        JSONObject obj = networks.getJSONObject(i);
                        JSONObject location = obj.getJSONObject("location");
                        Log.i("Location", location + "\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);



        }
    }
}

