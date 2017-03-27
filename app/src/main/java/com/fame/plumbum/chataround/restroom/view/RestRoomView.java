package com.fame.plumbum.chataround.restroom.view;

import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;

import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public interface RestRoomView {


    void showLoader(boolean show);

    void showMessage(String message);

    void onReceived(List<RestRoomDetails> restRoomDetailsList);


}
