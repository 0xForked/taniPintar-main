package id.my.asmith.rizalapps.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.PostProduct;
import id.my.asmith.rizalapps.views.activity.DetailList;

/**
 * Created by Asmith on 10/6/2017.
 */

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {

    private Context context;
    private List<PostProduct> results;
    public ListProductAdapter(Context context, List<PostProduct> results) {
        this.context = context;
        this.results = results;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product, parent, false);
        ListProductAdapter.ViewHolder holder = new ViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(ListProductAdapter.ViewHolder holder, int position) {
        PostProduct result = results.get(position);
        holder.textViewNama.setText(result.getNamaBarang());
        holder.textViewKategori.setText(result.getKategori());
        holder.textViewHarga.setText(result.getHarga());
        holder.textViewKeterangan.setText(result.getKeterangan());
        holder.produkId.setText(String.valueOf(result.getProdukId()));
        holder.userID.setText(result.getUid());
        holder.imgurl.setText(result.getPic());
        Picasso.with(context)
                .load(result.getPic())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_broken_image)
                .into(holder.imgProducts);
        Log.d("asd", result.getPic());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textNama) TextView textViewNama;
        @BindView(R.id.textKategori) TextView textViewKategori;
        @BindView(R.id.textHarga) TextView textViewHarga;
        @BindView(R.id.textKeterangan) TextView textViewKeterangan;
        @BindView(R.id.produk_id) TextView produkId;
        @BindView(R.id.imgProduk) ImageView imgProducts;
        @BindView(R.id.uid) TextView userID;
        @BindView(R.id.imgrul) TextView imgurl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String nama = textViewNama.getText().toString();
            String kategori = textViewKategori.getText().toString();
            String harga = textViewHarga.getText().toString();
            String keterangan = textViewKeterangan.getText().toString();
            String produkid = String.valueOf(produkId.getText());
            String userid = userID.getText().toString();
            String imguri = imgurl.getText().toString();

            Intent i = new Intent(context, DetailList.class);
            i.putExtra("produkid", produkid);
            i.putExtra("nama", nama);
            i.putExtra("kategori", kategori);
            i.putExtra("harga", harga);
            i.putExtra("keterangan", keterangan);
            i.putExtra("userid", userid);
            i.putExtra("img", imguri);
            context.startActivity(i);
        }
    }
}
