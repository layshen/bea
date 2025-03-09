package com.epiboly.bea2.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author: mao
 * @date: 20-12-1
 * @desc:
 */
public class BitmapHelper {

    /**
     * 防止拷贝的时候出现OOM
     * @param bitmap 原bitmap
     * @param config 颜色通道
     * @param retryCount 重试次数
     */
    public static Bitmap safeCopyBitmap(Bitmap bitmap,Bitmap.Config config, int retryCount) {
        try {
            return bitmap.copy(config,true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return safeCopyBitmap(bitmap,config, retryCount - 1);
            }
            return null;
        }
    }

    /**
     * 创建Bitmap 防止OOM
     * @param w  The width of the bitmap
     * @param h  The height of the bitmap
     * @param config The bitmap config to create.
     * @param retryCount 重试次数
     * @return 返回bitmap可能为null
     */
    @Nullable
    public static Bitmap safeCreateBitmap(int w, int h, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(w, h, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return safeCreateBitmap(w,h,config, retryCount - 1);
            }
            return null;
        }
    }

    /**
     * drawable转为Bitmap
     * @param drawable drawable
     * @return Bitmap
     */
    @Nullable
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null)
            return null;
        else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
            return null;

        try {
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 按比例进行缩放
     * @param origin 原始bitmap
     * @param ratio 比例
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBitmap;
        try {
            newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        }catch (OutOfMemoryError e){
            return null;
        }
        if (newBitmap==null){
            return null;
        }
        if (newBitmap.equals(origin)) {
            return newBitmap;
        }
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBitmap;
    }

    public static Bitmap createBitmapFromView(View view) {
        return createBitmapFromView(view, 1f,Color.TRANSPARENT);
    }

    public static Bitmap createBitmapFromView(View view ,@ColorInt int color) {
        return createBitmapFromView(view, 1f, color);
    }

    public static Bitmap createBitmapFromView(View view, float scale,@ColorInt int color) {
        if (view == null){
            return null;
        }
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();
        Bitmap bitmap = safeCreateBitmap((int) (view.getWidth() * scale),
                (int) (view.getHeight() * scale), Bitmap.Config.ARGB_8888, 1);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);;
            canvas.setBitmap(bitmap);
            canvas.save();
            canvas.drawColor(color);
            canvas.scale(scale, scale);
            view.draw(canvas);
            canvas.restore();
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * 将topBitmap和bottomBitmap合并成一张
     * @return
     */
    @Deprecated
    public static Bitmap drawBitmapV(Bitmap topBitmap , Bitmap bottomBitmap, int margin) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>(2);
        bitmaps.add(topBitmap);
        bitmaps.add(bottomBitmap);
        int width = bitmaps.get(0).getWidth();
        int height = bitmaps.get(0).getHeight();
        for (int i = 1;i<bitmaps.size();i++) {
            if (width < bitmaps.get(i).getWidth()) {
                width = bitmaps.get(i).getWidth();
            }
            height = height+bitmaps.get(i).getHeight();
        }
        height = height + margin;
        Bitmap bitmap = safeCreateBitmap(width, height, Bitmap.Config.ARGB_8888,1);
        if (bitmap!=null){
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setDither(true);
            canvas.drawBitmap(topBitmap, 0, 0, paint);
            int h = topBitmap.getHeight();
            canvas.drawBitmap(bottomBitmap, 0, h + margin , paint);
        }
        return bitmap;
    }

    public static Bitmap drawBitmapVWithScale(Bitmap topBitmap , Bitmap bottomBitmap, int margin) {
        int width = topBitmap.getWidth();
        int height = topBitmap.getHeight();

        int width2 = bottomBitmap.getWidth();

        float scaleX = (float)width/width2;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, 1);
        Bitmap bottomBitmapScale;
        try {
            bottomBitmapScale = Bitmap.createBitmap(bottomBitmap, 0, 0, bottomBitmap.getWidth(), bottomBitmap.getHeight(),
                    matrix, true);
        }catch (Exception ignored){
            bottomBitmapScale = bottomBitmap;
        }
        height+=bottomBitmapScale.getHeight();
        height = height + margin;
        Bitmap bitmap = safeCreateBitmap(width, height, Bitmap.Config.ARGB_8888,1);
        if (bitmap!=null){
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setDither(true);
            canvas.drawBitmap(topBitmap, 0, 0, paint);
            int h = topBitmap.getHeight();
            canvas.drawBitmap(bottomBitmapScale, 0, h + margin , paint);
        }
        return bitmap;
    }
}
