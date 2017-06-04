package com.fame.plumbum.chataround.add_photos.view;


import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddImageView {

    void showLoader(boolean show);

    void showMessage(String message);

    void onImageAdded(ImageUploadData imageUploadData);

}
