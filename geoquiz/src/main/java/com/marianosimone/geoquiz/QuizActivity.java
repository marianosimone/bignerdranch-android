package com.marianosimone.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_QUESTION_INDEX = "question_index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView mQuestionTextView;
    private final Question[] mQuestions = new Question[] {
            new Question(R.string.question_turkey, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentQuestion = 0;
    private boolean[] mIsCheater;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentQuestion = savedInstanceState.getInt(KEY_QUESTION_INDEX, 0);
            mIsCheater = savedInstanceState.getBooleanArray(KEY_IS_CHEATER);
        }

        if (mIsCheater == null) {
            mIsCheater = new boolean[mQuestions.length];
        }

        final Button trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkAnswer(true);
            }
        });
        final Button falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkAnswer(false);
            }
        });
        final Button cheatButton = (Button) findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final boolean answerIsTrue = mQuestions[mCurrentQuestion].isAnswerTrue();
                startActivityForResult(
                        CheatActivity.newIntent(QuizActivity.this, answerIsTrue),
                        REQUEST_CODE_CHEAT
                );
            }
        });
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkAnswer(true);
            }
        });
        final View.OnClickListener nextQuestionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCurrentQuestion = (mCurrentQuestion + 1) % mQuestions.length;
                updateQuestion();
            }
        };
        final ImageButton nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(nextQuestionClickListener);

        final ImageButton previousButton = (ImageButton) findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mCurrentQuestion == 0) {
                    mCurrentQuestion = mQuestions.length;
                }
                mCurrentQuestion = (mCurrentQuestion - 1) % mQuestions.length;
                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();
        mQuestionTextView.setOnClickListener(nextQuestionClickListener);
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState() called");
        savedInstanceState.putInt(KEY_QUESTION_INDEX, mCurrentQuestion);
        savedInstanceState.putBooleanArray(KEY_IS_CHEATER, mIsCheater);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater[mCurrentQuestion] = mIsCheater[mCurrentQuestion] || CheatActivity.wasAnswerShown(data);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        final int question = mQuestions[mCurrentQuestion].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(final boolean userAnswer) {
        boolean actualAnswer = mQuestions[mCurrentQuestion].isAnswerTrue();
        final int messageResId = mIsCheater[mCurrentQuestion]
                ? R.string.judgment_toast
                : userAnswer == actualAnswer
                    ? R.string.correct_toast
                    : R.string.incorrect_toast;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
