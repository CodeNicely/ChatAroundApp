package com.fame.plumbum.chataround.image_upload.view;

import com.fame.plumbum.chataround.image_upload.model.data.AddRestroomData;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddRestroomView {

    void showLoader(boolean show);

    void showMessage(String message);

    void onRestroomAdded(AddRestroomData addRestroomData);

}
