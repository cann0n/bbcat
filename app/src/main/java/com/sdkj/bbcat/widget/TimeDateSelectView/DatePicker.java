package com.sdkj.bbcat.widget.TimeDateSelectView;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sdkj.bbcat.widget.TimeDateSelectView.wheel.DateObject;
import com.sdkj.bbcat.widget.TimeDateSelectView.wheel.OnWheelChangedListener;
import com.sdkj.bbcat.widget.TimeDateSelectView.wheel.StringWheelAdapter;
import com.sdkj.bbcat.widget.TimeDateSelectView.wheel.WheelView;


/**
 * 自定义的日期选择器
 * @author sxzhang
 *
 */
public class DatePicker extends LinearLayout {
	
	private Calendar calendar = Calendar.getInstance(); //日期实例
	private WheelView newDays;
	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private ArrayList<DateObject> dateList ,yearList,monthList,dayList;
	private OnChangeListener onChangeListener; //onChangeListener
	private final int MARGIN_RIGHT = 20;
	private DateObject dateObject;		//日期数据对象
	private int year;
	private int month;
	private int day;
	//Constructors
	public DatePicker(Context context) {
		super(context);
		init(context);
	}
	
	public DatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context){
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		dateList = new ArrayList<DateObject>();
		yearList = new ArrayList<DateObject>();
		monthList = new ArrayList<DateObject>();
		dayList = new ArrayList<DateObject>();
//		for (int i = 0; i < 7; i++) {
//			dateObject = new DateObject(year, month, day+i, week+i);
//			dateList.add(dateObject);
//		}
		for (int i = 0; i < 60; i++) {
			dateObject = new DateObject(1,year-20+i);
			yearList.add(dateObject);
		}
		for (int i = 0; i < 12; i++) {
			dateObject = new DateObject(2,i+1);
			monthList.add(dateObject);
		}
		for (int i = 0; i < calMaxDayByYearMonth(year,month); i++) {
			dateObject = new DateObject(3,i+1);
			dayList.add(dateObject);
		}

//		newDays = new WheelView(context);
//		LayoutParams newDays_param = new LayoutParams(300,LayoutParams.WRAP_CONTENT);
//		newDays_param.setMargins(0, 0, MARGIN_RIGHT, 0);
//		newDays.setLayoutParams(newDays_param);
//		newDays.setAdapter(new StringWheelAdapter(dateList, 7));
//		newDays.setVisibleItems(3);
//		newDays.setCyclic(true);
//		newDays.addChangingListener(onDaysChangedListener);
//		addView(newDays);

		//年选择器
		yearView = new WheelView(context);
		LayoutParams lparams_hours = new LayoutParams(120,LayoutParams.WRAP_CONTENT);
		lparams_hours.setMargins(0, 0, MARGIN_RIGHT, 0);
		yearView.setLayoutParams(lparams_hours);
		yearView.setAdapter(new StringWheelAdapter(yearList, yearList.size()));
		yearView.setVisibleItems(3);
		yearView.setCyclic(false);
		yearView.addChangingListener(onYearChangedListener);
		addView(yearView);

		//月选择器
		monthView = new WheelView(context);
		monthView.setLayoutParams(new LayoutParams(120, LayoutParams.WRAP_CONTENT));
		monthView.setAdapter(new StringWheelAdapter(monthList, monthList.size()));
		monthView.setVisibleItems(3);
		monthView.setCyclic(true);
		monthView.addChangingListener(onMonthChangedListener);
		addView(monthView);

		//日选择器
		dayView = new WheelView(context);
		dayView.setLayoutParams(new LayoutParams(120, LayoutParams.WRAP_CONTENT));
		dayView.setAdapter(new StringWheelAdapter(dayList, dateList.size()));
		dayView.setVisibleItems(3);
		dayView.setCyclic(true);
		dayView.addChangingListener(onDayChangedListener);
		addView(dayView);

		//设置为当前年 月 日
		yearView.setCurrentItem(20);
		monthView.setCurrentItem(month-1);
		dayView.setCurrentItem(day - 1);
	}
	
	/**
	 * 滑动改变监听器
	 */
	private OnWheelChangedListener onDaysChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView mins, int oldValue, int newValue) {
			calendar.set(Calendar.DAY_OF_MONTH, newValue + 1);
			change();
		}
	};
	//listeners
	private OnWheelChangedListener onYearChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView wheelView, int oldValue, int newValue) {
			change();
		}
	};
	private OnWheelChangedListener onMonthChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView wheelView, int oldValue, int newValue) {
			change();
		}
	};
	private OnWheelChangedListener onDayChangedListener = new OnWheelChangedListener(){
		@Override
		public void onChanged(WheelView wheelView, int oldValue, int newValue) {
			change();
		}
	};
	/**
	 * 滑动改变监听器回调的接口
	 */
	public interface OnChangeListener {
		void onChange(int year, int month, int day);
	}
	
	/**
	 * 设置滑动改变监听器
	 * @param onChangeListener
	 */
	public void setOnChangeListener(OnChangeListener onChangeListener){
		this.onChangeListener = onChangeListener;
		if(onChangeListener!=null){
			onChangeListener.onChange(
					yearList.get(yearView.getCurrentItem()).getYear(),
					monthList.get(monthView.getCurrentItem()).getMonth(),
					dayList.get(dayView.getCurrentItem()).getDay());
		}
	}
	
	/**
	 * 滑动最终调用的方法
	 */
	private void change(){
		int maxDay=calMaxDayByYearMonth(yearList.get(yearView.getCurrentItem()).getYear(),
				monthList.get(monthView.getCurrentItem()).getMonth());
		if (maxDay!=dayView.getAdapter().getItemsCount()){
			int dayindex=dayList.get(dayView.getCurrentItem()).getDay();
			if (dayindex>maxDay){
				dayindex=maxDay;
			}
			dateList.clear();
			for (int i = 0; i < maxDay; i++) {
				dateObject = new DateObject(3,i+1);
				dayList.add(dateObject);
			}
			dayView.setAdapter(new StringWheelAdapter(dayList, dateList.size()));
			dayView.setCurrentItem(dayindex-1);
		}
		if(onChangeListener!=null){
			onChangeListener.onChange(
					yearList.get(yearView.getCurrentItem()).getYear(),
					monthList.get(monthView.getCurrentItem()).getMonth(),
					dayList.get(dayView.getCurrentItem()).getDay());
		}
	}

	/**
	 * 通过年和月计算当月天数
	 * @param year
	 * @param month
	 * @return
	 */
	private int calMaxDayByYearMonth(int year,int month){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.clear();    //在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间
		calendar1.set(Calendar.YEAR,year);
		calendar1.set(Calendar.MONTH,month-1);  //Calendar对象默认一月为0
		int endday = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);//获得本月的天数
		return endday;
	}
	/**
	 * 根据day_of_week得到汉字星期
	 * @return
	 */
	public static String getDayOfWeekCN(int day_of_week){
		String result = null;
		switch(day_of_week){
		case 1:
			result = "星期日";
			break;
		case 2:
			result = "星期一";
			break;
		case 3:
			result = "星期二";
			break;
		case 4:
			result = "星期三";
			break;
		case 5:
			result = "星期四";
			break;
		case 6:
			result = "星期五";
			break;
		case 7:
			result = "星期六";
			break;	
		default:
			break;
		}
		return result;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	
}
