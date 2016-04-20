package com.example.syedyousuff.farmer1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syed Yousuff on 4/15/2016.
 */
public class CheckCropActivity extends Activity {
    private String crop = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkcrop);

        Spinner selectCrop = (Spinner)findViewById(R.id.spinner);
        Button search = (Button)findViewById(R.id.button5);

        String[] cropItems = new String[]{"Wheat", "Rice", "Jowar", "Sugarcane", "Ragi"};
        ArrayAdapter<String> adapter_crops = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cropItems);

        selectCrop.setAdapter(adapter_crops);

        selectCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crop = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String[] stores = new String[]{"Kisan Store", "Rural Co-operative", "Salim Merchants", "Deepa Agri-store", "Meena Store", "Central Market", "Agarwal Brothers", "Mangalore Agri-store", "Udupi Fertilizers"};
                Talker t = new Talker(CheckCropActivity.this);
                String[] params = new String[]{"checkCrop", crop};
                t.execute(params);
                //String toastMessage = "Input: " + input + "\nLocation: " + location;
                //Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();

            }
        });


    }

    public void showCropDetails(String a)
    {
        TextView soil = (TextView)findViewById(R.id.soilType);
        TextView temp = (TextView)findViewById(R.id.temperature);
        TextView rainfall = (TextView)findViewById(R.id.railfall);
        TextView price = (TextView)findViewById(R.id.price);

        String[] details = a.split(",");

        soil.setText(details[0]);
        temp.setText(details[1]);
        rainfall.setText(details[2]);
        price.setText(details[3]);


        //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
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
                case "checkCrop":
                    nameValuePairs.add(new BasicNameValuePair("task", task));
                    nameValuePairs.add(new BasicNameValuePair("crop", params[1]));
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
                case "checkCrop":
                    showCropDetails(responseString);
                    break;

            }

        }


    }
}
