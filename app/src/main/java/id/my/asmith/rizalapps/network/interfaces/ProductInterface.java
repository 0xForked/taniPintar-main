package id.my.asmith.rizalapps.network.interfaces;

import id.my.asmith.rizalapps.model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Asmith on 10/3/2017.
 */

public interface ProductInterface {

    @FormUrlEncoded
    @POST("/insert_product.php")
    Call<Value> product(
                        @Field("uid") String uid,
                        @Field("nama_barang") String nama_barang,
                        @Field("kategori") String kategori,
                        @Field("harga") String harga,
                        @Field("keterangan") String keterangan,
                        @Field("pic") String pic);

    @FormUrlEncoded
    @POST("/output_toko.php")
    Call<Value> tokos(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("/update_toko_product.php")
    Call<Value> update( @Field("produk_id") String produk_id,
                        @Field("nama_barang") String nama_barang,
                        @Field("kategori") String kategori,
                        @Field("harga") String harga,
                        @Field("keterangan") String keterangan,
                        @Field("pic") String pic);

    @FormUrlEncoded
    @POST("/delete_toko_product.php")
    Call<Value> delete(@Field("produk_id") String produk_id);

    @FormUrlEncoded
    @POST("/list_perkategori.php")
    Call<Value> category(@Field("kategori") String kategori);

    //OnProgress
    @FormUrlEncoded
    @POST("/search_product.php")
    Call<Value> search(@Field("search") String search);


}
