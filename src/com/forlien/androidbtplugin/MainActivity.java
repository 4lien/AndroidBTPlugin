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
		//������� ���� ����

		//������� ��Ű ����
		mBLEManager.bluetoothOn();
		//������� ��Ű ��
		//������� ��ĵ ����
		leScan();
		//������� ��ĵ ��
		//GattService ����
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		Log.i(SharedData.TAG, "Main : gattServiceIntent");
		bindService(gattServiceIntent, mBLEManager.mServiceConnection, BIND_AUTO_CREATE);
		Log.i(SharedData.TAG, "Main : bindService");
		//GattService ��
		//mBLEManager.connect();
		//������� ��� ����
		for(int i = 0 ; i < 10000 ; i++){for(int j = 0 ; j < 10000 ;j++){}} // ��� ������ �ֱ� ����
		SharedData.register = register();
	}
	


	public String bluetoothConnection(String address) // address : ������� �ּ�
	{


		
		return SharedData.register;
		//������� ��� ��
		//������� ���� ��
		
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
			Log.i(SharedData.TAG , "���� �õ��� �ٽ� ���ֽñ� �ٸ��ϴ�.");
			return "���� �õ��� �ٽ� ���ֽñ� �ٸ��ϴ�.";
		}
		else{
			Log.i(SharedData.TAG , "��� ���� ���� ����");
			return "��� ���� ���� ����";
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
