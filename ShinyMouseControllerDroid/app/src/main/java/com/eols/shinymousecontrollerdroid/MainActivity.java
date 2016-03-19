package com.eols.shinymousecontrollerdroid;

import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.eols.shinymousecontrollerdroid.interfaces.JoystickListener;
import com.eols.shinymousecontrollerdroid.interfaces.TCPListener;
import com.eols.shinymousecontrollerdroid.utils.TCPClient;
import com.eols.shinymousecontrollerdroid.views.Joystick;

public class MainActivity extends AppCompatActivity implements JoystickListener {

    private Joystick joystickView;
    private TextView outputView;

    private TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.joystickView = (Joystick)findViewById(R.id.joystick);
        this.joystickView.addListener(this);

        this.outputView = (TextView)findViewById(R.id.output_txt);

        // Start connect
        new connectTask().execute("");
    }

    @Override
    public void onJoystickTouch(float magnitude, PointF angleVector) {
        this.outputView.setText("Magnitude: " + magnitude + " AngleVec: " + angleVector.toString());

        if(tcpClient != null){
            tcpClient.sendMessage(
                    String.format("{ " +
                            "\"type\": \"joystick\", " +
                            "\"magnitude\": %1$.5f, " +
                            "\"vector\": { " +
                                "\"x\": %2$.5f, " +
                                "\"y\": %3$.5f " +
                            "} }", magnitude, angleVector.x, angleVector.y));
        }
    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... params) {
            tcpClient = new TCPClient(new TCPListener() {
                @Override
                public void onMessageReceived(String message) {
                    publishProgress(message);
                }
            }, "10.0.0.34", 1337);
            tcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            // Do whatever with the recieved values
        }
    }
}
