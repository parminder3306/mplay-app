package in.mplay.app.adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.mplay.app.R;
import in.mplay.app.activites.Info;
import in.mplay.app.activites.OnLoadMoreListener;
import in.mplay.app.admob.GoogleAds;
import in.mplay.app.json.JsonMovies;


public class MoviesAdapter extends RecyclerView.Adapter {
    private Context context;
    private List < JsonMovies > jsonMovies;
    private GoogleAds ads;
    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public MoviesAdapter(Context context, List<JsonMovies> jsonMovies, RecyclerView recyclerView) {
        this.context = context;
        this.jsonMovies = jsonMovies;
        ads = new GoogleAds(context);

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }
                    }
                });
            }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movies, parent, false);
            return new MyViewHolder(v);
        } else if (viewType == VIEW_PROG) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final JsonMovies myjson = jsonMovies.get(position);
            Glide.with(context).load(myjson.getImg_link()).into(((MyViewHolder) holder).image_movie);
            ((MyViewHolder) holder).title_movie.setText(myjson.getTitle());
            ((MyViewHolder) holder).category_movie.setText(myjson.getCategory());
            ((MyViewHolder) holder).movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ads.showAds();
                    Intent intent = new Intent(context, Info.class);
                    intent.putExtra("id", myjson.getId());
                    intent.putExtra("img_link", myjson.getImg_link());
                    intent.putExtra("title", myjson.getTitle());
                    intent.putExtra("category", myjson.getCategory());
                    intent.putExtra("language", myjson.getLanguage());
                    intent.putExtra("quality", myjson.getQuality());
                    intent.putExtra("d_link", myjson.getD_link());
                    intent.putExtra("channel", myjson.getChannel());
                    intent.putExtra("date", myjson.getDate());
                    intent.putExtra("description", myjson.getDescription());
                    context.startActivity(intent);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return jsonMovies.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return jsonMovies.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    public void setLoaded() {
        loading = false;
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView title_movie,category_movie;
         ImageView image_movie;
         CardView movie_card;

        public MyViewHolder(View v) {
            super(v);
            image_movie = v.findViewById(R.id.image_movie);
            title_movie = v.findViewById(R.id.title_movie);
            category_movie = v.findViewById(R.id.category_movie);
            movie_card = v.findViewById(R.id.movie_card);

        }
    }
}