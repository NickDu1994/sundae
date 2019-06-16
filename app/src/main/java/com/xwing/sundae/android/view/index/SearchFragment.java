package com.xwing.sundae.android.view.index;

import android.content.Context;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.CustomeButtonGroupView;
import com.xwing.sundae.android.util.SharedPreferencesUtil;
import com.xwing.sundae.android.view.MainActivity;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PANEL_SUGGESTION = 1;
    private static final int PANEL_LIST = 2;
    private static final int PANEL_DETAIL = 3;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Button cancelButton;
    private ImageButton cleanButton;
    private EditText mainEditText;
    private LinearLayout suggestionPanel;
    private LinearLayout listPanel;
    private LinearLayout detailPanel;
    private ListView listView;

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
                if(count != 0) {
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
        mainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(getContext());
                    String currentKeywordNote = spUtil.getSP("keyword_note");
                    Log.d("dk","dkdebug currentKeywordNote=" +currentKeywordNote);
                    currentKeywordNote = mainEditText.getText().toString() + "," + currentKeywordNote;
                    Log.d("dk","dkdebug new currentKeywordNote=" +currentKeywordNote);
                    spUtil.putSP("keyword_note",currentKeywordNote);
                    Log.d("dk","dkdebug currentKeywordNote=" +spUtil.getSP("keyword_note"));
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
                mainEditText.setText("");
            }
        });

        SharedPreferencesUtil spUtil = SharedPreferencesUtil.getInstance(getContext());
        Log.d("dk","dkdebug");
        if(spUtil.getSP("keyword_note")!= null){
            Log.d("dkdebug keywordNote=", spUtil.getSP("keyword_note"));
        }

        String[] testName = spUtil.getSP("keyword_note").split(",");
        String[] testName2 = {"apple2","title2","layout2","LinearLayout2","new2","child2"};
        CustomeButtonGroupView customeButtonGroupView = getActivity().findViewById(R.id.historyPanel);
        customeButtonGroupView.setTagList(testName);
        CustomeButtonGroupView customeButtonGroupView2 = getActivity().findViewById(R.id.hotPanel);
        customeButtonGroupView2.setTagList(testName2);
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
        for(int i=0;i<5;i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("label", "数据列1-");
            keywordList.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), keywordList, R.layout.item_search_result, new String[]{"label"}, new int[]{R.id.label});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "position=" + position, Toast.LENGTH_SHORT).show();
                displayController(PANEL_DETAIL);
                HtmlTextView htmlTextView = (HtmlTextView) getActivity().findViewById(R.id.html_text);
                htmlTextView.setHtml("<h2>This content come from html</h2><p>This content come from html</p><img src=\"https://img.alicdn.com/tfs/TB1Fym0cUCF3KVjSZJnXXbnHFXa-760-460.jpg\"/>",
                        new HtmlHttpImageGetter(htmlTextView));
            }
        });
    }
}
