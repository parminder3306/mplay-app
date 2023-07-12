package in.mplay.app.activites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.admob.GoogleAds;
import in.mplay.app.json.JsonMovies;
import in.mplay.app.utils.DatabaseHelper;

public class YouTubePlayerActivity extends YouTubeBaseActivity{
    private String GOOGLE_API_KEY = "AIzaSyAdCJTWldDxEoIUEBRzaMT9F9hdFgQCqfE";
    private String YOUTUBE_VIDEO_ID = null;
    private YouTubePlayer mYouTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplayer);
        new Online(this);

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        TextView empty_view = findViewById(R.id.empty_view);
        DatabaseHelper db = new DatabaseHelper(this);
        Bundle q = getIntent().getExtras();
        String category = q.getString("category");
        YOUTUBE_VIDEO_ID = q.getString("youtube_id");

        if (db.RelatedVideos(category, 50).size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            RelatedMovies mAdapter = new RelatedMovies(this, db.RelatedVideos(category, 30));
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(YouTubePlayerActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
        youTubePlayerSetup();
    }

    private void youTubePlayerSetup(){

        YouTubePlayerView mYouTubePlayerView = findViewById(R.id.youtube_player);
        YouTubePlayer.OnInitializedListener mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    mYouTubePlayer = youTubePlayer;
                    mYouTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

            }
        };

        mYouTubePlayerView.initialize(GOOGLE_API_KEY, mOnInitializedListener);

    }

    public class RelatedMovies extends RecyclerView.Adapter<RelatedMovies.MyViewHolder> {
        private Context context;
        private List<JsonMovies> list;
        private GoogleAds ads;


        RelatedMovies(Context context, List<JsonMovies> movieslist) {
            this.list = movieslist;
            this.context = context;
            ads = new GoogleAds(context);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movies, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final JsonMovies vid = list.get(position);
            Glide.with(context).load(vid.getImg_link()).into(holder.image_movie);
            holder.title_movie.setText(vid.getTitle());
            holder.category_movie.setText(vid.getCategory());

            String string = vid.getD_link();
            string.replace("http://", "https://");
            String newString = string.replace("http://", "https://");
            final String[] urlslist = newString.split("https://");

            holder.movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ads.showAds();
                    mYouTubePlayer.cueVideo(Verify("https://" + urlslist[1]));
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private String Verify(String youTubeUrl) {
            String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";

            Pattern compiledPattern = Pattern.compile(pattern,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = compiledPattern.matcher(youTubeUrl);
            if (matcher.find()) {
                return matcher.group(1);
            }else{
                return "Other";
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title_movie,category_movie;
            ImageView image_movie;
            CardView movie_card;

            MyViewHolder(View view) {
                super(view);
                image_movie = view.findViewById(R.id.image_movie);
                title_movie = view.findViewById(R.id.title_movie);
                category_movie = view.findViewById(R.id.category_movie);
                movie_card = view.findViewById(R.id.movie_card);

            }
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mYouTubePlayer != null) {
            mYouTubePlayer.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mYouTubePlayer != null) {
            mYouTubePlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
        }
    }
}