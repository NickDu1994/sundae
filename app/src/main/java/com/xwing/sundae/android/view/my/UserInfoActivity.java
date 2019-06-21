package com.xwing.sundae.android.view.my;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.xwing.sundae.android.util.PostImageUtil;
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

    private static final String IMAGE_FILE_NAME = "user_head_icon.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        sharedPreferencesHelper = new SharedPreferencesHelper(this,"user");
        getUserInfo = new GetUserInfo(this);

        initView();
        initEvent();
//        showUserPic();
        userInfo = getUserInfo.getUserInfo().getData();
        RequestOptions options = new RequestOptions().
                    circleCropTransform();
        Glide.with(this)
                .load(userInfo.getAvatarUrl())
                .apply(options)
                .into(info_user_pic);

//        if (null !=userInfo && !"".equals(userInfo)) {
//
//            RequestOptions options = new RequestOptions().
//                    circleCropTransform();
//            if(null != userInfo.getAvatarUrl() && "".equals(userInfo.getAvatarUrl())) {
//                Glide.with(this)
//                        .load(userInfo.getAvatarUrl())
//                        .apply(options)
//                        .into(info_user_pic);
//            } else {
//                Glide.with(this)
//                        .load(R.drawable.defaultpic)
//                        .apply(options)
//                        .into(info_user_pic);
//            }
//            add_info_list(userInfo);
//        }

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
    private void add_info_list(UserInfo userinfo) {
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

//        CommonMethod.mkdirSundaeDirectory();
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Uri uri = data.getData();
        info_user_pic.setBackgroundResource(0);
        info_user_pic.setImageURI(uri);

        Bitmap bitmap = ((BitmapDrawable) (info_user_pic).getDrawable()).getBitmap();
        String base64Image = PostImageUtil.imgToBase64(50, bitmap);

        updateUserInfoPost("avatar",base64Image);

    }

    private void uploadImage(String base64) {

        String url = Constant.REQUEST_URL_MY + "/image/upload/img";

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("uploadFile", base64);

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
//        String[] items = {"选择本地照片", "拍照"};
        String[] items = {"选择本地照片"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        choosePic();
                        break;
//                    case TAKE_PICTURE: // 拍照
//                        takePic();
//                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 选择图片方法
     */
    private void choosePic() {
//        Log.e("Maggie Image", "choose pic");
//        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
//        openAlbumIntent.setType("image/*");
//        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        if (openAlbumIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//        } else {
//            Toast.makeText(UserInfoActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
//        }

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    /**
     * 拍照方法
     */
    private void takePic() {
        Log.e("Maggie Image", "take pic");
//        Intent intent;
        Uri pictureUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + Environment.DIRECTORY_DCIM + "/Camera/" + System.currentTimeMillis() + ".jpg");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            pictureUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileProvider", cameraSavePath);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            pictureUri = Uri.fromFile(cameraSavePath);
//        }
        pictureUri = Uri.fromFile(cameraSavePath);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        //也就是我之前创建的存放头像的文件夹（目录）
//        File pictureFile = new File(CommonMethod.getSundaeRootDirectory(), IMAGE_FILE_NAME);
//        // 判断当前系统
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            //这一句非常重要
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
//            pictureUri = FileProvider.getUriForFile(this,
//                    "com.xwing.sundae.file", pictureFile);
//        } else {
//            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            pictureUri = Uri.fromFile(pictureFile);
//        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        Log.e("maggietest", "before take photo" + pictureUri.toString());
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            Uri uri;
            switch (requestCode) {
                case TAKE_PICTURE:
                    setImageToView(data);
                    break;
                case CHOOSE_PICTURE:
                    setImageToView(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    sharedPreferencesHelper.remove("user_info");
                    sharedPreferencesHelper.put("user_info", response);
//                    CommonResponse<UserInfo> userInfoBean = getUserInfo.getUserInfo();
//                    getUserInfo.setUserInfo(userInfoBean.getData());
                    finish();
                } catch (Exception e) {
                    Log.v("update user failed", "error" + e);
                }
            }
        });
    }
}
