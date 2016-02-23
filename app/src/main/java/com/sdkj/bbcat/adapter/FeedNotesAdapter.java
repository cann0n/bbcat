package com.sdkj.bbcat.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaxi100.networkapp.activity.BaseActivity;
import com.huaxi100.networkapp.adapter.UltimatCommonAdapter;
import com.huaxi100.networkapp.utils.Utils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.bean.FeedInoVo;

import java.util.List;

/**
 * Created by Mr.Yuan on 2016/1/8 0008.
 */
public class FeedNotesAdapter extends UltimatCommonAdapter<FeedInoVo, FeedNotesAdapter.ViewHolder> {
    public FeedNotesAdapter(BaseActivity activity, List<FeedInoVo> data) {
        super(activity, ViewHolder.class, R.id.class, data, R.layout.inflater_feednote);
    }

    public void onBindViewHolder(UltimateRecyclerviewViewHolder h, int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            final FeedInoVo feedInoVo = getItem(position);
            holder.tv_time.setText(feedInoVo.getDay());
            holder.tv_week.setText(feedInoVo.getWeek());
            holder.ll_container.removeAllViews();
            if (!Utils.isEmpty(feedInoVo.getList())) {
                for (FeedInoVo.FeedInfo vo : feedInoVo.getList()) {
                    View view = activity.makeView(R.layout.item_feed_list);
                    ImageView iv_type = (ImageView) view.findViewById(R.id.iv_type);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
                    TextView tv_long = (TextView) view.findViewById(R.id.tv_long);
                    TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                    if (vo.getType() == 1) {
                        iv_type.setImageResource(R.drawable.icon_naiping);
                    } else if (vo.getType() == 2) {
                        iv_type.setImageResource(R.drawable.icon_naifen);
                    } else if (vo.getType() == 3) {
                        iv_type.setImageResource(R.drawable.icon_muru);
                    } else if (vo.getType() == 4) {
                        iv_type.setImageResource(R.drawable.icon_fushi);
                    }
                    tv_name.setText(vo.getTime());
                    tv_type.setText(vo.getName());
                    tv_long.setText(vo.getNum());
                    tv_desc.setText(vo.getDesc());
                    holder.ll_container.addView(view);
                }
            }
        }
    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {
        TextView tv_time;
        TextView tv_week;
        LinearLayout ll_container;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
