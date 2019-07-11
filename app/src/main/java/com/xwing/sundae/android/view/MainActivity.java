package com.xwing.sundae.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.google.gson.Gson;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.WeiboPopupWindow.MoreWindow;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.view.explore.ExploreFragment;
import com.xwing.sundae.android.view.explore.FollowFragment;
import com.xwing.sundae.android.view.explore.RankFragment;
import com.xwing.sundae.android.view.index.IndexFragment;
import com.xwing.sundae.android.view.message.MessageFragment;
import com.xwing.sundae.android.view.my.MyFragment;
import com.xwing.sundae.android.view.post.PostFragment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener,
        ExploreFragment.OnFragmentInteractionListener,
        PostFragment.OnFragmentInteractionListener,
        MessageFragment.OnFragmentInteractionListener,
        MyFragment.OnFragmentInteractionListener,
        FollowFragment.OnFragmentInteractionListener,
        RankFragment.OnFragmentInteractionListener {

    private String TAG = "MainActivity";
    private int lastSelectedPosition;

    private static BottomNavigationBar mBottomNavigationBar;
    private static ShapeBadgeItem mShapeBadgeItem;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private IndexFragment mIndexFragment;
    private ExploreFragment mExploreFragment;
    private PostFragment mPostFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;
    private MoreWindow mMoreWindow;
    GetUserInfo getUserInfo;
    UserInfo userInfo;
    private int currentIndex = 0;
    long lastTime = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
//        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this,"user");
//        sharedPreferencesHelper.clear();
        getUserInfo = new GetUserInfo(this);

        if (getUserInfo.isUserLogined()) {
            userInfo = getUserInfo.getUserInfo().getData();
        }
        
        setDefaultFragment();
        initView();
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void initView() {
        mBottomNavigationBar = findViewById(R.id.navigationBar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.colorNavigationBarBg);

        setBottomNavigationItem(mBottomNavigationBar, 16, 16, 10);

        mShapeBadgeItem = new ShapeBadgeItem()
                .setShape(ShapeBadgeItem.SHAPE_OVAL)
                .setShapeColor(R.color.colorPrimary)
                .setShapeColorResource(R.color.colorPrimary)
                .setSizeInDp(this, 5, 5)
                .setEdgeMarginInDp(this, 10)
//                .setSizeInPixels(30,30)
//                .setEdgeMarginInPixels(-1)
                .setGravity(Gravity.TOP | Gravity.END);

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.index_actived, "首页").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.index).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.explore_actived, "发现").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.explore).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.post_actived, "发布").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.post).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.message_actived, "消息").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.message).setInActiveColorResource(R.color.colorNavigationBarInactivedText).setBadgeItem(mShapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.my_actived, "我").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.my).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        if (getUserInfo.isUserLogined()) {
            checkUnreadMessage();
        } else {
            mShapeBadgeItem.hide();
        }

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {

            @Override
            public void onTabSelected(int position) {
                lastSelectedPosition = position;
                fragmentTransaction = fragmentManager.beginTransaction();
                hideFragment(fragmentTransaction);
                switch (position) {
                    case 0:  // index
                        currentIndex = 0;
                        if (getUserInfo.isUserLogined()) {
                            checkUnreadMessage();
                        }
                        if (mIndexFragment == null) {
                            mIndexFragment = IndexFragment.newInstance("", "");
                            fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
                        } else {
                            fragmentTransaction.show(mIndexFragment);
                        }
                        break;
                    case 1:  // explore
                        if (!getUserInfo.isUserLogined()) {
                            Toast.makeText(MainActivity.this, "请先进行登录", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            switchSelectTab(4);
                            return;
                        }
                        if (getUserInfo.isUserLogined()) {
                            checkUnreadMessage();
                        }
                        currentIndex = 1;
                        if (mExploreFragment == null) {
                            mExploreFragment = ExploreFragment.newInstance("", "");
                            fragmentTransaction.add(R.id.mainContainer, mExploreFragment);
                        } else {
                            fragmentTransaction.show(mExploreFragment);
                        }
                        break;
                    case 2:  // post
                        /*List<Fragment> fragmentslist =  fragmentManager.getFragments();
                        boolean existflag= false;
                        for(Fragment f :fragmentslist){
                            if(f ==mPostFragment){
                                existflag=true;
                            }
                        }
                        if(!existflag ){
                            mPostFragment = PostFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
                        } else {
                            fragmentTransaction.show(mPostFragment);
                        }*/
                        // showMoreWindow(getWindow().getDecorView().findViewById(R.id.mainContainer));
                        currentIndex = 2;
                        if (!getUserInfo.isUserLogined()) {
                            Toast.makeText(MainActivity.this, "请先进行登录", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            switchSelectTab(4);
                            return;
                        }
                        showMoreWindow(findViewById(R.id.mainContainer));
                        //For navigation logic, go to below
                        break;
                    case 3:  // message
                        if (!getUserInfo.isUserLogined()) {
                            Toast.makeText(MainActivity.this, "请先进行登录", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            switchSelectTab(4);
                            return;
                        }
                        currentIndex = 3;
                        if (mMessageFragment == null) {
                            mMessageFragment = MessageFragment.newInstance("", "");
                            fragmentTransaction.add(R.id.mainContainer, mMessageFragment);
                        } else {
                            fragmentTransaction.show(mMessageFragment);
                        }
                        mShapeBadgeItem.hide();
                        break;
                    case 4:  // my

                        boolean isLogin = false;//Todo
                        currentIndex = 4;
                        if (getUserInfo.isUserLogined()) {
                            checkUnreadMessage();
                        }

//                        if(isLogin) {
                        if (mMyFragment == null) {
                            mMyFragment = MyFragment.newInstance("", "");
                            fragmentTransaction.add(R.id.mainContainer, mMyFragment);
                        } else {
                            fragmentTransaction.show(mMyFragment);
                        }
//                        } else {
//                            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }

                        break;
                    default:
                        Log.e(TAG, "navigation tab index catching error");
                        break;
                }//switch
                fragmentTransaction.commit();
            }//onTabSelected
        });
    }

    public static void switchSelectTab(int index) {
        if (index <= 4) {
            mBottomNavigationBar.selectTab(index);
        }
    }

    public static void removeMessageBadge() {
        mShapeBadgeItem.hide();
    }

    public static void addMessageBadge() {
        if (mShapeBadgeItem.isHidden())
            mShapeBadgeItem.show();
    }

    public boolean checkUnreadMessage() {
        String url = Constant.REQUEST_URL_MY + "/message/checkHasUnread";
        if (userInfo == null) {
            userInfo = getUserInfo.getUserInfo().getData();
        }

        Long user_id = userInfo.getId();

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", user_id.toString());
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MainActivity.this, "网络有点问题哦，稍后再试试吧！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                Log.e("loginPostRequest", "checkunread" + response);

                try {
                    Map<String, Object> map_res = gson.fromJson(response, Map.class);
                    Boolean data = (Boolean) map_res.get("data");
                    if (data) {
                        if (mShapeBadgeItem.isHidden())
                            mShapeBadgeItem.show();
                    } else {
                        mShapeBadgeItem.hide();
                    }
                } catch (Exception e) {
                    Log.e("checkunreadmessage", "error" + e);
                }
            }
        });
        return true;
    }

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mainContainer")) {
                try {
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        View view = mTabContainer.getChildAt(j);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(22), dip2px(0), dip2px(12), dip2px(0));
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private void setDefaultFragment() {
        mIndexFragment = new IndexFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
        fragmentTransaction.commit();
    }

    public void gotoMyFragment() {
        mBottomNavigationBar.selectTab(4);
    }

    public void hidePostFragment() {
        mBottomNavigationBar.selectTab(0);
        //  fragmentTransaction.hide(mPostFragment);

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mIndexFragment != null) {
            transaction.hide(mIndexFragment);
        }

        if (mExploreFragment != null) {
            transaction.hide(mExploreFragment);
        }

        if (mPostFragment != null) {
            transaction.hide(mPostFragment);
        }

        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        if (mMyFragment != null) {
            transaction.hide(mMyFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMoreWindow != null && mMoreWindow.isShowing()) {
                switchSelectTab(0);
                mMoreWindow.dismiss();
                Log.d("dandan in", String.valueOf(mMoreWindow.isShowing()));
                return false;
            } else {

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastTime < 2 * 1000) {
                    super.onKeyDown(keyCode, event);
                } else {
                    Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                    lastTime = currentTime;
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (Constant.LOG_LEVEL == "DEV") {
            Toast.makeText(this, "MainActivity recieve message from fragment" + uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showMoreWindow(View view) {
        Log.e(TAG, "enter showMoreWindow");
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(view, 100, getSupportFragmentManager());
    }

    public void clickCiTiao(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        mMoreWindow.hideMoreWindow();
        if (Constant.LOG_LEVEL == "DEV") {
            Toast.makeText(this, "点击了词条", Toast.LENGTH_SHORT).show();
        }
        if (mPostFragment == null) {
            mPostFragment = PostFragment.newInstance("1", "");

            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
        } else {
            fragmentTransaction.remove(mPostFragment);
            mPostFragment = PostFragment.newInstance("1", "");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);

        }
        fragmentTransaction.commit();

    }

    public void clickXinDe(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        mMoreWindow.hideMoreWindow();
        if (mPostFragment == null) {
            mPostFragment = PostFragment.newInstance("2", "");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
        } else {
            fragmentTransaction.remove(mPostFragment);
            mPostFragment = PostFragment.newInstance("2", "");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);

        }
        fragmentTransaction.commit();
        // mMoreWindow.hideMoreWindow();
        // Toast.makeText(this, "点击了心得", Toast.LENGTH_SHORT).show();
    }

    public void triggerBottomNavigationBar(boolean isShow) {
        if (isShow) {
            mBottomNavigationBar.setVisibility(View.VISIBLE);
            //mBottomNavigationBar.show();
        } else {
            mBottomNavigationBar.setVisibility(View.GONE);
            //mBottomNavigationBar.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6 || requestCode == 4 || requestCode == 5) {
            if (mPostFragment != null) {
                mPostFragment.onActivityResult(requestCode, resultCode, data);
            }


        }
    }
}
