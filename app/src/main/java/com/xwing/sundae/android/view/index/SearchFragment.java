package com.xwing.sundae.android.view.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.CommentAdapter;
import com.xwing.sundae.android.customview.CustomeButtonGroupView;
import com.xwing.sundae.android.customview.FScrollView;
import com.xwing.sundae.android.model.AbbreviationDetailModel;
import com.xwing.sundae.android.model.AbbreviationPlusModel;
import com.xwing.sundae.android.model.CommentModel;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.ImageServerConstant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.SharedPreferencesUtil;
import com.xwing.sundae.android.view.AddCommentActivity;
import com.xwing.sundae.android.view.CommentListActivity;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean isFollow = false;
    private String storageAuthorId = "";
    private String storageUserId = "";

    private CardView mainEditTextCardView;
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
    private TextView followTV;
    private Context mContext;

    private TextView collectNumber;
    private TextView addComment;
    private TextView totalCount;
    private LinearLayout viewMore;
    private LinearLayout commentEmpty;
    private int totalCommentNumber;

    private List<CommentModel> commentList = new ArrayList<>();
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    private Handler handler = new Handler();

    private FScrollView detailScroll;
    private LinearLayout bottomBar;

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
        detailScroll = getActivity().findViewById(R.id.detail_scroll);
        bottomBar = getActivity().findViewById(R.id.bottom_bar);

        mainEditTextCardView = getActivity().findViewById(R.id.mainEditTextCardView);
        mainEditTextCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainEditText.requestFocus();
            }
        });
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

        String[] histroyAbbr = spUtil.getSP("keyword_note").split(",");
        CustomeButtonGroupView customeButtonGroupView = getActivity().findViewById(R.id.historyPanel);
        customeButtonGroupView.setTitle("搜索历史");
        customeButtonGroupView.setTagList(histroyAbbr);
        customeButtonGroupView.setOnItemClickListener(new CustomeButtonGroupView.OnTagClickListener() {
            @Override
            public void onTagClick(String keyword) {
                mainEditText.setText(keyword);
            }
        });
        setHotSearch();


        saveIV = getActivity().findViewById(R.id.detailSave);
        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSave){
                    handleSaveAndLike(currentEntryId, true, "save");

                }else {
                    handleSaveAndLike(currentEntryId, false, "save");

                }
            }
        });

        likeIV = getActivity().findViewById(R.id.detailLike);
        likeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLike){
                    handleSaveAndLike(currentEntryId, true, "like");

                }else {
                    handleSaveAndLike(currentEntryId, false, "like");

                }
            }
        });

        followTV = getActivity().findViewById(R.id.user_follow);
        followTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFollow){
                    handleFollow(false);
                }else {
                    handleFollow(true);
                }
            }
        });

        addComment = (TextView) getActivity().findViewById(R.id.add_comment);
        totalCount = (TextView) getActivity().findViewById(R.id.comment_count_title);
        viewMore = (LinearLayout) getActivity().findViewById(R.id.view_more_comment);
        commentEmpty = (LinearLayout) getActivity().findViewById(R.id.comment_empty);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, AddCommentActivity.class);
                intent.putExtra("id", currentEntryId);
                startActivity(intent);
            }
        });

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, CommentListActivity.class);
                intent.putExtra("id", currentEntryId);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.comment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(commentList, getContext());
        recyclerView.setAdapter(commentAdapter);
    }

    public void displayController(int panelName){
        suggestionPanel.setVisibility(View.GONE);
        listPanel.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);
        detailScroll.setVisibility(View.GONE);
        switch (panelName){
            case 1:
                suggestionPanel.setVisibility(View.VISIBLE);
                break;
            case 2:
                listPanel.setVisibility(View.VISIBLE);
                break;
            case 3:
                bottomBar.setVisibility(View.VISIBLE);
                detailScroll.setVisibility(View.VISIBLE);
                break;
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
//                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
//                }
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                keywordList.clear();
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<List<AbbreviationPlusModel>> responseSearchSuggestionList =
                            (CommonResponse<List<AbbreviationPlusModel>>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<List<AbbreviationPlusModel>>>() {}.getType());
                    final List<AbbreviationPlusModel> dataList = responseSearchSuggestionList.getData();
                    for(AbbreviationPlusModel item : dataList){
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("label", item.getAbbrName() + (item.getFullName() == null ? "" : (" - " + item.getFullName())));
                        map.put("id", item.getId());
                        keywordList.add(map);
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, keywordList, R.layout.item_search_result, new String[]{"label"}, new int[]{R.id.label});
                    listView.setAdapter(simpleAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(Constant.LOG_LEVEL == "DEV") {
                                Toast.makeText(mContext, "position=" + position + keywordList.get(position).get("id"), Toast.LENGTH_SHORT).show();
                            }
                            showDetail(dataList.get(position).getId());
                            getComments(dataList.get(position).getId());

                        }
                    });

                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void showDetail(String entryId) {

        mainEditText.clearFocus();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
//        imm.toggleSoftInput (0, InputMethodManager.HIDE_IMPLICIT_ONLY);

        displayController(PANEL_DETAIL);


        currentEntryId = entryId;

        String url = Constant.globalServerUrl + "/abbreviation/getOneEntryDetail";
        HashMap<String, String> paramsMap = new HashMap<>();
        GetUserInfo getUserInfo = new GetUserInfo(mContext);
        try{
            if(getUserInfo.isUserLogined()){
                paramsMap.put("userId", getUserInfo.getUserInfo().getData().getId().toString());
                storageUserId = getUserInfo.getUserInfo().getData().getId().toString();
            }else {
                paramsMap.put("userId","");
                followTV.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e) {
            Log.d("dkdebug NPE", "e=" + e);
            paramsMap.put("userId","");
            followTV.setVisibility(View.INVISIBLE);
        }
        paramsMap.put("entryId",entryId);

        Log.d("dkdebug", "request " + "paramsMap=" + paramsMap.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
//                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
//                }
                Log.d("dkdebug onFailure", "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
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
                    ImageView userPicIV = getActivity().findViewById(R.id.user_pic);
                    RequestOptions options = new RequestOptions().
                            circleCropTransform();

                    if(null == data.getAvatar() || "".equals(data.getAvatar())) {
                        Glide.with(mContext).load(R.drawable.defaultpic_theme).apply(options).into(userPicIV);
                    } else {
                        Glide.with(mContext).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAvatar()).apply(options).into(userPicIV);
                    }

                    TextView authorTV = getActivity().findViewById(R.id.author);
                    authorTV.setText(data.getAuthor());
                    storageAuthorId = data.getAbbreviation().getCreateBy();
                    TextView likeTV = getActivity().findViewById(R.id.like_number);
                    likeTV.setText(data.getAbbreviation().getLikedCount());
                    collectNumber = getActivity().findViewById(R.id.collect_number);
                    collectNumber.setText(data.getCollectNumber());

                    if(data.isLike()){
                        likeIV.setImageResource(R.drawable.like);
                        isLike = true;
                    }else {
                        likeIV.setImageResource(R.drawable.dislike);
                        isLike = false;
                    }

                    if(data.isCollect()){
                        saveIV.setImageResource(R.drawable.heart_fill);
                        isSave = true;
                    }else {
                        saveIV.setImageResource(R.drawable.heart);
                        isSave = false;
                    }

                    if(data.getAbbreviation().getCreateBy().equals(storageUserId)){
                        followTV.setVisibility(View.INVISIBLE);
                    }else {
                        if(data.isFollow()){
                            followTV.setText("已关注");
                            isFollow = true;
                        }else {
                            followTV.setText("关注");
                            isFollow = false;
                        }
                    }


                    if(data.getAbbreviation().getImageList().size() >= 1){
                        ImageView detailMainImage = getActivity().findViewById(R.id.detailMainImage);
                        Glide.with(mContext).load(ImageServerConstant.IMAGE_SERVER_URL + data.getAbbreviation().getImageList().get(0).getPath()).into(detailMainImage);
                    }

                    SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(mContext);
                    String currentKeywordNote = spUtil.getSP("keyword_note");
                    currentKeywordNote = data.getAbbreviation().getAbbrName() + "," + currentKeywordNote;
                    spUtil.putSP("keyword_note",currentKeywordNote);
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });

    }

    public void getComments(String entryId) {
        String url = Constant.REQUEST_URL_MY + "/comment/getCommentListTwo";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("abbrId", entryId);
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                if(!Constant.LOG_LEVEL.equals("PRD")) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getCommentList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    LinkedTreeMap data = (LinkedTreeMap) map_res.get("data");
                    Object content = data.get("content");
                    double total = (double) data.get("totalElements");
                    totalCommentNumber = (int) total;
                    totalCount.setText("网友点评（" + String.valueOf(totalCommentNumber) + "）");
                    if (totalCommentNumber == 0) {
                        viewMore.setVisibility(View.GONE);
                        commentEmpty.setVisibility(View.VISIBLE);
                    } else {
                        viewMore.setVisibility(View.VISIBLE);
                        commentEmpty.setVisibility(View.GONE);
                    }
                    String tmp = gson.toJson(content);
                    CommentModel[] commentModels = gson.fromJson(tmp, CommentModel[].class);
                    commentList.addAll(Arrays.asList(commentModels));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    public void handleSaveAndLike(final String entryId, boolean isEnroll, String type) {
        String url= "";
        final boolean finalIsEnroll = isEnroll;
        final String finalType = type;
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

        final String tempLogUrl = url;

        GetUserInfo getUserInfo = new GetUserInfo(mContext);
        if(!getUserInfo.isUserLogined())   {
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
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug onFailure", tempLogUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", tempLogUrl + "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {}.getType());
                    if(saveResult.getStatus() == 200){
                        Log.d("dkdebug","save success");
                        TextView likeTV = getActivity().findViewById(R.id.like_number);
                        if("like".equals(finalType)){
                            String currentNum = likeTV.getText().toString();
                            int resultNum;
                            if(finalIsEnroll){
                                likeIV.setImageResource(R.drawable.like);
                                resultNum = Integer.valueOf(currentNum) + 1;
                                isLike = true;
                            }else {
                                likeIV.setImageResource(R.drawable.dislike);
                                resultNum = Integer.valueOf(currentNum) - 1;
                                isLike = false;
                            }
                            likeTV.setText(String.valueOf(resultNum));
                        }else {
                            String currentNum = collectNumber.getText().toString();
                            int resultNum;
                            if(finalIsEnroll){
                                saveIV.setImageResource(R.drawable.heart_fill);
                                resultNum = Integer.valueOf(currentNum) + 1;
                                isSave = true;
                            }else {
                                saveIV.setImageResource(R.drawable.heart);
                                resultNum = Integer.valueOf(currentNum) - 1;
                                isSave = false;
                            }
                            collectNumber.setText(String.valueOf(resultNum));
                        }
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void handleFollow(boolean isEnroll) {
        String url= "";
        if(isEnroll){
            url = Constant.globalServerUrl + "/follow/follow";
        }else {
            url = Constant.globalServerUrl + "/follow/remove";
        }
        final String logUrl = url;
        final boolean finalIsEnroll = isEnroll;
        GetUserInfo getUserInfo = new GetUserInfo(mContext);
        if(!getUserInfo.isUserLogined())   {
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
        paramsMap.put("followedUserId", storageAuthorId);
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug onFailure", logUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", logUrl + "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<Object> saveResult =
                            (CommonResponse<Object>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<Object>>() {}.getType());
                    if(saveResult.getStatus() == 200){
                        Log.d("dkdebug","save success");
                        if(finalIsEnroll){
                            followTV.setText("已关注");
                            isFollow = true;
                        }else {
                            followTV.setText("关注");
                            isFollow = false;
                        }
                    }
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void setHotSearch() {
        String url= Constant.globalServerUrl + "/abbreviation/getHotSearchResults";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("temp", "");
        final String finalUrl = url;
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug onFailure", finalUrl + "e=" + e);
            }

            @Override
            public void onResponse(String response) {
                if(Constant.LOG_LEVEL == "DEV") {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                }
                Log.d("dkdebug", finalUrl + "response" + response);
                Gson gson = new Gson();
                try{
                    CommonResponse<List<AbbreviationPlusModel>> hotResult =
                            (CommonResponse<List<AbbreviationPlusModel>>)gson.fromJson(response,
                                    new TypeToken<CommonResponse<List<AbbreviationPlusModel>>>() {}.getType());
                    List<String> tempList = new ArrayList<>();
                    for(AbbreviationPlusModel hotAbbr: hotResult.getData()){
                        tempList.add(hotAbbr.getAbbrName());
                    }
                    String[] hotAbbrArray = tempList.toArray(new String[tempList.size()]);
                    CustomeButtonGroupView customeButtonGroupView = getActivity().findViewById(R.id.hotPanel);
                    customeButtonGroupView.setTitle("热门搜索");
                    customeButtonGroupView.setTagList(hotAbbrArray);
                    customeButtonGroupView.hideCleanButton();
                    customeButtonGroupView.setOnItemClickListener(new CustomeButtonGroupView.OnTagClickListener() {
                        @Override
                        public void onTagClick(String keyword) {
                            mainEditText.setText(keyword);
                        }
                    });
                } catch (Exception e) {
                    Log.d("dkdebug onResponse", "e=" + e);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        if(!mainEditText.getText().toString().isEmpty()){
//            showDetail(mainEditText.getText().toString());
            commentList.clear();
            getComments(currentEntryId);
        }
    }
}
