package com.gelostech.automart.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BindingAdapter {

    @android.databinding.BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, int url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

    @android.databinding.BindingAdapter({"bind:imageUrl"})
    public static void loadImage(CircleImageView view, int url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

    @android.databinding.BindingAdapter({"bind:imageUrl"})
    public static void loadImage(RoundedImageView view, int url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

}
