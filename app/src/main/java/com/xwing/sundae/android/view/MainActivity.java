package com.xwing.sundae.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.WeiboPopupWindow.MoreWindow;
import com.xwing.sundae.android.view.explore.ExploreFragment;
import com.xwing.sundae.android.view.explore.FollowFragment;
import com.xwing.sundae.android.view.explore.RankFragment;
import com.xwing.sundae.android.view.index.IndexFragment;
import com.xwing.sundae.android.view.message.MessageFragment;
import com.xwing.sundae.android.view.my.MyFragment;
import com.xwing.sundae.android.view.post.PostFragment;


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

    private BottomNavigationBar mBottomNavigationBar;
    private ShapeBadgeItem mShapeBadgeItem;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private IndexFragment mIndexFragment;
    private ExploreFragment mExploreFragment;
    private PostFragment mPostFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;
    private MoreWindow mMoreWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
//        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this,"user");
//        sharedPreferencesHelper.clear();
        setDefaultFragment();
        initView();
    }

    private void initView() {
        mBottomNavigationBar = findViewById(R.id.navigationBar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.colorNavigationBarBg);

        mShapeBadgeItem = new ShapeBadgeItem()
                .setShape(ShapeBadgeItem.SHAPE_OVAL)
                .setShapeColor(R.color.colorPrimary)
                .setShapeColorResource(R.color.colorPrimary)
                .setSizeInDp(this,10,10)
                .setEdgeMarginInDp(this,2)
//                .setSizeInPixels(30,30)
//                .setEdgeMarginInPixels(-1)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(true);

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.index_actived, "首页").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.index).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.explore_actived, "发现").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.explore).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.post_actived, "").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.post).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.message_actived, "消息").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.message).setInActiveColorResource(R.color.colorNavigationBarInactivedText).setBadgeItem(mShapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.my_actived, "我").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.my).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener(){

            @Override
            public void onTabSelected(int position) {
                lastSelectedPosition = position;
                fragmentTransaction = fragmentManager.beginTransaction();
                hideFragment(fragmentTransaction);
                switch (position){
                    case 0:  // index

                        if(mIndexFragment == null){
                            mIndexFragment = IndexFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
                        } else {
                            fragmentTransaction.show(mIndexFragment);
                        }
                        break;
                    case 1:  // explore
                        if(mExploreFragment == null){
                            mExploreFragment = ExploreFragment.newInstance("","");
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

                        showMoreWindow(findViewById(R.id.mainContainer));
                        //For navigation logic, go to below
                        break;
                    case 3:  // message
                        if(mMessageFragment == null){
                            mMessageFragment = MessageFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mMessageFragment);
                        } else {
                            fragmentTransaction.show(mMessageFragment);
                        }
                        break;
                    case 4:  // my

                        boolean isLogin = false;//Todo
//                        if(isLogin) {
                            if(mMyFragment == null){
                                mMyFragment = MyFragment.newInstance("","");
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
                        Log.e(TAG , "navigation tab index catching error");
                            break;
                }//switch
                fragmentTransaction.commit();
            }//onTabSelected
        });
    }

    private void setDefaultFragment() {
        mIndexFragment = new IndexFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
        fragmentTransaction.commit();
    }

    public void gotoMyFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
       // MyFragment myFragment = new MyFragment();
        //fragmentTransaction.add(R.id.mainContainer, myFragment);
        if(mMyFragment == null){
            fragmentTransaction.remove(mPostFragment);
            mMyFragment = MyFragment.newInstance("","");
            fragmentTransaction.add(R.id.mainContainer, mMyFragment);
        } else {
            fragmentTransaction.remove(mPostFragment);
            fragmentTransaction.show(mMyFragment);
        }
        fragmentTransaction.commit();
    }

    public void hidePostFragment(){
        fragmentTransaction.hide(mPostFragment);

    }
    private void hideFragment(FragmentTransaction transaction){
        if (mIndexFragment != null){
            transaction.hide(mIndexFragment);
        }

        if(mExploreFragment != null){
            transaction.hide(mExploreFragment);
        }

        if(mPostFragment != null) {
            transaction.hide(mPostFragment);
        }

        if(mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        if(mMyFragment != null) {
            transaction.hide(mMyFragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "MainActivity recieve message from fragment" + uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void showMoreWindow(View view) {
        Log.e(TAG , "enter showMoreWindow");
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }
        mMoreWindow.showMoreWindow(view,100, getSupportFragmentManager());
    }

    public void clickCiTiao(View view){
        fragmentTransaction = fragmentManager.beginTransaction();
        mMoreWindow.hideMoreWindow();
        Toast.makeText(this, "点击了词条", Toast.LENGTH_SHORT).show();
        if(mPostFragment == null){
            mPostFragment = PostFragment.newInstance("1","");

            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
        } else {
            fragmentTransaction.remove(mPostFragment);
            mPostFragment = PostFragment.newInstance("1","");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);

        }
        fragmentTransaction.commit();

    }

    public void clickXinDe(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        mMoreWindow.hideMoreWindow();
        if(mPostFragment == null){
            mPostFragment = PostFragment.newInstance("2","");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
        } else {
            fragmentTransaction.remove(mPostFragment);
            mPostFragment = PostFragment.newInstance("2","");
            fragmentTransaction.add(R.id.mainContainer, mPostFragment);

        }
        fragmentTransaction.commit();
       // mMoreWindow.hideMoreWindow();
       // Toast.makeText(this, "点击了心得", Toast.LENGTH_SHORT).show();
    }

    public void triggerBottomNavigationBar(boolean isShow){
        if(isShow){
            mBottomNavigationBar.setVisibility(View.VISIBLE);
            //mBottomNavigationBar.show();
        }else{
            mBottomNavigationBar.setVisibility(View.GONE);
            //mBottomNavigationBar.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==6||requestCode==4||requestCode==5)
        {
           if(mPostFragment!=null){
               mPostFragment.onActivityResult(requestCode, resultCode, data);
           }


        }
    }
}
