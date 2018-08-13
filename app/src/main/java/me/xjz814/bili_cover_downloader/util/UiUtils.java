package me.xjz814.bili_cover_downloader.util;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import me.xjz814.bili_cover_downloader.*;

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
	
	public static AlertDialog.Builder createTextAutoLinkDialog(Activity context,String str){
		TextView autoLinked=(TextView) context.getLayoutInflater().inflate(R.layout.layout_linked_textview,null,false);
		autoLinked.setText(str);
		return new AlertDialog.Builder(context).setView(autoLinked);
	}
	

	public  static AlertDialog.Builder createTextAutoLinkDialog(Activity context,int strRes){
		return createTextAutoLinkDialog(context,context.getString(strRes));
	}
	
	
	public static void toast(Context context,CharSequence str)
	{
		Toast.makeText(context, str, 0).show();
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
