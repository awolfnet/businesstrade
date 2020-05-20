package net.awolf.businesstrade.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by zhaohai on 2018/1/1.
 */

public class ImageUtil {
    private String fileName = "";
    private File tempFile;
    private static final int OPEN_CAMERA_CODE = 10;
    private static final int OPEN_GALLERY_CODE = 11;
    private static final int CROP_PHOTO_CODE = 12;


    public static Bitmap getLoacalBitmap(String url) throws FileNotFoundException {

            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

    }
}
