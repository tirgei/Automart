package com.gelostech.automart.utils;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BindingAdapter {

    @android.databinding.BindingAdapter({"imageUrl"})
    public static void loadImageFromInt(ImageView view, int url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

    @android.databinding.BindingAdapter({"imageUrl"})
    public static void loadImageFromString(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .thumbnail(0.05f)
                .into(view);
    }

    @android.databinding.BindingAdapter({"imageUri"})
    public static void loadImageFromUri(ImageView view, Uri uri) {
        Glide.with(view.getContext())
                .load(uri)
                .thumbnail(0.05f)
                .into(view);
    }


}
