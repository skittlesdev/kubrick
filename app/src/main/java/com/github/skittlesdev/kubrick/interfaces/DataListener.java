package com.github.skittlesdev.kubrick.interfaces;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

/**
 * Created by lowgr on 10/29/2015.
 */
/*







USELESS (FOR NOW?)









 */
public interface DataListener {
    void onDataRetrieved(List<? extends IdElement> data);
}
