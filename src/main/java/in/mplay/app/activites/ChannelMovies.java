package in.mplay.app.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.adapter.MoviesAdapter;
import in.mplay.app.utils.DatabaseHelper;


public class ChannelMovies extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TextView empty_view;
    private DatabaseHelper db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_two);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty_view = findViewById(R.id.empty_view);
        db = new DatabaseHelper(this);
        new Online(this);

        Bundle q = getIntent().getExtras();
        String channel = q.getString("channel_name");
        getSupportActionBar().setTitle(channel);
        function_movies(channel);
        super.onCreate(savedInstanceState);
    }

    private void function_movies(String channel) {
        if (db.channel_movies(channel).size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            MoviesAdapter movie = new MoviesAdapter(this, db.channel_movies(channel),mRecyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChannelMovies.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(movie);
            mRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}