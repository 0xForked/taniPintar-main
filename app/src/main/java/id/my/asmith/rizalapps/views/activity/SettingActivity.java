package id.my.asmith.rizalapps.views.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.my.asmith.rizalapps.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
}
