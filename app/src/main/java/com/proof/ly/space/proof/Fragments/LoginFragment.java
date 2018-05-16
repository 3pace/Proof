package com.proof.ly.space.proof.Fragments;

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
import android.view.Menu;
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

    private Button btn;
    private TextView txt_timer;
    private EditText etxt_name, etxt_phone, etxt_code;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private int btnType = 0;
    private PhoneAuthProvider mProvider;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private CountDownTimer timer;
    private String mName;
    private String mPhone;
    private static final int SECONDS_REM = 60;
    private DBManager mDBManager;

    public static Fragment getInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = ((MainActivity) getActivity()).getAuth();
        mDBManager = ((MainActivity) getActivity()).getmDBManager();

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
        etxt_name = itemView.findViewById(R.id.etxt_name);
        etxt_phone = itemView.findViewById(R.id.etxt_login);
        etxt_code = itemView.findViewById(R.id.etxt_code);
        txt_timer = itemView.findViewById(R.id.txt_timer);
        btn = itemView.findViewById(R.id.btn_next);
        txt_timer.setTextColor(((MainActivity) getActivity()).getDisabledColor());

    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        etxt_name.setTypeface(typeface);
        etxt_phone.setTypeface(typeface);
        etxt_code.setTypeface(typeface);
        txt_timer.setTypeface(typeface);
        btn.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = etxt_name.getText().toString().trim();
                mPhone = etxt_phone.getText().toString().trim();
                switch (btnType) {
                    case 0:
                        if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPhone)) {
                            mProvider.verifyPhoneNumber(
                                    "+7" + mPhone,
                                    SECONDS_REM,
                                    TimeUnit.SECONDS,
                                    getActivity(),
                                    mCallbacks
                            );
                            etxt_name.setEnabled(false);
                            btn.setEnabled(false);
                        } else {
                            Toast.makeText(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1:

                        if (!TextUtils.isEmpty(etxt_code.getText())) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etxt_code.getText().toString().trim());
                            signInWithPhoneAuthCredential(credential);
                            btn.setEnabled(false);
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
                btn.setEnabled(true);
                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                etxt_code.animate().alpha(1).setDuration(200).start();
                etxt_code.setEnabled(true);
                btn.setEnabled(true);
                btn.setText(getResources().getString(R.string.send_code));
                btnType = 1;

                txt_timer.setVisibility(View.VISIBLE);

                timer = new CountDownTimer(SECONDS_REM * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txt_timer.setText(":".concat(String.valueOf(millisUntilFinished / 1000)));
                    }

                    public void onFinish() {
                        txt_timer.setVisibility(View.INVISIBLE);
                        btnType = 0;
                        btn.setText(getResources().getString(R.string.get_code));
                    }
                }.start();

            }
        };
    }

    @Override
    public void initSetters() {
        etxt_code.setEnabled(false);
        etxt_code.setAlpha(0);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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


                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            btn.setEnabled(true);



                        } else {
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            btn.setEnabled(true);
                        }
                        timer.cancel();
                    }
                });
    }

}
