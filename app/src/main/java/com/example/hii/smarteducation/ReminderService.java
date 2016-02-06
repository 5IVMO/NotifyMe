package com.example.hii.smarteducation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hii on 1/26/2016.
 */
public class ReminderService extends Service {

    Context mContext;
    String TAG = "service";
    String userID;
    String CurrentTime,CurrentDate;
    int Day,Month,Year,Hour,Minute;
    Firebase myFirebaseRef;
    private AppPreferences appPrefs;
    private static final int START_AFTER_SECONDS = 10;
    public ReminderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this.getApplicationContext();
        Firebase.setAndroidContext(getApplicationContext());
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");
        appPrefs = new AppPreferences(mContext);
        userID=appPrefs.getUserID();
        GetCurrentDateTime();
        checkForNotification();
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }
    public void  checkForNotification(){
        myFirebaseRef.child("users").child(userID.toString()).child("Tasks").child("Reminders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    try {
                        int i=0;
                        if (postSnapshot.getValue() != null) {
                            Reminder task = postSnapshot.getValue(Reminder.class);
                            String Task_Title = task.getTitle();
                            String Task_Date = task.getDate();
                            String Task_Time = task.getTime();

                            Log.d("CurrentDate",""+CurrentDate);
                            Log.d("Task_Date",""+Task_Date);

                            Log.d("CurrentTime",""+CurrentTime);
                            Log.d("Task_Time",""+Task_Time);

                            if (Task_Date.equals(CurrentDate) && Task_Time.equals(CurrentTime)) {
                                Log.i(TAG,"conditionTrue");
                                  notifyUser(Task_Title,Task_Date,Task_Time);
                          }

                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage().toString());
                    }
                }
                stopSelf();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void notifyUser(String Task_Title,String Task_Date,String Task_Time){

        int count=(int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setTicker("Notification")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(Task_Title)
                .setContentText(new StringBuilder().append("Date: ").append(Task_Date).append("\n").append("Time: ")
                .append(Task_Time));
       mBuilder.setContentIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0))
                .setAutoCancel(true);
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager notificationManager = (NotificationManager)getApplication().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(count, mBuilder.build());
    }
    public void GetCurrentDateTime()
    {
        //Get Current Date
        Calendar calendar = Calendar.getInstance();
        Day = calendar.get(calendar.DAY_OF_MONTH); //26
        Month = calendar.get(calendar.MONTH); //0
        Year = calendar.get(calendar.YEAR);//2016

        CurrentDate = Month+1 + "/" + Day + "/" + Year;

         //Get Current Time
        Hour = calendar.get(calendar.HOUR_OF_DAY);//14
        Minute = calendar.get(calendar.MINUTE);//39

        String state = " ";
        String min= " ";
        if(Hour > 12){
            Hour -=12;
            state = "PM";
        }
        else if (Hour == 0) {
            Hour += 12;
            state = "AM";
        } else if (Hour == 12)
            state = "PM";
        else
            state = "AM";

        if (Minute < 10)
            min = "0" + Minute ;
        else
            min = String.valueOf(Minute);

           CurrentTime = Hour + ":" + min + " " + state;
    }

        @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
