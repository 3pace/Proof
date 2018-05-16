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
import com.proof.ly.space.proof.Data.NewAnswers;
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
    private TextView txt_title;
    private Button btn_add;
    private FloatingActionButton fab;
    private Typeface typeface;
    private RecyclerView rview;
    private RecyclerAddAnswerAdapter adapter;
    private EditText etxt;
    private boolean enabledNext = true;
    private QManager qManager;


    public static Fragment getInstance(){
        return new AddAnswersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qManager = ((MainActivity)getActivity()).getmQuestionManager();

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
        txt_title = itemView.findViewById(R.id.txt_add_answer_title);
        btn_add = itemView.findViewById(R.id.btn_add);
        etxt = itemView.findViewById(R.id.etxt_answer);
        rview = itemView.findViewById(R.id.rview);
        fab = itemView.findViewById(R.id.fab);

    }

    @Override
    public void initTypeface() {
        typeface = ((MainActivity)getActivity()).getTypeface();
        txt_title.setTypeface(typeface);
        btn_add.setTypeface(typeface);
        etxt.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = etxt.getText().toString().trim();
                addAnswer(answer);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        adapter.setOnItemClick(new OnItemClickView() {
            @Override
            public void onClick(int pos,View view) {
                switch (view.getId()) {
                    case R.id.txt_answer:
                        adapter.setAnswerCorrect(pos);
                        break;
                    case R.id.img_clear:
                        adapter.removeAnswer(pos);
                        fabVisible();
                        break;
                }
            }
        });
    }



    @Override
    public void initObjects() {
        adapter = new RecyclerAddAnswerAdapter();
    }

    @Override
    public void initSetters() {
        adapter.setTypeface(typeface);
        adapter.setDisabledColor(((MainActivity)getActivity()).getDisabledColor());
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);
        rview.setHasFixedSize(false);
        setDisabledNext();
        etxt.addTextChangedListener(new TextWatcher() {
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
        if (!enabledNext) {
            enabledNext = true;
            btn_add.setEnabled(true);
            btn_add.animate().alpha(1f).setDuration(300).start();

        }
    }

    private void setDisabledNext() {
        if (enabledNext) {
            enabledNext = false;
            btn_add.setEnabled(false);
            btn_add.animate().alpha(0.2f).setDuration(300).start();
        }
    }
    public void addAnswer(String answer){
        adapter.addAnswer(answer);
        fabVisible();
        etxt.setText("");
    }
    private void fabVisible(){
        if (adapter.getItemCount()>=2) {
            fab.show();
            fab.setClickable(true);
        }
        else {
            fab.hide();
            fab.setClickable(false);
        }
    }

    private void confirm() {
        int cc = 0,nc=0;
        for (NewAnswers answers : adapter.getArrayList()){
            if (answers.isCorrect()) cc++;
            else nc++;
        }
        if (cc == 0){
            toast("Должен быть хоть 1 правильный ответ");
            return;
        }
        else if (nc == 0){
            toast("Должен быть хоть 1 неправильный ответ");
            return;
        }
        if (adapter.getItemCount()>=2){
            etxt.setEnabled(false);
            qManager.createJsonAnswers(adapter.getArrayList());
            MQuestionManagerFragment.nextPage();
            fab.setClickable(false);
            fab.hide();

        }
    }

    public void toast(String t){
        Toast.makeText(getContext(),t,Toast.LENGTH_SHORT).show();
    }
}
