package com.forlien.impression;

import java.util.ArrayList;
import java.util.List;







import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class BLEManager extends UnityPlayerActivity{
	private BluetoothLeService mBluetoothLeService;
	Activity mainActivity;
	private boolean mConnected;
	 // Code to manage Service lifecycle.
	private static BluetoothGattCharacteristic mSCharacteristic, mModelNumberCharacteristic, mSerialPortCharacteristic, mCommandCharacteristic;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";
	public static final String CommandUUID="0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID="00002a24-0000-1000-8000-00805f9b34fb";
	public final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.e(SharedData.TAG, "BLEManager Init");
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(SharedData.TAG, "Unable to initialize Bluetooth");
				finish();
			}
			mBluetoothLeService.connect(SharedData.BTDEVICE.getAddress());
			Log.i(SharedData.TAG, "connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mBluetoothLeService = null;			
		}
      
    };
	public BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
	    @Override
	    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
	        // your implementation here
			Log.i(SharedData.TAG, "device : "  + device.getName());
			Log.i(SharedData.TAG, "rssi : "  + rssi);
			Log.i(SharedData.TAG, "scanRecord : "  + scanRecord.toString());
			
			if(device.getName().equals("Bluno")){
				SharedData.BTADAPTER.stopLeScan(leScanCallback);
				SharedData.BTDEVICE = device;
				SharedData.RSSI = rssi;
				SharedData.SCANRECORD = scanRecord;
				
			}
	    }
	};
	public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.i(SharedData.TAG, "Receiving1" + action);
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				Log.i(SharedData.TAG, "ACTION_GATT_CONNECTED2");				
				mConnected = true;
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				Log.i(SharedData.TAG, "ACTION_GATT_DISCONNECTED3");
				mConnected = false;
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            	if(mSCharacteristic==mModelNumberCharacteristic)
            	{
            		if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().startsWith("DF BLUNO")) {
						mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
						mSCharacteristic=mCommandCharacteristic;
						mSCharacteristic=mSerialPortCharacteristic;
						mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);						
					}
            		else {
          
					}
            	}            	
            	else if (mSCharacteristic==mSerialPortCharacteristic) {
            		
				}
            	
            	if(BluetoothLeService.EXTRA_DATA.length() <= 6 ){
            		
            		SharedData.DATA = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            		//Log.i(SharedData.TAG," if ::::::::: " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));


            	}else{
            		SharedData.DATA = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            		//Log.i(SharedData.TAG," else ::::::::: " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            				
            				
            	}
            	

            	
			}else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            	for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
            		System.out.println("ACTION_GATT_SERVICES_DISCOVERED  "+
            				gattService.getUuid().toString());
            	}
            	getGattServices(mBluetoothLeService.getSupportedGattServices());
			}
			
		}
	};
	public BLEManager(Activity mainActivity) {
		// TODO Auto-generated constructor stub		
		this.mainActivity = mainActivity;
		SharedData.BTMANAGER = (BluetoothManager)(mainActivity.getSystemService(Context.BLUETOOTH_SERVICE));		 
	}
	

	public void bluetoothOn(){
		
		SharedData.BTADAPTER = SharedData.BTMANAGER.getAdapter();
		if (SharedData.BTADAPTER != null && !SharedData.BTADAPTER.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);			
			mainActivity.startActivityForResult(enableIntent , SharedData.REQUEST_ENABLE_BT);			
		}
	}	
	public boolean connect() {
		Log.i(SharedData.TAG , "connecting");
		Log.i(SharedData.TAG , ""+(SharedData.BTDEVICE .getAddress()));
		if(mBluetoothLeService != null){
			mConnected =  mBluetoothLeService.connect(SharedData.BTDEVICE.getAddress());
			return mConnected;
		}else{
			Log.i(SharedData.TAG , "mBluetoothLeService == null;"+(SharedData.BTDEVICE .getAddress()));
			return false;
		}
		
	}
	
    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        mModelNumberCharacteristic=null;
        mSerialPortCharacteristic=null;
        mCommandCharacteristic=null;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            System.out.println("displayGattServices + uuid="+uuid);
            
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                if(uuid.equals(ModelNumberStringUUID)){
                	mModelNumberCharacteristic=gattCharacteristic;
                	System.out.println("mModelNumberCharacteristic  "+mModelNumberCharacteristic.getUuid().toString());
                }
                else if(uuid.equals(SerialPortUUID)){
                	mSerialPortCharacteristic = gattCharacteristic;
                	System.out.println("mSerialPortCharacteristic  "+mSerialPortCharacteristic.getUuid().toString());
//                    updateConnectionState(R.string.comm_establish);
                }
                else if(uuid.equals(CommandUUID)){
                	mCommandCharacteristic = gattCharacteristic;
                	System.out.println("mSerialPortCharacteristic  "+mSerialPortCharacteristic.getUuid().toString());
//                    updateConnectionState(R.string.comm_establish);
                }
            }
            mGattCharacteristics.add(charas);
        }
        
        if (mModelNumberCharacteristic==null || mSerialPortCharacteristic==null || mCommandCharacteristic==null) {
			
            Log.i(SharedData.TAG,"fail");
		}
        else {
        	mSCharacteristic=mModelNumberCharacteristic;
        	mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
        	mBluetoothLeService.readCharacteristic(mSCharacteristic);
		}
        
    }
}
