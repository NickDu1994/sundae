package com.xwing.sundae.android.view.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.lcw.library.imagepicker.ImagePicker;
import com.xwing.sundae.R;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.GlideLoader;
import com.xwing.sundae.android.util.LoadingView;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.PostImageUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;
import com.xwing.sundae.android.view.index.IndexFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PostFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RichEditor mEditor;
    private TextView mPreview;
    private OnFragmentInteractionListener mListener;
    private String content ;
    private ImageView postBackShow;
    private ImageView postBackShow2;
    private ImageView postBackShow3;
    private Uri  postBackShowuri;

    private Uri  postBackShowuri2;
    private Uri  postBackShowuri3;
    private LoadingView loadingView;
    private final int REQUEST_CODE_PICKER = 100;
    private EditText editText1;
    private EditText editText2;
    private IndexFragment mIndexFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<String> mImagePaths;
    GetUserInfo getUserInfo;

    private MaterialTextField materialTextField1;
    private  MaterialTextField materialTextField2;
    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        fragmentManager = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =null;
        getUserInfo = new GetUserInfo(getActivity());
        if (null == getUserInfo || null == getUserInfo.getUserInfo() || "".equals(getUserInfo.getUserInfo())) {
            Toast.makeText(getContext(), "请先进行登录", Toast.LENGTH_SHORT).show();
            mIndexFragment = new IndexFragment();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, mIndexFragment);
            fragmentTransaction.commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            view = inflater.inflate(R.layout.fragment_post, container, false);
            // Inflate the layout for this fragment
            mEditor = (RichEditor) view.findViewById(R.id.editor);
            loadingView = view.findViewById(R.id.loadingView);
            loadingView.setVisibility(View.INVISIBLE);
            postBackShow = (ImageView) view.findViewById(R.id.post_back_show1);
            postBackShow2 = (ImageView) view.findViewById(R.id.post_back_show2);
            postBackShow3 = (ImageView) view.findViewById(R.id.post_back_show3);
            postBackShow2.setVisibility(View.INVISIBLE);
            postBackShow3.setVisibility(View.INVISIBLE);
            mEditor.setEditorHeight(200);
            mEditor.setEditorFontSize(22);
            mEditor.setEditorFontColor(Color.BLACK);

            mEditor.setPadding(10, 10, 10, 10);

            mEditor.setPlaceholder("请输入正文");

            materialTextField1 = view.findViewById(R.id.post_edit_t1);
            materialTextField2 = view.findViewById(R.id.post_edit_t2);
            editText1 = view.findViewById(R.id.post_edit1);
            editText2 = view.findViewById(R.id.post_edit2);
            if(mParam1!=null&&mParam1.equals("2")){
                materialTextField2.setVisibility(View.INVISIBLE);
            }else{
                materialTextField2.setVisibility(View.VISIBLE);
            }

            mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                @Override
                public void onTextChange(String text) {
                    content = text;
                    Log.d(TAG, " post onTextChange: " + content);

                }
            });
            postBackShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImagePicker.getInstance()
                            .setTitle("选择背景图")//设置标题
                            .showCamera(true)//设置是否显示拍照按钮
                            .showImage(true)//设置是否展示图片
                            .showVideo(true)//设置是否展示视频
                            .setMaxCount(3)//设置最大选择图片数目(默认为1，单选)
                            .setSingleType(true)//设置图片视频不能同时选择
                           // .setImagePaths(mImagePaths)//设置历史选择记录
                            .setImageLoader(new GlideLoader())//设置自定义图片加载器
                            .start(getActivity(), 6);


                }
            });
            postBackShow2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImagePicker.getInstance()
                            .setTitle("选择背景图")//设置标题
                            .showCamera(true)//设置是否显示拍照按钮
                            .showImage(true)//设置是否展示图片
                            .showVideo(true)//设置是否展示视频
                            .setMaxCount(2)//设置最大选择图片数目(默认为1，单选)
                            .setSingleType(true)//设置图片视频不能同时选择
                           // .setImagePaths(mImagePaths)//设置历史选择记录
                            .setImageLoader(new GlideLoader())//设置自定义图片加载器
                            .start(getActivity(), 4);


                }
            });
            postBackShow3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImagePicker.getInstance()
                            .setTitle("选择背景图")//设置标题
                            .showCamera(true)//设置是否显示拍照按钮
                            .showImage(true)//设置是否展示图片
                            .showVideo(true)//设置是否展示视频
                            .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                            .setSingleType(true)//设置图片视频不能同时选择
                           // .setImagePaths(mImagePaths)//设置历史选择记录
                            .setImageLoader(new GlideLoader())//设置自定义图片加载器
                            .start(getActivity(), 5);
//                    Intent intent = new Intent(Intent.ACTION_PICK, null);
//                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                    startActivityForResult(intent, 5);

                }
            });

            view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.undo();
                }
            });

            view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.redo();
                }
            });

            view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setBold();
                }
            });

            view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setItalic();
                }
            });

            view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setSubscript();
                }
            });

            view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setSuperscript();
                }
            });

            view.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setStrikeThrough();
                }
            });

            view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setUnderline();
                }
            });

            view.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(1);
                }
            });

            view.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(2);
                }
            });

            view.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(3);
                }
            });

            view.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(4);
                }
            });

            view.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(5);
                }
            });

            view.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(6);
                }
            });

            view.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
                private boolean isChanged;

                @Override
                public void onClick(View v) {
                    mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                    isChanged = !isChanged;
                }
            });

            view.findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
                private boolean isChanged;

                @Override
                public void onClick(View v) {
                    mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                    isChanged = !isChanged;
                }
            });

            view.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setIndent();
                }
            });

            view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setOutdent();
                }
            });

            view.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setAlignLeft();
                }
            });

            view.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setAlignCenter();
                }
            });

            view.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setAlignRight();
                }
            });

            view.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setBlockquote();
                }
            });

            view.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setBullets();
                }
            });

            view.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setNumbers();
                }
            });



            view.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = LayoutInflater.from(getActivity());

                    final View textEntryView = factory.inflate(R.layout.post_dialog_hyperlink, null);
                    final EditText editTextName = (EditText) textEntryView.findViewById(R.id.hpeditText1);
                    final EditText editTextlink = (EditText) textEntryView.findViewById(R.id.hpeditText2);
                    new AlertDialog.Builder(getActivity()).setTitle(" ")

                            .setView(textEntryView)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input1 = editTextName.getText().toString();
                                    String input2 = editTextName.getText().toString();
                                    if (input1.equals("") || input2.equals("")) {
                                        Toast.makeText(getActivity().getApplicationContext(), "内容不能为空！", Toast.LENGTH_LONG).show();
                                    } else {
                                        mEditor.insertLink(input2, input1);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null).show();


                }
            });
            view.findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.insertTodo();
                }
            });

        view.findViewById(R.id.post_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).hidePostFragment();

                }
            });
            view.findViewById(R.id.post_enter).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                String base64Image="";
                String base64Image2="";
                String base64Image3="";
                String title1 = editText1.getText().toString();
                String title2 = editText2.getText().toString();
                if(editText1==null||editText1.length()<=0){
                    materialTextField1.expand();
                    materialTextField1.setBackgroundColor(Color.RED);
                    materialTextField1.setHasFocus(true);

                    return;
                }else if(editText2==null||editText2.length()<=0){
                    materialTextField2.expand();

                    return;
                }
                if((postBackShow).getDrawable()==null||(postBackShow2).getDrawable()==null||(postBackShow3).getDrawable()==null){

                        Toast.makeText(getActivity().getApplicationContext(), "请上传3张图片", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Bitmap bitmap = ((BitmapDrawable) (postBackShow).getDrawable()).getBitmap();

                        base64Image = PostImageUtil.imgToBase64(50, bitmap);
                        Bitmap bitmap2 = ((BitmapDrawable) (postBackShow2).getDrawable()).getBitmap();

                        base64Image2 = PostImageUtil.imgToBase64(50, bitmap2);
                        Bitmap bitmap3 = ((BitmapDrawable) (postBackShow3).getDrawable()).getBitmap();

                        base64Image3 = PostImageUtil.imgToBase64(50, bitmap3);

                    }
                    Map<String, String> paramMap = new HashMap<>();
                    Long userId = getUserInfo.getUserInfo().getData().getId();
                    paramMap.put("userId", userId.toString());
                    paramMap.put("title1", title1);
                    paramMap.put("title2", title2);
                    paramMap.put("backgroundImage", base64Image);
                    paramMap.put("backgroundImage2", base64Image2);
                    paramMap.put("backgroundImage3", base64Image3);
                    paramMap.put("type",mParam1);
                    if (content != null) {
                        paramMap.put("content", content);
                    }
                    loadingView.setVisibility(View.VISIBLE);
                    loadingView.showLoading();

                    String url = Constant.REQUEST_URL_MY + "/abbreviation/upload";

                    OkhttpUtil.okHttpPost(url, paramMap, new CallBackUtil<Response>() {
                                @Override
                                public Response onParseResponse(Call call, Response response) {
                                    return response;
                                }

                                @Override
                                public void onFailure(Call call, Exception e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onFailure: ..." + e.getMessage());
                                }

                                @Override
                                public void onResponse(Response response) {
                                    try {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadingView.showSuccess();
                                            }
                                        });
                                        Log.d(TAG, "onResponse: " + response.body().string());
                                        ((MainActivity) getActivity()).gotoMyFragment();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                    );

                }
            });
       }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径

                Uri uri = data.getData();
                postBackShowuri = uri;
                postBackShow.setBackgroundResource(0);
                postBackShow.setImageURI(uri);
                postBackShow2.setVisibility(View.VISIBLE);
            }


        }

        else if(requestCode ==3){
            if(data!=null){
                Uri uri = data.getData();
                String imagePath = getImagePath(uri,null);
                Log.d(TAG, "onActivityResult: "+imagePath);
                mEditor.insertImage("file:"+imagePath,
                        "dachshund");

            }
        }
       else  if (requestCode == 4) {
           if(data!=null) {
               mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
               // 从相册返回的数据
               //if (data != null) {
               // 得到图片的全路径
               if (mImagePaths != null && mImagePaths.size() == 1) {
                   postBackShowuri2 = Uri.parse(mImagePaths.get(0));
                   postBackShow2.setBackgroundResource(0);
                   postBackShow2.setImageURI(postBackShowuri2);
                   postBackShow2.setVisibility(View.VISIBLE);
                   postBackShow3.setVisibility(View.VISIBLE);
               } else if (mImagePaths != null && mImagePaths.size() == 2) {
                   postBackShowuri2 = Uri.parse(mImagePaths.get(0));
                   postBackShow2.setBackgroundResource(0);
                   postBackShow2.setImageURI(postBackShowuri2);
                   postBackShow2.setVisibility(View.VISIBLE);
                   postBackShowuri3 = Uri.parse(mImagePaths.get(1));
                   postBackShow3.setBackgroundResource(0);
                   postBackShow3.setImageURI(postBackShowuri3);
                   postBackShow3.setVisibility(View.VISIBLE);
               }


           }
        }
        else if (requestCode == 5) {
            if(data!=null) {
                mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                // 从相册返回的数据
                //if (data != null) {
                if (mImagePaths != null && mImagePaths.size() == 1) {
                    postBackShowuri3 = Uri.parse(mImagePaths.get(0));
                    postBackShow3.setBackgroundResource(0);
                    postBackShow3.setImageURI(postBackShowuri3);
                    postBackShow3.setVisibility(View.VISIBLE);
                } else {
                    postBackShow3.setVisibility(View.VISIBLE);
                }
                // 得到图片的全路径
//                Uri uri = data.getData();
//                postBackShowuri3= uri;
//                postBackShow3.setBackgroundResource(0);
//                postBackShow3.setImageURI(uri);
                // }
            }
        }
        else if (requestCode == 6) {
            if(data!=null) {
                mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
                Log.d(TAG, "onActivityResult: 666 " + mImagePaths);
                // 从相册返回的数据
                if (mImagePaths != null && mImagePaths.size() == 1) {
                    postBackShowuri = Uri.parse(mImagePaths.get(0));
                    postBackShow.setBackgroundResource(0);
                    postBackShow.setImageURI(postBackShowuri);
                    postBackShow.setVisibility(View.VISIBLE);
                    postBackShow2.setVisibility(View.VISIBLE);
                } else if (mImagePaths != null && mImagePaths.size() == 2) {
                    postBackShowuri = Uri.parse(mImagePaths.get(0));
                    postBackShow.setBackgroundResource(0);
                    postBackShow.setImageURI(postBackShowuri);
                    postBackShow.setVisibility(View.VISIBLE);
                    postBackShowuri2 = Uri.parse(mImagePaths.get(1));
                    postBackShow2.setBackgroundResource(0);
                    postBackShow2.setImageURI(postBackShowuri2);
                    postBackShow2.setVisibility(View.VISIBLE);
                    postBackShow3.setVisibility(View.VISIBLE);
                } else if (mImagePaths != null && mImagePaths.size() == 3) {
                    postBackShowuri = Uri.parse(mImagePaths.get(0));
                    postBackShow.setBackgroundResource(0);
                    postBackShow.setImageURI(postBackShowuri);
                    postBackShow.setVisibility(View.VISIBLE);
                    postBackShowuri2 = Uri.parse(mImagePaths.get(1));
                    postBackShow2.setBackgroundResource(0);
                    postBackShow2.setImageURI(postBackShowuri2);
                    postBackShow2.setVisibility(View.VISIBLE);
                    postBackShowuri3 = Uri.parse(mImagePaths.get(2));
                    postBackShow3.setBackgroundResource(0);
                    postBackShow3.setImageURI(postBackShowuri3);
                    postBackShow3.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection 来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
