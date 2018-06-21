package com.proof.ly.space.proof.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.util.WorkSourceUtil.TAG;

/**
 * Created by aman on 29.03.18.
 */

public class LoginFragment extends Fragment implements FragmentInterface {

    private Button mButton;
    private TextView mTextViewTimer;
    private EditText mEditTextName, mEditTextPhone, mEditTextCode;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private int mButtonType = 0;
    private PhoneAuthProvider mProvider;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private CountDownTimer mDownTimer;
    private String mName;
    private String mPhone;
    private static final int SECONDS_REM = 60;
    private DBManager mDBManager;
    private MainActivity mActivity;

    public static Fragment getInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = mActivity.getAuth();
        mDBManager = mActivity.getDatabaseManager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initSetters();
        initTypeface();
        initOnClick();
    }

    @Override
    public void initViews(View itemView) {
        mEditTextName = itemView.findViewById(R.id.etxt_name);
        mEditTextPhone = itemView.findViewById(R.id.etxt_login);
        mEditTextCode = itemView.findViewById(R.id.etxt_code);
        mTextViewTimer = itemView.findViewById(R.id.txt_timer);
        mButton = itemView.findViewById(R.id.btn_next);
        mTextViewTimer.setTextColor(mActivity.getDisabledColor());

    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mEditTextName.setTypeface(typeface);
        mEditTextPhone.setTypeface(typeface);
        mEditTextCode.setTypeface(typeface);
        mTextViewTimer.setTypeface(typeface);
        mButton.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = mEditTextName.getText().toString().trim();
                mPhone = mEditTextPhone.getText().toString().trim();
                switch (mButtonType) {
                    case 0:
                        if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPhone)) {
                            mProvider.verifyPhoneNumber(
                                    "+7" + mPhone,
                                    SECONDS_REM,
                                    TimeUnit.SECONDS,
                                    mActivity,
                                    mCallbacks
                            );
                            mEditTextName.setEnabled(false);
                            mButton.setEnabled(false);
                        } else {
                            Toast.makeText(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1:

                        if (!TextUtils.isEmpty(mEditTextCode.getText())) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mEditTextCode.getText().toString().trim());
                            signInWithPhoneAuthCredential(credential);
                            mButton.setEnabled(false);
                        }

                        break;
                }


            }
        });

    }

    @Override
    public void initObjects() {
        mProvider = PhoneAuthProvider.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mButton.setEnabled(true);
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                mEditTextCode.animate().alpha(1).setDuration(200).start();
                mEditTextCode.setEnabled(true);
                mButton.setEnabled(true);
                mButton.setText(getResources().getString(R.string.send_code));
                mButtonType = 1;

                mTextViewTimer.setVisibility(View.VISIBLE);

                mDownTimer = new CountDownTimer(SECONDS_REM * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mTextViewTimer.setText(":".concat(String.valueOf(millisUntilFinished / 1000)));
                    }

                    public void onFinish() {
                        mTextViewTimer.setVisibility(View.INVISIBLE);
                        mButtonType = 0;
                        mButton.setText(getResources().getString(R.string.get_code));
                    }
                }.start();

            }
        };
    }

    @Override
    public void initSetters() {
        mEditTextCode.setEnabled(false);
        mEditTextCode.setAlpha(0);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if (user.getDisplayName() == null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(mName).build();
                                user.updateProfile(profileUpdates);
                                mDBManager.getmRef().child(DBManager.FT_ONLINE).child(mName).setValue("yes");
                            } else {
                                mDBManager.getmRef().child(DBManager.FT_ONLINE).child(user.getDisplayName()).setValue("yes");
                            }


                            mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                            mActivity.finish();
                            mButton.setEnabled(true);



                        } else {
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            mButton.setEnabled(true);
                        }
                        mDownTimer.cancel();
                    }
                });
    }

}
