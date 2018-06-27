package id.my.asmith.rizalapps.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Locale;

import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.Users;
import id.my.asmith.rizalapps.utils.AppPrefs;
import id.my.asmith.rizalapps.utils.LoctListnImpl;

public class TokoActivity extends AppCompatActivity  {
    private static final int GALLERY_REQ = 1;
    private LocationManager mLocationManager;
    private ImageView mTokoPic, mMapsPic;
    private Button mBtnAuto, mBtnSeller;
    private EditText mEdName, mEdPhone, mEdLoc;
    private TextView mTxtLat, mTxtLon;
    private ProgressDialog mProgressdlg;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private Uri mImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressdlg = new ProgressDialog(this);

        initView();
        btnPressed();

    }

    private void initView() {

        mTokoPic = (ImageView) findViewById(R.id.userPic);
        mMapsPic = (ImageView) findViewById(R.id.map_toko);
        mBtnAuto = (Button) findViewById(R.id.btnAutoLoc);
        mBtnSeller = (Button) findViewById(R.id.btnSellersToko);
        mEdName = (EditText) findViewById(R.id.txtNameToko);
        mEdPhone = (EditText) findViewById(R.id.txtPhoneToko);
        mEdLoc = (EditText) findViewById(R.id.txtLocationToko);
        mTxtLat = (TextView) findViewById(R.id.txtLatToko);
        mTxtLon = (TextView) findViewById(R.id.txtLonToko);

    }

    private void btnPressed() {
        mTokoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQ);
            }
        });

        mBtnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationProvider(mLocationManager);
            }
        });

        mMapsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "setPosition";
                String latitude = "1.4554847";
                String longitude = "124.8250461";
                MapsActivity.start(TokoActivity.this, status, latitude, longitude);
            }
        });

        mBtnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userId = getUid();

                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                final Users users = dataSnapshot.getValue(Users.class);
                                // [START_EXCLUDE]
                                if (users == null) {
                                    // User is null, error out
                                    Log.e("Profile", "User " + userId + " is unexpectedly null");
                                    Toast.makeText(TokoActivity.this,
                                            "Error: could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if(!users.toko) {
                                        final String toko_nama = mEdName.getText().toString().trim();
                                        final String toko_phone = mEdPhone.getText().toString().trim();
                                        final String pemilik_nama = users.fullName;
                                        final String pemilik_email = users.email;
                                        final String lokasi_tokos = mEdLoc.getText().toString().trim();
                                        final String lat_toko = mTxtLat.getText().toString();
                                        final String lon_toko = mTxtLon.getText().toString();
                                        mProgressdlg.setMessage("Mengirim data . . .!");
                                        mProgressdlg.setCancelable(false);
                                        mProgressdlg.show();
                                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (!TextUtils.isEmpty(toko_nama)
                                                                && !TextUtils.isEmpty(lat_toko)
                                                                && !TextUtils.isEmpty(lon_toko)
                                                                && !TextUtils.isEmpty(toko_phone)
                                                                && mImageUri != null) {

                                                            StorageReference path = mStorage.child("market_pic").child(mImageUri.getLastPathSegment());
                                                            path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                                                        DatabaseReference system = mDatabase.child("tokos/"+userId);

                                                                        system.child("namaToko").setValue(toko_nama);
                                                                        system.child("nomorToko").setValue(toko_phone);
                                                                        system.child("namaPemilik").setValue(pemilik_nama);
                                                                        system.child("emailPemilik").setValue(pemilik_email);
                                                                        system.child("lokasiToko").setValue(lokasi_tokos);
                                                                        system.child("uid").setValue(userId);
                                                                        system.child("lat").setValue(lat_toko);
                                                                        system.child("lon").setValue(lon_toko);
                                                                        system.child("pic").setValue(downloadUrl.toString());

                                                                        mProgressdlg.dismiss();

                                                                        Toast.makeText(TokoActivity.this, "Data berhasil dikirim!",
                                                                                Toast.LENGTH_SHORT).show();


                                                                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                                                            new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    finish();
                                                                                    DatabaseReference system = mDatabase.child("users/"+userId);
                                                                                    system.child("toko").setValue(true);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                    Log.w("send", "loadPost:onCancelled", databaseError.toException());
                                                                                }
                                                                            });
                                                                    }
                                                            });



                                                        } else {
                                                            mProgressdlg.dismiss();
                                                            Toast.makeText(TokoActivity.this, "Tidak boleh ada yang kosong!",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Log.w("send", "loadPost:onCancelled", databaseError.toException());
                                                    }
                                                });
                                    }
                                }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("Profile", "getUser:onCancelled", databaseError.toException());
                        }
                });
            }
        });
    }

    private void getLocationProvider(LocationManager locationManager) {
        try {
            if (ActivityCompat.checkSelfPermission(TokoActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(TokoActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            Boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled || !isNetworkEnabled) {
                Toast.makeText(TokoActivity.this, "Network or GPS provider is disabled", Toast.LENGTH_LONG).show();
            } else {

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1800000, //30 min = 1.8 million ms
                            50F,
                            new LoctListnImpl());
                    Log.d("GPS", "GPS Provider enable");

                    getLocationData(locationManager);

                }

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1800000, //30 min = 1.8 million ms
                            50F,
                            new LoctListnImpl());
                    Log.d("Network", "Network Provider enable");

                    getLocationData(locationManager);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocationData(LocationManager locationManager) {
        try {
            if (ActivityCompat.checkSelfPermission(TokoActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(TokoActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            Geocoder geo = new Geocoder(TokoActivity.this, Locale.getDefault());
            List<Address> address = geo.getFromLocation(latitude, longitude, 1);
            String country = address.get(0).getCountryName();
            String state = address.get(0).getAdminArea();
            String city = address.get(0).getSubAdminArea();
            String districts = address.get(0).getLocality();
            String urbanVillage = address.get(0).getSubLocality();
            String addresses = address.get(0).getAddressLine(0);
            String postalCode = address.get(0).getPostalCode();
            String knownName = address.get(0).getFeatureName();

            String fullAddress =
                    country + ", "
                            + state + ", "
                            + city + ", "
                            + districts + ", "
                            + urbanVillage + ", "
                            + addresses + ", "
                            + postalCode + ", "
                            + knownName ;

            mTxtLat.setText(String.valueOf(latitude));
            mTxtLon.setText(String.valueOf(longitude));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppPrefs appPrefs = AppPrefs.getInstance(TokoActivity.this);
        String lat = appPrefs.getData("PREF_LAT");
        String lon = appPrefs.getData("PREF_LON");
        mTxtLat.setText(lat);
        mTxtLon.setText(lon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPrefs appPrefs = AppPrefs.getInstance(TokoActivity.this);
        appPrefs.clearPrefs();
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mTokoPic.setImageURI(mImageUri);

        }
    }
}
