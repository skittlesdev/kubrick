package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.R;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import info.movito.themoviedbapi.model.people.Person;

import java.util.List;

public class CreditsOverviewAdapter extends RecyclerView.Adapter<CreditsOverviewAdapter.ViewHolder> {
    private List<Person> credits;
    private Context context;

    public CreditsOverviewAdapter(List<Person> credits) {
        this.credits = credits;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.credits.get(position));
        holder.setProfile(this.context);
    }

    @Override
    public int getItemCount() {
        return this.credits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private Person item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.profile = (ImageView) itemView.findViewById(R.id.moviePoster);
        }
        public void setProfile(Context context) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w500" + item.getProfilePath())
                    .placeholder(R.drawable.poster_default_placeholder)
                    .error(R.drawable.poster_default_error)
                    .into(this.profile);
        }

        public void setItem(Person item) {
            this.item = item;
        }
    }
}
