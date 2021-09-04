package com.myapp.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCall extends BroadcastReceiver {
public String number = "05";
public String number2 = "+90";
private Context mContext = null;
private AudioManager audioManager;
private Handler mHandler;

public static final int RECEIVED_CALL = 99;

private static final int INCOMING_CALL_STARTED = 1;
private static final int INCOMING_CALL_ENDED   = 0;

public  IncomingCall(Context mContext, Handler handler){
this.mContext = mContext;
this.mHandler = handler;
audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
}
    public void onReceive(Context context, Intent intent) {
        try {
            // TELEPHONY MANAGER class object to register one listner
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            //Create Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("MyPhoneListener",state+"   incoming no:" + incomingNumber);
            if (state == INCOMING_CALL_STARTED) {
                mHandler.obtainMessage(RECEIVED_CALL, (Object)incomingNumber).sendToTarget();
                if(incomingNumber.trim().equals(number) || incomingNumber.trim().equals(number2)){
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        }
    }

}