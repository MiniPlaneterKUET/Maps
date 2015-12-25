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

import java.util.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {

    private GoogleMap googleMap;
    private static final String SERVER_KEY = "AIzaSyCtqKtRtaSzQJ8qYnRs9kBY4UiRqFNLsKI";
    private LatLng camera = new LatLng(22.8991066, 89.5018581);
    private LatLng origin = new LatLng(22.8991066, 89.5018581);
    private LatLng destination = new LatLng(22.8983666, 89.5012359);
    private Button requestBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestBtn = (Button) findViewById(R.id.requestButton);
        requestBtn.setOnClickListener(this);

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
            for (LatLng pos : sectionPositionList){
                googleMap.addMarker(new MarkerOptions().position(pos));
            }

            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
            ArrayList <PolylineOptions> polyLineOptionsList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.LTGRAY, 3, Color.BLACK);

            for (PolylineOptions polylineOptions : polyLineOptionsList){
                googleMap.addPolyline(polylineOptions);
            }

            requestBtn.setVisibility(View.GONE);
        }


    }

    @Override
    public void onDirectionFailure(Throwable t){
        Snackbar.make(requestBtn, t.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}