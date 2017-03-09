package com.fame.plumbum.chataround.image_upload.presenter;

import android.net.Uri;

import com.fame.plumbum.chataround.image_upload.model.data.ImageData;

import java.util.List;

/**
 * This is interface for presenter of Upload Image Module
 * This class contains all necessary methods that are necessary for View to interact with presenter.
 * Created by Meghal on 5/24/2016.
 */
public interface ImagePresenter {

    /**
     * This method is called just after images are selected.
     * We have to override this method to define code lines to be executed after selection of images.
     *
     * @param imageUriList is list of imageUri
     */
    void onImagesSelected(List<Uri> imageUriList);

    void onImagesUpload(List<Uri> imageUriList);

    /**
     * This function is called to open camera for clicking new image
     */
    void openCamera();

    /**
     * This function s called from view if user chooses to select images already present in gallery
     */
    void openGallery();

    /**
     * This is important method where View demands for imageData list
     * {@link ImageData} is a POJO that contains image data i.e file and fileName
     *
     * @return this method returns a list of imageDataListfor further processing.
     */
    List<ImageData> getImageDataList();

    /**
     * This method is setter for imageDataList , This method sets a new value to imageDataList variable
     * in {@link ImagePresenter}
     *
     * @param imageDataList is a list of {@link ImageData}
     */
    void setImageDataList(List<ImageData> imageDataList);


}
