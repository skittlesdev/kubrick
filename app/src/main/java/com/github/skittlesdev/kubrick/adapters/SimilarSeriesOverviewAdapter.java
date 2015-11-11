package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Hugo Caille on 10/11/2015.
 */
public class SimilarSeriesOverviewAdapter extends RecyclerView.Adapter<SimilarSeriesOverviewAdapter.ViewHolder> {
    private List<HashMap<String, Object>> results;

    public SimilarSeriesOverviewAdapter(HashMap<String, Object> series) {
        this.results = (List<HashMap<String, Object>>) series.get("results");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.results.get(position));
    }

    @Override
    public int getItemCount() {
        return this.results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView poster;
        private HashMap<String, Object> item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
            this.poster.setOnClickListener(this);
        }

        public void setItem(HashMap<String, Object> item) {
            this.item = item;
            this.poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w154" + item.get("poster_path")));
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Intent intent = new Intent(context, MediaActivity.class);
            intent.putExtra("MEDIA_TYPE", "tv");
            intent.putExtra("MEDIA_ID", (int) this.item.get("id"));

            context.startActivity(intent);
        }
    }
}
