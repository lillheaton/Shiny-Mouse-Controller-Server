package com.eols.shinymousecontrollerdroid.interfaces;

import android.graphics.PointF;

/**
 * Created by emiols on 2016-03-19.
 */
public interface JoystickListener {
    void onJoystickTouch(float magnitude, PointF angleVector);
}
