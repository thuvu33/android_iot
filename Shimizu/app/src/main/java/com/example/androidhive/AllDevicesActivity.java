package com.example.androidhive;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.androidhive.JSONParser.json;

public class AllDevicesActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> deviceIdList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_devices_list);
		
		// Hashmap for ListView
        deviceIdList = new ArrayList<HashMap<String, String>>();
 
        // Loading INBOX in Background Thread
        new LoadAllDevices().execute();
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id)
	{
		Toast.makeText(getApplicationContext(), "xxxxx" + deviceIdList.get(position).get("idDevice"),
				Toast.LENGTH_LONG).show();
		Intent myIntent = new Intent(this, DetailDeviceActivity.class);
		myIntent.putExtra("idDevice", deviceIdList.get(position).get("idDevice"));
		myIntent.putExtra("nameDevice", deviceIdList.get(position).get("nameDevice"));
		startActivity(myIntent);
	}

	/**
	 * Background Async Task to Load all Devices  by making HTTP Request
	 * */
	class LoadAllDevices extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllDevicesActivity.this);
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
            JSONArray jsonArrayDevices = jsonParser.makeHttpRequestGetArrayJson("http://shimizuappservice.azurewebsites.net/tables/Device", "GET",
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
                    String name = c.getString("name");
                    String createdAt = c.getString("createdAt");
                    String id = c.getString("id");
                    HashMap<String, String> map = new HashMap<String, String>();

					Log.d("Inbox JSON: ", "#DEBUG " + createdAt);
                    map.put("nameDevice", name);
					map.put("nameDevice", name);
					String timePush = createdAt.replace('T',' ');
                    map.put("timePush", timePush.replace('Z',' '));

                    // adding HashList to ArrayList
                    deviceIdList.add(map);
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
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllDevicesActivity.this, deviceIdList,
							R.layout.show_all_devices, new String[] { "timePush", "nameDevice" },
							new int[] { R.id.createdAt, R.id.nameDevice });

					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}
