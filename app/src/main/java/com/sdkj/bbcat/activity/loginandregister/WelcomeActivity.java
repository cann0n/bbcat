package com.sdkj.bbcat.activity.loginandregister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sdkj.bbcat.R;
import com.sdkj.bbcat.constValue.Const;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask()
        {
            public void run()
            {
				SharedPreferences sharedPreferences = getSharedPreferences(Const.SPNAME, Context.MODE_PRIVATE);
				if(sharedPreferences.getBoolean(Const.SPNAME_FIRSTINSTALL, true))//第一次安装
				{
					sharedPreferences.edit().putBoolean(Const.SPNAME_FIRSTINSTALL, false).commit();
					Intent intent = new Intent(WelcomeActivity.this,FirstInstallActivity.class);
					startActivity(intent);
					finish();
				}

				else//已经安装过
				{
					Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
		timer.schedule(timerTask, 3000);
	}
}