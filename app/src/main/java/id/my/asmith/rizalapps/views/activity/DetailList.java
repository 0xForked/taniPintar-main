package id.my.asmith.rizalapps.views.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import id.my.asmith.rizalapps.R;

public class DetailList extends AppCompatActivity {
    @BindView(R.id.imageDetail) ImageView imgDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        //Intent intent = getIntent();
        //String nama = intent.getStringExtra("nama");
        //String harga = intent.getStringExtra("harga");
        //String keterangan = intent.getStringExtra("keterangan");
        //String kategori = intent.getStringExtra("kategori");
        //String produkid = intent.getStringExtra("produkid");
        //String userid = intent.getStringExtra("userid");
        //String pic = intent.getStringExtra("img");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
