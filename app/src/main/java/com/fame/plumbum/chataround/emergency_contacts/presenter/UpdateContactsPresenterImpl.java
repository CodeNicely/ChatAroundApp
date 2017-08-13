package com.fame.plumbum.chataround.emergency_contacts.presenter;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fame.plumbum.chataround.emergency_contacts.OnEmergencyContactsUpdateResponse;
import com.fame.plumbum.chataround.emergency_contacts.model.UpdateEmergencyContactsData;
import com.fame.plumbum.chataround.emergency_contacts.provider.EmergencyContactsUpdateProvider;
import com.fame.plumbum.chataround.emergency_contacts.view.EmergencyContactsView;
import com.fame.plumbum.chataround.helper.Keys;

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
    public void updateContactsList(final String userId, String contactList) {
        emergencyContactsView.showProgressDialog(true);
        emergencyContactsView.changeDialogMessage("Updating Contacts . . .");
        emergencyContactsUpdateProvider.updateContactsList(userId, contactList, new OnEmergencyContactsUpdateResponse() {
            @Override
            public void onSuccess(UpdateEmergencyContactsData updateEmergencyContactsData) {

                if (updateEmergencyContactsData.isSuccess()) {
                    emergencyContactsView.onEmergencyContactsUpdated();
                    Answers.getInstance().logCustom(new CustomEvent("Add Contacts Successfully")
                            .putCustomAttribute(Keys.KEY_USER_ID, userId));

                } else {
                    Answers.getInstance().logCustom(new CustomEvent("Add Contacts Failed")
                            .putCustomAttribute(Keys.KEY_USER_ID, userId));
                    emergencyContactsView.showMessage(updateEmergencyContactsData.getMessage());


                    Answers.getInstance().logCustom(new CustomEvent("Add Contacts Failed"+updateEmergencyContactsData.getMessage())
                            .putCustomAttribute(Keys.KEY_USER_ID, userId));

                }

                emergencyContactsView.showProgressDialog(false);
            }

            @Override
            public void onFailure(String message) {


                Answers.getInstance().logCustom(new CustomEvent("Add Contacts Failed")
                        .putCustomAttribute(Keys.KEY_USER_ID, userId));


                Answers.getInstance().logCustom(new CustomEvent("Add Contacts Failed"+message)
                        .putCustomAttribute(Keys.KEY_USER_ID, userId));
                emergencyContactsView.showProgressDialog(false);
                emergencyContactsView.showMessage(message);

            }
        });


    }

}