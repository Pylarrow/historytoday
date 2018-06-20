package com.idthk.historytoday.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.idthk.historytoday.APP;

import java.io.ByteArrayOutputStream;

/**
 * Created by williamyin on 2017/12/11.
 */

public class ImageManager {
    public static Bitmap getBitmap(Context context) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(APP.getContext().getResources(), context.getApplicationInfo().icon,options);

        options.inSampleSize =30;

        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        Bitmap bitmap =BitmapFactory.decodeResource(APP.getContext().getResources(), context.getApplicationInfo().icon,options);
        Bitmap newBitmap = compressImage(bitmap,4);
        if (bitmap != null){
            bitmap.recycle();
        }
        return newBitmap;
    }
    public static Bitmap getBitmap(Context context,int bitmapId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(APP.getContext().getResources(), context.getApplicationInfo().icon,options);
        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        Bitmap bitmap =BitmapFactory.decodeResource(APP.getContext().getResources(), bitmapId,options);
        return bitmap;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    public static Bitmap getSmallBitmap(Context context,String filePath) {
        int newWidth = UtilTools.getScreenWidth(context);
        int newHeight = UtilTools.getScreenHeight(context);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        Bitmap newBitmap = compressImage(bitmap, 100);
        if (bitmap != null){
            bitmap.recycle();
        }
        return newBitmap;
    }
//    public static Bitmap getBitmap(Context context,String filePath) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath,options);
////         Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
//        Bitmap newBitmap = compressImage(bitmap, 500);
//        if (bitmap != null){
//            bitmap.recycle();
//        }
//        return newBitmap;
//    }
    /**
     * 质量压缩
     * @param image
     * @param maxSize
     */
    public static Bitmap compressImage(Bitmap image, int maxSize){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 80;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while ( os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        Bitmap bitmap = null;
        byte[] b = os.toByteArray();
        if (b.length != 0) {
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }
}
