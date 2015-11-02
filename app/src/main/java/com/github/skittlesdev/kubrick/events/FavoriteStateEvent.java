package com.github.skittlesdev.kubrick.events;

import com.github.skittlesdev.kubrick.utils.FavoriteState;

public class FavoriteStateEvent {
    private FavoriteState favoriteState;

    public FavoriteStateEvent(FavoriteState favoriteState) {
        this.favoriteState = favoriteState;
    }

    public FavoriteState getFavoriteState() {
        return favoriteState;
    }
}
