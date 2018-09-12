package com.hhh.hunnumarket.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhh.hunnumarket.R;

public class BarItemView extends RelativeLayout {


    private View view;

    public BarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.bar_item_layout, this);
    }

    public void setText(String content) {
        ((TextView) view.findViewById(R.id.tv_left)).setText(content);
    }

    public void setOnBarItemClickListener(OnClickListener listener){
        view.findViewById(R.id.bar_item).setOnClickListener(listener);
    }


}
