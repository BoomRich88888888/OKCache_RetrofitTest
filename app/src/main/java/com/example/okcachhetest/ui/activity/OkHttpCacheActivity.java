package com.example.okcachhetest.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.okcachhetest.R;
import com.example.okcachhetest.di.data.Bean;
import com.example.okcachhetest.ui.adapter.MyRecyclerAdaper;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCacheActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Bean.ResultBean.DataBean> mList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String datas = (String) msg.obj;
                    Gson gson = new Gson();
                    Bean bean = gson.fromJson(datas, Bean.class);
                    List<Bean.ResultBean.DataBean> data = bean.getResult().getData();
                    mList.addAll(data);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private MyRecyclerAdaper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_cache);
        initView();
        initData();
    }

    private void initData() {
        long max = 20 * 1024 * 1024;
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/OkHttppppppppppppppCache/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        final Cache cache = new Cache(file, max);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();//获取请求体
                Response proceed = chain.proceed(request);//重新发起请求，获取Response
                //移除Response响应体中的关于缓存的头信息，并重新设置缓存机制
                Response.Builder pragma = proceed.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control").addHeader("Cache-Control", "max-age=" + 1000 * 200);
                return pragma.build();
            }
        }).build();

        CacheControl.Builder builder = new CacheControl.Builder().maxAge(100, TimeUnit.HOURS);
        Request build = new Request.Builder().url("http://v.juhe.cn/toutiao/index?type=top&key=097060266650f67b2cebd2a06aded587").cacheControl(builder.build()).build();
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                Message message = handler.obtainMessage(1, content);
                handler.sendMessage(message);
            }
        });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //RecyclerViewAdapter横向布局
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerAdaper(OkHttpCacheActivity.this, mList);
        mRecyclerView.setAdapter(adapter);
    }
}
