package com.hhh.hunnumarket.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hhh.hunnumarket.bean.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    MyOpenHelper helper;

    public CategoryDao(Context context) {
        helper = new MyOpenHelper(context);
    }

    public void insert(List<Category> categoryList) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Category category : categoryList) {
            values.put("cid", category.getCid());
            values.put("cname", category.getCname());
            long insert = database.insert("category", null, values);
            System.out.println(insert);
        }
        helper.close();
    }

    public List<Category> query() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query("category", null, null, null, null, null, null);
        List<Category> categoryList = null;
        if (cursor.moveToFirst()) {
            categoryList = new ArrayList<>();
            do {
                int cid = cursor.getInt(0);
                String cname = cursor.getString(1);
                Category category = new Category();
                category.setCid(cid);
                category.setCname(cname);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        helper.close();
        return categoryList;
    }
}
