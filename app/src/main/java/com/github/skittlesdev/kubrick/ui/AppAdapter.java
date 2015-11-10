package com.github.skittlesdev.kubrick.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;

import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeason;

public class AppAdapter extends BaseAdapter {

    private List<TvSeason> seasonList;
    private Context context;

    public AppAdapter(List<TvSeason> seasonList, Context context) {
        this.seasonList = seasonList;
        this.context = context;
    }

    public List<TvSeason> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<TvSeason> seasonList) {
        this.seasonList = seasonList;
    }

    @Override
    public int getCount() {
        return seasonList.size();
    }

    @Override
    public TvSeason getItem(int position) {
        return seasonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.season_list_item, null);
            new CustomSwipeViewHolder(convertView);
        }

        CustomSwipeViewHolder holder = (CustomSwipeViewHolder) convertView.getTag();
        TvSeason item = getItem(position);
        holder.getSeasonListName().setText(item.getName());
        item.getPosterPath()
        holder.getSeasonListIcon().setImageDrawable();
        return convertView;
    }

    public class Viewholder {
        ImageView seasonListIcon;
        TextView seasonListName;

        public Viewholder(View view) {
            seasonListIcon = (ImageView) view.findViewById(R.id.seasonListIcon);
            seasonListName = (TextView) view.findViewById(R.id.seasonListName);
            view.setTag(this);
        }

        public ImageView getSeasonListIcon() {
            return seasonListIcon;
        }

        public void setSeasonListIcon(ImageView seasonListIcon) {
            this.seasonListIcon = seasonListIcon;
        }

        public TextView getSeasonListName() {
            return seasonListName;
        }

        public void setSeasonListName(TextView seasonListName) {
            this.seasonListName = seasonListName;
        }
    }

}


