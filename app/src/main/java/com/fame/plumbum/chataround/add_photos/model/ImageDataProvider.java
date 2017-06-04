package com.fame.plumbum.chataround.add_photos.model;

import android.net.Uri;

import com.fame.plumbum.chataround.add_photos.model.data.ImageData;

import rx.Observable;

/**
 * This interface contains the functions to provide imageData
 * Created by Meghal on 5/24/2016.
 */
public interface ImageDataProvider {

    /**
     * @param uri is Uri as received onActivity result from view i.e when camera is turned on or when
     *            images are selected from gallery
     * @return Returns Observable<ImageData> so that in presenter we can subscribe.
     */
    Observable<ImageData> getImageData(Uri uri);
}
