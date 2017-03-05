package com.fame.plumbum.chataround.restroom.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.restroom.model.RestRoomDetails;
import com.fame.plumbum.chataround.restroom.presenter.RestRoomPresenter;
import com.fame.plumbum.chataround.restroom.presenter.RestRoomPresenterImpl;
import com.fame.plumbum.chataround.restroom.provider.RetrofitRestRoomProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestroomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestroomFragment extends Fragment implements RestRoomView {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RestroomFragment";

    private static View view;
    private RestRoomPresenter restRoomPresenter;
    private SharedPrefs sharedPrefs;
    private Context context;
    private RestroomAdapter restroomAdapter;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RestroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestroomFragment newInstance(String param1, String param2) {
        RestroomFragment fragment = new RestroomFragment();
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

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_restroom, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        ButterKnife.bind(this, view);
        context = getContext();
        restroomAdapter = new RestroomAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(restroomAdapter);

        sharedPrefs = new SharedPrefs(context);

        restRoomPresenter = new RestRoomPresenterImpl(this, new RetrofitRestRoomProvider());
        restRoomPresenter.requestRestRooms(sharedPrefs.getUserId(), 12.12, 13.12);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        onDestroy();
    }

    @Override
    public void showLoader(boolean show) {
        if (show) {

            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onReceived(List<RestRoomDetails> restRoomDetailsList) {

        Log.d("Data Received", String.valueOf(restRoomDetailsList.size()));
        restroomAdapter.setData(restRoomDetailsList);
        restroomAdapter.notifyDataSetChanged();


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
