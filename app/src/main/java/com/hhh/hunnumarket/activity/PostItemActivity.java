package com.hhh.hunnumarket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhh.hunnumarket.R;

public class PostItemActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_contact;
    private EditText et_details;
    private EditText et_price;
    private TextView tv_location;
    private TextView tv_tendency;
    private String purpose = "sell";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        initUI();
    }

    private void initUI() {
        et_contact = findViewById(R.id.act_post_et_contact);
        et_details = findViewById(R.id.act_post_et_details);
        et_price = findViewById(R.id.act_post_et_price);
        tv_location = findViewById(R.id.act_post_tv_location);
        tv_tendency = findViewById(R.id.act_post_tv_tendency);
        findViewById(R.id.act_post_rg).setOnClickListener(this);
        findViewById(R.id.act_post_btn_confirm).setOnClickListener(this);
        findViewById(R.id.act_post_btn_cancel).setOnClickListener(this);
        findViewById(R.id.act_post_rg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_post_btn_cancel:
                break;
            case R.id.act_post_btn_confirm:
                break;
            case R.id.act_post_ib_pic:
                break;
            case R.id.act_post_rg:
                int checkedRadioButtonId = ((RadioGroup) v).getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.act_post_rb_demand) {
                    purpose = "demand";
                } else if (checkedRadioButtonId == R.id.act_post_rb_offer) {
                    purpose = "offer";
                }
                break;

        }
    }
}
