package com.eols.shinymousecontrollerdroid;

import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eols.shinymousecontrollerdroid.interfaces.JoystickListener;
import com.eols.shinymousecontrollerdroid.interfaces.TCPListener;
import com.eols.shinymousecontrollerdroid.utils.TCPClient;
import com.eols.shinymousecontrollerdroid.views.Joystick;

public class MainActivity extends AppCompatActivity implements JoystickListener, View.OnClickListener {

    private Joystick joystickView;
    private TextView outputView;
    private Button leftBtn;
    private Button rightBtn;

    private String connectIpAddress;
    private TCPClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.joystickView = (Joystick)findViewById(R.id.joystick);
        this.joystickView.addListener(this);

        this.leftBtn = (Button)findViewById(R.id.left_click_btn);
        this.rightBtn = (Button)findViewById(R.id.right_click_btn);

        this.leftBtn.setOnClickListener(this);
        this.rightBtn.setOnClickListener(this);

        this.outputView = (TextView)findViewById(R.id.output_txt);

        this.connectIpAddress = getIntent().getStringExtra(ConnectActivity.EXTRA_IP_ADDRESS);

        // Start connect
        new connectTask().execute("");
    }

    private void sendButtonClick(String button){
        if(tcpClient != null){
            tcpClient.sendMessage(String.format(
                    "{" +
                        "\"type\": \"click\"," +
                        "\"button\": \"%s\"" +
                    "}", button
            ));
        }
    }



    @Override
    public void onJoystickTouch(float magnitude, PointF angleVector) {
        this.outputView.setText("Magnitude: " + magnitude + "\nAngleVec: " + angleVector.toString());

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

    @Override
    public void onClick(View v) {
        if(v == this.leftBtn){
            this.sendButtonClick("left");
        } else if(v == this.rightBtn){
            this.sendButtonClick("right");
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
            }, connectIpAddress, 1337);
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
