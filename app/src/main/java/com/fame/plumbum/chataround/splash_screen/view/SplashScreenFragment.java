package com.fame.plumbum.chataround.splash_screen.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.helper.utils.NetworkUtils;
import com.fame.plumbum.chataround.splash_screen.model.RetrofitSplashScreenProvider;
import com.fame.plumbum.chataround.splash_screen.model.data.SplashScreenData;
import com.fame.plumbum.chataround.splash_screen.presenter.SplashScreenPresenter;
import com.fame.plumbum.chataround.splash_screen.presenter.SplashScreenPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SplashScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SplashScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashScreenFragment extends Fragment implements SplashScreenView {
    @BindView(R.id.one_mile_logo)
    ImageView one_mile_logo;
    @BindView(R.id.tag_line)
    TextView tag_line;
    @BindView(R.id.codeNicelyLogo)
    ImageView codenicely_logo;
    private SplashScreenPresenter splashScreenPresenter;
    private SharedPrefs sharedPrefs;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashScreenFragment newInstance(String param1, String param2) {
        SplashScreenFragment fragment = new SplashScreenFragment();
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
        View view=inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ButterKnife.bind(this,view);
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("No Internet Connection Available !");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!NetworkUtils.isNetworkAvailable(getContext())) {

                                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            else {
                                dialog.dismiss();
                            }
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder1.create();
            alert.show();
        }
        Glide.with(this).load(R.drawable.codenicely_logo).into(codenicely_logo);
        Glide.with(this).load(R.drawable.logo).into(one_mile_logo);
        tag_line.setVisibility(View.VISIBLE);
        splashScreenPresenter = new SplashScreenPresenterImpl(this, new RetrofitSplashScreenProvider());
        splashScreenPresenter.getSplashResponse();
        sharedPrefs = new SharedPrefs(getContext());

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onVersionReceived(SplashScreenData splashScreenData) throws PackageManager.NameNotFoundException {

    }

    @Override
    public void onFailed() {

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
