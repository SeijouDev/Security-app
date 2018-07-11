package com.app.inpahu.securityapp.Helpers;

import android.content.Context;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class OSMHelper {

    private MapView map;
    private IMapController mapController;
    private Marker mMarker;

    public OSMHelper (MapView mView) {
        this.map = mView;
        this.mapController = this.map.getController();
    }

    public void initMap(MapView map){
        this.map.setTileSource(TileSourceFactory.MAPNIK);
        this.map.setBuiltInZoomControls(true);
        this.map.setMultiTouchControls(true);
    }

    public void setZoom(double level) {
        this.mapController.setZoom(level);
    }

    public void setCenter(double lat, double lng) {
        this.mapController.setCenter(new GeoPoint(lat, lng));
    }

    public Marker addMarker(double lat, double lng, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(lat,lng));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle( (title != null) ? title : "Sin información" );
        map.getOverlays().add(marker);
        setZoom(map.getZoomLevelDouble());
        return marker;
    }

    public Marker addMarkerV2(Context mContext, double lat, double lng, String title, int idIcon) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(lat,lng));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle( (title != null) ? title : "Sin información" );
        marker.setIcon(mContext.getResources().getDrawable(idIcon));
        map.getOverlays().add(marker);
        setZoom(map.getZoomLevelDouble());
        return marker;
    }

    public void removeMarker(Marker marker) {
        map.getOverlays().remove(marker);
    }

    public void setClickListener(final Context mContext, final OnTaskCompleted reverseGeocodingCallback) {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if(mMarker != null) {
                    removeMarker(mMarker);
                    mMarker = null;
                }

                mMarker = addMarker(p.getLatitude(),p.getLongitude() , null);
                new CustomTask.GeocodeTask(mContext, p.getLatitude(),p.getLongitude(), reverseGeocodingCallback).execute();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(mContext, mReceive);
        map.getOverlays().add(OverlayEvents);
    }


}
