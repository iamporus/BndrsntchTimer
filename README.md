# BndrsntchTimer
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/iamporus/BndrsntchTimer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/iamporus/BndrsntchTimer/context:java)
[![Build Status](https://travis-ci.com/iamporus/BndrsntchTimer.svg?branch=master)](https://travis-ci.com/iamporus/BndrsntchTimer)
[![](https://jitpack.io/v/iamporus/BndrsntchTimer.svg)](https://jitpack.io/#iamporus/BndrsntchTimer)

A horizontal progress bar shrinking with time; similar to Bandersnatch choice interface.  
## Inspiration
<img src="bandersnatch.png" width="600">

## Preview
<img src="preview.gif">

## Gradle
* **Step 1.** Add the JitPack repository to your build file.

Add following in your **Project level** build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
* **Step 2.** Add the dependency
```gradle
dependencies {
  ...
  implementation 'com.github.iamporus:BndrsntchTimer:x.y.z'
}
```
The latest version of BndrsntchTimer is  [![](https://jitpack.io/v/iamporus/BndrsntchTimer.svg)](https://jitpack.io/#iamporus/BndrsntchTimer)


## Usage

### XML

```xml
 <com.prush.bndrsntchtimer.BndrsntchTimer
    android:id="@+id/timer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentTop="true"
    android:layout_marginBottom="8dp"
    app:progress_color="@android:color/white"/>
            
```

### Java
```java
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
