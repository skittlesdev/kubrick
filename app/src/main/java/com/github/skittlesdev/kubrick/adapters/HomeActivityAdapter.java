package com.github.skittlesdev.kubrick.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;

/**
 * Created by lowgr on 10/29/2015.
 */
public class HomeActivityAdapter extends BaseAdapter implements View.OnClickListener {
    private List<? extends IdElement> mElements;

    public HomeActivityAdapter(List<? extends IdElement> movies) {
        this.mElements = movies;
    }

    @Override
    public int getCount() {
        return this.mElements.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mElements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mElements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(KubrickApplication.getContext());
        convertView = inflater.inflate(R.layout.movies_layout, null);

        final String posterPath = ((MovieDb) this.getItem(position)).getPosterPath();

        Picasso.with(parent.getContext())
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                //.resize(holder.avatar.getMaxHeight(), holder.avatar.getMaxWidth())
                .into((ImageView) convertView.findViewById(R.id.moviePoster));


        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
