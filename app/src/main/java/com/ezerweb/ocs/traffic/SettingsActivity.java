package com.ezerweb.ocs.traffic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdView mAdView = (AdView) findViewById(R.id.adViewSetting);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //get saved location data
        SharedPreferences prefs = getSharedPreferences("Locations", MODE_PRIVATE);
        String locA = prefs.getString("LocationA", "");
        String locB = prefs.getString("LocationB", "");

        final EditText editLocA = (EditText) findViewById(R.id.editText_LocA);
        editLocA.setText(locA);
        final EditText editLocB = (EditText) findViewById(R.id.editText_LocB);
        editLocB.setText(locB);

        //save location data
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                // Check location inputs
                String locA = editLocA.getText().toString();
                String locB = editLocB.getText().toString();

                if (TextUtils.isEmpty(locA) || TextUtils.isEmpty(locB) ){
                    Snackbar.make(v, "Please input Address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                // save
                SharedPreferences.Editor editor = getSharedPreferences("Locations", MODE_PRIVATE).edit();
                editor.putString("LocationA", locA);
                editor.apply();
                editor.putString("LocationB", locB);
                editor.apply();

                //Snackbar.make(v, "Saved", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Toast toast = Toast.makeText(getApplicationContext(), "Locations Saved", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
