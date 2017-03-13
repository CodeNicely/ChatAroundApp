package com.fame.plumbum.chataround.pollution.view;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualValue;

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
    private Context context;

    PollutuionAqiAdapter(Context context, List<AirPollutionIndividualValue> valueList) {
        this.valueList = valueList;

        for (int i = 0; i < valueList.size(); i++) {

            Log.d("PollutionFragment", String.valueOf(valueList.get(i).getName()));
            Log.d("PollutionFragment", String.valueOf(valueList.get(i).getV()));

        }


        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.aqi_item, parent, false);
        return new IndividualAqiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        IndividualAqiViewHolder individualAqiViewHolder = (IndividualAqiViewHolder) holder;
        final AirPollutionIndividualValue value = valueList.get(position);


        // Setting Color

        Log.i("Adapter", value.getName() + "  " + String.valueOf(value.getV()));


        // Setting Up Values
        if (valueList.get(position).getV() != -9999) {
//            individualAqiViewHolder.circleView.setTitleText(String.valueOf(value.getV()));
//            individualAqiViewHolder.title.setText(value.getName());

            individualAqiViewHolder.aqi.setText(value.getName() + " - - Value - - " + String.valueOf(value.getV()));

//            individualAqiViewHolder.circleView.setFillColor(value.getColor());
//            individualAqiViewHolder.circleView.setBackgroundColor(value.getColor());
//            individualAqiViewHolder.title.setBackgroundColor(value.getColor());


            individualAqiViewHolder.discreteSeekBar.setMax(Math.round(value.getValue6()));
            individualAqiViewHolder.discreteSeekBar.setProgress((int) value.getV());

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                            new int[]{
                                    0xBB00E400, 0xBBFFFF00, 0xBBFF7E00, 0xBBFF0000,
                                    0xBB8F3F97, 0xBB7E0023
                            }, //substitute the correct colors for these
                            new float[]{
                                    value.getValue1() / value.getValue6(),
                                    value.getValue2() / value.getValue6(),
                                    value.getValue3() / value.getValue6(),
                                    value.getValue4() / value.getValue6(),
                                    value.getValue5() / value.getValue6(),
                                    value.getValue6() / value.getValue6()},
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);
/*

            LinearGradient test = new LinearGradient(100.f, 100.f, 300.f, 0.0f,

                    new int[] { R.color.good, R.color.moderate, R.color.sensitive,R.color.unhealthy,
                            R.color.very_unhealthy, R.color.hazardous},
                    null, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(test);
*/

            individualAqiViewHolder.discreteSeekBar.setProgressDrawable(paint);
            individualAqiViewHolder.discreteSeekBar.setEnabled(false);


        } else {
//            individualAqiViewHolder.circleView.setTitleText("NA");
//            individualAqiViewHolder.title.setText(value.getName());

            individualAqiViewHolder.aqi.setText(value.getName() + " - - Value - - " + "NA");

//            individualAqiViewHolder.circleView.setFillColor(ContextCompat.getColor(context, R.color.accentGray));
//            individualAqiViewHolder.circleView.setBackgroundColor(ContextCompat.getColor(context, R.color.accentGray));
//            individualAqiViewHolder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.accentGray));

        }


    }

    @Override
    public int getItemCount() {


        return valueList.size();
    }

    public class IndividualAqiViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.seek_bar)
        SeekBar discreteSeekBar;

        @BindView(R.id.aqi)
        TextView aqi;

        public IndividualAqiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
