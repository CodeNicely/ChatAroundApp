package com.fame.plumbum.chataround.add_restroom.model;

import com.fame.plumbum.chataround.add_restroom.OnAddRestroomResponse;
import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomRequestData;

/**
 * Created by meghal on 6/3/17.
 */

public interface AddRestroomProvider {


    void addRestroom(AddRestroomRequestData addRestroomRequestData,
                     OnAddRestroomResponse onAddRestroomResponse)
            ;


}
