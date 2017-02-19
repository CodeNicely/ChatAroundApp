package com.fame.plumbum.chataround.restroom;

import com.fame.plumbum.chataround.restroom.model.RestRoomData;

import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public interface OnRestRoomApiResponse {


    void onSuccess(List<RestRoomData> restRoomDataList);

    void onFailure(String message);
}
