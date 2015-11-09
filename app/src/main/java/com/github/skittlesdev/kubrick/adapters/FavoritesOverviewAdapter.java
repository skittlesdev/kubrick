package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;
import com.parse.ParseObject;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.List;

public class FavoritesOverviewAdapter extends RecyclerView.Adapter<FavoritesOverviewAdapter.ViewHolder> {
    private List<ParseObject> favorites;
    private Context context;

    public FavoritesOverviewAdapter(List<ParseObject> favorites) {
        this.favorites = favorites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.favorites.get(position));
        holder.setPoster(this.context);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView poster;
        private ParseObject item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
            this.poster.setOnClickListener(this);
        }

        public void setPoster(Context context) {
            this.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w185" + item.getString("poster_path")));
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Intent intent = new Intent(context, MediaActivity.class);

            if(this.item.has("tmdb_movie_id")){
                intent.putExtra("MEDIA_TYPE", "movie");
                intent.putExtra("MEDIA_ID", this.item.getInt("tmdb_movie_id"));
            }

            if(this.item.has("tmdb_series_id")){
                intent.putExtra("MEDIA_TYPE", "tv");
                intent.putExtra("MEDIA_ID", this.item.getInt("tmdb_series_id"));
            }

            context.startActivity(intent);
        }

        public void setItem(ParseObject item) {
            this.item = item;
        }
    }
}
