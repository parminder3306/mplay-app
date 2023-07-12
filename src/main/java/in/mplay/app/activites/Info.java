package in.mplay.app.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.mplay.app.Online;
import in.mplay.app.R;
import in.mplay.app.admob.GoogleAds;


public class Info extends AppCompatActivity {
    private Bundle q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        new Online(this);

        View adContainer = findViewById(R.id.adMobView);
        new GoogleAds(this).showBannerAds(adContainer);

        ImageView image = findViewById(R.id.movie_image);
        TextView title = findViewById(R.id.movie_title);
        TextView category = findViewById(R.id.movie_category);
        TextView language = findViewById(R.id.movie_language);
        TextView quality = findViewById(R.id.movie_quality);
        TextView channel = findViewById(R.id.movie_channel);
        TextView date = findViewById(R.id.movie_date);
        TextView description = findViewById(R.id.movie_description);
        Button download = findViewById(R.id.download);
        Button share_movie = findViewById(R.id.share_movie);

        q = getIntent().getExtras();
        Glide.with(this).load(q.getString("img_link")).into(image);
        title.setText(q.getString("title"));
        category.setText(q.getString("category"));
        language.setText(q.getString("language"));
        quality.setText(q.getString("quality"));
        channel.setText(q.getString("channel"));
        date.setText(q.getString("date"));
        description.setText(q.getString("description"));

        String string = q.getString("d_link");
        string.replace("http://", "https://");
        String newString = string.replace("http://", "https://");
        final String[] urlslist = newString.split("https://");

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Verify("https://" + urlslist[1]);
                Intent link;

                if (url.equals("Other")){
                    link = new Intent(Info.this, Links.class);
                    link.putExtra("title", q.getString("title"));
                }else {
                    link = new Intent(Info.this, YouTubePlayerActivity.class);
                    link.putExtra("category", q.getString("category"));
                    link.putExtra("youtube_id", url);
                }
                startActivity(link);
            }
        });


        share_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, q.getString("title"));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://megaplay.in/?shareid=" + q.getString("id"));
                startActivity(Intent.createChooser(sharingIntent, "Share With"));
            }
        });
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

}