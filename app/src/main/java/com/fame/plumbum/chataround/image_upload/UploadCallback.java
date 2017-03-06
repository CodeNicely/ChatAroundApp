package com.fame.plumbum.chataround.image_upload;


import com.fame.plumbum.chataround.image_upload.api.ImageUploadApi;
import com.fame.plumbum.chataround.image_upload.model.data.FileUploadData;
import com.fame.plumbum.chataround.image_upload.presenter.ImagePresenter;

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
     * @param fileUploadData this is POJO for resonse data of {@link ImageUploadApi}
     */
    void onUploadSuccess(FileUploadData fileUploadData);

    /**
     * This method is called when api call is failed due to android side issue
     */
    void onUploadFailed();


}
