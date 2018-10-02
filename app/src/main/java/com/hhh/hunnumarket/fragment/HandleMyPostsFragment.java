package com.hhh.hunnumarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.activity.ItemDetailsActivity;
import com.hhh.hunnumarket.bean.Condition;
import com.hhh.hunnumarket.bean.Good;
import com.hhh.hunnumarket.bean.ItemsResponseObject;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;
import com.hhh.hunnumarket.utils.XhttpUtil;
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

import static android.app.Activity.RESULT_OK;


public class HandleMyPostsFragment extends Fragment implements View.OnClickListener {
    private int page = 0;
    private int size = 10;
    private String order = null;
    private String orderBy = null;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private final String TAG = "OfferFragment";
    private Button btn_selectAll;
    private Button btn_delete;
    private Condition condition;

    private RecyclerView mRecyclerView;
    private List<Good> mData;
    private MyAdapter myAdapter;
    private RefreshLayout mRefreshLayout;
    private Map<String, String[]> pics;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_handle_posts, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.fra_handle_recycler_view);
        btn_selectAll = view.findViewById(R.id.fra_handle_btn_select_all);
        btn_delete = view.findViewById(R.id.fra_handle_btn_delete);
        mRecyclerView = view.findViewById(R.id.fra_handle_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = view.findViewById(R.id.frag_handle_refreshLayout);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        myAdapter = new MyAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fra_handle_btn_delete:

                break;
            case R.id.fra_handle_btn_select_all:

                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        condition = new Condition();
        condition.setType(0);
        condition.setOrder(Condition.ORDER_DOWN);
        condition.setOrderBy("gid");
        condition.setSex_tendency(Condition.SEX_ALL);
        condition.setUid(SharedPreferenceUtil.getUid());
        if (mData == null) {
            mData = new ArrayList<>();
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
                if (mData != null) {
                    mData.clear();
                    pics.clear();
                }
                loadData(REFRESH);
            }
        });
    }


    private void loadData(final int tag) {
        if (tag == REFRESH) {
            page = 0;
            if (mData != null && mData.size() != 0) {
                mData.clear();
                pics.clear();
            }
        }
        RequestParams params = new RequestParams(Api.GET_GOODS);
        XhttpUtil.WrapParamsWithToken(params);
        params.setAsJsonContent(true);
        params.setConnectTimeout(5000);
        condition.setPage(page);
        params.setBodyContent(new Gson().toJson(condition));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ItemsResponseObject<Good> responseObject = new Gson().fromJson(result, new TypeToken<ItemsResponseObject<Good>>() {
                }.getType());
                Log.d(TAG, responseObject.toString());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    if (responseObject.getItems().size() != 0) {
                        mData.addAll(responseObject.getItems());
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
                        } else if (tag == REFRESH) {
                            mRefreshLayout.finishRefresh();
                            Toast.makeText(getContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
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
            private LinearLayout ll;

            MyHolder(View itemView) {
                super(itemView);
                iv_head = itemView.findViewById(R.id.offer_iv_head);
                tv_details = itemView.findViewById(R.id.offer_tv_details);
                tv_keyword = itemView.findViewById(R.id.offer_tv_keyword);
                tv_price = itemView.findViewById(R.id.offer_tv_price);
                tv_postTime = itemView.findViewById(R.id.offer_tv_post_time);
                tv_visited = itemView.findViewById(R.id.offer_tv_visited);
                ll = itemView.findViewById(R.id.offer_ll);
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
            final Good good = mData.get(position);
            holder.tv_details.setText(good.getDetails());
            holder.tv_keyword.setText(good.getKeyword());
            holder.tv_price.setText("¥" + good.getPrice());
            holder.tv_postTime.setText(good.getPost_time());
            holder.tv_visited.setText("浏览量:" + good.getVisited());

            final String[] urls = pics.get(good.getGid() + "");
            Log.d(TAG, Arrays.toString(urls) + " gid=" + good.getGid() + " pics=" + pics.size() + " mData=" + mData.size());
            if (pics != null && pics.size() != 0 && urls.length != 0) {
                Picasso.get().load(Api.ITEM_IMAGE + urls[0]).into(holder.iv_head);
            }
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                    intent.putExtra("good", good);
                    if (pics != null && pics.size() != 0 && urls.length != 0) {
                        intent.putExtra("pics", urls);
                    }
                    startActivityForResult(intent, 1);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int gid = data.getIntExtra("gid", -1);
                    for (Good good : mData) {
                        if (good.getGid() == gid) {
                            mData.remove(good);
                            myAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                break;
        }
    }
}
