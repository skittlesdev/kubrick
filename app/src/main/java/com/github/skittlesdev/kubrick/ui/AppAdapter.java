package com.github.skittlesdev.kubrick.ui;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.R;


import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeason;



public class AppAdapter extends BaseAdapter {
    private List<TvSeason> tvSeasons;
    private Context context;

    public AppAdapter(List<TvSeason> tvSeasons) {
        this.tvSeasons = tvSeasons;
    }

    @Override
    public TvSeason getItem(int position) {
        return tvSeasons.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.season_list_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        TvSeason item = getItem(position);
        holder.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w185" + item.getPosterPath()));
        holder.name.setText(item.getName());
        Log.d("getView", "erggergregerg");
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return tvSeasons.size();
    }

    /*@Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_list_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.tvSeasons.get(position));
        holder.setPoster(this.context);
    }

    @Override
    public int getItemCount() {
        return tvSeasons.size();
    }*/

    public static class ViewHolder {
        public ImageView poster;
        public TextView name;


        public ViewHolder(View itemView) {
            this.poster = (ImageView) itemView.findViewById(R.id.seasonListIcon);
            this.name = (TextView) itemView.findViewById(R.id.seasonListName);
            itemView.setTag(this);
        }

    }
}


