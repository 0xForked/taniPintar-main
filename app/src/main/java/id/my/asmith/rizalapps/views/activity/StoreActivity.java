package id.my.asmith.rizalapps.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.PostProduct;
import id.my.asmith.rizalapps.model.Tokos;
import id.my.asmith.rizalapps.model.Value;
import id.my.asmith.rizalapps.network.interfaces.ProductInterface;
import id.my.asmith.rizalapps.views.adapter.RecylcerviewProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static id.my.asmith.rizalapps.network.config.Config.BASE_URL;

public class StoreActivity extends AppCompatActivity {

    private TextView namaToko, phoneToko, namaPemilik, emailPemilik;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private RelativeLayout inputProduct, listProduct;
    private Button btnCreate, btnCancel;
    private EditText edName, edPrice, edAbout;
    private ImageView imgProduct;
    private Spinner spCategory;
    private String[] SPINNERVALUES = {"Beras", "Bawang Merah", "Bawang Putih", "Cabai", "Tomat",
            "Rempah", "Lain-Lain"};
    private static final String REQUIRED = "Required";
    private ProgressDialog mProgressdlg;
    private static final int GALLERY_REQ = 1;
    private Uri mImageUri = null;

    private List<PostProduct> results = new ArrayList<>();
    private RecylcerviewProductAdapter viewAdapter;

    @BindView(R.id.recycler_tokos)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        getSupportActionBar().setTitle("My Store");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        namaToko = (TextView) findViewById(R.id.nama_toko);
        namaPemilik = (TextView) findViewById(R.id.nama_pemilik);
        phoneToko = (TextView) findViewById(R.id.phone_toko);
        emailPemilik = (TextView) findViewById(R.id.email_pemili);
        inputProduct = (RelativeLayout) findViewById(R.id.produkInput);
        mProgressdlg = new ProgressDialog(this);
        imgProduct = (ImageView) findViewById(R.id.picProduct);
        edName = (EditText) findViewById(R.id.nameProduct);
        edPrice = (EditText) findViewById(R.id.priceProduct);
        edAbout = (EditText) findViewById(R.id.aboutProduct);
        btnCreate = (Button) findViewById(R.id.createProduct);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInput();
            }
        });
        listProduct = (RelativeLayout) findViewById(R.id.produkList) ;
        btnCancel =(Button) findViewById(R.id.cancels);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputProduct.setVisibility(View.GONE);
                listProduct.setVisibility(View.VISIBLE);
                Intent refresh = new Intent(StoreActivity.this, StoreActivity.class);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.

            }
        });
        ButterKnife.bind(this);

        viewAdapter = new RecylcerviewProductAdapter(this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);
        loadData();

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQ);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputProduct.setVisibility(View.VISIBLE);
                listProduct.setVisibility(View.GONE);
            }
        });

        //Spinner
        spCategory =(Spinner)findViewById(R.id.categoryProduct);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StoreActivity.this,
                android.R.layout.simple_list_item_1, SPINNERVALUES);
        spCategory.setAdapter(adapter);
        //Adding setOnItemSelectedListener method on spinner.
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(StoreActivity.this, spCategory.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        //Task
        mProgressdlg.setMessage("Load data...");
        mProgressdlg.setCancelable(false);
        mProgressdlg.show();
        final String Uid = getUid();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductInterface api = retrofit.create(ProductInterface.class);
        Call<Value> call = api.tokos(Uid);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                mProgressdlg.dismiss();
                if (value.equals("1")) {
                    results = response.body().getResult();
                    viewAdapter = new RecylcerviewProductAdapter(StoreActivity.this, results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                mProgressdlg.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){
            mImageUri = data.getData();
            imgProduct.setImageURI(mImageUri);

        }
    }


    private void startInput() {

        final String nameProduct = edName.getText().toString().trim();
        final String priceProduct = edPrice.getText().toString().trim();
        final String aboutProduct = edAbout.getText().toString().trim();
        final String categoryProduct = spCategory.getSelectedItem().toString();
        final String userId = getUid();
        // All is required
        if (TextUtils.isEmpty(nameProduct)) {
            edName.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(priceProduct)) {
            edPrice.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(aboutProduct)) {
            edAbout.setError(REQUIRED);
            return;
        }

        //Task
        mProgressdlg.setMessage("Sending data...");
        mProgressdlg.setCancelable(false);
        mProgressdlg.show();
        if(!TextUtils.isEmpty(nameProduct) && !TextUtils.isEmpty(priceProduct)&&
                !TextUtils.isEmpty(aboutProduct) && mImageUri != null){
            StorageReference path = mStorage.child("product_pic").child(mImageUri.getLastPathSegment());
            path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    //Retrofit
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ProductInterface api = retrofit.create(ProductInterface.class);
                    Call<Value> call = api.product(userId, nameProduct, categoryProduct,
                            priceProduct, aboutProduct, downloadUrl.toString() );
                    call.enqueue(new Callback<Value>() {
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {
                           // String value = response.body().getValue();
                           // String message = response.body().getMessage();
                            mProgressdlg.dismiss();
                            inputProduct.setVisibility(View.GONE);
                            listProduct.setVisibility(View.VISIBLE);

                            Intent refresh = new Intent(StoreActivity.this, StoreActivity.class);
                            startActivity(refresh);//Start the same Activity
                            finish(); //finish Activity.

                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            if (value.equals("1")) {
                                Toast.makeText(StoreActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StoreActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            mProgressdlg.dismiss();
                            Toast.makeText(StoreActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //End Retrofit

                }
            });
        }else {
            mProgressdlg.dismiss();
            Toast.makeText(StoreActivity.this,
                    "Please put all item!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userId = getUid();
        mDatabase.child("tokos").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        final Tokos toko = dataSnapshot.getValue(Tokos.class);
                        // [START_EXCLUDE]
                        namaToko.setText(toko.namaToko);
                        phoneToko.setText(toko.nomorToko);
                        namaPemilik.setText(toko.namaPemilik);
                        emailPemilik.setText(toko.emailPemilik);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Profile", "getUser:onCancelled", databaseError.toException());
                    }

                });

    }
}
