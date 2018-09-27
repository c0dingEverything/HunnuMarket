package com.hhh.hunnumarket.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.hhh.hunnumarket.activity.PostItemActivity;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.Condition;
import com.hhh.hunnumarket.bean.Good;
import com.hhh.hunnumarket.bean.ItemsResponseObject;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.UserToken;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.utils.DataUtil;
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


public class ScanFragment extends Fragment implements View.OnClickListener {
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
    private FloatingActionButton flb_post;
    private TextView tv_order;
    private TextView tv_sex;
    private TextView tv_type;
    private TextView tv_category;
    private List<Category> categoryList;
    private String[] categories;
    private int category_checked;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.fra_scan_recycler_view);
        flb_post = view.findViewById(R.id.fra_scan_fab_post);
        et_search = view.findViewById(R.id.fra_scan_et_search);
        btn_search = view.findViewById(R.id.fra_scan_btn_search);
        tv_order = view.findViewById(R.id.fra_scan_tv_order);
        tv_sex = view.findViewById(R.id.fra_scan_tv_sex);
        tv_type = view.findViewById(R.id.fra_scan_tv_type);
        tv_category = view.findViewById(R.id.fra_scan_tv_category);
        tv_category.setOnClickListener(this);
        tv_order.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        flb_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostItemActivity.class));
            }
        });
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        myAdapter = new MyAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fra_scan_tv_category:
                if (categoryList == null) {
                    Toast.makeText(getActivity(), R.string.data_not_loaded_completely, Toast.LENGTH_SHORT).show();
                    categoryList = DataUtil.getCategories(getContext());
                } else {
                    int i = 0;
                    for (Category category : categoryList) {
                        categories[i++] = category.getCname();
                    }
                    new AlertDialog.Builder(getActivity()).setTitle(R.string.category).setSingleChoiceItems(categories, category_checked, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            category_checked = which;
                            tv_category.setText(categories[which]);
                            dialog.dismiss();
                        }
                    }).show();
                }
                break;
            case R.id.fra_scan_tv_order:

                break;
            case R.id.fra_scan_tv_sex:

                break;
            case R.id.fra_scan_tv_type:

                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryList = DataUtil.getCategories(getContext());

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
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(getContext(), R.string.msg_error_input_null, Toast.LENGTH_SHORT).show();
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

    private Condition getRequestCondition() {
        Condition condition = new Condition();
        String keyword = et_search.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            condition.setKeyword(new String[]{keyword});
        } else {
            condition.setKeyword(new String[]{});
        }
        condition.setType(Good.TYPE_SELL);
        condition.setPage(page);
        condition.setSize(size);
        condition.setOrder(order == null ? "" : order);
        condition.setOrderBy(orderBy == null ? "" : orderBy);
        return condition;
    }

    private void loadData(final int tag) {
        UserToken userToken = SharedPreferenceUtil.getUserToken(getContext());
        RequestParams params = new RequestParams(Api.GET_GOODS);
        params.addBodyParameter("uid", userToken.getUid() + "");
        params.addBodyParameter("access_token", userToken.getAccess_token());
        params.setAsJsonContent(true);
        params.setConnectTimeout(5000);
        params.setBodyContent(new Gson().toJson(getRequestCondition()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ItemsResponseObject<Good> responseObject = new Gson().fromJson(result, new TypeToken<ItemsResponseObject<Good>>() {
                }.getType());
                Log.d(TAG, responseObject.toString());
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
