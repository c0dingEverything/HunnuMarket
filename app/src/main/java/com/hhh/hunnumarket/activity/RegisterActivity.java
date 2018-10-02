package com.hhh.hunnumarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_register;
    private TextView tv_location;
    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_qq;
    private EditText et_sid;
    private ProgressBar pb;
    private CheckBox cb;
    private TextView tv_agreement;
    private int checkedItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
    }

    private void initUI() {
        btn_register = findViewById(R.id.register_btn);
        tv_location = findViewById(R.id.register_tv_location);
        et_phone = findViewById(R.id.register_et_phone);
        et_qq = findViewById(R.id.register_et_qq);
        et_phone = findViewById(R.id.register_et_phone);
        et_pwd = findViewById(R.id.register_et_pwd);
        et_sid = findViewById(R.id.register_et_sid);
        pb = findViewById(R.id.register_pb);
        cb = findViewById(R.id.register_cb);
        tv_agreement = findViewById(R.id.register_tv_agreement);
        btn_register.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);
    }


    private void prepareData() {
        String sid = et_sid.getText().toString().trim();
        String password = et_pwd.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String location = tv_location.getText().toString().trim();
        String qq = et_qq.getText().toString().trim();

        if (!verifyInput(sid, password, phone, location, qq)) {
            return;
        }
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        try {
            if (tm != null) {
                String imei;
                if (Build.VERSION.SDK_INT >= 26) {
                    imei = tm.getImei();
                } else {
                    imei = tm.getDeviceId();
                }
                User user = new User();
                user.setSid(sid);
                user.setPassword(password);
                user.setPhone_number(phone);
                user.setUser_location(location);
                user.setQq(qq);
                user.setImei(imei);
                RequestParams params = new RequestParams(Api.REGISTER);
                params.setAsJsonContent(true);
                params.setConnectTimeout(3000);
                params.setBodyContent(new Gson().toJson(user));
                register(params);
                Log.d("RegisterActivity", params.getBodyContent());
            } else {
                Toast.makeText(getApplicationContext(), R.string.permission_deny_imei, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.permission_deny_imei, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean verifyInput(String sid, String password, String phone, String location, String qq) {
        if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(location) || TextUtils.isEmpty(qq)) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sid.length() != 12) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_sid_lenght_lt, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 4) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_password_lenght, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() != 11) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_phone_lt, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (qq.length() < 5 || qq.length() > 10) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_qq_lt, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void register(RequestParams params) {

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    ResponseObject<User> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<User>>() {
                    }.getType());
                    if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                        Intent intent = getIntent();
                        Toast.makeText(getApplicationContext(), responseObject.getMsg(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("user", responseObject.getData());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pb.setVisibility(View.INVISIBLE);
                btn_register.setClickable(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                pb.setVisibility(View.INVISIBLE);
                btn_register.setClickable(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                pb.setVisibility(View.VISIBLE);
                btn_register.setClickable(false);
                prepareData();
                break;
            case R.id.register_tv_agreement:
                Toast.makeText(getApplicationContext(), "协议就是:用户信息完全保密,使用过程中被他人骗取钱财,开发者不负责赔偿!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_tv_location:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] items = getResources().getStringArray(R.array.locations);
                builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem = which;
                        tv_location.setText(items[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }
}
