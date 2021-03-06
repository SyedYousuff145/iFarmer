package com.example.syedyousuff.farmer1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syed Yousuff on 4/15/2016.
 */
public class BuyActivity extends Activity{

    private String input = "";
    private String location = "";
    private String store = "";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Spinner selectInput = (Spinner)findViewById(R.id.spinner1);
        Spinner selectLocation = (Spinner)findViewById(R.id.spinner2);
        Button search = (Button)findViewById(R.id.button4);
        listView = (ListView)findViewById(R.id.listView);

        String[] inputItems = new String[]{"Fertilizer", "Pesticide", "Hybrid Seeds", "Tools"};
        String[] locationItems = new String[]{"Hubli", "Dharwad", "Kannur", "Shimoga", "Hospet"};

        ArrayAdapter<String> adapter_items = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, inputItems);
        ArrayAdapter<String> adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locationItems);

        selectInput.setAdapter(adapter_items);
        selectLocation.setAdapter(adapter_location);

        selectInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
                Talker t = new Talker(BuyActivity.this);
                String[] params = new String[]{"getStoreDetails", parent.getItemAtPosition(position).toString(), input};
                t.execute(params);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String[] stores = new String[]{"Kisan Store", "Rural Co-operative", "Salim Merchants", "Deepa Agri-store", "Meena Store", "Central Market", "Agarwal Brothers", "Mangalore Agri-store", "Udupi Fertilizers"};
                Talker t = new Talker(BuyActivity.this);
                String[] params = new String[]{"getStores", input, location};
                t.execute(params);
                String toastMessage = "Input: " + input + "\nLocation: " + location;
                //Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void listStores(String a)
    {
        String[] stores = a.split(",");

        ArrayAdapter<String> adapter_markets = new ArrayAdapter<String>(BuyActivity.this, android.R.layout.simple_expandable_list_item_1, stores);
        listView.setAdapter(adapter_markets);
    }

    public void showStoreDetails(String a)
    {
        Intent i = new Intent(getApplicationContext(), StoreActivity.class);

        i.putExtra("storeDetails", a);
        startActivity(i);
        //String[] details = a.split(",");
        //Toast.makeText(getApplicationContext(), a , Toast.LENGTH_LONG).show();
    }

    class Talker extends AsyncTask<String, Void, String> {
        String URL = "http://192.168.137.1/SE_App/HomePage.php";

        private Activity activity;
        private ProgressDialog dialog;
        private Context context;
        private TextView message;
        Context parentContext;
        private String task = "";

        private String responseString = "";
        private boolean done = false;

        public Talker(Activity activity){
            parentContext = activity;
            //this.activity = activity;
            //this.context = activity;
            this.dialog = new ProgressDialog(activity);
            this.dialog.setTitle("Retrieving Data");
            this.dialog.setMessage("Please wait..");
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if(!this.dialog.isShowing()){
                this.dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            task = params[0];

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            switch(task)
            {
                case "getStores":
                    nameValuePairs.add(new BasicNameValuePair("task", task));
                    nameValuePairs.add(new BasicNameValuePair("input", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("location", params[2]));
                    break;

                case "getStoreDetails" :
                    nameValuePairs.add(new BasicNameValuePair("task", task));
                    nameValuePairs.add(new BasicNameValuePair("storeName", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("input", params[2]));
                    break;
            }

            //nameValuePairs.add(new BasicNameValuePair("id", id));

            String s = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity ent = response.getEntity();
                s = EntityUtils.toString(ent);

                //Toast.makeText(activity,responseStr,Toast.LENGTH_LONG).show();

            } catch (ClientProtocolException e) {
                return "failure";

            } catch (IOException e) {
                return "failure";
            }


            return s;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //Toast.makeText(parentContext, res, Toast.LENGTH_LONG).show();
            responseString = res;

            switch(task)
            {
                case "getStores":
                    listStores(responseString);
                    break;
                case "getStoreDetails":
                    showStoreDetails(responseString);
                    break;
            }

        }


    }
}
