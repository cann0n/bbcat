package com.sdkj.bbcat.BluetoothBle;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class BleOut_BServiceAction implements BleIn_CServiceAction
{
    private static BleOut_BServiceAction mInstance;
    private BleOut_BServiceAction(){}

    public synchronized static final BleOut_BServiceAction getInstance()
    {
        if(mInstance == null)
            mInstance = new BleOut_BServiceAction();
        return mInstance;
    }

    public void beginSearchBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.BeginSearchBleBtClazz());
    }

    public void finishSearchBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.FinishSearchBleBtClazz());
    }

    public void completeSearchBleBt(ArrayList<BleIn_ADeviceBean> deviceList)
    {
        BleOut_CResponseClass.CompleteSearchBleBtClazz csb = BleOut_CResponseClass.CompleteSearchBleBtClazz.getInstance();
        csb.setDeviceList(deviceList);
        EventBus.getDefault().post(csb);
    }

    public void reConnectBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReConnectBleBtClazz());
    }

    public void connectingBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectingBleBtClazz());
    }

    public void connectedBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectedBleBtClazz());
    }

    public void connectedFailBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectedFailBleBtClazz());
    }

    public void disConnectingBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectingBleBtClazz());
    }

    public void disConnectedBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectedBleBtClazz());
    }

    public void disConnectedFailBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectedFailBleBtClazz());
    }

    public void readingBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadingBleBtDataClazz());
    }

    public void readedBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtDataClazz());
    }

    public void writingBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.WritingBleBtDataClazz());
    }

    public void writedBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.WritedBleBtDataClazz());
    }

    public void readedBleBtChangeData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtChangeDataClazz());
    }

    public void readingBleBtRssi()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadingBleBtRssiClazz());
    }

    public void readedBleBtRssi()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtRssiClazz());
    }
}