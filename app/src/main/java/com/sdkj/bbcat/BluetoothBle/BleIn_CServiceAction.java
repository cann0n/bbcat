package com.sdkj.bbcat.BluetoothBle;

import java.util.ArrayList;

public interface BleIn_CServiceAction
{
    public void beginSearchBleBt();
    public void finishSearchBleBt();
    public void completeSearchBleBt(ArrayList<BleIn_ADeviceBean> deviceList);
    public void reConnectBleBt();
    public void connectingBleBt();
    public void connectedBleBt();
    public void connectedFailBleBt();
    public void disConnectingBleBt();
    public void disConnectedBleBt();
    public void disConnectedFailBleBt();
    public void readingBleBtData();
    public void readedBleBtData();
    public void writingBleBtData();
    public void writedBleBtData();
    public void readedBleBtChangeData();
    public void readingBleBtRssi();
    public void readedBleBtRssi();
}