package com.prush.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prush.bndrsntchtimer.BndrsntchTimer;

public class MainActivity extends AppCompatActivity
{

    @SuppressLint( "ResourceAsColor" )
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        final BndrsntchTimer bndrsntchTimer = findViewById( R.id.bndrsntchTimer );
        bndrsntchTimer.setProgressColorInt( R.color.colorAccent );

        Button button = findViewById( R.id.button );
        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                bndrsntchTimer.start( 5000 );
            }
        } );

        bndrsntchTimer.setOnTimerElapsedListener( new BndrsntchTimer.OnTimerElapsedListener()
        {
            @Override
            public void onTimerElapsed()
            {
                Toast.makeText( getApplicationContext(), "Finished", Toast.LENGTH_SHORT ).show();
            }
        } );

        getLifecycle().addObserver( bndrsntchTimer.getLifecycleObserver() );
    }
}
