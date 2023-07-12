package in.mplay.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import in.mplay.app.R;
import in.mplay.app.activites.Info;
import in.mplay.app.admob.GoogleAds;
import in.mplay.app.json.JsonMovies;


public class SlideShowAdapter extends PagerAdapter {
    private Context context;
    private List<JsonMovies> jsonMoviesSlider;
    private LayoutInflater inflater;
    private GoogleAds ads;

    public SlideShowAdapter(Context context, List<JsonMovies> jsonMoviesSlider) {
        this.context = context;
        this.jsonMoviesSlider=jsonMoviesSlider;
        inflater = LayoutInflater.from(context);
        ads = new GoogleAds(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
            return jsonMoviesSlider.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        final JsonMovies myjson = jsonMoviesSlider.get(position);
        View myImageLayout = inflater.inflate(R.layout.slideshow, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image);
        Glide.with(context).load(myjson.getImg_link()).into(myImage);
        view.addView(myImageLayout, 0);


        myImage.setOnClickListener(new View.OnClickListener() {
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
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}