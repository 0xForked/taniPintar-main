package id.my.asmith.rizalapps.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.Tokos;

public class DetailList extends AppCompatActivity {
    @BindView(R.id.imageDetail) ImageView imgDetail;
    @BindView(R.id.textNamaDL) TextView textViewNama;
    @BindView(R.id.textKategoriDL) TextView textViewKategori;
    @BindView(R.id.textHargaDL) TextView textViewHarga;
    @BindView(R.id.textKeteranganDL) TextView textViewKeterangan;
    @BindView(R.id.textNamaTK) TextView textViewNamaToko;
    @BindView(R.id.textAlamatTK) TextView textViewLokasiToko;
    @BindView(R.id.textPonselTK) TextView textViewPonselToko;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String harga = intent.getStringExtra("harga");
        String keterangan = intent.getStringExtra("keterangan");
        String kategori = intent.getStringExtra("kategori");
        String produkid = intent.getStringExtra("produkid");
        String userid = intent.getStringExtra("userid");
        String pic = intent.getStringExtra("img");

        textViewNama.setText(nama);
        textViewKategori.setText(kategori);
        textViewHarga.setText(harga);
        textViewKeterangan.setText(keterangan);
        Picasso.with(DetailList.this)
                .load(pic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_broken_image)
                .into(imgDetail);


        mDatabase.child("tokos").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        final Tokos toko = dataSnapshot.getValue(Tokos.class);
                        textViewNamaToko.setText(toko.namaToko);
                        textViewLokasiToko.setText(toko.lokasiToko);
                        textViewPonselToko.setText(toko.nomorToko);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.w("Profile", "getUser:onCancelled", databaseError.toException());
                    }

                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                overridePendingTransition( android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition( android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }
}
