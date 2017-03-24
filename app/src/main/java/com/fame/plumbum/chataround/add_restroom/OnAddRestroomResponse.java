package com.fame.plumbum.chataround.add_restroom;

import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomData;

/**
 * Created by meghal on 7/3/17.
 */

public interface OnAddRestroomResponse {

    void onSuccess(AddRestroomData addRestroomData);

    void onFailure(String message);

}
