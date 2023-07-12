package in.mplay.app.activites;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.adapter.CategoryAdapter;
import in.mplay.app.adapter.MoviesAdapter;
import in.mplay.app.adapter.SlideShowAdapter;
import in.mplay.app.admob.GoogleAds;
import in.mplay.app.json.JsonMovies;
import in.mplay.app.utils.DatabaseHelper;
import in.mplay.app.utils.Session;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private List < String > jsonCategories = new ArrayList < > ();
    private List <JsonMovies> jsonMovies = new ArrayList < > ();
    private List <JsonMovies> response = new ArrayList < > ();
    private List <JsonMovies> jsonMoviesSlider = new ArrayList < > ();
    protected Handler handler = new Handler();
    private DatabaseHelper db;
    private TextView cat_name;
    private Session session;
    private static long backPressed = 0;
    private ViewPager mPager;
    private static int currentPage = 0;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private TextView empty_view;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchbtn) {
            final Dialog search_dailog = new Dialog(Objects.requireNonNull(this));
            search_dailog.setContentView(R.layout.dialog_file_search);
            final TextInputLayout enter_search_keyword = search_dailog.findViewById(R.id.enter_search_keyword);
            Button search_button = search_dailog.findViewById(R.id.search_button);

            search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyword = Objects.requireNonNull(enter_search_keyword.getEditText()).getText().toString();
                    if (keyword.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Videoname is empty ! ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(MainActivity.this, Search.class);
                        i.putExtra("keyword", keyword);
                        startActivity(i);
                        search_dailog.dismiss();
                    }
                }
            });
            search_dailog.show();
        }
        if (id == R.id.sharebtn) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Watch TV Shows, Movies - MegaPlay");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://megaplay.in");
            startActivity(Intent.createChooser(sharingIntent, "Share With"));
        }
        return super.onOptionsItemSelected(item);

    }

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Online(this);
        new GoogleAds(this).loadAds();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        db = new DatabaseHelper(this);
        session = new Session(this);
        mPager = findViewById(R.id.pager);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty_view = findViewById(R.id.empty_view);
        RelativeLayout channels_btn = findViewById(R.id.channels_btn);
        RelativeLayout categories_btn = findViewById(R.id.categories_btn);
        cat_name = findViewById(R.id.cat_name);

        categories_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category_Select();
            }
        });

        channels_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Channels.class));
            }
        });


        jsonCategories.addAll(db.getCategory());
        if (session.getmyCat() == null) {
            Category_Select();
        }else {
            loadData();
            slider();
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MoviesAdapter(MainActivity.this, jsonMovies, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (jsonMovies.size() < response.size()) {
                    jsonMovies.add(null);
                    mAdapter.notifyItemInserted(jsonMovies.size() - 1);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jsonMovies.remove(jsonMovies.size() - 1);
                            mAdapter.notifyItemRemoved(jsonMovies.size());
                            int start = jsonMovies.size();
                            int end = start + 10;

                            while (start < (Math.min(response.size(), end))) {
                                JsonMovies json = response.get(start);
                                JsonMovies model = new JsonMovies(json.getId(), json.getTitle(),
                                        json.getDescription(), json.getQuality(), json.getLanguage(),
                                        json.getChannel(), json.getCategory(), json.getImg_link(), json.getD_link(), json.getDate());
                                jsonMovies.add(model);
                                mAdapter.notifyItemInserted(jsonMovies.size());
                                start++;
                            }
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                        }
                    },2000);

                } else {
                    Toast.makeText(MainActivity.this, "No more data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        }

    private void Category_Select() {
        final Dialog dialog = new Dialog(this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_caregory, null);
        GridView listView =view.findViewById(R.id.simpleGridView);
        CategoryAdapter adapter = new CategoryAdapter(this, jsonCategories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select= jsonCategories.get(position);
                session.setCategory(select);
                cat_name.setText(session.getmyCat());
                slider();
                loadData();
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void loadData() {
        jsonMovies.clear();
        response.clear();
        response.addAll(db.allmovies(session.getmyCat()));
              if(response.size() == 0){
                  empty_view.setVisibility(View.VISIBLE);
                  mRecyclerView.setVisibility(View.GONE);
              }else{
                  mRecyclerView.setVisibility(View.VISIBLE);
                  empty_view.setVisibility(View.GONE);
                  int i = 0;

                  while (i < (Math.min(response.size(), 10))) {
                      JsonMovies json = response.get(i);
                      JsonMovies model = new JsonMovies(json.getId(), json.getTitle(), json.getDescription(), json.getQuality(), json.getLanguage(),
                              json.getChannel(), json.getCategory(), json.getImg_link(), json.getD_link(), json.getDate());
                      jsonMovies.add(model);
                      i++;
                  }
        }

        }
    private void slider() {
        jsonMoviesSlider.clear();
        jsonMoviesSlider.addAll(db.getSlider_list(session.getmyCat()));
        mPager.setAdapter(new SlideShowAdapter(this,jsonMoviesSlider));
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == jsonMoviesSlider.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 10000);
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