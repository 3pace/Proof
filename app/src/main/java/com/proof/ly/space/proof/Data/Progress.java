package com.proof.ly.space.proof.Data;

public class Progress {
    private float mProgress = 0;
    private float mAnimProgress = 0;

    public Progress(float progress, float animProgress) {
        mProgress = progress;
        mAnimProgress = animProgress;
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public float getAnimProgress() {
        return mAnimProgress;
    }

    public void setAnimProgress(float animProgress) {
        mAnimProgress = animProgress;
    }
}
