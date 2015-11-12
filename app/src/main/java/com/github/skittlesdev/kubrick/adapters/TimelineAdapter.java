package com.github.skittlesdev.kubrick.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.R;

import java.util.HashMap;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private List<HashMap<String, Object>> items;

    public TimelineAdapter(List<HashMap<String, Object>> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, Object> item = this.items.get(position);
        holder.item = item;
        holder.image.setImageURI(Uri.parse((String) item.get("image")));
        holder.message.setText((String) item.get("message"));
        holder.date.setText((String) item.get("date"));
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView image;
        public TextView message;
        public TextView date;
        public HashMap<String, Object> item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            this.message = (TextView) itemView.findViewById(R.id.message);
            this.date = (TextView) itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {
            String uri = (String) item.get("uri");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));

            view.getContext().startActivity(intent);
        }
    }
}
