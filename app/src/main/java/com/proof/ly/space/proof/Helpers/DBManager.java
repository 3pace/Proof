package com.proof.ly.space.proof.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proof.ly.space.proof.CustomViews.MSnackbar;
import com.proof.ly.space.proof.Data.JsonQuestion;
import com.proof.ly.space.proof.Data.SearchData;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


import static android.content.ContentValues.TAG;
import static com.proof.ly.space.proof.Helpers.DBHelper.QUESTION_FROM;
import static com.proof.ly.space.proof.Helpers.DBHelper.QUESTION_ROW;
import static com.proof.ly.space.proof.Helpers.SettingsManager.mLocalDBVersion;

/**
 * Created by aman on 27.03.18.
 */

public class DBManager {


    private static final String MENU_LESSON_ITEMS = "menu_items";
    private static final String FT_QUESTIONS = "questions";
    private DBHelper helper;
    private SQLiteDatabase database;
    private DBSearchHelper dbSearchHelper;
    private USManager usManager;
    private Context context;
    private ArrayList<SearchData> mfullSearchList = new ArrayList<>();
    private ArrayList<SearchData> mSearchList = new ArrayList<>();
    private TinyDB tinyDB;
    private static final String VIEWED_QUESTION_LIST = LessonManager.CURRENT_DB;
    private static final String CYCLE_NUM = "cycle_num_";
    private float cursorCount = 0;
    private DatabaseReference mRef;
    public static final String FT_DB_VERSION = "version";
    public static final String FT_LESSONS = "lessons";
    public static final String FT_ONLINE = "online";
    public static int version = 0;
    private SettingsManager settingsManager;
    private boolean databaseIsNew = false;
    private MainActivity activity;


    public DBManager(Context context) {
        helper = new DBHelper(context);
        database = helper.getReadableDatabase();
        usManager = new USManager(context);
        tinyDB = new TinyDB(context);
        mRef = FirebaseDatabase.getInstance().getReference();
        checkNewDatabaseVersion();
        this.context = context;


    }

    public ArrayList<JsonQuestion> getQuestionFromAssetsWithCycleFromLocalDb(int count) {
        ArrayList<Integer> viewedQuestions = getViewedQuestions();
        int viewedSize = viewedQuestions.size();
        String q;
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        cursorCount = cursor.getCount();
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
        database = helper.getReadableDatabase();
        ArrayList<JsonQuestion> arrayList = new ArrayList<>();
        Cursor cursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);

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

    boolean checkNewDatabaseVersion() {
        databaseIsNew = false;
        mRef.child(FT_DB_VERSION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                version = dataSnapshot != null ? dataSnapshot.getValue(Integer.class) : 0;
                if (mLocalDBVersion < version) {
                    showSnackBarUpdateAvailable();
                    databaseIsNew = true;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return databaseIsNew;
    }
    public boolean checkNewDatabaseVersionFromClick() {
        databaseIsNew = false;
        mRef.child(FT_DB_VERSION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                version = dataSnapshot != null ? dataSnapshot.getValue(Integer.class) : 0;
                if (mLocalDBVersion < version) {
                    showSnackBarUpdateAvailable();
                    databaseIsNew = true;
                } else {
                    showSnackBarHasLastVersion();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return databaseIsNew;
    }

    private void showSnackBarUpdateAvailable() {
        MSnackbar.builder()
                .setDuration(15000)
                .setActivity(activity)
                .setText(context.getResources().getString(R.string.update_available))
                .setTextTypeface(activity.getTypeface())
                .setBackgroundColor(context.getResources().getColor(R.color.teal))
                .setActionText(context.getResources().getString(R.string.to_update))
                .setActionTextTypeface(activity.getTypeface())
                .setActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addQuestionsFromFirebase();
                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.white))
                .info()
                .show();

    }
    private void showSnackBarHasLastVersion() {
        MSnackbar.builder()
                .setDuration(15000)
                .setActivity(activity)
                .setText(context.getResources().getString(R.string.has_last_version))
                .setTextTypeface(activity.getTypeface())
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setActionText(context.getResources().getString(R.string.reset_update))
                .setActionTextTypeface(activity.getTypeface())
                .setActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLocalDBVersion = 0;
                        settingsManager.saveDBVersion(mLocalDBVersion);
                        databaseIsNew = true;
                        addQuestionsFromFirebase();
                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.white))
                .info()
                .show();

    }

    private void showSnackBarUpdating() {
        MSnackbar.builder()
                .setDuration(180000)
                .setActivity(activity)
                .setText(context.getResources().getString(R.string.updating))
                .setTextTypeface(activity.getTypeface())
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setActionTextTypeface(activity.getTypeface())
                .build()
                .show();

    }

    private void showSnackBarUpdated() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(activity)
                .setText(context.getResources().getString(R.string.updating_complete))
                .setTextTypeface(activity.getTypeface())
                .setActionTextTypeface(activity.getTypeface())
                .setBackgroundColor(context.getResources().getColor(R.color.teal))
                .success()
                .show();

    }

    private void showSnackBarUpdatingFailed() {
        MSnackbar.builder()
                .setDuration(3000)
                .setActivity(activity)
                .setText(context.getResources().getString(R.string.error))
                .setTextTypeface(activity.getTypeface())
                .setActionTextTypeface(activity.getTypeface())
                .setBackgroundColor(context.getResources().getColor(R.color.colorAccent))
                .error()
                .show();

    }

    public void addQuestionsFromFirebase() {
        showSnackBarUpdating();
        mRef.child(FT_LESSONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
        database = getSearchHelper().getReadableDatabase();
        Cursor cursor = database.query(DBHelper.QUESTION_TABLE, null, null, null, null, null, null);
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
        database = getSearchHelper().getReadableDatabase();
        Cursor cursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
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
        database = getSearchHelper().getReadableDatabase();
        Cursor cursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        cursorCount = cursor.getCount();
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
        mRef.child(FT_LESSONS).child(child).child(FT_QUESTIONS).child(id).setValue(q);
        //database.close();
    }

    public void delQuestion(int id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String sql = "DELETE FROM " + DBHelper.QUESTION_TABLE + " WHERE _id = " + id + ";";
        database.execSQL(sql);
        database.close();
    }

    public void close() {
        SQLiteDatabase database = helper.getWritableDatabase();
        helper.close();
        database.close();
    }

    public void addUser(String name, String password) {
        database = helper.getWritableDatabase();
        String sql = "INSERT INTO " + DBHelper.USER_TABLE + " (" + DBHelper.USERNAME_ROW + ", " + DBHelper.USER_PASSWORD + ") VALUES ('" + name + "', '" + password + "')";
        database.execSQL(sql);

    }

    public boolean logIn(String username, String password) {
        database = helper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.USER_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String pswr = cursor.getString(2);
                if (name.equals(username) && pswr.equals(password)) {
                    usManager.logIn(id);
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
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.USER_TABLE, null, null, null, null, null, null);
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
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.QUESTION_TABLE, null, null, null, null, null, null);
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
        dbSearchHelper = new DBSearchHelper(context);
        return dbSearchHelper;
    }

    public ArrayList<SearchData> generateSearchListFullFromLocalDb() {
        if (LessonManager.lessonChanged) {
            mfullSearchList.clear();
        }
        if (mfullSearchList.size() <= 0) {
            database = helper.getReadableDatabase();
            Cursor mSearchCursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
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
                        mfullSearchList.add(searchData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSearchCursor.moveToPosition(i);
                }
            }
            mSearchCursor.close();
        }

        LessonManager.lessonChanged = false;
        return mfullSearchList;
    }

    public ArrayList<SearchData> generateSearchListFull() {
        if (LessonManager.lessonChanged) {
            mfullSearchList.clear();
        }
        if (mfullSearchList.size() <= 0) {
            database = dbSearchHelper.getReadableDatabase();
            Cursor mSearchCursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
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
                        mfullSearchList.add(searchData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSearchCursor.moveToPosition(i);
                }
            }
            mSearchCursor.close();

        }

        LessonManager.lessonChanged = false;
        return mfullSearchList;
    }

    public ArrayList<SearchData> getSearchList(int size) {

        int max = (mfullSearchList.size() < size) ? mfullSearchList.size() : size;
        mSearchList.clear();
        if (mfullSearchList.size() <= 0) {
            return new ArrayList<>();
        } else {
            for (int i = 0; i < max; i++)
                mSearchList.add(new SearchData(mfullSearchList.get(i).getQuestion(), mfullSearchList.get(i).getAnswer()));
            return mSearchList;
        }
    }

    public ArrayList<SearchData> getSearchListLoad(int start, int size) {
        int max = (start + size);
        if (mfullSearchList.size() <= (start + size))
            max = mfullSearchList.size();
        for (int i = start; i < max; i++)
            mSearchList.add(new SearchData(mfullSearchList.get(i).getQuestion(), mfullSearchList.get(i).getAnswer()));
        return mSearchList;
    }

    public ArrayList<SearchData> getFullSearchList() {
        return mfullSearchList;
    }


    public ArrayList<Integer> getViewedQuestions() {

        return tinyDB.getListInt(LessonManager.CURRENT_DB);
    }

    public String getViewedPercent() {
        database = helper.getReadableDatabase();
        Cursor cursor = database.query(LessonManager.CURRENT_DB, null, null, null, null, null, null);
        cursorCount = (float) cursor.getCount();
        cursor.close();
        return String.format(Locale.US, "%.1f", (float) ((getViewedQuestions().size() * 100f) / cursorCount));

    }

    public boolean isAllQuestionsViewed() {
        return getViewedQuestions().size() >= cursorCount && cursorCount != 0;
    }

    public void addViewedQuestion(int qId) {

        ArrayList<Integer> list = getViewedQuestions();
        list.add(qId);
        tinyDB.putListInt(LessonManager.CURRENT_DB, list);


    }

    public void newCycle() {
        String cycle_path = CYCLE_NUM + LessonManager.CURRENT_DB;
        ArrayList<Integer> list = getViewedQuestions();
        list.clear();
        tinyDB.putListInt(LessonManager.CURRENT_DB, list);
        tinyDB.putInt(cycle_path, (tinyDB.getInt(cycle_path) + 1));
        Log.d("test", "new cycle started #" + tinyDB.getInt(cycle_path));
    }

    public int getCycleNum() {
        return tinyDB.getInt(CYCLE_NUM + LessonManager.CURRENT_DB) + 1;
    }

    public void restartCycle() {
        tinyDB.clear();
        Log.d("test", "restart cycle");
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    private class LoadDataFromFirebase extends AsyncTask<DataSnapshot, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(DataSnapshot... dataSnapshots) {

            if (mLocalDBVersion < version) {

                final ContentValues contentValues = new ContentValues();
                database = helper.getWritableDatabase();
                Log.d("ASD", database.getPath());

                for (DataSnapshot table : dataSnapshots[0].getChildren()) {

                    if (isTableExists(table.getKey(), database)) {
                        Log.d(TAG, "onDataChange: " + table.getKey() + " IS EXIST");
                        String s = "DELETE FROM " + table.getKey();
                        database.execSQL(s);
                    } else {
                        Log.d(TAG, "onDataChange: " + table.getKey() + " IS NOT EXIST");
                        String s = "CREATE TABLE IF NOT EXISTS " + table.getKey() + " (" +
                                "_id integer primary key, " +
                                QUESTION_ROW + " text," +
                                QUESTION_FROM + " integer);";
                        database.execSQL(s);
                        //addNewLessonMenuItem(table.getKey(), table.child());

                        Log.d(TAG, "onDataChange: TABLE " + table.getKey() + " CREATED");
                        addNewLessonMenuItem(table.child("name").getValue(String.class), table.getKey());
                    }
                    Log.d(TAG, "onDataChange: " + table.child("name").getValue());
                    for (DataSnapshot snapshot : table.getChildren()) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            if (!database.isOpen()) database = helper.getWritableDatabase();
                            contentValues.put(QUESTION_ROW, item.getValue(String.class));
                            contentValues.put(QUESTION_FROM, "admin");
                            database.insert(table.getKey(), null, contentValues);
                            Log.d(TAG, "onDataChange: " + item.getValue());
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
            settingsManager.saveDBVersion(version);
            restartCycle();
            activity.startAsyncLoad();
            activity.updateAdapter();


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
        database = helper.getWritableDatabase();
        database.execSQL(sql);

    }

    public DatabaseReference getmRef() {
        return mRef;
    }

    public DBHelper getHelper() {
        return helper;
    }
}
