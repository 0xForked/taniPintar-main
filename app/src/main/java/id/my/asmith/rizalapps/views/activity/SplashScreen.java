package id.my.asmith.rizalapps.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import id.my.asmith.rizalapps.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start  app main activity
                // firebase Auth
                // Get Firebase auth instance (user still login)
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }else {
                    // user has logout
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
