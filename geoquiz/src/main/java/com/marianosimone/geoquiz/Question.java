package com.marianosimone.geoquiz;

public class Question {
    private final int mTextResId;
    private final boolean mAnswer;

    public Question(int textResId, boolean answer) {
        mTextResId = textResId;
        mAnswer = answer;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswer;
    }
}
