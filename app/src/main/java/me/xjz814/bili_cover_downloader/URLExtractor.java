package me.xjz814.bili_cover_downloader;
import java.util.regex.*;

public class URLExtractor
{
	
	public static String extractImageUrl(int type,String html){
		if(html==null){
			return null;
		}
		//通过api返回的是json数据，但是我们只需要获得图片链接，
		//就不用解析json，直接正则表达式匹配（通过解析json可以获得更多额外的信息）
		//没有api的孩子，只能从html中硬匹配链接
		Pattern p;
		Matcher m;
		switch(type){
			case ImageGetterTask.TASK_TYPE_AV:
				p=Pattern.compile("\"og:image\" content=\"([^\"]*)\"");
				 m=p.matcher(html);
				if(m.find()){
					return m.group(1).replace("@400w_300h.jpg","");
				}
				break;
			case ImageGetterTask.TASK_TYPE_MEDIA:
				p=Pattern.compile("\"cover\":\"([^\"]*)\"");
				m=p.matcher(html);
				if(m.find()){
					return m.group(1).replace("u002F","").replace("\\","/");
				}
				break;
			case ImageGetterTask.TASK_TYPE_CV:
				p=Pattern.compile("\"banner_url\":\"([^\"]*)\"");
			    m=p.matcher(html);
				if(m.find()){
					return m.group(1);
				}
				break;
			case ImageGetterTask.TASK_TYPE_VC:
				p=Pattern.compile("\"first_pic\":\"([^\"]*)\"");
				m=p.matcher(html);
				if(m.find()){
					return m.group(1);
				}
				break;
			case ImageGetterTask.TASK_TYPE_USER:
				 p=Pattern.compile("face\\\":\\\"([^\"]*)\\\"");
				m=p.matcher(html);
				if(m.find()){
					return m.group(1);
				}
				break;
			case ImageGetterTask.TASK_TYPE_LIVE:
				p=Pattern.compile("\"cover\":\"([^\"]*)\"");
				m=p.matcher(html);
				if(m.find()){
					return m.group(1).replace("\\","");
				}
		
				break;
			case ImageGetterTask.TASK_TYPE_AUDIO:
				p=Pattern.compile("\"cover_url\":\"([^\"]*)\"");
				m=p.matcher(html);
				if(m.find()){
					return m.group(1).replace("\\","");
				}
				
				break;
		}
		return null;
	}
	
}
