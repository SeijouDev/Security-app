package com.app.inpahu.securityapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.app.inpahu.securityapp.Helpers.Generics;
import com.app.inpahu.securityapp.Helpers.OSMHelper;
import com.app.inpahu.securityapp.Helpers.OnTaskCompleted;
import com.app.inpahu.securityapp.Helpers.PreferencesHelper;
import com.app.inpahu.securityapp.Helpers.SingleShotLocationProvider;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    private MapView map = null;
    private OSMHelper mapHelper;
    private Context mContext;

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

    public void onResume(){
        super.onResume();
        if(map != null) map.onResume();
    }

    public void onPause(){
        super.onPause();
        if(map != null)  map.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                PreferencesHelper.deleteUser(mContext);
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean result = true;

        switch (requestCode) {
            case 1: {
                for(int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        result = false;
                    }
                }
            }
        }

        if(!result || permissions.length <= 0) {
            Toast.makeText(mContext, "Es necesario aceptar los permisos para continuar", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            configView();
        }
    }

    private OnClickListener fabCreateReportClicked = new OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), CreateReportActivity.class));
            finish();
        }
    };

    private OnClickListener searchAddressClicked = new OnClickListener() {
        @Override
        public void onClick(View view) {
            String address = String.valueOf(( (EditText) findViewById(R.id.address_request_edit_text)).getText()).trim();

            if(address.length() > 0) {
                Log.e("ADDRESS" , address);
            }
            else {
                Toast.makeText(getApplicationContext(), "No has ingresado la dirección", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void configView() {
        setContentView(R.layout.activity_main);
        setTitle(PreferencesHelper.getUser(mContext).getName());

        (findViewById(R.id.fab_add_report)).setOnClickListener(fabCreateReportClicked);
        (findViewById(R.id.search_address_button)).setOnClickListener(searchAddressClicked);
        configMap();
    }

    private void configMap () {
        map = findViewById(R.id.map);
        mapHelper = new OSMHelper(map);
        mapHelper.initMap(map);
        mapHelper.setZoom(12d);
        //mapHelper.setClickListener(getBaseContext());
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
}

