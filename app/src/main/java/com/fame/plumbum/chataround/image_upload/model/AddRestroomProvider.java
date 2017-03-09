package com.fame.plumbum.chataround.image_upload.model;

import com.fame.plumbum.chataround.image_upload.OnAddRestroomResponse;
import com.fame.plumbum.chataround.image_upload.model.data.AddRestroomRequestData;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddRestroomProvider {


    void addRestroom(AddRestroomRequestData addRestroomRequestData,
                     OnAddRestroomResponse onAddRestroomResponse)
            ;


}
