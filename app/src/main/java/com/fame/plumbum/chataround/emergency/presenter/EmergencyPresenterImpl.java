package com.fame.plumbum.chataround.emergency.presenter;

import com.fame.plumbum.chataround.emergency.EmergencyContactsCallback;
import com.fame.plumbum.chataround.emergency.OnEmergencyContactDeleted;
import com.fame.plumbum.chataround.emergency.OnSosStartResponse;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactDeleteData;
import com.fame.plumbum.chataround.emergency.model.EmergencyContactsListData;
import com.fame.plumbum.chataround.emergency.model.StartSosData;
import com.fame.plumbum.chataround.emergency.provider.EmergencyProvider;
import com.fame.plumbum.chataround.emergency.view.EmergencyView;

/**
 * Created by ramya on 3/7/17.
 */

public class EmergencyPresenterImpl implements EmergencyPresenter {
    private EmergencyView emergencyView;
    private EmergencyProvider emergencyProvider;


    public EmergencyPresenterImpl(EmergencyView emergencyView, EmergencyProvider emergencyProvider) {
        this.emergencyView = emergencyView;
        this.emergencyProvider = emergencyProvider;
    }

    @Override
    public void getContacts(String userId) {
        emergencyView.showLoader(true);
        emergencyProvider.getEmergencyContacts(userId, new EmergencyContactsCallback() {
            @Override
            public void onSuccess(EmergencyContactsListData emergencyContactsListData) {
                if (emergencyContactsListData.isSuccess()) {
                    emergencyView.onEmergencyContactsRecieved(emergencyContactsListData.getContactList());

                } else {
                    emergencyView.showMessage(emergencyContactsListData.getMessage());
                }

                emergencyView.showLoader(false);

            }

            @Override
            public void onFailure(String message) {
                emergencyView.showMessage(message);
                emergencyView.showLoader(false);

            }
        });

    }

    @Override
    public void startSos(String userId, double latitude, double longitude) {
        emergencyView.changeDialogMessage("Enabling SOS","Please wait . . .");
        emergencyView.showProgressDialog(true);
        emergencyProvider.startSos(userId, latitude, longitude, new OnSosStartResponse() {
            @Override
            public void onSuccess(StartSosData startSosData) {
                if (startSosData.isSuccess()) {
                    emergencyView.onSosStarted(startSosData);
                } else {
                    emergencyView.showMessage(startSosData.getMessage());
                }

                emergencyView.showProgressDialog(false);
            }

            @Override
            public void onFailed(String message) {

                emergencyView.showMessage(message);

                emergencyView.showProgressDialog(false);

            }
        });

    }

    @Override
    public void deleteContact(String userId, String mobile) {

        emergencyView.showProgressDialog(true);
        emergencyProvider.deleteContact(userId, mobile, new OnEmergencyContactDeleted() {
            @Override
            public void onSuccess(EmergencyContactDeleteData emergencyContactDeleteData) {

                if(emergencyContactDeleteData.isSuccess()){
                    emergencyView.onContactDeleted();
                }else{
                    emergencyView.showMessage(emergencyContactDeleteData.getMessage());
                }

                emergencyView.showProgressDialog(false);
            }

            @Override
            public void onFailure(String message) {

                emergencyView.showMessage(message);
                emergencyView.showProgressDialog(false);

            }
        });


    }
}