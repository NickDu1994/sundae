package com.xwing.sundae.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xwing.sundae.android.model.BaseImage;
import com.xwing.sundae.android.model.IndexBannerImage;
import com.youth.banner.loader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        BaseImage imagepath = (BaseImage)path;
        Glide.with(context).load(imagepath.getImageUrl()).into(imageView);
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
