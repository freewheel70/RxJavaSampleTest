package com.hong.app.rxjavatest.Utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.hong.app.rxjavatest.FreeGankApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/23.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final String PUBLIC_IMAGE_STORAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pretty";

    public static final String PRIVATE_IMAGE_STORAGE_DIR = FreeGankApplication.getInstance().getFilesDir().getAbsolutePath() + "/Pretty";

    public static void saveBitmapIntoFile(Bitmap bitmap, String dirPath, String imageName) {

        Log.d(TAG, "saveBitmapIntoFile() called with: " + "bitmap = [" + bitmap + "], dirPath = [" + dirPath + "], imageName = [" + imageName + "]");

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //create a file to write bitmap data
        File file = new File(dir, imageName + ".png");
        FileOutputStream fos = null;
        try {
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean isImageFileExist(String dirPath, String imageName) {
        Log.d(TAG, "isImageFileExist() called with: " + "dirPath = [" + dirPath + "], imageName = [" + imageName + "]");

        File dir = new File(dirPath);
        File file = new File(dir, imageName + ".png");

        return file.exists();
    }

    public static void deleteImage(String dirPath, String imageName) {
        Log.d(TAG, "deleteImage() called with: " + "dirPath = [" + dirPath + "], imageName = [" + imageName + "]");

        File dir = new File(dirPath);
        File file = new File(dir, imageName + ".png");
        if (file.exists()) {
            file.delete();
        }

    }

    public static File getImageFile(String dirPath, String imageName) {
        Log.d(TAG, "deleteImage() called with: " + "dirPath = [" + dirPath + "], imageName = [" + imageName + "]");

        File dir = new File(dirPath);
        File file = new File(dir, imageName + ".png");

        return file;
    }
}
