package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

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
        SimpleDraweeView poster = holder.moviePoster;
        String posterPath = "";

        if(element instanceof MovieDb){
            posterPath = ((MovieDb) element).getPosterPath();
        }

        if(element instanceof TvSeries){
            posterPath = ((TvSeries) element).getPosterPath();
        }

        holder.element = element;

        poster.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w154" + posterPath));
    }

    @Override
    public int getItemCount() {
        return this.mElements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView moviePoster;
        public IdElement element;

        public ViewHolder(View itemView) {
            super(itemView);
            this.moviePoster = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
            this.moviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, MediaActivity.class);

            if (this.element instanceof MovieDb) {
                intent.putExtra("MEDIA_TYPE", "movie");
            }

            if (this.element instanceof TvSeries) {
                intent.putExtra("MEDIA_TYPE", "tv");
            }

            intent.putExtra("MEDIA_ID", this.element.getId());
            context.startActivity(intent);
        }
    }
}
