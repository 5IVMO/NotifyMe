package com.example.hii.smarteducation;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link ListFragment} subclass.
 */
public class ReminderFragment extends ListFragment {

    View view;
    Firebase myFirebaseRef;
    ArrayList<Reminder> list;
    MyCustomAdapter ca;
    ListView lvItems;
    Dialog dialog;
    Button addTask;
    EditText editTextTitle, editTextDate, editTexTime;
    String Title, Date, Time, userID;
    private AppPreferences appPrefs;
    ProgressBar progressBar;
    FloatingActionButton fab;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reminder, container, false);
        Firebase.setAndroidContext(getContext());
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");
        appPrefs = new AppPreferences(getActivity().getApplicationContext());
        userID = appPrefs.getUserID();
        setupListView();
        checkData();
        setupAlarm();
        loadReminders();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_add_reminder);
               // dialog.setTitle("Reminder...");
                dialog.show();

                addTask = (Button) dialog.findViewById(R.id.button_addTask);
                editTextTitle = (EditText) dialog.findViewById(R.id.editText_Title);
                editTextDate = (EditText) dialog.findViewById(R.id.editText_Date);
                editTexTime = (EditText) dialog.findViewById(R.id.editText_Time);

                editTextDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new mDateSetListener(), mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });
                editTexTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new mTimeSetListener(), hour, minute, false);
                        timePickerDialog.show();
                    }
                });
                addTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Title = editTextTitle.getText().toString();
                        Date = editTextDate.getText().toString();
                        Time = editTexTime.getText().toString();
                        if (!(Title.equals("") || Date.equals("") || Time.equals(""))) {

                            Reminder reminder = new Reminder(Title, Date, Time);
                            addReminder(reminder);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Fields Should not be left Empty", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                //Snackbar.make(view, "Clicked On FAB button", Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });

//        lvItems = (ListView) view.findViewById(android.R.id.list);
//        lvItems.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
//                if (lvItems.getLastVisiblePosition() == lvItems.getAdapter().getCount() - 1
//                        && lvItems.getChildAt(lvItems.getChildCount() - 1).getBottom() <= lvItems.getHeight())
//                {
//
//                    fab.setVisibility(View.GONE);
//                }
//
//            }
//        });
        return view;
    }

    public void setupListView() {
        lvItems = (ListView) view.findViewById(android.R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_loading);
        progressBar.setIndeterminate(true);
        list = new ArrayList<Reminder>();
        ca = new MyCustomAdapter(getActivity(), list);
        setListAdapter(ca);
    }

    public void setupAlarm(){
        Intent intentService = new Intent(getActivity().getApplicationContext(),TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity()
                .getApplication(), 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        final AlarmManager alarmManager = (AlarmManager)(getActivity().getSystemService( Context.ALARM_SERVICE ));
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+ 10 * 1000, 55 * 1000, pendingIntent);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void checkData(){

        myFirebaseRef.child("users").child(userID.toString()).child("Tasks").child("Reminders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    //data exists, do something
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    lvItems.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {
            }
        });
    }
    public void loadReminders() {
        try {
            myFirebaseRef.child("users").child(userID.toString()).child("Tasks").child("Reminders").addChildEventListener(new MyFirebaseChildListener(){
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    super.onChildAdded(dataSnapshot, s);
                    Reminder Reminder = dataSnapshot.getValue(Reminder.class);
                    list.add(Reminder);
                    ca.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    lvItems.setVisibility(View.VISIBLE);
                }
                // Get the data on a post that has been removed
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Reminder Reminder = dataSnapshot.getValue(Reminder.class);
                    ca.remove(Reminder);
                    ca.notifyDataSetChanged();
                }

            });


        } catch (Exception e) {
            Log.d("TAG1", e.getMessage().toString());
        }
    }

    public void addReminder(Reminder reminder) {

      myFirebaseRef.child("users").child(userID).child("Tasks").child("Reminders").push().setValue(reminder, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Toast.makeText(getActivity(), "Reminder Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteReminder(final Reminder st,int position){
        myFirebaseRef.child("users").child(userID).child("Tasks").child("Reminders").orderByChild("title")
                .equalTo((String) st.getTitle())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.hasChildren()) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Reminder Reminder = snapshot.getValue(Reminder.class);
                                String Task_title= Reminder.getTitle();
                                String Task_Date= Reminder.getDate();
                                String Task_Time= Reminder.getTime();
                                if(Task_Date.equals(st.getDate()) && Task_Time.equals(st.getTime())){
                                    snapshot.getRef().removeValue();
                                    Toast.makeText(getActivity(), "Reminder Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
        list.remove(position);
        ca.notifyDataSetChanged();
    }
    public class MyCustomAdapter extends ArrayAdapter {

        public MyCustomAdapter(Activity activity, ArrayList<Reminder> list) {
            super(activity, 0, list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_list_item, null);
            }
            final Reminder st = (Reminder) getItem(position);
            TextView t1 = (TextView) convertView.findViewById(R.id.titleTextView);
            t1.setText(st.getTitle());


            TextView t2 = (TextView) convertView.findViewById(R.id.dateTextView);
            t2.setText("Date: " + st.getDate());

            TextView t3 = (TextView) convertView.findViewById(R.id.timeTextView);
            t3.setText("Time: " + st.getTime());

            Button b2 = (Button) convertView.findViewById(R.id.button);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   deleteReminder(st,position);
                }
            });

            return convertView;
        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            editTextDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/").append(mDay).append("/")
                    .append(mYear));
        }
    }

    class mTimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // TODO Auto-generated method stub
            // getCalender();
            int mHour = hour;
            int mMinute = minute;
            String state = " ";
            String min = " ";
            if (mHour > 12) {
                mHour -= 12;
                state = "PM";
            } else if (mHour == 0) {
                mHour += 12;
                state = "AM";
            } else if (mHour == 12)
                state = "PM";
            else
                state = "AM";

            if (mMinute < 10)
                min = "0" + mMinute;
            else
                min = String.valueOf(mMinute);


            // String time = mHour + ":" + minute + ":" + state;

            editTexTime.setText(new StringBuilder()
                    .append(mHour).append(":").append(min).append(" ").append(state));
        }
    }
}