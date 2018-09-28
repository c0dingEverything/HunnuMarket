package com.hhh.hunnumarket.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.Good;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.DataUtil;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PostItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_contact;
    private EditText et_details;
    private EditText et_price;
    private EditText et_keyword;
    private TextView tv_location;
    private TextView tv_category;
    private TextView tv_tendency;
    private int purpose = Good.TYPE_SELL;
    private LinearLayout ll_container;
    private HashSet<String> pics = null;
    private int tendency_checked;
    private int location_checked;
    private int category_checked;
    private String[] categories;
    private List<Category> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        initUI();
        pics = new HashSet<>();
        getCategories();
    }

    private void getCategories() {
        data = DataUtil.getCategories(getApplicationContext());
        categories = new String[data.size()];
        int i = 0;
        for (Category category : data) {
            categories[i++] = category.getCname();
        }
    }

    private void initUI() {
        et_keyword = findViewById(R.id.act_post_et_keyword);
        et_contact = findViewById(R.id.act_post_et_contact);
        et_details = findViewById(R.id.act_post_et_details);
        et_price = findViewById(R.id.act_post_et_price);
        tv_location = findViewById(R.id.act_post_tv_location);
        tv_tendency = findViewById(R.id.act_post_tv_tendency);
        tv_category = findViewById(R.id.act_post_tv_category);
        ll_container = findViewById(R.id.act_post_image_container);
        tv_location.setOnClickListener(this);
        tv_tendency.setOnClickListener(this);
        tv_category.setOnClickListener(this);
        findViewById(R.id.act_post_rg).setOnClickListener(this);
        findViewById(R.id.act_post_btn_confirm).setOnClickListener(this);
        findViewById(R.id.act_post_btn_cancel).setOnClickListener(this);
        findViewById(R.id.act_post_ib_pic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_post_tv_category:
                if (categories == null) {
                    Toast.makeText(getApplicationContext(), R.string.data_not_loaded_completely, Toast.LENGTH_SHORT).show();
                    getCategories();
                } else {
                    new AlertDialog.Builder(this).setTitle(R.string.category).setSingleChoiceItems(categories, category_checked, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            category_checked = which;
                            tv_category.setText(categories[which]);
                            dialog.dismiss();
                        }
                    }).show();
                }
                break;
            case R.id.act_post_btn_cancel:
                finish();
                break;
            case R.id.act_post_btn_confirm:
                String keyword = et_keyword.getText().toString().trim();
                String contact = et_contact.getText().toString().trim();
                String details = et_details.getText().toString().trim();
                String price = et_price.getText().toString().trim();
                String sex_tendency = tv_tendency.getText().toString().trim();
                String location = tv_location.getText().toString().trim();
                String category = tv_category.getText().toString().trim();
                if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(details) || TextUtils.isEmpty(price) || TextUtils.isEmpty(sex_tendency) || TextUtils.isEmpty(location)) {
                    Toast.makeText(this, R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                int cid = 0;
                for (Category c : data) {
                    if (category.equals(c.getCname())) {
                        cid = c.getCid();
                        break;
                    }
                }

                int gender = 2;
                if ("女".equals(sex_tendency)) {
                    gender = 1;
                } else if ("男".equals(sex_tendency)) {
                    gender = 0;
                }
                Good good = new Good();
                good.setKeyword(keyword);
                good.setTendency(gender);
                good.setUser_contact(contact);
                good.setDetails(details);
                good.setCid(cid);
                good.setPrice(Float.parseFloat(price));
                good.setUser_location(location);
                good.setType(purpose);
                good.setUid(SharedPreferenceUtil.getUid(getApplicationContext()));
                RequestParams params = new RequestParams(Api.POST_ITEM);
                if (pics != null && pics.size() != 0) {
                    List<KeyValue> list = new ArrayList<>();
                    int i = 1;
                    for (String pic : pics) {
                        list.add(new KeyValue("pic_" + i++, new File(pic)));
                    }
                    MultipartBody body = new MultipartBody(list, "UTF-8");
                    params.setRequestBody(body);
                    params.setMultipart(true);
                }
                params.addBodyParameter("purpose", purpose + "");
                params.addBodyParameter("uid", SharedPreferenceUtil.getUid(getApplicationContext()) + "");
                params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken(getApplicationContext()));
                params.addBodyParameter("good", new Gson().toJson(good));
                final AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("上传中，请稍后!").setCancelable(false).show();
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.act_post_tv_location:
                final String[] locations = getResources().getStringArray(R.array.locations);
                new AlertDialog.Builder(this).setSingleChoiceItems(locations, location_checked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_location.setText(locations[which]);
                        location_checked = which;
                        dialog.dismiss();
                    }
                }).setTitle(R.string.location).show();
                break;
            case R.id.act_post_tv_tendency:
                final String[] sex = getResources().getStringArray(R.array.sex);
                new AlertDialog.Builder(this).setSingleChoiceItems(sex, tendency_checked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_tendency.setText(sex[which]);
                        tendency_checked = which;
                        dialog.dismiss();
                    }
                }).setTitle(R.string.sex_tendency).show();
                break;
            case R.id.act_post_ib_pic:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.act_post_rg:
                int checkedRadioButtonId = ((RadioGroup) v).getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.act_post_rb_demand) {
                    purpose = Good.TYPE_DEMAND;
                } else if (checkedRadioButtonId == R.id.act_post_rb_offer) {
                    purpose = Good.TYPE_SELL;
                }
                break;
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String path = null;
                Uri uri = data.getData();
                if (DocumentsContract.isDocumentUri(this, uri)) {
                    String documentId = DocumentsContract.getDocumentId(uri);
                    if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String id = documentId.split(":")[1];
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        path = getImagePath(uri, null);
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        path = uri.getPath();
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    ImageView imageView = new ImageView(getApplicationContext());
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    if (layoutParams == null) {
                        layoutParams = new LinearLayout.LayoutParams(150, 150);
                    } else {
                        layoutParams.height = 150;
                        layoutParams.width = 150;
                    }
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setImageBitmap(bitmap);
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ll_container.removeView(v);
                            pics.remove(v.getTag());
                            return true;
                        }
                    });
                    if (!pics.contains(path)) {
                        ll_container.addView(imageView);
                        imageView.setTag(path);
                        pics.add(path);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.pic_alread_selected, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
