package com.bluehomestudio.luckywheel;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mohamed on 22/04/17.
 */

final class WheelView extends View {
    private RectF range = new RectF();
    private Paint archPaint, textPaint;
    private int textSize = 36;
    private int textColor = 0xffffffff;
    private int padding, radius, center, mWheelBackground, mImagePadding;
    private List<WheelItem> mWheelItems;
    private int spinTime ;
    private OnLuckyWheelReachTheTarget mOnLuckyWheelReachTheTarget;
    private OnRotationListener onRotationListener;

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initComponents() {
        //arc paint object
        archPaint = new Paint();
        archPaint.setAntiAlias(true);
        archPaint.setDither(true);
        //text paint object
        textPaint = new Paint();
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.be_vietnam_pro);
        textPaint.setTypeface(typeface);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        spinTime = 9000;
        textPaint.setDither(true);
        textPaint.setTextSize(textSize);
        //rect rang of the arc
        range = new RectF(padding, padding, padding + radius, padding + radius);
    }
    public void setItemsColor(List<Integer> listColor)
    {
        int indexColor = 0;
        for(int i = 0 ; i < mWheelItems.size(); i++)
        {
            mWheelItems.get(i).setColor(listColor.get(indexColor));
            if(indexColor < listColor.size() -1)
            {
                indexColor += 1;
            }
            else
            {
                indexColor = 0;
            }
        }
    }
    public void setSpinTime(int time)//time is second
    {
        this.spinTime = time*1000; // spintime is milisecond
    }
    public void setTextSize(int size)
    {
        this.textSize = size;
        invalidate();
    }
    public void setTextColor(int color)
    {
        this.textColor = color;
        invalidate();
    }
    public int getTextSize(){
        return (int) this.textPaint.getTextSize();
    }
    public int getTextColor(){
        return (int) this.textColor;
    }
    public void setSliceRepeat(int num)
    {
        List<WheelItem> list = new ArrayList<WheelItem>();
        list.addAll(mWheelItems);
        mWheelItems.clear();
        for(int i = 0 ; i < num ; i++)
        {
           mWheelItems.addAll(list);
        }
    }
    /**
     * Get the angele of the target
     *
     * @return Number of angle
     */
    private float getAngleOfIndexTarget(int target) {
        return ((float)(360 / mWheelItems.size())) * target;
    }

    /**
     * Function to set wheel background
     *
     * @param wheelBackground Wheel background color
     */
    public void setWheelBackgoundWheel(int wheelBackground) {
        mWheelBackground = wheelBackground;
        invalidate();
    }

    public void setItemsImagePadding(int imagePadding) {
        mImagePadding = imagePadding;
        invalidate();
    }

    /**
     * Function to set wheel listener
     *
     * @param onLuckyWheelReachTheTarget target reach listener
     */
    public void setWheelListener(OnLuckyWheelReachTheTarget onLuckyWheelReachTheTarget) {
        mOnLuckyWheelReachTheTarget = onLuckyWheelReachTheTarget;
    }

    /**
     * Function to add wheels items
     *
     * @param wheelItems Wheels model item
     */
    public void addWheelItems(List<WheelItem> wheelItems) {
        mWheelItems = wheelItems;
        invalidate();
    }

    /**
     * Function to draw wheel background
     *
     * @param canvas Canvas of draw
     */
    private void drawWheelBackground(Canvas canvas) {
        Paint backgroundPainter = new Paint();
        backgroundPainter.setAntiAlias(true);
        backgroundPainter.setDither(true);
        backgroundPainter.setColor(mWheelBackground);
        canvas.drawCircle(center, center, center, backgroundPainter);
    }

    /**
     * Function to draw image in the center of arc
     *
     * @param canvas    Canvas to draw
     * @param tempAngle Temporary angle
     * @param bitmap    Bitmap to draw
     */
    private void drawImage(Canvas canvas, float tempAngle, Bitmap bitmap) {
        //get every arc img width and angle
        int imgWidth = (radius / mWheelItems.size()) - mImagePadding;
        float angle = (float) ((tempAngle + 360 / (float)(mWheelItems.size()) / 2) * Math.PI / 180);
        //calculate x and y
        int x = (int) (center + radius / 2 / 2 * Math.cos(angle));
        int y = (int) (center + radius / 2 / 2 * Math.sin(angle));
        //create arc to draw
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        //rotate main bitmap
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(tempAngle + 120);
        matrix.postTranslate(px, py);
        canvas.drawBitmap(bitmap, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG ));
        Log.d("sadsdsddssd" , bitmap.getWidth() + " : "+bitmap.getHeight());
        matrix.reset();
    }


    /**
     * Function to draw text below image
     *
     * @param canvas     Canvas to draw
     * @param tempAngle  Temporary angle
     * @param sweepAngle current index angle
     * @param text       string to show
     */
    private void drawText(Canvas canvas, float tempAngle, float sweepAngle, String text, int textColor) {
        Path path = new Path();
        path.addArc(range, tempAngle, sweepAngle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(0.15f);
        }
        textPaint.setColor(textColor);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(textSize);
        float textWidth = textPaint.measureText(text);
        int hOffset = (int) (radius * Math.PI / mWheelItems.size() / 2 - textWidth / 2);
        int vOffset = (radius / 2 / 3) - 3;
        canvas.drawTextOnPath(text, path, hOffset, vOffset, textPaint);
    }

    /**
     * Function to rotate wheel to target
     *
     * @param target target number
     */
    public void rotateWheelToTarget(int target) {

        final float wheelItemCenter = 270 - getAngleOfIndexTarget(target)+ (360 / mWheelItems.size()) / 2;
        int DEFAULT_ROTATION_TIME = spinTime;
        Random random = new Random();
        final int num = random.nextInt(360);
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(DEFAULT_ROTATION_TIME)
                .rotation((360 * 20)  + wheelItemCenter )
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mOnLuckyWheelReachTheTarget != null) {
                            mOnLuckyWheelReachTheTarget.onReachTarget();
                        }
                        if (onRotationListener != null) {
                            onRotationListener.onFinishRotation();
                        }
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    /**
     * Function to rotate to zero angle
     *
     * @param target target to reach
     */
    public void resetRotationLocationToZeroAngle(final int target) {
        animate().setDuration(0)
                .rotation(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateWheelToTarget(target);
                clearAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWheelBackground(canvas);
        initComponents();
        float totalSweep = 360;
        float tempAngle = 0;
        float sweepAngle = totalSweep / mWheelItems.size();
        for (int i = 0; i < mWheelItems.size(); i++) {
            archPaint.setColor(mWheelItems.get(i).color);
            canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint);
            //drawImage(canvas, tempAngle, mWheelItems.get(i).bitmap.getBitmap());
            drawText(canvas, tempAngle, sweepAngle, mWheelItems.get(i).text == null ? "" : mWheelItems.get(i).text, mWheelItems.get(i).textColor);
            tempAngle += sweepAngle;

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int DEFAULT_PADDING = 5;
        padding = getPaddingLeft() == 0 ? DEFAULT_PADDING : (int) (getPaddingLeft() * 0.8);
        radius = width - padding * 2;
        center = width / 2;
        setMeasuredDimension(width, width);
    }

    public void setOnRotationListener(OnRotationListener onRotationListener) {
        this.onRotationListener = onRotationListener;
    }

    public int getSpinTime() {
        return spinTime;
    }
}
