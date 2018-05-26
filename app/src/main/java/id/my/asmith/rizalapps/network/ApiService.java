package id.my.asmith.rizalapps.network;

import com.google.gson.JsonObject;

import id.my.asmith.rizalapps.model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Asmith on 10/3/2017.
 */

public interface ApiService {

    @GET("product/list")
    Call<Value> getData(@Query("category") String category,
                        @Query("search") String search,
                        @Query("toko") String mart);

    @GET("product/detail/{id}")
    Call<Value> getDataDetail(@Path("id") int id);

    @FormUrlEncoded
    @POST("product/store")
    Call<Value> productSave(
                        @Field("uid") String uid,
                        @Field("nama_barang") String nama_barang,
                        @Field("kategori") String kategori,
                        @Field("harga") String harga,
                        @Field("keterangan") String keterangan,
                        @Field("pic") String pic,
                        @Field("lat") Double lat,
                        @Field("lon") Double lon);

    @FormUrlEncoded
    @POST("product/update/{id}")
    Call<Value> productUpdate(
                        @Path("id") int id,
                        @Field("nama_barang") String nama_barang,
                        @Field("kategori") String kategori,
                        @Field("harga") String harga,
                        @Field("keterangan") String keterangan,
                        @Field("pic") String pic,
                        @Field("lat") Double lat,
                        @Field("lon") Double lon);

    @GET("product/delete/{id}")
    Call<Value> productDelete(@Path("id") int id);


//    @GET("/output_toko.php")
//    Call<Value> tokos(@Field("uid") String uid);
//
//    @FormUrlEncoded
//    @POST("/insert_product.php")
//    Call<Value> product(
//                        @Field("uid") String uid,
//                        @Field("nama_barang") String nama_barang,
//                        @Field("kategori") String kategori,
//                        @Field("harga") String harga,
//                        @Field("keterangan") String keterangan,
//                        @Field("pic") String pic);
//
//    @FormUrlEncoded
//    @POST("/update_toko_product.php")
//    Call<Value> update( @Field("produk_id") String produk_id,
//                        @Field("nama_barang") String nama_barang,
//                        @Field("kategori") String kategori,
//                        @Field("harga") String harga,
//                        @Field("keterangan") String keterangan,
//                        @Field("pic") String pic);
//
//    @FormUrlEncoded
//    @POST("/delete_toko_product.php")
//    Call<Value> delete(@Field("produk_id") String produk_id);
//
//    @FormUrlEncoded
//    @POST("/list_perkategori.php")
//    Call<Value> category(@Field("kategori") String kategori);
//
//
//    @FormUrlEncoded
//    @POST("/search_product.php")
//    Call<Value> search(@Field("search") String search);


}
