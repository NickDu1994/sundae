package com.xwing.sundae.android.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.android.model.BaseImage;
import com.xwing.sundae.android.model.IndexBannerImage;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        BaseImage imagepath = (BaseImage)path;
        Glide.with(context).load(imagepath.getImageUrl()).into(imageView);
    }
}
