package com.xwing.sundae.android.view.index;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.ComplexListAdapter;
import com.xwing.sundae.android.customview.SearchRoundCTACardView;
import com.xwing.sundae.android.model.AbbreviationPlusModel;
import com.xwing.sundae.android.model.BaseImage;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.ComplexListModel;
import com.xwing.sundae.android.model.IndexBannerModel;
import com.xwing.sundae.android.model.AbbreviationBaseModel;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.GlideImageLoader;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.IndexDetailActivity;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.xwing.sundae.android.util.CommonMethod.CalculateTimeUntilNow;

public class PortalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private IndexDetailFragment mIndexDetailFragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<ComplexListModel> complexListModelList = new ArrayList<>();

    public PortalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PortalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PortalFragment newInstance(String param1, String param2) {
        PortalFragment fragment = new PortalFragment();
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
        return inflater.inflate(R.layout.fragment_portal, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SearchRoundCTACardView searchRoundCTACardView = getActivity().findViewById(R.id.search_button);
        searchRoundCTACardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("dkdebug", "enter click to search fragment");
                SearchFragment searchFragment = new SearchFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.index_fragment_container, searchFragment);
                fragmentTransaction.commit();

                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.triggerBottomNavigationBar(false);

            }
        });

        getNews();

        getRecommend();

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

    public void openIndexDetail() {
        Intent intent= new Intent(getContext(), IndexDetailActivity.class);
        getActivity().startActivity(intent);
    }

    public void getNews(){

        String url = Constant.globalServerUrl + "/news/getLastestNews";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("reserve","123456");
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<List<IndexBannerModel>> responseIndexBannerImage =
                            (CommonResponse<List<IndexBannerModel>>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<List<IndexBannerModel>>>() {}.getType());
                    final List<IndexBannerModel> indexBannerModel = responseIndexBannerImage.getData();
                    List<BaseImage> displayImages = new ArrayList<BaseImage>();
                    for(IndexBannerModel item : indexBannerModel){
                        displayImages.add(new BaseImage(item.getImage().getPath()));
                    }

                    Banner banner = (Banner) getActivity().findViewById(R.id.banner);
                    banner.setImageLoader(new GlideImageLoader());
                    banner.setImages(displayImages);
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Toast.makeText(getContext(), "open url=" + indexBannerModel.get(position).getHtmlUrl(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    banner.start();
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void getRecommend(){

        String url = Constant.globalServerUrl + "/abbreviation/getRecommendedEntryList";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("reserve","123456");
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<List<AbbreviationPlusModel>> responseIndexRecommendList =
                            (CommonResponse<List<AbbreviationPlusModel>>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<List<AbbreviationPlusModel>>>() {}.getType());
                    final List<AbbreviationPlusModel> dataList = responseIndexRecommendList.getData();
                    List<BaseImage> displayImages = new ArrayList<BaseImage>();
                    for(AbbreviationPlusModel item : dataList){
                        List<String> imageList = new ArrayList<>();
                        if(item.getImageList().size() != 0){
                            for(IndexBannerModel.Image imageUrl : item.getImageList()){
                                imageList.add(imageUrl.getPath());
                            }
                        }else {
                            imageList.add("https://img-bss.csdn.net/201903111202548906.png");
                        }

                        ComplexListModel complexListModel = new ComplexListModel(
                                item.getAbbrName() + " " + item.getFullName(),
                                item.getContent(),
                                imageList.toArray(new String[imageList.size()]),
                                true,
                                (item.getVisitedCount() != null)? item.getVisitedCount(): "0" + "次阅读",
                                CalculateTimeUntilNow(item.getCreateTime())
                        );
                        complexListModelList.add(complexListModel);
                    }

                    RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.index_recylerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
                    recyclerView.setHasFixedSize(true);
                    ComplexListAdapter complexListAdapter = new ComplexListAdapter(complexListModelList, getContext());
                    complexListAdapter.setOnItemClickListener(new ComplexListAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            openIndexDetail();
                        }
                    });
                    recyclerView.setAdapter(complexListAdapter);

                } catch (Exception e) {
                    Log.d("dkdebug", "e=" + e);
                }
            }
        });
    }
}
