package com.hhh.hunnumarket.datahelper;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class ImagePicker {
    private Context mContext;
    private Intent mData;

    public ImagePicker(Context context, Intent data) {
        mContext = context;
        mData = data;
    }


    public Bitmap getBitmap() {
        String path = resolvePath();
        if (path != null) {
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String resolvePath() {
        String path = null;
        Uri uri = mData.getData();
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
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
            return path;
        }
        return null;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}