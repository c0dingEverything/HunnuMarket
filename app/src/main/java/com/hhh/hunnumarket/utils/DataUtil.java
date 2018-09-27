package com.hhh.hunnumarket.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.RequestObject;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.consts.Api;

import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

public class DataUtil {
    private static List<Category> data;

    public class JsonResponseParse implements ResponseParser{

        @Override
        public void checkResponse(UriRequest request) throws Throwable {

        }

        @Override
        public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
            return new Gson().fromJson(result,new TypeToken<ResponseObject<List<Category>>>(){}.getType());
        }
    }

    public class response extends ResponseObject{

    }

    public static List<Category> getCategories(Context context) {
        if (data != null && data.size() > 0) {
            return data;
        }
        RequestParams params = new RequestParams(Api.GET_CATEGORIES);
        params.addBodyParameter("uid", SharedPreferenceUtil.getUid(context) + "");
        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken(context));
        try {
            ResponseObject result = x.http().getSync(params, ResponseObject.class);
            if (result.getStatus() == ResponseObject.STATUS_OK) {
                data = (List<Category>) result.getData();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return data;
        /*x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseObject<List<Category>> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<List<Category>>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    data = responseObject.getData();
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
        });*/

    }
}
