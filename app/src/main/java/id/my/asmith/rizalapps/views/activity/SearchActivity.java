package id.my.asmith.rizalapps.views.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.PostProduct;
import id.my.asmith.rizalapps.model.Value;
import id.my.asmith.rizalapps.network.interfaces.ProductInterface;
import id.my.asmith.rizalapps.views.adapter.ListProductAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static id.my.asmith.rizalapps.network.config.Config.BASE_URL;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private List<PostProduct> results = new ArrayList<>();
    private ListProductAdapter viewAdapter;
    private ProgressDialog mProgressdlg;
    @BindView(R.id.recycler_search)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mProgressdlg = new ProgressDialog(this);
        ButterKnife.bind(this);
        viewAdapter = new ListProductAdapter(this, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari Produk");
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mProgressdlg.setMessage("Load data...");
        mProgressdlg.setCancelable(false);
        mProgressdlg.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductInterface api = retrofit.create(ProductInterface.class);
        Call<Value> call = api.search(newText);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                mProgressdlg.dismiss();

                if (value.equals("1")) {
                    results = response.body().getResult();
                    viewAdapter = new ListProductAdapter(SearchActivity.this, results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                mProgressdlg.dismiss();
                Log.e("TAG", "onFailure: GAGAL" );
            }
        });
        return true;
    }
}
