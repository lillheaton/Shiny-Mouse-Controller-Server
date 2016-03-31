package com.eols.shinymousecontrollerdroid.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.eols.shinymousecontrollerdroid.R;
import com.eols.shinymousecontrollerdroid.interfaces.JoystickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emiols on 2016-03-18.
 */
public class Joystick extends View {

    private List<JoystickListener> listeners = new ArrayList<>();

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

    public void addListener(JoystickListener listener){
        this.listeners.add(listener);
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
        this.outerCirlcePaint.setPathEffect(new DashPathEffect(new float[] { 10f, 20f }, 0f));
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
                new float[]{
                        0, centerH, this.w, centerH,
                        centerW, 0, centerW, this.h
                },
                outerCirlcePaint);

        float arrowSize = 10f;
        drawArrow(canvas, new PointF(0, centerH), arrowSize, (byte) 0); // left
        drawArrow(canvas, new PointF(this.w, centerH), arrowSize, (byte) 2); // right
        drawArrow(canvas, new PointF(centerW, 0), arrowSize, (byte) 1);
        drawArrow(canvas, new PointF(centerW, this.h), arrowSize, (byte) 3);
    }

    private void drawInnerCircle(Canvas canvas) {
        RectF rect = new RectF(
                this.joystickPoint.x - innerRadius,
                this.joystickPoint.y - innerRadius,
                this.joystickPoint.x + innerRadius,
                this.joystickPoint.y + innerRadius);

        canvas.drawArc(rect, 0, 360, false, innerCirclePaint);
    }

    private void drawArrow(Canvas canvas, PointF point, float size, byte direction){
        PointF p1 = point, p2 = new PointF(), p3 = new PointF();

        switch (direction){
            case 0: // left
                p2.set(point.x + size, point.y - size);
                p3.set(point.x + size, point.y + size);
                break;

            case 1: // top
                p2.set(point.x + size, point.y + size);
                p3.set(point.x - size, point.y + size);
                break;

            case 2: // right
                p2.set(point.x - size, point.y - size);
                p3.set(point.x - size, point.y + size);
                break;

            case 3: // down
                p2.set(point.x - size, point.y - size);
                p3.set(point.x + size, point.y - size);
                break;
        }

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1.x, p1.y);
        //path.lineTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();

        canvas.drawPath(path, innerCirclePaint);
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

        float magnitude = 0;
        PointF angleVector = new PointF(0, 0);

        if(dragging){

            float xDist = event.getX() - centerW;
            float yDist = event.getY() - centerH;
            double dist = Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0));

            float angle = (float)Math.atan2(yDist, xDist);
            angleVector.set((float)Math.cos(angle), (float)Math.sin(angle));
            magnitude = (float)dist;

            if(dist > joystickRadius){
                float fraction = joystickRadius / (float)dist;
                this.joystickPoint.set((event.getX() - centerW) * fraction + centerW, (event.getY() - centerH) * fraction + centerW);
            }
            else {
                this.joystickPoint.set(event.getX(), event.getY());
            }

        } else {
            this.joystickPoint.set(centerW, centerH);
        }

        this.invalidate();

        // Call the listeners
        for (JoystickListener jl : listeners)
            jl.onJoystickTouch(magnitude, angleVector);

        return true;
    }
}
