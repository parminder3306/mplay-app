package in.mplay.app.activites;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.adapter.MoviesAdapter;
import in.mplay.app.utils.DatabaseHelper;


public class Search extends AppCompatActivity {
    private TextView empty_view;
    private RecyclerView mRecyclerView;
    private DatabaseHelper db;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchbtn) {
            final Dialog search_dailog = new Dialog(Objects.requireNonNull(Search.this));
            search_dailog.setContentView(R.layout.dialog_file_search);
            final TextInputLayout enter_search_keyword = search_dailog.findViewById(R.id.enter_search_keyword);
            Button search_button = search_dailog.findViewById(R.id.search_button);

            search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyword = Objects.requireNonNull(enter_search_keyword.getEditText()).getText().toString();
                    if (keyword.isEmpty()) {
                        Toast.makeText(Search.this, "Videoname is empty ! ", Toast.LENGTH_SHORT).show();
                    } else {
                        SearchMovies(keyword);
                        search_dailog.dismiss();
                    }
                }
            });
            search_dailog.show();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_two);
        new Online(this);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty_view = findViewById(R.id.empty_view);
        db = new DatabaseHelper(Search.this);

        Bundle q = getIntent().getExtras();
        String key = q.getString("keyword");
        SearchMovies(key);
        super.onCreate(savedInstanceState);
    }

    private void SearchMovies(String keyword) {
        if (db.search(keyword).size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            MoviesAdapter movie = new MoviesAdapter(Search.this, db.search(keyword),mRecyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(Search.this);
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