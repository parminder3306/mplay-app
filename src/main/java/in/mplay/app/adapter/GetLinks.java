package in.mplay.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import in.mplay.app.R;


public class GetLinks extends RecyclerView.Adapter < GetLinks.MyViewHolder > {
    private Context context;
    private List <String> list;

    public GetLinks(Context context, List <String> getlinks) {
        this.list = getlinks;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_links, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       final String link = list.get(position);
        holder.text_links.setText("Download Link: " + (position+1));
        holder.card_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + link));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_links;
        RelativeLayout card_links;

        MyViewHolder(View view) {
            super(view);
            text_links = view.findViewById(R.id.text_links);
            card_links = view.findViewById(R.id.card_links);

        }
    }
}