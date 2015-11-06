package com.github.skittlesdev.kubrick.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.github.skittlesdev.kubrick.R;

/**
 * Created by Mimi on 05/11/2015.
 */
public class PosterFullScreenDialog extends Dialog {
    public PosterFullScreenDialog(Context context) {
        super(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.poster_fullscreen);
        setCancelable(true);

        ImageView img = (ImageView) findViewById(R.id.poster_full_screen);
        img.setImageResource(R.drawable.poster_default_placeholder);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public PosterFullScreenDialog(Context context, Drawable drawable) {
        super(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.poster_fullscreen);
        setCancelable(true);

        ImageView img = (ImageView) findViewById(R.id.poster_full_screen);
        img.setImageDrawable(drawable);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }
}