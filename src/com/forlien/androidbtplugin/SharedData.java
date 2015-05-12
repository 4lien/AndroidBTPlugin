package com.forlien.impression;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;

public class SharedData {
	public static final int REQUEST_ENABLE_BT = 1;
	public static final String TAG = "SejinBLEPlugin";
	public static BluetoothManager BTMANAGER;
	public static BluetoothAdapter BTADAPTER;
	public static BluetoothDevice BTDEVICE;
	public static int RSSI;
	public static byte[] SCANRECORD;
	public static BluetoothGatt BTGATT;
	public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public static String DATA;
	public static String register;
	
}
