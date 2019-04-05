package com.example.jewharyimer.newopas.Fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jewharyimer.newopas.Add_member;
import com.example.jewharyimer.newopas.ChatActivity;
import com.example.jewharyimer.newopas.Model.Friends;
import com.example.jewharyimer.newopas.Model.Users;
import com.example.jewharyimer.newopas.ProfileActivity;
import com.example.jewharyimer.newopas.R;
import com.example.jewharyimer.newopas.RegisterDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsFragment extends Fragment implements  RegisterDialog.ExampleDialogListener{

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase,mtemp;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView ;

    Button add_memb;

    String titl,name;

    FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter;
    public FriendsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        add_memb=mMainView.findViewById(R.id.add_membership);

        mtemp=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrent_user_id);
        mtemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);

                titl=users.getProjecttitle();
                mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(titl);
                mUsersDatabase.keepSynced(true);

                mFriendsDatabase.keepSynced(true);
                if(users.getType().equals("advisor")){
                    //add_memb.setVi
                    add_memb.setEnabled(true);
                    add_memb.setVisibility(View.VISIBLE);
                    add_memb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileInten = new Intent(getContext(), Add_member.class);
                            startActivity(profileInten);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friends> option=new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(mFriendsDatabase,Friends.class).build();
        friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(

                option) {
            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout,viewGroup,false);
                FriendsViewHolder viewHolder=new FriendsViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder friendsViewHolder, int position, @NonNull final Friends friends) {

                friendsViewHolder.setDate(friends.getName());

                final String list_user_id = getRef(position).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String userName = dataSnapshot.child("name").getValue().toString();
                                String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                                if(dataSnapshot.hasChild("online")) {

                                    String userOnline = dataSnapshot.child("online").getValue().toString();
                                    friendsViewHolder.setUserOnline(userOnline);

                                }

                                friendsViewHolder.setName(userName);
                                friendsViewHolder.setUserImage(userThumb, getContext());

                                friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                        builder.setTitle("Select Options");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //Click Event for each item.
                                                if(i == 0){

                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id);
                                                    startActivity(profileIntent);

                                                }

                                                if(i == 1){

                                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                    chatIntent.putExtra("user_id", list_user_id);
                                                    chatIntent.putExtra("user_name", userName);
                                                    startActivity(chatIntent);

                                                }

                                            }
                                        });

                                        builder.show();

                                    }
                                });





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);
        //friendsRecyclerViewAdapter.startListening();


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){

            TextView userStatusView = mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(String name){

            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView =  mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView =  mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }
    @Override
    public void applyTexts(String memb1) {
        HashMap<String, String> hashmap1 = new HashMap<>();
        hashmap1.put("name", memb1);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mFriendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                titl=users.getProjecttitle();
                name=users.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Friends").child(titl);

        ref.child(ref.push().getKey()).setValue(hashmap1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(MainActivity.this,"Members successfully sdded",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        /*DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Friends").child(titl);
        ref.child(memb1);
        */

    }
    public void openDialog() {

        RegisterDialog exampleDialog = new RegisterDialog();
        exampleDialog.show(getFragmentManager(),"register dialog");

    }

}
