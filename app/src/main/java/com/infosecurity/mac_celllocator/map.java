package com.infosecurity.mac_celllocator;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.crypto.Mac;


public class map extends FragmentActivity {

    final Context context = this;
    public double userLatitude;
    public double userLongitude;
    public double otherLatitude;
    public double otherLongitude;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private LatLng userLoc, otherLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        otherLatitude = 39.1005;
        otherLongitude = -84.5162;
        otherLoc = new LatLng(otherLatitude,otherLongitude);

        userLatitude = 39.1000;
        userLongitude = -84.5167;

        Intent iin= getIntent();
        Bundle b = iin.getExtras();



        if(b!=null)
        {
            double j = b.getDouble("otherlatitudeMAC");
            otherLatitude = j;
            double i = b.getDouble("otherlongitudeMAC");
            otherLongitude = i;
        }

        setUpMapIfNeeded();
        userLoc = new LatLng(userLatitude, userLongitude);



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        try {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
        switch(item.getItemId()){
            case R.id.activity_mac:
                Intent mac = new Intent(context, MAC.class);
                startActivity(mac);
                break;
            case R.id.activity_map:
                Intent map = new Intent(context, map.class);
                startActivity(map);
                break;
            case R.id.activity_triangle:
                Intent triangle = new Intent(context, triangle.class);
                startActivity(triangle);
                break;
            case R.id.activity_register_access_point_location:
                Intent registerAccessPointLocation = new Intent(context, RegisterAccessPointLocation.class);
                startActivity(registerAccessPointLocation);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //Return false to allow normal menu processing to proceed,
        //true to consume it here.
        return false;
    }

    public void placeUserPin(View view)
    {
        /*
        TextView text=(TextView)findViewById(R.id.userlat);
        TextView text2=(TextView)findViewById(R.id.userlong);
        text.setText(""+userLatitude);
        text2.setText(""+userLongitude);
        mMap.addMarker(new MarkerOptions().position(new LatLng(userLatitude, userLongitude)).title("User Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc,18.0f));
        */
    }

    public void placeOtherPin(View view)
    {
        /*
        TextView text=(TextView)findViewById(R.id.otherlat);
        TextView text2=(TextView)findViewById(R.id.otherlong);
        text.setText(""+otherLatitude);
        text2.setText(""+otherLongitude);
        mMap.addMarker(new MarkerOptions().position(new LatLng(otherLatitude, otherLongitude)).title("Other Location"));
        */
    }
}

