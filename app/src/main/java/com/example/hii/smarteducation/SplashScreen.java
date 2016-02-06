package com.example.hii.smarteducation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * Created by hii on 1/27/2016.
 */
public class SplashScreen extends Activity implements ProgressBarSplash.LoadingTaskFinishedListener {
    private AppPreferences appPrefs;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        appPrefs = new AppPreferences(getApplicationContext());
        userID=appPrefs.getUserID();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new ProgressBarSplash(progressBar, this).execute();
  }
    @Override
    public void onTaskFinished(){
        completeSplash();
    }
    private void completeSplash(){
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        if(!(userID.equals(""))) {
               Intent openHomeActivity = new Intent(SplashScreen.this,HomeActivity.class);
               startActivity(openHomeActivity);
             }
         else{
                 Intent openLoginActivity = new Intent(SplashScreen.this,MainActivity.class);
                 startActivity(openLoginActivity);
             }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
