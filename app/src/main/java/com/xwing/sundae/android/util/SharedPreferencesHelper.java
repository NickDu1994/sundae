package com.xwing.sundae.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

/**
 * Author: Maggie
 * Date: 2019/5/30
 * Time: 11:46
 */
public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context, String FILE_NAME) {
        try{
            sharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }

    }


    /**
     * 存储
     *
     * @param key
     * @param object
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }


    /**
     * 获取
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public SharedPreferencesHelper clear() {
        editor.clear();
        editor.commit();
        return this;
    }

    /**
     * 查询某个key是否存在
     *
     * @param key
     * @return
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }
}
