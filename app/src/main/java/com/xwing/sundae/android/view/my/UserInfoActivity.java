package com.xwing.sundae.android.view.my;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.UserInfoOneLineView;
import com.xwing.sundae.android.model.CommonResponse;
import com.xwing.sundae.android.model.MyFollowerModel;
import com.xwing.sundae.android.model.UserInfo;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.CommonMethod;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.GlideImageLoader;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.SharedPreferencesHelper;
import com.xwing.sundae.android.view.GetUserInfo;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.internal.Utils;
import okhttp3.Call;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UserInfoOneLineView.OnRootClickListener {

//    private static final String REQUEST_URL_MY = "http://192.168.31.17:8080/user";

    /**
     * user 编辑的内容类型
     */
    private static final String EDITTYPE = "EDITTYPE";

    /**
     * user的头像区域(头像+修改头像)
     */
    RelativeLayout user_logo_field;

    /**
     * user的头像
     */
    ImageView info_user_pic;

    /**
     * user的所有可编辑列表
     */
    LinearLayout info_list;

    /**
     * user 个人信息
     */

    private static GetUserInfo getUserInfo;

    private UserInfo userInfo;

    /**
     * selected province
     */
    private String province;
    /**
     * selected city
     */
    private String city;
    /**
     * selected district
     */
    private String district;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * SharedPreferencesHelper 对象
     */
    SharedPreferencesHelper sharedPreferencesHelper;

    /**
     * user selected Region
     */
    private String selectedRegion;


    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    protected static Uri tempUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getUserInfo = new GetUserInfo(this);

        initView();
        initEvent();
        showUserPic();

        if (null != getUserInfo.getUserInfo() && !"".equals(getUserInfo.getUserInfo())) {
            userInfo = getUserInfo.getUserInfo().getData();
            add_info_list(userInfo);
        }

        requestPermission();
    }

    @Override
    protected void onResume() {
        if (null != getUserInfo.getUserInfo() && !"".equals(getUserInfo.getUserInfo())) {
            add_info_list(getUserInfo.getUserInfo().getData());
        }
        super.onResume();
    }

    /**
     * user点击修改头像的事件监听
     */
    private void initEvent() {
        user_logo_field.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void initView() {
        info_user_pic = findViewById(R.id.info_user_pic);
        info_list = findViewById(R.id.user_info);
        user_logo_field = findViewById(R.id.user_logo_field);
    }

    /**
     * 将用户可编辑的列表通过自定义view传入
     */
    private void add_info_list(com.xwing.sundae.android.model.UserInfo userinfo) {
        info_list.removeAllViews();
        // 加入昵称列
        UserInfoOneLineView nick_name = new UserInfoOneLineView(this)
                .initMine(R.drawable.id, this.getString(R.string.string_nickname), userinfo.getNickname(), true)
                .setOnRootClickListener(this, "nickname");
        info_list.addView(nick_name);

        // 加入性别列
        UserInfoOneLineView gender = new UserInfoOneLineView(this)
                .initMine(R.drawable.gender, this.getString(R.string.string_gender), userinfo.getGender(), true)
                .setOnRootClickListener(this, "gender");
        info_list.addView(gender);
        // 加入地区列
        UserInfoOneLineView region = new UserInfoOneLineView(this)
                .initMine(R.drawable.region, this.getString(R.string.string_region), userinfo.getRegion(), true)
                .setOnRootClickListener(this, "region");
        info_list.addView(region);
        // 加入个人简介列
        UserInfoOneLineView my_profile = new UserInfoOneLineView(this)
                .initMine(R.drawable.profile, this.getString(R.string.string_my_profile), userinfo.getProfile(), true)
                .setOnRootClickListener(this, "profile");
        info_list.addView(my_profile);

    }

    /**
     * 处理user头像(圆形)
     */
    private void showUserPic() {
        RequestOptions options = new RequestOptions().
                circleCropTransform();
        Glide.with(this)
                .load(R.drawable.defaultpic)
                .apply(options)
                .into(info_user_pic);
    }

    /**
     * 拿到修改头像的权限
     */
    private void requestPermission() {
        AndPermission.with(UserInfoActivity.this)
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .start();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String photoBase64 = GlideImageLoader.bitmapToBase64(photo);
            uploadImage(photoBase64);
//            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            iv_personal_icon.setImageBitmap(photo);
            Glide.with(this)
                    .load(photo)
                    .into(info_user_pic);
//            uploadPic(photo);
        }
    }

    private void uploadImage(String photoBase64) {
        String url = Constant.REQUEST_URL_MY + "/image/upload/img";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("uploadFile", photoBase64);

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(UserInfoActivity.this, "upload Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                String res = response;
                Toast.makeText(UserInfoActivity.this, "upload succ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 选择图片上传
     */
    private void chooseImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 监听点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_logo_field:
                chooseImage();
                break;
        }
    }

    /**
     * root点击事件监听
     *
     * @param view
     */
    @Override
    public void onRootClick(View view) {

        String tag_name = view.getTag().toString();
        if (!"region".equals(tag_name)) {
            Bundle data = new Bundle();
            data.putString("name", tag_name);
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra(EDITTYPE, data);
            startActivity(intent);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                selectAddress();
            }
        }
    }

    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(UserInfoActivity.this)
                .textSize(16)
                .title("地址选择")
                .titleBackgroundColor("#eeeeee")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("上海市")
                .city("上海市")
                .district("浦东新区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                province = citySelected[0];
                //城市
                city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                district = citySelected[2];
                //
                selectedRegion = province + "-" + city + "-" + district;
                Log.e("MAGGIE==>", province + city + district);
                userInfo.setRegion(selectedRegion);
                add_info_list(userInfo);

                updateUserInfoPost("region", selectedRegion);
            }
        });

    }

    private void updateUserInfoPost(String editType, String editValue) {
        Long user_id = userInfo.getId();
        String url = Constant.REQUEST_URL_MY + "/user/modify/" + user_id;

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put(editType, editValue);

        OkhttpUtil.okHttpPost(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(UserInfoActivity.this, "update user server error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                Toast.makeText(UserInfoActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                try {
                    CommonResponse<com.xwing.sundae.android.model.UserInfo> userInfoBean = CommonMethod.getUserInfo(response);
                    getUserInfo.setUserInfo(userInfoBean.getData());
                    sharedPreferencesHelper.remove("user_info");
                    sharedPreferencesHelper.put("user_info", response);
                    finish();
                } catch (Exception e) {
                    Log.v("update user failed", "error" + e);
                }
            }
        });
    }
}
