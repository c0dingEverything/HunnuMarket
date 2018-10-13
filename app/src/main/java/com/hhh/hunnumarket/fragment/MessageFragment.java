package com.hhh.hunnumarket.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.MsgAdmin;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.XhttpUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    private String Tag = "MessageFragment";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FragmentActivity mActivity;
    private MyAdapter mMyAdapter;
    private List<MsgAdmin> mData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mRecyclerView = view.findViewById(R.id.fra_recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.fra_msg_refresh);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mData = new ArrayList<>();
        prepareRefreshLayout();
        prepareRecyclerView();
        requestData();
    }


    private void prepareRefreshLayout() {
        Resources resources = getResources();
        mSwipeRefreshLayout.setColorSchemeColors(resources.getColor(android.R.color.holo_blue_bright),
                resources.getColor(android.R.color.holo_green_light),
                resources.getColor(android.R.color.holo_red_light),
                resources.getColor(android.R.color.holo_orange_light));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
    }

    private void requestData() {
        mData.clear();
        RequestParams params = new RequestParams(Api.GET_MSG);
        params.addBodyParameter("flag", "admin");
        XhttpUtil.WrapParamsWithToken(params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(Tag, result);
                ResponseObject<List<MsgAdmin>> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<ArrayList<MsgAdmin>>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    mData.addAll(responseObject.getData());
                    mMyAdapter.notifyDataSetChanged();
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
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
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void prepareRecyclerView() {
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private class MyHolder extends RecyclerView.ViewHolder {

            public TextView tv_body;
            public TextView tv_datetime;
            public TextView tv_title;

            MyHolder(View itemView) {
                super(itemView);
                tv_body = itemView.findViewById(R.id.msg_tv_body);
                tv_datetime = itemView.findViewById(R.id.msg_tv_datetime);
                tv_title = itemView.findViewById(R.id.msg_tv_title);
            }
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rec_view_msg, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            MsgAdmin msgAdmin = mData.get(position);
            holder.tv_title.setText(msgAdmin.getTitle());
            holder.tv_body.setText(msgAdmin.getBody());
            holder.tv_datetime.setText(msgAdmin.getPost_datetime());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
