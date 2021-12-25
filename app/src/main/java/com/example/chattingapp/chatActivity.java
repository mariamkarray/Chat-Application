package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class chatActivity extends AppCompatActivity {

    //TabLayout provides a horizontal layout to display tabs.
    TabLayout tabLayout;

    TabItem mchat, mcall, mstatus;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    androidx.appcompat.widget.Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tabLayout= findViewById(R.id.include);
        mchat = findViewById(R.id.chats);
        mcall = findViewById(R.id.calls);
        mstatus = findViewById(R.id.status);
        viewPager = findViewById(R.id.container);
        mtoolbar = findViewById(R.id.toolbar);

        //enabling action on the toolbar to add the menu later
        setSupportActionBar(mtoolbar);

        //getting the image of the icon from drawable of the menu to change the menu image to it
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_more_vert_24);

        //setting the menu icon image int the toolbar
        mtoolbar.setOverflowIcon(drawable);

        pagerAdapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());

        //when the user swap the tabs we set the current view from the PagerAdapter
        viewPager.setAdapter(pagerAdapter);

        //setting onclick listener for the tabs to switch to the correct view if the user clicked the tab instead of swapping to it.
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //set the view of the selected tab
                viewPager.setCurrentItem(tab.getPosition());

                //if the user changed the tab selected we need to update the pagerAdapter data
                if(tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener (new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    //setting actions to be done after selecting items from the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if the user selected the profile from the menu we open the profile intent else if he tabbed the settings menu we create a toast.
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(chatActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Setting is clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //we here are specifying the the options of the menu in the chat activity using the xml file containing the items of the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}