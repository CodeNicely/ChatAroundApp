package com.fame.plumbum.chataround.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Meghal on 5/26/2016.
 */
public class FileUtils {

    private static final String LOG = "Save Image to File";

    public static File BitmapToFileConverter(Context context, Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(context.getCacheDir(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            destination = null;
            e.printStackTrace();
        } catch (IOException e) {
            destination = null;
            e.printStackTrace();
        }
        return destination;
    }
}
