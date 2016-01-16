package com.sdkj.bbcat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.SearchTagVo;

import java.util.List;

public class TagBaseAdapter extends BaseAdapter
{
    private Context mContext;
    private List<SearchTagVo> mList;

    public TagBaseAdapter(Context context, List<SearchTagVo> list)
    {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SearchTagVo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tagview, null);
            holder = new ViewHolder();
            holder.tagBtn = (Button) convertView.findViewById(R.id.tag_btn);
            convertView.setTag(holder);
        }

        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        final SearchTagVo text = getItem(position);
        holder.tagBtn.setText(text.getKeyword());
        return convertView;
    }

    static class ViewHolder
    {
        Button tagBtn;
    }
}
