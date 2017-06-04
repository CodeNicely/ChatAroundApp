package com.fame.plumbum.chataround.add_photos;


import com.fame.plumbum.chataround.add_photos.api.ImageUploadApi;
import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;
import com.fame.plumbum.chataround.add_photos.presenter.ImagePresenter;

/**
 * This interface acts as a callback for module upload images
 * Created by Meghal on 5/27/2016.
 */
public interface UploadCallback {

    /**
     * this method is for notifying presenter about status of api call
     * if the api call is successful then this method is called and response data is passed to
     * {@link ImagePresenter}
     *
     * @param imageUploadData this is POJO for resonse data of {@link ImageUploadApi}
     */
    void onUploadSuccess(ImageUploadData imageUploadData);

    /**
     * This method is called when api call is failed due to android side issue
     */
    void onUploadFailed();


}
