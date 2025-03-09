package com.epiboly.bea2.http.glide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Create by mao on 20-8-31
 */
public class Glide4RoundCornerTransform extends BitmapTransformation {

    private static final String ID = Glide4RoundCornerTransform.class.getName();

    private int radius;
    private @CornerType int cornerType;

    public Glide4RoundCornerTransform() {
        this(0,ALL);
    }

    public Glide4RoundCornerTransform(int dpValue, @CornerType int cornerType) {
        this.radius = (int) (Resources.getSystem().getDisplayMetrics().density * dpValue);
        this.cornerType=cornerType;
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        if (cornerType==ALL){
           return TransformationUtils.roundedCorners(pool, toTransform, radius);
        }else {
            int width = toTransform.getWidth();
            int height = toTransform.getHeight();
            Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
            result.setHasAlpha(true);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            drawRoundRect(canvas, paint, width, height);
            return result;
        }
    }

    private void drawRoundRect(Canvas canvas, Paint paint, int width, int height) {
        switch (cornerType) {
            case TOP:
                canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
                canvas.drawRect(new RectF(0, radius, width, height), paint);
                break;
            case BOTTOM:
                canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
                canvas.drawRect(new RectF(0, 0, width, height - radius), paint);
                break;
            case LEFT:
                canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
                canvas.drawRect(new RectF(radius, 0, width, height), paint);
                break;
            case RIGHT:
                canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
                canvas.drawRect(new RectF(0, 0, width-radius, height), paint);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Glide4RoundCornerTransform &&
                ((Glide4RoundCornerTransform) o).radius == radius &&
                ((Glide4RoundCornerTransform) o).cornerType == cornerType;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 10000  + cornerType * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + cornerType).getBytes(CHARSET));
    }

    //圆角类型
    @IntDef({ALL,TOP,BOTTOM,LEFT,RIGHT})
    public @interface CornerType{}
    //4个角
    public static final int ALL=1;
    //左上、右上
    public static final int TOP=2;
    //左下、右下
    public static final int BOTTOM=3;
    //左上、左下
    public static final int LEFT=4;
    //右上、右下
    public static final int RIGHT=5;

}
