package com.fame.plumbum.chataround.gallery.view;

import com.fame.plumbum.chataround.gallery.model.ImageData;

import java.util.List;

/**
 * Created by meghalagrawal on 03/06/17.
 */

public interface GalleryView {


    void showLoader(boolean show);
    void onGalleryData(List<ImageData> imageDataList);
    void showMessage(String message);
}
