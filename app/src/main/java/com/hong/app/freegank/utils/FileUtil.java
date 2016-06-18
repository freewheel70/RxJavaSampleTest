package com.hong.app.freegank.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.hong.app.freegank.FreeGankApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

        return saveSuccess;
    }

    public static boolean copyFile(String sourcePath, String targetPath) throws IOException {
        return copyFile(new File(sourcePath), new File(targetPath));
    }

    public static boolean copyFile(File sourceFile, File targetFile) {
        boolean saveSuccess = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);

            out = new FileOutputStream(targetFile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            saveSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            saveSuccess = false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
