package com.fame.plumbum.chataround.add_photos.presenter;

import android.net.Uri;
import android.util.Log;

import com.fame.plumbum.chataround.add_photos.model.FileProvider;
import com.fame.plumbum.chataround.add_photos.model.ImageDataProvider;
import com.fame.plumbum.chataround.add_photos.model.data.ImageData;
import com.fame.plumbum.chataround.add_photos.model.data.PhotoEvent;
import com.fame.plumbum.chataround.add_photos.view.UploadImageView;
import com.fame.plumbum.chataround.helper.RxSchedulersHook;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by Meghal on 5/24/2016.
 */
public class ImagePresenterImpl implements ImagePresenter {

    private static final String TAG = "ImagePresenterImpl";
    private UploadImageView uploadImageView;
    private ImageDataProvider imageDataProvider;
    private RxSchedulersHook rxSchedulersHook;
    private EventBus eventBus;

    public ImagePresenterImpl(UploadImageView uploadImageView, ImageDataProvider imageDataProvider, RxSchedulersHook rxSchedulersHook, EventBus eventBus) {

        this.imageDataProvider = imageDataProvider;
        this.uploadImageView = uploadImageView;
        this.rxSchedulersHook = rxSchedulersHook;
        this.eventBus = eventBus;
    }


    @Override
    public void onImagesUpload(final List<Uri> imageUriList) {


        Observable.from(imageUriList).flatMap(new Func1<Uri, Observable<ImageData>>() {
            @Override
            public Observable<ImageData> call(Uri uri) {
                return imageDataProvider.getImageData(uri);
            }
        }).observeOn(rxSchedulersHook.getMainThreadScheduler()).subscribeOn(rxSchedulersHook.getIOScheduler())
                .subscribe(new Observer<ImageData>() {
                               @Override
                               public void onCompleted() {



                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ImageData imageData) {

                                   Log.d(TAG,"On next onImagesUpload");
                                   eventBus.post(new PhotoEvent(imageData.getFile()));
                                   //             EventBus.getDefault().post(String.valueOf(imageData.getFile()));
//                                   imageDataList.add(imageData);
                               }
                           }
                );
    }


    @Override
    public void onImagesSelected(final List<Uri> imageUriList) {

//        if (imageDataList.size() != 0) {
//
//            imageDataList.remove(imageDataList.size() - 1);
//        }
        final List<ImageData> imageDataList = new ArrayList<>();

        Observable.from(imageUriList).flatMap(new Func1<Uri, Observable<ImageData>>() {
            @Override
            public Observable<ImageData> call(Uri uri) {
                return imageDataProvider.getImageData(uri);
            }
        }).observeOn(rxSchedulersHook.getMainThreadScheduler()).subscribeOn(rxSchedulersHook.getIOScheduler())
                .subscribe(new Observer<ImageData>() {
                               @Override
                               public void onCompleted() {

                                   imageDataList.add(new ImageData(null, true));

                                   uploadImageView.setData(imageDataList);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ImageData imageData) {


//                                   eventBus.post(new PhotoEvent(imageData.getFile()));
                                   //             EventBus.getDefault().post(String.valueOf(imageData.getFile()));

                                   imageDataList.add(imageData);
                               }
                           }
                );
    }


    @Override
    public void openCamera() {
        File image = FileProvider.requestNewFile();

        if (uploadImageView.checkPermissionForCamera()) {

            uploadImageView.fileFromPath(image.getPath());
            uploadImageView.showCamera();
        } else {
            if (uploadImageView.requestCameraPermission()) {

                uploadImageView.fileFromPath(image.getPath());
                uploadImageView.showCamera();

            }
        }
    }

    @Override
    public void openGallery() {

        if (uploadImageView.checkPermissionForGallery()) {
            uploadImageView.showGallery();
        } else {

            if (uploadImageView.requestGalleryPermission()) {
                uploadImageView.showGallery();
            }
        }

    }

/*    @Override
    public List<ImageData> getImageDataList() {
        return imageDataList;
    }

    @Override
    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }*/
}
/*
  observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ImageData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
            }

            @Override
            public void onNext(ImageData imageData) {

                Log.i(TAG,"onNext Called");
                imageDataList.add(imageData);
                uploadImageView.setData(imageDataList);

            }
        });



 */
