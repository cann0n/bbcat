package com.sdkj.bbcat.BluetoothBle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sdkj.bbcat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * ******手机对远端Ble设备的所有操作*******
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleIn_BService extends Service {
    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;
    private BluetoothAdapter.LeScanCallback mBtAdapterCallback;
    private BluetoothGatt mBtGatt;
    private BluetoothGattCallback mBtGattCallback;
    private BluetoothGattService mBtGattService;
    private BluetoothGattCharacteristic mBtGattRCharacteristic;
    private BluetoothGattCharacteristic mBtGattWCharacteristic;
    private BluetoothGattDescriptor mBtGattCharacterDescriptor;
    private BleBinder mBleBinder;

    private Boolean mBtAvailable = true;
    private Boolean mBleAvailable = true;
    private Integer mScanningTime = 8000;
    private Boolean mScanningDevice = false;
    private UUID[] mScanDeviceUUIDs = new UUID[]{};
    private UUID mServiceUUID = UUID.randomUUID();
    private UUID mRCharacteristicUUID = UUID.randomUUID();
    private UUID mWCharacteristicUUID = UUID.randomUUID();
    private Boolean mConedDevice = false;
    private Boolean mIsReConnect = false;
    private String mRequestDeviceAddress = "";
    private String mCurConedDeviceAddress = "";
    private Set<BleIn_ADeviceBean> mScanDeviceSet = new HashSet<BleIn_ADeviceBean>();
    private Map<String, BluetoothDevice> mScanDeviceMap = new HashMap<String, BluetoothDevice>();
    private final Handler mSearchHandler = new Handler();
    private final Runnable mSearchRunnable = new Runnable() {
        public void run() {
            finishSearchBleBt(true);
        }
    };
    private final Handler mReConDeviceHandler = new Handler();
    private final Runnable mReConDeviceRunnable = new Runnable() {
        public void run() {
            abnormalConnectBleBt();
            mReConDeviceHandler.postDelayed(this, 30000);
        }
    };
    private final Handler mDisConDeviceHandler = new Handler() {
        public void handleMessage(Message msg) {
            notifyDisConnectBleBt();
        }
    };

    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public void onCreate() {
        super.onCreate();
        mBleBinder = new BleBinder();
        mBtManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            mBtAvailable = false;
            quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机没有可用的蓝牙设备哟！");
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mBleAvailable = false;
            quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机的蓝牙设备不支持BLE通讯协议哟！");
        }

        if (mBtAvailable && mBleAvailable) {
            mBtAdapterCallback = new BluetoothAdapter.LeScanCallback() {
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    mScanDeviceMap.put(device.getAddress(), device);
                    mScanDeviceSet.add(new BleIn_ADeviceBean(device.getName(), device.getAddress()));
                }
            };

            mBtGattCallback = new BluetoothGattCallback() {
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    /***表示成功的执行了一次GATT行为，至于执行了什么则需要自行判断才可得知***/
                    if (BluetoothGatt.GATT_SUCCESS == status) {
                        /**********表示手机与从端BLE设备正式连接********/
                        if (BluetoothProfile.STATE_CONNECTED == newState) {
                            try {
                                Thread.sleep(800);
                                mConedDevice = true;
                                mBtGatt.discoverServices();
                                mCurConedDeviceAddress = mRequestDeviceAddress;
                                mReConDeviceHandler.removeCallbacks(mReConDeviceRunnable);
                                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_CONNECTEDBLEBT);
                                startService(intent);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        /**********表示手机与从端BLE设备正式断开********/
                        else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                            try {
                                Thread.sleep(800);
                                mConedDevice = false;
                                mDisConDeviceHandler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        /**********表示手机与从端BLE设备连接失败********/
                        if (BluetoothProfile.STATE_CONNECTED == newState) {
                            if (!mIsReConnect) {
                                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_CONNECTEDFAILBLEBT);
                                startService(intent);
                                quickToast("亲，连接指定的BLE蓝牙设备失败了哟！");
                            } else {
                                quickToast("亲，重新连接BLE蓝牙设备失败了哟！马上为您再次连接......");
                            }
                        }
                        /**********表示手机与从端BLE设备断开失败********/
                        else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_DISCONNECTEDFAILBLEBT);
                            startService(intent);
                            quickToast("亲，断开指定的BLE蓝牙设备失败了哟！");
                        }
                    }
                }

                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    if (BluetoothGatt.GATT_SUCCESS == status) {
                        if (mServiceUUID != null) {
                            if (mBtGatt.getService(mServiceUUID) != null) {
                                mBtGattService = mBtGatt.getService(mServiceUUID);
                                List<BluetoothGattCharacteristic> BtGattCharacteristics = mBtGattService.getCharacteristics();
                                if (BtGattCharacteristics != null) {
                                    for (BluetoothGattCharacteristic characteristic : BtGattCharacteristics) {
                                        if (!setCharacteristicNotification(characteristic, true)) {
                                            quickToast("亲,UUid为：" + characteristic.getUuid() + "的特征属性，为其设置监听特征数据变化的功能失败了哟！");
                                        }
                                    }
                                } else quickToast("亲,当前连接的BLE设备没有任何的特征数据哟！");
                            } else quickToast("亲，指定的BLE设备没有相关的Service哟！");
                        } else quickToast("亲，没有指明所要查找BLE设备中特定的Service哟！");
                    } else quickToast("亲，查找指定BLE设备的所有Service失败了哟！");
                }

                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                    intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READEDBLEBTCHANGEDATA);
                    startService(intent);
                }

                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                    if (BluetoothGatt.GATT_SUCCESS == status) {
                        Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READEDBLEBTDATA);
                        startService(intent);
                    } else quickToast("亲，读取远端BLE设备发送给手机数据的操作失败了哟！");
                }

                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                    if (BluetoothGatt.GATT_SUCCESS == status) {
                        Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_WRITEDBLEBTDATA);
                        startService(intent);
                    } else quickToast("亲，发送命令给远端BLE设备的操作失败了哟！");
                }

                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    super.onReadRemoteRssi(gatt, rssi, status);
                    if (BluetoothGatt.GATT_SUCCESS == status) {
                        Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READEDBLEBTRSSI);
                        startService(intent);
                    } else quickToast("亲，获取手机与BLE设备相隔距离的操作失败了哟！");
                }
            };
        }
    }

    private void quickToast(final String hintStr) {
        mMainThreadHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), hintStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean baseIsAvailable() {
        if (mBtAvailable) {
            if (mBleAvailable) return true;
            else quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机的蓝牙设备不支持BLE通讯协议哟！");
        } else quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机没有可用的蓝牙设备哟！");
        return false;
    }

    private void openLocalBt(final Activity activity, final String notifyStr, final UUID[] uuids) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.inflater_onbt, null);
        alertDialog.setContentView(view);

        TextView hintTv = (TextView) view.findViewById(R.id.bta_hint);
        TextView offTv = (TextView) view.findViewById(R.id.bta_off);
        TextView onTv = (TextView) view.findViewById(R.id.bta_on);
        hintTv.setText(notifyStr);
        offTv.setText("拒绝");
        onTv.setText("允许");

        offTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        onTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    mBtAdapter.enable();
                    alertDialog.dismiss();
                    Thread.sleep(800);
                    mBleBinder.startSearchDevices(activity, notifyStr, uuids);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.BlueToothOpenDialogAnim);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels / 5 * 4;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }

    private void notifyDisConnectBleBt() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.inflater_onbt, null);
        alertDialog.setContentView(view);
        TextView hintTv = (TextView) view.findViewById(R.id.bta_hint);
        TextView offTv = (TextView) view.findViewById(R.id.bta_off);
        TextView reOnTv = (TextView) view.findViewById(R.id.bta_on);
        hintTv.setText("亲，手机与蓝牙设备的连接已经断开了哟！需要帮您重新连接吗？");
        offTv.setText("不用了");
        reOnTv.setText("重新连接吧！");
        offTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mIsReConnect = false;
                mRequestDeviceAddress = "";
                mCurConedDeviceAddress = "";
                mBtGatt.close();
                mBtGatt = null;
                mBtGattService = null;
                mServiceUUID = UUID.randomUUID();
                mBtGattRCharacteristic = null;
                mBtGattWCharacteristic = null;
                mBtGattCharacterDescriptor = null;
                alertDialog.dismiss();
                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_DISCONNECTEDBLEBT);
                startService(intent);
            }
        });

        reOnTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mIsReConnect = true;
                if (deviceBrandIsSamSung()) mBtGatt.connect();
                else {
                    mBtGatt.close();
                    mReConDeviceHandler.post(mReConDeviceRunnable);
                }
                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_RECONNECTBLEBT);
                startService(intent);
                alertDialog.dismiss();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.BlueToothOpenDialogAnim);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels / 5 * 4;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }

    private void beginSearchBleBt(final UUID[] uuids) {
        mSearchHandler.postDelayed(mSearchRunnable, mScanningTime);
        mScanningDevice = true;
        mScanDeviceSet.clear();
        mScanDeviceMap.clear();
        if (uuids != null && uuids.length != 0) {
            mScanDeviceUUIDs = uuids;
            mBtAdapter.startLeScan(mScanDeviceUUIDs, mBtAdapterCallback);
        } else {
            mScanDeviceUUIDs = new UUID[]{};
            mBtAdapter.startLeScan(mBtAdapterCallback);
        }
        Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_BEGINSEARCHBLEBT);
        startService(intent);
    }

    private void finishSearchBleBt(final Boolean searchComplete) {
        mScanningDevice = false;
        mBtAdapter.stopLeScan(mBtAdapterCallback);
        mSearchHandler.removeCallbacks(mSearchRunnable);
        if (searchComplete) {
            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_COMPLETESEARCHBLEBT);
            intent.putParcelableArrayListExtra("ActionData", new ArrayList<BleIn_ADeviceBean>(mScanDeviceSet));
            startService(intent);
        } else {
            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_FINISHSEARCHBLEBT);
            startService(intent);
        }
    }

    private void connectBleBt(final String address, final UUID serviceUUID) {
        if (mScanDeviceMap.get(address) != null) {
            mRequestDeviceAddress = address;
            mServiceUUID = serviceUUID;
            mBtGatt = mScanDeviceMap.get(address).connectGatt(getApplicationContext(), false, mBtGattCallback);
            if (!mIsReConnect) {
                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_CONNECTINGBLEBT);
                startService(intent);
            }
        } else {
            if (!mIsReConnect) quickToast("亲，连接的BLE蓝牙设备不在连接范围内哟！");
            else quickToast("亲，可能重连的BLE蓝牙设备不在连接范围内哟！马上为您再次连接......");
        }
    }

    private void abnormalConnectBleBt() {
        mBtAdapter.enable();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mScanningDevice = false;
                mBtAdapter.stopLeScan(mBtAdapterCallback);
                connectBleBt(mCurConedDeviceAddress, mServiceUUID);
            }
        }, 6000);

        mScanningDevice = true;
        mScanDeviceSet.clear();
        mScanDeviceMap.clear();
        if (mScanDeviceUUIDs != null && mScanDeviceUUIDs.length != 0)
            mBtAdapter.startLeScan(mScanDeviceUUIDs, mBtAdapterCallback);
        else mBtAdapter.startLeScan(mBtAdapterCallback);
    }

    private void stopAbnormalConnectBleBt() {
        mReConDeviceHandler.removeCallbacks(mReConDeviceRunnable);
        mBtGatt.close();
        mBtGatt = null;
        mIsReConnect = false;
        mRequestDeviceAddress = "";
        mCurConedDeviceAddress = "";
        mBtGattService = null;
        mServiceUUID = UUID.randomUUID();
        mBtGattRCharacteristic = null;
        mBtGattWCharacteristic = null;
        mBtGattCharacterDescriptor = null;
    }

    private void disConnectBleBt() {
        if (mBtGatt != null) {
            mBtGatt.disconnect();
            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_DISCONNECTINGBLEBT);
            startService(intent);
        } else quickToast("亲，当前手机没有连接任何BLE设备，无需断开连接哟！");
    }

    private Boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, Boolean enable) {
        if (mBtAdapter == null || mBtGatt == null || mBtGattService == null) {
            quickToast("亲，因为手机未与任何BLE设备连接，所以添加监听数据变化的功能失败了哟！");
            return false;
        }
        if (characteristic == null) {
            quickToast("亲，因为没有特征数据，所以添加监听数据变化的功能失败了哟！");
            return false;
        }
        if (!mBtGatt.setCharacteristicNotification(characteristic, enable)) {
            quickToast("亲，因为添加监听数据变化的操作失败，所以添加监听数据变化的功能失败了哟！");
            return false;
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(BleOut_AUuidData.SERVICE_DESCRIPTORUUID);
        if (descriptor == null) return true;

        if (enable) {
            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            else descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        } else descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        return mBtGatt.writeDescriptor(descriptor);
    }

    private void ReadDatasFromBleBt(final UUID rCharacteristicUUID) {
        if (mBtGatt != null) {
            if (mBtGattService != null) {
                if (rCharacteristicUUID != null) {
                    mRCharacteristicUUID = rCharacteristicUUID;
                    if (mBtGattService.getCharacteristic(mRCharacteristicUUID) != null) {
                        mBtGattRCharacteristic = mBtGattService.getCharacteristic(mRCharacteristicUUID);
                        mBtGatt.readCharacteristic(mBtGattRCharacteristic);
                        Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READINGBLEBTDATA);
                        startService(intent);
                    } else quickToast("亲，因为指定的BLE设备中，指明的Service里面没有查找到相关的特征属性，所以无法进行读取操作哟！");
                } else quickToast("亲，因为没有指定读取特征属性的UUID，所以无法进行读取操作哟！");
            } else quickToast("亲，因为没有查找到BLE设备中指定的Service，所以无法进行读取操作哟！");
        } else quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法进行读取操作哟！");
    }

    private void writeDatasToBleBt(final UUID wCharacteristicUUID, final byte[] byteArray) {
        if (mBtGatt != null) {
            if (mBtGattService != null) {
                if (wCharacteristicUUID != null) {
                    mWCharacteristicUUID = wCharacteristicUUID;
                    if (mBtGattService.getCharacteristic(mWCharacteristicUUID) != null) {
                        mBtGattWCharacteristic = mBtGattService.getCharacteristic(mWCharacteristicUUID);
                        if (byteArray != null && byteArray.length != 0) {
                            mBtGattWCharacteristic.setValue(byteArray);
                            mBtGattWCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            mBtGatt.writeCharacteristic(mBtGattWCharacteristic);
                            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_WRITINGBLEBTDATA);
                            startService(intent);
                        } else quickToast("亲，因为发送给BLE设备的命令为空，所以无法进行写入操作哟！");
                    } else quickToast("亲，因为指定的BLE设备中，指明的Service里面没有查找到相关的特征属性，所以无法进行写入操作哟！");
                } else quickToast("亲，因为没有指定写入特征属性的UUID，所以无法进行写入操作哟！");
            } else quickToast("亲，因为没有查找到BLE设备中指定的Service，所以无法进行写入操作哟！");
        } else quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法进行写入操作哟！");
    }

    private void getRssiFromBleBt() {
        if (mBtGatt != null) {
            mBtGatt.readRemoteRssi();
            Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READINGBLEBTRSSI);
            startService(intent);
        } else quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法获取RSSI距离哟！");
    }

    private Boolean deviceBrandIsSamSung() {
        String deviceBrand = android.os.Build.BRAND.toLowerCase();
        if (deviceBrand.equals("samsung")) return true;
        return false;
    }

    public class BleBinder extends Binder {
        public void startSearchDevices(final Activity activity, final String notifyStr, final UUID[] uuids) {
            if (baseIsAvailable()) {
                if (mBtAdapter.isEnabled()) beginSearchBleBt(uuids);
                else openLocalBt(activity, notifyStr, uuids);
            }
        }

        public void stopSearchDevices() {
            if (baseIsAvailable()) {
                finishSearchBleBt(false);
            }
        }

        public void conDevice(final String address, final UUID serviceUUID) {
            if (baseIsAvailable()) {
                connectBleBt(address, serviceUUID);
            }
        }

        public void disConDevice() {
            if (baseIsAvailable()) {
                disConnectBleBt();
            }
        }

        public void stopReConDevice() {
            if (baseIsAvailable()) {
                stopAbnormalConnectBleBt();
            }
        }

        public void readDatas(final UUID rCharacteristicUUID) {
            if (baseIsAvailable()) {
                ReadDatasFromBleBt(rCharacteristicUUID);
            }
        }

        public void writeDatas(final UUID wCharacteristicUUID, final byte[] byteArray) {
            if (baseIsAvailable()) {
                writeDatasToBleBt(wCharacteristicUUID, byteArray);
            }
        }

        public void getRssi() {
            if (baseIsAvailable()) {
                getRssiFromBleBt();
            }
        }

        public Boolean isScanningDevices() {
            return mScanningDevice;
        }

        public void setScanDevicesTime(final Integer scanTime) {
            mScanningTime = scanTime;
        }

        public Boolean getConnectState() {
            return mConedDevice;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBleBinder;
    }
}