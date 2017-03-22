package com.ezerweb.ocs.traffic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private String LocationA, LocationB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdView mAdView = (AdView) findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Button buttonViewMapA = (Button) findViewById(R.id.buttonViewMap1);
        buttonViewMapA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(LocationA) && !TextUtils.isEmpty(LocationB)) {
                    viewMap(true);
                }else{
                    Toast.makeText(getApplicationContext(), "Address is empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button buttonViewMapB = (Button) findViewById(R.id.buttonViewMap2);
        buttonViewMapB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(LocationA) && !TextUtils.isEmpty(LocationB)) {
                    viewMap(false);
                }else{
                    Toast.makeText(getApplicationContext(), "Address is empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(LocationA) && !TextUtils.isEmpty(LocationB)) {
                    getLocations();
                }else{
                    Toast.makeText(getApplicationContext(), "Address is empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;

            case R.id.action_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateDisplayA(String result) {

        if (result != "") {
            TextView textViewDurationA = (TextView) findViewById(R.id.textViewDurationValue1);
            textViewDurationA.setText(result);
        }
    }

    protected void updateDisplayB(String result) {

        if (result != "") {
            TextView textViewDurationB = (TextView) findViewById(R.id.textViewDurationValue2);
            textViewDurationB.setText(result);
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void getLocations() {

        SharedPreferences prefs = getSharedPreferences("Locations", MODE_PRIVATE);
        LocationA = prefs.getString("LocationA", "");
        LocationB = prefs.getString("LocationB", "");

        TextView textViewDurationA = (TextView) findViewById(R.id.textViewDurationValue1);
        textViewDurationA.setText("loading...");

        TextView textViewDurationB = (TextView) findViewById(R.id.textViewDurationValue2);
        textViewDurationB.setText("loading...");

        if (!TextUtils.isEmpty(LocationA) && !TextUtils.isEmpty(LocationB)) {

            TextView textViewLocationA = (TextView) findViewById(R.id.textViewFromLocation1);
            textViewLocationA.setText(LocationA);

            TextView textViewLocationB = (TextView) findViewById(R.id.textViewToLocation1);
            textViewLocationB.setText(LocationB);

//            TextView textViewLocationA2 = (TextView) findViewById(R.id.textViewFromLocation2);
//            textViewLocationA2.setText(LocationB);
//
//            TextView textViewLocationB2 = (TextView) findViewById(R.id.textViewToLocation2);
//            textViewLocationB2.setText(LocationA);

            if (isOnline() && LocationA !="" && LocationB !=""){

                //String uri = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyAlPPrkosomlrQZPYXxDwJVhSPSvaMI6-8";
                String uri = "https://maps.googleapis.com/maps/api/distancematrix/json";
                uri += "?units=imperial";
                uri += "&traffic_model=best_guess";
                uri += "&departure_time=now";
                uri += "&origins=" + LocationA.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                uri += "&destinations=" + LocationB.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                uri += "&key=AIzaSyAlPPrkosomlrQZPYXxDwJVhSPSvaMI6-8";  //AIzaSyCDQh6kVBofBXYMXJwTqZ3ObTyU4TvpXak
                MyTaskA taskA = new MyTaskA();
                taskA.execute(uri);

                uri = "https://maps.googleapis.com/maps/api/distancematrix/json";
                uri += "?units=imperial";
                uri += "&traffic_model=best_guess";
                uri += "&departure_time=now";
                uri += "&origins=" + LocationB.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                uri += "&destinations=" + LocationA.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                uri += "&key=AIzaSyAlPPrkosomlrQZPYXxDwJVhSPSvaMI6-8";
                MyTaskB taskB = new MyTaskB();
                taskB.execute(uri);

            }else{
                Toast.makeText(this, " Network isn't available", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, " Please set locations!", Toast.LENGTH_LONG).show();
        }
    }

    private class MyTaskA extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            String Duration = DurationJSONParser.parseFeed(result);
            updateDisplayA(Duration);
        }
    }

    private class MyTaskB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            String Duration = DurationJSONParser.parseFeed(result);
            updateDisplayB(Duration);

        }
    }

    private void viewMap(boolean ascending) {

        //String mapUrl = "https://www.google.com/maps/dir/Washington,DC/New+York+City,NY;
        if (!TextUtils.isEmpty(LocationA) && !TextUtils.isEmpty(LocationB)){
            String mapUrl = "https://www.google.com/maps/dir/";
            if (ascending){
                mapUrl += LocationA.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                mapUrl += "/" + LocationB.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
            }else{
                mapUrl += LocationB.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
                mapUrl += "/" + LocationA.replaceAll(", ", ",").replaceAll("  "," ").replaceAll(" ","+");
            }

            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
            if ( webIntent.resolveActivity(getPackageManager()) != null){
                startActivity(webIntent);
            }
        }else {
            Toast toastB = Toast.makeText(getApplicationContext(), "Set Addresses At Settings", Toast.LENGTH_LONG);
            toastB.show();
        }
    }
}
