package com.proof.ly.space.proof.Utils;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aman on 25.03.18.
 */

public class ClickEffect {
    private static int DURATION = 500;
    private static float ALPHA = 0.2f;
    public static void setView(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        view.animate().alpha(ALPHA).setDuration(DURATION - 300).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.animate().alpha(ALPHA).setDuration(0).start();
                        view.animate().alpha(1f).setDuration(DURATION).start();
                        break;
                    default:
                        view.animate().alpha(1f).setDuration(DURATION).start();
                }
                return false;
            }
        });
    }
    public static void setViewFast(View view){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        view.animate().alpha(ALPHA).setDuration(DURATION-300).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.animate().alpha(1f).setDuration(DURATION).start();
                        break;
                    default:
                        view.animate().alpha(1f).setDuration(DURATION).start();
                }
                return false;
            }
        });

    }
}
