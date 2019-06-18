package com.xwing.sundae.android.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.SharedPreferencesHelper;

/**
 * Author: Maggie
 * Date: 2019/6/4
 * Time: 13:41
 */
public class GetUserInfo {
    public SharedPreferencesHelper sharedPreferencesHelper;
    public static Context context;

    public static UserInfo userInfo = new UserInfo();

    public static Boolean ifAuth = false;

    /**
     * 构造器
     *
     * @param context
     */
    public GetUserInfo(Context context) {
        this.context = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(context, "user");
    }

    /**
     * 声明SharePerference
     *
     * @return
     */
    public SharedPreferencesHelper getSharePreferenceObj() {
        return this.sharedPreferencesHelper;
    }

    /**
     * 得到登录后的用户信息
     *
     * @return userInfoBean
     */
    public CommonResponse<UserInfo> getUserInfo() {

        String response = sharedPreferencesHelper.get("user_info","").toString();

        if ("".equals(response)) {
            return null;
        }

        Log.d("maggieTest", "getUserInfo");
        if (!"".equals(response) && null != response) {
            Gson gson = new Gson();
            CommonResponse<UserInfo> userInfoBean = gson.fromJson(response,
                    new TypeToken<CommonResponse<UserInfo>>() {
                    }.getType());
            return userInfoBean;
        }
        return null;
    }

    public static void setUserInfo(UserInfo info) {
        userInfo = info;
    }


    /**
     * 判断用户登录状态
     *
     * @return
     */
    public boolean ifLogin() {
        if (null == getUserInfo() || "".equals(getUserInfo())) {
            return false;
        }
        UserInfo userInfoCommonResponse = getUserInfo().getData();
        int status = getUserInfo().getStatus();
        if (200==status && null != userInfoCommonResponse) {
            return true;
        } else {
            return false;
        }

    }

}
