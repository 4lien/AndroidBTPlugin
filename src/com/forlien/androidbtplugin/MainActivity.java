package com.forlien.impression;



import com.unity3d.player.UnityPlayerActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends UnityPlayerActivity {
	
	private BLEManager mBLEManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Log.i(SharedData.TAG, "start");
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
//		serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200
		
		mBLEManager = new BLEManager(this);
		//블루투스 접속 시작

		//블루투스 켜키 시작
		mBLEManager.bluetoothOn();
		//블루투스 켜키 끝
		//블루투스 스캔 시작
		leScan();
		//블루투스 스캔 끝
		//GattService 시작
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		Log.i(SharedData.TAG, "Main : gattServiceIntent");
		bindService(gattServiceIntent, mBLEManager.mServiceConnection, BIND_AUTO_CREATE);
		Log.i(SharedData.TAG, "Main : bindService");
		//GattService 끝
		//mBLEManager.connect();
		//블루투스 등록 시작
		for(int i = 0 ; i < 10000 ; i++){for(int j = 0 ; j < 10000 ;j++){}} // 잠시 딜레이 주기 위함
		SharedData.register = register();
	}
	


	public String bluetoothConnection(String address) // address : 블루투스 주소
	{


		
		return SharedData.register;
		//블루투스 등록 끝
		//블루투스 접속 끝
		
	}
	
	public String bluetoothDataRead()
	{
		Log.i(SharedData.TAG , "SharedData.DATA");
		Log.i(SharedData.TAG , SharedData.DATA + "??");
		return SharedData.DATA;
		
	}
	
	private String register() {
		// TODO Auto-generated method stub
		//Log.i(SharedData.TAG , "reigister");
		if(registerReceiver(mBLEManager.mGattUpdateReceiver, makeGattUpdateIntentFilter()) != null){
			Log.i(SharedData.TAG , "연결 시도를 다시 해주시기 바립니다.");
			return "연결 시도를 다시 해주시기 바립니다.";
		}
		else{
			Log.i(SharedData.TAG , "블루 투스 연결 성공");
			return "블루 투스 연결 성공";
		}
	}

	public void bluetoothOn(){
		mBLEManager.bluetoothOn();
	}
	public void leScan(){
		Log.i(SharedData.TAG, "LESCAN");
		SharedData.BTADAPTER.startLeScan(mBLEManager.leScanCallback);
		Log.i(SharedData.TAG, "LESCANEND");
	}
	
	public void stopScan(){
		Log.i(SharedData.TAG, "LESTOP");
		SharedData.BTADAPTER.stopLeScan(mBLEManager.leScanCallback);
	}
	
	public void connection(){
		Log.i(SharedData.TAG, "Connection Start");
		String deviceName = SharedData.BTDEVICE.getName().toString();
		String deviceAddress = SharedData.BTDEVICE.getAddress().toString();
		if(deviceName.equals("No Device Available") && deviceAddress.equals("No Address Available")){
			Log.i(SharedData.TAG, "Connect fail");
		}else{
			
		}
		Log.i(SharedData.TAG, "Connected");
	}
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}	
}
