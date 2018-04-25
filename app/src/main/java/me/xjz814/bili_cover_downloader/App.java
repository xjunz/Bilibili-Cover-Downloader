package me.xjz814.bili_cover_downloader;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;

public class App extends Application
{

	public static SharedPreferences SETTINGS;
	public static SharedPreferences.Editor SETTING_EDITOR;
	public static String version_name;
	public static int version_code;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		SETTINGS=getSharedPreferences("config",MODE_PRIVATE);
		SETTING_EDITOR=SETTINGS.edit();
		try
		{
			 PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), 0);
			 version_name= pi.versionName;	
			 version_code=pi.versionCode;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			
		}
	}
	
}
