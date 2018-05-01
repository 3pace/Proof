package com.proof.ly.space.proof.Fragments.windows;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.proof.ly.space.proof.Adapters.ViewPagerAdapter;
import com.proof.ly.space.proof.CustomViews.MViewPager;
import com.proof.ly.space.proof.Data.JsonQuestion;
import com.proof.ly.space.proof.Data.Question;
import com.proof.ly.space.proof.Fragments.TestingFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Helpers.TinyDB;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import static android.content.ContentValues.TAG;
import static com.proof.ly.space.proof.Helpers.SettingsManager.colored;
import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;

import java.util.ArrayList;


/**
 * Created by aman on 4/14/18.
 */

public class MTestingFragment extends Fragment implements FragmentInterface{

    private static MViewPager vpager;
    private ViewPagerAdapter vpadapter;
    private ArrayList<Question> questionsList = new ArrayList<>();
    private ArrayList<JsonQuestion> questions = new ArrayList<>();
    public static final int MINI = 20, BIGI = 40;
    public static int COUNT = BIGI;
    private static Handler handler;
    private Menu menu;
    private SettingsManager settingsManager;
    private QManager qManager;
    private TinyDB tinyDB;
    public static boolean loading = false;



    public MTestingFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = ((MainActivity)getActivity()).getSettingsManager();
        colored = settingsManager.getColoredState();
        cycleMode = settingsManager.getCycleModeState();
        autoflip = settingsManager.getAutoflipState();
        quesCount = settingsManager.getQuesCount();
        tinyDB = ((MainActivity)getActivity()).getTinyDB();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.testing_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        new loading().execute();

    }

    @Override
    public void initViews(View itemView) {
        vpager = itemView.findViewById(R.id.vpager);
    }

    @Override
    public void initTypeface() {


    }

    @Override
    public void initOnClick() {
        vpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 1)
                    vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.right);
                else if (position == questions.size())
                    vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.left);
                else if (position == questions.size() + 1) {
                    vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.none);
                    for (int i=0;i<menu.size();i++){
                        menu.getItem(i).setVisible(false);
                    }
                }
                else vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.all);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initObjects() {

        vpadapter = new ViewPagerAdapter(getChildFragmentManager(), questions.size());
        handler = new Handler();
        qManager = ((MainActivity)getActivity()).getqManager();


    }

    @Override
    public void initSetters() {
        vpager.setAdapter(vpadapter);
        vpager.setOffscreenPageLimit(1);
        vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.none);


    }



    private class loading extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //initViews();
            initTypeface();
            initObjects();
            initSetters();
            loading = true;
            Log.d(TAG, "onPreExecute: " + vpadapter.getCount());



        }

        @Override
        protected Void doInBackground(Void... voids) {


            COUNT = quesCount ? BIGI : MINI;
            questions = qManager.generateQFromDB(COUNT);
            questionsList = qManager.generateQuestionsList(); //question.size = count

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading = false;
            vpadapter.setCount(questionsList.size());
            initOnClick();
            Log.d(TAG, "onPreExecute: " + vpadapter.getCount());
            Log.d("MTestingFragment","size: "+questionsList.size());
            QManager.isGenerate = true;


        }
    }
    public static void nextPage(final int size) {
        if (SettingsManager.autoflip) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (vpager.getCurrentItem() != size)
                        vpager.setCurrentItem(vpager.getCurrentItem() + 1);
                }
            }, 320);
        }

    }
    public static void restartTesting(){
        vpager.setCurrentItem(1);
    }
    public static void startTesting() {
        vpager.setCurrentItem(1);
    }
    public static void finishTesting(final int size) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vpager.setCurrentItem(size + 1);
            }
        }, 320);

    }
    public void stopTesting() {
        for (int i = 0; i < questionsList.size(); i++) {
            if (!questionsList.get(i).isChecked()) {
                for (int j = 0; j < questionsList.get(i).getAnswers().size(); j++) {
                    if (questionsList.get(i).getAnswers().get(j).isCorrect()) {
                        questionsList.get(i).getAnswers().get(j).setChecked(1);
                        questionsList.get(i).getAnswers().get(j).setCorrectChecked(true);
                    }

                }
                for (int j = 0; j < questionsList.get(i).getAnswers().size(); j++) {
                    if (questionsList.get(i).getAnswers().get(j).getChecked() != 1) {
                        questionsList.get(i).getAnswers().get(j).setChecked(2);
                        questionsList.get(i).getAnswers().get(j).setEnabled(false);
                    }
                }

            }
        }
        finishTesting(questions.size());

    }

    public void all() {


        String s = "android:switcher:"; //2131230929 = R.id.vpager


        for (int i = 0; i <= questionsList.size(); i++) {
            Fragment fragment = getChildFragmentManager().findFragmentByTag(s +R.id.vpager+":"+ i);
            if (fragment != null && fragment instanceof TestingFragment) {
                ((TestingFragment) fragment).getAdapter().notifyDataSetChanged();
            }
        }
        vpadapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_testing, menu);

        if (menu.getItem(0).getIcon() != null)
            if (colored)
                menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            else menu.getItem(0).getIcon().setColorFilter(((MainActivity)getActivity()).getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.colored:

                colored = !colored;
                if (colored)
                    item.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                else item.getIcon().setColorFilter(((MainActivity)getActivity()).getClickedColor(), PorterDuff.Mode.SRC_ATOP);
                settingsManager.saveColoredState(colored);
                all();
                break;
            case R.id.stop:
                stopTesting();
                break;
            case R.id.settings:
                ((MainActivity)getActivity()).replaceFragment(new MSettingsFragment());
                break;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    public TinyDB getTinyDB() {

        return tinyDB;
    }
}
