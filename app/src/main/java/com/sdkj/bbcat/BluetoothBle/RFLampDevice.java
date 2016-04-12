package com.sdkj.bbcat.BluetoothBle;

import java.util.List;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;

public class RFLampDevice extends Bledevice {

	private BluetoothGattCharacteristic shishiCharateristic;
	private BluetoothGattCharacteristic shujuCharateristic;

	public RFLampDevice(Context context, BluetoothDevice device) {
		super(context, device);
		this.device = device;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void discoverCharacteristicsFromService() {
		if (bleService == null || device == null) {

			return;
		}
		List<BluetoothGattService> services = bleService
				.getSupportedGattServices(this.device);
		if (services == null) {
			return;
		}
		for (BluetoothGattService service : services) {
			for (BluetoothGattCharacteristic characteristic : service
					.getCharacteristics()) {
				if (characteristic.getUuid().toString().contains("fff6")) {
					shujuCharateristic = characteristic;
				} else if (characteristic.getUuid().toString().contains("fff7")) {
					shishiCharateristic = characteristic;
					this.setCharacteristicNotification(characteristic, true);
				}

			}
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void sendUpdate(byte[] value) {
		if (shujuCharateristic == null)
			return;

		shujuCharateristic.setValue(value);
		this.writeValue(shujuCharateristic);
	}

}
