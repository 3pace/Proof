package com.proof.ly.space.proof.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.melnykov.fab.FloatingActionButton;
import com.proof.ly.space.proof.Adapters.RecyclerAddAnswerAdapter;
import com.proof.ly.space.proof.Data.NewAnswer;
import com.proof.ly.space.proof.Fragments.windows.MQuestionManagerFragment;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.Interfaces.OnItemClickView;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

/**
 * Created by aman on 25.03.18.
 */

public class AddAnswersFragment extends Fragment implements FragmentInterface{
    private TextView mTextViewTitle;
    private Button mButttonAdd;
    private FloatingActionButton mFloatingActionButton;
    private Typeface mTypeface;
    private RecyclerView mRecyclerView;
    private RecyclerAddAnswerAdapter mAdapter;
    private EditText mEditText;
    private boolean mIsNextEnabled = true;
    private QManager mQManager;


    public static Fragment getInstance(){
        return new AddAnswersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQManager = ((MainActivity)getActivity()).getmQuestionManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_anwers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initSetters();
        initOnClick();
    }

    @Override
    public void initViews(View itemView) {
        mTextViewTitle = itemView.findViewById(R.id.txt_add_answer_title);
        mButttonAdd = itemView.findViewById(R.id.btn_add);
        mEditText = itemView.findViewById(R.id.etxt_answer);
        mRecyclerView = itemView.findViewById(R.id.rview);
        mFloatingActionButton = itemView.findViewById(R.id.fab);

    }

    @Override
    public void initTypeface() {
        mTypeface = ((MainActivity)getActivity()).getTypeface();
        mTextViewTitle.setTypeface(mTypeface);
        mButttonAdd.setTypeface(mTypeface);
        mEditText.setTypeface(mTypeface);
    }

    @Override
    public void initOnClick() {
        mButttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = mEditText.getText().toString().trim();
                addAnswer(answer);
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        mAdapter.setOnItemClick(new OnItemClickView() {
            @Override
            public void onClick(int pos,View view) {
                switch (view.getId()) {
                    case R.id.txt_answer:
                        mAdapter.setAnswerCorrect(pos);
                        break;
                    case R.id.img_clear:
                        mAdapter.removeAnswer(pos);
                        fabVisible();
                        break;
                }
            }
        });
    }



    @Override
    public void initObjects() {
        mAdapter = new RecyclerAddAnswerAdapter();
    }

    @Override
    public void initSetters() {
        mAdapter.setTypeface(mTypeface);
        mAdapter.setDisabledColor(((MainActivity)getActivity()).getDisabledColor());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
        setDisabledNext();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    setEnabledNext();
                } else {
                    setDisabledNext();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fabVisible();
    }
    private void setEnabledNext() {
        if (!mIsNextEnabled) {
            mIsNextEnabled = true;
            mButttonAdd.setEnabled(true);
            mButttonAdd.animate().alpha(1f).setDuration(300).start();

        }
    }

    private void setDisabledNext() {
        if (mIsNextEnabled) {
            mIsNextEnabled = false;
            mButttonAdd.setEnabled(false);
            mButttonAdd.animate().alpha(0.2f).setDuration(300).start();
        }
    }
    public void addAnswer(String answer){
        mAdapter.addAnswer(answer);
        fabVisible();
        mEditText.setText("");
    }
    private void fabVisible(){
        if (mAdapter.getItemCount()>=2) {
            mFloatingActionButton.show();
            mFloatingActionButton.setClickable(true);
        }
        else {
            mFloatingActionButton.hide();
            mFloatingActionButton.setClickable(false);
        }
    }

    private void confirm() {
        int cc = 0,nc=0;
        for (NewAnswer answers : mAdapter.getArrayList()){
            if (answers.isCorrect()) cc++;
            else nc++;
        }
        if (cc == 0){
            toast(getResources().getString(R.string.one_correct_answer));
            return;
        }
        else if (nc == 0){
            toast(getResources().getString(R.string.one_not_correct_answer));
            return;
        }
        if (mAdapter.getItemCount()>=2){
            mEditText.setEnabled(false);
            mQManager.createJsonAnswers(mAdapter.getArrayList());
            MQuestionManagerFragment.nextPage();
            mFloatingActionButton.setClickable(false);
            mFloatingActionButton.hide();

        }
    }

    public void toast(String t){
        Toast.makeText(getContext(),t,Toast.LENGTH_SHORT).show();
    }
}
