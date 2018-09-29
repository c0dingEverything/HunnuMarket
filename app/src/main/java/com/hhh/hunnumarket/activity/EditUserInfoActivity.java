package com.hhh.hunnumarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.datahelper.ImagePicker;
import com.hhh.hunnumarket.utils.DataUtil;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_confirm;
    private TextView tv_head;
    private TextView tv_location;
    private EditText et_nickname;
    private EditText et_phone;
    private EditText et_qq;
    private int locationChecked;
    public String headPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initUI();
        initData();
        setClickListener();
    }

    private void initData() {
        User user = DataUtil.getUerInfo();
        tv_location.setText(user.getUser_location());
        et_phone.setText(user.getPhone_number());
        et_nickname.setText(user.getNickname());
        et_qq.setText(user.getQq());
        RequestParams params = new RequestParams(Api.ITEM_IMAGE + user.getImage_url());
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Drawable left = getResources().getDrawable(R.drawable.head);
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                Drawable right = Drawable.createFromPath(result.getAbsolutePath());
                right.setBounds(0, 0, right.getMinimumWidth() - 5, right.getMinimumHeight() - 5);
                tv_head.setCompoundDrawables(left, null, right, null);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initUI() {
        btn_confirm = findViewById(R.id.edit_btn_confirm);
        tv_head = findViewById(R.id.edit_tv_head);
        tv_location = findViewById(R.id.edit_tv_location);
        et_nickname = findViewById(R.id.edit_et_nickname);
        et_phone = findViewById(R.id.edit_et_phone);
        et_qq = findViewById(R.id.edit_et_qq);
    }

    private void setClickListener() {
        btn_confirm.setOnClickListener(this);
        tv_head.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        et_nickname.setOnClickListener(this);
        et_phone.setOnClickListener(this);
        et_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn_confirm:
                String location = tv_location.getText().toString();
                String nickname = et_nickname.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                String qq = et_qq.getText().toString().trim();
                if (TextUtils.isEmpty(location) || TextUtils.isEmpty(nickname) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(qq)) {
                    Toast.makeText(this, R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog dialog = new AlertDialog.Builder(this).setMessage(R.string.updating).setCancelable(false).show();
                final User user = new User();
                user.setQq(qq);
                user.setNickname(nickname);
                user.setPhone_number(phone);
                user.setUser_location(location);
                user.setUid(SharedPreferenceUtil.getUid());
                RequestParams params = new RequestParams(Api.MODIFY_USER_INFO);
                params.addBodyParameter("uid", SharedPreferenceUtil.getUid() + "");
                params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken());
                params.addBodyParameter("user", new Gson().toJson(user));
                if (!TextUtils.isEmpty(headPath)) {
                    List<KeyValue> list = new ArrayList<>();
                    list.add(new KeyValue("head", new File(headPath)));
                    params.setRequestBody(new MultipartBody(list, "UTF-8"));
                    params.setMultipart(true);
                }
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseObject<String> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<String>>() {
                        }.getType());
                        Toast.makeText(getApplicationContext(), responseObject.getMsg(), Toast.LENGTH_SHORT).show();

                        if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                            user.setImage_url(responseObject.getData());
                            SharedPreferenceUtil.updateUserInfo(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.edit_tv_head:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.edit_tv_location:
                final String[] locations = getResources().getStringArray(R.array.locations);
                new AlertDialog.Builder(this).setSingleChoiceItems(locations, locationChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationChecked = which;
                        tv_location.setText(locations[locationChecked]);
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ImagePicker imagePicker = new ImagePicker(this, data);
            headPath = imagePicker.resolvePath();
            if (headPath != null) {
                BitmapDrawable drawable = new BitmapDrawable();
                Drawable left = getResources().getDrawable(R.drawable.head);
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                Drawable right = Drawable.createFromPath(headPath);
                right.setBounds(0, 0, right.getMinimumWidth() - 5, right.getMinimumHeight() - 5);
                tv_head.setCompoundDrawables(left, null, right, null);
            }
        }
    }
}
