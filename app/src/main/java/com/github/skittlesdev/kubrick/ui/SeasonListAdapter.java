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


import java.util.HashMap;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeason;



public class SeasonListAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> tvSeasons;
    private Context context;

    public SeasonListAdapter(List<HashMap<String, Object>> tvSeasons) {
        this.tvSeasons = tvSeasons;
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
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
        HashMap<String, Object> item = getItem(position);
        //holder.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w185" + item.getPosterPath()));
        holder.name.setText((String) item.get("name"));
        if (false) {// TODO test if current user have watched this item
            holder.watched.setImageDrawable(convertView.getContext().getResources().getDrawable(R.drawable.ic_view));
        }
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

    public static class ViewHolder {
        public ImageView poster;
        public TextView name;
        public ImageView watched;


        public ViewHolder(View itemView) {
            this.poster = (ImageView) itemView.findViewById(R.id.seasonListIcon);
            this.name = (TextView) itemView.findViewById(R.id.seasonListName);
            this.watched = (ImageView) itemView.findViewById(R.id.seasonListWatched);
            itemView.setTag(this);
        }

    }
}


