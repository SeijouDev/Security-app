package com.app.inpahu.securityapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.inpahu.securityapp.Helpers.CustomTask;
import com.app.inpahu.securityapp.Helpers.DatePickerFragment;
import com.app.inpahu.securityapp.Helpers.Generics;
import com.app.inpahu.securityapp.Helpers.OSMHelper;
import com.app.inpahu.securityapp.Helpers.OnTaskCompleted;
import com.app.inpahu.securityapp.Helpers.PreferencesHelper;
import com.app.inpahu.securityapp.Helpers.SingleShotLocationProvider;
import com.app.inpahu.securityapp.Helpers.TimePickerFragment;
import com.app.inpahu.securityapp.Objects.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.MapView;

public class CreateReportActivity extends AppCompatActivity {

    private MapView map = null;
    private OSMHelper mapHelper;
    private Context mContext;
    private String latTemp = "";
    private String lngTemp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        if(!Generics.validatePermissions(mContext, this)){
            Generics.requestPermissions(mContext, this);
            return;
        }
        else {
            configView();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void onResume(){
        super.onResume();
        if(map != null) map.onResume();
    }

    public void onPause(){
        super.onPause();
        if(map != null)  map.onPause();
    }

    private void configView() {
        setContentView(R.layout.activity_create_report);
        setTitle(PreferencesHelper.getUser(mContext).getName());

        (findViewById(R.id.date_edit_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCallback(dateSelectedCallback);
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        (findViewById(R.id.hour_edit_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setCallback(hourSelectedCallback);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        findViewById(R.id.create_report_button).setOnClickListener(createReportClicked);

        configMap();
    }

    private void configMap () {
        map = findViewById(R.id.map);
        mapHelper = new OSMHelper(map);
        mapHelper.initMap(map);
        mapHelper.setZoom(12d);
        mapHelper.setClickListener(CreateReportActivity.this, reverseGeocodingCallback);
        SingleShotLocationProvider.requestSingleUpdate(mContext, getLocationCallback);
    }

    private OnTaskCompleted getLocationCallback = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            double lat = 4.642511;
            double lng = -74.091156;

            if(response != null) {
                lat = Float.parseFloat(response.split("\\|")[0]);
                lng = Float.parseFloat(response.split("\\|")[1]);
                mapHelper.setZoom(16d);
            }

            mapHelper.setCenter(lat,lng);

        }
    };

    private OnTaskCompleted reverseGeocodingCallback = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            if(response != null) {
                try {

                    latTemp = response.split("\\|")[0];
                    lngTemp = response.split("\\|")[1];
                    String res = response.split("\\|")[2];

                    JSONObject jobj = new JSONObject(res);

                    String address = jobj.getString("display_name");

                    ((EditText) findViewById(R.id.address_edit_text)).setText(address);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                ((EditText) findViewById(R.id.address_edit_text)).setText("");
                Toast.makeText(mContext, "No fue posible obtener la dirección", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnTaskCompleted dateSelectedCallback = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            ((TextView) findViewById(R.id.date_edit_text)).setText(response);
        }
    };

    private OnTaskCompleted hourSelectedCallback = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            ((TextView) findViewById(R.id.hour_edit_text)).setText(response);
        }
    };

    private View.OnClickListener createReportClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String date = ((TextView) findViewById(R.id.date_edit_text)).getText().toString().trim();
            String hour = ((TextView) findViewById(R.id.hour_edit_text)).getText().toString().trim();
            String address = ((TextView) findViewById(R.id.address_edit_text)).getText().toString().trim();

            if(date.length() <= 0)
                Toast.makeText(mContext, "Debes seleccionar una fecha", Toast.LENGTH_SHORT).show();
            else if(hour.length() <= 0)
                Toast.makeText(mContext, "Debes seleccionar una hora", Toast.LENGTH_SHORT).show();
            else if (address.length() <= 0 || latTemp.length() <= 0 || lngTemp.length() <= 0)
                Toast.makeText(mContext, "Debes seleccionar una ubicación", Toast.LENGTH_SHORT).show();
            else {

                try {

                    User u = PreferencesHelper.getUser(mContext);

                    JSONObject data = new JSONObject();
                    data.put("address", address);
                    data.put("latitude", latTemp);
                    data.put("longitude", lngTemp);
                    data.put("dates", date);
                    data.put("hours", hour);
                    data.put("id_user", u.getId());
                    data.put("states", false);

                    new CustomTask.MyAsyncTask(CreateReportActivity.this,"/reports/create",data,reportInsertedCallback).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private OnTaskCompleted reportInsertedCallback = new OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String response) {
            Log.e("RESP", ""+ response);
        }
    };
}
