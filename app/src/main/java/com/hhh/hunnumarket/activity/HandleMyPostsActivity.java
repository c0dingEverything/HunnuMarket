package com.hhh.hunnumarket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.fragment.HandleMyPostsFragment;
import com.hhh.hunnumarket.fragment.ScanFragment;

public class HandleMyPostsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_my_posts);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.act_handle_posts_fl_placeholder, new HandleMyPostsFragment());
        transaction.commit();
    }
}
