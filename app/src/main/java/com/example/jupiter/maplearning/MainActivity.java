package com.example.jupiter.maplearning;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity  {

    GoogleMap mMap;

    private static final int ERROR_DIALOGUE_REQUEST=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(servicesOk())
        {
            setContentView(R.layout.activity_map);
            if(initMap()) {


            }
            else{
                Toast.makeText(this, "Map Not Connected", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            setContentView(R.layout.activity_main);
        }
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
        switch(id){
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }




        return super.onOptionsItemSelected(item);
    }

    public boolean servicesOk()
    {
        int isAvailable= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable== ConnectionResult.SUCCESS)
        {
            return  true;
        }
        else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog=GooglePlayServicesUtil.getErrorDialog(isAvailable,this,ERROR_DIALOGUE_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this,"Can't connect to mapping service",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private  boolean initMap()
    {
        if(mMap==null)
        {
            SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap=mapFragment.getMap();
        }
        return(mMap != null);
    }
    private void gotoLocation(double lat,double lng,float zoom)
    {
        LatLng latLng= new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(update);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.locationicon)));
    }
    public  void hideSoftKeyboard(View view)
    {
        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void geoLocate(View view) throws IOException {
        mMap.clear();
        hideSoftKeyboard(view);

        EditText etLocation= (EditText) findViewById(R.id.etLocation);
        String searchString= etLocation.getText().toString();

        Geocoder gc=new Geocoder(this);
        List<Address> list=gc.getFromLocationName(searchString,10);

        int sz=list.size();

        if(sz>0)
        {
            Address add=list.get(0);
            String locality=add.getLocality();
            //Log.d("Akshay",locality);
            //Log.d("Akshay", add.getPostalCode());
            //Log.d("Akshay","size is"+sz +list.get(0).getLocality()+list.get(1).getLocality()+list.get(2).getLocality());
            //Toast.makeText(this,"Found : " + locality,Toast.LENGTH_SHORT).show();
            double lat=add.getLatitude();
            double lng=add.getLongitude();
            gotoLocation(lat, lng, 3);
            //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));


        }

    }





}
