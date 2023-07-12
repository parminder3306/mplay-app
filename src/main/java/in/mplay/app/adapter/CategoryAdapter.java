package in.mplay.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.mplay.app.R;


public class CategoryAdapter extends BaseAdapter {

    private List<String> list;

    private LayoutInflater layoutInflater;

    public CategoryAdapter(Context context, List <String> getlist) {
        this.list = getlist;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_category, null);
            holder = new ViewHolder();
            holder.category_name = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.category_name.setText(list.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView category_name;
    }

}
