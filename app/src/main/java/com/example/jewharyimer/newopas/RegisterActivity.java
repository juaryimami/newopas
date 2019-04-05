package com.example.jewharyimer.newopas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jewharyimer.newopas.Model.Friends;
import com.example.jewharyimer.newopas.Model.Projects;
import com.example.jewharyimer.newopas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
        implements  RegisterDialog.ExampleDialogListener{

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword,title;
    private Button mCreateBtn;
    String titl, typ,member1,member2,member3,member4,member5,member6;
    String display_name,email,password;
    RadioGroup radioGroup;
    RadioButton usertype;
    boolean completed =false;


    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[a-zA-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)" +
            ".{6,8}" +
            "$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
                    "[a-zA-Z0-9\\+\\/\\.\\_\\%\\-\\+]{15,255}"

                     );
    private static final Pattern USERNAME_PATTERN1 = Pattern.compile(
            "[a-zA-Z0-9\\+\\/\\.\\_\\%\\-\\+\\t]{15,255}"

    );


    private Toolbar mToolbar;

    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;
    List<Friends> uploadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar Set
        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uploadList = new ArrayList<>();
        mRegProgress = new ProgressDialog(this);




        mAuth = FirebaseAuth.getInstance();



        mDisplayName =  findViewById(R.id.register_display_name);
        mEmail =  findViewById(R.id.register_email);
        mPassword =  findViewById(R.id.reg_password);
        mCreateBtn =  findViewById(R.id.reg_create_btn);
        title=findViewById(R.id.register_type);




        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                      display_name = mDisplayName.getEditText().getText().toString();
                      email = mEmail.getEditText().getText().toString();
                      password = mPassword.getEditText().getText().toString();
                       titl=title.getEditText().getText().toString();

                          checkButton();

                if(TextUtils.isEmpty(display_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(titl)|| TextUtils.isEmpty(typ)){
                    Toast.makeText(RegisterActivity.this, "ALL FIELDS ARE REQUIRED", Toast.LENGTH_SHORT).show();
                    }
                    else if (!USERNAME_PATTERN.matcher(display_name).matches()) {
                    mDisplayName.setError("PLEASE ENTER CORRECT ID NUMBER");
                } else if (!USERNAME_PATTERN1.matcher(titl).matches()) {
                    title.setError("project name cann't start with number");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("please enter valid email");
                }else if(!PASSWORD_PATTERN.matcher(password).matches()){
                    mPassword.setError("password should be a-zA-b0-9[@#$*&]");
                }else{
                    mRegProgress.setTitle("Registering Users");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);


                    if(typ.equals("advisor")){
                        openDialog();
                        //memberrigistry();
                        /*mRegProgress.show();
                        register_user(display_name, email, password);*/

                    }else {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Friends").child(titl);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Friends friends=dataSnapshot.getValue(Friends.class);
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Friends upload = postSnapshot.getValue(Friends.class);
                                    uploadList.add(upload);
                                }
                                String[] uploads = new String[uploadList.size()];

                                for (int i = 0; i < uploads.length; i++) {
                                    uploads[i] = uploadList.get(i).getName();
                                    if(uploads[i].equals(display_name)){
                                        mRegProgress.show();
                                        register_user(display_name,email,password);
                                        completed=true;
                                        break;}
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    }
                }
               });


         /*if(completed){
             mRegProgress.show();
             register_user(display_name,email,password);
         }else {
            Toast.makeText(RegisterActivity.this,"FAIL TO COMPLETE REGISTERING",Toast.LENGTH_SHORT).show();
         }*/

    }




public void memberrigistry(){


}
    @Override
    public void applyTexts(String memb1) {
        member1=memb1;
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
//    String uid = current_user.getUid();
        HashMap<String, String> hashmap1 = new HashMap<>();
        hashmap1.put("name", member1);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(titl);
        mDatabase.child(mDatabase.push().getKey()).setValue(hashmap1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    completed=true;
                    mRegProgress.show();
                    register_user(display_name,email,password);
                }else {completed=false;
                    Toast.makeText(RegisterActivity.this,"FAIL TO COMPLETE REGISTERING",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
        public void checkButton(){
        radioGroup=findViewById(R.id.radioGroup);
        int radioId=radioGroup.getCheckedRadioButtonId();
        usertype=findViewById(radioId);
        typ=usertype.getText().toString();

    }

    public void openDialog() {

        RegisterDialog exampleDialog = new RegisterDialog();
        exampleDialog.show(getSupportFragmentManager(), "register dialog");

    }


    private void register_user(final String display_name, String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    mRegProgress.show();


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there I'm using new opass App.");
                    userMap.put("image", "default");
                    userMap.put("projecttitle", titl);
                    userMap.put("type", typ);
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mRegProgress.dismiss();
                                completed=false;
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                        }
                    });


                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
