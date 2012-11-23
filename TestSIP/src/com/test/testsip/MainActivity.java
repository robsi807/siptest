package com.test.testsip;

import java.text.ParseException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public SipManager manager = null;
	public SipProfile me = null;
	public SipAudioCall call = null;
	public IncomingCallReceiver callReceiver;

	public final String DOMAIN = "iptel.org";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.SipDemo.INCOMING_CALL"); // <------------varför
															// just denna
															// action?
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);

		initializeManager();

	}

	private void initializeManager() {
		Log.d("SIP_TEST", "Init manager!");
		if (manager == null) {
			manager = SipManager.newInstance(this);

			if (manager != null) {
				Toast t = Toast.makeText(this, "manager new instance",
						Toast.LENGTH_SHORT);
				t.show();
			}
		}

	}

	private void initializeLocalProfile() {
		if (manager == null) {
			Log.d("SIP_TEST", "manager = null, returned");
			Toast t = Toast.makeText(this,
					"SIP_TEST: manager = null, returned", Toast.LENGTH_SHORT);
			t.show();
			return;
		}

		if (me != null) {
			closeLocalProfile();
		}

		try {
			SipProfile.Builder builder = new SipProfile.Builder(
					((EditText) findViewById(R.id.editUsername)).getText()
							.toString(), DOMAIN);
			Toast t = Toast.makeText(this,
					"SIP_TEST: SipProfile byggd", Toast.LENGTH_SHORT);
			t.show();
			builder.setPassword(((EditText) findViewById(R.id.editPassword))
					.getText().toString());
			me = builder.build();

			
			t = Toast.makeText(this,
					"SIP_TEST: innan intent", Toast.LENGTH_SHORT);
			t.show();
			Intent i = new Intent();
			i.setAction("android.SipDemo.INCOMING_CALL");
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
					Intent.FILL_IN_DATA);

			manager.open(me, pi, null);
			
			
			Log.d("SIP_TEST", "open for calls!");

			// This listener must be added AFTER manager.open is called,
			// Otherwise the methods aren't guaranteed to fire.

			manager.setRegistrationListener(me.getUriString(),
					new SipRegistrationListener() {
						public void onRegistering(String localProfileUri) {
							Log.d("SIP_TEST", "Registering with SIP Server...");
						}

						public void onRegistrationDone(String localProfileUri,
								long expiryTime) {
							Log.d("SIP_TEST", "Ready");
							Log.d("SIP_TEST", "Ready");
							Log.d("SIP_TEST", "Ready");
							Log.d("SIP_TEST", "Ready");
							
							
						}

						public void onRegistrationFailed(
								String localProfileUri, int errorCode,
								String errorMessage) {
							Log.d("SIP_TEST",
									"Registration failed.  Please check settings.");
						}
					});
		} catch (ParseException pe) {
			Log.d("SIP_TEST", "Connection Error.");
		} catch (SipException se) {
			Log.d("SIP_TEST", "Connection Error.");
		}
	}

	public void closeLocalProfile() {
		if (manager == null) {
			return;
		}
		try {
			if (me != null) {
				manager.close(me.getUriString());
			}
		} catch (Exception ee) {
			Log.d("WalkieTalkieActivity/onDestroy",
					"Failed to close local profile.", ee);
		}
	}

	public void btnRegisterSipClicked(View v) {
		initializeLocalProfile();
	}

	public void btnCallClicked(View v) {
		Toast t = Toast.makeText(this, "inne i try för btnCallClicked", Toast.LENGTH_SHORT);
		t.show();
		try {
			
			
			SipAudioCall.Listener listener = new SipAudioCall.Listener() {
				// Much of the client's interaction with the SIP Stack will
				// happen via listeners. Even making an outgoing call, don't
				// forget to set up a listener to set things up once the call is
				// established.
				@Override
				public void onCallEstablished(SipAudioCall call) {
					call.startAudio();
					call.setSpeakerMode(true);
				}

				@Override
				public void onCallEnded(SipAudioCall call) {
					
				}
			};
			t = Toast.makeText(this, "innan call", Toast.LENGTH_SHORT);
			t.show();
			call = manager.makeAudioCall(me.getUriString(),
					"sip:raddningstjanst2@iptel.org", listener, 30);

		} catch (Exception e) {
			Log.i("WalkieTalkieActivity/InitiateCall",
					"Error when trying to close manager.", e);
			if (me != null) {
				try {
					manager.close(me.getUriString());
				} catch (Exception ee) {
					Log.i("WalkieTalkieActivity/InitiateCall",
							"Error when trying to close manager.", ee);
					ee.printStackTrace();
				}
			}
			if (call != null) {
				call.close();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
