package com.proof.ly.space.proof.CustomViews;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.proof.ly.space.proof.R;

public class MBottomSheet extends BottomSheetDialog {

    private Context mContext;

    public MBottomSheet(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_container, null);
        setContentView(view);
;

    }
}
