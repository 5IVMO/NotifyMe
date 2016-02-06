package com.example.hii.smarteducation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container1, new LoginFragment()).commit();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }
}
