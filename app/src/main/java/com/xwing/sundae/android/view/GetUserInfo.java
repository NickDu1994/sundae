package com.xwing.sundae.android.view;

import android.content.Context;
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
    public static SharedPreferencesHelper sharedPreferencesHelper;
    Context context;

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
    public CommonResponse<UserInfo> getUserInfo() {
        getSharePreferenceObj();
        String response = sharedPreferencesHelper.get("user_info", "").toString();
        CommonResponse<UserInfo> userInfoBean = new CommonResponse<UserInfo>();
        Log.d("maggieTest", "getUserInfo");
        if (!"".equals(response) && null != response) {
            Gson gson = new Gson();
            userInfoBean = (CommonResponse<UserInfo>) gson.fromJson(response,
                            new TypeToken<CommonResponse<UserInfo>>() {
                            }.getType());
            return userInfoBean;
        }
        return userInfoBean;
    }

    /**
     * 判断用户登录状态
     * @return
     */
    public boolean ifLogin() {
        CommonResponse<UserInfo> userInfoCommonResponse = getUserInfo();
        if (null != userInfoCommonResponse && userInfoCommonResponse.getData().getAuth().equals("true")) {
            return true;
        }
        return false;
    }

}
