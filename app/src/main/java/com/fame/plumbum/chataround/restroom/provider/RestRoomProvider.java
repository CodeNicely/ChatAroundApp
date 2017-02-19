package com.fame.plumbum.chataround.restroom.provider;

import com.fame.plumbum.chataround.restroom.OnRestRoomApiResponse;

/**
 * Created by meghal on 19/2/17.
 */

public interface RestRoomProvider {

    void requestRestRooms(Float latitude, Float langitude, OnRestRoomApiResponse onRestRoomApiResponse);


}
