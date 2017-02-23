package com.fame.plumbum.chataround.pollution.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionDetails;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualAqi;
import com.fame.plumbum.chataround.pollution.model.air_model.AirPollutionIndividualValue;
import com.fame.plumbum.chataround.pollution.presenter.PollutionPresenterImpl;
import com.fame.plumbum.chataround.pollution.provider.RetrofitPollutionProvider;
import com.github.pavlospt.CircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.login.widget.ProfilePictureView.TAG;

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
    private PollutuionAqiAdapter pollutuionAqiAdapter;
    private OnFragmentInteractionListener mListener;


    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @BindView(R.id.circleView)
    CircleView circleView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.aqi_health_notice)
    TextView aqi_health_notice;

    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.wind)
    TextView wind;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


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
            scrollView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setData(AirPollutionDetails airPollutionDetails) {

        double aqi = airPollutionDetails.getData().getAqi();
        String healthStatement = "";
        if (aqi < 50) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.good));
            healthStatement = "Air quality is considered satisfactory,and air pollution poses " +
                    "little or no risk";

        } else if (aqi > 50 && aqi <= 100) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.moderate));
            healthStatement = "Air quality is acceptable; however, for some pollutants there may be a " +
                    "moderate health concern for a very small number of people who are unusually " +
                    "sensitive to air pollution.";

        } else if (aqi > 100 && aqi <= 150) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.sensitive));
            healthStatement = "Members of sensitive groups may experience health effects. The " +
                    "general public is not likely to be affected.";

        } else if (aqi > 150 && aqi <= 200) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.unhealthy));
            healthStatement = "Everyone may begin to experience health effects; members of sensitive " +
                    "groups may experience more serious health effects.";

        } else if (aqi > 200 && aqi <= 300) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.very_unhealthy));
            healthStatement = "Health warnings of emergency conditions. The entire population is " +
                    "more likely to be affected.";

        } else if (aqi > 300 && aqi <= 300) {

            circleView.setFillColor(ContextCompat.getColor(getContext(), R.color.hazardous));
            healthStatement = "Health alert: everyone may experience more serious health effects";

        }


        try {
            humidity.setText("Humidity - " + String.valueOf(airPollutionDetails.getData().getIaqi().getH().getV()));
        }catch (Exception e){
            Log.e(TAG,e.getMessage());

        }

        try {
            wind.setText("Wind Speed - " + String.valueOf(airPollutionDetails.getData().getIaqi().getWd().getV()));
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        circleView.setTitleText(String.valueOf(airPollutionDetails.getData().getAqi()) + "\n" + "AQI");
        aqi_health_notice.setText(healthStatement);
        pollutuionAqiAdapter = new
                PollutuionAqiAdapter(getContext(),
                getIndividualAqiList(airPollutionDetails.getData().getIaqi()));

        recyclerView.setAdapter(pollutuionAqiAdapter);
        pollutuionAqiAdapter.notifyDataSetChanged();


    }


    List<AirPollutionIndividualValue> getIndividualAqiList(AirPollutionIndividualAqi airPollutionIndividualAqi) {

        List<AirPollutionIndividualValue> airPollutionIndividualValueList = new ArrayList<>();


        if (airPollutionIndividualAqi.getPm25() != null) {

            double aqi = airPollutionIndividualAqi.getPm25().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 30) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 30 && aqi <= 60) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 60 && aqi <= 90) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 90 && aqi <= 120) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 120 && aqi <= 250) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 250) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm25().getV(), "PM 2.5", color));

        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "PM 2.5", color));
        }

        if (airPollutionIndividualAqi.getPm10() != null) {

            double aqi = airPollutionIndividualAqi.getPm10().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 50) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 50 && aqi <= 100) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 100 && aqi <= 250) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 250 && aqi <= 350) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 350 && aqi <= 430) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 430) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getPm10().getV(), "PM 10", color));

        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "PM 10", color));
        }


        if (airPollutionIndividualAqi.getO3() != null) {

            double aqi = airPollutionIndividualAqi.getO3().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 50) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 50 && aqi <= 100) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 100 && aqi <= 168) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 168 && aqi <= 208) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 208 && aqi <= 748) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 748) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getO3().getV(), "o3", color));

        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "o3", color));
        }


        if (airPollutionIndividualAqi.getNo2() != null) {

            double aqi = airPollutionIndividualAqi.getNo2().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 40) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 40 && aqi <= 80) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 80 && aqi <= 180) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 180 && aqi <= 280) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 280 && aqi <= 400) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 400) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getNo2().getV(), "No2", color));
        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "No2", color));
        }


        if (airPollutionIndividualAqi.getS02() != null) {

            double aqi = airPollutionIndividualAqi.getS02().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 40) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 40 && aqi <= 80) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 80 && aqi <= 380) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 380 && aqi <= 800) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 800 && aqi <= 1600) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 1600) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getS02().getV(), "SO2", color));
        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "So2", color));
        }

        if (airPollutionIndividualAqi.getCo() != null) {

            double aqi = airPollutionIndividualAqi.getCo().getV();
            int color = ContextCompat.getColor(getContext(), R.color.white);
            if (aqi < 1.0) {

                color = ContextCompat.getColor(getContext(), R.color.good);

            } else if (aqi > 1.0 && aqi <= 2.0) {

                color = ContextCompat.getColor(getContext(), R.color.moderate);


            } else if (aqi > 2.0 && aqi <= 10) {

                color = ContextCompat.getColor(getContext(), R.color.sensitive);

            } else if (aqi > 10 && aqi <= 17) {

                color = ContextCompat.getColor(getContext(), R.color.unhealthy);

            } else if (aqi > 17 && aqi <= 34) {

                color = ContextCompat.getColor(getContext(), R.color.very_unhealthy);

            } else if (aqi > 34) {

                color = ContextCompat.getColor(getContext(), R.color.hazardous);

            }


            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getCo().getV(), "CO", color));
        }else {

            int color=R.color.black;
            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    -9999, "Co", color));
        }
/*
    This Method is for Temperature that we are not going to use.
    Only Gases Will be Showin in Recycler View .


        if (airPollutionIndividualAqi.getT() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getT().getV(), "T"));

        }


         if (airPollutionIndividualAqi.getW() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getW().getV(), "W"));

        }
        if (airPollutionIndividualAqi.getWd() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getWd().getV(), "WD"));
        }

        if (airPollutionIndividualAqi.getD() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getD().getV(), "D"));
        }

        if (airPollutionIndividualAqi.getH() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getH().getV(), "H"));
        }*/
/*
This Method is for Pressure that we are not going to use.

        if (airPollutionIndividualAqi.getP() != null) {

            airPollutionIndividualValueList.add(new AirPollutionIndividualValue(
                    airPollutionIndividualAqi.getP().getV(), "P"));

        }
*/

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
