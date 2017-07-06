package com.fame.plumbum.chataround.emergency.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.emergency.model.data.EmergencyContactsFeed;
import com.fame.plumbum.chataround.helper.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ramya on 2/7/17.
 */

public class EmergencyContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EmergencyContactsFeed> emergencyContactsFeedList= new ArrayList<>();
    private Context context;
    private EmergencyContactsFeed emergencyContactsFeed;
    private LayoutInflater layoutInflater;
    private SharedPrefs sharedPrefs;
    private EmergencyFragment emergencyFragment;

    public EmergencyContactsAdapter(Context context){
        this.context=context;

        this.sharedPrefs=new SharedPrefs(context);
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.contacts_feed_item, parent, false);
        return new EmergencyContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        emergencyContactsFeed=emergencyContactsFeedList.get(position);
        EmergencyContactsViewHolder emergencyContactsViewHolder=(EmergencyContactsViewHolder)holder;
        String contactName=emergencyContactsFeed.getContactName();
        String contactNumber=emergencyContactsFeed.getContactNumber();
        String contactImage=emergencyContactsFeed.getContactImage();
        if(contactName!=null&&!contactName.isEmpty())
        {
            emergencyContactsViewHolder.contactName.setText(emergencyContactsFeed.getContactName());
        }
        if(contactNumber!=null&&!contactNumber.isEmpty()){
            emergencyContactsViewHolder.contactNumber.setText(emergencyContactsFeed.getContactNumber());
        }
        if(contactImage!=null){
            emergencyContactsViewHolder.contactsImage.setImageURI(Uri.parse(contactImage));
        }
        emergencyContactsViewHolder.contactCheckBox.setChecked(emergencyContactsFeed.isChecked());

        emergencyContactsViewHolder.contactCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EmergencyContactsFeed emergencyContactsFeed= emergencyContactsFeedList.get(position);
                emergencyFragment.requestContactsUpdate(sharedPrefs.getUserId(),emergencyContactsFeed,isChecked,position);

            }
        });
    }

    public void setEmergencyContactsFeedList(List<EmergencyContactsFeed> list){
        this.emergencyContactsFeedList=list;
    }
    @Override
    public int getItemCount() {
        return emergencyContactsFeedList.size();
    }

    public class EmergencyContactsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contacts_image)
        ImageView contactsImage;
        @BindView(R.id.contacts_name)
        TextView contactName;
        @BindView(R.id.contacts_number)
        TextView contactNumber;
        @BindView(R.id.select_contact_check_box)
        CheckBox contactCheckBox;
        public EmergencyContactsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
