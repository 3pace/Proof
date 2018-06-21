package com.proof.ly.space.proof.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proof.ly.space.proof.CustomViews.MSnackbar;
import com.proof.ly.space.proof.Data.JsonQuestion;
import com.proof.ly.space.proof.Data.Question;
import com.proof.ly.space.proof.Data.SearchData;
import com.proof.ly.space.proof.Data.StatsData;
import com.proof.ly.space.proof.Helpers.Retrofit.ApiClient;
import com.proof.ly.space.proof.Helpers.Retrofit.ApiInterface;
import com.proof.ly.space.proof.Helpers.Retrofit.QuestionResponse;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.proof.ly.space.proof.Helpers.DBHelper.QUESTION_FROM;
import static com.proof.ly.space.proof.Helpers.DBHelper.QUESTION_ROW;
import static com.proof.ly.space.proof.Helpers.SettingsManager.localDbVersion;

/**
 * Created by aman on 27.03.18.
 */

public class DBManager {


    private static final String MENU_LESSON_ITEMS = "menu_items";
    private static final String FT_QUESTIONS = "questions";
    private static final String LANG_RU = "ru";
    private static final String LANG_KZ = "kz";
    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private DBSearchHelper mSearchHelper;
    private USManager mUsManager;
    private Context mContext;
    private ArrayList<SearchData> mArrayListSearchFull = new ArrayList<>();
    private ArrayList<SearchData> mArrayListSearch = new ArrayList<>();
    private TinyDB mTinyDB;
    private static final String VIEWED_QUESTION_LIST = LessonManager.CURRENT_DB;
    private static final String CYCLE_NUM = "cycle_num_";
    private float mCursorCount = 0;
    private DatabaseReference mRef;
    private static final String FT_DB_VERSION = "version";
    private static final String FT_LESSONS = "lessons";
    public static final String FT_ONLINE = "online";
    public static int version = 0;
    private SettingsManager mSettingsManager;
    private boolean mIsDatabaseNew = false;
    private MainActivity mActivity;
    private boolean mUpdateIsRunning = false;


    public DBManager(Context context) {
        mHelper = new DBHelper(context);
        mDatabase = mHelper.getReadableDatabase();
        mUsManager = new USManager(context);
        mTinyDB = new TinyDB(context);
        mRef = FirebaseDatabase.getInstance().getReference();
        checkNewDatabaseVersion();
        this.mContext = context;


    }

    public ArrayList<JsonQuestion> getQuestionFromAssetsWithCycleFromLocalDb(int count) {
        ArrayList<Integer> viewedQuestions = getViewedQuestions();
        int viewedSize = viewedQuestions.size();
        String q;
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        mDatabase = mHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        mCursorCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            whileloop:
            do {
                int id = cursor.getInt(0);
                for (int i = 0; i < viewedSize; i++) {
                    if (id == viewedQuestions.get(i)) {
                        continue whileloop;
                    }
                }
                q = cursor.getString(1).replaceAll("([0-9]+\\.)", "").trim();
                arrayList.add(new JsonQuestion(cursor.getInt(0), q));
            } while (cursor.moveToNext() && arrayList.size() < count);
        }
        cursor.close();
        return arrayList;

    }

    public ArrayList<JsonQuestion> getQuestionListFromLocalDb(int count) {
        String q;
        mDatabase = mHelper.getReadableDatabase();
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        Cursor cursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);

        int cursorCount = cursor.getCount();
        int max = cursorCount < count ? cursorCount : count;
        if (cursorCount != 0) {

            while (cursor.moveToPosition((int) (Math.random() * cursorCount))) {
                q = cursor.getString(1).replaceAll("([0-9]+\\.)", "").trim();
                arrayList.add(new JsonQuestion(cursor.getInt(0), q));
                if (arrayList.size() >= max) break;
            }
        }
        cursor.close();
        return arrayList;
    }

    private boolean checkNewDatabaseVersion() {
        mIsDatabaseNew = false;
        mRef.child(FT_DB_VERSION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                version = dataSnapshot != null ? dataSnapshot.getValue(Integer.class) : 0;
                if (localDbVersion < version) {
                    showSnackBarUpdateAvailable();
                    mIsDatabaseNew = true;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mIsDatabaseNew;
    }

    public boolean checkNewDatabaseVersionFromClick() {
        if (isNetworkAvailable(mContext)) {
            mIsDatabaseNew = false;
            mRef.child(FT_DB_VERSION).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    version = dataSnapshot != null ? dataSnapshot.getValue(Integer.class) : 0;
                    if (localDbVersion < version) {
                        showSnackBarUpdateAvailable();
                        mIsDatabaseNew = true;
                    } else {
                        showSnackBarHasLastVersion();

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return mIsDatabaseNew;
        } else {
            showSnackBarNoInternetConnection();
            return false;
        }
    }

    private void showSnackBarUpdateAvailable() {
        MSnackbar.builder()
                .setDuration(15000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.update_available))
                .setTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.teal))
                .setActionText(mContext.getResources().getString(R.string.to_update))
                .setActionTextTypeface(mActivity.getTypeface())
                .setActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addQuestionsFromFirebase();
                    }
                })
                .setActionTextColor(mContext.getResources().getColor(R.color.white))
                .info()
                .show();

    }

    private void showSnackBarHasLastVersion() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.has_last_version))
                .setTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setActionText(mContext.getResources().getString(R.string.reset_update))
                .setActionTextTypeface(mActivity.getTypeface())
                .setActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localDbVersion = 0;
                        mSettingsManager.saveDBVersion(localDbVersion);
                        mIsDatabaseNew = true;
                        addQuestionsFromFirebase();
                    }
                })
                .setActionTextColor(mContext.getResources().getColor(R.color.white))
                .info()
                .show();

    }

    private void showSnackBarUpdating() {
        MSnackbar.builder()
                .setDuration(180000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.updating))
                .setTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setActionTextTypeface(mActivity.getTypeface())
                .build()
                .show();

    }

    private void showSnackBarUpdated() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.updating_complete))
                .setTextTypeface(mActivity.getTypeface())
                .setActionTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.teal))
                .success()
                .show();

    }

    private void showSnackBarUpdatingFailed() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.error))
                .setTextTypeface(mActivity.getTypeface())
                .setActionTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent))
                .error()
                .show();

    }
    private void showSnackBarNoInternetConnection() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(mActivity)
                .setText(mContext.getResources().getString(R.string.no_internet))
                .setTextTypeface(mActivity.getTypeface())
                .setActionTextTypeface(mActivity.getTypeface())
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent))
                .error()
                .show();

    }
    public void addQuestionsFromFirebase() {
        showSnackBarUpdating();
        mRef.child(FT_LESSONS).child(mSettingsManager.langIsRussian() ? LANG_RU : LANG_KZ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mUpdateIsRunning)
                    new LoadDataFromFirebase().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
                showSnackBarUpdatingFailed();
            }
        });

    }

    public ArrayList<JsonQuestion> getQuestionFromAssets() {
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        mDatabase = getSearchHelper().getReadableDatabase();
        Cursor cursor = mDatabase.query(DBHelper.QUESTION_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new JsonQuestion(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;

    }

    public ArrayList<JsonQuestion> getQuestionFromAssets(int count) {
        String q;
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        mDatabase = getSearchHelper().getReadableDatabase();
        Cursor cursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        int cursorCount = cursor.getCount();
        int max = cursorCount < count ? cursorCount : count;
        if (cursorCount != 0) {

            while (cursor.moveToPosition((int) (Math.random() * cursorCount))) {
                q = cursor.getString(1).replaceAll("([0-9]+\\.)", "").trim();
                arrayList.add(new JsonQuestion(cursor.getInt(0), q));
                if (arrayList.size() >= max) break;
            }
        }
        cursor.close();
        return arrayList;

    }

    public ArrayList<JsonQuestion> getQuestionFromAssetsWithCycle(int count) {
        ArrayList<Integer> viewedQuestions = getViewedQuestions();
        int viewedSize = viewedQuestions.size();
        String q;
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        mDatabase = getSearchHelper().getReadableDatabase();
        Cursor cursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        mCursorCount = cursor.getCount();
        if (cursor.moveToFirst()) {
            whileloop:
            do {
                int id = cursor.getInt(0);
                for (int i = 0; i < viewedSize; i++) {
                    if (id == viewedQuestions.get(i)) {
                        continue whileloop;
                    }
                }
                q = cursor.getString(1).replaceAll("([0-9]+\\.)", "").trim();
                arrayList.add(new JsonQuestion(cursor.getInt(0), q));
            } while (cursor.moveToNext() && arrayList.size() < count);
        }
        cursor.close();
        return arrayList;

    }

    public void addQuestion(String q, int uid) {
        String child = "zfromusers";
        String id = mRef.push().getKey();
        Log.d(TAG, "addQuestion: " + child + " " + id + "\n" + q);
        mRef.child(FT_LESSONS).child(mSettingsManager.langIsRussian() ? LANG_RU : LANG_KZ).child(child).child(FT_QUESTIONS).child(id).setValue(q);
        //mDatabase.close();
    }

    public void delQuestion(int id) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        String sql = "DELETE FROM " + DBHelper.QUESTION_TABLE + " WHERE _id = " + id + ";";
        database.execSQL(sql);
        database.close();
    }

    public void close() {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        mHelper.close();
        database.close();
    }

    public void addUser(String name, String password) {
        mDatabase = mHelper.getWritableDatabase();
        String sql = "INSERT INTO " + DBHelper.USER_TABLE + " (" + DBHelper.USERNAME_ROW + ", " + DBHelper.USER_PASSWORD + ") VALUES ('" + name + "', '" + password + "')";
        mDatabase.execSQL(sql);

    }

    public boolean logIn(String username, String password) {
        mDatabase = mHelper.getWritableDatabase();
        Cursor cursor = mDatabase.query(DBHelper.USER_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String pswr = cursor.getString(2);
                if (name.equals(username) && pswr.equals(password)) {
                    mUsManager.logIn(id);
                    Log.d("TEST", true + "");
                    return true;
                }
            }
            while (cursor.moveToNext());
        cursor.close();
        return false;
    }

    public HashMap<String, String> getUserById(int userId) {
        HashMap<String, String> map = new HashMap<>();
        mDatabase = mHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(DBHelper.USER_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int id = cursor.getInt(0);
                if (id == userId) {
                    String username = cursor.getString(1);
                    String password = cursor.getString(2);
                    map.put("username", username);
                    map.put("password", password);
                    break;
                }

            }
            while (cursor.moveToNext());
        cursor.close();
        return map;


    }

    public ArrayList<String> getUserQuestions(int id) {
        ArrayList<String> arrayList = new ArrayList<>();
        mDatabase = mHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(DBHelper.QUESTION_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int from = cursor.getInt(2);
                if (from == id) {
                    try {
                        String json = cursor.getString(1);
                        JSONObject object = new JSONObject(json);
                        String question = object.getString("question");
                        arrayList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (cursor.moveToNext());
        cursor.close();
        return arrayList;
    }

    public DBSearchHelper getSearchHelper() {
        mSearchHelper = new DBSearchHelper(mContext);
        return mSearchHelper;
    }

    public ArrayList<SearchData> generateSearchListFullFromLocalDb() {
        if (LessonManager.lessonChanged) {
            mArrayListSearchFull.clear();
        }
        if (mArrayListSearchFull.size() <= 0) {
            mDatabase = mHelper.getReadableDatabase();
            Cursor mSearchCursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
            int cursorCount = mSearchCursor.getCount();
            if (mSearchCursor.moveToFirst()) {

                for (int i = 1; i < cursorCount; i++) {

                    try {
                        JSONObject object = new JSONObject(mSearchCursor.getString(1));
                        String question = object.getString("question");
                        JSONArray canswers = object.getJSONArray("correct");
                        SearchData searchData = new SearchData();
                        searchData.setQuestion(question.replaceAll("([0-9]+\\.)", "").trim());
                        searchData.setAnswer("");
                        for (int k = 0; k < canswers.length(); k++) {
                            searchData.setAnswer(searchData.getAnswer().concat("\n#").concat(canswers.getString(k).trim()));
                        }
                        mArrayListSearchFull.add(searchData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSearchCursor.moveToPosition(i);
                }
            }
            mSearchCursor.close();
        }

        LessonManager.lessonChanged = false;
        return mArrayListSearchFull;
    }

    public ArrayList<SearchData> generateSearchListFull() {
        if (LessonManager.lessonChanged) {
            mArrayListSearchFull.clear();
        }
        if (mArrayListSearchFull.size() <= 0) {
            mDatabase = mSearchHelper.getReadableDatabase();
            Cursor mSearchCursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
            int cursorCount = mSearchCursor.getCount();
            if (mSearchCursor.moveToFirst()) {

                for (int i = 1; i < cursorCount; i++) {

                    try {
                        JSONObject object = new JSONObject(mSearchCursor.getString(1));
                        String question = object.getString("question");
                        JSONArray canswers = object.getJSONArray("correct");
                        SearchData searchData = new SearchData();
                        searchData.setQuestion(question.replaceAll("([0-9]+\\.)", "").trim());
                        searchData.setAnswer("");
                        for (int k = 0; k < canswers.length(); k++) {
                            searchData.setAnswer(searchData.getAnswer().concat("\n#").concat(canswers.getString(k).trim()));
                        }
                        mArrayListSearchFull.add(searchData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSearchCursor.moveToPosition(i);
                }
            }
            mSearchCursor.close();

        }

        LessonManager.lessonChanged = false;
        return mArrayListSearchFull;
    }

    public ArrayList<SearchData> getSearchList(int size) {

        int max = (mArrayListSearchFull.size() < size) ? mArrayListSearchFull.size() : size;
        mArrayListSearch.clear();
        if (mArrayListSearchFull.size() <= 0) {
            return new ArrayList<>();
        } else {
            for (int i = 0; i < max; i++)
                mArrayListSearch.add(new SearchData(mArrayListSearchFull.get(i).getQuestion(), mArrayListSearchFull.get(i).getAnswer()));
            return mArrayListSearch;
        }
    }

    public ArrayList<SearchData> getSearchListLoad(int start, int size) {
        int max = (start + size);
        if (mArrayListSearchFull.size() <= (start + size))
            max = mArrayListSearchFull.size();
        for (int i = start; i < max; i++)
            mArrayListSearch.add(new SearchData(mArrayListSearchFull.get(i).getQuestion(), mArrayListSearchFull.get(i).getAnswer()));
        return mArrayListSearch;
    }

    public ArrayList<SearchData> getFullSearchList() {
        return mArrayListSearchFull;
    }


    public ArrayList<Integer> getViewedQuestions() {

        return mTinyDB.getListInt(LessonManager.CURRENT_DB);
    }

    public String getViewedPercent() {
        mDatabase = mHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        mCursorCount = (float) cursor.getCount();
        cursor.close();
        return String.format(Locale.US, "%.1f", (float) ((getViewedQuestions().size() * 100f) / mCursorCount));

    }

    public boolean isAllQuestionsViewed() {
        return getViewedQuestions().size() >= mCursorCount && mCursorCount != 0;
    }

    public void addViewedQuestion(int qId) {

        ArrayList<Integer> list = getViewedQuestions();
        list.add(qId);
        mTinyDB.putListInt(LessonManager.CURRENT_DB, list);


    }

    public void saveStats(String lesson, int point, int correct, int notCorrect, int percent, int questionsCount) {
        String date = String.valueOf(System.currentTimeMillis());
        mDatabase = mHelper.getWritableDatabase();
        String sqlRequest = "INSERT INTO " +
                DBHelper.STATS_TABLE +
                " (" +
                DBHelper.STATS_LESSON + ", " +
                DBHelper.STATS_POINT + ", " +
                DBHelper.STATS_CORRECT + ", " +
                DBHelper.STATS_NOT_CORRECT + ", " +
                DBHelper.STATS_PERCENT + ", " +
                DBHelper.STATS_QUESTIONS_COUNT + ", " +
                DBHelper.STATS_DATE +
                ")" +
                " VALUES (" +
                "'" + lesson + "', " +
                "'" + point + "', " +
                "'" + correct + "', " +
                "'" + notCorrect + "', " +
                "'" + percent + "', " +
                "'" + questionsCount + "', " +
                "'" + date + "')";
        System.out.println(date);
        mDatabase.execSQL(sqlRequest);
    }

    public ArrayList<StatsData> getStats() {
        ArrayList<StatsData> arrayList = new ArrayList<>();
        mDatabase = mHelper.getReadableDatabase();
//        String sqlRequest = "SELECT * FROM " + DBHelper.STATS_TABLE + " ORDER BY _id DESC LIMIT 10";
        String sqlRequest = "SELECT * FROM (SELECT * FROM " + DBHelper.STATS_TABLE + " ORDER BY _id DESC LIMIT 10) ORDER BY " + DBHelper.STATS_DATE + " DESC";
        Cursor cursor = mDatabase.rawQuery(sqlRequest, null);
        if (cursor.moveToFirst()) {
            do {
                String lesson = cursor.getString(cursor.getColumnIndex(DBHelper.STATS_LESSON));
                if (lesson.equals(LessonManager.CURRENT_LESSON)) {
                    StatsData statsData = new StatsData();
                    statsData.setPoint(cursor.getInt(cursor.getColumnIndex(DBHelper.STATS_POINT)));
                    statsData.setCorrect(cursor.getInt(cursor.getColumnIndex(DBHelper.STATS_CORRECT)));
                    statsData.setNotCorrect(cursor.getInt(cursor.getColumnIndex(DBHelper.STATS_NOT_CORRECT)));
                    statsData.setPercent(cursor.getInt(cursor.getColumnIndex(DBHelper.STATS_PERCENT)));
                    statsData.setCount(cursor.getInt(cursor.getColumnIndex(DBHelper.STATS_QUESTIONS_COUNT)));
                    statsData.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.STATS_DATE)));

                    arrayList.add(statsData);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(arrayList);
        return arrayList;
    }

    public void newCycle() {
        String cycle_path = CYCLE_NUM + LessonManager.CURRENT_DB;
        ArrayList<Integer> list = getViewedQuestions();
        list.clear();
        mTinyDB.putListInt(LessonManager.CURRENT_DB, list);
        mTinyDB.putInt(cycle_path, (mTinyDB.getInt(cycle_path) + 1));
        Log.d("test", "new cycle started #" + mTinyDB.getInt(cycle_path));
    }

    public int getCycleNum() {
        return mTinyDB.getInt(CYCLE_NUM + LessonManager.CURRENT_DB) + 1;
    }

    public void restartCycle() {
        mTinyDB.clear();
        Log.d("test", "restart cycle");
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.mSettingsManager = settingsManager;
    }

    public void setActivity(MainActivity activity) {
        this.mActivity = activity;
    }

    public void updateFromFirebase() {
        if (isNetworkAvailable(mContext)) {
            if (!mUpdateIsRunning) {
                localDbVersion = 0;
                mIsDatabaseNew = true;
                addQuestionsFromFirebase();
            }
        } else {
            showSnackBarNoInternetConnection();
        }
    }

    public void loadDataRetrofit() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ArrayList<QuestionResponse>> call = apiInterface.getAllQuestions();
        call.enqueue(new Callback<ArrayList<QuestionResponse>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ArrayList<QuestionResponse>> call, @NonNull Response<ArrayList<QuestionResponse>> response) {
                ArrayList<QuestionResponse> arrayList = response.body();
                if (arrayList == null) {
                    Log.d("retrofit", "empty");
                    return;
                }
                for (QuestionResponse questionResponse : arrayList) {
                    Log.d("retrofit", questionResponse.getQuestion());
                }



            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<QuestionResponse>> call, Throwable t) {
                Log.d("retrofit", "failure" + t.getMessage());
            }
        });
    }
    private class LoadDataFromFirebase extends AsyncTask<DataSnapshot, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mUpdateIsRunning = true;

        }

        @Override
        protected synchronized Void doInBackground(DataSnapshot... dataSnapshots) {

            if (localDbVersion < version) {

                final ContentValues contentValues = new ContentValues();
                mHelper.close();
                mContext.deleteDatabase(mContext.getDatabasePath(DBHelper.DB_NAME).getPath()); // Удаялем БД чтобы удалить старые предметы
                mDatabase = mHelper.getWritableDatabase();


                for (DataSnapshot table : dataSnapshots[0].getChildren()) {
                    if (isTableExists(table.getKey(), mDatabase)) {
                        String s = "DELETE FROM " + table.getKey();
                        mDatabase.execSQL(s);
                    } else {
                        String s = "CREATE TABLE IF NOT EXISTS " + table.getKey() + " (" +
                                "_id integer primary key, " +
                                QUESTION_ROW + " text," +
                                QUESTION_FROM + " integer);";
                        mDatabase.execSQL(s);
                        addNewLessonMenuItem(table.child("name").getValue(String.class), table.getKey());
                    }

                    for (DataSnapshot snapshot : table.getChildren()) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            if (!mDatabase.isOpen()) mDatabase = mHelper.getWritableDatabase();
                            contentValues.put(QUESTION_ROW, item.getValue(String.class));
                            contentValues.put(QUESTION_FROM, "admin");
                            mDatabase.insert(table.getKey(), null, contentValues);

                        }


                    }
                }

                Log.d(TAG, "onDataChange: SUCCESFULL");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showSnackBarUpdated();
            mSettingsManager.saveDBVersion(version);
            restartCycle();
            mActivity.startAsyncLoad();
            mActivity.updateAdapter();
            mUpdateIsRunning = false;


        }
    }

    public boolean isTableExists(String tableName, SQLiteDatabase mDatabase) {


        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private void addNewLessonMenuItem(String lessonName, String lessonDb) {
        String sql = "INSERT INTO " + DBHelper.MENU_LESSONS + " (" + DBHelper.MENU_LESSON_NAME + ", " + DBHelper.MENU_LESSON_DB + ") VALUES ('" + lessonName + "','" + lessonDb + "')";
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.execSQL(sql);

    }

    public DatabaseReference getmRef() {
        return mRef;
    }

    public DBHelper getHelper() {
        return mHelper;
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
