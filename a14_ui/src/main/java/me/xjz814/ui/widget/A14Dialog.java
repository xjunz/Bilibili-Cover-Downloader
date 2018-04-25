package me.xjz814.ui.widget;
import android.app.*;
import android.view.*;
import me.xjz814.ui.*;
import android.view.View.*;
import android.widget.*;
import me.xjz814.ui.util.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;

public class A14Dialog 
{
	private ViewGroup decor;
	private ViewGroup dialog_container,dialog_frame;
	private TextView tvTitle,tvMsg;
	private static Interpolator fosi;
	private boolean cancelable=true;
	private boolean scrimed=false;
	private Button btnPositive,btnNegative;
	private ViewGroup custom_view_container;
	private Activity activity;
    private static List<A14Dialog> dialogList=new ArrayList<A14Dialog>();
	
	public  A14Dialog(Activity act)
	{
		this.activity = act;
		if (fosi == null)
		{
			fosi = AnimationUtils.loadInterpolator(act, android.R.interpolator.fast_out_slow_in);
		}

		decor = (ViewGroup) act.getWindow().getDecorView();
		dialog_container = (ViewGroup) act.getLayoutInflater()
			.inflate(R.layout.layout_dialog, decor, false);
		dialog_frame = (ViewGroup) dialog_container.getChildAt(0);
		tvMsg = (TextView) dialog_container.findViewById(R.id.dialog_msg);
		btnPositive = (Button) dialog_container.findViewById(R.id.dialog_positive_btn);
		btnNegative = (Button) dialog_container.findViewById(R.id.dialog_negative_btn);
		tvTitle = (TextView) dialog_container.findViewById(R.id.dialog_title);
		custom_view_container = (ViewGroup) dialog_container.findViewById(R.id.dialog_msg_container);
	}


	public void performOutsideClick()
	{
		this.dialog_container.performClick();
	}


	public void show()
	{
		dialog_container.setScaleX(1.2f);
		dialog_container.setScaleY(1.2f);
		dialog_container.setAlpha(.5f);
		decor.addView(dialog_container);
		if (!scrimed)
		{
			dialog_container.setBackgroundColor(0);
		}
		dialogList.add(this);
		dialog_container.animate().alpha(1).scaleX(1).scaleY(1)
			.setInterpolator(fosi)
			.setListener(null)
			.setDuration(150)
			.start();
		dialog_container.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (cancelable)
					{
						dismiss();
					}
					else
					{
						new ObjectAnimator().ofFloat(dialog_frame, View.TRANSLATION_X, -20, 20, -10, 10, 0)
							.setDuration(300).start();	
					}
				}
			});
	}


	public void dismiss()
	{
		dialogList.remove(this);
		dialog_container.animate()
			.scaleX(1.1f)
			.scaleY(1.1f)
			.alpha(0)
			.setListener(new AnimatorListenerAdapter(){
				public void onAnimationEnd(Animator a)
				{
					decor.removeView(dialog_container);		
				}
			})
			.start();
	}


	public A14Dialog setCancelable(boolean cancelable)
	{
		this.cancelable = cancelable;
		return this;
	}

	public A14Dialog setScrimed(boolean scrimed)
	{
		this.scrimed = scrimed;
		return this;
	}
	public boolean isShown()
	{
		return dialog_container.isShown();
	}

	public A14Dialog setPositiveButton(int textRes, OnClickListener listener)
	{
		return setPositiveButton(activity.getString(textRes), listener);
	}

	public A14Dialog setNegativeButton(int textRes, OnClickListener listener)
	{
		return setNegativeButton(activity.getString(textRes), listener);
	}

	public A14Dialog setPositiveButton(String text, final OnClickListener listener)
	{
		UiUtils.visible(btnPositive);
		btnPositive.setText(text);
		btnPositive.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (listener != null)
					{
						listener.onClick(p1);
					}
					dismiss();
				}
			});
		return this;
	}

	public A14Dialog setNegativeButton(String text, final OnClickListener listener)
	{
		UiUtils.visible(btnNegative);
		btnNegative.setText(text);
		btnNegative.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (listener != null)
					{
						listener.onClick(p1);
					}
					dismiss();
				}
			});
		return this;
	}

	public A14Dialog enableDefaultNegativeButton()
	{
		return setNegativeButton(android.R.string.cancel, null);
	}

	public A14Dialog setTitle(int res)
	{
		return setTitle(activity.getString(res));
	}
	public A14Dialog setTitle(String title)
	{
		UiUtils.visible(tvTitle);
		tvTitle.setText(title);
		return this;
	}

	public A14Dialog setMsg(int res)
	{
		return setMsg(activity.getString(res));
	}
	public A14Dialog setMsg(String Msg)
	{
		UiUtils.visible(custom_view_container);
		tvMsg.setText(Msg);
		return this;
	}


	public A14Dialog setView(View view)
	{
		UiUtils.visible(custom_view_container);
		custom_view_container.removeAllViews();
		custom_view_container.addView(view, -1, -2);
		return this;
	}

	public A14Dialog setView(int res)
	{
		UiUtils.visible(custom_view_container);
		custom_view_container.removeAllViews();
		custom_view_container.addView(activity.getLayoutInflater().inflate(res, custom_view_container, false));
		return this;
	}

	public static boolean onBackPressed()
	{
        if (dialogList.size() > 0)
		{
			dialogList.get(dialogList.size() - 1).performOutsideClick();
			return true;
		}
		return false;
	}

	public static boolean dismissAt(int index)
	{
		if (dialogList.size() > index)
		{
			dialogList.get(index).dismiss();
			return true;
		}
		return false;

	}

	public static void dismissAll()
	{
		for (A14Dialog dialog:dialogList)
		{
			dialog.dismiss();
		}
	}
}

