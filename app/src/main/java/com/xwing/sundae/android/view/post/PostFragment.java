package com.xwing.sundae.android.view.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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

import com.xwing.sundae.R;
import com.xwing.sundae.android.util.CallBackUtil;
import com.xwing.sundae.android.util.Constant;
import com.xwing.sundae.android.util.LoadingView;
import com.xwing.sundae.android.util.OkhttpUtil;
import com.xwing.sundae.android.util.PostImageUtil;
import com.xwing.sundae.android.view.GetUserInfo;
import com.xwing.sundae.android.view.LoginActivity;
import com.xwing.sundae.android.view.MainActivity;
import com.xwing.sundae.android.view.index.IndexFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


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
    private String content;
    private ImageView postBackShow;
    private ImageView postBackShow2;
    private ImageView postBackShow3;
    private Uri postBackShowuri;

    private Uri postBackShowuri2;
    private Uri postBackShowuri3;
    private LoadingView loadingView;
    private final int REQUEST_CODE_PICKER = 100;
    private EditText editText1;
    private EditText editText2;
    private IndexFragment mIndexFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    GetUserInfo getUserInfo;

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
        View view = null;
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

            mEditor.setEditorHeight(200);
            mEditor.setEditorFontSize(22);
            mEditor.setEditorFontColor(Color.BLACK);
            //mEditor.setEditorBackgroundColor(Color.BLUE);
            //mEditor.setBackgroundColor(Color.BLUE);
            //mEditor.setBackgroundResource(R.drawable.bg);
            mEditor.setPadding(10, 10, 10, 10);
            //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
            mEditor.setPlaceholder("请输入正文");
            //mEditor.setInputEnabled(false);
            editText1 = view.findViewById(R.id.post_edit1);
            editText2 = view.findViewById(R.id.post_edit2);
            editText1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(15);
                    // drawable.setGradientRadius(20);
                    drawable.setStroke(2, Color.GRAY);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        editText1.setBackground(drawable);
                    } else {
                        editText1.setBackgroundDrawable(drawable);
                    }


                }
            });
            editText2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(15);
                    drawable.setStroke(2, Color.GRAY);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        editText2.setBackground(drawable);
                    } else {
                        editText2.setBackgroundDrawable(drawable);
                    }

                }
            });

            // = (TextView) view.findViewById(R.id.preview);
            mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                @Override
                public void onTextChange(String text) {
                    content = text;
                    Log.d(TAG, " post onTextChange: " + content);
                    // mPreview.setText(text);
                }
            });
            postBackShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 2);

                }
            });
            postBackShow2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 4);

                }
            });
            postBackShow3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 5);

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

       /* view.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 3);


            }
        });*/

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

            view.findViewById(R.id.post_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostFragment postFragment = (PostFragment) getFragmentManager().findFragmentById(R.id.mainContainer);
                    getFragmentManager().beginTransaction().hide(postFragment).commit();


                }
            });
            view.findViewById(R.id.post_enter).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    String base64Image = "";
                    String base64Image2 = "";
                    String base64Image3 = "";
                    String title1 = editText1.getText().toString();
                    String title2 = editText2.getText().toString();
                    if (editText1 == null || editText1.length() <= 0) {
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setCornerRadius(15);
                        drawable.setStroke(2, Color.RED);
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            editText1.setBackground(drawable);
                        } else {
                            editText1.setBackgroundDrawable(drawable);
                        }
                        return;
                    } else if (editText2 == null || editText2.length() <= 0) {
                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setCornerRadius(15);
                        drawable.setStroke(2, Color.RED);
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            editText2.setBackground(drawable);
                        } else {
                            editText2.setBackgroundDrawable(drawable);
                        }
                        return;
                    }
                    if ((postBackShow).getDrawable() == null || (postBackShow2).getDrawable() == null || (postBackShow3).getDrawable() == null) {

                        Toast.makeText(getActivity().getApplicationContext(), "请上传3张图片", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Bitmap bitmap = ((BitmapDrawable) (postBackShow).getDrawable()).getBitmap();

                        base64Image = PostImageUtil.imgToBase64(50, bitmap);
                        Bitmap bitmap2 = ((BitmapDrawable) (postBackShow2).getDrawable()).getBitmap();

                        base64Image = PostImageUtil.imgToBase64(50, bitmap);
                        Bitmap bitmap3 = ((BitmapDrawable) (postBackShow3).getDrawable()).getBitmap();

                        base64Image = PostImageUtil.imgToBase64(50, bitmap);
                        Log.d(TAG, "base64Image: " + base64Image);
                    }
                    Map<String, String> paramMap = new HashMap<>();
                    Long userId = getUserInfo.getUserInfo().getData().getId();
                    paramMap.put("userId", userId.toString());
                    paramMap.put("title1", title1);
                    paramMap.put("title2", title2);
                    paramMap.put("backgroundImage", base64Image);
                    paramMap.put("backgroundImage2", base64Image2);
                    paramMap.put("backgroundImage3", base64Image3);
                    if (content != null) {
                        paramMap.put("content", content);
                    }
                    loadingView.setVisibility(View.VISIBLE);
                    loadingView.showLoading();

                    String post_url = Constant.REQUEST_URL_MY + "/abbreviation/upload";

                    OkhttpUtil.okHttpPost(post_url, paramMap, new CallBackUtil<Response>() {
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
                    List<File> fileList = new ArrayList<>();

                    File file = new File(getImagePath(postBackShowuri, ""));

                    File file2 = new File(getImagePath(postBackShowuri2, ""));
                    File file3 = new File(getImagePath(postBackShowuri3, ""));
                    fileList.add(file);
                    fileList.add(file2);
                    fileList.add(file3);
                    OkhttpUtil.okHttpUploadListFile("http://10.0.2.2:8080/image/upload/img", fileList, "uploadFile", "image", new CallBackUtil() {
                        @Override
                        public Object onParseResponse(Call call, Response response) {
                            return response;
                        }

                        @Override
                        public void onFailure(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(Object response) {
                            Log.d(TAG, "onResponse: " + response.toString());

                        }
                    });
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
            }


        } else if (requestCode == 3) {
            if (data != null) {
                Uri uri = data.getData();
                String imagePath = getImagePath(uri, null);
                Log.d(TAG, "onActivityResult: " + imagePath);
                mEditor.insertImage("file:" + imagePath,
                        "dachshund");

            }
        } else if (requestCode == 4) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                postBackShowuri2 = uri;
                postBackShow2.setBackgroundResource(0);
                postBackShow2.setImageURI(uri);
            }


        } else if (requestCode == 5) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                postBackShowuri3 = uri;
                postBackShow3.setBackgroundResource(0);
                postBackShow3.setImageURI(uri);
            }


        }

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection 来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
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
