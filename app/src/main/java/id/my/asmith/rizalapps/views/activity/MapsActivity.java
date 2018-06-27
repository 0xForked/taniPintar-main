package id.my.asmith.rizalapps.views.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.utils.AppPrefs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String MAP_STATUS = "id.aasumitro.maps.status";
    private static final String MAP_LAT = "id.aasumitro.maps.lat";
    private static final String MAP_LON = "id.aasumitro.maps.lon";

    private GoogleMap mMap;

    private RelativeLayout mLaySet;
    private Button mSet;
    private TextView mLat, mLon, mAddress;
    private LatLng marketLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initView();
        btnOnPressed();
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
        mMap = googleMap;

        // Get intent data
        Intent intent = this.getIntent();
        String map_status = intent.getStringExtra(MAP_STATUS);
        String map_lat = intent.getStringExtra(MAP_LAT);
        String map_lon = intent.getStringExtra(MAP_LON);

        switch (map_status) {
            case "getPosition":
                // Add a marker in market and move the camera
                LatLng market = new LatLng(Double.valueOf(map_lat), Double.valueOf(map_lon));
                mMap.addMarker(new MarkerOptions().position(market).title("Marker in market place!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(market));
                mMap.setMinZoomPreference(15F);
                break;
            case "setPosition":
                mLaySet.setVisibility(View.VISIBLE);
                LatLng sulut = new LatLng(Double.valueOf(map_lat), Double.valueOf(map_lon));
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(sulut)
                        .zoom(10)
                        .bearing(0)
                        .tilt(70)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                googleMap.animateCamera(camUpd3);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Custom location")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        marketLocation = latLng;
                        Double selectedLat = marketLocation.latitude;
                        Double selectedLon =marketLocation.longitude;

                        AppPrefs appPrefs = AppPrefs.getInstance(MapsActivity.this);

                        mLat.setText(String.valueOf(selectedLat));
                        mLon.setText(String.valueOf(selectedLon));

                        try {
                            Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                            List<Address> address = geo.getFromLocation(selectedLat, selectedLon, 1);
                            String addresses = address.get(0).getAddressLine(0);
                            mAddress.setText(addresses);

                            appPrefs.saveData("PREF_LAT",String.valueOf(selectedLat));
                            appPrefs.saveData("PREF_LON",String.valueOf(selectedLon));
                            appPrefs.saveData("PREF_ADDRESS",String.valueOf(addresses));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                break;
            default:
                Toast.makeText(MapsActivity.this,
                        "No data available",
                        Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    public static void start(Context context, String status, String lat, String lon) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(MAP_STATUS, status);
        intent.putExtra(MAP_LAT, lat);
        intent.putExtra(MAP_LON, lon);
        context.startActivity(intent);
    }

    private void initView() {
        mLaySet = (RelativeLayout) findViewById(R.id.laySetManual);
        mLat = (TextView) findViewById(R.id.lat);
        mLon = (TextView) findViewById(R.id.lon);
        mAddress= (TextView) findViewById(R.id.full_address);
        mSet = (Button) findViewById(R.id.btnSet);
    }

    private void btnOnPressed() {
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
