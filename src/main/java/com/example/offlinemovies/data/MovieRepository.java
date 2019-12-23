package com.example.offlinemovies.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.offlinemovies.app.MyApp;
import com.example.offlinemovies.data.local.MovieRoomDatabase;
import com.example.offlinemovies.data.local.dao.MovieDao;
import com.example.offlinemovies.data.local.entity.MovieEntity;
import com.example.offlinemovies.data.network.NetworkBoundResource;
import com.example.offlinemovies.data.network.Resource;
import com.example.offlinemovies.data.remote.ApiConstants;
import com.example.offlinemovies.data.remote.MovieAPIService;
import com.example.offlinemovies.data.remote.RequestInterceptor;
import com.example.offlinemovies.data.remote.model.MoviesResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
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

    public LiveData<Resource<List<MovieEntity>>> getPopularMovies(){
        //Tipo que devuelve Room(Local) y tipo que devuelve el API(remoto) con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(){

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                //Retorna los datos de la base de dato local con Room
                return movieDao.loadMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                //Realiza peticion a API remota
                return service.loadPopularMovies();
            }
        }.getAsLiveData();
    }
}
