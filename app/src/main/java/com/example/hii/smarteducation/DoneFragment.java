package com.example.hii.smarteducation;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneFragment extends ListFragment {

    View view;
    Firebase myFirebaseRef,doneQuery;
    ArrayList<Todo> list;
    MyCustomAdapter ca;
    ListView lvItems;
    String userID;
    private AppPreferences appPrefs;
    ProgressBar progressBar;

    public DoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_done, container, false);
        Firebase.setAndroidContext(getContext());
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");
        appPrefs = new AppPreferences(getActivity().getApplicationContext());
        userID = appPrefs.getUserID();
        doneQuery = myFirebaseRef.child("users").child(userID.toString()).child("Tasks").child("Todos").child("Done");
        setupListView();
        checkData();
        loadTodos();
        return view;
    }

    public void setupListView() {
        lvItems = (ListView) view.findViewById(android.R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_loading);
        progressBar.setIndeterminate(true);
        list = new ArrayList<Todo>();
        ca = new MyCustomAdapter(getActivity(), list);
        setListAdapter(ca);
    }
    public void checkData(){

        doneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("TAG","checkData");
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
    public void loadTodos() {

        try {
            doneQuery.addChildEventListener(new MyFirebaseChildListener(){
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    super.onChildAdded(dataSnapshot, s);
                    Todo todo = dataSnapshot.getValue(Todo.class);
                    list.add(todo);
                    ca.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    lvItems.setVisibility(View.VISIBLE);
                }
                // Get the data on a post that has been removed
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Todo todo = dataSnapshot.getValue(Todo.class);
                    ca.remove(todo);
                    ca.notifyDataSetChanged();
                }

            });


        } catch (Exception e) {
            Log.d("TAG1", e.getMessage().toString());
        }
    }
    public void deleteTodo(final Todo st,int position){
        doneQuery.orderByChild("title")
                .equalTo((String) st.getTitle())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Todo todo = snapshot.getValue(Todo.class);
                                String Todo_Description=todo.getDescription();

                                if(Todo_Description.equals(st.getDescription())){
                                    snapshot.getRef().removeValue();
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
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

        public MyCustomAdapter(Activity activity, ArrayList<Todo> list) {
            super(activity, 0, list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_list_item, null);
            }
            final Todo st = (Todo) getItem(position);
            TextView t1 = (TextView) convertView.findViewById(R.id.titleTextView);
            t1.setText(st.getTitle());


            TextView t2 = (TextView) convertView.findViewById(R.id.dateTextView);
            t2.setText("Description: " + st.getDescription());

            TextView t3 = (TextView) convertView.findViewById(R.id.timeTextView);
            t3.setText(" ");

            Button b1 = (Button) convertView.findViewById(R.id.button);
            b1.setText("Delete");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTodo(st,position);
                }
            });

            return convertView;
        }
    }


}
