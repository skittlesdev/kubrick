package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;
import com.parse.*;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.HashMap;
import java.util.List;

public class FavoritesOverviewAdapter extends RecyclerView.Adapter<FavoritesOverviewAdapter.ViewHolder> {
    private List<ParseObject> favorites;
    private Context context;
    private ParseUser user;

    public FavoritesOverviewAdapter(List<ParseObject> favorites, ParseUser user) {
        this.favorites = favorites;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ParseObject item = this.favorites.get(position);
        holder.setItem(item);
        holder.setPoster(this.context);

        if (item.has("tmdb_series_id")) {
            holder.setToSeries(this.context);
            HashMap<String, String> params = new HashMap<>();
            params.put("seriesId", String.valueOf(item.getInt("tmdb_series_id")));
            params.put("userId", this.user.getObjectId());
            ParseCloud.callFunctionInBackground("seriesProgress", params, new FunctionCallback<Integer>() {
                @Override
                public void done(Integer percentage, ParseException e) {
                    holder.setPercentage(percentage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView poster;
        private ParseObject item;
        private TextView percentageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
            this.poster.setOnClickListener(this);
            this.percentageView = (TextView) itemView.findViewById(R.id.percentage);
        }

        public void setPoster(Context context) {
            this.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w92" + item.getString("poster_path")));
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

        public void setToSeries(Context context) {
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, context.getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 158, context.getResources().getDisplayMetrics());
            this.poster.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            this.percentageView.setVisibility(View.VISIBLE);
        }

        public void setPercentage(Integer percentage) {
            this.percentageView.setText(String.valueOf(percentage) + "%");
        }
    }
}
