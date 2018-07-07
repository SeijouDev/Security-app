package com.app.inpahu.securityapp;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {
    MapView map = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(9d);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        (findViewById(R.id.fab_add_report)).setOnClickListener(fabCreateReportClicked);
        (findViewById(R.id.search_address_button)).setOnClickListener(searchAddressClicked);

        
    }

    public void onResume(){
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
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
}

