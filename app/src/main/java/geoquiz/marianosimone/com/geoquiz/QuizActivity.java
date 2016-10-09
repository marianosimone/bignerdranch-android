package geoquiz.marianosimone.com.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private TextView mQuestionTextView;
    private Question[] mQuestions = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentQuestion = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                checkAnswer(false);
            }
        });

        final View.OnClickListener nextQuestionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mCurrentQuestion = (mCurrentQuestion + 1) % mQuestions.length;
                updateQuestion();
            }
        };
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextQuestionClickListener);

        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
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

    private void updateQuestion() {
        final int question = mQuestions[mCurrentQuestion].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(final boolean userAnswer) {
        boolean actualAnswer = mQuestions[mCurrentQuestion].isAnswerTrue();
        final int messageResId = userAnswer == actualAnswer
                ? R.string.correct_toast
                : R.string.incorrect_toast;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
