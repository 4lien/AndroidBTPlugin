package com.forlien.androidbtplugin;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class TBlue {
	String address = null;
	String TAG = "tBlue";
	BluetoothAdapter localAdapter = null;
	BluetoothDevice remoteDevice = null;
	BluetoothSocket socket = null;
	public OutputStream outStream = null;
	public InputStream inStream = null;
	boolean failed = false;
	private String log=""; 
	
	public TBlue(String address){
		this.address = address.toUpperCase();
		localAdapter = BluetoothAdapter.getDefaultAdapter();
		if((localAdapter != null) && localAdapter.isEnabled()){
			Log.i(TAG, "enable");			
		}else{
			Log.e(TAG, "noe enable");
			return;
		}
		log = "Constructure";
		log = connect();
		if(!"".equals(BluetoothService.LOG))
			BluetoothService.LOG = log;
	}

	public String connect() {
		// TODO Auto-generated method stub
		Log.i(TAG, "connecting to" + address);
		
		try{
			remoteDevice = localAdapter.getRemoteDevice(address);
		}catch(IllegalArgumentException e){
			Log.e(TAG, "failed" + e.toString());
			return "Method connect failed";			
		}
		
		Log.i(TAG, "RFCOMM");
		try{
			Method m = remoteDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
					
			socket = (BluetoothSocket)m.invoke(remoteDevice, 1);
			Log.i(TAG,"RFCOMM socket created.");
			
			}catch (NoSuchMethodException e){
				return "Method connect failed";
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				return "Method connect failed";
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				return "Method connect failed";
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				return "Method connect failed";
			}
			localAdapter.cancelDiscovery();
		
			Log.i(TAG, "connecting socket");
			
			try{
				socket.connect();
				Log.i(TAG, "connected");
			}catch(IOException e){
				
				try{
					socket.close();
				}catch(IOException eb){
					
				}
				return "Method connect failed";
				
			}
			
			try{
				outStream = socket.getOutputStream();
				inStream = socket.getInputStream();
			}catch(IOException eb){
				
			}
			return "Method connect succes";
					
		}
	public void write(String s){
		byte[] outBuffer = s.getBytes();
		
		try{
			outStream.write(outBuffer);
		}catch(IOException eb){
			
		}
	}
	
	public boolean streaming(){
		
		return ((inStream!=null) && (outStream != null));
	}
	
	public String read(){
		
		if(!streaming()) return "";
		String inStr = "";
		try{
			if(0<inStream.available()){
				byte[] inBuffer = new byte[1024];
				int bytesRead = inStream.read(inBuffer);
				inStr = new String(inBuffer , "ASCII");
				inStr = inStr.substring(0,bytesRead);
			}
			
		}catch(IOException e){
			
		}
		return inStr;
	}
	
	public void close(){
		try{
			socket.close();
		}catch(IOException e){
			
		}
	}
}

