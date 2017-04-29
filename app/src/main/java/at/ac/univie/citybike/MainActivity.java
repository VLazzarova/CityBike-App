package at.ac.univie.citybike;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<>();

    private String city;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView citiesView = (ListView) findViewById(R.id.citiesList);

        final ArrayList<String> citiesList =  new ArrayList<>(asList("Wr.Neustadt, AT","Wien, AT", "Salt Lake City, US"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, citiesList);

        //final String[] clickedOn = new String[1];

        citiesView.setAdapter(adapter);
        citiesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONHandler.setValuesToNull();
                Log.i("You clicked on:", citiesList.get(position));
                String clickedOn;
                clickedOn = citiesList.get(position);
                String[] split = clickedOn.split(",");
                city = split[0];
                DownloadAPITask task = new DownloadAPITask();
                task.execute("https://api.citybik.es/v2/networks");
            }
        });


    }


    public class DownloadAPITask extends AsyncTask<String, Void, String> {

    /**
     * Getting the passed URL, builds a Request and connection.
     * @param urls
     * @return
     */
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("WE ARE IN doInBackground");

            String url = urls[0];

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
               Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error by request");
            }
            return null;
        }

        /**
         * After the doInBackground is done, it passes the result
         * to the onPostExecute function.
          * @param response
         */
        @Override
        protected void onPostExecute(String response) {
            System.out.println("WE ARE IN onPostExecute");
            super.onPostExecute(response);

            try {
            JSONHandler.jsonMagic(response, city);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}

