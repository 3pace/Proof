package com.proof.ly.space.proof.Fragments.windows;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

/**
 * Created by aman on 5/9/18.
 */

public class MUserFragment extends Fragment implements FragmentInterface {

    private TextView mInfoTextView, mUserOnlineTextView;
    private Button mLogoutButton;
    private FirebaseAuth mAuth;
    private String mUsername;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private int mOnlineUsersCount;
    private String mOnlineTitle, mOnlineUsers = "";
    private String mFullText;


    public MUserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = ((MainActivity) getActivity()).getAuth();
        mUser = mAuth.getCurrentUser();
        mUsername = mUser != null ? mUser.getDisplayName() : "?#$!&^";
        mRef = FirebaseDatabase.getInstance().getReference(DBManager.FT_ONLINE);

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initSetters();
        initOnClick();
        mOnlineTitle = getResources().getString(R.string.is_online);
    }

    @Override
    public void initViews(View itemView) {
        mInfoTextView = itemView.findViewById(R.id.txt_user_info);
        mUserOnlineTextView = itemView.findViewById(R.id.txt_online_info);
        mLogoutButton = itemView.findViewById(R.id.btn_logout);
    }

    @Override
    public void initTypeface() {
        Typeface mTypeface = ((MainActivity) getActivity()).getTypeface();
        mInfoTextView.setTypeface(mTypeface);
        mUserOnlineTextView.setTypeface(mTypeface);
        mLogoutButton.setTypeface(mTypeface);
    }

    @Override
    public void initOnClick() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child(mUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAuth.signOut();
                            getActivity().onBackPressed();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void initObjects() {

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOnlineUsersCount = (int) dataSnapshot.getChildrenCount();
                mOnlineUsers = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    mOnlineUsers = mOnlineUsers.concat(snapshot.getKey()).concat(", ");
                mFullText = mOnlineTitle.concat(" (").concat(String.valueOf(mOnlineUsersCount)).concat(") ").concat(mOnlineUsers).replace(mUsername, "Вы").trim();
                if (mOnlineUsersCount > 0)
                    mFullText = mFullText.substring(0, mFullText.length() - 1) + ".";
                mUserOnlineTextView.setText(mFullText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void initSetters() {
        mInfoTextView.setText(getResources().getString(R.string.welcome).concat(", ").concat(mUsername).concat("."));

    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
