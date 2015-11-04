package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.ui.fragments.FavoritesOverviewFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        buildProfile();

        FavoritesOverviewFragment movieFavorites = new FavoritesOverviewFragment();
        Bundle movieFavoritesArgs = new Bundle();
        movieFavoritesArgs.putString("MEDIA_TYPE", "movie");
        movieFavorites.setArguments(movieFavoritesArgs);

        FavoritesOverviewFragment seriesFavorites = new FavoritesOverviewFragment();
        Bundle seriesFavoritesArgs = new Bundle();
        seriesFavoritesArgs.putString("MEDIA_TYPE", "tv");
        seriesFavorites.setArguments(seriesFavoritesArgs);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_movies, movieFavorites, "movies");
        transaction.add(R.id.fragment_series, seriesFavorites, "series");
        transaction.commit();
    }

    private void buildProfile() {
        ProfileElement profile = new ProfileElement(ParseUser.getCurrentUser());

        TextView name = (TextView) findViewById(R.id.name);
        CircleImageView image = (CircleImageView) findViewById(R.id.circleView);

        name.setText(profile.getName());
        if (!TextUtils.isEmpty(profile.getAvatarUrl())) {
            Picasso.with(KubrickApplication.getContext())
                .load(profile.getAvatarUrl())
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .fit()
                .into(image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }

}
