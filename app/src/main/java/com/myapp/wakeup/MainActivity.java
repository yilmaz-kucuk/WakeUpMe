package com.myapp.wakeup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnWakeup;
    private Button btnCancel;
    private TextView textWakeNumber;
    private EditText wakeNumber;
    private TextView textLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int PERMISSIONS_REQUEST_CODE = 1;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.WRITE_CALL_LOG},
                PERMISSIONS_REQUEST_CODE);

        IncomingCall incomingCall = new IncomingCall(getApplicationContext(), handler);
        incomingCall.onReceive(getApplicationContext(), getIntent());

        btnWakeup = (Button)findViewById(R.id.buttonWakeUp);
        btnCancel = (Button)findViewById(R.id.buttonCancel);
        btnCancel.setBackgroundColor(Color.RED);
        textWakeNumber = (TextView)findViewById(R.id.wakeNumber);
        wakeNumber = (EditText)findViewById(R.id.text_numInput);
        textLog = (TextView)findViewById(R.id.textLogs);

        btnWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWakeNumber.setText(wakeNumber.getText().toString().trim());
                incomingCall.number = wakeNumber.getText().toString().trim();
                incomingCall.number2 = "+9"+wakeNumber.getText().toString().trim();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textWakeNumber.setText("");
                incomingCall.number = null;
                incomingCall.number2 = null;
            }
        });
    }

    public void updateLog(String logText){
        textLog.setText((textLog.getText().toString().equals("")?"":textLog.getText().toString()+"\r\n") + logText);
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IncomingCall.RECEIVED_CALL:
                    updateLog(msg.obj.toString());
                    break;
            }
        }
    };
}