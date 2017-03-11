package com.fame.plumbum.chataround.news.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.activity.MainActivity;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.helper.image_loader.GlideImageLoader;
import com.fame.plumbum.chataround.helper.image_loader.ImageLoader;
import com.fame.plumbum.chataround.news.presenter.NewsListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailsFragment extends Fragment {
    @BindView(R.id.timestamp)
    TextView news_timestamp;
    @BindView(R.id.NewsDescription)
    TextView news_description;
    @BindView(R.id.newSource)
    TextView news_source;
    @BindView(R.id.newsImage)
    ImageView news_image;
    @BindView(R.id.newsAuthor)
    TextView news_author;
    @BindView(R.id.newsTitle)
    TextView news_title;
    @BindView(R.id.imageProgressBar)
    ProgressBar imageProgressBar;
    private MainActivity mainActivity;
    private String author;
    private String timestamp;
    private String image_url;
    private String title;
    private String source;
    private String description;
    private ImageLoader imageLoader;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDetailsFragment newInstance(String param1, String param2) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
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
        View view= inflater.inflate(R.layout.fragment_news_details, container, false);
        Bundle bundle = this.getArguments();
        source=bundle.getString("newsSource");
        author=bundle.getString("newsAuthor");
        description=bundle.getString("newsDescription");
        image_url=bundle.getString("image");
        title=bundle.getString("newsTitle");
        timestamp=bundle.getString("newsTimestamp");
        imageLoader=new GlideImageLoader(getContext());
        news_author.setText(author);
        news_description.setText(description);
        if(image_url!=null)
        {
            news_image.setVisibility(View.VISIBLE);
            imageLoader.loadImage(image_url,news_image,imageProgressBar);
        }
        else
        {
            news_image.setVisibility(View.GONE);
            imageProgressBar.setVisibility(View.GONE);
        }
        if(source!=null)
        {
            news_source.setVisibility(View.VISIBLE);
         news_source.setText(source);
        }
        else
        {
            news_source.setVisibility(View.GONE);
        }
        if(author!=null)
        {
            news_author.setVisibility(View.VISIBLE);
            news_author.setText(author);
        }
        else
        {
            news_author.setVisibility(View.GONE);
        }

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
