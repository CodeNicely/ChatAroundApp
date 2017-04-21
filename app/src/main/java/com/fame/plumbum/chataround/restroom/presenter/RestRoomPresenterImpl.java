package com.fame.plumbum.chataround.restroom.presenter;

import com.fame.plumbum.chataround.restroom.OnRestRoomApiResponse;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;
import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;
import com.fame.plumbum.chataround.restroom.provider.RestRoomProvider;
import com.fame.plumbum.chataround.restroom.view.RestRoomView;

import java.util.List;

/**
 * Created by meghal on 19/2/17.
 */

public class RestRoomPresenterImpl implements RestRoomPresenter {


    private RestRoomView restRoomView;
    private RestRoomProvider restRoomProvider;

    public RestRoomPresenterImpl(RestRoomView restRoomView, RestRoomProvider restRoomProvider) {
        this.restRoomView = restRoomView;
        this.restRoomProvider = restRoomProvider;
    }

    @Override
    public void requestRestRooms(String user_id, double latitude, double longitude) {
        restRoomView.showLoader(true);
        restRoomProvider.requestRestRooms(user_id, latitude, longitude, new OnRestRoomApiResponse() {

            @Override
            public void onSuccess(RestRoomData restRoomData) {
                restRoomView.showLoader(false);
                if (restRoomData.isSuccess()) {
                    restRoomView.onReceived(restRoomData.getRestroom_list());
                    restRoomView.showLoader(false);
                } else {
                    restRoomView.showMessage(restRoomData.getMessage());
                    restRoomView.showLoader(false);
                }
            }

            @Override
            public void onFailure(String message) {
                restRoomView.showLoader(false);
                restRoomView.showMessage(message);
            }
        });

    }

    @Override
    public void onDestroy() {
        restRoomProvider.onDestroy();
    }
}
