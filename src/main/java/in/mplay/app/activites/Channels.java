package in.mplay.app.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.adapter.ChannelAdapter;
import in.mplay.app.http.ApiClient;
import in.mplay.app.http.ApiInterface;
import in.mplay.app.json.JsonChannels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Channels extends AppCompatActivity {
    private List<JsonChannels> jsonChannels = new ArrayList< >();
    private RecyclerView mRecyclerView;
    private TextView empty_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        new Online(this);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty_view = findViewById(R.id.empty_view);
        getChannel();
    }

    private void getChannel() {
        jsonChannels.clear();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call< String > result = service.getChannel("true");
        result.enqueue(new Callback< String >() {
            @Override
            public void onResponse(@NonNull Call <String> call, @NonNull Response< String > response) {
                if (response.body().equals("null")) {
                    empty_view.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    try {
                        JSONArray jsonarray = new JSONArray(response.body());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonChannels.add(new JsonChannels(jsonarray.getJSONObject(i).getString("channel_name"),jsonarray.getJSONObject(i).getString("channel_logo")));
                        }
                        mRecyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        ChannelAdapter movie = new ChannelAdapter(Channels.this,jsonChannels);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Channels.this);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(movie);
                        mRecyclerView.setNestedScrollingEnabled(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call < String > call, @NonNull Throwable t) {
                Toast.makeText(Channels.this,"Server Down!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}