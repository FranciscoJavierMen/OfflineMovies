package com.example.offlinemovies.data;

import androidx.room.Room;

import com.example.offlinemovies.app.MyApp;
import com.example.offlinemovies.data.local.MovieRoomDatabase;
import com.example.offlinemovies.data.local.dao.MovieDao;
import com.example.offlinemovies.data.remote.ApiConstants;
import com.example.offlinemovies.data.remote.MovieAPIService;
import com.example.offlinemovies.data.remote.RequestInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    private MovieAPIService service;
    private final MovieDao movieDao;

    public MovieRepository() {

        //Local > Room
        MovieRoomDatabase database = Room.databaseBuilder(
                MyApp.getContext(),
                MovieRoomDatabase.class,
                "db_movies"
        ).build();

        movieDao = database.getMovieDao();

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
