package id.my.asmith.rizalapps.views.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.Users;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ImageView profilePic, picOut;
    private EditText mEmail, mName, mBirth, mNumber;
    private Spinner mGender;
    private String[] SPINNERVALUES = {"Pria", "Wanita"};
    private Button mBtnBirth, mBtnDone, mBtnCancel, mBtnToko, mBtnEditProfile;
    private TextView emailOut, nameOut, birthOut, numberOut, genderOut;
    private Uri mImageUri = null;
    private static final int DLG_ID = 0;
    private static final int GALLERY_REQ = 1;
    private static final String REQUIRED = "Required";
    private int year, month, day;
    private ProgressDialog mProgressdlg;
    RelativeLayout mOutput, mInput;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        showDatePicter();
        //Calender
        final java.util.Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressdlg = new ProgressDialog(this);

        profilePic = (ImageView) findViewById(R.id.userPic);
        mName = (EditText) findViewById(R.id.editFname);
        mBirth = (EditText) findViewById(R.id.editTempat);
        mNumber = (EditText) findViewById(R.id.editPN);
        mEmail = (EditText) findViewById(R.id.editEmail);
        mBtnBirth = (Button)findViewById(R.id.btnBirth);
        mBtnDone = (Button) findViewById( R.id.submit_ud);
        mBtnToko = (Button) findViewById(R.id.beaSelller);
        picOut = (ImageView) findViewById(R.id.userPicOutput);
        nameOut = (TextView) findViewById(R.id.fullnameOutput);
        emailOut = (TextView) findViewById(R.id.emailOutput);
        birthOut = (TextView) findViewById(R.id.birthOutput);
        numberOut = (TextView) findViewById(R.id.phoneOutput);
        genderOut =(TextView) findViewById(R.id.genderOutput);
        mBtnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        mOutput = (RelativeLayout) findViewById(R.id.layOutput);
        mInput = (RelativeLayout) findViewById(R.id.layInput);
        mBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOutput.setVisibility(View.GONE);
                mInput.setVisibility(View.VISIBLE);
            }
        });

        mBtnCancel = (Button) findViewById(R.id.cancel_ud);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOutput.setVisibility(View.VISIBLE);
                mInput.setVisibility(View.GONE);
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQ);
            }
        });

        //Spinner
        mGender =(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUES);
        mGender.setAdapter(adapter);
        //Adding setOnItemSelectedListener method on spinner.
        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(ProfileActivity.this, mGender.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            mImageUri = data.getData();
            profilePic.setImageURI(mImageUri);

        }
    }

    public void showDatePicter (){
        mBtnBirth = (Button)findViewById(R.id.btnBirth);
        mBtnBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DLG_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        if (id == DLG_ID )

            return new DatePickerDialog(this, dpListener, year, month ,day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                    year = Year;
                    month = Month;
                    day = Day ;

                    mBtnBirth.setText(day + "/" + month + "/" + year);
                }
            };


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    private void startUpdate() {
        //Get data string from EditText

        final String fullname = mName.getText().toString().trim();
        final String tempat = mBirth.getText().toString().trim();
        final String numb = mNumber.getText().toString().trim();
        final String userId = getUid();


        // All is required
        if (TextUtils.isEmpty(fullname)) {
            mName.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(tempat)) {
            mBirth.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(numb)) {
            mNumber.setError(REQUIRED);
            return;
        }
        if ( mImageUri == null ) {
            Toast.makeText(ProfileActivity.this,
                    "please select an images",
                    Toast.LENGTH_SHORT).show();
        }
        //Task
        mProgressdlg.setMessage("Sending data...");
        mProgressdlg.setCancelable(false);
        mProgressdlg.show();
        if(!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(tempat)&&
                !TextUtils.isEmpty(numb) && mImageUri != null){
            StorageReference path = mStorage.child("profile_pic").child(mImageUri.getLastPathSegment());
            path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference userDetail = mDatabase.child("users/"+userId);
                    userDetail.child("fullName").setValue(fullname);
                    userDetail.child("tempat").setValue(tempat);
                    userDetail.child("phone").setValue(numb);
                    userDetail.child("tanggal").setValue(mBtnBirth.getText().toString());
                    userDetail.child("pic").setValue(downloadUrl.toString());
                    userDetail.child("gender").setValue(mGender.getSelectedItem().toString());

                    mProgressdlg.dismiss();
                    Intent refresh = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(refresh);//Start the same Activity
                    finish(); //finish Activity.
                    Toast.makeText(ProfileActivity.this,
                            "well done vroo! your profile was updated!",
                            Toast.LENGTH_SHORT).show();
                    mOutput.setVisibility(View.VISIBLE);
                    mInput.setVisibility(View.GONE);
                }
            });
        }else {
            mProgressdlg.dismiss();
            Toast.makeText(ProfileActivity.this,
                    "Please put all item!",
                    Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    protected void onStart() {
        super.onStart();

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
                            Toast.makeText(ProfileActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            nameOut.setText(users.fullName);
                            emailOut.setText(users.email);
                            numberOut.setText(users.phone);
                            if(users.pic != null || users.tanggal != null || users.tempat != null ||
                                    users.gender != null) {
                                Picasso.with(ProfileActivity.this)
                                      .load(users.pic)
                                      .placeholder(R.mipmap.ic_launcher)
                                      .error(R.drawable.ic_broken_image)
                                      .into(picOut);

                                birthOut.setText( users.tempat + " " +users.tanggal);
                                genderOut.setText(users.gender);
                            }else{
                                Toast.makeText(ProfileActivity.this, "Kami tak dapat mengambil " +
                                        "sebagian data anda", Toast.LENGTH_SHORT).show();
                            }

                            if(!users.toko) {
                                mBtnToko.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("RestrictedApi")
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(ProfileActivity.this, TokoActivity.class));
//                                        final AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
//                                                .setCancelable(false)
//                                                .setTitle("Become a seller!")
//                                                .setPositiveButton("Start Selling", null)
//                                                .setNegativeButton("Set Location", null)
//                                                .setNeutralButton("Cancel", null)
//                                                .create();
//
//                                        LinearLayout layout = new LinearLayout(ProfileActivity.this);
//                                        layout.setOrientation(LinearLayout.VERTICAL);
//
//                                        EditText nama_toko = new EditText(ProfileActivity.this);
//                                        EditText phone_toko = new EditText(ProfileActivity.this);
//                                        EditText lokasi_toko = new EditText(ProfileActivity.this);
//                                        TextView txt_lat = new TextView(ProfileActivity.this);
//                                        TextView txt_lon = new TextView(ProfileActivity.this);
//                                        TextView txt_maps = new TextView(ProfileActivity.this);
//
//                                        nama_toko.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//                                        nama_toko.setHint("Market Name");
//                                        nama_toko.setSingleLine(true);
//
//                                        phone_toko.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//                                        phone_toko.setHint("Phone Number");
//                                        phone_toko.setSingleLine(true);
//
//                                        lokasi_toko.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                                        lokasi_toko.setHint("City/Village");
//                                        lokasi_toko.setSingleLine(true);
//
//                                        txt_lat.setHint("Latitude");
//                                        txt_lon.setHint("Longitude");
//                                        txt_maps.setHint("Tap 'set location' to get lat and lon via GMaps");
//                                        txt_maps.setTextColor(getResources().getColor(R.color.colorAccent));
//
//                                        txt_lat.setVisibility(View.GONE);
//                                        txt_lon.setVisibility(View.GONE);
//
//                                        layout.addView(nama_toko);
//                                        layout.addView(phone_toko);
//                                        layout.addView(lokasi_toko);
//                                        layout.addView(txt_lat);
//                                        layout.addView(txt_lon);
//                                        layout.addView(txt_maps);
//
//                                        dialog.setView(layout, 30, 0, 30, 0);
//
//                                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                                            @Override
//                                            public void onShow(final DialogInterface dialog) {
//
//
//                                                Button btnSeller = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
//                                                btnSeller.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                    }
//                                                });
//                                                Button btnLocation = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
//                                                btnLocation.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                    }
//                                                });
//                                                Button btnCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
//                                                btnCancel.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        dialog.dismiss();
//                                                    }
//                                                });
//                                            }
//                                        });
//
//                                        dialog.show();
//
                                        //Showing AlertDialog after clicking Button
//                                        final AlertDialog.Builder alertDialogBuilder =
//                                                new AlertDialog.Builder(ProfileActivity.this);
//                                        alertDialogBuilder.setTitle("Become a seller!");
//                                        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
//                                        alertDialogBuilder.setCancelable(false);
//                                        LinearLayout layout = new LinearLayout(ProfileActivity.this);
//                                        layout.setOrientation(LinearLayout.VERTICAL);
//
//                                        final EditText nama_toko = new EditText(ProfileActivity.this);
//                                        final EditText phone_toko = new EditText(ProfileActivity.this);
//                                        final EditText lokasi_toko = new EditText(ProfileActivity.this);
//                                        final EditText lat_toko = new EditText(ProfileActivity.this);
//                                        final EditText lon_toko = new EditText(ProfileActivity.this);
//
//                                        nama_toko.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//                                        nama_toko.setHint("Market Name");
//                                        nama_toko.setSingleLine(true);
//
//                                        phone_toko.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//                                        phone_toko.setHint("Phone Number");
//                                        phone_toko.setSingleLine(true);
//
//                                        lokasi_toko.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                                        lokasi_toko.setHint("Kota/Desa Lokasi Toko");
//                                        lokasi_toko.setSingleLine(true);
//
//                                        lat_toko.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                                        lat_toko.setHint("Latitude");
//                                        lat_toko.setSingleLine(true);
//
//                                        lon_toko.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                                        lon_toko.setHint("Longitude");
//                                        lon_toko.setSingleLine(true);
//
//                                        layout.addView(nama_toko);
//                                        layout.addView(phone_toko);
//                                        layout.addView(lokasi_toko);
//                                        layout.addView(lat_toko);
//                                        layout.addView(lon_toko);
//
//                                        alertDialogBuilder.setView(layout, 30, 0, 30, 0);
//                                        // set positive button: Sing out
//                                        alertDialogBuilder.setPositiveButton("Start Selling",new DialogInterface.OnClickListener() {
//                                            public void onClick(final DialogInterface dialog, int id) {
//                                                // go to a new activity of the app
//                                                // onClickEvent to sing out
//                                                final String userId = getUid();
//                                                final String toko_nama = nama_toko.getText().toString().trim();
//                                                final String toko_phone = phone_toko.getText().toString().trim();
//                                                final String pemilik_nama = users.fullName;
//                                                final String pemilik_email = users.email;
//                                                final String lokasi_tokos = lokasi_toko.getText().toString().trim();
//
//                                                mProgressdlg.setMessage("Mengirim data . . .!");
//                                                mProgressdlg.setCancelable(false);
//                                                mProgressdlg.show();
//                                                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
//                                                        new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                if (!TextUtils.isEmpty(toko_nama) &&
//                                                                        !TextUtils.isEmpty(toko_phone)) {
//
//                                                                    DatabaseReference system = mDatabase.child("tokos/"+userId);
//
//                                                                    system.child("namaToko").setValue(toko_nama);
//                                                                    system.child("nomorToko").setValue(toko_phone);
//                                                                    system.child("namaPemilik").setValue(pemilik_nama);
//                                                                    system.child("emailPemilik").setValue(pemilik_email);
//                                                                    system.child("lokasiToko").setValue(lokasi_tokos);
//                                                                    system.child("uid").setValue(userId);
//
//                                                                    mProgressdlg.dismiss();
//
//                                                                    Toast.makeText(ProfileActivity.this, "Data berhasil dikirim!",
//                                                                            Toast.LENGTH_SHORT).show();
//                                                                    dialog.cancel();
//
//                                                                    Intent refresh = new Intent(ProfileActivity.this, ProfileActivity.class);
//                                                                    startActivity(refresh);//Start the same Activity
//                                                                    finish(); //finish Activity.
//
//                                                                    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
//                                                                            new ValueEventListener() {
//                                                                                @Override
//                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                                                DatabaseReference system = mDatabase.child("users/"+userId);
//                                                                                system.child("toko").setValue(true);
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(DatabaseError databaseError) {
//                                                                                Log.w("send", "loadPost:onCancelled", databaseError.toException());
//                                                                            }
//                                                                    });
//
//                                                                } else {
//                                                                    mProgressdlg.dismiss();
//                                                                    Toast.makeText(ProfileActivity.this, "Tidak boleh ada yang kosong!",
//                                                                            Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError databaseError) {
//                                                                Log.w("send", "loadPost:onCancelled", databaseError.toException());
//                                                            }
//                                                });
//
//                                            }
//                                        });
//                                        // set negative button: Cancel
//                                        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                // cancel the alert box and put a Toast to the user
//                                                dialog.cancel();
//                                                // notify to user hes press the cancel button
//                                                Toast.makeText(ProfileActivity.this, "Canceled",
//                                                        Toast.LENGTH_LONG).show();
//                                            }
//                                        });
//
//                                        alertDialogBuilder.setNeutralButton("Get Place", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                mProgressdlg.setMessage("Sending data...");
//                                                mProgressdlg.setCancelable(false);
//                                                mProgressdlg.show();
//                                                int data = 1;
//                                                if (data == 0) {
//                                                    mProgressdlg.dismiss();
//                                                }
//                                            }
//                                        });
//                                        AlertDialog alertDialog = alertDialogBuilder.create();
//                                        // show alert
//                                        alertDialog.show();
                                    }
                                });

                            }else{
                                mBtnToko.setEnabled(false);
                                mBtnToko.setText("Anda Sudah Menjadi Penjual");
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Profile", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
