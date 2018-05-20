package com.proof.ly.space.proof.Fragments;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.proof.ly.space.proof.Helpers.MConstans;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewResult, mTextViewFinish, mTextViewResultPercent, mTextViewTime;
    private ImageView mImageViewResultIcon;
    private Button mButtonRestart,mButtonShare;
    private boolean mInit = false;
    private boolean mOnceIconAnimation = false;
    private View mScreenView;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 222;

    public ResultFragment() {

    }

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
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
        mInit = true;
        mTextViewResult = itemView.findViewById(R.id.txt_result);
        mTextViewFinish = itemView.findViewById(R.id.txt_finish_title);
        mButtonRestart = itemView.findViewById(R.id.btn_restart);
        mTextViewResultPercent = itemView.findViewById(R.id.txt_result_percent);
        mImageViewResultIcon = itemView.findViewById(R.id.img_view_result);
        mScreenView = itemView.findViewById(R.id.linear_layout_screen);
        mButtonShare = itemView.findViewById(R.id.btn_share);
        mTextViewTime = itemView.findViewById(R.id.txt_time);

    }

    @Override
    public void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntum.ttf");
        mTextViewResult.setTypeface(typeface);
        mTextViewFinish.setTypeface(typeface);
        mButtonRestart.setTypeface(typeface);
        mTextViewResultPercent.setTypeface(typeface);
        mButtonShare.setTypeface(typeface);
        mTextViewTime.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();


            }
        });
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission())
                    takeResultScreen();
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        Drawable mShareButtonIcon = getResources().getDrawable(R.drawable.baseline_share_black_24);
        mShareButtonIcon.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        mButtonShare.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mShareButtonIcon, null);


        HashMap<String, Integer> resultMap = ((MainActivity) getActivity()).getmQuestionManager().getTestingResult();
        int correctQuestions = resultMap.get(MConstans.TESTING_RESULT_CORRECT_QUESTIONS_COUNT);
        int notCorrectQuestions = resultMap.get(MConstans.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT);
        int totalPoints = resultMap.get(MConstans.TESTING_RESULT_POINTS);
        int mistakes = resultMap.get(MConstans.TESTING_RESULT_MISTAKES);
        int percent = resultMap.get(MConstans.TESTING_RESULT_PERCENT);
        int notChecked = resultMap.get(MConstans.TESTING_RESULT_NOT_CHECKED_COUNT);
        int questionsCount = resultMap.get(MConstans.TESTING_RESULT_QUESTIONS_COUNT);
        String spentTime = ((MainActivity) getActivity()).getStopTime();

        StringBuilder resultText = new StringBuilder("");
        resultText
                .append(getResources().getString(R.string.result))
                .append(" ")
                .append(correctQuestions)
                .append(" ")
                .append(getResources().getString(R.string.from).toLowerCase())
                .append(" ")
                .append(questionsCount)
                .append(".\n")
                .append(getResources().getString(R.string.vy_nabrali))
                .append(" ")
                .append(totalPoints)
                .append(" ")
                .append(getWord(totalPoints, "баллов", "балл", "балла"))
                .append(", ")
                .append(getResources().getString(R.string.sovershili))
                .append(" ")
                .append(mistakes)
                .append(" ")
                .append(getWord(mistakes, "ошибок", "ошибку", "ошибки"));

        if (notChecked != 0)
            resultText
                    .append(" ")
                    .append(getResources().getString(R.string.ne_otevili))
                    .append(" ")
                    .append(notChecked)
                    .append(" ")
                    .append(getWord(notChecked, "вопросов", "вопрос", "вопроса"))
                    .append(".");
        else
            resultText.append(".");
        mTextViewResult.setText(resultText);

        startCountAnimation("%", 0, percent, percent < 20 ? 1000 : 3000);

        Drawable mResultIcon = getResultIcon(percent);
        mResultIcon.setColorFilter(((MainActivity) getActivity()).getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        mImageViewResultIcon.setImageDrawable(mResultIcon);


        mTextViewTime.setText(getResources().getString(R.string.spent_time).concat(" ").concat(spentTime));


        System.out.println("\ncorrect questions: " + resultMap.get(MConstans.TESTING_RESULT_CORRECT_QUESTIONS_COUNT));
        System.out.println("not correct questions: " + resultMap.get(MConstans.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT));
        System.out.println("points: " + resultMap.get(MConstans.TESTING_RESULT_POINTS));
        System.out.println("mistakes: " + resultMap.get(MConstans.TESTING_RESULT_MISTAKES));
        System.out.println("percent: " + resultMap.get(MConstans.TESTING_RESULT_PERCENT) + "%");
        System.out.println("not checked: " + resultMap.get(MConstans.TESTING_RESULT_NOT_CHECKED_COUNT));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mInit) {
            initSetters();
        }
    }

    public String getWord(int count, String word0, String word1, String word2) {
        int rem = count % 100;
        if (rem < 11 || rem > 14) {
            rem = count % 10;
            if (rem == 1) return word1;
            if (rem >= 2 && rem <= 4) return word2;
        }
        return word0;
    }

    private void startCountAnimation(final String suffix, int start, int end, long duration) {
        mImageViewResultIcon.setVisibility(View.INVISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextViewResultPercent.setText(animation.getAnimatedValue().toString().concat(suffix));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImageViewResultIcon.setVisibility(View.VISIBLE);
                blinkView();
            }
        });
        animator.start();
    }

    private void blinkView() {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(40); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(3);
        if (!mOnceIconAnimation) {
            mImageViewResultIcon.startAnimation(anim);
            mOnceIconAnimation = true;
        }

    }

    private Drawable getResultIcon(int totalPercent) {
        if (totalPercent > -100 && totalPercent < 35)
            return getResources().getDrawable(R.drawable.round_sentiment_dissatisfied_black_36);
        else if (totalPercent >= 35 && totalPercent < 65)
            return getResources().getDrawable(R.drawable.round_sentiment_satisfied_black_36);
        else
            return getResources().getDrawable(R.drawable.round_mood_black_36);
    }

    private void takeResultScreen() {
        mTextViewFinish.setText(getResources().getString(R.string.app_name).toUpperCase());
        mScreenView.setDrawingCacheEnabled(true);
        mScreenView.buildDrawingCache();
        mImageViewResultIcon.setDrawingCacheEnabled(true);
        Bitmap bitmap = mScreenView.getDrawingCache();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        String fname = "/axiom-result.jpg";
        File file = new File(myDir, fname);
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextViewFinish.setText(getResources().getText(R.string.result_test_title));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ();
        StrictMode.setVmPolicy (builder.build ());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            return true;
        }
        return false;
    }


}
