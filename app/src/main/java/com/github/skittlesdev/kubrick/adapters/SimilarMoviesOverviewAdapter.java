package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.R;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class SimilarMoviesOverviewAdapter extends RecyclerView.Adapter<SimilarMoviesOverviewAdapter.ViewHolder> {
    private List<MovieDb> movies;
    private Context context;

    public SimilarMoviesOverviewAdapter(List<MovieDb> movies) {
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.movies.get(position));
        holder.setposter(this.context);
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView poster;
        private MovieDb item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
        }
        public void setposter(Context context) {
            this.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w500" + item.getPosterPath()));
        }

        public void setItem(MovieDb item) {
            this.item = item;
        }
    }
}
