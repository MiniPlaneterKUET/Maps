package mini.planeter.maps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import com.akexorcist.googledirection.*;
import com.akexorcist.googledirection.model.*;
import com.akexorcist.googledirection.util.*;
import com.akexorcist.googledirection.constant.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {

    double originLat = 23.737259;
    double originLong = 90.382750;

    double destinationLat = 23.726760;
    double destinationLong = 90.421376;

    private GoogleMap googleMap;
    private static final String SERVER_KEY = "AIzaSyCtqKtRtaSzQJ8qYnRs9kBY4UiRqFNLsKI";
//    private LatLng camera = new LatLng(originLat, originLong);
//    private LatLng origin = new LatLng(originLat, originLong);
//    private LatLng destination = new LatLng(destinationLat, destinationLong);

    private LatLng camera = new LatLng(22.8991066, 89.5018581);
    private LatLng origin = new LatLng(22.8991066, 89.5018581);
    private LatLng destination = new LatLng(22.8983666, 89.5012359);

    private Button requestBtn;

    private final static String FORWARD = "forward";
    private final static String BACKWARD = "backward";
    private final static String LEFT = "left";
    private final static String RIGHT = "right";


    private final static String TAG = "MapsActivity";

    private ArrayList<String> directions;
    private ArrayList<LatLng> reversedLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestBtn = (Button) findViewById(R.id.requestButton);
        requestBtn.setOnClickListener(this);

        directions = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 14));

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if (id == R.id.requestButton){
            requestDirection();
        }
    }

    public void requestDirection(){
        Snackbar.make(requestBtn, "Direction Requesting....", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(SERVER_KEY).from(origin).to(destination).transitMode(TransportMode.WALKING).execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction){
        Snackbar.make(requestBtn, "Success with status: " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();

        if (direction.isOK()){



            ArrayList<LatLng> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getSectionPoint();

            reversedLatLng = new ArrayList<>(sectionPositionList);

            Collections.reverse(reversedLatLng);

            for (LatLng pos : sectionPositionList){
                googleMap.addMarker(new MarkerOptions().position(pos));

                Log.i(TAG, "Latitude: " + pos.latitude);
                Log.i(TAG, "Longitude: " + pos.longitude);
            }


            Log.i(TAG, "REVERSED: ");

            for (LatLng pos : reversedLatLng){
                Log.i(TAG, "Latitude: " + pos.latitude);
                Log.i(TAG, "Longitude: " + pos.longitude);
            }

            Log.i(TAG, "NORMAL ");


            for (int i = 0 ; i < reversedLatLng.size(); i++){

                Log.i(TAG, "Latitude: " + reversedLatLng.get(i).latitude);
            Log.i(TAG, "Longitude: " + reversedLatLng.get(i).longitude);
        }






            /*

            Location Direction Section

             */

            //Start

            for (int i = 1; i < reversedLatLng.size() - 1; i++){

                boolean largeLat = false;
                boolean largeLong = false;
                boolean firstLong = false;
                boolean firstLat = false;
                boolean secondLat = false;
                boolean secondLong = false;

                Log.i(TAG, "INSIDE REVERESED LATLNG");
                Log.i(TAG, "Latitude: " + reversedLatLng.get(i).latitude);
                Log.i(TAG, "Longitude: " + reversedLatLng.get(i).longitude);

                Log.i(TAG, "INSIDE REVERESED LATLNG 2");
                Log.i(TAG, "Latitude: " + reversedLatLng.get(i + 1).latitude);
                Log.i(TAG, "Longitude: " + reversedLatLng.get(i + 1).longitude);


                LatLng first = sectionPositionList.get(i);
                LatLng second = sectionPositionList.get(i + 1);

                double compareLongitude = second.longitude - first.longitude;
                double compareLatitude = second.latitude - first.latitude;


                if (compareLatitude > compareLongitude) largeLat = true;
                else largeLong = true;

                if (largeLat == true){
                    if (compareLatitude > 0) {
                        secondLat = true;
                        Log.i(TAG, "2nd Latitude > 1st");
                    }
                    else {
                        firstLat = true;
                        Log.i(TAG, "1st Latitude > 2nd");
                    }
                }

                if (largeLong == true){
                    if (compareLongitude > 0) {
                        secondLong = true;
                        Log.i(TAG, "2nd Longitude > 1st");
                    }
                    else {
                        firstLong = true;
                        Log.i(TAG, "1st Longitude > 2nd");
                    }
                }

                if (secondLat == true) directions.add(FORWARD);
                if (firstLat == true) directions.add(BACKWARD);
                if (secondLong == true) directions.add(RIGHT);
                if (firstLong == true) directions.add(LEFT);




                Log.i(TAG, "COMPARED LATITUDE: " + compareLatitude);
                Log.i(TAG, "COMPARED LONGITUDE: "  + compareLongitude);








//                if (compareLatitude > compareLongitude) { largeLat = true; Log.i(TAG, "LARGELAT " +
//                        "TRUE"); }
//                else { largeLong = true; Log.i(TAG, "LARGELONG" + "TRUE"); }
//
//                if (largeLat){
//                    if (first.latitude < second.latitude)  { secondLat = true; Log.i(TAG, "SECOND LAT" + "TRUE"); }
//                    else { firstLat = true; Log.i(TAG, "FIRSTLAT" + "TRUE"); }
//                }
//
//                if (largeLong){
//                    if (first.longitude < second.longitude) { secondLong = true; Log.i(TAG, "SECOND LONG" + "TRUE"); }
//                    else {firstLong = true; Log.i(TAG, "FIRST LONG" + "TRUE"); }
//                }

//
//                if (secondLat) directions.add(BACKWARD);
//                else if (firstLat) directions.add(FORWARD);
//                if (firstLong) directions.add(RIGHT);
//                else if (secondLong) directions.add(LEFT);




            }

            //End


            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();

            ArrayList <PolylineOptions> polyLineOptionsList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.BLUE, 3, Color.GREEN);

            for (PolylineOptions polylineOptions : polyLineOptionsList){
                googleMap.addPolyline(polylineOptions);
            }

            requestBtn.setVisibility(View.GONE);
        }


        for (String dir : directions){
            Log.i(TAG, dir);
        }



    }

    @Override
    public void onDirectionFailure(Throwable t){
        Snackbar.make(requestBtn, t.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
