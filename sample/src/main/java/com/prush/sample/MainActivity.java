package com.prush.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prush.bndrsntchtimer.BndrsntchTimer;
import com.prush.typedtextview.TypedTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    @SuppressLint( "ResourceAsColor" )
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        final BndrsntchTimer bndrsntchTimer = findViewById( R.id.timer );
        final TypedTextView textView = findViewById( R.id.stageTextView );
        final RelativeLayout layout = findViewById( R.id.choiceLayout );

        textView.setTypedText( "Two roads diverged in a wood. Which one would you choose to travel?" );
        textView.setOnCharacterTypedListener( new TypedTextView.OnCharacterTypedListener()
        {
            @Override
            public void onCharacterTyped( char character, int index )
            {
                Log.d( "MainActivity", "onCharacterTyped: " + textView.getText().length() + " - " + index );
                if( textView.getText().length() - 1 == index )
                {
                    ObjectAnimator valueAnimator = ObjectAnimator.ofFloat( layout, "alpha", 0f, 1f );
                    valueAnimator.setDuration( 1000 );
                    valueAnimator.start();

                    valueAnimator.addListener( new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd( Animator animation )
                        {
                            bndrsntchTimer.start( 10000 );
                        }
                    } );
                }
            }
        } );

        bndrsntchTimer.setOnTimerElapsedListener( new BndrsntchTimer.OnTimerElapsedListener()
        {
            @Override
            public void onTimeElapsed( long millis )
            {
                if( millis >= 10000 )
                {
                    Random random = new Random();
                    int choice = random.nextInt( 2 );
                    if( choice == 0 )
                    {
                        ( ( TextView ) findViewById( R.id.leftChoiceTextView ) ).setTextColor( ContextCompat.getColor( getApplicationContext(), R.color.colorAccent ) );
                    }
                    else
                    {
                        ( ( TextView ) findViewById( R.id.rightChoiceTextView ) ).setTextColor( ContextCompat.getColor( getApplicationContext(), R.color.colorAccent ) );
                    }
                }
            }
        } );

        getLifecycle().addObserver( bndrsntchTimer.getLifecycleObserver() );
    }
}
