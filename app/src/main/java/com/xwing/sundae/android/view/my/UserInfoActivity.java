package com.xwing.sundae.android.view.my;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xwing.sundae.R;
import com.xwing.sundae.android.customview.UserInfoOneLineView;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UserInfoOneLineView.OnRootClickListener{

    private static final String EDITTYPE = "EDITTYPE";
    ImageView info_user_pic;
    TextView return_icon, profile_title, save_info;

    LinearLayout info_list;

    RelativeLayout user_logo_field;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    protected static Uri tempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initEvent();
        showUserPic();

        add_info_list();
        requestPermission();
    }


    private void initEvent() {

        user_logo_field.setOnClickListener(this);
    }

    private void initView() {
        info_user_pic = findViewById(R.id.info_user_pic);
        info_list = findViewById(R.id.user_info);
        user_logo_field = findViewById(R.id.user_logo_field);
    }

    private void add_info_list() {
        UserInfoOneLineView nick_name = new UserInfoOneLineView(this)
                .init(R.mipmap.id,this.getString(R.string.string_nickname),true,true)
                .setOnRootClickListener(this, "nickname");
        info_list.addView(nick_name);
        UserInfoOneLineView gender = new UserInfoOneLineView(this)
                .init(R.mipmap.gender,this.getString(R.string.string_gender),false,true)
                .setOnRootClickListener(this, "gender");
        info_list.addView(gender);
        UserInfoOneLineView region = new UserInfoOneLineView(this)
                .init(R.mipmap.region,this.getString(R.string.string_region),false,true)
                .setOnRootClickListener(this, "region");
        info_list.addView(region);
        UserInfoOneLineView my_profile = new UserInfoOneLineView(this)
                .init(R.mipmap.profile,this.getString(R.string.string_my_profile),false,true)
                .setOnRootClickListener(this, "profile");
        info_list.addView(my_profile);
    }

    private void showUserPic() {
        RequestOptions options = new RequestOptions().
                circleCropTransform();
        Glide.with(this)
                .load(R.mipmap.girl)
                .apply(options)
                .into(info_user_pic);
    }

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
     * @param
     * @param picdata
     */
    protected void setImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            iv_personal_icon.setImageBitmap(photo);
//            uploadPic(photo);
//        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_logo_field:
                chooseImage();
                break;
        }
    }

    @Override
    public void onRootClick(View view) {
        Log.v("userInfoActivity",view.getTag().toString());
        String tag_name = view.getTag().toString();
        Bundle data = new Bundle();
        data.putString("name",tag_name);
        Intent intent = new Intent(UserInfoActivity.this, EditLineActivity.class);
        intent.putExtra(EDITTYPE, data);
        startActivity(intent);
//        switch ((String) view.getTag()) {
//
//            case "nickname":
//
//
//                break;
//            case "gender":
//                break;
//            case "region":
//                break;
//            case "profile":
//                intent = new Intent(UserInfoActivity.this, EditLineActivity.class);
//                intent.putExtra(EDITTYPE, tag_name);
//                startActivity(intent);
//        }

    }

}
