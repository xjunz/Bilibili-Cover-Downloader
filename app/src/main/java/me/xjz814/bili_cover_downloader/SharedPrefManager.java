package me.xjz814.bili_cover_downloader;
import android.app.*;
import android.content.*;

public class SharedPrefManager extends Application
{

	public static final String var_save_path="var_save_path";
	public static final String var_auto_save="var_auto_save";
	public static final String var_version_code="var_version_code";
	public static final String DEFAULT_SAVE_PATH="/sdcard/Pictures/哔哩哔哩封面";
	public static void setSavePath(String path){
		App.SETTING_EDITOR.putString(var_save_path,path).commit();
	}
	
	public static String getSavePath(){
		return App.SETTINGS.getString(var_save_path,DEFAULT_SAVE_PATH);
	}
	
	public static void setIsAutoSave(boolean autoSave){
		App.SETTING_EDITOR.putBoolean(var_auto_save,autoSave).commit();
	}
	
	public static boolean isAutoSave(){
	    return App.SETTINGS.getBoolean(var_auto_save,false);
	}
	
	public static boolean isUpdated(){
		int versionCode=App.SETTINGS.getInt(var_version_code,-1);
		if(versionCode<App.version_code){
			App.SETTING_EDITOR.putInt(var_version_code,App.version_code).commit();
			return true;
		}
		return false;
	}
	
}
