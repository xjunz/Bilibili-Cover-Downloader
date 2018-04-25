package me.xjz814.bili_cover_downloader;
import android.graphics.*;
import android.os.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ImageGetterTask extends AsyncTask<Void,String,MediaBean>
{

	private ImageGetterListener mListener;
	private MediaBean mMediaBean;
	public static final int TASK_TYPE_UNKNOWN=-1;
	public static final int TASK_TYPE_AV=0;
	public static final int TASK_TYPE_MEDIA=1;
	public static final int TASK_TYPE_CV=2;
	public static final int TASK_TYPE_VC=3;
	public static final int TASK_TYPE_USER=4;
	public static final int TASK_TYPE_LIVE=5;

	public interface ImageGetterListener
	{
		void onBegin();
		void onGet(MediaBean bean);
		void onError(String info);
	}

	public ImageGetterTask(MediaBean bean, ImageGetterListener listener)
	{
		this.mMediaBean=bean;
		this.mListener = listener;
	}

	@Override
	protected MediaBean doInBackground(Void... p1)
	{

		String html=loadHtml(mMediaBean.implingURL);	
	    mMediaBean.coverURL= URLExtractor.extractImageUrl(mMediaBean.type, html);
		mMediaBean.coverBmap= loadBitmap(mMediaBean.coverURL);
		return mMediaBean;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mListener.onBegin();
	}

	@Override
	protected void onPostExecute(MediaBean result)
	{
		super.onPostExecute(result);
		mListener.onGet(result);
	}

	

	private byte[] fetchData(String strUrl)
	{

		try
		{
			URL url=new URL(strUrl);
			try
			{
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000);
				InputStream in= conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
                int len=0;
				byte[] data=new byte[1024];
				while ((len = in.read(data)) != -1)
				{  
					outStream.write(data, 0, len);  
				}  
				in.close();  
				conn.disconnect();
				return outStream.toByteArray();
			}
			catch (IOException e)
			{}
		}
		catch (MalformedURLException e)
		{
			//publishProgress("输入的链接格式错误：" + strUrl);
		}
		return null;
	}


	@Override
	protected void onProgressUpdate(String[] values)
	{
		mListener.onError(values[0]);
	}


	private String loadHtml(String strUrl)
	{
		byte[] bytes=fetchData(strUrl);
		return bytes == null ?null: new String(fetchData(strUrl));
	}


	private Bitmap loadBitmap(String url)
	{
		if (url == null)
		{
			return null;
		}
		byte[] bytes=fetchData(url);
		return bytes == null ?null: BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}


}
