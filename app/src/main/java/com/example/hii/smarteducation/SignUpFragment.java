package com.example.hii.smarteducation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    View view;
    ImageView imageView;
    Button mSignUp;
    Spinner Gender;
    EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPassWord;
    String name,email,userID,gender,passWord,confirmPassword;
    String imageFile="";
    Firebase myFirebaseRef;
    ProgressDialog progressDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        Firebase.setAndroidContext(getContext());
        myFirebaseRef = new Firebase("https://smarteducation.firebaseio.com/");

        initView();
        return  view;

    }
    public void initView(){

        mSignUp= (Button) view.findViewById(R.id.button_signUp);
        mSignUp.setOnClickListener(this);
        editTextName= (EditText) view.findViewById(R.id.editText_Name);
        editTextEmail= (EditText) view.findViewById(R.id.editText_Email);
        editTextPassword= (EditText) view.findViewById(R.id.editText_Password);
        editTextConfirmPassWord= (EditText) view.findViewById(R.id.editText_ConfirmPassWord);
        imageView= (ImageView) view.findViewById(R.id.profile_image);
        imageView.setOnClickListener(this);

        Gender = (Spinner) view.findViewById(R.id.spinner1);
        getSpinnerData();

    }
    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.profile_image:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,0);

                break;
            case R.id.button_signUp:
                name=editTextName.getText().toString();
                email=editTextEmail.getText().toString();
                passWord=editTextPassword.getText().toString();
                confirmPassword=editTextConfirmPassWord.getText().toString();
                gender=String.valueOf(Gender.getSelectedItem());

                if(!(name.equals("") || email.equals("") || passWord.equals("") || confirmPassword.equals(""))){

                    if(!(imageFile.equals(""))){
                        if (passWord.equals(confirmPassword)) {
                            progressDialog = ProgressDialog.show(getActivity(),"Please Wait...","Signing Up",true,false);
                            myFirebaseRef.createUser(email, passWord, new Firebase.ValueResultHandler<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> result) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Successfully created user account",Toast.LENGTH_SHORT).show();
                                    userID=result.get("uid").toString();
                                    User user=new User(name,email,userID,gender,passWord,confirmPassword,imageFile);
                                    addUsers(user);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container1,new LoginFragment()).commit();

                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),firebaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Passwords does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),"Tap on Image to select Your image", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Fields Should not be left Empty", Toast.LENGTH_SHORT).show();

                }

                break;

            default:
                break;
        }
      }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            try {
                //this is for picking Image from Gallery or file
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
             //   bitmap.recycle();
                byte[] byteArray = stream.toByteArray();
                imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void getSpinnerData(){
        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), R.layout.spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        Gender.setAdapter(dataAdapter);

    }
    public void addUsers(User user){

                myFirebaseRef.child("users").child(userID).child("AccountInfo").push().setValue(user, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Toast.makeText(getActivity(),"User Added",Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }
