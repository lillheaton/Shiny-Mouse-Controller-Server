package com.eols.shinymousecontrollerdroid.interfaces;

import android.graphics.PointF;

/**
 * Created by emiols on 2016-03-19.
 */
public interface JoystickListener {
    void onTouch(float magnitude, PointF angleVector);
}
