package com.example.sanjeevaniadmin;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sanjeevaniadmin.Fragments.ChatFragment;
import com.example.sanjeevaniadmin.Fragments.ListFragment;
import com.example.sanjeevaniadmin.Fragments.UsersFragment;
import com.google.android.material.tabs.TabLayout;

public class ChatActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    //     private FrameLayout frameLayout;
    // private ChatFragment chatFragment;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");

        tabLayout = findViewById(R.id.chat_tabLayout);
        viewPager = findViewById(R.id.chat_viewPager);

        MyFragmentViewPagerAdapter myFragmentViewPageerAdapter = new MyFragmentViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentViewPageerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new ChatFragment();
            }
            if (position == 1) {
                return new ListFragment();
            }
            if (position == 2) {
                return new UsersFragment();
            }
            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Chats";
            }
            if (position == 1) {
                return "Lists";
            }
            if (position == 2) {
                return "Users";
            }
            return super.getPageTitle(position);
        }

    }
}
