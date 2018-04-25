package me.xjz814.ui;
import android.app.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import me.xjz814.ui.widget.*;

public abstract class BaseActivity extends Activity
{
	//重写返回键方法
	//如果拦截返回键事件，返回true
	//否则返回false
	public abstract boolean onBack();

	@Override
	public void onBackPressed()
	{
		if (!A14Dialog.onBackPressed())
		{
			if (!onBack())
			{
				super.onBackPressed();
			}
		}
	}


	public <A extends View> A findView(int id)
	{
		final A a=(A) findViewById(id);
		final CharSequence des;
		if (!TextUtils.isEmpty(des = a.getContentDescription()))
		{
			a.setOnLongClickListener(new OnLongClickListener(){
					@Override
					public boolean onLongClick(View p1)
					{
						Toast.makeText(BaseActivity.this,des,0).show();
						return true;
					}
				});
		}
		return a;
	}

}
