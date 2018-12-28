package com.paytech.paytechsystems.helper;
 
import java.util.List;
 
import com.paytech.paytechsystems.getset.Message;
import com.paytech.paytechsystems.getset.RetrofitResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
 
public interface ApiInterface {
 
    @GET("inbox.json")
    Call<List<Message>> getInbox();

    @GET("movie/top_rated")
    Call<RetrofitResponse> getTopRatedMovies(@Query("api_key") String apiKey);
 
    @GET("movie/{id}")
    Call<RetrofitResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
 
}