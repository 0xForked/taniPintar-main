package id.my.asmith.rizalapps.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Asmith on 9/28/2017.
 */

@IgnoreExtraProperties
public class Tokos {

    public String namaToko;
    public String nomorToko;
    public String uid;
    public String namaPemilik;
    public String emailPemilik;
    public String lokasiToko;

    public Tokos (){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Tokos(String namaToko, String nomorToko, String uid, String namaPemilik, String emailPemilik, String lokasiToko) {
        this.namaToko = namaToko;
        this.nomorToko = nomorToko;
        this.uid = uid ;
        this.namaPemilik = namaPemilik;
        this.emailPemilik = emailPemilik;
        this.lokasiToko = lokasiToko;

    }
}
