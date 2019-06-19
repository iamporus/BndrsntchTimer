# BndrsntchTimer
A horizontal progress bar shrinking with time; similar to Bandersnatch choice interface.  
## Inspiration
<img src="bandersnatch.png" width="600">

## Preview
<img src="preview.gif">


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

//set progress color
bndrsntchTimer.setProgressColorInt( android.R.color.holo_blue_bright );

bndrsntchTimer.start( 10000 );

//attach listener
bndrsntchTimer.setOnTimerElapsedListener( new BndrsntchTimer.OnTimerElapsedListener()
{
    @Override
    public void onTimeElapsed( long elapsedDuration, long totalDuration )
    {
        if( elapsedDuration >= totalDuration )
        {
            //Timer elapsed.
        }
    }
});

//reset it for repeated usage.
bndrsntchTimer.reset();

// set duration along with listener
bndrsntchTimer.start( 5000, new BndrsntchTimer.OnTimerElapsedListener()
{
    @Override
    public void onTimeElapsed( long elapsedDuration, long totalDuration )
    {
        if( elapsedDuration >= totalDuration )
        {
            //Timer elapsed.
        }
    }
});

```

## License
```
Copyright 2019 Purushottam Pawar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
