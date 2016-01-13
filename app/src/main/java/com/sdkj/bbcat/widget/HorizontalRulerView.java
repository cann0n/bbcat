package com.sdkj.bbcat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdkj.bbcat.R;

public class HorizontalRulerView extends RelativeLayout
{
	private Context mContext;
	private LinearLayout mContentView;
	private HorizontalScrollView mHorizontalScrollView;
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
			if(mOldTouchValue == mHorizontalScrollView.getScrollX())
			{
				mCurrentValue = mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
			}
			
			else
			{
				mOldTouchValue = mHorizontalScrollView.getScrollX();
				mCurrentValue = mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize);
				if(mListener != null)
					mListener.onScroll(mCurrentValue);
				mLeaveTouchHandler.sendEmptyMessageDelayed(0, 100);
			}
		}
	};
	
	public HorizontalRulerView(Context context)
	{
		this(context,null);
	}

	public HorizontalRulerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalRulerview);
		mDirection = typedArray.getInteger(R.styleable.HorizontalRulerview_hDirections, 1);
		mMinValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hMinValue, 0);
		mMaxValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hMaxValue, 200);
		mCurrentValue = typedArray.getInteger(R.styleable.HorizontalRulerview_hCurrentValue, 60);
		mAlreadyInitView = false;
		typedArray.recycle();
	}
	
	public void initView(int parentWidth)
	{
		if(!mAlreadyInitView)
		{
			View  view = null;
			switch (mDirection) 
			{
				case 1:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtop,this,true);break;
				case 2:view = LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottom,this,true);break;
			}
			mContentView = (LinearLayout)view.findViewById(R.id.ruler_ll); 
			mHorizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.ruler_sv);
			initRuler(parentWidth);
			
			mHorizontalScrollView.setOnTouchListener(new OnTouchListener()
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
	
	private void initRuler(int parentWidth)
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
		   View leftViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   ImageView nullImg1 =  (ImageView)leftViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewt_null);
		   nullImg1.setLayoutParams(new LinearLayout.LayoutParams(parentWidth/2-bigGridSize/2,LayoutParams.MATCH_PARENT));
		   mContentView.addView(leftViewNullBegin);
		   View leftViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   TextView beginTv = (TextView)leftViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(((float)InitValue)/10+"");
		   ImageView beginImg =  (ImageView)leftViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewt_begin);
		   beginImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
		   mContentView.addView(leftViewBegin);			   
		   
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10)
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(((float)InitValue)/10+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewt_content);
			   contentImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
			   mContentView.addView(viewContent);
		   }
				
		   View rightViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   TextView endTv = (TextView)rightViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(((float)InitValue)/10+"");
		   ImageView endImg =  (ImageView)rightViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewt_end);		  
		   endImg.setLayoutParams(new LinearLayout.LayoutParams(bigGridSize,LayoutParams.MATCH_PARENT));
		   mContentView.addView(rightViewEnd);
		   View rightViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewtopitem, null);
		   ImageView nullImg2 =  (ImageView)rightViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewt_null);
		   nullImg2.setLayoutParams(new LinearLayout.LayoutParams(parentWidth/2-bigGridSize/2,LayoutParams.MATCH_PARENT));
		   mContentView.addView(rightViewNullEnd);
		}
		
		
		else/**底部布局*/
		{
		   View leftViewNullBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   ImageView nullImg1 =  (ImageView)leftViewNullBegin.findViewById(R.id.rulerview_img);
		   nullImg1.setBackgroundResource(R.drawable.rulerviewb_null);
		   LinearLayout.LayoutParams  nullParams1 = ( LinearLayout.LayoutParams)nullImg1.getLayoutParams();
		   nullParams1.width = parentWidth/2-bigGridSize/2;
		   nullImg1.setLayoutParams(nullParams1);
		   mContentView.addView(leftViewNullBegin);
		   View leftViewBegin = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   TextView beginTv = (TextView)leftViewBegin.findViewById(R.id.rulerview_tv);
		   beginTv.setVisibility(View.VISIBLE);
		   beginTv.setText(((float)InitValue)/10+"");
		   ImageView beginImg =  (ImageView)leftViewBegin.findViewById(R.id.rulerview_img);
		   beginImg.setBackgroundResource(R.drawable.rulerviewb_begin);
		   LinearLayout.LayoutParams  BeginParams = ( LinearLayout.LayoutParams)beginImg.getLayoutParams();
		   BeginParams.width = bigGridSize;
		   beginImg.setLayoutParams(BeginParams);
		   mContentView.addView(leftViewBegin);			   
		   
		   for (InitValue = InitValue + 10;InitValue < mMaxValue; InitValue = InitValue + 10)
		   {
			   View viewContent = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
			   TextView contentTv = (TextView)viewContent.findViewById(R.id.rulerview_tv);
			   contentTv.setVisibility(View.VISIBLE);
			   contentTv.setText(((float)InitValue)/10+"");
			   ImageView contentImg =  (ImageView)viewContent.findViewById(R.id.rulerview_img);
			   contentImg.setBackgroundResource(R.drawable.rulerviewb_content);
			   LinearLayout.LayoutParams  contentParams = ( LinearLayout.LayoutParams)contentImg.getLayoutParams();
			   contentParams.width = bigGridSize;
			   contentImg.setLayoutParams(contentParams);
			   mContentView.addView(viewContent);
		   }
				
		   View rightViewEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   TextView endTv = (TextView)rightViewEnd.findViewById(R.id.rulerview_tv);
		   endTv.setVisibility(View.VISIBLE);
		   endTv.setText(((float)InitValue)/10+"");
		   ImageView endImg =  (ImageView)rightViewEnd.findViewById(R.id.rulerview_img);
		   endImg.setBackgroundResource(R.drawable.rulerviewb_end);		  
		   LinearLayout.LayoutParams  endParams = (LinearLayout.LayoutParams)endImg.getLayoutParams();
		   endParams.width = bigGridSize;
		   endImg.setLayoutParams(endParams);
		   mContentView.addView(rightViewEnd);
		   View rightViewNullEnd = (View) LayoutInflater.from(mContext).inflate(R.layout.inflater_rulerviewbottomitem, null);
		   ImageView nullImg2 =  (ImageView)rightViewNullEnd.findViewById(R.id.rulerview_img);
		   nullImg2.setBackgroundResource(R.drawable.rulerviewb_null);
		   LinearLayout.LayoutParams  nullParams2 = (LinearLayout.LayoutParams)nullImg2.getLayoutParams();
		   nullParams2.width = parentWidth/2-bigGridSize/2;
		   nullImg2.setLayoutParams(nullParams2);
		   mContentView.addView(rightViewNullEnd);
		}	
		
		new Handler()
		{
			public void handleMessage(Message msg)
			{
				mHorizontalScrollView.smoothScrollTo(mCurrentValue * smallGridSize,0);
				if(mListener != null)
					mListener.onScroll(mMinValue+ (int) Math.ceil(mHorizontalScrollView.getScrollX() / smallGridSize));
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