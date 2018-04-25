package me.xjz814.bili_cover_downloader;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.view.View.*;
import me.xjz814.ui.util.*;
import me.xjz814.ui.widget.*;

public class Feedback
{
	public static final String feedbackQGroupNum="561721325";
	public static void donate(final Activity context){
	
		new A14Dialog(context).setTitle(R.string.donate)
		.setMsg(R.string.msg_donate).setPositiveButton(android.R.string.ok,new
			OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					try{
						context.startActivity(new Intent().setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=HTTPS://QR.ALIPAY.COM/FKX09948AIIIZRJRKVBD01")));
					}catch(Exception e){
						UiUtils.toast(context,"操作失败，感谢您的支持。");
					}
				}
		}).setScrimed(true).enableDefaultNegativeButton().show();
	}
	
	public static boolean feedbackAddingQGroup(Activity context){
		try{
			context.startActivity(new Intent().setData(Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin="+feedbackQGroupNum+"&card_type=group&source=qrcode")));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static void about(Activity context){
		new A14Dialog(context).setTitle(R.string.about).setMsg(String.format(context.getString( R.string.msg_about),App.version_name)).setScrimed(true).show();
	}
	
	public static void help(Activity context){
		new A14Dialog(context).setScrimed(true).setTitle(R.string.help).setMsg(R.string.msg_help).show();
	}
}
