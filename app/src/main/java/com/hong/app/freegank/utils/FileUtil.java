package com.hong.app.freegank.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.hong.app.freegank.FreeGankApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Freewheel on 2016/4/23.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final String PUBLIC_IMAGE_STORAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pretty";

    public static final String PRIVATE_IMAGE_STORAGE_DIR = FreeGankApplication.getInstance().getFilesDir().getAbsolutePath() + "/Pretty";

    public static boolean saveBitmapIntoFile(Bitmap bitmap, String dirPath, String imageName) {

        Log.d(TAG, "saveBitmapIntoFile() called with: " + "bitmap = [" + bitmap + "], dirPath = [" + dirPath + "], imageName = [" + imageName + "]");

        boolean saveSuccess = false;

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            File file = new File(dir, imageName + ".png");
            FileOutputStream fos = new FileOutputStream(file);

            try {
                fos.write(bitmapData);
                fos.flush();
                saveSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                saveSuccess = false;
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return saveSuccess;
    }

    public static boolean copyFile(String sourcePath, String targetPath) throws IOException {
        return copyFile(new File(sourcePath), new File(targetPath));
    }

    public static boolean copyFile(File sourceFile, File targetFile) {

        boolean saveSuccess = false;

        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(targetFile);

            try {

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                saveSuccess = true;

            } catch (IOException e1) {
                e1.printStackTrace();
                saveSuccess = false;
            } finally {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }


        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }


        return saveSuccess;
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

    public static void deleteImage(String filePath) {

        File file = new File(filePath);
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
