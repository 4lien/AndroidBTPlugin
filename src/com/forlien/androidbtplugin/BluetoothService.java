package com.forlien.androidbtplugin;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
//Connect 함수 처리 단계
public class BluetoothService {
	// Debugging
	private static final String TAG = "BluetoothService";
	static String enableTest="";
	// Intent request code
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	// RFCOMM Protocol
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	static TBlue tBlue;
	private BluetoothAdapter btAdapter;
	private ConnectThread mConnectThread;
	public static String LOG = "";
	
	private Activity mActivity;
	private Handler mHandler;
	
	// Constructors
	public BluetoothService(Activity ac, Handler h) {
		mActivity = ac;
		mHandler = h;
		
		// BluetoothAdapter 얻기
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	/**
	 * Check the Bluetooth support
	 * @return boolean
	 */
	public String getDeviceState() {
		Log.i(TAG, "Check the Bluetooth support");
		
		
		if(btAdapter == null) {
			Log.d(TAG, "Bluetooth is not available");
			
			return "Bluetooth is not available";
			
		}else{
			return "Bluetooth is available";
		}
		
	}
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		public ConnectThread(BluetoothDevice device) {
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;
		// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
		// MY_UUID is the app's UUID string, also used by the server code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) { }
				mmSocket = tmp;
			}

		public void run() {
			// Cancel discovery because it will slow down the connection
			btAdapter.cancelDiscovery();
			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) { }
				return;
			}
			// Do work to manage the connection (in a separate thread)
			if(mmSocket.isConnected()){
				BluetoothService.enableTest = "Connected";
			}else{
				BluetoothService.enableTest = "Connected failed";
			}
		}
		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}
	/**
	 * Check the enabled Bluetooth
	 */
	public String enableBluetooth() {
		Log.i(TAG, "Check the enabled Bluetooth");
		
		enableTest = "??:::??";
		
		if(btAdapter.isEnabled() == true){

			Log.d(TAG, "Bluetooth Enable Now");
			

			Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
			if (pairedDevices.size() != 0) {
				//enableTest = btAdapter.getAddress();
				if(!"".equals(enableTest)){
					
					tBlue = new TBlue("98:D3:31:40:08:84");
					
				}
			}else{
				enableTest = "Not Paired";
			}
		}else{

			Log.d(TAG, "Bluetooth Enable Request");
			enableTest = "Bluetooth Enable Request";
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
		}
		
		return enableTest;
	}


}
