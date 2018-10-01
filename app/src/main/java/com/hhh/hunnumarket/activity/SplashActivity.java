package com.hhh.hunnumarket.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.VersionInfo;
import com.hhh.hunnumarket.consts.Api;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

@ContentView(value = R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private VersionInfo newVersion;
    private VersionInfo currentVersion;
    private long start;
    private long end;

    @ViewInject(value = R.id.pb_download)
    private ProgressBar pb;
    private boolean isActive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = System.currentTimeMillis();
        x.view().inject(this);

        getVersionInfo();
    }



    /**
     * 与本地进行版本核对
     * <p>
     * 检查是否要升级
     */


    private void shouldUpdate() {
        currentVersion = new VersionInfo();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            currentVersion.setVersionCode(packageInfo.versionCode);
            currentVersion.setVersionName(packageInfo.versionName);
            if (newVersion.getVersionCode() <= currentVersion.getVersionCode()) {
                goToNext();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.new_version_found)
                        .setMessage(newVersion.getUpdateInfo())
                        .setNegativeButton(R.string.cancel, new NegativeClickListener())
                        .setPositiveButton(R.string.confirm, new PositiveClickListener())
                        .setCancelable(false)
                        .show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            final String url = newVersion.getLink();
            RequestParams params = new RequestParams(url);
            params.setConnectTimeout(3000);
            params.setAutoRename(true);
            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    File dest = new File(result.getParentFile().getAbsolutePath() + File.separator + url.substring(url.lastIndexOf("/")));
                    result.renameTo(dest);
                    installApk(dest);
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
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    pb.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    pb.setMax((int) total);
                    pb.setProgress((int) current);
                }
            });
        }
    }

    private class NegativeClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            goToNext();
        }
    }

    private void installApk(File apk) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(this, "com.hhh.hunnumarket.FileProvider", apk),
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        if (isActive) {
                            goToNext();
                            break;
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    private void goToNext() {
        while (true) {
            end = System.currentTimeMillis();
            if (end - start > 2000) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            }
        }
    }


    /**
     * 获取新版本信息
     */
    private void getVersionInfo() {
        RequestParams params = new RequestParams(Api.getFileUrl("version.json"));
        params.setConnectTimeout(3000);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                newVersion = new Gson().fromJson(String.valueOf(result), VersionInfo.class);
                shouldUpdate();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, ex.getCause().toString());
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, cex.getCause().toString());
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
            }
        });
    }


}
