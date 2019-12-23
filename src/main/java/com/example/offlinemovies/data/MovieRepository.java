package com.example.offlinemovies.data;

import com.example.offlinemovies.data.remote.ApiConstants;
import com.example.offlinemovies.data.remote.MovieAPIService;
import com.example.offlinemovies.data.remote.RequestInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private MovieAPIService service;

    public MovieRepository() {
        //RequestInterceptor para incluir en la cabecera de la petición
        //el api_key
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.addInterceptor(new RequestInterceptor());
        OkHttpClient client = okhttpClientBuilder.build();

        //Remote > Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieAPIService.class);
    }
}
