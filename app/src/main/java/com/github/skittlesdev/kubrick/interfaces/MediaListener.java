package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.model.core.IdElement;

public interface MediaListener {
    public void onMediaRetrieved(IdElement media);
}
