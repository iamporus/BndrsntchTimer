# BndrsntchTimer
A horizontal progress bar shrinking with time; similar to Bandersnatch choice interface.  
## Inspiration
![](bandersnatch.png)

## Usage

### XML

```
 <com.prush.bndrsntchtimer.BndrsntchTimer
    android:id="@+id/timer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentTop="true"
    android:layout_marginBottom="8dp"
    app:progress_color="@android:color/white"/>
            
```

### Java
```
final BndrsntchTimer bndrsntchTimer = findViewById( R.id.timer );
bndrsntchTimer.start( 10000 );

 bndrsntchTimer.setOnTimerElapsedListener( new BndrsntchTimer.OnTimerElapsedListener()
        {
            @Override
            public void onTimeElapsed( long millis )
            {
                if( millis >= 10000 )
                {
                  //Timer elapsed.
                }
            }
        });

```
