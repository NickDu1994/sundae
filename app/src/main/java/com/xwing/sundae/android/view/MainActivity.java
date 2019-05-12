package com.xwing.sundae.android.view;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.xwing.sundae.R;
import com.xwing.sundae.android.view.explore.ExploreFragment;
import com.xwing.sundae.android.view.index.IndexFragment;
import com.xwing.sundae.android.view.message.MessageFragment;
import com.xwing.sundae.android.view.my.MyFragment;
import com.xwing.sundae.android.view.post.PostFragment;


public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener,
        ExploreFragment.OnFragmentInteractionListener,
        PostFragment.OnFragmentInteractionListener,
        MessageFragment.OnFragmentInteractionListener,
        MyFragment.OnFragmentInteractionListener {

    private String TAG = "MainActivity";
    private int lastSelectedPosition;

    private BottomNavigationBar mBottomNavigationBar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private IndexFragment mIndexFragment;
    private ExploreFragment mExploreFragment;
    private PostFragment mPostFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        setDefaultFragment();
        initView();
    }

    private void initView() {
        mBottomNavigationBar = findViewById(R.id.navigationBar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.colorNavigationBarInactivedBg);

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.index_actived, "首页").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.index).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.explore_actived, "发现").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.explore).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.post_actived, "发布").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.post).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
                .addItem(new BottomNavigationItem(R.drawable.message_actived, "消息").setActiveColorResource(R.color.colorPrimary).setInactiveIconResource(R.drawable.message).setInActiveColorResource(R.color.colorNavigationBarInactivedText))
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
                        Toast.makeText(MainActivity.this, "click 0", Toast.LENGTH_SHORT).show();
                        if(mIndexFragment == null){
                            mIndexFragment = IndexFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mIndexFragment);
                        } else {
                            fragmentTransaction.show(mIndexFragment);
                        }
                        break;
                    case 1:  // explore
                        Toast.makeText(MainActivity.this, "click 1", Toast.LENGTH_SHORT).show();
                        if(mExploreFragment == null){
                            mExploreFragment = ExploreFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mExploreFragment);
                        } else {
                            fragmentTransaction.show(mExploreFragment);
                        }
                        break;
                    case 2:  // post
                        Toast.makeText(MainActivity.this, "click 2", Toast.LENGTH_SHORT).show();
                        if(mPostFragment == null){
                            mPostFragment = PostFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mPostFragment);
                        } else {
                            fragmentTransaction.show(mPostFragment);
                        }
                        break;
                    case 3:  // message
                        Toast.makeText(MainActivity.this, "click 3", Toast.LENGTH_SHORT).show();
                        if(mMessageFragment == null){
                            mMessageFragment = MessageFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mMessageFragment);
                        } else {
                            fragmentTransaction.show(mMessageFragment);
                        }
                        break;
                    case 4:  // my
                        Toast.makeText(MainActivity.this, "click 4", Toast.LENGTH_SHORT).show();
                        if(mMyFragment == null){
                            mMyFragment = MyFragment.newInstance("","");
                            fragmentTransaction.add(R.id.mainContainer, mMyFragment);
                        } else {
                            fragmentTransaction.show(mMyFragment);
                        }
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
}
