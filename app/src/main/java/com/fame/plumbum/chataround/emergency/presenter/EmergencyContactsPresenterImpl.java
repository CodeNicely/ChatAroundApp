package com.fame.plumbum.chataround.emergency.presenter;

import android.content.Context;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsProvider;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsProviderImpl;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeedData;
import com.fame.plumbum.chataround.emergency.view.EmergencyContactsView;

import java.util.List;

/**
 * Created by ramya on 3/7/17.
 */

public class EmergencyContactsPresenterImpl implements EmergencyContactsPresenter{
    private EmergencyContactsView emergencyContactsView;
    private EmergencyContactsProvider emergencyContactsProvider;

    public EmergencyContactsPresenterImpl(EmergencyContactsView emergencyContactsView){
        this.emergencyContactsView=emergencyContactsView;
        this.emergencyContactsProvider= new EmergencyContactsProviderImpl();

    }
    @Override
    public void getContacts(String userId) {
        emergencyContactsView.showLoader(true);
        emergencyContactsProvider.getEmergencyContacts(userId,new EmergencyContactsCallback() {
            @Override
            public void onSuccess(EmergencyContactsFeedData emergencyContactsFeedData) {
                emergencyContactsView.showLoader(false);
                if(emergencyContactsFeedData.isSuccess()&&emergencyContactsFeedData.getEmergencyContactsFeedList().size()>0){
                    emergencyContactsView.onEmergencyContactsRecieved(emergencyContactsFeedData.getEmergencyContactsFeedList());

                }
                else {
                    emergencyContactsView.showMessage("Unfortunately something went wrong..Try Again!");
                }
            }

            @Override
            public void onFailure(String message) {
                emergencyContactsView.showMessage(message);
            }
        });

    }
}
