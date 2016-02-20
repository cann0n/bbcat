package com.sdkj.bbcat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdkj.bbcat.BluetoothBle.BleIn_ADeviceBean;
import com.sdkj.bbcat.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/17 0017.
 */
public class DeviceListAdapter extends BaseAdapter
{
    private static DeviceListAdapter mInstance;
    private static Context mContext;
    private ArrayList<BleIn_ADeviceBean> deviceList;
    private DeviceListAdapter() {}

    public synchronized static final DeviceListAdapter getInstance(Context context)
    {
        if(mInstance == null)
            mInstance = new DeviceListAdapter();
        mContext = context;
        return mInstance;
    }

    public void setDeviceList(ArrayList<BleIn_ADeviceBean> deviceList)
    {
        this.deviceList = deviceList;
    }

    public int getCount()
    {
        return deviceList.size();
    }

    public Object getItem(int position)
    {
        return deviceList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        Holder holder;
        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.devicelistitem,null);
            holder = new Holder();
            holder.name = (TextView)view.findViewById(R.id.devicelistitem_name);
            holder.address = (TextView)view.findViewById(R.id.devicelistitem_address);
            view.setTag(holder);
        }
        else
            holder = (Holder)view.getTag();

        BleIn_ADeviceBean  bean = deviceList.get(position);
        holder.name.setText(bean.getmDeviceName());
        holder.address.setText(bean.getmDeviceAddress());
        return view;
    }

    private class Holder
    {
        TextView name;
        TextView address;
    }
}