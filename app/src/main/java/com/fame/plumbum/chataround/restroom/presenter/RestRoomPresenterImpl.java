package com.fame.plumbum.chataround.restroom.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.helper.Keys;
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
    public void requestRestRooms(final String user_id, final double latitude, final double longitude) {
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
                    Answers.getInstance().logCustom(new CustomEvent("Restroom Module Loading Failed - Server end")
                            .putCustomAttribute(Keys.USER_EMAIL, user_id)
                            .putCustomAttribute(Keys.KEY_LATITUDE,latitude)
                            .putCustomAttribute(Keys.KEY_LONGITUDE,longitude)

                    );

                }
            }

            @Override
            public void onFailure(String message) {
                Answers.getInstance().logCustom(new CustomEvent("Restroom Module Loading Failed - Local")
                        .putCustomAttribute(Keys.USER_EMAIL, user_id)
                        .putCustomAttribute(Keys.KEY_LATITUDE,latitude)
                        .putCustomAttribute(Keys.KEY_LONGITUDE,longitude)

                );
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
