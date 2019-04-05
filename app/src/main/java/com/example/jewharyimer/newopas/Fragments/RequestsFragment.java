package com.example.jewharyimer.newopas.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jewharyimer.newopas.Add_member;
import com.example.jewharyimer.newopas.Addcomment;
import com.example.jewharyimer.newopas.Model.Comments;
import com.example.jewharyimer.newopas.Model.Users;
import com.example.jewharyimer.newopas.My_Adapter;
import com.example.jewharyimer.newopas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class RequestsFragment extends Fragment {

        private  FloatingActionButton fab;
        private  RecyclerView rview;
        private View cview;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase,mtemp;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id,titli;
    public RequestsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cview=inflater.inflate(R.layout.fragment_requests, container, false);

        fab=cview.findViewById(R.id.comment_fab);


        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mtemp=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrent_user_id);
        mtemp.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(users.getProjecttitle());
                mFriendsDatabase.keepSynced(true);
                titli=users.getProjecttitle();




                recyclerView =cview.findViewById(R.id.comment_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                My_Adapter my_adapter=new My_Adapter(recyclerView,getContext(),new ArrayList<String>(),new ArrayList<String>());
                recyclerView.setAdapter(my_adapter);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Comments").child(titli);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //HashMap<String,Object> url = (HashMap<String, Object>)dataSnapshot.getValue();
                        String filename=dataSnapshot.getKey();
                         // Comments comments= dataSnapshot.getValue(Comments.class);
                        //String filename=comments.getNote();
                       // String date=comments.getDate();
//                String filename=dataSnapshot.getKey();
//                HashMap<String,Object> url = (HashMap<String, Object>)dataSnapshot.getValue();
                        String url=dataSnapshot.child(titli).getValue(String.class);
                        ((My_Adapter)recyclerView.getAdapter()).update(url,filename);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                if(users.getType().equals("advisor")){

                    fab.setEnabled(true);
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileInten = new Intent(getContext(), Addcomment.class);
                            startActivity(profileInten);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
















        return cview;





    }

}
