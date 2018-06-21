package com.proof.ly.space.proof.Fragments;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.proof.ly.space.proof.Data.Lesson;
import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Helpers.MConstants;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewResult, mTextViewFinish, mTextViewResultPercent, mTextViewTime;
    private ImageView mImageViewResultIcon;
    private Button mButtonRestart, mButtonShare;
    private boolean mOnceIconAnimation = false;
    private View mScreenView;
    private MainActivity mActivity;
    private DBManager mDatabaseManager;
    private Toolbar mToolbar;
    private TextView mTextViewToolbar;
    public ResultFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseManager = mActivity.getDatabaseManager();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mToolbar = itemView.findViewById(R.id.tbar);
        mTextViewToolbar = mToolbar.findViewById(R.id.txt_tbar);
        mActivity.setSupportActionBar(mToolbar);
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
        Typeface typeface = mActivity.getTypeface();
        mTextViewToolbar.setTypeface(typeface);
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
                mActivity.getSupportFragmentManager().popBackStack();
                mActivity.getSupportFragmentManager().popBackStack();
                mActivity.replaceFragment(new MTestingFragment(), getResources().getString(R.string.tag_testing));


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
        mTextViewToolbar.setText(getResources().getString(R.string.tag_result));
        Drawable mShareButtonIcon = getResources().getDrawable(R.drawable.baseline_share_black_24);
        mShareButtonIcon.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        mButtonShare.setCompoundDrawablesWithIntrinsicBounds(null, null,
                mShareButtonIcon, null);


        HashMap<String, Integer> resultMap = mActivity.getQuestionManager().getTestingResult();
        int correctQuestions = resultMap.get(MConstants.TESTING_RESULT_CORRECT_QUESTIONS_COUNT);
        int notCorrectQuestions = resultMap.get(MConstants.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT);
        int totalPoints = resultMap.get(MConstants.TESTING_RESULT_POINTS);
        int mistakes = resultMap.get(MConstants.TESTING_RESULT_MISTAKES);
        int percent = resultMap.get(MConstants.TESTING_RESULT_PERCENT);
        int notChecked = resultMap.get(MConstants.TESTING_RESULT_NOT_CHECKED_COUNT);
        int questionsCount = resultMap.get(MConstants.TESTING_RESULT_QUESTIONS_COUNT);
        String spentTime = mActivity.getStopTime();

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

        startCountAnimation("%", 0, percent, percent < 30 ? 1000 : 3000); //TODO: сделать чтобы время анимации зависело от процента

        Drawable mResultIcon = getResultIcon(percent);
        mResultIcon.setColorFilter(mActivity.getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        mImageViewResultIcon.setImageDrawable(mResultIcon);


        mTextViewTime.setText(getResources().getString(R.string.spent_time).concat(" ").concat(spentTime));

        mDatabaseManager.saveStats(LessonManager.CURRENT_LESSON, totalPoints, correctQuestions, notCorrectQuestions, percent, questionsCount);

//        System.out.println("\n correct questions: " + resultMap.get(MConstants.TESTING_RESULT_CORRECT_QUESTIONS_COUNT));
//        System.out.println("not correct questions: " + resultMap.get(MConstants.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT));
//        System.out.println("points: " + resultMap.get(MConstants.TESTING_RESULT_POINTS));
//        System.out.println("mistakes: " + resultMap.get(MConstants.TESTING_RESULT_MISTAKES));
//        System.out.println("percent: " + resultMap.get(MConstants.TESTING_RESULT_PERCENT) + "%");
//        System.out.println("not checked: " + resultMap.get(MConstants.TESTING_RESULT_NOT_CHECKED_COUNT));

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
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextViewFinish.setText(getResources().getText(R.string.result_test_title));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 222;
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);

    }
}
