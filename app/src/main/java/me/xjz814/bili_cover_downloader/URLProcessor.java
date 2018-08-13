package me.xjz814.bili_cover_downloader;
import android.text.*;
import java.security.*;
import java.util.regex.*;

public class URLProcessor
{
	
	public static String  processUrl(int type,String url,String id){
		if(url==null){
			return null;
		}
		switch(type){
			case ImageGetterTask.TASK_TYPE_AV:
				return "https://m.bilibili.com/video/av"+id+".html";
				//return "https://api.bilibili.com/playurl?callback=callbackfunction&aid="+id;
			case ImageGetterTask.TASK_TYPE_MEDIA:
				//目前还没有找到获取番剧封面的api
			    return "https://m.bilibili.com/bangumi/play/ep"+id;
			case ImageGetterTask.TASK_TYPE_CV:
				return "https://api.bilibili.com/x/article/viewinfo?id="+id;
			case ImageGetterTask.TASK_TYPE_VC:
				return "http://api.vc.bilibili.com/clip/v1/video/detail?video_id="+id;
			case ImageGetterTask.TASK_TYPE_USER:
				//目前还没有找到获取用户头像的api
				return "https://m.bilibili.com/space/"+id;
			case ImageGetterTask.TASK_TYPE_LIVE:
				return "http://api.live.bilibili.com/AppRoom/index?device=phone&platform=android&scale=3&room_id="+id;
			case ImageGetterTask.TASK_TYPE_AUDIO:
				return "https://m.bilibili.com/audio/au"+id;
		}
		return null;
	}
	
	
	public static String extractIDFromInput(int type,String input){
		if(input==null){
			return null;
		}
		
		if(input.matches("\\d+")){
			return input;
		}
		
		switch(type){
			case ImageGetterTask.TASK_TYPE_AV:
               //示例： http://www.bilibili.com/video/av[id]
			  return extract(input,"av(\\d+)");
			case ImageGetterTask.TASK_TYPE_MEDIA:
				//示例：http://m.bilibili.com/bangumi/play/ss[id]
               return extract(input,"ep(\\d+)");
			case ImageGetterTask.TASK_TYPE_CV:
				//示例：http://www.bilibili.com/read/cv[id]
				return extract(input,"cv(\\d+)");
			case ImageGetterTask.TASK_TYPE_VC:
				//示例：http://vc.bilibili.com/mobile/detail?vc=[id]&bilifrom=1
			    return extract(input,"vc=(\\d+)");
			case ImageGetterTask.TASK_TYPE_USER:
				//示例：http://m.bilibili.com/space/[id]
				return extract(input,"\\/(\\d+)");
			case ImageGetterTask.TASK_TYPE_LIVE:
				//示例：http://live.bilibili.com/live/[id].html
				return extract(input,"\\/(\\d+)");
			case ImageGetterTask.TASK_TYPE_AUDIO:
				//示例：https://m.bilibili.com/audio/au[id]
				return extract(input,"au(\\d+)");
		}
		return null;
	}
	
	private static String extract(String input,String regex){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(input);
		if(m.find()){
			return m.group(1);
		}
		return null;
	}
	
	public static int judgeType(String input){
		if(input.contains("/video")){
            return ImageGetterTask.TASK_TYPE_AV;
		}else if(input.contains("/read")){
            return ImageGetterTask.TASK_TYPE_CV;
		}else if(input.contains("/live")){
			return ImageGetterTask.TASK_TYPE_LIVE;
		}else if(input.contains("/bangumi")){
			return ImageGetterTask.TASK_TYPE_MEDIA;
		}else if(input.contains("/space")){
			return ImageGetterTask.TASK_TYPE_USER;
		}else if(input.contains("/vc")){
			return ImageGetterTask.TASK_TYPE_VC;
		}else if(input.contains("/au")){
			return ImageGetterTask.TASK_TYPE_AUDIO;
		}
		return ImageGetterTask.TASK_TYPE_UNKNOWN;
	}
	
	public static String getMD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
