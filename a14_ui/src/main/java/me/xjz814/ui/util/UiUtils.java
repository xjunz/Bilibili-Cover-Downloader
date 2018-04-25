package me.xjz814.ui.util;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;

public class UiUtils
{
    private static Interpolator linear_out_slow_in;
	private static Interpolator fast_out_slow_in;

	
	
	public static Interpolator getLinearOutSlowIn(Context context)
	{
		return linear_out_slow_in == null
		?(linear_out_slow_in = AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in))
		: linear_out_slow_in;
	}
	public static Interpolator getFastOutSlowIn(Context context)
	{
		return fast_out_slow_in == null
			?(fast_out_slow_in = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in))
			: fast_out_slow_in;
	}
	

	//通过在图片旋转过程中切换图片营造过渡效果
	public static void windmillTrick(Context context,final ImageView v, final int imageRes, int rotation)
	{
		v.animate().rotation(rotation).setDuration(200)
			.setInterpolator(getLinearOutSlowIn(context))
			.start();
		v.postDelayed(new Runnable(){
				@Override
				public void run()
				{
					v.setImageResource(imageRes);
				}
			}, 50);
	}

	public static void toast(Context context,CharSequence str)
	{
		Toast.makeText(context, str, 0).show();
	}

	

	
	

	public static int dip2px(Context context,int dipValue)
	{
		return (int)(dipValue * context.getResources().getDisplayMetrics().density + .5);
	}


	public static void visible(View...views)
	{
		for (View v:views)
		{
			v.setVisibility(v.VISIBLE);
		}
	}
	public static void invisible(View...views)
	{
		for (View v:views)
		{
			v.setVisibility(v.INVISIBLE);
		}
	}
	public static void disable(View...views)
	{
		for (View v:views)
		{
			v.setEnabled(false);
		}
	}

	public static void enable(View...views)
	{
		for (View v:views)
		{
			v.setEnabled(true);
		}
	}

	public static void gone(View...views)
	{
		for (View v:views)
		{
			v.setVisibility(v.GONE);
		}
	}


	

}
