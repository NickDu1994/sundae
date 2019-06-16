package com.xwing.sundae.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PostImageUtil {

    public static String imgToBase64( int quality,Bitmap bitmap) {


        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            long length = imgBytes.length;
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
