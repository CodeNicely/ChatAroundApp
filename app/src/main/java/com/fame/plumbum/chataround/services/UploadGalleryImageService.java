package com.fame.plumbum.chataround.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.add_photos.MyQueue;
import com.fame.plumbum.chataround.add_photos.UploadCallback;
import com.fame.plumbum.chataround.add_photos.model.ImageUploader;
import com.fame.plumbum.chataround.add_photos.model.RetrofitImageUploader;
import com.fame.plumbum.chataround.add_photos.model.data.ImageData;
import com.fame.plumbum.chataround.add_photos.model.data.PhotoEvent;
import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;
import com.fame.plumbum.chataround.helper.Keys;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * This class is used for uploading images to server using Service.
 * Created by Meghal on 5/30/2016.
 */
public class UploadGalleryImageService extends Service {

    private static final String TAG = "Upload Service";
    private MyQueue<ImageData> myQueue = new MyQueue<>();
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private boolean UploadImages = false;
    public static boolean ACTIVITY_DESTROYED = false;
    public static boolean QUEUE_EMPTY = true;

    private static final int DELAY = 6000;
    private static final int NOTIFICATION_ID = 1;
    private ImageUploader imageUploader;
    private SharedPrefs sharedPrefs;
    private String userMobile="";
    private double latitude;
    private double longitude;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        sharedPrefs = new SharedPrefs(this);
        imageUploader = new RetrofitImageUploader();
        Log.i(TAG, "Service Created");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        userMobile = intent.getExtras().getString(Keys.KEY_USER_MOBILE);
        latitude = intent.getExtras().getDouble(Keys.KEY_LATITUDE);
        longitude = intent.getExtras().getDouble(Keys.KEY_LONGITUDE);
        Log.i(TAG, "Service OnStartCommand");

        return START_REDELIVER_INTENT;
    }

    /**
     * Start to upload images one by one using Queue {@link MyQueue}
     */
    public void uploadImages() {

        Log.i(TAG, "Service UploadImages");


        if (UploadImages) {
            return;
        }
        if (myQueue == null || myQueue.isEmpty()) {

            QUEUE_EMPTY = true;
            if (ACTIVITY_DESTROYED) {
                stopSelf();
            }

            UploadImages = false;
            return;
        }
        QUEUE_EMPTY = false;
        uploadImage(myQueue.poll());
        UploadImages = true;
    }

    /**
     * This method is used to upload image and is called by method {@link UploadRestroomImageService#uploadImages()}
     * after getting a single image from Queue.
     *
     * @param imageData
     */
    private void uploadImage(final ImageData imageData) {

        Log.i(TAG, "Service uploadImage");

        imageUploader.uploadImage(sharedPrefs.getUserId(),
                userMobile,latitude,longitude, new File(imageData.getFile())
                , new UploadCallback() {



                    @Override
                    public void onUploadSuccess(ImageUploadData imageUploadData) {
                        if (imageUploadData.isSuccess()) {
                            Log.i(TAG, "Image Uploaded");

                            startForeground(NOTIFICATION_ID, showNotification());
                            UploadImages = false;
                            uploadImages();

                        } else {
                            showMessage("Photo uploading Failed " + imageUploadData.getMessage() +
                                    ". Please try again");

                        }

                    }

                    @Override
                    public void onUploadFailed() {
                        showMessage("Image uploading Failed ! Something is wrong in Front end");

                    }
                });


    /*    Observable.just(imageData).delay(DELAY, TimeUnit.MILLISECONDS).flatMap(new Func1<ImageData, Observable<ImageData>>() {
            @Override
            public Observable<ImageData> call(ImageData imageData) {

                Log.i(TAG,"Observable"+imageData.getFile());

                return Observable.just(imageData);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ImageData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ImageData imageData) {
*//*
                while (UPLOADING == 0) {

                    try {
                        Log.i(TAG, "Waiting");
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
*//*
                fileUploader.uploadImage(sharedPrefs.getAdminToken(),
                        sharedPrefs.getOrderId(), new File(imageData.getFile())
                        , new UploadCallback() {

                            @Override
                            public void onUploadSuccess(String message) {

                                Log.i(TAG, "Uploading Started");
                                startForeground(NOTIFICATION_ID, showNotification());
                                showMessage(imageData);
                                uploadImages = false;
                                uploadImages();

                            }

                            @Override
                            public void onUploadFailed() {

                            }
                        });

            }
        });
*/
    }

    /**
     * This method is used to show notification to the user after successful response from
     * {@link }
     *
     * @return
     */
    private Notification showNotification() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Photo Added Successfully")
                .setContentText("Photo has been added successfully by you and now we are waiting for Verification Process. It will be accepted within 48 hours")
                .setSmallIcon(R.drawable.ic_file_upload_white)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
        ;
// Start a lengthy operation in a background thread
   /*     new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        // Do the "lengthy" operation 20 times
                        for (int incr = 0; incr <= 100; incr += 1) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(PROGRESS_DELAY);
                                mBuilder.setContentText(incr + "% Complete");
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Upload complete").setOngoing(false)
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();*/

        return mBuilder.build();

    }

    /**
     * This method is used for showing toast message to the user.
     *
     * @param message is the message that is to be shown to user.
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onImageUploadEvent(PhotoEvent photoEvent) {

        ImageData imageData = new ImageData(photoEvent.getFile());
        myQueue.add(imageData);
        uploadImages();
        Log.i(TAG, "Service Yeah Added to Queue");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.i(TAG, "Service Destroyed");

    }
}
