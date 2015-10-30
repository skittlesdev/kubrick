package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;

/**
 * Created by lowgr on 10/29/2015.
 */
public class HomeActivityRecyclerAdapter extends
        RecyclerView.Adapter<HomeActivityRecyclerAdapter.ViewHolder> {
    private List<? extends IdElement> mElements;
    private Context mContext;

    public HomeActivityRecyclerAdapter(List<? extends IdElement> elements) {
        this.mElements = elements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(this.mContext);
        final View contactView = inflater.inflate(R.layout.movies_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IdElement element = this.mElements.get(position);
        ImageView poster = holder.moviePoster;
        String posterPath = ((MovieDb) element).getPosterPath();

        Picasso.with(this.mContext)
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                //.resize(holder.avatar.getMaxHeight(), holder.avatar.getMaxWidth())
                .into(poster);
    }

    @Override
    public int getItemCount() {
        return this.mElements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);

            this.moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            this.moviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(KubrickApplication.getContext(), "CLICKED!", Toast.LENGTH_SHORT).show();
        }
    }
}