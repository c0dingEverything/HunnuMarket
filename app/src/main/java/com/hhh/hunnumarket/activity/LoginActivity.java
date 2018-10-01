package com.hhh.hunnumarket.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.bean.UserToken;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final int REGISTER = 10;
    public final String TAG = this.getClass().getName();
    @ViewInject(value = R.id.login_et_sid)
    private EditText et_sid;

    @ViewInject(value = R.id.login_et_pwd)
    private EditText et_pwd;
    @ViewInject(value = R.id.btn_login)
    private Button btn_login;
    @ViewInject(value = R.id.login_cb_rememberPwd)
    private CheckBox cb_rememberPwd;
    @ViewInject(value = R.id.login_tv_register)
    private TextView tv_register;
    @ViewInject(value = R.id.login_pb)
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        checkPermissions();

        setDrawableSize();

        cb_rememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceUtil.setCheckBoxStatus(null, isChecked);
            }
        });
        checkIfShouldReadInfo();

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), REGISTER);
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请授予相关权限", Toast.LENGTH_SHORT).show();
                    checkPermissions();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            User user = (User) data.getSerializableExtra("user");
            et_sid.setText(user.getSid());
            et_pwd.setText(user.getPassword());
        }
    }

    private void checkIfShouldReadInfo() {
        Map<String, ?> userInfo = SharedPreferenceUtil.getUserInfo();
        Boolean checked = (Boolean) userInfo.get("checked");
        if (checked != null) {
            if (checked) {
                cb_rememberPwd.setChecked(true);
                et_sid.setText((String) userInfo.get("sid"));
                et_pwd.setText((String) userInfo.get("pwd"));
            }
        }
    }

    @Event(value = R.id.btn_login)
    private void login(View v) {
        final String sid = et_sid.getText().toString().trim();
        final String pwd = et_pwd.getText().toString().trim();

        if (checkInput(sid, pwd)) {
            try {
                JSONObject login_json = new JSONObject();
                login_json.put("sid", sid);
                login_json.put("password", pwd);
                RequestParams params = new RequestParams(Api.LOGIN);
                params.setConnectTimeout(3000);
                params.setAsJsonContent(true);
                params.setBodyContent(login_json.toString());
                pb.setVisibility(View.VISIBLE);
                btn_login.setClickable(false);
                btn_login.setFocusable(false);
                x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if (result != null) {
                                Gson gson = new Gson();
                                ResponseObject<UserToken> responseObject = gson.fromJson(result.toString(), new TypeToken<ResponseObject<UserToken>>() {
                                }.getType());
                                Toast.makeText(getApplicationContext(), responseObject.getMsg(), Toast.LENGTH_SHORT).show();
                                if (responseObject.getStatus() == 1) {
                                    UserToken userToken = responseObject.getData();
                                    SharedPreferenceUtil.saveUserToken( userToken);
                                    SharedPreferenceUtil.saveUserInfo( cb_rememberPwd.isChecked(), sid, pwd);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        btn_login.setClickable(true);
                        btn_login.setFocusable(true);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean checkInput(String account, String pwd) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (account.length() != 12) {
            Toast.makeText(this, R.string.msg_error_sid_lenght_lt, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pwd.length() < 4) {
            Toast.makeText(this, R.string.msg_error_pwd_lt, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setDrawableSize() {
        Drawable drawable_login = getResources().getDrawable(R.drawable.app_login_account);
        Drawable drawable_pwd = getResources().getDrawable(R.drawable.app_login_password);
        drawable_login.setBounds(0, 0, 50, 50);
        drawable_pwd.setBounds(0, 0, 50, 50);
        et_sid.setCompoundDrawables(drawable_login, null, null, null);
        et_pwd.setCompoundDrawables(drawable_pwd, null, null, null);
    }
}
