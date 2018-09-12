package com.hhh.hunnumarket.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.Condition;
import com.hhh.hunnumarket.bean.Good;
import com.hhh.hunnumarket.bean.ItemsResponseObject;
import com.hhh.hunnumarket.bean.RequestObject;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.UserToken;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OfferFragment extends Fragment {
    private int page = 0;
    private int size = 5;
    private String order = null;
    private String orderBy = null;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private final String TAG = "OfferFragment";

    private RecyclerView mRecyclerView;
    private List<Good> data;
    private MyAdapter myAdapter;
    private RefreshLayout mRefreshLayout;
    private Map<String, String[]> pics;
    private EditText et_search;
    private Button btn_search;


    public OfferFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.offer_recycler_view);
        et_search = view.findViewById(R.id.offer_et_search);
        btn_search = view.findViewById(R.id.offer_btn_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        myAdapter = new MyAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (data == null) {
            data = new ArrayList<>();
            pics = new HashMap<>();
            mRecyclerView.setAdapter(myAdapter);
            loadData(REFRESH);
        }
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(LOAD_MORE);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                if (data != null) {
                    data.clear();
                    pics.clear();
                }
                loadData(REFRESH);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_search.getText().toString();
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(getContext(),R.string.msg_error_input_null,Toast.LENGTH_SHORT).show();
                    return;
                }
                page = 0;
                if (data != null) {
                    data.clear();
                    pics.clear();
                }
                loadData(REFRESH);
            }
        });
    }

    private RequestObject getRequestObject() {
        RequestObject<Condition> requestObject = new RequestObject<>();
        UserToken userToken = SharedPreferenceUtil.getUserToken(getContext());
        Condition condition = new Condition();
        String keyword = et_search.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            condition.setKeyword(new String[]{keyword});
        }else {
            condition.setKeyword(new String[]{});
        }
        condition.setPage(page);
        condition.setSize(size);
        condition.setOrder(order == null ? "" : order);
        condition.setOrderBy(orderBy == null ? "" : orderBy);
        requestObject.setAccess_token(userToken.getAccess_token());
        requestObject.setUid(userToken.getUid());
        requestObject.setData(condition);
        return requestObject;
    }

    private void loadData(final int tag) {
        final RequestParams params = new RequestParams(Api.GET_GOODS);
        params.setAsJsonContent(true);
        params.setConnectTimeout(5000);
        params.setBodyContent(new Gson().toJson(getRequestObject()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ItemsResponseObject<Good> responseObject = new Gson().fromJson(result, new TypeToken<ItemsResponseObject<Good>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    if (responseObject.getItems().size() != 0) {
                        data.addAll(responseObject.getItems());
                        pics.putAll(responseObject.getPics());
                        myAdapter.notifyDataSetChanged();
                        if (tag == LOAD_MORE) {
                            mRefreshLayout.finishLoadMore();
                        } else {
                            mRefreshLayout.finishRefresh();
                        }
                        page += size;
                    } else {
                        if (tag == LOAD_MORE) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else if(tag==REFRESH){
                            mRefreshLayout.finishRefresh();
                            Toast.makeText(getContext(), R.string.no_result,Toast.LENGTH_SHORT).show();
                        }
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

            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private class MyHolder extends RecyclerView.ViewHolder {
            private ImageView iv_head;
            private TextView tv_details;
            private TextView tv_keyword;
            private TextView tv_price;
            private TextView tv_visited;
            private TextView tv_postTime;

            MyHolder(View itemView) {
                super(itemView);
                iv_head = itemView.findViewById(R.id.offer_iv_head);
                tv_details = itemView.findViewById(R.id.offer_tv_details);
                tv_keyword = itemView.findViewById(R.id.offer_tv_keyword);
                tv_price = itemView.findViewById(R.id.offer_tv_price);
                tv_postTime = itemView.findViewById(R.id.offer_tv_post_time);
                tv_visited = itemView.findViewById(R.id.offer_tv_visited);
            }
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_rec_view_offer, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            Good good = data.get(position);
            holder.tv_details.setText(good.getDetails());
            holder.tv_keyword.setText(good.getKeyword());
            holder.tv_price.setText("¥" + good.getPrice());
            holder.tv_postTime.setText(good.getPost_time());
            holder.tv_visited.setText("浏览量:" + good.getVisited());
            String[] urls = pics.get(good.getGid() + "");
            Log.d(TAG, Arrays.toString(urls) + " gid=" + good.getGid() + " pics=" + pics.size() + " data=" + data.size());
            if (pics != null && pics.size() != 0 && urls.length != 0) {
                Picasso.get().load(Api.ITEM_IMAGE + urls[0]).into(holder.iv_head);
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}