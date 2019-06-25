package com.xwing.sundae.android.view.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.xwing.sundae.R;
import com.xwing.sundae.android.adapter.MessageAdapter;
import com.xwing.sundae.android.model.MessageModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.GetUserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<MessageModel> messageList = new ArrayList<>();

    XRefreshView xRefreshView;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    private Handler handler = new Handler();
    UserInfo userInfo;
    GetUserInfo getUserInfo;
    int currentPage = 0;
    Boolean isLast = false;
    Long currentUserId;
    TextView no_text;

    private String no_text_value = "oh ho! 你没有任何消息哦~";

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("dandan", "oncreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("dandan", "createview");
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("dandan", "created");
        no_text = getActivity().findViewById(R.id.no_text);
        no_text.setVisibility(View.GONE);
        xRefreshView = getActivity().findViewById(R.id.message_list_wrapper);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(messageList, getContext());
        recyclerView.setAdapter(messageAdapter);

        // 添加删除(取消关注)监听器
        messageAdapter.setOnDelListener(new MessageAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < messageList.size()) {
                    Long messageId = messageList.get(pos).getId();
                    Long user_id = userInfo.getId();
                    // call remove api
                    removeMessage(user_id, messageId, pos);
                    Toast.makeText(getActivity(), "删除消息:" + pos, Toast.LENGTH_SHORT).show();

                }
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });

        getUserInfo = new GetUserInfo(getActivity());
        if (null != getUserInfo) {
            userInfo = getUserInfo.getUserInfo().getData();
            currentUserId = userInfo.getId();
            getMessageList();
        }

        setPullandRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.w("change", "www");
        super.onHiddenChanged(hidden);
        Log.w("dandan", "message");
        if (hidden) {

        } else {
            if (null != getUserInfo) {
                userInfo = getUserInfo.getUserInfo().getData();
                Long newId = userInfo.getId();
//                if (!currentUserId.equals(newId)) {
                    messageList.clear();
                    currentPage = 0;
                    currentUserId = newId;
                    isLast = false;
                    readAllMessage();
                    getMessageList();
//                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("dandan", "message resume");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d("dandan", "onattach");
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
        Log.d("dandan", "detach");
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

    private void readAllMessage() {
        Long user_id = userInfo.getId();
        String url = Constant.REQUEST_URL_MY + "/message/readAll";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getActivity(), "server error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "全部已读", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessageList() {
        String url = Constant.REQUEST_URL_MY + "/message/getMessageList";
        Long user_id = userInfo.getId();

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        paramsMap.put("page", Integer.toString(currentPage));
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getContext(), "getMessageList Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "getMessageList" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    LinkedTreeMap data = (LinkedTreeMap) map_res.get("data");
                    Object content = data.get("content");
                    isLast = (Boolean) data.get("last");
                    String tmp = gson.toJson(content);
                    MessageModel[] messageModels = gson.fromJson(tmp, MessageModel[].class);
                    messageList.addAll(Arrays.asList(messageModels));
                    if(null==messageList || messageList.size() == 0) {
                        no_text.setVisibility(View.VISIBLE);
                        no_text.setText(no_text_value);
                        recyclerView.setVisibility(View.GONE);
                        xRefreshView.setLoadComplete(true);
                    } else {
                        no_text.setVisibility(View.GONE);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            messageAdapter.notifyDataSetChanged();
                            xRefreshView.stopRefresh();
                            xRefreshView.stopLoadMore();

                        }
                    });

                } catch (Exception e) {
                    Log.e("loginPostRequestError", "error" + e);
                }
            }
        });
    }

    private void setPullandRefresh() {
        xRefreshView.setPinnedTime(1000);
        //如果刷新时不想让里面的列表滑动，可以这么设置
        xRefreshView.setPinnedContent(false);
        xRefreshView.setMoveForHorizontal(true);
        //允许下拉刷新
        xRefreshView.setPullRefreshEnable(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
//        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xRefreshView.enableReleaseToLoadMore(false);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(false);
//        xRefreshView.setEmptyView(R.layout.layout_empty_view);//添加empty_view

        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                //super.onRefresh(isPullDown);
                xRefreshView.setLoadComplete(false);
                messageList.clear();
                currentPage = 0;
                isLast = false;
                getMessageList();
//                xRefreshView.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLast) {
                            currentPage++;
                            getMessageList();
                        } else {
                            xRefreshView.stopLoadMore();
                        }

                    }
                }, 2000);
            }
        });
    }

    private void removeMessage(Long user_id, Long messageId, final int pos) {
        String url = Constant.REQUEST_URL_MY + "/message/remove";
        int listSize = messageList.size();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        paramsMap.put("messageId", messageId.toString());
        paramsMap.put("listSize", String.valueOf(listSize));
        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Map<String, Object> map_res = gson.fromJson(response, Map.class);
                Object data = map_res.get("data");
                String json_data = gson.toJson(data);
                MessageModel message = gson.fromJson(json_data, MessageModel.class);
                messageList.remove(pos);
                if (message != null)
                    messageList.add(message);
                messageAdapter.notifyItemRemoved(pos);//推荐用这个
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
