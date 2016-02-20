package com.sdkj.bbcat.BluetoothBle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class BleOut_CResponseClass
{
    public static class BeginSearchBleBtClazz
    {}

    public static class FinishSearchBleBtClazz
    {}

    public static class CompleteSearchBleBtClazz
    {
        private static CompleteSearchBleBtClazz mInstance;
        private ArrayList<BleIn_ADeviceBean> deviceList;

        private  CompleteSearchBleBtClazz()
        {
        }

        public synchronized static final CompleteSearchBleBtClazz getInstance()
        {
            if(mInstance == null)
                mInstance = new CompleteSearchBleBtClazz();
            return mInstance;
        }

        public ArrayList<BleIn_ADeviceBean> getDeviceList()
        {
            return deviceList;
        }

        public void setDeviceList(ArrayList<BleIn_ADeviceBean> deviceList)
        {
            this.deviceList = deviceList;
        }
    }

    public static class ReConnectBleBtClazz
    {}

    public static class ConnectingBleBtClazz
    {}

    public static class ConnectedBleBtClazz
    {}

    public static class ConnectedFailBleBtClazz
    {}

    public static class DisConnectingBleBtClazz
    {}

    public static class DisConnectedBleBtClazz
    {}

    public static class DisConnectedFailBleBtClazz
    {}

    public static class ReadingBleBtDataClazz
    {}

    public static class ReadedBleBtDataClazz
    {}

    public static class WritingBleBtDataClazz
    {}

    public static class WritedBleBtDataClazz
    {}

    public static class ReadedBleBtChangeDataClazz
    {}

    public static class ReadingBleBtRssiClazz
    {}

    public static class ReadedBleBtRssiClazz
    {}
}