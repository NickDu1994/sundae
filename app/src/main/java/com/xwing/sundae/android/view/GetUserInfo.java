package com.xwing.sundae.android.view;

import android.content.Context;

import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.SharedPreferencesHelper;

/**
 * Author: Maggie
 * Date: 2019/6/4
 * Time: 13:41
 */
public class GetUserInfo {
    public static SharedPreferencesHelper sharedPreferencesHelper;
    Context context;

    public static UserInfo userInfo = new UserInfo();

    public static Boolean ifAuth = false;



    /**
     * 构造器
     *
     * @param context
     */
    public GetUserInfo(Context context) {
        this.context = context;
    }

    /**
     * 声明SharePerference
     *
     * @return
     */
    public GetUserInfo getSharePreferenceObj() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this.context, "user");
        return this;
    }

    /**
     * 得到登录后的用户信息
     *
     * @return userInfoBean
     */
//    public CommonResponse<UserInfo> getUserInfo() {
//        getSharePreferenceObj();
//        String response = sharedPreferencesHelper.get("user_info", "").toString();
//        CommonResponse<UserInfo> userInfoBean = new CommonResponse<UserInfo>();
//        Log.d("maggieTest", "getUserInfo");
//        if (!"".equals(response) && null != response) {
//            Gson gson = new Gson();
//            userInfoBean = (CommonResponse<UserInfo>) gson.fromJson(response,
//                            new TypeToken<CommonResponse<UserInfo>>() {
//                            }.getType());
//            return userInfoBean;
//        }
//        return userInfoBean;
//    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo info) {
        userInfo = info;
    }

    public static Boolean getIfAuth() {
        return ifAuth;
    }

    public static void setIfAuth(Boolean auth) {
        ifAuth = auth;
    }

    /**
     * 判断用户登录状态
     * @return
     */
    public boolean ifLogin() {
        UserInfo userInfoCommonResponse = getUserInfo();
        if (null != userInfoCommonResponse) {
            return true;
        }
        return false;
    }

}
