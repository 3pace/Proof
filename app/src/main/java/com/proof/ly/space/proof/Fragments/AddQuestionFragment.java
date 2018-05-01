package com.proof.ly.space.proof.Fragments;

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

    private TextView txt_title;
    private FloatingActionButton fab;
    private EditText etxt_question;
    private boolean enabledNext = true;
    private QManager qManager;

    public static Fragment getInstance() {
        return new AddQuestionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qManager = ((MainActivity)getActivity()).getqManager();
        qManager.initJson();
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
        txt_title = itemView.findViewById(R.id.txt_add_question_title);
        etxt_question = itemView.findViewById(R.id.etxt_question);
        fab = itemView.findViewById(R.id.fab);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        txt_title.setTypeface(typeface);
        etxt_question.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confrim();
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        if (USManager.hasLogIn())
            txt_title.append(", " + ((MainActivity) getActivity())
                    .getDbManager()
                    .getUserById(USManager.getUID())
                    .get("username"));
        fab.setAlpha(0.2f);
        setDisabledNext();


        etxt_question.addTextChangedListener(new TextWatcher() {
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
        if (!enabledNext) {
            enabledNext = true;
            fab.setEnabled(true);
            fab.animate().alpha(1f).setDuration(300).start();
            fab.setClickable(true);
            fab.show();
        }
    }

    private void setDisabledNext() {
        if (enabledNext) {
            enabledNext = false;
            fab.setClickable(false);
            fab.hide();
        }
    }


    private void confrim() {
        String text = etxt_question.getText().toString().trim();
        if (text.length() > 0) {
            etxt_question.setEnabled(false);
            etxt_question.setTextColor(getResources().getColor(R.color.grey));
            qManager.createJsonQuestion(text);
            MQuestionManagerFragment.nextPage();
            setDisabledNext();
        }
    }
}
