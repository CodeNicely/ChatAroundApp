package com.fame.plumbum.chataround.image_upload.model;


import com.fame.plumbum.chataround.image_upload.UploadCallback;
import com.fame.plumbum.chataround.image_upload.api.ImageUploadApi;

import java.io.File;

/**
 * This interface contains necessary functions for calling {@link ImageUploadApi}
 * Created by Meghal on 5/27/2016.
 */
public interface FileUploader {

    /**
     * @param user_id     is unique AdminToken for admin verification
     * @param restroom_id        is orderId to which we have to attach the image file
     * @param file           is file to be uploaded
     * @param uploadCallback is an interface that is used as Callback to notify presenter when
     *                       response from api call is ready or when api call is failed.
     */
    void uploadImage(String user_id, String restroom_id, File file, UploadCallback uploadCallback);

}
