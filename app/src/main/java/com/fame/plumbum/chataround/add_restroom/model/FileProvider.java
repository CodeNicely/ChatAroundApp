package com.fame.plumbum.chataround.add_restroom.model;

import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This is a Provider class and it provides new File.
 * Created by Meghal on 6/8/2016.
 */
public class FileProvider {

    /**
     * The function provides a new file and it provides file name on the basis of timestamp so
     * confirmaing that every file name is unique.
     *
     * @return
     */
    public static File requestNewFile() {

        File file;
        final String dir = Environment.getExternalStorageDirectory() + "/Folder/";
        File newdir = new File(dir);
        if (!newdir.exists()) {
            newdir.mkdirs();
        }
        String file1 = DateFormat.format("yyyy-mm-dd-hh-mm-ss", new Date()).toString() + ".jpg";

        file = new File(newdir, file1);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}
