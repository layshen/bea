package com.epiboly.bea2.util;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author mao
 * @time 2023/1/21
 * @describe
 */
public class ImageUtil {
    //保存图片到系统相册
    public static void saveBitmapToAlbum(Context appContext, Bitmap bitmap, String displayName, String mimeType, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null) return;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        } else {
            values.put(MediaStore.MediaColumns.DATA, Environment.getExternalStorageDirectory().getPath()+ File.separator+Environment.DIRECTORY_DCIM+File.separator+displayName);
        }
        Uri uri = appContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = appContext.getContentResolver().openOutputStream(uri);
                bitmap.compress(compressFormat, 100, outputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
