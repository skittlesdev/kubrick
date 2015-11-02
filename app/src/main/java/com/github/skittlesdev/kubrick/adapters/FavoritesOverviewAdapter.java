package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.skittlesdev.kubrick.R;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPoster(this.context, this.favorites.get(position).getString("poster_path"));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (ImageView) itemView.findViewById(R.id.moviePoster);
        }
        public void setPoster(Context context, String posterPath) {
            Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into(this.poster);
        }
    }
}
