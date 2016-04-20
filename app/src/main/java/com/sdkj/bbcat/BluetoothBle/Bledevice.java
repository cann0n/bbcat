package com.sdkj.bbcat.BluetoothBle;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class Bledevice {
    Intent serviceIntent;
    protected static final byte[] CRCPASSWORD = {'C', 'h', 'e', 'c', 'k', 'A', 'e', 's'};
    protected Context context = null;
    public String deviceName = null, deviceMac = null;
    protected LightBLEService bleService = null;
    public BluetoothDevice device = null;
    public int bleDeviceType;

    public Bledevice(Context context, BluetoothDevice device) {
        this.device = device;
        this.deviceName = this.device.getName();
        this.deviceMac = this.device.getAddress();
        this.context = context;
        this.registerReceiver();
        if (serviceIntent == null) {
            serviceIntent = new Intent(this.context, LightBLEService.class);
            this.context.bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    public boolean isConnected() {
        if (bleService == null) {
            return false;
        }
        return bleService.isConnect();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = ((LightBLEService.LocalBinder) service).getService();
            bleService.initBluetoothDevice(device, context);
        }
    };

    public void reConnected() {
        if (bleService == null) return;
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        bleService.initBluetoothDevice(device, context);
    }

    public void getSingle() {
        if (bleService == null) return;
        if (device == null) {
            return;
        }
        bleService.readRssi(device);
    }

    public void readValue(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) {
        } else {
            bleService.readValue(this.device, characteristic);
        }
    }

    public void writeValue(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) {
        } else {
            if (bleService == null) return;
            bleService.writeValue(this.device, characteristic);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        if (characteristic == null) {
        } else {
            bleService.setCharacteristicNotification(this.device, characteristic, enable);
        }
    }

    public void disconnectedDevice() {
        this.ungisterReceiver();
        if (serviceConnection != null) this.context.unbindService(serviceConnection);
    }

    protected IntentFilter bleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LightBLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(LightBLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(LightBLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(LightBLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(LightBLEService.ACTION_GAT_RSSI);
        intentFilter.addAction(LightBLEService.ACTION_GATT_CONNECTING);
        intentFilter.addAction("com.imagic.connected");
        return intentFilter;
    }

    public void registerReceiver() {
        Activity activity = (Activity) this.context;
        activity.registerReceiver(gattUpdateRecevice, this.bleIntentFilter());

    }

    public void ungisterReceiver() {
        Activity activity = (Activity) this.context;
        activity.unregisterReceiver(gattUpdateRecevice);
        bleService.disconnect();
        bleService = null;
    }

    protected abstract void discoverCharacteristicsFromService();

    private BroadcastReceiver gattUpdateRecevice = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (LightBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(intent.getAction())) {
                try {
                    discoverCharacteristicsFromService();
                } catch (Exception e) {
                }

            }
        }
    };

}
