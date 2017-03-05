package com.fame.plumbum.chataround.restroom;

import com.fame.plumbum.chataround.restroom.model.RestRoomData;
import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;

import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public interface OnRestRoomApiResponse {


    void onSuccess(RestRoomData restRoomData);

    void onFailure(String message);
}
