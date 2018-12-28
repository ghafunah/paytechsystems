package com.paytech.paytechsystems.helper;
 
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
 
 
public class ApiClient {
    public static final String BASE_URL = "https://api.androidhive.info/json/";
    public static final String BASE_URL2 = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
     
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

     public static Retrofit getClient2() {
         if (retrofit2==null) {
             retrofit2 = new Retrofit.Builder()
                     .baseUrl(BASE_URL2)
                     .addConverterFactory(GsonConverterFactory.create())
                     .build();
         }
         return retrofit2;
     }
}