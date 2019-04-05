package com.example.jewharyimer.newopas;

import android.content.Intent;
import android.content.pm.ChangedPackages;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Change_Password extends AppCompatActivity {

    EditText currenttxt,newtxt,confirmtxt;
    Button change;
    FirebaseUser firebaseAuth;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[a-zA-Z])" +
            "(?=.*[@#$%^&+=])" +
            "(?=\\S+$)" +
            ".{6,8}" +
            "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        Toolbar toolbar =findViewById(R.id.rstoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currenttxt=findViewById(R.id.current_password);
        newtxt=findViewById(R.id.new_password);
        confirmtxt=findViewById(R.id.confirmation);
        firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();

        change=findViewById(R.id.btn_CHANGE);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentpassword=currenttxt.getText().toString();
                String newtpassword=newtxt.getText().toString();
                String confirmation=confirmtxt.getText().toString();
                if(currentpassword.equals("") || newtpassword.equals("") || confirmation.equals("")){
                    Toast.makeText(Change_Password.this,"All fields are requires!",Toast.LENGTH_SHORT).show();
                }else if(!PASSWORD_PATTERN.matcher(newtpassword).matches()) {
                    newtxt.setError("Password too weak");

                }
                else if(!PASSWORD_PATTERN.matcher(currentpassword).matches()) {
                    newtxt.setError("Password too weak");

                }
                else if(!newtpassword.equals(confirmation)){
                    confirmtxt.setError("confirmation error");
                }


                else {
                    firebaseAuth.updatePassword(newtpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Change_Password.this,"PASSWORD SUCCESFULLY CHANGED",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Change_Password.this,ProfileActivity.class));
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText(Change_Password.this,error,Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
    }
}
