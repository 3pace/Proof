package com.proof.ly.space.proof;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.proof.ly.space.proof.CustomViews.MBottomSheet;
import com.proof.ly.space.proof.Fragments.ResultFragment;
import com.proof.ly.space.proof.Fragments.windows.MAuthFragment;
import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Fragments.windows.MUserFragment;
import com.proof.ly.space.proof.Fragments.windows.TMenuFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Helpers.TinyDB;
import com.proof.ly.space.proof.Interfaces.ActivityInterface;

import java.util.Timer;
import java.util.TimerTask;

import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.colored;
import static com.proof.ly.space.proof.Helpers.SettingsManager.currentLanguage;
import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.localDbVersion;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityInterface {

    private Typeface mTypeface;
    private QManager mQuestionManager;
    private DBManager mDBManager;
    private LessonManager mLessonManager;
    private SettingsManager mSettingsManager;
    private TinyDB tinyDB;
    private TypedValue mTypedValue;
    public static final String MENU_FRAGMENT_TAG = "Axiom";
    private FirebaseAuth mAuth;
    private Timer mTimer;
    private int mTimerSeconds = 0;
    private String mTimerText = "";
    private int mMinutes = 0;
    private int mSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSettingsManager().getNightmodeState())
            setTheme(R.style.darktheme);
        else setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        initViews();
        initObjects();
        initSetters();
        initOnClick();
        initTypeface();

        initFragment(new TMenuFragment());
        addBackstackChangeListener();


    }

    private void initBottomSheet() {
        MBottomSheet bottomSheet = new MBottomSheet(MainActivity.this);
        bottomSheet.show();
    }

    public void startTimer() {
        stopTimer();
        resetTimer();
        mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimerSeconds++;
                mMinutes = mTimerSeconds / 60;
                mSeconds = mTimerSeconds - (mMinutes * 60);

                Log.d("timer", mMinutes + ":" + mSeconds);
            }
        }, 1000, 1000);

    }


    public void stopTimer() {

        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        Log.d("timer", "stop");
    }

    public String getStopTime() {
        String min = String.valueOf(mMinutes);
        String sec = mSeconds < 10 ? "0" + String.valueOf(mSeconds) : String.valueOf(mSeconds);
        return min + ":" + sec;
    }

    public void resetTimer() {
        mTimerSeconds = 0;
        mMinutes = 0;
        mSeconds = 0;
    }

    private void initFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.add(R.id.container, fragment, MENU_FRAGMENT_TAG);
        fragmentTransaction.commit();


    }

    public void replaceFragment(Fragment fragment) {

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.container, fragment, fragment.getTag());
        ft.addToBackStack(null);
        ft.commit();

    }

    public void replaceFragment(Fragment fragment, String name) {

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.container, fragment, name);
        ft.addToBackStack(name);
        ft.commit();

    }

    public void replaceFragmentWithoutBackstack(Fragment fragment, String name) {
        getSupportFragmentManager().popBackStack();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.container, fragment, name);
        ft.commit();


    }

    public void toolbarPath(String path, int end) {


        Spannable textPath = new SpannableString(path);
        textPath.setSpan(new ForegroundColorSpan(getDisabledColor()), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


    }


    public void addBackstackChangeListener() {

        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        fm.addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                StringBuilder path = new StringBuilder();
                int end = 0;
                for (int i = 0; i < fm.getFragments().size(); i++) {
                    end = fm.getFragments().get(i).getTag().length();
                    path.append("/").append(fm.getFragments().get(i).getTag());
                }
                toolbarPath(path.toString(), path.length() - end);


            }
        });
        toolbarPath("/" + MENU_FRAGMENT_TAG, 1);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            Fragment f = fragmentManager.findFragmentByTag(getResources().getString(R.string.tag_result));
            if (f != null)
                if (f instanceof ResultFragment) {
                    fragmentManager.popBackStack();
                }
        }
        super.onBackPressed();
    }

    @Override
    public void initViews() {



    }

    @Override
    public void initTypeface() {
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/robotom.ttf");

    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {
        mDBManager = new DBManager(getApplicationContext());
        mQuestionManager = new QManager(mDBManager, getApplicationContext());
        mLessonManager = new LessonManager(mDBManager);
        mTypedValue = new TypedValue();
        mAuth = FirebaseAuth.getInstance();
        startAsyncLoad();


    }

    @Override
    public void initSetters() {
        mDBManager.setSettingsManager(getSettingsManager());
        mDBManager.setActivity(this);

    }

    @Override
    public void onClick(View v) {

    }

    public void toast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (menu.getItem(0).getIcon() != null)
            if (mAuth.getCurrentUser() != null)
                menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            else
                menu.getItem(0).getIcon().setColorFilter(getClickedColor(), PorterDuff.Mode.SRC_ATOP);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:

                if (mAuth.getCurrentUser() != null) {
                    replaceFragment(new MUserFragment(), getResources().getString(R.string.tag_user));
                } else {
                    replaceFragment(new MAuthFragment(), getResources().getString(R.string.tag_auth));
                }
                break;
        }
        return false;

    }


    public Typeface getTypeface() {
        return mTypeface;
    }

    public QManager getQuestionManager() {
        return mQuestionManager;
    }

    public DBManager getDatabaseManager() {
        if (mDBManager != null)
            return mDBManager;
        else return mDBManager = new DBManager(getApplicationContext());
    }

    public LessonManager getLessonManager() {
        return mLessonManager;
    }

    public SettingsManager getSettingsManager() {
        if (mSettingsManager != null) return mSettingsManager;
        else return mSettingsManager = new SettingsManager(getApplicationContext());
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public TinyDB getTinyDB() {
        if (tinyDB != null) {
            return tinyDB;
        } else {
            return tinyDB = new TinyDB(getApplicationContext());
        }
    }

    public void updateAdapter() {
        mLessonManager.update(getDatabaseManager());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MENU_FRAGMENT_TAG);
        if (fragment != null && fragment instanceof TMenuFragment) {
            TMenuFragment f = (TMenuFragment) fragment;
            f.updateAdapter();
        }

    }


    public class GenerateQuestions extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TMenuFragment.isLoading = true;
            QManager.isGenerate = false;
            getSettingsManager();
            colored = mSettingsManager.getColoredState();
            cycleMode = mSettingsManager.getCycleModeState();
            autoflip = mSettingsManager.getAutoflipState();
            quesCount = mSettingsManager.getQuesCount();
            localDbVersion = mSettingsManager.getDBVersion();
            currentLanguage = mSettingsManager.langIsRussian();


        }

        @Override
        protected synchronized Void doInBackground(Void... voids) {
            MTestingFragment.COUNT = quesCount ? MTestingFragment.BIGY : MTestingFragment.MINI;
            mQuestionManager.generateQFromDB(MTestingFragment.COUNT);
            mQuestionManager.generateQuestionsList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LessonManager.isLessonChangedForTesting = false;
            TMenuFragment.isLoading = false;


        }
    }

    public void startAsyncLoad() {
        if (!TMenuFragment.isLoading)
            new GenerateQuestions().execute();
    }

    public int getDisabledColor() {

        return getTheme().resolveAttribute(R.attr.disabled_answer, mTypedValue, true) ?
                mTypedValue.data : getResources().getColor(R.color.grey);
    }

    public int getClickedColor() {
        return getTheme().resolveAttribute(R.attr.clicked_answer, mTypedValue, true) ?
                mTypedValue.data : getResources().getColor(R.color.grey);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth != null)
            if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getDisplayName() != null)
                getDatabaseManager().getmRef().child(DBManager.FT_ONLINE).child(mAuth.getCurrentUser().getDisplayName()).setValue("yes");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth != null)
            if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getDisplayName() != null)
                getDatabaseManager().getmRef().child(DBManager.FT_ONLINE).child(mAuth.getCurrentUser().getDisplayName()).removeValue();
    }


}
