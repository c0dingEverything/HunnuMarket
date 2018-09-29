package com.hhh.hunnumarket.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.fragment.DissFragment;
import com.hhh.hunnumarket.fragment.MessageFragment;
import com.hhh.hunnumarket.fragment.MineFragment;
import com.hhh.hunnumarket.fragment.ScanFragment;
import com.hhh.hunnumarket.utils.BottomNavigationViewHelper;
import com.hhh.hunnumarket.utils.DataUtil;

import org.xutils.x;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private boolean shouldFinish = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    ft.replace(R.id.fragment_container, new ScanFragment());
                    ft.commit();
                    return true;
                case R.id.navigation_message:
                    ft.replace(R.id.fragment_container, new MessageFragment());
                    ft.commit();
                    return true;
                case R.id.navigation_diss:
                    ft.replace(R.id.fragment_container, new DissFragment());
                    ft.commit();
                    return true;
                case R.id.navigation_mine:
                    ft.replace(R.id.fragment_container, new MineFragment());
                    ft.commit();
                    return true;
            }
            return false;
        }
    };
    public FragmentManager fm = null;
    public ExecutorService service;
    public long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        DataUtil.updateCategoriesFromServer();     //更新类别
        DataUtil.updateUserInfoFromServer();       //更新用户信息
        service = Executors.newSingleThreadExecutor();
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new ScanFragment());
        ft.commit();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        if (!shouldFinish) {
            start = System.currentTimeMillis();
            Toast.makeText(this, R.string.msg_exit_confirm, Toast.LENGTH_SHORT).show();//long 3.5 short 2
            shouldFinish = true;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (System.currentTimeMillis() - start >= 2000) {
                            shouldFinish = false;
                            break;
                        }
                    }
                }
            });

        } else {
            finish();
        }

    }
}
