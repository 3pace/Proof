package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.util.Log;

import com.proof.ly.space.proof.Data.Answers;
import com.proof.ly.space.proof.Data.JsonQuestion;
import com.proof.ly.space.proof.Data.NewAnswers;
import com.proof.ly.space.proof.Data.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by aman on 4/14/18.
 */

public class QManager {
    private ArrayList<Question> questionsList = new ArrayList<>();
    private ArrayList<JsonQuestion> questions = new ArrayList<>();
    private DBManager dbManager;
    private JSONObject object;
    public static boolean isGenerate = false;

    public QManager(DBManager dbManager, Context c) {
        this.dbManager = dbManager;
    }

    public ArrayList<Question> generateQuestionsList() {

        if (isGenerate) questionsList.clear();
        if (LessonManager.isLessonChangedForTesting) questionsList.clear();
        if (questionsList.size() <= 0) {
            Log.d(TAG, "generateQuestionsList: firstgenrated success");

            for (JsonQuestion jsonQuestion : questions) {
                try {
                    JSONObject object = new JSONObject(jsonQuestion.getJsonQuestion());
                    int qId = jsonQuestion.getId();
                    String question = object.getString("question");
                    JSONArray answers = object.getJSONArray("answers");
                    JSONArray canswers = object.getJSONArray("correct");
                    ArrayList<Answers> a = new ArrayList<>();
                    boolean isC = false;
                    int cAnswers = 0;//колчиество правильных вариантов
                    for (int j = 0; j < answers.length(); j++) {
                        for (int c = 0; c < canswers.length(); c++) {
                            if (answers.getString(j).equals(canswers.getString(c))) {
                                isC = true;
                                cAnswers++;//можно было просто взять canswer.size() но это не работает если будут одинаковые варианты
                                break;
                            } else {
                                isC = false;
                            }
                        }
                        a.add(new Answers(answers.getString(j), isC));
                    }
                    Collections.shuffle(a);
                    questionsList.add(new Question(qId, question, a, cAnswers));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return questionsList;
    }

    public ArrayList<Question> getQ() {
        return questionsList;
    }

    public ArrayList<JsonQuestion> generateQFromDB() {
        questions.clear();
        questions.addAll(dbManager.getQuestionFromAssets());
        return questions;
    }

    public ArrayList<JsonQuestion> generateQFromDB(int count) {
        //questions.clear();
/*
        проверяем если уже прошла генерация при запуске приложения,
        то каждый раз при старте тестирование, очищаем список и генерируем новые вопросы,
        а если нет, то берем сгенерированный список из запуска приложения */
        if (isGenerate) questions.clear();
        if (LessonManager.isLessonChangedForTesting) questions.clear();

        if (questions.size() <= 0) {
            Log.d(TAG, "generateQFromDB: GENERATE");
            if (SettingsManager.cycleMode)
                questions.addAll(dbManager.getQuestionFromAssetsWithCycleFromLocalDb(count));
            else
                questions.addAll(dbManager.getQuestionListFromLocalDb(count));
        }
        return questions;
    }

    public int getResult() {
        boolean acount;
        int points, ccount;
        int total = 0;
        int result = 0;

        for (Question question : questionsList) {
            acount = question.getAnswers().size() > 5;//если больше 5, то значит 1 балл дает 2 балла
            points = question.getcAnswers();
            ccount = question.getcCount();


            switch (ccount) {
                case 3:
                    if (points == 3) total = 2;
                    else if (points == 2) total = 1;
                    else if (points == 1) total = 0;
                    else if (points == 0) total = 0;

                    break;
                case 2:
                    if (points == 2) total = 2;
                    else if (points == 1) total = 1;
                    else if (points == 0) total = 0;
                    break;
                case 1:
                    if (acount) {
                        if (points == 1) total = 2;
                        else if (points == 0) total = 0;
                    } else {
                        if (points == 1) total = 1;
                        else if (points == 0) total = 0;
                    }
                    break;
                default:
                    if (points >= 3) total = 3;
                    else if (points == 2) total = 1;
                    else if (points == 1) total = 0;
                    else if (points == 0) total = 0;
                    break;
            }

            if (total != 0) {
                question.setCorrectChecked(true);//если кол-во очков больше, чем 0, то считается что ответили правильно
            }
            result += total;
        }
        return result;
    }

    public HashMap<String, Integer> getNotClickedAnswers() {
        HashMap<String, Integer> map = new HashMap<>();
        int count = 0, notcorrect = 0;
        if (questionsList != null)
            for (Question question : questionsList) {
                if (!question.isChecked()) {
                    count++;
                } else {
                    notcorrect += question.getNotCorrect();
                }
            }
        map.put("notclicked", count);
        map.put("notcorrect", notcorrect);
        return map;
    }

    public void stopTesting() {


        for (Question question : questionsList) {
            if (!question.isChecked()) {

                for (Answers answer : question.getAnswers()) {
                    if (answer.isCorrect()) {
                        answer.setChecked(1);
                        answer.setCorrectChecked(true);
                        answer.setEnabled(false);
                    }
                    if (answer.getChecked() != 1) {
                        answer.setChecked(2);
                        answer.setEnabled(false);
                    }

                }


            }
        }


    }

    public void initJson() {
        object = new JSONObject();

    }

    public void createJsonQuestion(String question) {
        try {
            object.put("question", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getJson();
    }

    public String getJson() {
        return object.toString();
    }

    public void createJsonAnswers(ArrayList<NewAnswers> answers) {
        JSONArray array = new JSONArray();
        JSONArray carray = new JSONArray();
        for (NewAnswers a : answers) {
            array.put(a.getAnswer());
            if (a.isCorrect())
                carray.put(a.getAnswer());

        }
        try {
            object.put("answers", array);
            object.put("correct", carray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getJson();
    }
}
