package id.my.asmith.rizalapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asmith on 10/3/2017.
 */

public class PostProduct {

    @SerializedName("produk_id")
    @Expose
    private Integer produkId;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;

    public Integer getProdukId() {
        return produkId;
    }

    public void setProdukId(Integer produkId) {
        this.produkId = produkId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lat) {
        this.lon = lon;
    }
}
