package com.sdkj.bbcat.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FirstInstallViewPagerAdapter extends PagerAdapter
{
    private Context mContext;
    private List<View> mContentArray;

    public FirstInstallViewPagerAdapter(Context context,List<View> contentArray)
    {
        mContext = context;
        mContentArray = contentArray;
    }
    @Override
    public int getCount()
    {
        return mContentArray.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(mContentArray.get(position));
        return mContentArray.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
       container.removeView(mContentArray.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return (view == object);
    }
}
