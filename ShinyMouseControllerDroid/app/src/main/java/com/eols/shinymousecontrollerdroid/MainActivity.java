package com.eols.shinymousecontrollerdroid;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.eols.shinymousecontrollerdroid.interfaces.JoystickListener;
import com.eols.shinymousecontrollerdroid.views.Joystick;

public class MainActivity extends AppCompatActivity implements JoystickListener {

    private Joystick joystickView;
    private TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.joystickView = (Joystick)findViewById(R.id.joystick);
        this.joystickView.addListener(this);

        this.outputView = (TextView)findViewById(R.id.output_txt);
    }

    @Override
    public void onTouch(float magnitude, PointF angleVector) {
        this.outputView.setText("Magnitude: " + magnitude + " AngleVec: " + angleVector.toString());
    }
}
