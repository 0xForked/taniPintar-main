package id.my.asmith.rizalapps.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Asmith on 9/27/2017.
 */
    @IgnoreExtraProperties
    public class Users {

        public String email;
        public String fullName;
        public String phone;
        public boolean toko;
        public String pic;
        public String tempat;
        public String tanggal;
        public String gender;

        public Users() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Users(String fullName,  String email, String phone, boolean toko, String pic,
                     String tempat, String gender, String tanggal) {
            this.toko = toko;
            this.email = email;
            this.fullName = fullName;
            this.phone = phone;
            this.pic = pic;
            this.tempat = tempat;
            this.tanggal = tanggal;
            this.gender = gender;

        }

    }
