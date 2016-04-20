package com.example.syedyousuff.farmer1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Syed Yousuff on 4/16/2016.
 */
public class StoreActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Intent i = getIntent();
        String temp = i.getStringExtra("storeDetails");
        //Toast.makeText(StoreActivity.this, details, Toast.LENGTH_LONG).show();
        String[] details = temp.split(",");

        TextView name = (TextView)findViewById(R.id.storeName);
        TextView contact = (TextView)findViewById(R.id.contact);
        TextView location = (TextView)findViewById(R.id.location);
        TextView timings = (TextView)findViewById(R.id.timings);
        TextView price = (TextView)findViewById(R.id.s_price);


        name.setText(details[0]);
        contact.setText(details[1]);
        location.setText(details[2]);
        timings.setText(details[3]);
        price.setText(details[4]);

    }
}
