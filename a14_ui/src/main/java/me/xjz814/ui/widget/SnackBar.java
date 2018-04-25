package me.xjz814.ui.widget;

import android.animation.*;
import android.app.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import me.xjz814.ui.*;

import static java.lang.Math.abs;


//一个非常简单的SnackBar实现
public class SnackBar
{
	private Activity mContext;
	private ViewGroup  mContainer;
	private ViewGroup  mFrame;
	private static Interpolator fosi;
	private ViewGroup mDecorView;
	private TextView mMsg;
	private Button mAction;
	private ImageButton mDismissButton;
	private boolean shouldDismiss;
	private static final int SHOW_ANIM_DURATION=250;
	private static final int DISMISS_ANIM_DURATION=250;
	private static final int FADE_DURATION=180;
	private int showDuration=3000;
	private boolean isCancelable;
	private Callback mCallback;

	public SnackBar(Activity context)
	{   
	    mContext = context;
		if (fosi == null)
		{fosi = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);}
		mDecorView = (ViewGroup) context.findViewById(android.R.id.content);
		mFrame = (ViewGroup) LayoutInflater.from(context).inflate
		(R.layout.layout_snack_bar, mDecorView, false);
		mContainer = (ViewGroup) mFrame.getChildAt(0);
		mMsg = (TextView) mContainer.findViewById(R.id.layout_snack_bar_text);
		mAction = (Button) mContainer.findViewById(R.id.layout_snack_bar_action);
		mDismissButton = (ImageButton) mContainer.findViewById(R.id.layout_snack_bar_dismiss);
		shouldDismiss = true;
		setCancelable(true);
	}



	public static SnackBar build(Activity context, int resText, int resActionText)
	{   
		SnackBar sb=new SnackBar(context);
		sb.setMessage(resText);
	    sb.setAction(resActionText, null);
		sb.setCancelable(true);
		return sb;
	}


	public SnackBar setMessage(int res)
	{
		mMsg.setText(res);
		return this;
	}



	public SnackBar setAction(int res, final View.OnClickListener listener)
	{   
	    mAction.setVisibility(View.VISIBLE);
		mAction.setText(res);
		mAction.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mAction.setEnabled(false);
					if (listener != null)
					{
						listener.onClick(p1);
					}
					dismiss();
				}
			});
		return this;
	}

	public SnackBar setCancelable(boolean cancelable)
	{
		isCancelable = cancelable;
		if (!cancelable)
		{
			mDismissButton.setVisibility(View.GONE);

		}
		else
		{
			mDismissButton.setVisibility(View.VISIBLE);
			mDismissButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						dismiss();
					}
				});
			mDismissButton.setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(View p1)
					{
						Toast.makeText(mContext, p1.getContentDescription(), 0).show();
						return true;
					}
				});
		}
		return this;
	}


    private void rawShow()
	{
		mDecorView.addView(mFrame, -1, -1);
		mContainer.post(new Runnable(){
				@Override
				public void run()
				{    
					final int height=mContainer.getHeight();
					mContainer.setVisibility(View.VISIBLE);
					ValueAnimator va=new ValueAnimator().ofFloat(height, 0);
					va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
							@Override
							public void onAnimationUpdate(ValueAnimator p1)
							{
								float value=p1.getAnimatedValue();
								mContainer.setTranslationY(value);
								if (mCallback != null)
								{
									mCallback.onAnimate(SnackBar.this, value - height, -height);
								}
							}
						});
					va.addListener(new AnimatorListenerAdapter(){
						@Override
							public void onAnimationStart(Animator p1)
							{
								for (int i=0;i < mContainer.getChildCount();i++)
								{
									View view=mContainer.getChildAt(i);
									view.setAlpha(0);
									view.animate().alpha(1f).setDuration(FADE_DURATION).setStartDelay(SHOW_ANIM_DURATION).start();
								}
							}
							@Override
							public void onAnimationEnd(Animator p1)
							{
								if (mCallback != null)
								{
									mCallback.onShow(SnackBar.this);	
								}

							}
						});
					va.setInterpolator(fosi);
					va.setDuration(SHOW_ANIM_DURATION);
					va.start();

				}
			});
	}
	public void show()
	{   
	    rawShow();
		indefiniteDismiss(showDuration);
	}

	public static int DURATION_INFINATE=-1;

	public void show(int duration)
	{
		rawShow();
		if (duration == DURATION_INFINATE)
		{return;}
		indefiniteDismiss(duration);
	}

	//无期限关闭（当用户手指按住SnackBar时(不包括按钮)，不关闭)
	private void indefiniteDismiss(final int duration)
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(duration);
					}
					catch (InterruptedException e)
					{}
					mContext.runOnUiThread(new Runnable(){
							@Override
							public void run()
							{
								//判断是否被按压
								if (!(mContainer.isPressed() || mAction.isPressed() || mDismissButton.isPressed()))
								{
									//确保未退出应用
									if (mFrame.getParent() != null)
									{   
										dismiss();
									}
								}
								else
								{ 
									indefiniteDismiss(duration);
								}
							}
						});
				}
			}).start();

	}

    private boolean isDismissing;

	public void dismiss()
	{         
		//保证即使连续调用dismiss(),动画也能完整执行
		if (!isDismissing)
		{
			isDismissing = true;
			final int height=mContainer.getHeight();
			ValueAnimator va=new ValueAnimator().ofFloat(0, height);
			va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

					@Override
					public void onAnimationUpdate(ValueAnimator p1)
					{
						float value=p1.getAnimatedValue();
						mContainer.setTranslationY(value);
						if (mCallback != null)
						{
							mCallback.onAnimate(SnackBar.this, value - height, height);
						}
					}
				});
			va.addListener(new AnimatorListenerAdapter(){
					@Override
					public void onAnimationStart(Animator p1)
					{
						for (int i=0;i < mContainer.getChildCount();i++)
						{
							View view=mContainer.getChildAt(i);
							view.setAlpha(1f);
							view.animate().alpha(0).setStartDelay(0).setDuration(FADE_DURATION).start();
						}
					}
					@Override
					public void onAnimationEnd(Animator p1)
					{
						if (mFrame.getParent() != null)
						{
							mDecorView.removeView(mFrame);
							if (mCallback != null)
							{
								mCallback.onDismiss(SnackBar.this);
							}
							isDismissing = false;
						}
					}
				});
			va.setInterpolator(fosi);
			va.setDuration(DISMISS_ANIM_DURATION);
			va.start();


		}
	}



	public boolean isShown()
	{
		return mContainer.isShown();
	}

	public interface Callback
	{
		void onShow(SnackBar snackbar);
		void onAnimate(SnackBar snackbar, float transY, float transDistance);
		void onDismiss(SnackBar snackbar);
	}

	public SnackBar setCallback(Callback cb)
	{
		mCallback = cb;
		return this;
	}

}
