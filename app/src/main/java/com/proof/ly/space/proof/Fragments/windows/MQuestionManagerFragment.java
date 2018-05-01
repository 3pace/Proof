package com.proof.ly.space.proof.Fragments.windows;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.Adapters.QMVPAdapter;
import com.proof.ly.space.proof.CustomViews.MViewPager;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by aman on 4/16/18.
 */

public class MQuestionManagerFragment extends Fragment implements FragmentInterface{
    private Toolbar tbar;
    private TextView txt_tbar;
    private Typeface tf;
    private static MViewPager vpager;
    private QMVPAdapter adapter;
    private static Handler handler;



    public MQuestionManagerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new QMVPAdapter(getChildFragmentManager());
        handler = new Handler();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qmanager_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initSetters();
        initOnClick();

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

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {

        vpager.setAdapter(adapter);
        vpager.setAllowedSwipeDirection(MViewPager.SwipeDirection.none);
    }

    public static void nextPage(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vpager.setCurrentItem(vpager.getCurrentItem()+1);
            }
        },400);

    }
    public static void nextPageFast(){
        vpager.setCurrentItem(vpager.getCurrentItem()+1);
    }
}
