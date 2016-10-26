package com.marianosimone.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.marianosimone.geoquiz.is_answer_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.marianosimone.geoquiz.answer_shown";
    private static final String KEY_ANSWER_SHOWN = "answer_shown";
    private boolean mAnswerShown;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN);
            setAnswerShownResult();
        }

        final Button showAnswerButton = (Button) findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final boolean isAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
                final TextView answerTextView = (TextView) findViewById(R.id.answer_text_view);
                answerTextView.setText(isAnswerTrue ? R.string.true_button : R.string.false_button);
                mAnswerShown = true;
                setAnswerShownResult();
                showAnimation();
            }

            private void showAnimation() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final int cx = showAnswerButton.getWidth() / 2;
                    final int cy = showAnswerButton.getHeight() / 2;
                    final float radius = showAnswerButton.getWidth();
                    final Animator animator = ViewAnimationUtils
                            .createCircularReveal(showAnswerButton, cx, cy, radius, 0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(final Animator animation) {
                            super.onAnimationEnd(animation);
                            showAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();
                } else {
                    showAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
    }

    static Intent newIntent(final Context packageContext, boolean isAnswerTrue) {
        final Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue);
        return intent;
    }

    private void setAnswerShownResult() {
        final Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(final Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
