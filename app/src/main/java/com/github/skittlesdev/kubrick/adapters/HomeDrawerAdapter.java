package com.github.skittlesdev.kubrick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.github.skittlesdev.kubrick.utils.RowElement;

import java.util.List;

/**
 * Created by lowgr on 10/30/2015.
 */
public class HomeDrawerAdapter extends RecyclerView.Adapter<HomeDrawerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<RowElement> mTitles;
    private ProfileElement mProfile;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int holderId;
        public TextView title;
        public ImageView icon;
        public ImageView avatar;
        public TextView name;
        public TextView email;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == HomeDrawerAdapter.TYPE_ITEM) {
                this.title = (TextView) itemView.findViewById(R.id.rowTextHome);
                this.title.setOnClickListener(this);
                this.icon = (ImageView) itemView.findViewById(R.id.rowIconHome);
                this.icon.setOnClickListener(this);
                this.holderId = 1;
            } else {
                this.name = (TextView) itemView.findViewById(R.id.name);
                this.name.setOnClickListener(this);
                this.email = (TextView) itemView.findViewById(R.id.email);
                this.email.setOnClickListener(this);
                this.avatar = (ImageView) itemView.findViewById(R.id.circleView);
                this.avatar.setOnClickListener(this);
                this.holderId = 0;
            }
        }

        @Override
        public void onClick(View view) {
            String toDisplay = "Hey!";

            if (view.getId() == R.id.rowTextHome || view.getId() ==  R.id.rowIconHome) {
                toDisplay += " Title clicked!";
            } else {
                toDisplay += " Profile clicked!";
            }

            Toast.makeText(KubrickApplication.getContext(), toDisplay, Toast.LENGTH_SHORT).show();
        }
    }

    public HomeDrawerAdapter(List<RowElement> titles, ProfileElement profile) {
        this.mTitles = titles;
        this.mProfile = profile;
    }

    @Override
    public HomeDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutToInflate;

        if (viewType == HomeDrawerAdapter.TYPE_ITEM) {
            layoutToInflate = R.layout.item_row_home;
        } else if (viewType == HomeDrawerAdapter.TYPE_HEADER) {
            layoutToInflate = R.layout.header_home;
        } else {
            return null;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeDrawerAdapter.ViewHolder holder, int position) {
        if (holder.holderId == 1) {
            holder.title.setText(this.mTitles.get(position - 1).getTitle());
            holder.icon.setImageResource(this.mTitles.get(position - 1).getIcon());
        } else {
            holder.avatar.setImageResource(this.mProfile.getAvatar());
            holder.name.setText(this.mProfile.getName());
            holder.email.setText(this.mProfile.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return this.mTitles.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (this.isPositionHeader(position)) {
            return HomeDrawerAdapter.TYPE_HEADER;
        }

        return HomeDrawerAdapter.TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}