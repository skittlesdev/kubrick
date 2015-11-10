package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.MediaActivity;
import com.github.skittlesdev.kubrick.PeopleActivity;
import com.github.skittlesdev.kubrick.R;
import com.parse.ParseObject;
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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_layout, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView profile;
        private TextView name;
        private Person item;

        public ViewHolder(View itemView) {
            super(itemView);

            this.profile = (SimpleDraweeView) itemView.findViewById(R.id.moviePoster);
            this.profile.setOnClickListener(this);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }

        public void setProfile(Context context) {
            this.profile.setImageURI(Uri.parse("http://image.tmdb.org/t/p/w185" + item.getProfilePath()));
        }

        public void setItem(Person item) {
            this.item = item;
            this.name.setText(item.getName());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, PeopleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("person", this.item);

            intent.putExtra("PERSON_OBJECT", bundle);
            context.startActivity(intent);
        }
    }
}
