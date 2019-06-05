package com.xwing.sundae.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwing.sundae.R;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.view.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static java.util.Objects.isNull;

public class CommonMethod {


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String checkVersion(Context context) {
        //拿到包的管理器
        PackageManager pm = context.getPackageManager();
        try {
            //封装了所有的功能清单中的数据
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得当前时间
     */
    public static String getCurrentTime(String format) {
        if (format.equals("")) {
            format = "yyyy年MM月dd日 HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * dp 转换成 px
     * @param context
     * @param dp
     * @return
     */
    public static int convertDpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * @return 获取屏幕的高 单位：px
     */
    public static int getScreenHeightPx(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);
//            windowManager.getDefaultDisplay().getRealMetrics(dm);//API 17
            return dm.heightPixels;
        }
        return 0;
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    /**
     * 判断用户是否登录
     *
     * @param response
     * @return boolean
     */
    public static boolean ifLogin(String response) {
        CommonResponse<UserInfo> userInfoCommonResponse = getUserInfo(response);
        if(!"".equals(response) && null!=userInfoCommonResponse) {
            userInfoCommonResponse.getData().getAuth().equals("true");
            return true;
        }
        return false;
//        return (getUserInfo(response))!=null && (getUserInfo(response).getData().getAuth().equals("true"));
    }


    /**
     * 判断用户是否登录
     *
     * @param response
     * @return userInfoBean
     */
    public static CommonResponse<UserInfo> getUserInfo(String response) {
        Log.d("maggieTest","getUserInfo");
        if(!"".equals(response) && null!=response) {
            Gson gson = new Gson();
            CommonResponse<UserInfo> userInfoBean =
                    (CommonResponse<UserInfo>) gson.fromJson(response,
                            new TypeToken<CommonResponse<UserInfo>>() {
                            }.getType());
            return userInfoBean;
        }
        return null;
    }


}
