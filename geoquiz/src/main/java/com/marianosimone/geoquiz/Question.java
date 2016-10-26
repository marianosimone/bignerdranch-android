package com.marianosimone.geoquiz;

class Question {
    private final int mTextResId;
    private final boolean mAnswer;

    Question(int textResId, boolean answer) {
        mTextResId = textResId;
        mAnswer = answer;
    }

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswer;
    }
}
