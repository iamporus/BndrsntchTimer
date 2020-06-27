package com.prush.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prush.bndrsntchtimer.BndrsntchTimer;
import com.prush.typedtextview.TypedTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BndrsntchTimer mBndrsntchTimer;
    private TypedTextView mTypedTextView;
    private RelativeLayout mRelativeLayout;
    private TextView mLeftChoiceView;
    private TextView mRightChoiceView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBndrsntchTimer = findViewById(R.id.timer);
        mTypedTextView = findViewById(R.id.stageTextView);
        mRelativeLayout = findViewById(R.id.choiceLayout);
        mLeftChoiceView = findViewById(R.id.leftChoiceTextView);
        mRightChoiceView = findViewById(R.id.rightChoiceTextView);

        mTypedTextView.splitSentences(false);
        mTypedTextView.setTypedText("Dad asks Stefan about Lunch. Stefan just gets angry. How he should react?");

        mTypedTextView.setOnCharacterTypedListener(new TypedTextView.OnCharacterTypedListener() {
            @Override
            public void onCharacterTyped(char character, int index) {
                Log.d("MainActivity", "onCharacterTyped: " + mTypedTextView.getText().length() + " - " + index);
                if (mTypedTextView.getText().length() - 1 == index) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRelativeLayout, "alpha", 0f, 1f);
                    objectAnimator.setDuration(2000);
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mBndrsntchTimer.start(10000);
                        }
                    });
                    objectAnimator.start();

                }
            }
        });

        mBndrsntchTimer.setOnTimerElapsedListener(new BndrsntchTimer.OnTimerElapsedListener() {
            @Override
            public void onTimeElapsed(long elapsedDuration, long totalDuration) {
                if (elapsedDuration >= totalDuration) {
                    Random random = new Random();
                    int choice = random.nextInt(2);
                    if (choice == 0) {
                        mLeftChoiceView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                    } else {
                        mRightChoiceView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                    }

                    Toast.makeText(getApplicationContext(), "Click on Left choice to reset timer, Right choice to start again.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mLeftChoiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBndrsntchTimer.reset(new BndrsntchTimer.OnTimerResetListener() {
                    @Override
                    public void onTimerResetCompleted() {
                        Log.d("TAG", "onTimerResetCompleted: ");
                    }
                });
                mLeftChoiceView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            }
        });

        mRightChoiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBndrsntchTimer.start(10000);
                mRightChoiceView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            }
        });

        getLifecycle().addObserver(mBndrsntchTimer.getLifecycleObserver());
        getLifecycle().addObserver(mTypedTextView.getLifecycleObserver());
    }
}
