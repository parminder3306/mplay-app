package in.mplay.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.mplay.app.R;
import in.mplay.app.activites.ChannelMovies;
import in.mplay.app.json.JsonChannels;


public class ChannelAdapter extends RecyclerView.Adapter < ChannelAdapter.MyViewHolder > {
    private Context context;
    private List<JsonChannels> jsonChannels;

    public ChannelAdapter(Context context, List <JsonChannels> jsonChannels) {
        this.jsonChannels = jsonChannels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_channels, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final JsonChannels getdata = jsonChannels.get(position);
        holder.channel_name.setText(getdata.getChannel_name());
        Glide.with(context).load(getdata.getChannel_logo()).into(holder.channel_logo);

        holder.card_channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChannelMovies.class);
                intent.putExtra("channel_name",getdata.getChannel_name());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return jsonChannels.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView channel_logo;
        CardView card_channels;
        TextView channel_name;

        MyViewHolder(View view) {
            super(view);
            channel_logo = view.findViewById(R.id.channel_logo);
            channel_name = view.findViewById(R.id.channel_name);
            card_channels = view.findViewById(R.id.card_channels);

        }
    }
}