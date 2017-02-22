package com.fame.plumbum.chataround.pollution.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualAqi;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualValue;
import com.fame.plumbum.chataround.pollution.presenter.PollutionPresenterImpl;
import com.fame.plumbum.chataround.pollution.provider.RetrofitPollutionProvider;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.pavlospt.CircleView;
import com.ldoublem.ringPregressLibrary.OnSelectRing;
import com.ldoublem.ringPregressLibrary.Ring;
import com.ldoublem.ringPregressLibrary.RingProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.endColor;
import static android.R.attr.startColor;
import static android.R.id.progress;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PollutionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PollutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PollutionFragment extends Fragment implements PollutionView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.aqi)
    TextView aqi;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.ring_progress)
    RingProgress mRingProgress;

    @BindView(R.id.barChart)
    BarChart barChart;

    @BindView(R.id.horizontalBarChart)
    HorizontalBarChart horizontalBarChart;

    @BindView(R.id.circleView)
    CircleView circleView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public PollutionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PollutionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PollutionFragment newInstance(String param1, String param2) {
        PollutionFragment fragment = new PollutionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pollution, container, false);
        ButterKnife.bind(this, view);

        double latitude = 19.130306;
        double longitude = 72.889993;

        new PollutionPresenterImpl(this, new RetrofitPollutionProvider()).requestAirPollution(latitude, longitude);


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showLoader(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setData(AirPollutionDetails airPollutionDetails) {

        aqi.setText("Aqi Index : ");
        aqi.append(String.valueOf(airPollutionDetails.getData().getAqi()));

        ////////////////////////////////////////////////////

        circleView.setTitleText(String.valueOf(airPollutionDetails.getData().getAqi()) + "\n" + "AQI");


        List<AirPollutionIndividualValue> airPollutionIndividualValueList
                = getIndividualAqiList(airPollutionDetails.getData().getIaqi());



        ////////////////////////////////////////////////////

        List<BarEntry> barEntryList = new ArrayList<BarEntry>();

        BarEntry barEntry = new BarEntry(12, 12, "This is Label");

        BarEntry barEntry1 = new BarEntry(30, 120);

        barEntryList.add(barEntry);
        barEntryList.add(barEntry1);


        BarDataSet barDataSet = new BarDataSet(barEntryList, "Label");
        barDataSet.setColor(R.color.colorAccent);
        barDataSet.setValueTextColor(R.color.colorPrimary);

        BarData barData = new BarData(barDataSet);

        horizontalBarChart.setData(barData);
        horizontalBarChart.invalidate();

        barChart.setData(barData);
        barChart.invalidate();


        /////////////////////////////////////////////////////

        mRingProgress.setSweepAngle(360);
        mRingProgress.setDrawBg(true, Color.rgb(168, 168, 168));
//        mRingProgress.setDrawBgShadow(true, Color.argb(100, 235, 79, 56));
        mRingProgress.setCorner(true);
        mRingProgress.setOnSelectRing(new OnSelectRing() {
            @Override
            public void Selected(Ring r) {

            }
        });
        Ring r = new Ring(10, "Aqi Index : " + String.valueOf(54), "Title", R.color.colorAccent, R.color.colorPrimary);

        List<Ring> mlistRing = new ArrayList<>();
        mlistRing.add(r);

        mRingProgress.setData(mlistRing, 1000);// if >0 animation ==0 null

    }


    List<AirPollutionIndividualValue> getIndividualAqiList(AirPollutionIndividualAqi airPollutionIndividualAqi) {

        List<AirPollutionIndividualValue> airPollutionIndividualValueList = new ArrayList<>();

        if (airPollutionIndividualAqi.getCo() != null) {
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getCo().getV(), "CO"));
        }

        if (airPollutionIndividualAqi.getD() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getD().getV(), "D"));
        }

        if (airPollutionIndividualAqi.getH() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getH().getV(), "H"));
        }


        if (airPollutionIndividualAqi.getNo2() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getNo2().getV(), "NO2"));
        }
        if (airPollutionIndividualAqi.getO3() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getO3().getV(), "O3"));

        }
        if (airPollutionIndividualAqi.getP() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getP().getV(), "P"));

        }
        if (airPollutionIndividualAqi.getPm10() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm10().getV(), "PM10"));

        }
        if (airPollutionIndividualAqi.getPm25() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm25().getV(), "PM25"));

        }
        if (airPollutionIndividualAqi.getS02() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getS02().getV(), "SO2"));
        }
        if (airPollutionIndividualAqi.getT() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(airPollutionIndividualAqi.getT().getV(), "T"));

        }
        if (airPollutionIndividualAqi.getW() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getW().getV(), "W"));

        }
        if (airPollutionIndividualAqi.getWd() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getWd().getV(), "WD"));
        }

        return airPollutionIndividualValueList;


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
