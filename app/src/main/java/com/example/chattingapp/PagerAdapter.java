package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//this "PagerAdapter" class is used to mange the fragments classes and its xml files and determine witch fragments
//to display in each page of the pages of FragmentPagerAdapter

//we use FragmentPagerAdapter instead of "Action bar tabs with Fragments" to
//allow the user to swipe left and right through "pages" of content which are usually different fragments
public class PagerAdapter extends FragmentPagerAdapter {

    int tabcount;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //according to the position after the swap we display the fragment starting with 0 at the chats tab.
        switch (position) {
            case 0:
                return new chatFragment();
            case 1:
                return new statusFragment();
            case 2:
                return new callFragment();
            default:
                return null;
        }
    }

    //returns number of fragment(pages)
    @Override
    public int getCount() {
        return tabcount;
    }
}
