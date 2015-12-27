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

    double originLat = 23.725544;
    double originLong = 90.3937393;

    double destinationLat = 23.725937;
    double destinationLong =  90.390123;

    private GoogleMap googleMap;
    private static final String SERVER_KEY = "AIzaSyCtqKtRtaSzQJ8qYnRs9kBY4UiRqFNLsKI";

    private LatLng camera = new LatLng(originLat, originLong);
    private LatLng origin = new LatLng(originLat, originLong);
    private LatLng destination = new LatLng(destinationLat, destinationLong);

    private ArrayList<Double> bearingList;



    //BUET
    //    private LatLng camera = new LatLng(23.725544, 90.3937393);
//    private LatLng origin = new LatLng(23.725544, 90.3937393);
//    private LatLng destination = new LatLng(23.725937, 90.390123);



    //KUET
//    private LatLng camera = new LatLng(22.8991066, 89.5018581);
//    private LatLng origin = new LatLng(22.8991066, 89.5018581);
//    private LatLng destination = new LatLng(22.8983666, 89.5012359);

    private Button requestBtn;

    private final static String FORWARD = "forward";
    private final static String BACKWARD = "backward";
    private final static String LEFT = "left";
    private final static String RIGHT = "right";


    private final static String TAG = "MapsActivity";

    private ArrayList<String> directions;
    private ArrayList<LatLng> reversedLatLng;
    private ArrayList<String> caughtDirections;
    private ArrayList<String> finalDirections;

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

    public String getDirection(double first, double second){
        if ((first > 0 && first < 180) && (second > 0 && second < 180)){
            if (first > second) return LEFT;
            else return RIGHT;
        }

        else if ((first > 180 && first < 360) && (second > 180 && second < 360)){
            if (first > second) return LEFT;
            else return RIGHT;
        }

        else if ((first > 0 && first < 90) && (second > 180 && second < 360)){
            return LEFT;
        }

        else if ((first > 90 && first < 180) && (second > 180 && second < 360)){
            return RIGHT;
        }

        else if ((first > 180 && first < 270) && (second > 0 && second < 180)){
            return LEFT;
        }

        else if ((first > 270 && first < 360) && (second > 0 && second < 180)){
            return RIGHT;
        }


        return "";
    }


    public double getAngle(LatLng first, LatLng second){
        double lat1 = first.latitude;
        double lat2 = second.latitude;
        double lon1 = first.longitude;
        double lon2 = second.longitude;

        double dlon = lon2 - lon1;
        double y = Math.sin(Math.toRadians(dlon)) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) - Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(dlon));

        double bearing = Math.toDegrees(Math.atan2(y, x));

        bearing =  (bearing + 360) % 360;
        return  bearing;

    }


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

            caughtDirections = new ArrayList<>();
            finalDirections = new ArrayList<>();
            bearingList = new ArrayList<>();

            Collections.reverse(reversedLatLng);

            for (LatLng pos : sectionPositionList){
                googleMap.addMarker(new MarkerOptions().position(pos));

                Log.i(TAG, "Latitude: " + pos.latitude);
                Log.i(TAG, "Longitude: " + pos.longitude);
            }


            for (int i = 1; i < sectionPositionList.size(); i++){
                bearingList.add(getAngle(sectionPositionList.get(0), sectionPositionList.get(i)));
            }


            for (int i = 0; i < sectionPositionList.size() - 2; i++){
                directions.add(getDirection(bearingList.get(i), bearingList.get(i+1)));
            }






            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();

            ArrayList <PolylineOptions> polyLineOptionsList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.BLUE, 3, Color.GREEN);

            for (PolylineOptions polylineOptions : polyLineOptionsList){
                googleMap.addPolyline(polylineOptions);
            }

            requestBtn.setVisibility(View.GONE);
        }

        Log.i(TAG, "PRINTING DIRECTIONS");
        for (String dir : directions){
            Log.i(TAG, dir);
        }
        Log.i(TAG, "PRINTING DIRECTIONS DONE");

        Log.i(TAG, "\n\n\n\n\n");

        Log.i(TAG, "PRINTING ANGLES");
        for (double angle: bearingList){
            Log.i(TAG, String.valueOf(angle));
        }
        Log.i(TAG, "PRINTING ANGLES DONE");

    }


    @Override
    public void onDirectionFailure(Throwable t){
        Snackbar.make(requestBtn, t.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
