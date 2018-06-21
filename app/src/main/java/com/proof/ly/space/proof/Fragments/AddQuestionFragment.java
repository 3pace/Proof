package com.proof.ly.space.proof.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.melnykov.fab.FloatingActionButton;
import com.proof.ly.space.proof.Fragments.windows.MQuestionManagerFragment;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Helpers.USManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;


/**
 * Created by aman on 25.03.18.
 */

public class AddQuestionFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewTitle;
    private FloatingActionButton mFloatingActionButton;
    private EditText mEditTextQuestion;
    private boolean mIsNextEnabled = true;
    private QManager mQManager;
    private MainActivity mActivity;

    public static Fragment getInstance() {
        return new AddQuestionFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQManager = mActivity.getQuestionManager();
        mQManager.initJson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initOnClick();
        initSetters();
    }

    @Override
    public void initViews(View itemView) {
        mTextViewTitle = itemView.findViewById(R.id.txt_add_question_title);
        mEditTextQuestion = itemView.findViewById(R.id.etxt_question);
        mFloatingActionButton = itemView.findViewById(R.id.fab);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mTextViewTitle.setTypeface(typeface);
        mEditTextQuestion.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        if (USManager.hasLogIn())
            mTextViewTitle.append(", " +
                    mActivity.getDatabaseManager()
                    .getUserById(USManager.getUID())
                    .get("username"));
        mFloatingActionButton.setAlpha(0.2f);
        setDisabledNext();


        mEditTextQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 4) {
                    setEnabledNext();
                } else {
                    setDisabledNext();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setEnabledNext() {
        if (!mIsNextEnabled) {
            mIsNextEnabled = true;
            mFloatingActionButton.setEnabled(true);
            mFloatingActionButton.animate().alpha(1f).setDuration(300).start();
            mFloatingActionButton.setClickable(true);
            mFloatingActionButton.show();
        }
    }

    private void setDisabledNext() {
        if (mIsNextEnabled) {
            mIsNextEnabled = false;
            mFloatingActionButton.setClickable(false);
            mFloatingActionButton.hide();
        }
    }


    private void confirm() {
        String text = mEditTextQuestion.getText().toString().trim();
        if (text.length() > 0) {
            mEditTextQuestion.setEnabled(false);
            mEditTextQuestion.setTextColor(getResources().getColor(R.color.grey));
            mQManager.createJsonQuestion(text);
            MQuestionManagerFragment.nextPage();
            setDisabledNext();
        }
    }
}
