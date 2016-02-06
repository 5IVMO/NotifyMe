package com.example.hii.smarteducation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hii on 1/27/2016.
 */
public class TimerReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION = 3456;
    private AppPreferences appPrefs;
    private String userID;
    int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {

        appPrefs = new AppPreferences(context);
        userID = appPrefs.getUserID();

        if(!(userID.equals(""))){

                Intent intentService = new Intent(context, ReminderService.class);
                intentService.putExtra("userID", userID);
                context.startService(intentService);
        }
       }
    }
