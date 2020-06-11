package com.example.thomasburch.localchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.preference.PreferenceManager;
import java.math.BigInteger;
import java.security.SecureRandom;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "ChatApplication";
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private LocationData locationData = LocationData.getLocationData();//store location to share between activities

    AppInfo appInfo;
    String my_user_id;
    String my_nickname;
    String latitude;
    String longitude;

    public static String nickname;

    public final class SecureRandomString {
        private SecureRandom random = new SecureRandom();
        public String nextString() {
            return new BigInteger(130, random).toString(32);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appInfo = AppInfo.getInstance(this);

        // Gets the settings, and creates a random user id if missing.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        my_user_id = settings.getString("user_id", null);
        if (my_user_id == null) {
            // Creates a random one, and sets it.
            SecureRandomString srs = new SecureRandomString();
            my_user_id = srs.nextString();
            //my_user_id = "mrlucky";
            SharedPreferences.Editor e = settings.edit();
            e.putString("my_user_id", my_user_id);
            e.commit();
        }
    }

        @Override
        public void onResume () {
            Log.i(LOG_TAG, "Inside resume of main activity");

            //check if user already gave permission to use location
            Boolean locationAllowed = checkLocationAllowed();
            Button searchButton = (Button) findViewById(R.id.searchButton);

            if (locationAllowed) {
                requestLocationUpdate();
            } else {
                searchButton.setEnabled(false);//search button must not be enabled until we have a location
            }
            //Set the button text between "Enable Location" or "Disable Location"
            render();
            super.onResume();
        }

	/*
	Check users location sharing setting
	 */

    private boolean checkLocationAllowed() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("location_allowed", false);
    }

    /*
    Persist users location sharing setting
     */
    private void setLocationAllowed(boolean allowed) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("location_allowed", allowed);
        editor.commit();
    }

    /*
    Set the button text between "Enable Location" or "Disable Location"
     */
    private void render() {
        Boolean locationAllowed = checkLocationAllowed();
        Button button = (Button) findViewById(R.id.button);

        if (locationAllowed) {
            button.setText("Disable Location");
        } else {
            button.setText("Enable Location");
        }
    }

    /*
    Request location update. This must be called in onResume if the user has allowed location sharing
     */
    private void requestLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

                Log.i(LOG_TAG, "requesting location update");
            } else {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Log.i(LOG_TAG, "please allow to use your location");

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } else {
            Log.i(LOG_TAG, "requesting location update from user");
            //prompt user to enable location
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

                        Log.i(LOG_TAG, "requesting location update");
                    } else {
                        throw new RuntimeException("permission not granted still callback fired");
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*
    Remove location update. This must be called in onPause if the user has allowed location sharing
     */
    private void removeLocationUpdate() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                locationManager.removeUpdates(locationListener);
                Log.i(LOG_TAG, "removing location update");
            }
        }
    }

    public void toggleLocation(View v) {

        Boolean locationAllowed = checkLocationAllowed();

        if (locationAllowed) {
            //disable it
            removeLocationUpdate();
            setLocationAllowed(false);//persist this setting

            Button searchButton = (Button) findViewById(R.id.searchButton);
            searchButton.setEnabled(false);//now that we cannot use location, we should disable search facility

        } else {
            //enable it
            requestLocationUpdate();
            setLocationAllowed(true);//persist this setting
        }

        //Set the button text between "Enable Location" or "Disable Location"
        render();
    }

    @Override
    public void onPause() {
        if (checkLocationAllowed())
            removeLocationUpdate();//if the user has allowed location sharing we must disable location updates now
        super.onPause();
    }

    /**
     * Listens to the location, and gets the most precise recent location.
     * Copied from Prof. Luca class code
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Location lastLocation = locationData.getLocation();

            // Do something with the location you receive.
            double newAccuracy = location.getAccuracy();

            long newTime = location.getTime();
            // Is this better than what we had?  We allow a bit of degradation in time.
            boolean isBetter = ((lastLocation == null) ||
                    newAccuracy < lastLocation.getAccuracy() + (newTime - lastLocation.getTime()));
            if (isBetter) {
                // We replace the old estimate by this one.
                locationData.setLocation(location);

                //Now we have the location.
                Button searchButton = (Button) findViewById(R.id.searchButton);
                if (checkLocationAllowed())
                    searchButton.setEnabled(true);//We must enable search button
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void search(View v) {

        appInfo = AppInfo.getInstance(this);

        // Gets the settings, and creates global nickname
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        my_nickname = settings.getString("my_nickname", null);
        EditText editText = (EditText) findViewById(R.id.editText);
        my_nickname = editText.getText().toString();

        latitude = settings.getString("my_lat", null);
        longitude = settings.getString("my_lng", null);

        //convert latitude and longitude into acceptable format
        latitude = Double.toString(locationData.getLocation().getLatitude());
        longitude = Double.toString(locationData.getLocation().getLongitude());

        //latitude = "3.00";
        //longitude = "7.00";

        // Store my nickname, latitude, longitude in AppInfo
        SharedPreferences.Editor e = settings.edit();
        e.putString("my_nickname", my_nickname);
        e.putString("my_lat", latitude);
        e.putString("my_lng", longitude);
        e.commit();

        Log.i(LOG_TAG, "Lat is: " + latitude);
        Log.i(LOG_TAG, "Long is: " + longitude);
        Log.i(LOG_TAG, "my_user_id is: " + my_user_id);
        Log.i(LOG_TAG, "my_nickname is: " + my_nickname);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(nickname, my_nickname);
        startActivity(intent);//pass the my_nickname to the search activity for searching
    }
}