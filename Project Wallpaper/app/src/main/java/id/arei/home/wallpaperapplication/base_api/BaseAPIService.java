package id.arei.home.wallpaperapplication.base_api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BaseAPIService {
    @Headers("Content-Type: application/json")
    @GET("posts")
    Call<JsonObject> getImage(@Query("key") String keyAPI, @Query("pagetoken") String pageToken);
}