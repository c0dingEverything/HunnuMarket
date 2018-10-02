package com.hhh.hunnumarket.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.dbhelper.CategoryDao;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class DataUtil {
    private static List<Category> categoryList;
    private static User mUser;
    private static Application mApp;


    public static File compressImageToFile(String pic, boolean useDeviceSize, int width, int height) {
        if (useDeviceSize) {
            DisplayMetrics metrics = mApp.getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = mApp.getExternalCacheDir().getAbsolutePath();
        } else {
            path = mApp.getCacheDir().getAbsolutePath();
        }
        String desPath = path + File.separator + new File(pic).getName();
        File file = new File(desPath);
        if (file.exists()) {
            return file;
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pic, options);
            options.inJustDecodeBounds = false;
            int w = options.outWidth / width;
            int h = options.outHeight / height;
            int scale;
            if (w >= h) {
                scale = w;
            } else {
                scale = h;
            }
            if (scale < 0) {
                scale = 1;
            }
            options.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(pic, options);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void init(Application app) {
        mApp = app;
    }


    public static void updateUser() {
        mUser = null;
    }

    public static User getUerInfo() {
        if (mUser == null) {
            mUser = SharedPreferenceUtil.getUser();
        }
        return mUser;
    }

    public static void updateUserInfoFromServer() {
        RequestParams params = new RequestParams(Api.GET_USER_INFO);
        params.addBodyParameter("uid", SharedPreferenceUtil.getUid() + "");
        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseObject<User> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<User>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    mUser = responseObject.getData();
                    SharedPreferenceUtil.updateUserInfo(mUser);
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

    public static List<Category> getCategories() {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList;
        } else {
            CategoryDao categoryDao = new CategoryDao(mApp.getApplicationContext());
            categoryList = categoryDao.query();
            return categoryList;
        }
    }

    private static void saveCategoriesToDatabase() {
        if (categoryList != null && categoryList.size() > 0) {
            CategoryDao categoryDao = new CategoryDao(mApp.getApplicationContext());
            categoryDao.insert(categoryList);
        }
    }

    public static void updateCategoriesFromServer() {
        RequestParams params = new RequestParams(Api.GET_CATEGORIES);
//        params.addBodyParameter("uid", SharedPreferenceUtil.getUid(context) + "");
//        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken(context));
        try {
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResponseObject<List<Category>> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<List<Category>>>() {
                    }.getType());
                    if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                        categoryList = responseObject.getData();
                        saveCategoriesToDatabase();
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


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
