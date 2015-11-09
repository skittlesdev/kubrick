package com.github.skittlesdev.kubrick.interfaces;

import info.movito.themoviedbapi.model.core.IdElement;

public interface MediaListener {
    void onMediaRetrieved(IdElement media);
}
