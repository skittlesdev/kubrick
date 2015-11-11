package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.R;

import java.util.List;

import info.movito.themoviedbapi.model.Video;

/**
 * Created by lowgr on 11/11/2015.
 */
public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolder> {
    private List<Video> videos;

    public ListVideoAdapter(List<Video> videos) {
        super();

        this.videos = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_layout, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(this.videos.get(position));
        holder.setThumbnail();
    }

    @Override
    public int getItemCount() {
        return this.videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView thumbnail;
        private TextView name;
        private Video video;

        public ViewHolder(View itemView) {
            super(itemView);

            this.thumbnail = (SimpleDraweeView) itemView.findViewById(R.id.videoThumbnail);
            this.thumbnail.setOnClickListener(this);
            this.name = (TextView) itemView.findViewById(R.id.videoName);
        }

        public void setThumbnail() {
            String imageUri = "http://img.youtube.com/vi/" + this.video.getKey() + "/0.jpg";
            this.thumbnail.setImageURI(Uri.parse(imageUri));
        }

        public void setItem(Video item) {
            String videoName = item.getName();

            if (TextUtils.isEmpty(videoName)) {
                videoName = "Video " + this.getAdapterPosition();
            }

            this.video = item;
            this.name.setText(videoName);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + this.video.getKey()));

            context.startActivity(intent);
        }
    }
}
