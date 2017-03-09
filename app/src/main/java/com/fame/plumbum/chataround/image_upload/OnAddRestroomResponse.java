package com.fame.plumbum.chataround.image_upload;

import com.fame.plumbum.chataround.image_upload.model.data.AddRestroomData;

/**
 * Created by meghal on 7/3/17.
 */

public interface OnAddRestroomResponse {

    void onSuccess(AddRestroomData addRestroomData);

    void onFailure(String message);

}
