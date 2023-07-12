package in.mplay.app.http;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("index.php/{version}")
    Call<String> getVersion(@Query("version") String version);

    @GET("index.php/{movies}")
    Call<String> getMovies(@Query("movies") String movie);

    @GET("index.php/{category}")
    Call<String> getCategory(@Query("category") String category);

    @GET("index.php/{channel}")
    Call<String> getChannel(@Query("channel") String channel);

    @GET("index.php/{admob}")
    Call<String> getAdmob(@Query("admob") String admob);

    @GET("index.php/{online}/{token}/{deviceId}")
    Call<String> getOnline(@Query("online") String online,@Query("token") String token,@Query("deviceId") String deviceId);
}
