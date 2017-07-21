package com.fame.plumbum.chataround.emergency_contacts.presenter;

import com.fame.plumbum.chataround.emergency_contacts.OnEmergencyContactsUpdateResponse;
import com.fame.plumbum.chataround.emergency_contacts.model.UpdateEmergencyContactsData;
import com.fame.plumbum.chataround.emergency_contacts.provider.EmergencyContactsUpdateProvider;
import com.fame.plumbum.chataround.emergency_contacts.view.EmergencyContactsView;

/**
 * Created by ramya on 5/7/17.
 */

public class UpdateContactsPresenterImpl implements UpdateContactsPresenter {

    private EmergencyContactsView emergencyContactsView;
    private EmergencyContactsUpdateProvider emergencyContactsUpdateProvider;

    public UpdateContactsPresenterImpl(EmergencyContactsView emergencyContactsView, EmergencyContactsUpdateProvider emergencyContactsUpdateProvider) {
        this.emergencyContactsView = emergencyContactsView;
        this.emergencyContactsUpdateProvider = emergencyContactsUpdateProvider;
    }

    @Override
    public void updateContactsList(String userId, String contactList) {
        emergencyContactsView.showProgressDialog(true);
        emergencyContactsView.changeDialogMessage("Updating Contacts . . .");
        emergencyContactsUpdateProvider.updateContactsList(userId, contactList, new OnEmergencyContactsUpdateResponse() {
            @Override
            public void onSuccess(UpdateEmergencyContactsData updateEmergencyContactsData) {

                if (updateEmergencyContactsData.isSuccess()) {
                    emergencyContactsView.onEmergencyContactsUpdated();

                } else {
                    emergencyContactsView.showMessage(updateEmergencyContactsData.getMessage());
                }

                emergencyContactsView.showProgressDialog(false);
            }

            @Override
            public void onFailure(String message) {

                emergencyContactsView.showProgressDialog(false);
                emergencyContactsView.showMessage(message);

            }
        });


    }

}