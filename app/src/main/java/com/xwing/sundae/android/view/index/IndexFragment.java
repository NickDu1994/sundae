package com.xwing.sundae.android.view.index;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.ComplexListAdapter;
import com.xwing.sundae.android.customview.SearchRoundCTACardView;
import com.xwing.sundae.android.model.ComplexListModel;
import com.xwing.sundae.android.model.IndexBannerImage;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.GlideImageLoader;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private OnFragmentInteractionListener mListener;
    private List<ComplexListModel> complexListModelList = new ArrayList<>();


    public IndexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
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

        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);

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
        Log.d("IndexFragment","enter index fragment");
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SearchRoundCTACardView searchRoundCTACardView = getActivity().findViewById(R.id.search_button);
        searchRoundCTACardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("dkdebug", "enter click to search fragment");
                SearchFragment mIndexFragment = new SearchFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
                fragmentTransaction.commit();
            }
        });

        List<IndexBannerImage> images = new ArrayList<IndexBannerImage>();
        IndexBannerImage indexBannerImage = new IndexBannerImage();
        indexBannerImage.setImageUrl("https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg");
        images.add(indexBannerImage);
        IndexBannerImage indexBannerImage2 = new IndexBannerImage();
        indexBannerImage2.setImageUrl("https://img1.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/d7917d2a719c779.jpg");
        images.add(indexBannerImage2);
        IndexBannerImage indexBannerImage3 = new IndexBannerImage();
        indexBannerImage3.setImageUrl("https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/926d23b38826a86.jpg");
        images.add(indexBannerImage3);

        Banner banner = (Banner) getActivity().findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.start();

        initMockData();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.index_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setHasFixedSize(true);
        ComplexListAdapter complexListAdapter = new ComplexListAdapter(complexListModelList, getContext());
        recyclerView.setAdapter(complexListAdapter);
    }

    public void sendGetRequest() {

        String url = "http://10.0.2.2:3001/index";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("count","15");
        paramsMap.put("q","de");
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
            }
        });
    }

    public void sendPostRequest() {
        String url = "http://10.0.2.2:3001/list";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user","15");
        String jsonStr = CommonMethod.mapToJson(paramsMap);
        OkhttpUtil.okHttpPostJson(url, jsonStr, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
            }
        });
    }

    public void initMockData() {
        for(int i = 0; i < 5; i++){
            String[] imageArray = {"https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg",
                    "https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg",
                    "https://img3.doubanio.com/view/movie_gallery_frame_hot_rec/normal/public/0e4bef5f02adf70.jpg"};
            ComplexListModel complexListModel = new ComplexListModel(
                    "Digitial information",
                    "This is a content for sundae app usingThis is a content for sundae app usingThis is a content for sundae app using",
                    imageArray,
                    true,
                    "1900次浏览",
                    "3个月前"
            );
            complexListModelList.add(complexListModel);
        }
    }

}
