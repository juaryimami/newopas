package com.example.jewharyimer.newopas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jewharyimer.newopas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Add_member extends AppCompatActivity {

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase,mtemp,ref;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;
    private ProgressDialog mRegProgress;

    //private View mMainView ;

    Button add_memb;

    String titl,name,text;
    EditText textmemb;
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\/\\.\\_\\%\\-\\+]{15,255}"

    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        add_memb=findViewById(R.id.MEMBER_ADD_ID);
        textmemb=findViewById(R.id.MEMBERI_LD);
        mRegProgress = new ProgressDialog(this);

        Toolbar toolbar =findViewById(R.id.rstoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("add member");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mFriendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                titl=users.getProjecttitle();
                name=users.getName();
                ref=FirebaseDatabase.getInstance().getReference("Friends").child(titl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        add_memb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=textmemb.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(Add_member.this,"FILL THE MEMBERS NAME BEFOR CLICKING THE BUTTON",Toast.LENGTH_SHORT).show();
                }else if(!USERNAME_PATTERN.matcher(text).matches()){
                    textmemb.setError("please enter correct");
                }else{
                    mRegProgress.setTitle("Registering Members");
                    mRegProgress.setMessage("Please wait while we adding member name !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    final HashMap<String, String> hashmap1 = new HashMap<>();
                    hashmap1.put("name",text);
                    ref.child(ref.push().getKey()).setValue(hashmap1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgress.dismiss();
                                        Toast.makeText(Add_member.this,"Members successfully sdded",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Add_member.this,MainActivity.class));
                                    }else {
                                        Toast.makeText(Add_member.this,"Member not sdded",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }




            }
        });

    }
}
