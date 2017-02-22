package com.fame.plumbum.chataround.pollution.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualValue;
import com.github.pavlospt.CircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by meghal on 23/2/17.
 */

public class PollutuionAqiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AirPollutionIndividualValue> valueList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    PollutuionAqiAdapter(Context context, List<AirPollutionIndividualValue> valueList) {
        this.valueList = valueList;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.aqi_item, parent, false);
        return new IndividualAqiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        IndividualAqiViewHolder individualAqiViewHolder = (IndividualAqiViewHolder) holder;
        AirPollutionIndividualValue value = valueList.get(position);

        individualAqiViewHolder.circleView.setTitleText(String.valueOf(value.getV()));
        individualAqiViewHolder.title.setText(value.getName());


    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

    public class IndividualAqiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circleView)
        CircleView circleView;
        @BindView(R.id.title)
        TextView title;

        public IndividualAqiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
