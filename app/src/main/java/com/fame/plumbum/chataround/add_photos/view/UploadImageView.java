package com.fame.plumbum.chataround.add_photos.view;


import com.fame.plumbum.chataround.add_photos.model.data.ImageData;

import java.util.List;

/**
 * This interface is for View part of module UploadImage
 * <p>
 * Created by Meghal on 5/24/2016.
 */
public interface UploadImageView {

    /**
     * This function is used to set data to adapter
     *
     * @param imageDataList is List of {@link ImageData} to be passed to adapter class
     *                      to show in recycler view
     */

    /**
     * This method is called when user chooses to open camera.
     */
    void showCamera();

    /**
     * This method is called when user chooses to open gallery.
     */

    void showGallery();

    /**
     * This method is used to get file from file path.
     * This method changes variable {@link AddImageActivity#image} with new file.
     *
     * @param filePath is path of file
     */
    void fileFromPath(String filePath);

    /**
     * This method is for checking camera permission.
     * Applicable only for devices with Api 23 or more.
     *
     * @return
     */
    boolean checkPermissionForCamera();

    /**
     * This method is for checking gallery permission.
     * Applicable only for devices with api 23 or more.
     *
     * @return
     */
    boolean checkPermissionForGallery();

    /**
     * This function is for requesting camera permission if user does'nt have taken permission
     * previously.
     *
     * @return
     */
    boolean requestCameraPermission();

    /**
     * This function is for requesting gallery permission if user does'nt have taken permission
     * previously.
     *
     * @return
     */
    boolean requestGalleryPermission();

    void setData(List<ImageData> imageDataList);
}
