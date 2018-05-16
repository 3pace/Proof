package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.util.Log;

import com.proof.ly.space.proof.Data.Answer;
import com.proof.ly.space.proof.Data.JsonQuestion;
import com.proof.ly.space.proof.Data.NewAnswer;
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
    private ArrayList<Question> mArrayListQuestions = new ArrayList<>();
    private ArrayList<JsonQuestion> mArrayListJsonQuestions = new ArrayList<>();
    private DBManager mDbManager;
    private JSONObject mJsonObject;
    public static boolean isGenerate = false;

    public QManager(DBManager dbManager, Context c) {
        this.mDbManager = dbManager;
    }

    public ArrayList<Question> generateQuestionsList() {

        if (isGenerate) mArrayListQuestions.clear();
        if (LessonManager.isLessonChangedForTesting) mArrayListQuestions.clear();
        if (mArrayListQuestions.size() <= 0) {
            Log.d(TAG, "generateQuestionsList: firstgenrated success");

            for (JsonQuestion jsonQuestion : mArrayListJsonQuestions) {
                try {
                    JSONObject object = new JSONObject(jsonQuestion.getJsonQuestion());
                    int qId = jsonQuestion.getId();
                    String question = object.getString("question");
                    JSONArray answers = object.getJSONArray("answers");
                    JSONArray canswers = object.getJSONArray("correct");
                    ArrayList<Answer> a = new ArrayList<>();
                    boolean isC = false;
                    int cAnswers = 0;//колчиество правильных вариантов
                    if (answers.length() <= 0)
                        a.add(new Answer("click :(", isC));
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

                        a.add(new Answer(answers.getString(j), isC));

                    }
                    Collections.shuffle(a);
                    mArrayListQuestions.add(new Question(qId, question, a, cAnswers));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return mArrayListQuestions;
    }

    public ArrayList<Question> getQ() {
        return mArrayListQuestions;
    }

    public ArrayList<JsonQuestion> generateQFromDB() {
        mArrayListJsonQuestions.clear();
        mArrayListJsonQuestions.addAll(mDbManager.getQuestionFromAssets());
        return mArrayListJsonQuestions;
    }

    public ArrayList<JsonQuestion> generateQFromDB(int count) {
        //mArrayListJsonQuestions.clear();
/*
        проверяем если уже прошла генерация при запуске приложения,
        то каждый раз при старте тестирование, очищаем список и генерируем новые вопросы,
        а если нет, то берем сгенерированный список из запуска приложения */
        if (isGenerate) mArrayListJsonQuestions.clear();
        if (LessonManager.isLessonChangedForTesting) mArrayListJsonQuestions.clear();

        if (mArrayListJsonQuestions.size() <= 0) {
            Log.d(TAG, "generateQFromDB: GENERATE");
            if (SettingsManager.cycleMode)
                mArrayListJsonQuestions.addAll(mDbManager.getQuestionFromAssetsWithCycleFromLocalDb(count));
            else
                mArrayListJsonQuestions.addAll(mDbManager.getQuestionListFromLocalDb(count));
        }
        return mArrayListJsonQuestions;
    }

    public int getResult() {
        boolean acount;
        int points, ccount;
        int total = 0;
        int result = 0;

        for (Question question : mArrayListQuestions) {
            acount = question.getArrayListAnswers().size() > 5;//если больше 5, то значит 1 балл дает 2 балла
            points = question.getCorrectCheckedAnswersCount();
            ccount = question.getCorrectAnswersCount();


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

    public HashMap<String, Integer> getTestingResult() {
        HashMap<String, Integer> resultMap = new HashMap<>();
        boolean isCountMore5;
        int questionsCount = 0;
        int correctAnswersInQuestion, correctCheckedAnswers;
        int correctQuestionsCount = 0, notCorrectQuestionsCount = 0, totalPoints = 0, mistakes = 0;
        int editedPoints = 0, notChecked = 0;
        int totalPercent;
        if (mArrayListQuestions != null) {
            questionsCount = mArrayListQuestions.size();

            for (Question question : mArrayListQuestions) {
                correctQuestionsCount += question.isCorrectChecked() ? 1 : 0;
                notCorrectQuestionsCount += !question.isCorrectChecked() ? 1 : 0;
                mistakes += question.getNotCorrectCheckedAnswersCount();
                notChecked += question.isChecked() ? 0 : 1;

                isCountMore5 = question.getArrayListAnswers().size() > 5;
                correctAnswersInQuestion = question.getCorrectAnswersCount();
                correctCheckedAnswers = question.getCorrectCheckedAnswersCount();

                switch (correctAnswersInQuestion) {

                    case 3:
                        if (correctCheckedAnswers == 3) editedPoints = 2;
                        else if (correctCheckedAnswers == 2) editedPoints = 1;
                        else if (correctCheckedAnswers == 1) editedPoints = 0;
                        else if (correctCheckedAnswers == 0) editedPoints = 0;

                        break;
                    case 2:
                        if (correctCheckedAnswers == 2) editedPoints = 2;
                        else if (correctCheckedAnswers == 1) editedPoints = 1;
                        else if (correctCheckedAnswers == 0) editedPoints = 0;
                        break;
                    case 1:
                        if (isCountMore5) {
                            if (correctCheckedAnswers == 1) editedPoints = 2;
                            else if (correctCheckedAnswers == 0) editedPoints = 0;
                        } else {
                            if (correctCheckedAnswers == 1) editedPoints = 1;
                            else if (correctCheckedAnswers == 0) editedPoints = 0;
                        }
                        break;
                    default:
                        if (correctCheckedAnswers >= 3) editedPoints = 3;
                        else if (correctCheckedAnswers == 2) editedPoints = 1;
                        else if (correctCheckedAnswers == 1) editedPoints = 0;
                        else if (correctCheckedAnswers == 0) editedPoints = 0;
                        break;
                }

                totalPoints += editedPoints;
            }

        }
        totalPercent = (int) ((correctQuestionsCount * 100f) / questionsCount);

        resultMap.put(MConstans.TESTING_RESULT_CORRECT_QUESTIONS_COUNT, correctQuestionsCount);
        resultMap.put(MConstans.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT, notCorrectQuestionsCount);
        resultMap.put(MConstans.TESTING_RESULT_POINTS, totalPoints);
        resultMap.put(MConstans.TESTING_RESULT_MISTAKES, mistakes);
        resultMap.put(MConstans.TESTING_RESULT_PERCENT, totalPercent);
        resultMap.put(MConstans.TESTING_RESULT_NOT_CHECKED_COUNT, notChecked);
        resultMap.put(MConstans.TESTING_RESULT_QUESTIONS_COUNT, questionsCount);

        return resultMap;
    }

    public HashMap<String, Integer> getNotClickedAnswers() {
        HashMap<String, Integer> map = new HashMap<>();
        int count = 0, notcorrect = 0;
        if (mArrayListQuestions != null)
            for (Question question : mArrayListQuestions) {
                if (!question.isChecked()) {
                    count++;
                } else {
                    notcorrect += question.getNotCorrectCheckedQuestionsCount();
                }
            }
        map.put("notclicked", count);
        map.put("notcorrect", notcorrect);
        return map;
    }

    public void stopTesting() {


        for (Question question : mArrayListQuestions) {
            if (!question.isChecked()) {

                for (Answer answer : question.getArrayListAnswers()) {
                    if (answer.isCorrect()) {
                        answer.setIsChecked(1);
                        answer.setCorrectChecked(true);
                        answer.setEnabled(false);
                    }
                    if (answer.getIsChecked() != 1) {
                        answer.setIsChecked(2);
                        answer.setEnabled(false);
                    }

                }


            }
        }


    }

    public void initJson() {
        mJsonObject = new JSONObject();

    }

    public void createJsonQuestion(String question) {
        try {
            mJsonObject.put("question", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getJson();
    }

    public String getJson() {
        return mJsonObject.toString();
    }

    public void createJsonAnswers(ArrayList<NewAnswer> answers) {
        JSONArray array = new JSONArray();
        JSONArray carray = new JSONArray();
        for (NewAnswer a : answers) {
            array.put(a.getAnswer());
            if (a.isCorrect())
                carray.put(a.getAnswer());

        }
        try {
            mJsonObject.put("answers", array);
            mJsonObject.put("correct", carray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getJson();
    }
}
