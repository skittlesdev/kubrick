package com.github.skittlesdev.kubrick.interfaces;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

/**
 * Created by lowgr on 10/29/2015.
 */
public interface DataReceiver {
    void workOnData(List<? extends IdElement> elements);
}
