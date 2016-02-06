package com.example.hii.smarteducation;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText editTextEmail,editTextUserPassWord;
    Button mLogin;
    TextView textViewSignUp;
    private String userEmail,userPassword,userID;
    Firebase myFirebaseRef;
    ProgressDialog progressDialog;
    private AppPreferences appPrefs;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_login, container, false);
        Firebase.setAndroidContext(getContext());
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");
        initView();
        return view;
    }

    public void initView(){
        appPrefs = new AppPreferences(getActivity().getApplicationContext());
        editTextEmail= (EditText) view.findViewById(R.id.editText_Email);
        editTextUserPassWord= (EditText) view.findViewById(R.id.editText_UserPassword);
        mLogin= (Button) view.findViewById(R.id.button_login);
        mLogin.setOnClickListener(this);
        textViewSignUp= (TextView) view.findViewById(R.id.signUp_text);
        textViewSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {

            case R.id.button_login:
                progressDialog = ProgressDialog.show(getActivity(),"Please Wait...","Signing In...",true,false);
                userEmail=editTextEmail.getText().toString();
                userPassword=editTextUserPassWord.getText().toString();
                if(!(userEmail.equals("") || userPassword.equals(""))){
                    myFirebaseRef.authWithPassword(userEmail, userPassword, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Successfully Login",Toast.LENGTH_SHORT).show();
                            userID=authData.getUid().toString();
                            appPrefs.saveUserID(userID);
                            Intent intent = new Intent(getActivity(),HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),firebaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Fields Should not be left Empty", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.signUp_text:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container1,new SignUpFragment()).addToBackStack("Tag").commit();
                break;

            default:
                break;
        }

    }
}
