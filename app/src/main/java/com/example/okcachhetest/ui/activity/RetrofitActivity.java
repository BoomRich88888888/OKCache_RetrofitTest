package com.example.okcachhetest.ui.activity;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.okcachhetest.R;
import com.example.okcachhetest.di.api.ApiService;
import com.example.okcachhetest.di.data.Bean;
import com.example.okcachhetest.ui.adapter.MyRecyclerAdaper;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class RetrofitActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Bean.ResultBean.DataBean> mList = new ArrayList<>();
    private MyRecyclerAdaper adaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        initView();
        initData();
    }

    private void initData() {
        long max = 20 * 1024 * 1024;
        String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Retrofit+RX+Cache/";
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

        final String baseurl = "http://v.juhe.cn/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Bean> data = apiService.getData();
        data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                }).subscribe(new Observer<Bean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bean bean) {
                List<Bean.ResultBean.DataBean> data1 = bean.getResult().getData();
                mList.addAll(data1);
                adaper.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adaper = new MyRecyclerAdaper(RetrofitActivity.this, mList);
        mRecyclerView.setAdapter(adaper);
    }
}
