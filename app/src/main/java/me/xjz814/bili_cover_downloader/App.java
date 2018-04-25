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
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		SETTINGS=getSharedPreferences("config",MODE_PRIVATE);
		SETTING_EDITOR=SETTINGS.edit();
		try
		{
			 version_name= getPackageManager().getPackageInfo(getPackageName(), 0).versionName;	
		}
		catch (PackageManager.NameNotFoundException e)
		{
			
		}
	}
	
}
