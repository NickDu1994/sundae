package com.xwing.sundae.android.view.my;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xwing.sundae.R;

public class EditLineFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "EditLineFragment";

    EditText edit_text_value;
    ImageView clear_edit_text_icon;

    TextView edit_line_cancel,edit_line_save;

    RelativeLayout relative_edit_field;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_line,null);
        edit_text_value = view.findViewById(R.id.edit_text_value);
        clear_edit_text_icon = view.findViewById(R.id.clear_edit_text_icon);
        edit_line_cancel = view.findViewById(R.id.edit_line_cancel);
        edit_line_save = view.findViewById(R.id.edit_line_save);

        clear_edit_text_icon.setOnClickListener(this);
        edit_line_cancel.setOnClickListener(this);
        edit_line_save.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_edit_text_icon:
                Log.v(TAG,edit_text_value.getText().toString());
                edit_text_value.setText("");
                break;
            case R.id.edit_line_cancel:
                getActivity().finish();
        }
    }
}
