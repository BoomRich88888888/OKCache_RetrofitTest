package com.example.okcachhetest.di.api;

import com.example.okcachhetest.di.data.Bean;

import retrofit2.http.GET;
import rx.Observable;

public interface ApiService {
    @GET("toutiao/index?type=top&key=097060-266650f67b2cebd2a06aded587")
    Observable<Bean> getData();
}
