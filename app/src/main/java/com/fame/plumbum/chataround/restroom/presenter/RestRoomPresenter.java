package com.fame.plumbum.chataround.restroom.presenter;

/**
 * Created by meghal on 19/2/17.
 */

public interface RestRoomPresenter {

    void requestRestRooms(String user_id,double latitude, double langitude);

    void onDestroy();

}
