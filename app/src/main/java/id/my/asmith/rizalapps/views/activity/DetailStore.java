package id.my.asmith.rizalapps.views.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.Value;
import id.my.asmith.rizalapps.network.ApiClient;
import id.my.asmith.rizalapps.network.ApiService;
import id.my.asmith.rizalapps.views.adapter.ListProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static id.my.asmith.rizalapps.network.Const.BASE_URL;

public class DetailStore extends AppCompatActivity {
    ApiClient mApiClient = new ApiClient();
    @BindView(R.id.picProductUpdate)ImageView picUpdate;
    @BindView(R.id.nameProductUpdate) EditText nameUpdate;
    @BindView(R.id.categoryProductUpdate) Spinner kategoriUpdate;
    @BindView(R.id.priceProductUpdate) EditText priceUpdate;
    @BindView(R.id.aboutProductUpdate) EditText aboutUpdate;
    @BindView(R.id.updateProduct) Button update;
    @BindView(R.id.deleteProduct) Button delete;
    private String[] SPINNERVALUES = {"Beras", "Bawang Merah", "Bawang Putih", "Cabai", "Tomat",
            "Rempah","cingkeh", "Lain-Lain"};
    private static final int GALLERY_REQ = 1;
    private Uri mImageUri = null;
    private ProgressDialog mProgressdlg;
    private StorageReference mStorage;

    @OnClick(R.id.picProductUpdate) void pic(){
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_REQ);
    }

    @OnClick (R.id.deleteProduct) void  delete(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        Intent intent = getIntent();
                        String produkid = intent.getStringExtra("produkid");

                        mApiClient
                                .ApiServices()
                                .productDelete(Integer.parseInt(produkid))
                                .enqueue(new Callback<Value>() {
                                    @Override
                                    public void onResponse(Call<Value> call, Response<Value> response) {
                                        String value = response.body().getValue();
                                        String message = response.body().getMessage();
                                        if (value.equals("1")) {
                                            Toast.makeText(DetailStore.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(DetailStore.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Value> call, Throwable t) {
                                        t.printStackTrace();
                                        Toast.makeText(DetailStore.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @OnClick(R.id.updateProduct) void update(){

        //membuat progress dialog
        mProgressdlg.setCancelable(false);
        mProgressdlg.setMessage("Updating ...");
        mProgressdlg.show();

        if( mImageUri != null){
            StorageReference path = mStorage.child("product_pic").child(mImageUri.getLastPathSegment());
            path.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                //mengambil data dari edittext
                String nama = nameUpdate.getText().toString();
                String kategori = kategoriUpdate.getSelectedItem().toString();
                String haraga = priceUpdate.getText().toString();
                String keterangan = aboutUpdate.getText().toString();
                Intent intent = getIntent();
                String produkid = intent.getStringExtra("produkid");
                Double lat = 1.222;
                Double lon = 124.123;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService api = retrofit.create(ApiService.class);
                Call<Value> call = api.productUpdate(
                        Integer.parseInt(produkid), nama,
                        kategori, haraga, keterangan,
                        downloadUrl.toString(), lat, lon);
                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        mProgressdlg.dismiss();
                        if (value.equals("1")) {
                            Toast.makeText(DetailStore.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailStore.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        mProgressdlg.dismiss();
                        Toast.makeText(DetailStore.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                    }
                });

                    }
                });
        } else {
            Toast.makeText(DetailStore.this, "Mohon foto di pilih", Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store);
        getSupportActionBar().setTitle("Update Data");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        mProgressdlg = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String harga = intent.getStringExtra("harga");
        String keterangan = intent.getStringExtra("keterangan");

        nameUpdate.setText(nama);
        priceUpdate.setText(harga);
        aboutUpdate.setText(keterangan);

        //Spinner
        //kategoriUpdate =(Spinner)findViewById(R.id.categoryProduct);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailStore.this,
                android.R.layout.simple_list_item_1, SPINNERVALUES);
        kategoriUpdate.setAdapter(adapter);
        //Adding setOnItemSelectedListener method on spinner.
        kategoriUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(DetailStore.this, kategoriUpdate.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
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
            picUpdate.setImageURI(mImageUri);

        }
    }

}
