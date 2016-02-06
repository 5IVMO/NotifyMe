package com.example.hii.smarteducation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class HomeActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private AppPreferences appPrefs;
    TextView nameTextView,emailTextView;
    ImageView imageView;
    private String userID,imageFile;
    Firebase myFirebaseRef,userQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");
        appPrefs = new AppPreferences(getApplicationContext());
        userID = appPrefs.getUserID();
        userQuery = myFirebaseRef.child("users").child(userID.toString()).child("AccountInfo");

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View headerView= navigationView.getHeaderView(0);
       // RelativeLayout headerView= (RelativeLayout) findViewById(R.id.header);
        nameTextView= (TextView) headerView.findViewById(R.id.username);
        emailTextView=(TextView) headerView.findViewById(R.id.email);
        imageView= (ImageView) headerView.findViewById(R.id.profile_image);
        loadHeaderData();
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.todoTasks:
                        Toast.makeText(getApplicationContext(),"TodoTasks Selected",Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame,new TodoTaskFragment()).commit();
                        return true;

                    case R.id.Reminder:
                        Toast.makeText(getApplicationContext(), "Reminder Selected", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame,new ReminderFragment()).commit();
                        return true;

                    case R.id.signOut:
                        appPrefs.signOutUser();
                       Toast.makeText(getApplicationContext(),"User SignOut",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        getSupportFragmentManager().
                beginTransaction().
                add(R.id.frame, new ReminderFragment()).commit();

    }
    public void loadHeaderData(){
        try {

            userQuery.addChildEventListener(new ChildEventListener() {
                // Retrieve new posts as they are added to the database
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                    long count=snapshot.getChildrenCount();
                    User user = snapshot.getValue(User.class);
                    emailTextView.setText(user.getEmail().toString());
                    nameTextView.setText(user.getName().toString());
                    imageFile = user.getImageFile().toString();
                    byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    imageView.setImageBitmap(bmp);
                }

                // Get the data on a post that has changed
                @Override
                public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {

               }

                // Get the data on a post that has been removed
                @Override
                public void onChildRemoved(DataSnapshot snapshot) {

                }

                // Get the data on a post that has changed
                @Override
                public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
               }

                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        } catch (Exception e) {
            Log.d("TAG1", e.getMessage().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
