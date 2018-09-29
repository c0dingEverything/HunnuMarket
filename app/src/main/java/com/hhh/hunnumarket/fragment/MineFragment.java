package com.hhh.hunnumarket.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.activity.EditUserInfoActivity;
import com.hhh.hunnumarket.activity.LoginActivity;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.DataUtil;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;

import org.xutils.http.RequestParams;


public class MineFragment extends Fragment implements View.OnClickListener {

    private TextView tv_settings;
    private TextView tv_about;
    private TextView tv_nickname;
    private TextView tv_post;
    private TextView tv_need;
    private ImageView iv_head;
    private Button btn_edit;
    private RelativeLayout bar_post;
    private Button btn_logout;

    public MineFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {

    }


    private void initUI(View view) {
        tv_settings = view.findViewById(R.id.mine_tv_settings);
        tv_about = view.findViewById(R.id.mine_tv_about);
        tv_nickname = view.findViewById(R.id.mine_tv_nickname);
        tv_need = view.findViewById(R.id.mine_tv_need);
        iv_head = view.findViewById(R.id.mine_iv_head);
        tv_post = view.findViewById(R.id.mine_tv_post);
        btn_edit = view.findViewById(R.id.mine_btn_edit);
        bar_post = view.findViewById(R.id.mime_rl_my_post);
        btn_logout = view.findViewById(R.id.mine_btn_logout);
        setOnClickListeners();
        tv_nickname.setText("我真蠢");
        tv_need.setText("求购数:" + "100");
        tv_post.setText("发布数:" + "100");
        iv_head.setImageResource(R.drawable.ic_launcher_background);
    }

    private void setOnClickListeners() {
        bar_post.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        tv_settings.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mime_rl_my_post:
                Toast.makeText(getActivity(), "记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_tv_about:
                Toast.makeText(getActivity(), "关于", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_tv_settings:
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mine_btn_edit:
                startActivityForResult(new Intent(getActivity(), EditUserInfoActivity.class), 1);
                break;
            case R.id.mine_btn_logout:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                SharedPreferenceUtil.updateAccessToken();
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {

        }
    }
}
