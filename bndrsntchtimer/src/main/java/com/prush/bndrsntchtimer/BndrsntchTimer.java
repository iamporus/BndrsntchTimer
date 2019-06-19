package com.prush.bndrsntchtimer;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressWarnings( { "unused", "SpellCheckingInspection" } )
public class BndrsntchTimer extends View implements LifecycleObserver
{
    private static final String TAG = "BndrsntchTimer";
    private static final String LEFT_POS_PROPERTY = "xLeftPos";

    private static int DEFAULT_HEIGHT = 16;                     //px
    private static int DEFAULT_ROUND_RECT_RADIUS = 0;           //px
    private static final int DEFAULT_STROKE_WIDTH = 2;          //px
    private static int DEFAULT_PROGRESS_COLOR = android.R.color.white;

    @SuppressLint( "ResourceAsColor" )
    private @ColorInt
    int mProgressColor = DEFAULT_PROGRESS_COLOR;
    private int mRoundRectRadius = DEFAULT_ROUND_RECT_RADIUS;
    private int mTimerHeight = DEFAULT_HEIGHT;
    private long mTimerDuration;
    private int mLeftXPosition;
    private int mFactor;

    private RectF mRectF;
    private Paint mBackgroundPaint;
    private OnTimerElapsedListener mOnTimerElapsedListener;
    private ValueAnimator mTransformValueAnimator;
    private long mCurrentPlayTime;
    private boolean mbViewVisible;

    /**
     * Callback to be invoked when Timer is elaspsed.
     */
    public interface OnTimerElapsedListener
    {
        /**
         * Notifies the implementer when timer has elapsed.
         *
         * @param elapsedDuration long ellapsed duration in millis
         * @param totalDuration   long    total duration in millis
         */
        void onTimeElapsed( final long elapsedDuration, final long totalDuration );
    }

    public BndrsntchTimer( Context context )
    {
        super( context );

        init();
    }

    public BndrsntchTimer( Context context, @Nullable AttributeSet attrs )
    {
        this( context, attrs, 0 );
        init();
    }

    public BndrsntchTimer( Context context, @Nullable AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );

        TypedArray array = context.obtainStyledAttributes( attrs, R.styleable.BndrsntchTimer, defStyleAttr, 0 );
        mProgressColor = array.getColor( R.styleable.BndrsntchTimer_progress_color, -1 );
        if( mProgressColor == -1 )
        {
            mProgressColor = ContextCompat.getColor( context, DEFAULT_PROGRESS_COLOR );
        }

        init();

        array.recycle();
    }

    private void init()
    {
        mRectF = new RectF();

        initPaint();
    }

    @SuppressLint( "ResourceType" )
    private void initPaint()
    {
        mBackgroundPaint = new Paint();
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias( true );
        mBackgroundPaint.setStrokeWidth( DEFAULT_STROKE_WIDTH );
        mBackgroundPaint.setColor( mProgressColor );
    }

    private void startAnimation( final long currentPlayTime )
    {
        PropertyValuesHolder propertyLeftPositionHolder = PropertyValuesHolder.ofInt( LEFT_POS_PROPERTY,
                                                                                      mLeftXPosition - getPaddingRight(),
                                                                                      getWidth() / 2 - getPaddingRight() );

        mTransformValueAnimator = new ValueAnimator();
        mTransformValueAnimator.setValues( propertyLeftPositionHolder );
        mTransformValueAnimator.setDuration( mTimerDuration );
        mTransformValueAnimator.setCurrentPlayTime( currentPlayTime );
        mTransformValueAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate( ValueAnimator valueAnimator )
            {
                if( mbViewVisible )
                {
                    mFactor = ( int ) valueAnimator.getAnimatedValue( LEFT_POS_PROPERTY );
                    invalidate();

                    if( valueAnimator.getCurrentPlayTime() >= mTimerDuration )
                    {
                        postDelayed( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                cleanup();
                            }
                        }, 500 );
                    }

                    if( mOnTimerElapsedListener != null )
                    {
                        mOnTimerElapsedListener.onTimeElapsed( valueAnimator.getCurrentPlayTime(), mTimerDuration );
                    }
                }
            }
        } );

        mTransformValueAnimator.start();
    }

    private void cleanup()
    {
        mCurrentPlayTime = 0;
        mFactor = 0;
        mTimerDuration = 0;
    }

    /**
     * Start the timer for passed in duration. The timer bar will shrink in provided duration and will invoke
     *
     * @param duration long duration this timer will run for.
     */
    public void start( final long duration )
    {
        if( mCurrentPlayTime == 0 )
        {
            mTimerDuration = duration;
            startAnimation( 0 );
        }
        else
        {
            Log.e( TAG, "start: ", new IllegalStateException( "Timer is already running." ) );
        }
    }

    /**
     * Start the timer for passed in duration. The timer bar will shrink in provided duration and will invoke
     *
     * @param duration long duration this timer will run for.
     * @param listener @{@link OnTimerElapsedListener} listener to get callback once the @{@link BndrsntchTimer} elaspses.
     */
    public void start( final long duration, final OnTimerElapsedListener listener )
    {
        mTimerDuration = duration;
        setOnTimerElapsedListener( listener );
        start( duration );
    }

    /**
     * Register a callback to be invoked when {@link BndrsntchTimer} is elapsed.
     *
     * @param onTimerElapsedListener {@link OnTimerElapsedListener}
     */
    public void setOnTimerElapsedListener( final OnTimerElapsedListener onTimerElapsedListener )
    {
        mOnTimerElapsedListener = onTimerElapsedListener;
    }

    /**
     * Sets the color for the progress indicator.
     *
     * @param progressColor the color of the progress indicator of {@link BndrsntchTimer}
     */
    @SuppressLint( "ResourceType" )
    public void setProgressColorInt( @ColorInt final int progressColor )
    {
        mProgressColor = ContextCompat.getColor( getContext(), progressColor );
        initPaint();
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        mLeftXPosition = getPaddingLeft() + mFactor;

        mRectF.left = mLeftXPosition;
        mRectF.right = getWidth() - getPaddingRight() - mFactor;

        mRectF.top = getPaddingTop();
        mRectF.bottom = mRectF.top + mTimerHeight - getPaddingBottom();

        canvas.drawRoundRect( mRectF, mRoundRectRadius, mRoundRectRadius, mBackgroundPaint );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
    {
        Log.v( TAG, MeasureSpec.toString( widthMeasureSpec ) );
        Log.v( TAG, MeasureSpec.toString( heightMeasureSpec ) );

        int desiredWidth = getMeasuredWidth();
        int desiredHeight = mTimerHeight;

        desiredWidth = desiredWidth + getPaddingLeft() + getPaddingRight();
        desiredHeight = desiredHeight + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension( measureDimension( desiredWidth, widthMeasureSpec ), measureDimension( desiredHeight, heightMeasureSpec ) );
    }

    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh )
    {
        super.onSizeChanged( w, h, oldw, oldh );
        if( mCurrentPlayTime != 0 )
        {
            post( new Runnable()
            {
                @Override
                public void run()
                {
                    startAnimation( mCurrentPlayTime );
                }
            } );
        }
    }

    private int measureDimension( int desiredSize, int measureSpec )
    {
        int result = desiredSize;

        int specMode = MeasureSpec.getMode( measureSpec );
        int specSize = MeasureSpec.getSize( measureSpec );

        switch( specMode )
        {
            case MeasureSpec.EXACTLY:
            {
                result = specSize;
                break;
            }
            case MeasureSpec.AT_MOST:
            {
                result = Math.min( result, specSize );
                break;
            }
            case MeasureSpec.UNSPECIFIED:
            {
                result = desiredSize;
                break;
            }
        }

        if( result < desiredSize )
        {
            Log.e( TAG, "The view is too small." );
        }

        return result;
    }

    /**
     * Returns a LifecycleObserver that expects to be notified when the LifecycleOwner changes state.
     * Add this as a {@link LifecycleObserver} to {@link android.support.v7.app.AppCompatActivity} or
     * {@link android.support.v4.app.Fragment}
     *
     * @return LifecycleObserver
     */
    public LifecycleObserver getLifecycleObserver()
    {
        return this;
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_START )
    private void onViewStarted()
    {
        mbViewVisible = true;
        if( mCurrentPlayTime != 0 )
        {
            startAnimation( mCurrentPlayTime );
        }
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_STOP )
    private void onViewStopped()
    {
        mbViewVisible = false;
        if( mTransformValueAnimator != null && mTransformValueAnimator.isRunning() )
        {
            mCurrentPlayTime = mTransformValueAnimator.getCurrentPlayTime();
            mTransformValueAnimator.cancel();
        }
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState( superState );
        savedState.setSavedPlayTime( mCurrentPlayTime );
        savedState.setSavedDuration( mTimerDuration );
        savedState.setSavedFactor( mFactor );
        savedState.setSavedPosition( mLeftXPosition );
        return savedState;
    }

    @Override
    public void onRestoreInstanceState( Parcelable state )
    {
        SavedState savedState = ( SavedState ) state;
        super.onRestoreInstanceState( savedState.getSuperState() );

        mCurrentPlayTime = savedState.getSavedPlayTime();
        mTimerDuration = savedState.getSavedDuration();
        mFactor = savedState.getSavedFactor();
        mLeftXPosition = savedState.getSavedPosition();

        if( mCurrentPlayTime != 0 )
        {
            startAnimation( mCurrentPlayTime );
        }
    }

    /**
     * Class to save view's internal state across lifecycle owner's state changes.
     */
    private static class SavedState extends BaseSavedState
    {
        private long mSavedPlayTime;
        private long mSavedDuration;
        private int mSavedFactor;
        private int mSavedPosition;

        private static final int PLAY_TIME_INDEX = 0, FACTOR_INDEX = 0;
        private static final int DURATION_INDEX = 1, POSITION_INDEX = 1;

        private SavedState( Parcel source )
        {
            super( source );

            long[] longValues = { 0, 0 };
            source.readLongArray( longValues );

            mSavedPlayTime = longValues[ PLAY_TIME_INDEX ];
            mSavedDuration = longValues[ DURATION_INDEX ];

            int[] intValues = { 0, 0 };
            source.readIntArray( intValues );
            mSavedFactor = intValues[ FACTOR_INDEX ];
            mSavedPosition = intValues[ POSITION_INDEX ];
        }

        private SavedState( Parcelable superState )
        {
            super( superState );
        }

        private void setSavedPlayTime( long savedPlayTime )
        {
            mSavedPlayTime = savedPlayTime;
        }

        private long getSavedPlayTime()
        {
            return mSavedPlayTime;
        }

        private long getSavedDuration()
        {
            return mSavedDuration;
        }

        private void setSavedDuration( long savedDuration )
        {
            mSavedDuration = savedDuration;
        }

        private int getSavedFactor()
        {
            return mSavedFactor;
        }

        private void setSavedFactor( int savedFactor )
        {
            mSavedFactor = savedFactor;
        }

        private int getSavedPosition()
        {
            return mSavedPosition;
        }

        private void setSavedPosition( int savedPosition )
        {
            mSavedPosition = savedPosition;
        }

        @Override
        public void writeToParcel( Parcel out, int flags )
        {
            super.writeToParcel( out, flags );

            long[] longValues = { mSavedPlayTime, mSavedDuration };
            out.writeLongArray( longValues );

            int[] intValues = { mSavedFactor, mSavedPosition };
            out.writeIntArray( intValues );
        }

        public static final Parcelable.Creator< SavedState > CREATOR = new Creator< SavedState >()
        {
            @Override
            public SavedState createFromParcel( Parcel parcel )
            {
                return new SavedState( parcel );
            }

            @Override
            public SavedState[] newArray( int size )
            {
                return new SavedState[ size ];
            }
        };
    }

}
