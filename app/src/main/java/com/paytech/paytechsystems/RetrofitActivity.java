package com.paytech.paytechsystems;
 
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
 
import java.util.List;
 
import com.paytech.paytechsystems.R;
import com.paytech.paytechsystems.adapter.RetrofitAdapter;
import com.paytech.paytechsystems.getset.Retrofit;
import com.paytech.paytechsystems.getset.RetrofitResponse;
import com.paytech.paytechsystems.helper.ApiClient;
import com.paytech.paytechsystems.helper.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
 
public class RetrofitActivity extends AppCompatActivity {
 
    private static final String TAG = RetrofitActivity.class.getSimpleName();
 
 
    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "0f6d72c658c0ed471f3f85933fe90cb1";
 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
 
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }
 
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
 
        ApiInterface apiService =  ApiClient.getClient2().create(ApiInterface.class);
 
        Call<RetrofitResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {
                int statusCode = response.code();
                List<Retrofit> movies = response.body().getResults();
                recyclerView.setAdapter(new RetrofitAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }
 
            @Override
            public void onFailure(Call<RetrofitResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}