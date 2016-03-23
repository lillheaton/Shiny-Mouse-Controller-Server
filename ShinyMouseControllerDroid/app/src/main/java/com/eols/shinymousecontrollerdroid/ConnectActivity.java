package com.eols.shinymousecontrollerdroid;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.regex.Pattern;

/**
 * Created by emiols on 2016-03-21.
 */
public class ConnectActivity extends AppCompatActivity {

    public static final String EXTRA_IP_ADDRESS = "EXTRA_IP_ADDRESS";
    private static final Pattern ipRegex = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private EditText ipAddressInput;
    private TextView outputView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        this.ipAddressInput = (EditText)findViewById(R.id.ip_address_input);
        String currentIp = getIpAddress();
        this.ipAddressInput.setText(currentIp.substring(0, currentIp.lastIndexOf(".")));

        this.outputView = (TextView)findViewById(R.id.connect_output_txt);

        // On click connect
        findViewById(R.id.connect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipRegex.matcher(ipAddressInput.getText()).matches()){
                    Intent mainIntent = new Intent(ConnectActivity.this, MainActivity.class);
                    mainIntent.putExtra(EXTRA_IP_ADDRESS, ipAddressInput.getText().toString());
                    startActivity(mainIntent);
                } else {
                    outputView.setText("Bad format on IP address");
                }
            }
        });
    }

    private String getIpAddress(){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // https://sv.wikipedia.org/wiki/Endian
        if(ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)){
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try{
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        }catch (UnknownHostException e){
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }
}
