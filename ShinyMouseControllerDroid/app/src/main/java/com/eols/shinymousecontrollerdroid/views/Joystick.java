package com.eols.shinymousecontrollerdroid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.eols.shinymousecontrollerdroid.R;

/**
 * Created by emiols on 2016-03-18.
 */
public class Joystick extends View {

    private float outerCircleStrokeWidth;
    private float joystickSize;
    private int joystickColor;

    private Paint outerCirlcePaint;
    private Paint innerCirclePaint;
    private int w, h;
    private float centerW, centerH, innerW, innerH, innerRadius, joystickRadius;
    private PointF joystickPoint;
    private boolean dragging = false;

    public Joystick(Context context) {
        super(context);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Joystick, 0, 0);

        try {
            this.outerCircleStrokeWidth = a.getDimensionPixelSize(R.styleable.Joystick_outerRingStrokeSize, 10);
            this.joystickSize = a.getFloat(R.styleable.Joystick_joystickSize, 0.4f);
            this.joystickColor = a.getColor(R.styleable.Joystick_joystickColor, Color.BLACK);
        } finally {
            a.recycle();
        }

        this.outerCirlcePaint = new Paint();
        this.outerCirlcePaint.setAntiAlias(true);
        this.outerCirlcePaint.setStyle(Paint.Style.STROKE);
        this.outerCirlcePaint.setStrokeWidth(outerCircleStrokeWidth);
        this.outerCirlcePaint.setColor(Color.parseColor("#000000"));

        this.innerCirclePaint = new Paint();
        this.innerCirclePaint.setAntiAlias(true);
        this.innerCirclePaint.setStyle(Paint.Style.FILL);
        this.innerCirclePaint.setColor(this.joystickColor);
    }

    private void drawOuterCircle(Canvas canvas) {
        RectF rect = new RectF(
                outerCircleStrokeWidth,
                outerCircleStrokeWidth,
                this.w - outerCircleStrokeWidth,
                this.h - outerCircleStrokeWidth);

        canvas.drawArc(rect, 0f, 360f, false, outerCirlcePaint);

        canvas.drawLines(
                new float[] {
                        0, centerH, this.w, centerH,
                        centerW, 0, centerW, this.h
                },
                outerCirlcePaint);
    }

    private void drawInnerCircle(Canvas canvas) {
        RectF rect = new RectF(
                this.joystickPoint.x - innerRadius,
                this.joystickPoint.y - innerRadius,
                this.joystickPoint.x + innerRadius,
                this.joystickPoint.y + innerRadius);

        canvas.drawArc(rect, 0, 360, false, innerCirclePaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.drawOuterCircle(canvas);
        this.drawInnerCircle(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.centerW = w / 2f;
        this.centerH = h / 2f;
        this.joystickPoint = new PointF(centerW, centerH);
        this.innerW = this.w * this.joystickSize;
        this.innerH = this.h * this.joystickSize;
        this.innerRadius = innerW / 2f;
        this.joystickRadius = centerW - innerRadius;


        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN)
            dragging = true;
        else if(event.getAction() == MotionEvent.ACTION_UP){
            dragging = false;
        }

        if(dragging){

            float xDist = event.getX() - centerW;
            float yDist = event.getY() - centerH;
            double dist = Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0));

            if(dist > joystickRadius){
                float fraction = joystickRadius / (float)dist;
                this.joystickPoint.set((event.getX() - centerW) * fraction + centerW, (event.getY() - centerH) * fraction + centerW);
            }
            else {
                this.joystickPoint.set(event.getX(), event.getY());
            }

            this.invalidate();
        } else {
            this.joystickPoint.set(centerW, centerH);
        }

        return true;
    }
}
