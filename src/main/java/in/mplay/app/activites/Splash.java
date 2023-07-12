package in.mplay.app.activites;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

import in.mplay.app.R;
import in.mplay.app.http.ApiClient;
import in.mplay.app.http.ApiInterface;
import in.mplay.app.json.JsonMovies;
import in.mplay.app.utils.DatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private DatabaseHelper db;
    private ProgressBar progressBar;
    private static long backPressed = 0;
    private boolean add = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        db = new DatabaseHelper(Splash.this);
        progressBar = findViewById(R.id.progressBar);
        isOnline();
    }
    public void isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(Splash.this, "No Internet Access", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            updatecheck();
        }
    }

    public void updatecheck() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call < String > result = service.getVersion("true");
        result.enqueue(new Callback < String > () {
            @Override
            public void onResponse(@NonNull Call < String > call, @NonNull Response < String > response) {
                if(!response.body().equals("1.0")){
                    updateAlert();
                }else{
                    loadCategory();
                }
            }

            @Override
            public void onFailure(@NonNull Call < String > call, @NonNull Throwable t) {}

        });
    }

    public void updateAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setMessage("Update available!\n\nNew features are added\n");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://megaplay.in"));
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void loadCategory() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call < String > result = service.getCategory("true");
        result.enqueue(new Callback < String > () {
            @Override
            public void onResponse(@NonNull Call < String > call, @NonNull Response < String > response) {
                try {
                    JSONArray jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        if(jsonarray.getJSONObject(i).getString("status").equals("Hide")) {
                            db.deleteCat(jsonarray.getJSONObject(i).getString("id"));
                        }else{
                            add = db.setCategory(jsonarray.getJSONObject(i).getString("id"),jsonarray.getJSONObject(i).getString("category"));
                        }
                    }
                    if(add){
                        getMovies();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call < String > call, @NonNull Throwable t) {}

        });
    }

    private void getMovies() {
        if (db.getRows() > 0) {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call < String > result = service.getMovies("true");
        result.enqueue(new Callback < String > () {
            @Override
            public void onResponse(@NonNull Call < String > call, @NonNull Response < String > response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonarray = new JSONArray(response.body());
                    if (jsonarray.length() != db.getRows()) {
                        db.refreshDB();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject json = jsonarray.getJSONObject(i);
                            JsonMovies model = new JsonMovies(json.getString("id"),json.getString("title"),
                                    json.getString("description"), json.getString("quality"), json.getString("language"),
                                    json.getString("channel"),json.getString("category"), json.getString("img_link"), json.getString("d_link"), json.getString("date"));

                            if (db.addMovies(model)) {
                                startActivity(new Intent(Splash.this, MainActivity.class));
                                finish();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    }else{
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject json = jsonarray.getJSONObject(i);
                            JsonMovies model = new JsonMovies(json.getString("id"),json.getString("title"),
                                    json.getString("description"), json.getString("quality"), json.getString("language"),
                                    json.getString("channel"),json.getString("category"), json.getString("img_link"), json.getString("d_link"), json.getString("date"));
                            db.addMovies(model);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call < String > call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Splash.this, "!error fetch", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2500 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.tapAgain, Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}