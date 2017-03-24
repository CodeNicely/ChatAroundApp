package com.fame.plumbum.chataround.add_restroom.view;

import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomData;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddRestroomView {

    void showLoader(boolean show);

    void showMessage(String message);

    void onRestroomAdded(AddRestroomData addRestroomData);

}
