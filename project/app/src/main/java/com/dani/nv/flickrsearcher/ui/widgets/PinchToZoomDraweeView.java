package com.dani.nv.flickrsearcher.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * A SimpleDraweeView that supports Pinch to zoom and dragging.
 */
public class PinchToZoomDraweeView extends SimpleDraweeView {
    private final ScaleGestureDetector mScaleDetector;
    private final ScaleGestureDetector.OnScaleGestureListener mScaleListener;
    private final GestureDetector mGestureDetector;
    private float mCurrentScale;
    private final Matrix mCurrentMatrix;
    private float mMidX;
    private float mMidY;
    private Activity parent_view;

    @Nullable
    private OnZoomChangeListener mListener;

    float lastFocusX;
    float lastFocusY;

    public PinchToZoomDraweeView(Context context) {
        this(context, null);
    }

    public PinchToZoomDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinchToZoomDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        parent_view = (Activity) context;
        mScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                lastFocusX = detector.getFocusX();
                lastFocusY = detector.getFocusY();
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Matrix transformationMatrix = new Matrix();
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();

                //Zoom focus is where the fingers are centered,
                transformationMatrix.postTranslate(-focusX, -focusY);

                transformationMatrix.postScale(detector.getScaleFactor(), detector.getScaleFactor());

                //Edited after comment by chochim
                float focusShiftX = focusX - lastFocusX;
                float focusShiftY = focusY - lastFocusY;
                transformationMatrix.postTranslate(focusX + focusShiftX, focusY + focusShiftY);
                mCurrentMatrix.postConcat(transformationMatrix);
                lastFocusX = focusX;
                lastFocusY = focusY;
                invalidate();
                return true;

            }
        };
        mScaleDetector = new ScaleGestureDetector(getContext(), mScaleListener);
        mCurrentMatrix = new Matrix();
        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
    }

    public void setOnZoomChangeListener(OnZoomChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnZoomChangeListener(null);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int saveCount = canvas.save();
        canvas.concat(mCurrentMatrix);
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    /**
     * Resets the zoom on that view
     */
    public void resetZoom() {
        reset();
    }

    /**
     * Resets the zoom of the attached image.
     * This has no effect if the image has been destroyed
     */
    public void reset() {
        mCurrentMatrix.reset();
        mCurrentScale = 1.0f;
        invalidate();
    }

    /**
     * A listener interface for when the zoom scale changes
     */
    public interface OnZoomChangeListener {
        /**
         * Callback method getting triggered when the zoom scale changes.
         * This is not called when the zoom is programmatically reset
         *
         * @param scale the new scale
         */
        public void onZoomChange(float scale);
    }

    public class GestureListener implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        private int verticalMinistance = 20;
        private int minVelocity = 10;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            mCurrentMatrix.postTranslate(-distanceX, -distanceY);
            invalidate();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}