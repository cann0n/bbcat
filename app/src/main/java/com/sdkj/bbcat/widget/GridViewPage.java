package com.sdkj.bbcat.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sdkj.bbcat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格布局viewpage
 *
 * 基本用法：
 * 1.setModuleMenuRowCol(int row, int col)设置每屏显示的行列数（必须设置且放在第一步实现）
 * 2.new GridViewPage.GirdViewPageAdapter(activity,datas) 然后实现里面的方法
 * 3.setAdapter()
 *
 * 可以用setPoint_unselect_resId以及setPoint_select_resId设置小圆点图标
 *
 *
 *
 *导入控件文件之后引用此控件的XML就是一般的自定义View写法:eg:
 * <com.hx100.weilian.widget.GridViewPage
 *      android:id="@+id/modulemenu"
 *       android:layout_width="match_parent"
 *       android:layout_height="wrap_content"
 *       android:layout_gravity="bottom">
 * </com.hx100.weilian.widget.GridViewPage>
 *
 *
 */
//   tip:GirdViewPageAdapter的example：
//
//    GridViewPage.GirdViewPageAdapter adapter=new GridViewPage.GirdViewPageAdapter(activity,list) {
//        private int selectindex=0;
//
//        @Override
//        public View getView(final int position, View convertView) {
//            final ViewHolder holder;
//            if (convertView==null){
//                convertView=activity.makeView(R.layout.item_tv_select);
//                holder=new ViewHolder();
//                holder.tv= (TextView) convertView.findViewById(R.id.template_modulemenu_item_title);
//                convertView.setTag(holder);
//            }else {
//                holder= (ViewHolder) convertView.getTag();
//            }
//            holder.tv.setText(list.get(position));
//            if (selectindex==position){
//                holder.tv.setBackgroundResource(R.drawable.btn_corners5_white);
//            }else {
//                holder.tv.setBackgroundResource(R.drawable.btn_corners5);
//            }
//            holder.tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectindex=position;
//                    notifyDataSetChanged(position);
//                }
//            });
//            return convertView;
//        }
//        /*ViewHolder类*/
//        final class ViewHolder{
//            public TextView tv;
//        }
//
//    };
public class GridViewPage extends FrameLayout {

    private Context mContext;
    public ViewPager mViewPage;//菜单可滑动viewpage（菜单选项过少就只显示一页不滑动）
    public LinearLayout mPointLinearLayout;// 指示原点
    private ModuleMenuPagerAdapter mModuleMenuPagerAdapter;//viewpage的适配器
    private int mViewPageScreenCount = 1;//viewpage有几页 及需要显示几屏

    //默认指示小圆点resID
    private int Point_select_resId = R.drawable.icon_point_select;
    private int Point_unselect_resId = R.drawable.icon_point_unselect;
    private List<FlowLayout> list_flowlayout;
    //菜单每页的行数 row
    private int mFlowLayoutRow = 1;
    //菜单每页的列数col
    private int mFlowLayoutCol = 1;
    //菜单每页的个数 row*col
    private int mFlowLayoutRowMulCol = 1;
    //是否进行了SetModuleMenuRowCol函数
    private boolean isSetModuleMenuRowCol = false;

    //根据加入的数据 计算viewpage的高度
    private int mViewPageHeight = -1;


    private GirdViewPageAdapter mAdapter;

    //adapter里面的那个getView得到的view
    private View convertView;

    public GridViewPage(Context context) {
        this(context, null);
    }

    public GridViewPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    private void initViews() {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.template_girdviewpage, this);
        //初始化小圆点
        initPoint(view);
        //初始化viewpage
        initViewPage(view);
    }

    /**
     * 设置菜单选项的行列数
     *
     * @param row 行数
     * @param col 列数
     */
    public void setModuleMenuRowCol(int row, int col) {
        mFlowLayoutRow = row;
        mFlowLayoutCol = col;
        mFlowLayoutRowMulCol = row * col;
        isSetModuleMenuRowCol = true;
    }


    public void setAdapter(GirdViewPageAdapter adapter) {
        this.mAdapter = adapter;
        mAdapter.setmGridViewPage(this);
        loadView(mAdapter.mDatas);
        initViews();
    }


    public static abstract class GirdViewPageAdapter {
        public List mDatas;
        private GridViewPage mGridViewPage;

        public GirdViewPageAdapter(Context context, List datas) {
            this.mDatas = datas;
        }

        public abstract View getView(int position, View convertView);

        public void notifyDataSetChanged(int position) {
            if (mGridViewPage != null) {
                mGridViewPage.loadView(mDatas);
                mGridViewPage.refreshView(mGridViewPage.calViewPageScreenCount(position + 1, mGridViewPage.mFlowLayoutRowMulCol));
            }
        }

        public void setmGridViewPage(GridViewPage mGridViewPage) {
            this.mGridViewPage = mGridViewPage;
        }
    }

    /**
     * 刷新view
     */
    private void refreshView(int currentScreen) {
        mViewPage.setAdapter(mModuleMenuPagerAdapter);
        mViewPage.setCurrentItem(currentScreen - 1);
    }

    /**
     * 加载你需要显示的数据
     * <p/>
     * 必须要先调用setModuleMenuRowCol函数才行
     *
     * @param list 数据源集合
     */
    private void loadView(List list) {
        if (list != null && list.size() > 0 && isSetModuleMenuRowCol) {
            if (list_flowlayout != null) {
                list_flowlayout.clear();
            } else {
                list_flowlayout = new ArrayList<FlowLayout>();
            }
            if (mFlowLayoutRowMulCol >= list.size()) {
                mViewPageScreenCount = 1;
                FlowLayout flowLayout = creatFlowLayout();
                setFlowLayoutData(mViewPageScreenCount, 1, flowLayout);
                list_flowlayout.add(flowLayout);
            } else {
                mViewPageScreenCount = calViewPageScreenCount(list.size(), mFlowLayoutRowMulCol);
                for (int i = 0; i < mViewPageScreenCount; i++) {
                    FlowLayout flowLayout = creatFlowLayout();
                    setFlowLayoutData(mViewPageScreenCount, i + 1, flowLayout);
                    list_flowlayout.add(flowLayout);
                }
            }
            //计算viewpage高度
            calViewPageHeight(list_flowlayout.get(0));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mViewPageHeight != -1) {
                    mViewPage.getLayoutParams().height = mViewPageHeight;
//                    Toast.makeText(mContext, "" + mViewPage.getLayoutParams().height, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 根据加入进来的item计算 viewpage的高度
     * 这是异步方法 所以需要hander来通知viewpage改变高度
     *
     * @param flowlayout 每屏里面的flowlayout的高度就是viewpage的高度
     * @return
     */
    private void calViewPageHeight(final FlowLayout flowlayout) {
        ViewTreeObserver vto = flowlayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                        recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mViewPageHeight = flowlayout.getChildAt(0).getHeight() * mFlowLayoutRow;
//                Toast.makeText(mContext, "" + mViewPageHeight, Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(1);
            }
        });

    }

    /**
     * 计算一共需要几屏
     */
    private int calViewPageScreenCount(int datasize, int Row_mul_Col) {
        int size = 1;
        if (datasize % Row_mul_Col == 0) {
            size = datasize / Row_mul_Col;
        } else {
            size = datasize / Row_mul_Col + 1;
        }
        return size;
    }

    /**
     * 设置FlowLayoutData
     *
     * @param sumscreencount 总共需要加载的屏幕数
     * @param currentscreent 当前屏幕数
     * @param flowLayout     需要加载的flowlayout
     */
    private void setFlowLayoutData(int sumscreencount, int currentscreent, final FlowLayout flowLayout) {
        if (sumscreencount == 1) {
            for (int i = 0, len = mAdapter.mDatas.size(); i < len; i++) {
                LinearLayout averageItemView = creatAverageItemView(mFlowLayoutCol);
                if (convertView != null) {
                    convertView = null;
                }
                convertView = mAdapter.getView((currentscreent - 1) * mFlowLayoutRowMulCol + i, convertView);
                if (convertView == null) {
                    convertView = new View(mContext);
                }
                averageItemView.addView(convertView);
                flowLayout.addView(averageItemView);
            }

        } else {
            //多屏且不是最后一页
            if (sumscreencount != currentscreent) {
                List newlist = new ArrayList<>(mAdapter.mDatas.subList((currentscreent - 1) * mFlowLayoutRowMulCol,
                        currentscreent * mFlowLayoutRowMulCol));
                for (int i = 0, len = newlist.size(); i < len; i++) {
                    LinearLayout averageItemView = creatAverageItemView(mFlowLayoutCol);
                    if (convertView != null) {
                        convertView = null;
                    }
                    convertView = mAdapter.getView((currentscreent - 1) * mFlowLayoutRowMulCol + i, convertView);
                    if (convertView == null) {
                        convertView = new View(mContext);
                    }
                    averageItemView.addView(convertView);
                    flowLayout.addView(averageItemView);
                }

            } else {
                List newlist = new ArrayList<>(mAdapter.mDatas.subList((currentscreent - 1) * mFlowLayoutRowMulCol,
                        mAdapter.mDatas.size()));
                for (int i = 0, len = newlist.size(); i < len; i++) {
                    LinearLayout averageItemView = creatAverageItemView(mFlowLayoutCol);
                    if (convertView != null) {
                        convertView = null;
                    }
                    convertView = mAdapter.getView((currentscreent - 1) * mFlowLayoutRowMulCol + i, convertView);
                    if (convertView == null) {
                        convertView = new View(mContext);
                    }
                    averageItemView.addView(convertView);
                    flowLayout.addView(averageItemView);
                }
            }

        }
    }


    /**
     * 根据需要的列数来创建一个宽度为屏幕宽度1/col的子view 放在flowlayout中
     * <p/>
     * 这个view 充当 ModuleMenu.ModuleMenuItem.getItemView()的父view
     *
     * @param col
     * @return
     */
    private LinearLayout creatAverageItemView(int col) {
        LinearLayout averageItemView = new LinearLayout(mContext);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mScreenWidth / col,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        averageItemView.setLayoutParams(params);
        averageItemView.setGravity(Gravity.CENTER);
        return averageItemView;
    }


    /**
     * 创建FlowLayout
     *
     * @return
     */
    private FlowLayout creatFlowLayout() {
        FlowLayout flowLayout = new FlowLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        flowLayout.setLayoutParams(params);
        return flowLayout;
    }


    /**
     * 初始化小圆点
     *
     * @param view
     */
    private void initPoint(View view) {
        mPointLinearLayout = (LinearLayout) view.findViewById(R.id.template_gird_points);
        mPointLinearLayout.removeAllViews();
        //list.size=1就不显示原点
        if (list_flowlayout != null && list_flowlayout.size() == 1) {
            mPointLinearLayout.setVisibility(GONE);
        } else if (list_flowlayout != null && list_flowlayout.size() > 1) {
            mPointLinearLayout.setVisibility(VISIBLE);
            for (int i = 0, len = list_flowlayout.size(); i < len; i++) {
                // 设置小圆点
                ImageView point = new ImageView(mContext);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.rightMargin = 15;
                params2.height = 24;
                params2.width = 24;
                point.setLayoutParams(params2);
                // 默认显示第一页为选中
                if (i == 0) {
                    point.setImageResource(Point_select_resId);
                } else {
                    point.setImageResource(Point_unselect_resId);
                }
                mPointLinearLayout.addView(point);
            }
        }
    }

    /**
     * 初始化viewpage
     *
     * @param view
     */
    private void initViewPage(View view) {
        mViewPage = (ViewPager) view.findViewById(R.id.template_gird_viewpage);
        mViewPage.setOffscreenPageLimit(list_flowlayout.size());
        mModuleMenuPagerAdapter = new ModuleMenuPagerAdapter(list_flowlayout);
        mViewPage.setAdapter(mModuleMenuPagerAdapter);
        mViewPage.addOnPageChangeListener(new ModuleMenuOnPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     * Created by Levi on 2015/9/21.
     */
    public class ModuleMenuPagerAdapter extends PagerAdapter {
        private List<FlowLayout> mList;


        public ModuleMenuPagerAdapter(List<FlowLayout> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    //viewpage滑动监听
    private class ModuleMenuOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    break;
                case 2:// 界面切换中
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            if (list_flowlayout.size() > 1) {
                // 改变指示点的状态
                for (int i = 0, len = list_flowlayout.size(); i < len; i++) {
                    ImageView imgPoint = (ImageView) mPointLinearLayout.getChildAt(i);
                    if (i == arg0) {// 当前小圆点
                        imgPoint.setImageResource(Point_select_resId);
                    } else {// 设置没有在当前页面的指示点
                        imgPoint.setImageResource(Point_unselect_resId);
                    }
                }
            }
        }

    }

    /**
     * 设置未选中小圆点的resid
     *
     * @param point_unselect_resId
     */
    public void setPoint_unselect_resId(int point_unselect_resId) {
        Point_unselect_resId = point_unselect_resId;
    }

    /**
     * 设置选中小圆点的resid
     *
     * @param point_select_resId
     */
    public void setPoint_select_resId(int point_select_resId) {
        Point_select_resId = point_select_resId;
    }


}
