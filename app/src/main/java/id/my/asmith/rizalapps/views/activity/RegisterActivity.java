package id.my.asmith.rizalapps.views.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.User;

public class RegisterActivity extends AppCompatActivity {

    //Declaration
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText fullName, phone, mEmail, mPass;
    private Button btnRegister;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register Activity");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //Progress dialog
        mProgressDialog = new ProgressDialog(this);

        //Setup firebase Api
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fullName = (EditText) findViewById(R.id.fullnameText);
        phone = (EditText) findViewById(R.id.phoneText);
        mEmail = (EditText) findViewById(R.id.emailText_regis);
        mPass = (EditText) findViewById(R.id.passwordText_regis);
        btnRegister = (Button) findViewById(R.id.regisButton);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("user", "signUp");
                if (!validateForm()) {
                    return;
                }
                //getString from editText
                String email = mEmail.getText().toString();
                final String password = mPass.getText().toString();
                //Progress singUp
                mProgressDialog.show();
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("user", "createUser:onComplete:" +
                                        task.isSuccessful());

                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    onAuthSuccess(task.getResult().getUser());
                                } else {
                                    // there was an error
                                    if (password.length() < 6) {
                                        mPass.setError(getString(R.string.error_message_min_pwd));
                                    } else {
                                        Toast.makeText(RegisterActivity.this,
                                                "Oops! ada masalah dengan email anda," +
                                                        " coba periksa kembali!!",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
            }
        });

    }


    private void onAuthSuccess(FirebaseUser user) {
        String fullname = fullName.getText().toString();
        String phones = phone.getText().toString();
        boolean tokos = false;
        // Write new user
        writeNewUser(user.getUid(), fullname , user.getEmail(), phones, tokos);

        // Go to MainActivity
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(fullName.getText().toString())) {
            fullName.setError("Required");
            result = false;
        } else {
            fullName.setError(null);
        }

        if (TextUtils.isEmpty(phone.getText().toString())) {
            phone.setError("Required");
            result = false;
        } else {
            phone.setError(null);
        }

        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError("Required");
            result = false;
        } else {
            mEmail.setError(null);
        }

        if (TextUtils.isEmpty(mPass.getText().toString())) {
            mPass.setError("Required");
            result = false;
        } else {
            mPass.setError(null);
        }

        return result;
    }
    // [START basic_write]
    private void writeNewUser(String userId, String email, String fullName, String phone, boolean toko ) {
        User user = new User(email, fullName, phone, toko);

        mDatabase.child("users").child(userId).setValue(user);
    }

}
