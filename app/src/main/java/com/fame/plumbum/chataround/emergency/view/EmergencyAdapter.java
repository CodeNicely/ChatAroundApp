package com.fame.plumbum.chataround.emergency.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.emergency_contacts.model.EmergencyContactData;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ramya on 2/7/17.
 */

public class EmergencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<EmergencyContactData> emergencyContactDataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SharedPrefs sharedPrefs;
    private EmergencyFragment emergencyFragment;
    private Context mContext;


    public EmergencyAdapter(Context context, EmergencyFragment emergencyFragment) {
        this.emergencyFragment=emergencyFragment;
        this.mContext = context;
        this.sharedPrefs = new SharedPrefs(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.emergency_selected_contacts_list, parent, false);
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


        emergencyContactsViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emergencyFragment.deleteContact(emergencyContactDataList.get(position).getMobile());

            }
        });

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

        @BindView(R.id.delete)
        ImageView delete;


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
