package com.test.testsip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.util.Log;

public class IncomingCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SipAudioCall incomingCall = null;
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
		Log.d("SIP!", "@IncomingCall : inkommande broadcast: " + intent.getAction().toString());
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    try {
                        call.answerCall(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            MainActivity wtActivity = (MainActivity) context;

            incomingCall = wtActivity.manager.takeAudioCall(intent, listener);
            incomingCall.answerCall(30);
            incomingCall.startAudio();
            incomingCall.setSpeakerMode(true);
            if(incomingCall.isMuted()) {
                incomingCall.toggleMute();
            }

            wtActivity.call = incomingCall;

        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    
	}

}
