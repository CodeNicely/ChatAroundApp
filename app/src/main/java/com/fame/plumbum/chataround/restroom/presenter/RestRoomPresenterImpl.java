package com.fame.plumbum.chataround.restroom.presenter;

import com.fame.plumbum.chataround.restroom.OnRestRoomApiResponse;
import com.fame.plumbum.chataround.restroom.model.RestRoomData;
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
    public void requestRestRooms(Double latitude, Double longitude) {
        restRoomView.showLoader(true);
        restRoomProvider.requestRestRooms(latitude, longitude, new OnRestRoomApiResponse() {
            @Override
            public void onSuccess(List<RestRoomData> restRoomDataList) {
                restRoomView.showLoader(false);
                restRoomView.onReceived(restRoomDataList);

            }

            @Override
            public void onFailure(String message) {
                restRoomView.showLoader(false);
                restRoomView.showMessage("Something Went Wrong");
            }
        });

    }
}
