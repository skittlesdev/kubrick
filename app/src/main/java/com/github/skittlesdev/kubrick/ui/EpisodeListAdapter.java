package com.github.skittlesdev.kubrick.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;

import java.util.HashMap;
import java.util.List;

import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;

/**
 * Created by louis on 11/11/2015.
 */
public class EpisodeListAdapter extends BaseAdapter {

    private List<SeriesEpisode> tvEpisodeList;
    private Context context;

    public EpisodeListAdapter(List<SeriesEpisode> tvEpisodeList) {
        this.tvEpisodeList = tvEpisodeList;
    }

    @Override
    public SeriesEpisode getItem(int position) {
        return tvEpisodeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(),
                    R.layout.season_list_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        SeriesEpisode item = getItem(position);
        //holder.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w185" + item.getPosterPath()));
        holder.name.setText((String) item.name);
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
        return tvEpisodeList.size();
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
