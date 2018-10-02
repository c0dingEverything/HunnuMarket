package com.hhh.hunnumarket.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.Good;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;
import com.hhh.hunnumarket.utils.XhttpUtil;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    private ImageView iv_head;
    private LinearLayout ll;
    private TextView tv_basic_info;
    private TextView tv_nickname;
    private TextView tv_post_time;
    private TextView tv_verified;
    private ImageButton ib_report;
    private ImageButton ib_delete;
    private TextView tv_visited;
    private boolean is_reported;
    private AsyncTask<Integer, Void, User> execute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_item_details);
        initUI();
        setData();
    }


    private void updateNumAddOne(final Good good, final String key, boolean isReportBtn) {
        if (good.getUid() != SharedPreferenceUtil.getUid()) {
            if (isReportBtn) {
                ib_report.setVisibility(View.VISIBLE);
                ib_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AsyncConnect(good, key);
                    }
                });

            } else {
                AsyncConnect(good, key);
            }
        }
    }

    private void AsyncConnect(Good good, final String key) {
        RequestParams params = new RequestParams(Api.UPDATE_GOOD);
        params.addBodyParameter("uid", SharedPreferenceUtil.getUid() + "");
        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken());
        params.addBodyParameter("flag", key);
        params.addBodyParameter("good", new Gson().toJson(good));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseObject<String> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<String>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK && "reports".equals(key)) {
                    if (is_reported) {
                        Toast.makeText(getApplicationContext(), "多大仇，以至于反复举报", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "举报成功", Toast.LENGTH_SHORT).show();
                    is_reported = true;
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

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        execute.cancel(true);
    }

    private class AsyncUserTask extends AsyncTask<Integer, Void, User> {

        @Override
        protected User doInBackground(Integer... integers) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(Api.GET_USER_INFO).newBuilder();
            urlBuilder.setQueryParameter("uid", SharedPreferenceUtil.getUid() + "");
            urlBuilder.setQueryParameter("query_uid", integers[0] + "");
            urlBuilder.setQueryParameter("access_token", SharedPreferenceUtil.getAccessToken());
            Request request = requestBuilder.url(urlBuilder.build()).build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                ResponseObject<User> responseObject = new Gson().fromJson(response.body().string(), new TypeToken<ResponseObject<User>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    return responseObject.getData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user == null) {
                Toast.makeText(getApplicationContext(), "user is null", Toast.LENGTH_SHORT).show();
                user = new User();
                user.setState(0);
                user.setNickname("获取用户信息失败");
            }
            tv_nickname.setText(user.getNickname());
            tv_verified.setText(getUserStatus(user.getState()));
            Picasso.get().load(Api.getPicFullUrl(user.getImage_url())).into(iv_head);
        }
    }

    private String getUserStatus(int state) {
        if (state == User.STATE_AUTHORIZED) {
            return getString(R.string.authorized);
        } else if (state == User.STATE_UNAUTHORIZED) {
            return getString(R.string.unauthorized);
        } else if (state == User.STATE_BANNED) {
            return getString(R.string.banned);
        }
        return null;
    }

    private void setData() {
        Intent intent = getIntent();
        String[] pics = intent.getStringArrayExtra("pics");
        Good good = (Good) intent.getSerializableExtra("good");
        updateNumAddOne(good, "reports", true);
        updateNumAddOne(good, "visited", false);
        setDeleteBtn(good);
        execute = new AsyncUserTask().execute(good.getUid());
        if (pics != null) {
            for (String pic : pics) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 10, 5, 10);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Picasso.get().load(Api.getPicFullUrl(pic)).into(imageView);
                ll.addView(imageView);
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        tv_basic_info.setText(getString(R.string.basic_info, good.getKeyword(), decimalFormat.format(good.getPrice()), good.getUser_location(), good.getDetails(), good.getUser_contact()));

        tv_visited.setText(String.valueOf(good.getVisited()));
        tv_post_time.setText(good.getPost_time());
    }

    private void setDeleteBtn(final Good good) {
        if (good.getUid() == SharedPreferenceUtil.getUid()) {
            ib_delete.setVisibility(View.VISIBLE);
            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestParams params = new RequestParams(Api.UPDATE_GOOD);
                    XhttpUtil.WrapParamsWithToken(params);
                    params.addBodyParameter("flag", "delete");
                    params.addBodyParameter("good", new Gson().toJson(good));
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseObject<String> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<String>>() {
                            }.getType());
                            if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                                Toast.makeText(getApplicationContext(), responseObject.getMsg(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("gid", good.getGid());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.operation_fail, Toast.LENGTH_SHORT).show();
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

                        }
                    });
                }
            });
        }
    }


    private void initUI() {
        iv_head = findViewById(R.id.act_details_iv_head);
        ll = findViewById(R.id.act_details_ll);
        tv_basic_info = findViewById(R.id.act_details_tv_basic_info);
        tv_nickname = findViewById(R.id.act_details_tv_nickname);
        tv_post_time = findViewById(R.id.act_details_tv_post_time);
        tv_verified = findViewById(R.id.act_details_tv_verified);
        tv_visited = findViewById(R.id.act_details_tv_visited);
        ib_report = findViewById(R.id.act_details_ib_report);
        ib_delete = findViewById(R.id.act_details_ib_delete);
    }
}
