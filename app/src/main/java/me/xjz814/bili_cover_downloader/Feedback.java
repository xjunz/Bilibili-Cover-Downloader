package me.xjz814.bili_cover_downloader;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.view.View.*;

import android.widget.*;
import me.xjz814.bili_cover_downloader.util.*;

public class Feedback
{
	public static final String feedbackQGroupNum="561721325";
	public static void donate(final Activity context){
	
		new AlertDialog.Builder(context).setTitle(R.string.donate)
		.setMessage(R.string.msg_donate).setPositiveButton("捐赠",new
			DialogInterface. OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try{
						context.startActivity(new Intent().setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=HTTPS://QR.ALIPAY.COM/FKX072408K3WBUTRJWNL16")));
					}catch(Exception e){
						UiUtils.toast(context,"操作失败，感谢您的支持。");
					}
				}
			}).setNegativeButton("赏金", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try{
						context.startActivity(new Intent().setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/c1x05124iixwlk46tv3q180")));
					}catch(Exception e){
						UiUtils.toast(context,"操作失败，感谢您的支持。");
					}
				}
				
			
		}).show();
	}
	
	public static boolean feedbackAddingQGroup(Activity context){
		try{
			context.startActivity(new Intent().setData(Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin="+feedbackQGroupNum+"&card_type=group&source=qrcode")));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static void about(final Activity context){

		new AlertDialog.Builder(context).setTitle(R.string.about).setPositiveButton(R.string.about_os, new DialogInterface. OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					UiUtils.createTextAutoLinkDialog(context,R.string.msg_about_os).setTitle(R.string.about_os).show();
				}
				
			
			})
			.setMessage(String.format(context.getString(R.string.msg_about), App.version_name)).show();
	}
	
	public static void help(Activity context){
		new AlertDialog.Builder(context).setTitle(R.string.help).setMessage(R.string.msg_help).show();
	}
}
