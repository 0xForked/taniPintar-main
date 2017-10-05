package id.my.asmith.rizalapps.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Asmith on 9/8/2017.
 */

@IgnoreExtraProperties
public class User {

    public String email;
    public String fullName;
    public String phone;
    public boolean toko;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullName,  String email, String phone, boolean toko) {
        this.toko = toko;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;

    }


}
// [END blog_user_class]