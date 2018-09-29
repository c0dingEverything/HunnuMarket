package com.hhh.hunnumarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    @ViewInject(value = R.id.register_btn)
    private Button btn_register;

    @ViewInject(value = R.id.register_tv_location)
    private TextView tv_location;

    @ViewInject(value = R.id.register_et_phone)
    private EditText et_phone;
    @ViewInject(value = R.id.register_et_pwd)
    private EditText et_pwd;
    @ViewInject(value = R.id.register_et_qq)
    private EditText et_qq;
    @ViewInject(value = R.id.register_et_sid)
    private EditText et_sid;
    @ViewInject(value = R.id.register_pb)
    private ProgressBar pb;
    @ViewInject(value = R.id.register_toolbar)
    private Toolbar toolbar;
    @ViewInject(value = R.id.register_cb)
    private CheckBox cb;
    @ViewInject(value = R.id.register_tv_agreement)
    private TextView tv_agreement;

    private int checkedItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        x.view().inject(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Event({R.id.register_btn, R.id.register_tv_agreement, R.id.register_tv_location})
    private void click(View v) {
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

    private void prepareData() {
        String sid = et_sid.getText().toString().trim();
        String password = et_pwd.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String location = tv_location.getText().toString().trim();
        String qq = et_qq.getText().toString().trim();

        verifyInput(sid, password, phone, location, qq);

        String imei = null;
        JSONObject jsonObject = null;
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        try {
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    imei = tm.getImei();
                } else {
                    imei = tm.getDeviceId();
                }
                jsonObject = new JSONObject();
//                jsonObject.put("intent", "register");
                jsonObject.put("sid", sid);
                jsonObject.put("password", password);
                jsonObject.put("phone_number", phone);
                jsonObject.put("user_location", location);
                jsonObject.put("qq", qq);
                jsonObject.put("imei", imei);
                RequestParams params = new RequestParams(Api.REGISTER);
                params.setAsJsonContent(true);
                params.setConnectTimeout(3000);
                params.setBodyContent(jsonObject.toString());
                register(params);
            } else {
                Toast.makeText(getApplicationContext(), R.string.permission_deny_imei, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.permission_deny_imei, Toast.LENGTH_SHORT).show();
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void verifyInput(String sid, String password, String phone, String location, String qq) {
        if (TextUtils.isEmpty(sid) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(location) || TextUtils.isEmpty(qq)) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (sid.length() != 12) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_sid_lenght_lt, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_password_lenght, Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_phone_lt, Toast.LENGTH_SHORT).show();
            return;
        }

        if (qq.length() < 5 || qq.length() > 10) {
            Toast.makeText(getApplicationContext(), R.string.msg_error_qq_lt, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void register(RequestParams params) {

        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d(TAG, result + "");
                try {

                    Toast.makeText(getApplicationContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (result.getInt("status") == 1) {
                        Intent intent = getIntent();
                        intent.putExtra("user", new Gson().fromJson(String.valueOf(result.getJSONObject("data")), User.class));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.d(TAG, ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG, cex.toString());
            }

            @Override
            public void onFinished() {
                pb.setVisibility(View.INVISIBLE);
                btn_register.setClickable(true);
            }
        });

    }
}
