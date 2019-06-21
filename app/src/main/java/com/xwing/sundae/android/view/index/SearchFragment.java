package com.xwing.sundae.android.view.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.CustomeButtonGroupView;
import com.xwing.sundae.android.model.AbbreviationBaseModel;
import com.xwing.sundae.android.model.AbbreviationDetailModel;
import com.xwing.sundae.android.model.AbbreviationPlusModel;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.SearchSuggestionModel;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.SharedPreferencesUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;
import com.xwing.sundae.android.view.my.EditUserInfoActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PANEL_SUGGESTION = 1;
    private static final int PANEL_LIST = 2;
    private static final int PANEL_DETAIL = 3;
    private String currentEntryId = "";
    private boolean isLike = false;
    private boolean isSave = false;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button cancelButton;
    private ImageButton cleanButton;
    private EditText mainEditText;
    private LinearLayout suggestionPanel;
    private LinearLayout listPanel;
    private LinearLayout detailPanel;
    private ListView listView;
    private ImageView likeIV;
    private ImageView saveIV;
    private Context mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Map<String,String>> keywordList = new ArrayList<Map<String,String>>();

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        mContext = getActivity();
        fragmentManager = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        suggestionPanel = getActivity().findViewById(R.id.sugggestionPanel);
        listPanel = getActivity().findViewById(R.id.listPanel);
        detailPanel = getActivity().findViewById(R.id.detailPanel);
        mainEditText = getActivity().findViewById(R.id.mainEditText);
        mainEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Noting
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("dkdebug", "onTextChanged s=" + s + "count=" + count);
                if(s.length() != 0) {
                    displayController(PANEL_LIST);
                    handleInput(s.toString());
                }else {
                    displayController(PANEL_SUGGESTION);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Noting
            }
        });
        mainEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("dk","enter setOnEditorActionListener" + event);
                if ((event != null && KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction())) {
                    SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(mContext);
                    String currentKeywordNote = spUtil.getSP("keyword_note");
                    currentKeywordNote = mainEditText.getText().toString() + "," + currentKeywordNote;
                    spUtil.putSP("keyword_note",currentKeywordNote);
                    return true;
                }
                return false;
            }
        });
        cancelButton = getActivity().findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortalFragment portalFragment = new PortalFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.index_fragment_container, portalFragment);
                fragmentTransaction.commit();

                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.triggerBottomNavigationBar(true);
            }
        });
        cleanButton = getActivity().findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dkdebug", "click clean button");
                mainEditText.getText().clear();
            }
        });



        SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(mContext);
        Log.d("dk","dkdebug");
        if(spUtil.getSP("keyword_note")!= null){
            Log.d("dkdebug keywordNote=", spUtil.getSP("keyword_note"));
        }

        String[] testName = spUtil.getSP("keyword_note").split(",");
        String[] testName2 = {"apple2","title2","layout2","LinearLayout2","new2","child2"};
        CustomeButtonGroupView customeButtonGroupView = getActivity().findViewById(R.id.historyPanel);
        customeButtonGroupView.setTitle("搜索历史");
        customeButtonGroupView.setTagList(testName);
        CustomeButtonGroupView customeButtonGroupView2 = getActivity().findViewById(R.id.hotPanel);
        customeButtonGroupView2.setTitle("热门搜索");
        customeButtonGroupView2.setTagList(testName2);


        saveIV = getActivity().findViewById(R.id.detailSave);
        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSave){
                    handleSaveAndLike(currentEntryId, false, "save");
                    saveIV.setImageResource(R.drawable.heart_fill);
                    isSave = true;
                }else {
                    handleSaveAndLike(currentEntryId, true, "save");
                    saveIV.setImageResource(R.drawable.heart);
                    isSave = false;
                }
            }
        });

        likeIV = getActivity().findViewById(R.id.detailLike);
        likeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLike){
                    handleSaveAndLike(currentEntryId, false, "like");
                    likeIV.setImageResource(R.drawable.dislike);
                    isLike = true;
                }else {
                    handleSaveAndLike(currentEntryId, true, "like");
                    likeIV.setImageResource(R.drawable.like);
                    isLike = false;
                }
            }
        });

    }

    public void displayController(int panelName){
        suggestionPanel.setVisibility(View.GONE);
        listPanel.setVisibility(View.GONE);
        detailPanel.setVisibility(View.GONE);
        switch (panelName){
            case 1:
                suggestionPanel.setVisibility(View.VISIBLE);
                break;
            case 2:
                listPanel.setVisibility(View.VISIBLE);
                break;
            case 3:
                detailPanel.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }


    public void handleInput(String keyword){
        listView = getActivity().findViewById(R.id.listContainer);
        String url = Constant.globalServerUrl + "/search/searchAbbreviation";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyWords",keyword);
        Log.d("dkdebug", "request " + "paramsMap=" + paramsMap.toString());
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                keywordList.clear();
                Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<List<AbbreviationPlusModel>> responseSearchSuggestionList =
                            (CommonResponse<List<AbbreviationPlusModel>>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<List<AbbreviationPlusModel>>>() {}.getType());
                    final List<AbbreviationPlusModel> dataList = responseSearchSuggestionList.getData();
                    for(AbbreviationPlusModel item : dataList){
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("label", item.getAbbrName() + " - " + item.getFullName());
                        map.put("id", item.getId());
                        keywordList.add(map);
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, keywordList, R.layout.item_search_result, new String[]{"label"}, new int[]{R.id.label});
                    listView.setAdapter(simpleAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(mContext, "position=" + position + keywordList.get(position).get("id"), Toast.LENGTH_SHORT).show();
                            showDetail(dataList.get(position).getId());

                        }
                    });

                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void showDetail(String entryId) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

        displayController(PANEL_DETAIL);
        currentEntryId = entryId;

        String url = Constant.globalServerUrl + "/abbreviation/getOneEntryDetail";
        HashMap<String, String> paramsMap = new HashMap<>();
        GetUserInfo getUserInfo = new GetUserInfo(mContext);
        try{
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        }catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
        }
        paramsMap.put("entryId",entryId);

        Log.d("dkdebug", "request " + "paramsMap=" + paramsMap.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<AbbreviationDetailModel> responseAbbreviation =
                            (CommonResponse<AbbreviationDetailModel>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<AbbreviationDetailModel>>() {}.getType());
                    final AbbreviationDetailModel data = responseAbbreviation.getData();
                    TextView detailTitleTV = getActivity().findViewById(R.id.detailTitle);
                    detailTitleTV.setText(data.getAbbreviation().getAbbrName());
                    TextView detailFullnameTV = getActivity().findViewById(R.id.detailFullname);
                    detailFullnameTV.setText(data.getAbbreviation().getFullName());

                    HtmlTextView htmlTextView = (HtmlTextView) getActivity().findViewById(R.id.html_text);
                    htmlTextView.setHtml(data.getAbbreviation().getContent(),
                            new HtmlHttpImageGetter(htmlTextView));

                    TextView createTimeTV = getActivity().findViewById(R.id.create_time);
                    createTimeTV.setText(CommonMethod.CalculateTimeUntilNow(data.getAbbreviation().getCreateTime()));
                    TextView authorTV = getActivity().findViewById(R.id.author);
                    authorTV.setText(data.getAbbreviation().getCreateBy());
                    TextView likeTV = getActivity().findViewById(R.id.like_number);
                    likeTV.setText(data.getAbbreviation().getLikedCount() + "获赞");

                    if(data.isLike()){
                        likeIV.setImageResource(R.drawable.like);
                    }else {
                        likeIV.setImageResource(R.drawable.dislike);
                    }

                    if(data.isCollect()){
                        saveIV.setImageResource(R.drawable.heart_fill);
                    }else {
                        saveIV.setImageResource(R.drawable.heart);
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });

    }

    public void handleSaveAndLike(String entryId, boolean isEnroll, String type) {
        String url= "";
        if("like".equals(type)){
            if(isEnroll){
                url = Constant.globalServerUrl + "/abbreviation/like";
            }else {
                url = Constant.globalServerUrl + "/abbreviation/removeLike";
            }
        }else {
            if(isEnroll){
                url = Constant.globalServerUrl + "/collection/abbreviation";
            }else {
                url = Constant.globalServerUrl + "/collection/removeCollect";
            }
        }

        GetUserInfo getUserInfo = new GetUserInfo(mContext);
        if(null == getUserInfo && "".equals(getUserInfo))   {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        try{
            paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
        }catch (NullPointerException e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
        }
        paramsMap.put("entryId",entryId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {}.getType());
                    if(saveResult.getStatus() == 200){
                        Log.d("dkdebug","save success");
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }
}
