package com.fame.plumbum.chataround.emergency_contacts.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency.view.EmergencyFragment;
import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ramya on 2/7/17.
 */

public class EmergencyContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<EmergencyContactData> emergencyContactDataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SharedPrefs sharedPrefs;
    private EmergencyFragment emergencyFragment;
    private Context mContext;


    public EmergencyContactsAdapter(Context context) {
        this.mContext = context;
        this.sharedPrefs = new SharedPrefs(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.emergency_contacts_list_feed_item, parent, false);
        return new EmergencyContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final EmergencyContactData emergencyContactData = emergencyContactDataList.get(position);
        EmergencyContactsViewHolder emergencyContactsViewHolder = (EmergencyContactsViewHolder) holder;
        String contactName = emergencyContactData.getName();
        final String contactNumber = emergencyContactData.getMobile();
        // String contactImage = emergencyContactData.getContactImage();
        if (contactName != null && !contactName.isEmpty()) {
            emergencyContactsViewHolder.contactName.setText(emergencyContactData.getName());
        }
        if (contactNumber != null && !contactNumber.isEmpty()) {
            emergencyContactsViewHolder.contactNumber.setText(emergencyContactData.getMobile());
        }
        /*if (contactImage != null) {
            emergencyContactsViewHolder.contactsImage.setImageURI(Uri.parse(emergencyContactDataList.get(position).getContactImage()));
        }
*/
        emergencyContactsViewHolder.contactCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                emergencyContactData.setChecked(emergencyContactData.isChecked());
                emergencyContactDataList.get(position).setChecked(isChecked);
                if (mContext instanceof EmergencyContactsActivity) {
                    ((EmergencyContactsActivity) mContext).addContactToSelectedContactList(emergencyContactDataList.get(position));
                }

                /*EmergencyContactData emergencyContactData= emergencyContactDataList.get(position);
                emergencyFragment.requestContactsUpdate(sharedPrefs.getUserId(),emergencyContactData,isChecked,position);*/

            }
        });

        emergencyContactsViewHolder.contactCheckBox.setChecked(emergencyContactData.isChecked());

    }

    public void setEmergencyContactDataList(List<EmergencyContactData> list) {
        this.emergencyContactDataList = list;
    }


    @Override
    public int getItemCount() {
        return emergencyContactDataList.size();
    }

    public void addEmergencyContact(EmergencyContactData emergencyContactData) {

        emergencyContactDataList.add(emergencyContactData);

    }

    public class EmergencyContactsViewHolder extends RecyclerView.ViewHolder {
        /*@BindView(R.id.contacts_image)
        ImageView contactsImage;*/
        @BindView(R.id.contacts_name)
        TextView contactName;
        @BindView(R.id.contacts_number)
        TextView contactNumber;
        @BindView(R.id.select_contact_check_box)
        CheckBox contactCheckBox;

        public EmergencyContactsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
