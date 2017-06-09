package com.fame.plumbum.chataround.add_restroom.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.add_restroom.OnAddRestroomResponse;
import com.fame.plumbum.chataround.add_restroom.model.AddRestroomProvider;
import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomData;
import com.fame.plumbum.chataround.add_restroom.model.data.AddRestroomRequestData;
import com.fame.plumbum.chataround.add_restroom.view.AddRestroomView;
import com.fame.plumbum.chataround.helper.Keys;

/**
 * Created by meghal on 6/3/17.
 */

public class AddRestroomPresenterImpl implements AddRestroomPresenter {

    private AddRestroomView addRestroomView;
    private AddRestroomProvider addRestroomProvider;

    public AddRestroomPresenterImpl(AddRestroomView addRestroomView, AddRestroomProvider addRestroomProvider) {
        this.addRestroomView = addRestroomView;
        this.addRestroomProvider = addRestroomProvider;
    }

    @Override
    public void addRestroom(final AddRestroomRequestData addRestroomRequestData) {

        addRestroomView.showLoader(true);

        addRestroomProvider.addRestroom(addRestroomRequestData, new OnAddRestroomResponse() {
            @Override
            public void onSuccess(AddRestroomData addRestroomData) {

                if (addRestroomData.isSuccess()) {
                    if(addRestroomData.getRestroom_id()!=null) {
                        addRestroomView.onRestroomAdded(addRestroomData);
                        addRestroomView.showMessage(addRestroomData.getMessage());

                        Answers.getInstance().logCustom(new CustomEvent("Add Restroom Successful")
                                .putCustomAttribute(Keys.KEY_CITY, addRestroomRequestData.getCity())
                                .putCustomAttribute(Keys.KEY_COUNTRY, addRestroomRequestData.getCountry())
                                .putCustomAttribute(Keys.KEY_STATE, addRestroomRequestData.getState())
                                .putCustomAttribute(Keys.KEY_LATITUDE, addRestroomRequestData.getLangitude())
                                .putCustomAttribute(Keys.KEY_LONGITUDE, addRestroomRequestData.getLangitude())

                        );
                    }else{
                        addRestroomView.onRestroomAdded(addRestroomData);
                        addRestroomView.showMessage(addRestroomData.getMessage());

                        Answers.getInstance().logCustom(new CustomEvent("Add Restroom within 20 meters")
                                .putCustomAttribute(Keys.KEY_CITY, addRestroomRequestData.getCity())
                                .putCustomAttribute(Keys.KEY_COUNTRY, addRestroomRequestData.getCountry())
                                .putCustomAttribute(Keys.KEY_STATE, addRestroomRequestData.getState())
                                .putCustomAttribute(Keys.KEY_LATITUDE, addRestroomRequestData.getLangitude())
                                .putCustomAttribute(Keys.KEY_LONGITUDE, addRestroomRequestData.getLangitude()));

                    }

                } else {
                    addRestroomView.showMessage(addRestroomData.getMessage());

                    Answers.getInstance().logCustom(new CustomEvent("Add Restroom Failed")
                            .putCustomAttribute(Keys.KEY_CITY,addRestroomRequestData.getCity())
                            .putCustomAttribute(Keys.KEY_COUNTRY,addRestroomRequestData.getCountry())
                            .putCustomAttribute(Keys.KEY_STATE,addRestroomRequestData.getState())
                            .putCustomAttribute(Keys.KEY_LATITUDE,addRestroomRequestData.getLangitude())
                            .putCustomAttribute(Keys.KEY_LONGITUDE,addRestroomRequestData.getLangitude())

                    );
                }
                addRestroomView.showLoader(false);

            }

            @Override
            public void onFailure(String message) {
                addRestroomView.showLoader(false);
                addRestroomView.showMessage(message);
            }
        });


    }
}
