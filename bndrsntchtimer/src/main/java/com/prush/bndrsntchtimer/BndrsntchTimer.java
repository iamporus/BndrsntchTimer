package com.prush.bndrsntchtimer;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressWarnings( { "unused", "SpellCheckingInspection" } )
public class BndrsntchTimer extends View
{
    private static final String TAG = "BndrsntchTimer";
    private static final String LEFT_POS_PROPERTY = "xLeftPos";

    private static long DEFAULT_TIMER_DURATION = 10000;         //millis
    private static int DEFAULT_HEIGHT = 32;                     //px
    private static int DEFAULT_ROUND_RECT_RADIUS = 4;           //px
    private static final int DEFAULT_STROKE_WIDTH = 2;          //px
    private static String DEFAULT_BACKGROUND_COLOR = "aqua";

    private String mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mRoundRectRadius = DEFAULT_ROUND_RECT_RADIUS;
    private int mTimerHeight = DEFAULT_HEIGHT;
    private long mTimerDuration = DEFAULT_TIMER_DURATION;
    private int mLeftXPosition;
    private int mFactor;

    private RectF mRectF;
    private Paint mBackgroundPaint;
    private OnTimerElapsedListener mOnTimerElapsedListener;

    /**
     * Callback to be invoked when Timer is elaspsed.
     */
    public interface OnTimerElapsedListener
    {
        /**
         * Notifies the implementer when timer has elapsed.
         */
        void onTimerElapsed();
    }

    public BndrsntchTimer( Context context )
    {
        super( context );
        init();
    }

    public BndrsntchTimer( Context context, @Nullable AttributeSet attrs )
    {
        super( context, attrs );
        init();
    }

    public BndrsntchTimer( Context context, @Nullable AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        init();
    }

    private void init()
    {
        mRectF = new RectF();

        mBackgroundPaint = new Paint();
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias( true );
        mBackgroundPaint.setStrokeWidth( DEFAULT_STROKE_WIDTH );
        mBackgroundPaint.setColor( Color.parseColor( mBackgroundColor ) );

    }

    private void start()
    {
        PropertyValuesHolder propertyLeftPositionHolder = PropertyValuesHolder.ofInt( LEFT_POS_PROPERTY,
                                                                                      mLeftXPosition - getPaddingRight(),
                                                                                      getWidth() / 2 - getPaddingRight() );

        ValueAnimator transformValueAnimator = new ValueAnimator();
        transformValueAnimator.setValues( propertyLeftPositionHolder );
        transformValueAnimator.setDuration( mTimerDuration );
        transformValueAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate( ValueAnimator valueAnimator )
            {
                mFactor = ( int ) valueAnimator.getAnimatedValue( LEFT_POS_PROPERTY );
                invalidate();
                if( mOnTimerElapsedListener != null && mFactor == ( getWidth() / 2 - getPaddingRight() ) )
                {
                    mOnTimerElapsedListener.onTimerElapsed();
                }
            }
        } );

        transformValueAnimator.start();
    }

    /**
     * Start the timer for passed in duration. The timer bar will shrink in provided duration and will invoke
     *
     * @param duration long duration this timer will run for.
     */
    public void start( final long duration )
    {
        mTimerDuration = duration;
        start();
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
        start();
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


    @Override
    protected void onDraw( Canvas canvas )
    {
        mLeftXPosition = getPaddingLeft() + mFactor;

        mRectF.left = mLeftXPosition;
        mRectF.right = getWidth() - getPaddingRight() - mFactor;

        mRectF.top = ( float ) ( getHeight() / 2 ) + getPaddingTop();
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

}
