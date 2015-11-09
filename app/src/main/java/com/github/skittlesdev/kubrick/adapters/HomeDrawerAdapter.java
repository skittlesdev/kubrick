package com.github.skittlesdev.kubrick.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.LoginActivity;
import com.github.skittlesdev.kubrick.ProfileActivity;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.Callback;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.github.skittlesdev.kubrick.utils.RowElement;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by lowgr on 10/30/2015.
 */
public class HomeDrawerAdapter extends RecyclerView.Adapter<HomeDrawerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;

    private List<RowElement> mTitles;
    private ProfileElement mProfile;

    public static class ProfileViewHolder extends HomeDrawerAdapter.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView avatar;
        public TextView name;
        public TextView email;
        private Context context;

        public ProfileViewHolder(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.name);
            this.name.setOnClickListener(this);
            this.email = (TextView) itemView.findViewById(R.id.email);
            this.email.setOnClickListener(this);
            this.avatar = (SimpleDraweeView) itemView.findViewById(R.id.circleView);
            this.avatar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            if (ParseUser.getCurrentUser() != null) {
                intent = new Intent(this.context, ProfileActivity.class);
                intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
            }
            else {
                intent = new Intent(this.context, LoginActivity.class);
            }
            this.context.startActivity(intent);
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }

    public static class TitleViewHolder extends HomeDrawerAdapter.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView icon;
        public Callback callback;

        public TitleViewHolder(View itemView) {
            super(itemView);

            this.title = (TextView) itemView.findViewById(R.id.rowTextHome);
            this.title.setOnClickListener(this);
            this.icon = (ImageView) itemView.findViewById(R.id.rowIconHome);
            this.icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (callback != null) {
                callback.execute();
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public HomeDrawerAdapter(Context context, List<RowElement> titles, ProfileElement profile) {
        this.context = context;
        this.mTitles = titles;
        this.mProfile = profile;
    }

    @Override
    public HomeDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutToInflate;
        View view;

        if (viewType == HomeDrawerAdapter.TYPE_ITEM) {
            layoutToInflate = R.layout.item_row_home;
            view = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == HomeDrawerAdapter.TYPE_HEADER) {
            layoutToInflate = R.layout.header_home;
            view = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate, parent, false);
            return new ProfileViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(HomeDrawerAdapter.ViewHolder holder, int position) {
        if (holder instanceof ProfileViewHolder) {
            this.onBindViewHolder((ProfileViewHolder) holder, position);
        } else if (holder instanceof TitleViewHolder) {
            this.onBindViewHolder((TitleViewHolder) holder, position);
        }
    }

    private void onBindViewHolder(HomeDrawerAdapter.TitleViewHolder holder, int position) {
        holder.title.setText(this.mTitles.get(position - 1).getTitle());
        holder.icon.setImageResource(this.mTitles.get(position - 1).getIcon());
        holder.callback = this.mTitles.get(position - 1).getCallback();
    }

    private void onBindViewHolder(HomeDrawerAdapter.ProfileViewHolder holder, int position) {
        holder.setContext(this.context);
        if (!TextUtils.isEmpty(this.mProfile.getAvatarUrl())) {
            holder.avatar.setImageURI(Uri.parse(this.mProfile.getAvatarUrl()));
        }
        holder.name.setText(this.mProfile.getName());
        holder.email.setText(this.mProfile.getEmail());
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