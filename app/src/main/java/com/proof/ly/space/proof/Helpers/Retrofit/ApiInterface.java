package com.proof.ly.space.proof.Helpers.Retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("questions")
    Call<ArrayList<QuestionResponse>> getAllQuestions();
}
