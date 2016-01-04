package com.sdkj.bbcat.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.huaxi100.networkapp.adapter.FragPagerAdapter;
import com.huaxi100.networkapp.fragment.BaseFragment;
import com.huaxi100.networkapp.fragment.FragmentVo;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.sdkj.bbcat.R;

import java.util.ArrayList;

/**
 * Created by ${Rhino} on 2015/11/12 09:58
 */
public class BraceletPage extends BaseFragment
{
    @ViewInject(R.id.bra_vp)
    private ViewPager viewPager;

    @ViewInject(R.id.bra_vwselectorone)
    private View viewPagerLv;

    @ViewInject(R.id.bra_vwselectortwo)
    private View viewPagerRv;

    protected int setLayoutResID()
    {
        return R.layout.fragment_bracelet;
    }

    protected void setListener()
    {
        ArrayList<FragmentVo> pageVo = new ArrayList<FragmentVo>();
        pageVo.add(new FragmentVo(new FragmentBracelet(), "智能手环"));
        pageVo.add(new FragmentVo(new FragmentSpoor(), "成长足迹"));
        FragPagerAdapter adapter = new FragPagerAdapter(activity.getSupportFragmentManager(), pageVo);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                changeBtn(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        changeBtn(0);
    }

    private void changeBtn(int position)
    {
        if (position == 0)
        {
            viewPagerLv.setVisibility(View.VISIBLE);
            viewPagerRv.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewPagerRv.setVisibility(View.VISIBLE);
            viewPagerLv.setVisibility(View.INVISIBLE);
        }
    }
}
