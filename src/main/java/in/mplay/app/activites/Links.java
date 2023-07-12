package in.mplay.app.activites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.adapter.GetLinks;
import in.mplay.app.utils.DatabaseHelper;


@SuppressLint("Registered")
public class Links extends AppCompatActivity {
    private List <String> list = new ArrayList < > ();
    private RecyclerView mRecyclerView;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        new Online(this);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty_view = findViewById(R.id.empty_view);
        DatabaseHelper db = new DatabaseHelper(this);

        Bundle q = getIntent().getExtras();
        String title = q.getString("title");
        getSupportActionBar().setTitle(title);

        String string = db.getlinks(title);
        string.replace("http://", "https://");
        String newString = string.replace("http://", "https://");
        String[] urlslist = newString.split("https://");

        list.addAll(Arrays.asList(urlslist).subList(1, urlslist.length));

        GetLinks movie = new GetLinks(this, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(movie);
        mRecyclerView.setNestedScrollingEnabled(false);
        movie.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}