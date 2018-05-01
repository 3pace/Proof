package com.proof.ly.space.proof.Fragments.windows;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proof.ly.space.proof.Adapters.RecyclerSearchAdapter;
import com.proof.ly.space.proof.CustomViews.MRecyclerView;
import com.proof.ly.space.proof.Data.SearchData;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by aman on 4/17/18.
 */

public class MSearchFragment extends Fragment implements FragmentInterface{

    private MRecyclerView rview;
    private RecyclerSearchAdapter adapter;
    private ArrayList<SearchData> arrayList = new ArrayList<>();
    private EditText etxt;
    private DBManager dbManager;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int addItemCount = 5;
    private boolean loading;
    private View mEmptyView;

    public MSearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        new getData().execute();
    }

    @Override
    public void initViews(View itemView) {
        rview = itemView.findViewById(R.id.rview);
        etxt = itemView.findViewById(R.id.etxt_search);
        mEmptyView = itemView.findViewById(R.id.img_empty);

    }

    @Override
    public void initTypeface() {
        etxt.setTypeface(((MainActivity)getActivity()).getTypeface());
    }

    @Override
    public void initOnClick() {
        etxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etxt.setText("");
                adapter.filter(etxt.getText().toString());
                return true;
            }
        });
    }

    @Override
    public void initObjects() {
        adapter = new RecyclerSearchAdapter(arrayList);
        dbManager = ((MainActivity)getActivity()).getDbManager();
    }

    @Override
    public void initSetters() {
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
        adapter.setTypeface(((MainActivity)getActivity()).getTypeface());
        etxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        initLoadMore();


    }
    private class getData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList.clear();
            initObjects();
            initSetters();
            initTypeface();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //dbManager.getSearchHelper();
            dbManager.generateSearchListFullFromLocalDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: "+ LessonManager.CURRENT_DB);
            adapter.setFull(dbManager.getSearchList(10));
            adapter.setFullList(dbManager.getFullSearchList());
            rview.setEmptyView(mEmptyView);
            initOnClick();
        }
    }
    private class loadData extends AsyncTask<Integer,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            //arrayList.addAll(dbManager.getSearchListLoad(integers[0],integers[1]));
            adapter.addLoadedItems(dbManager.getSearchListLoad(integers[0],integers[1]),integers[0],integers[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            loading = false;
        }
    }
    public void initLoadMore(){
        final LinearLayoutManager lmanager = (LinearLayoutManager) rview.getLayoutManager();
        rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = lmanager.getItemCount();
                lastVisibleItem = lmanager.findLastVisibleItemPosition();
                if (!loading && (lastVisibleItem+visibleThreshold) >= totalItemCount && etxt.getText().length() <=0) {
                    new loadData().execute(adapter.getItemCount(),addItemCount);
                }
            }
        });
    }
}
