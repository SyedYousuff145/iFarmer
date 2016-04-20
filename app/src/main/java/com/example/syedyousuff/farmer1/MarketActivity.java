package com.example.syedyousuff.farmer1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Syed Yousuff on 4/17/2016.
 */
public class MarketActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Intent i = getIntent();
        String temp = i.getStringExtra("marketDetails");
        //Toast.makeText(StoreActivity.this, details, Toast.LENGTH_LONG).show();
        String[] details = temp.split(",");

        TextView name = (TextView)findViewById(R.id.marketName);
        TextView contact = (TextView)findViewById(R.id.marketContact);
        TextView location = (TextView)findViewById(R.id.marketLocation);
        TextView timings = (TextView)findViewById(R.id.marketTimings);
        TextView price = (TextView)findViewById(R.id.m_price);


        name.setText(details[0]);
        contact.setText(details[1]);
        location.setText(details[2]);
        timings.setText(details[3]);
        price.setText(details[4]);

    }
}
