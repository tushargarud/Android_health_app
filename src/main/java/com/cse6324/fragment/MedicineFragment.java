package com.cse6324.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.DietListAdapter;
import com.cse6324.adapter.MedicineListAdapter;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.MedicineBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class MedicineFragment extends Fragment {

    private MedicineListAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else{
                if(adapter == null){
                    adapter = new MedicineListAdapter(MedicineFragment.this.getContext());
                    recyclerView.setAdapter(adapter);
                }

                List<MedicineBean> list = JSON.parseArray(response, MedicineBean.class);
                adapter.setList(list);
            }

            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicine, null);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        }, 1000);
                    }
                }
        );

        initData();
        return v;
    }

    public void initData(){
        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETMEDICINE,callback);
    }
}
