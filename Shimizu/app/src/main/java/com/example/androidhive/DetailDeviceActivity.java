package com.example.androidhive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidhive.JSONParser.json;

public class DetailDeviceActivity extends Activity {

    private TextView nameDeviceDetailTextView;
    private TextView temperatureTextView;
    private TextView pHTextView;
    private TextView clarityTextView;
    private TextView DOTextView;
    private TextView salinityTextView;
    private TextView ORPTextView;
    private TextView water_flowTextView;
    private TextView light_intensityTextView;
    private ProgressDialog pDialog;
    private String idDevice;
    private String nameDeviceDetail;
    private String temperature;
    private String pH;
    private String clarity;
    private String DO;
    private String salinity;
    private String ORP;
    private String water_flow;
    private String light_intensity;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        TextView nameDeviceDetailTextView = (TextView) findViewById(R.id.nameDeviceDetail);
        TextView temperaturelTextView = (TextView) findViewById(R.id.temperature);
        TextView pHTextView = (TextView) findViewById(R.id.pH);
        TextView clarityTextView = (TextView) findViewById(R.id.clarity);
        TextView DOTextView = (TextView) findViewById(R.id.DO);
        TextView salinityTextView = (TextView) findViewById(R.id.salinity);
        TextView ORPTextView = (TextView) findViewById(R.id.ORP);
        TextView water_flowTextView = (TextView) findViewById(R.id.water_flow);
        TextView light_intensityTextView = (TextView) findViewById(R.id.light_intensity);

        idDevice = getIntent().getStringExtra("idDevice");
        nameDeviceDetail = getIntent().getStringExtra("nameDevice");
    }

    /**
     * Background Async Task to Load all sensor by making HTTP Request
     * */
    class LoadAllDevices extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailDeviceActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Inbox JSON
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> paramGetDevices = new ArrayList<NameValuePair>();

            // getting JSON string from URL
            paramGetDevices.add(new BasicNameValuePair("ZUMO-API-VERSION", "2.0.0"));
            paramGetDevices.add(new BasicNameValuePair("deviceId", idDevice));
            JSONArray jsonArrayDevices = jsonParser.makeHttpRequestGetArrayJson("http://shimizuappservice.azurewebsites.net/tables/DeviceData", "GET",
                    paramGetDevices);

            // Check your log cat for JSON reponse
            if(json == null)
            {
                Log.d("Inbox Error", "#DEBUG");
                return null;
            }
            else {
                Log.d("Inbox JSON: ", json.toString());
            }

            try {
                // looping through All messages
                for (int i = 0; i < jsonArrayDevices.length(); i++) {
                    JSONObject c = jsonArrayDevices.getJSONObject(i);
                    temperature = c.getString("s1");
                    pH = c.getString("s2");
                    clarity = c.getString("s3");
                    DO = c.getString("s4");
                    salinity = c.getString("s5");
                    ORP = c.getString("s6");
                    water_flow = c.getString("s7");
                    light_intensity = c.getString("s8");
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    nameDeviceDetailTextView.setText("Phao : " + nameDeviceDetail);
                    temperatureTextView.setText("Nhiet do : " + temperature);
                    pHTextView.setText("pH : " + pH);
                    clarityTextView.setText("Do trong : " + clarity);
                    DOTextView.setText("DO : " + DO);
                    salinityTextView.setText("Do man : " + salinity);
                    ORPTextView.setText("ORP : " + ORP);
                    water_flowTextView.setText("Dong chay : " + water_flow);
                    light_intensityTextView.setText("Do sang : " + light_intensity);

                }
            });

        }

    }
}
