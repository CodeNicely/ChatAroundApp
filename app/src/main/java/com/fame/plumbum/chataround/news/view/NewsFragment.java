package com.fame.plumbum.chataround.news.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fame.plumbum.chataround.R;
import com.fame.plumbum.chataround.helper.SharedPrefs;
import com.fame.plumbum.chataround.news.model.RetrofitNewsListProvider;
import com.fame.plumbum.chataround.news.model.data.NewsListData;
import com.fame.plumbum.chataround.news.model.data.NewsListDataDetails;
import com.fame.plumbum.chataround.news.presenter.NewsListPresenter;
import com.fame.plumbum.chataround.news.presenter.NewsListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fame.plumbum.chataround.R.id.recyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsPageView {
    private NewsListPresenter newsListPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(recyclerView)
    RecyclerView recyclerview;
    private SharedPrefs sharedPrefs;
    private NewsListAdapter newsListAdapter;
    @BindView(R.id.layout_not_available)
    LinearLayout linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        initialise();
        newsListPresenter.getNews(sharedPrefs.getUserId(),"Raipur");
        return view;

    }
    private void initialise() {
        newsListPresenter=new NewsListPresenterImpl(this,new RetrofitNewsListProvider());
        sharedPrefs=new SharedPrefs(getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        newsListAdapter=new NewsListAdapter(getContext(),this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setHasFixedSize(true);
        newsListAdapter=new NewsListAdapter(getContext(),this);
        recyclerview.setAdapter(newsListAdapter);


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
    public void showProgressBar(boolean show) {
        if(show)
        {
            progressBar.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerview.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNewsList(List<NewsListDataDetails> newsListDataList) {
        if(newsListDataList.size()==0)
        {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.INVISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);

        }
        newsListAdapter.setNewsListDataDetailsList(newsListDataList);
        newsListAdapter.notifyDataSetChanged();
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
