package com.sdkj.bbcat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sdkj.bbcat.R;

public class VerticalRulerView extends RelativeLayout
{
	private Context mContext;
	private ScrollView mScrollView;
	private LinearLayout mContentView;
	private Integer mDirection;
	private Integer mMinValue;
	private Integer mMaxValue;
	private Integer mCurrentValue;
	private Boolean mAlreadyInitView;
	
	private int mOldTouchValue = 0;
	private final int bigGridSize = 200;
	private final int smallGridSize = bigGridSize / 10;
	private onScrollListener mListener;
	
	private Handler mLeaveTouchHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if(mOldTouchValue == mScrollView.getScrollY())
			{
				mCurrentValue = mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
			}
			
			else
			{
				mOldTouchValue = mScrollView.getScrollY();
				mCurrentValue = mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
				mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);
			}
		}
	};

	public VerticalRulerView(Context context)
	{
		this(context,null);
	}

	public VerticalRulerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalRulerview);
		mDirection = typedArray.getInteger(R.styleable.VerticalRulerview_vDirections, 1);
		mMinValue = typedArray.getInteger(R.styleable.VerticalRulerview_vMinValue, 0);
		mMaxValue = typedArray.getInteger(R.styleable.VerticalRulerview_vMaxValue, 200);
		mCurrentValue = typedArray.getInteger(R.styleable.VerticalRulerview_vCurrentValue, 60);
		mAlreadyInitView = false;
		typedArray.recycle();
	}

	public void initView(int parentHeight)
	{
		if(!mAlreadyInitView)
		{
			View  view = null;
			switch (mDirection) 
			{
				case 1:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleft,this,true);break;
				case 2:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewright,this,true);break;
			}
			mScrollView = (ScrollView)view.findViewById(R.id.ruler_sv);
			mContentView = (LinearLayout)view.findViewById(R.id.ruler_ll); 
			initRuler(parentHeight);
			
			mScrollView.setOnTouchListener(new OnTouchListener()
			{
				public boolean onTouch(View v, MotionEvent event)
				{
					int action = event.getAction();
					switch (action) 
					{
						case MotionEvent.ACTION_DOWN:
						case MotionEvent.ACTION_MOVE:
						case MotionEvent.ACTION_UP:
						mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);break;
					}return false;
				}
			});
			mAlreadyInitView = true;
		}
	}
	
	private void initRuler(int parentHeight)
	{
		mMinValue = mMinValue/10*10;
		mMaxValue = mMaxValue%10 > 0 ? mMaxValue/10*10+10 : mMaxValue/10*10;
		if(mMinValue >= mMaxValue)
			mMaxValue = mMinValue+10;
		if(mCurrentValue < mMinValue)
			mCurrentValue = 0;
		else if(mCurrentValue > mMaxValue)
			mCurrentValue = mMaxValue - mMinValue;
		else
			mCurrentValue = mCurrentValue - mMinValue;
		
		int InitValue = mMinValue;
		if(mDirection == 1)
		{
		   View topViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   ImageView nullImg1 =  (ImageView)topViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewl_null);
		   nullImg1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,parentHeight/2-bigGridSize/2));
		   mContentView.addView(topViewNullBegin);
		   View topViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   TextView beginTv = (TextView)topViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(((float)InitValue)/10+"");
		   ImageView beginImg =  (ImageView)topViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewl_begin);
		   beginImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
		   mContentView.addView(topViewBegin);			   
		   
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10)
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(((float)InitValue)/10+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewl_content);
			   contentImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
			   mContentView.addView(viewContent);
		   }
				
		   View bottomViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   TextView endTv = (TextView)bottomViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(((float)InitValue)/10+"");
		   ImageView endImg =  (ImageView)bottomViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewl_end);		  
		   endImg.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,bigGridSize));
		   mContentView.addView(bottomViewEnd);
		   View bottomViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewleftitem, null);
		   ImageView nullImg2 =  (ImageView)bottomViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewl_null);
		   nullImg2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,parentHeight/2-bigGridSize/2));
		   mContentView.addView(bottomViewNullEnd);
		}
		
		
		else
		{
		   View topViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   ImageView nullImg1 =  (ImageView)topViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewr_null);
		   LinearLayout.LayoutParams  nullParams1 = (LinearLayout.LayoutParams)nullImg1.getLayoutParams();
		   nullParams1.height = parentHeight/2-bigGridSize/2;
		   nullImg1.setLayoutParams(nullParams1);
		   mContentView.addView(topViewNullBegin);
		   View topViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   TextView beginTv = (TextView)topViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(((float)InitValue)/10+"");
		   ImageView beginImg =  (ImageView)topViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewr_begin);
		   LinearLayout.LayoutParams  BeginParams = (LinearLayout.LayoutParams)beginImg.getLayoutParams();
		   BeginParams.height = bigGridSize;
		   beginImg.setLayoutParams(BeginParams);
		   mContentView.addView(topViewBegin);			   
		   
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10)
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(((float)InitValue)/10+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewr_content);
			   LinearLayout.LayoutParams  contentParams = (LinearLayout.LayoutParams)contentImg.getLayoutParams();
			   contentParams.height = bigGridSize;
			   contentImg.setLayoutParams(contentParams);
			   mContentView.addView(viewContent);
		   }
				
		   View bottomViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   TextView endTv = (TextView)bottomViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(((float)InitValue)/10+"");
		   ImageView endImg =  (ImageView)bottomViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewr_end);		  
		   LinearLayout.LayoutParams  endParams = (LinearLayout.LayoutParams)endImg.getLayoutParams();
		   endParams.height = bigGridSize;
		   endImg.setLayoutParams(endParams);
		   mContentView.addView(bottomViewEnd);
		   View bottomViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewrightitem, null);
		   ImageView nullImg2 =  (ImageView)bottomViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewr_null);
		   LinearLayout.LayoutParams  nullParams2 = (LinearLayout.LayoutParams)nullImg2.getLayoutParams();
		   nullParams2.height = parentHeight/2-bigGridSize/2;
		   nullImg2.setLayoutParams(nullParams2);
		   mContentView.addView(bottomViewNullEnd);
		}	
		
		new Handler()
		{
			public void handleMessage(Message msg) 
			{
				mScrollView.smoothScrollTo(0, mCurrentValue * smallGridSize);
				if(mListener != null)
					mListener.onScroll(mMinValue+ (int) Math.ceil(mScrollView.getScrollY() / smallGridSize));
			}
		}.sendEmptyMessageDelayed(0, 200);
	}
	
	public interface onScrollListener
	{
		public void onScroll(int currentValue);
	}
	
	public void setOnScrollListener(onScrollListener listener)
	{
		mListener = listener;
	}
}