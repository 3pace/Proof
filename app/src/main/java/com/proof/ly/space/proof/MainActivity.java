package com.proof.ly.space.proof;



import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.proof.ly.space.proof.Fragments.windows.MAuthFragment;
import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Fragments.windows.TMenuFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Helpers.TinyDB;
import com.proof.ly.space.proof.Helpers.USManager;
import com.proof.ly.space.proof.Interfaces.ActivityInterface;

import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.colored;
import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.db_version;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityInterface {

    private Toolbar tbar;
    private TextView txt_tbar;
    private Typeface tf;
    private QManager qManager;
    private DBManager dbManager;
    private LessonManager lessonManager;
    private SettingsManager settingsManager;
    private TinyDB tinyDB;
    private TypedValue typedValue;
    private final String MENU_FRAGMENT_TAG = "menufrag";

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
        addMenuItems();
        initTypeface();
        initFragment(new TMenuFragment());

    }

    private void initFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in,R.animator.none,R.animator.fade_in,R.animator.fade_out);
        fragmentTransaction.add(R.id.container,fragment,MENU_FRAGMENT_TAG);
        fragmentTransaction.commit();


    }
    public void replaceFragment(Fragment fragment){

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
            ft.add(R.id.container, fragment, fragment.getTag());
            ft.addToBackStack(null);
            ft.commit();

    }
    public void replaceFragment(Fragment fragment,String name){

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.container, fragment, fragment.getTag());
        ft.addToBackStack(name);
        ft.commit();

    }
    public void replaceFragmentWithPop(Fragment fragment,String name){
        getSupportFragmentManager().popBackStack();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.none, R.animator.fade_in, R.animator.fade_out);
        ft.add(R.id.container, fragment, fragment.getTag());
        ft.addToBackStack(name);
        ft.commit();


    }


    public void addMenuItems() {

    }

    @Override
    public void initViews() {
        tbar = findViewById(R.id.tbar);
        txt_tbar = tbar.findViewById(R.id.txt_tbar);




    }

    @Override
    public void initTypeface() {
        tf = Typeface.createFromAsset(getAssets(), "fonts/ubuntum.ttf");
        txt_tbar.setTypeface(tf);

    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {
       dbManager = new DBManager(getApplicationContext());
       qManager = new QManager(dbManager,getApplicationContext());
       lessonManager = new LessonManager(dbManager);
       typedValue = new TypedValue();
       startAsyncLoad();



    }

    @Override
    public void initSetters() {
        setSupportActionBar(tbar);
        dbManager.setSettingsManager(getSettingsManager());
        dbManager.setActivity(this);

    }

    @Override
    public void onClick(View v) {

    }

    public void toast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

            if (menu.getItem(0).getIcon() != null)
                if (USManager.hasLogIn(getApplicationContext()))
                    menu.getItem(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                else menu.getItem(0).getIcon().setColorFilter(getClickedColor(), PorterDuff.Mode.SRC_ATOP);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.user:
/*                if (USManager.hasLogIn())
                    Log.d("test", "onOptionsItemSelected: " + "is logged");
                else
                    //startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    replaceFragment(new MAuthFragment());*/
                break;
        }
        return false;

    }


    public Typeface getTypeface() {
        return tf;
    }

    public QManager getqManager() {
        return qManager;
    }

    public DBManager getDbManager() {
        if (dbManager != null)
            return dbManager;
        else return dbManager = new DBManager(getApplicationContext());
    }

    public LessonManager getLessonManager() {
        return lessonManager;
    }
    public SettingsManager getSettingsManager(){
        if (settingsManager != null) return settingsManager;
        else return settingsManager = new SettingsManager(getApplicationContext());
    }

    public TinyDB getTinyDB() {
        if (tinyDB != null) {
            Log.d("test", "getTinyDB: ");
            return tinyDB;
        }
        else{
            Log.d("test", "getTinyDB: new ");
            return tinyDB = new TinyDB(getApplicationContext());
        }
    }

    public void updateAdapter() {
        lessonManager.update(getDbManager());
        Fragment fragment =  getSupportFragmentManager().findFragmentByTag(MENU_FRAGMENT_TAG);
        if (fragment != null && fragment instanceof TMenuFragment) {
            TMenuFragment f = (TMenuFragment) fragment;
            f.updateAdapter();
        }

    }


    public class generateQuestions extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TMenuFragment.isLoading = true;
            QManager.isGenerate = false;
            getSettingsManager();
            colored = settingsManager.getColoredState();
            cycleMode = settingsManager.getCycleModeState();
            autoflip = settingsManager.getAutoflipState();
            quesCount = settingsManager.getQuesCount();
            db_version = settingsManager.getDBVersion();
            Log.d("SDASD", "onPreExecute: STARTED");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            MTestingFragment.COUNT = quesCount ? MTestingFragment.BIGI : MTestingFragment.MINI;
            qManager.generateQFromDB(MTestingFragment.COUNT);
            qManager.generateQuestionsList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LessonManager.isLessonChangedForTesting = false;
            TMenuFragment.isLoading = false;
            Log.d("test","FIRST LOAD QUESTION SUCCCES");

        }
    }

    public void startAsyncLoad(){
        if (!TMenuFragment.isLoading)
            new generateQuestions().execute();
    }
    public int getDisabledColor(){

        return getTheme().resolveAttribute(R.attr.disabled_answer, typedValue, true) ?
                typedValue.data : getResources().getColor(R.color.grey);
    }
    public int getClickedColor(){
        return getTheme().resolveAttribute(R.attr.clicked_answer, typedValue, true) ?
                typedValue.data : getResources().getColor(R.color.grey);
    }
}
