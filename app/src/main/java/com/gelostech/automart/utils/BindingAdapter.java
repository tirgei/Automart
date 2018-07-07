package com.gelostech.automart.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingAdapter {

    @android.databinding.BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, int url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

}
