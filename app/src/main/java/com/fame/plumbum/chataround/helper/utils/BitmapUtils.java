package com.fame.plumbum.chataround.helper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by Meghal on 5/25/2016.
 */
public class BitmapUtils {


    private static final String TAG = "BitmapUtils";

    public static Observable<Bitmap> filePathToBitmapObservable(final String filePath) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                try {
                    return Observable.just(filePathToBitmapConverter(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    public static Bitmap filePathToBitmapConverter(String filePath) throws IOException {

     /*   BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
       */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;  //Math.max(options.outWidth / width, options.outHeight / height);

        int rotate = 0;
        int orientation;
        try {

            File imageFile = new File(filePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.v(TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null || rotate == 0) {
            return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
/*
        bitmapObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                InputStream is = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(filePath, options);
                int height = options.outHeight;
                int width = options.outWidth;
                String imageType = options.outMimeType;

                BitmapFactory.Options options2 = new BitmapFactory.Options();
                //    Log.i(TAG, "Image Details before Conversion is : \n Height :" + height + "\nWidth : " + width + "\n Image Type : " + imageType);

                options2.inSampleSize = 2;  //Math.max(options.outWidth / width, options.outHeight / height);

                try {
                    is = new FileInputStream(filePath);
                    subscriber.onNext(BitmapFactory.decodeStream(is, null, options2));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    subscriber.onNext(BitmapFactory.decodeStream(is, null, options2));
                }
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });



        return bitmapObservable;
*/
