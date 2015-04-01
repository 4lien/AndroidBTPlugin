package com.forlien.androidbtplugin;

import android.content.Intent;
import android.os.Bundle;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ForBtActivity extends UnityPlayerActivity {

	static final String TAG = "UnityBTPlugin";
	
	
	
	private static final int REQUEST_ENABLE_BT = 2;

	private BluetoothService btService = null;
	
	
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	};

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.d(TAG, "onActivityResult " + resultCode);
        
			switch (requestCode) {

				case REQUEST_ENABLE_BT:
					// When the request to enable Bluetooth returns
					if (resultCode == UnityPlayerActivity.RESULT_OK) {
            	
					} else {

						Log.d(TAG, "Bluetooth is not enabled");
					}
					break;
			}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// BluetoothService °´Ã¼ »ý¼º
		if(btService == null) {
			btService = new BluetoothService(this, mHandler);
		}
	}
	
	String GetData() //GetPluginNum
	{
		return BluetoothService.tBlue.read();

	}
	
	void PluginToUnitySendMessage() //PluginToUnitySendMessage 
	{
		


		UnityPlayer.UnitySendMessage( "BTSetting"
				, "bluetoothScanning"
				,  btService.getDeviceState() + btService.enableBluetooth());
	}
	
	
}
