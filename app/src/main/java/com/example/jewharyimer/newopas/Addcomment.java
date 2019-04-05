package com.example.jewharyimer.newopas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.jewharyimer.newopas.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;

public class Addcomment extends AppCompatActivity {

    private EditText inputNote;

    DatabaseReference mDatabaseReference;
    DatabaseReference nDatabaseReference;

    FirebaseUser firebaseUser;

    String title,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomment);
        Toolbar toolbar = findViewById(R.id.add_text_toolbar);
        setSupportActionBar(toolbar);

        inputNote = findViewById(R.id.input_note);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Users users=dataSnapshot.getValue(Users.class);
             title=users.getProjecttitle();
             name=users.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addcomment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            onSaveNote();
        }else {
            startActivity(new Intent(Addcomment.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {

        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            //long date = new Date().getTime(); // get  system time

            nDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Comments").child(title);
            //HashMap<String,String> hashMap=new HashMap<>();
            HashMap<String,String> hashMap1=new HashMap<>();
            //hashMap1.put("date",name);
            hashMap1.put(title,text);
            nDatabaseReference.child(nDatabaseReference.push().getKey()).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(Addcomment.this,"COMMENT SUCCESSFULLY ADDED",Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(Addcomment.this,MainActivity.class));
                   }else {
                       Toast.makeText(Addcomment.this,"COMMENT NOT ADDED", Toast.LENGTH_SHORT).show();
                   }
                }
            });
            } else {

            }


        }

    }


