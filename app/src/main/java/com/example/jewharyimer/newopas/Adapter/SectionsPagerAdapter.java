package com.example.jewharyimer.newopas.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jewharyimer.newopas.Fragments.ChatsFragment;
import com.example.jewharyimer.newopas.Fragments.FriendsFragment;
import com.example.jewharyimer.newopas.Fragments.RequestsFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;

            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "COMMENTS";

            case 1:
                return "CHATS";

            case 2:
                return "MEMBERS";

            default:
                return null;
        }

    }

}
