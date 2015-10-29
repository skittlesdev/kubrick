package com.github.skittlesdev.kubrick.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.skittlesdev.kubrick.interfaces.DataReceiver;
import com.github.skittlesdev.kubrick.utils.Constants;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

/**
 * Created by lowgr on 10/29/2015.
 */
public class TmdbApiBroadcastReceiver extends BroadcastReceiver {
    private DataReceiver mDataReceiver;

    public TmdbApiBroadcastReceiver() {
        this(null);
    }

    public TmdbApiBroadcastReceiver(DataReceiver dataReceiver) {
        super();

        this.mDataReceiver = dataReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (this.mDataReceiver != null) {
            List<? extends IdElement> movies = (List<? extends IdElement>) intent.getSerializableExtra(Constants.Intent.INTENT_ID_ELEMENTS);
            this.mDataReceiver.workOnData(movies);
        }
    }
}