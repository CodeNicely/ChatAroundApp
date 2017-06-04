package com.fame.plumbum.chataround.add_photos;

import com.fame.plumbum.chataround.add_photos.model.data.ImageUploadData;

/**
 * Created by meghal on 7/3/17.
 */

public interface OnAddRestroomResponse {

    void onSuccess(ImageUploadData imageUploadData);

    void onFailure(String message);

}
