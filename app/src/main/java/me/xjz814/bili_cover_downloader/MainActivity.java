package me.xjz814.bili_cover_downloader;

import android.*;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.nio.channels.*;
import java.util.regex.*;
import me.xjz814.bili_cover_downloader.util.*;

import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;


public class MainActivity extends Activity implements ImageGetterTask.ImageGetterListener
{
	private ImageView ivImagePreview,ivEmptyView;
	private Toolbar mToolbar;
    private Spinner spType;
	private ProgressBar pbProcess;
	private static String[] mStrTypes,mStrHints;
	private  static int[] mIcons;
	private TextView tvHint;
	private CheckedTextView ctvAutoSave;
	private EditText etSavePath;
	private static android.content.ClipboardManager clipManager;
	private ImageButton ibGet;
	private Bitmap mCoverImage;
	private MediaBean mBean=new MediaBean();
	private static String CACHE_PATH;

	

	private EditText etInput;
	private boolean shouldDetectInput;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
			== PackageManager.PERMISSION_DENIED)
		{   
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		}

        setContentView(R.layout.main);
		initViews();
		CACHE_PATH = getExternalCacheDir().getPath();
		mStrTypes = getResources().getStringArray(R.array.types);
		mStrHints = getResources().getStringArray(R.array.hints);
		ibGet = findViewById(R.id.fab_get);
		TypedArray ar = getResources().obtainTypedArray(R.array.icons);
		final int len = ar.length();
		mIcons = new int[len];
		for (int i = 0; i < len; i++)
		{
			mIcons[i] = ar.getResourceId(i, 0);
		}
		ar.recycle();
		spType.setAdapter(new SpinnerTypeAdapter());
		clipManager = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		if(SharedPrefManager.isUpdated()){
			Feedback.help(this);
			Feedback.about(this);
		}
		
		Intent i=getIntent();
		shouldDetectInput = true;
		if (i != null)
		{
			String input=i.getStringExtra(Intent.EXTRA_TEXT);
			if (TextUtils.isEmpty(input))
			{
				return;
			}
			shouldDetectInput = false;
			Pattern p=Pattern.compile("(http.+)");
			Matcher m=p.matcher(input);
			if (m.find())
			{
				input = m.group(1);
			}
			etInput.setText(input);
			ibGet.performClick();
		}
		


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUSET_PATH && resultCode == RESULT_OK)
		{
			if (data != null)
			{
				String path;
				if (!TextUtils.isEmpty(path = data.getStringExtra(FileChooserActivity.PATH_RETURNED)))
				{
					SharedPrefManager.setSavePath(path);
					etSavePath.setText(path);
				}
			}
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
		if (grantResults[0] == PackageManager.PERMISSION_DENIED)
		{
			new AlertDialog.Builder(this).setTitle("错误").setMessage("请授予本应用读写内部储存权限，否则应用无法正常运行")
				.setPositiveButton("重试", new DialogInterface. OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
					}
					
				}).setNegativeButton("退出", new DialogInterface. OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						finish();
					}
					
				})
				.show();
		}else{
			File defSaveDir=new File(SharedPrefManager.DEFAULT_SAVE_PATH);
			if(!defSaveDir.exists()){
				defSaveDir.mkdirs();
			}
		}
	}

	private String clipboardText;
	@Override
	protected void onResume()
	{

		super.onResume();

		if (!shouldDetectInput)
		{
			return;
		}
		CharSequence text=clipManager.getText();

		if (text == null)
		{
			return;
		}
		if (text.equals(clipboardText))
		{
			return;
		}
     // android.R.style.TextAppearance_Material_Subhead
	    clipboardText = text.toString();
		int type=URLProcessor.judgeType(clipboardText);
		if (type != ImageGetterTask.TASK_TYPE_UNKNOWN)
		{
			spType.setSelection(type);
			UiUtils.createTextAutoLinkDialog(this,String.format(getString(R.string.msg_available_url_detected), clipboardText, mStrTypes[type]))
				.setTitle(R.string.prompt)
				.setPositiveButton(R.string.get_cover, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						etInput.setText(clipboardText);
						etInput.setSelection(0, clipboardText.length());
						ibGet.performClick();
					}
					
				}).setNegativeButton(android.R.string.cancel,null).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	private View flSetting,mScrim;

	public void dismissSetting(View view)
	{
		if (view.getAlpha() != 0)
		{
			findViewById(R.id.cog).performClick();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.cog:
				UiUtils.visible(flSetting, mScrim);
				float endTransY=0;
				final int flHeight=flSetting.getHeight();
				if (flSetting.getTranslationY() == 0)
				{
					endTransY = -flHeight;
				}
				else
				{

				}
				final float end=endTransY;
				ValueAnimator va=new ValueAnimator().ofFloat(flSetting.getTranslationY(), endTransY);
				va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

						@Override
						public void onAnimationUpdate(ValueAnimator p1)
						{
							float f=p1.getAnimatedValue();
							flSetting.setTranslationY(f);
							mScrim.setAlpha((flSetting.getHeight() + f) / flHeight);
							if (end != 0 && p1.getAnimatedFraction() > .996)
							{
								UiUtils.invisible(flSetting, mScrim);

							}
						}
					});
				va.addListener(new AnimatorListenerAdapter(){

						@Override
						public void onAnimationStart(Animator p1)
						{
							ibGet.animate().scaleX(0).scaleY(0).setInterpolator(new AnticipateInterpolator())
								.setListener(new AnimatorListenerAdapter(){
									@Override
									public void onAnimationEnd(Animator p1)
									{

										ibGet.setTranslationY(flSetting.getBottom() - ibGet.getHeight() / 2 - ibGet.getY());
										ibGet.animate().scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator()).setListener(null).start();
									}
								}).start();
						}




					});
				va.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in));
				va.start();
				break;
			case R.id.quit:
				System.exit(0);
				break;
			case R.id.donate:
			    Feedback.donate(this);
				break;
			case R.id.feedback:
				if(!Feedback.feedbackAddingQGroup(this)){
					toast("操作失败，反馈方式已复制到剪贴板");
					copyToClipboard("QQ群号码："+Feedback.feedbackQGroupNum);
				}
				break;
			case R.id.about:
				Feedback.about(this);
				break;
			case R.id.help:
				Feedback.help(this);
				break;
		}
		return super.onOptionsItemSelected(item);
	}





	private int mType;

	private void initViews()
	{
		mToolbar = findViewById(R.id.toolbar);
		setActionBar(mToolbar);
	    spType = findViewById(R.id.sp_type);
		etInput = findViewById(R.id.et_input);
		tvHint = findViewById(R.id.tv_hint);
		etSavePath = findViewById(R.id.et_download_path);
		etSavePath.setText(SharedPrefManager.getSavePath());
		pbProcess = findViewById(R.id.pb_process);
		ivImagePreview = findViewById(R.id.iv_image_preview);
		ivEmptyView = findViewById(R.id.iv_empty_view);
		ctvAutoSave=findViewById(R.id.ctv_auto_save);
		ctvAutoSave.setChecked(SharedPrefManager.isAutoSave());
		flSetting = findViewById(R.id.fl_setting);
		flSetting.post(new Runnable(){

				@Override
				public void run()
				{
					flSetting.setTranslationY(-flSetting.getHeight());
				}
			});

		mScrim = findViewById(R.id.view_scrim);
		spType.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					tvHint.setText(mStrHints[p3]);
					mType = p3;
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{

				}
			});
		etInput.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{

				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{

				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					int type=URLProcessor.judgeType(p1.toString());
					if (type != ImageGetterTask.TASK_TYPE_UNKNOWN)
					{
						mType = type;
						spType.setSelection(type);
					}
				}
			});
		etInput.setOnKeyListener(new OnKeyListener(){

				@Override
				public boolean onKey(View p1, int p2, KeyEvent p3)
				{

					if (p2 == p3.KEYCODE_ENTER && p3.getAction() == p3.ACTION_UP)
					{
						ibGet.performClick();
					}
					return false;
				}
			});
	}

	private void saveBitmapAsPNG(String path)
	{
		try
		{
			FileOutputStream fos=new FileOutputStream(path);
			mCoverImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			try
			{
				fos.flush();
				fos.close();
			}
			catch (IOException e)
			{}
		}
		catch (FileNotFoundException e)
		{}

	}


	@Override
	public void onBegin()
	{
		pbProcess.animate().alpha(1f).start();
	}



	@Override
	public void onGet(MediaBean bean)
	{
		Bitmap bmap=bean.coverBmap;
		bean. filename=URLProcessor.getMD5(bean.coverURL) + ".png";
	    bean.cachePath = CACHE_PATH + "/" + bean.filename;
		if (bmap != null)
		{
			ivEmptyView.animate().alpha(0).start();
			ivImagePreview.animate().alpha(1).start();
			this.mCoverImage = bmap;
			saveBitmapAsPNG(bean.cachePath);
			ivImagePreview.setImageBitmap(bmap);	
			//妖王彩蛋
			if (bean.type == ImageGetterTask.TASK_TYPE_AV && bean.id.equals("170001"))
			{
				toast("Lover～我已迷上门口的老头～");
			}
			
				if (SharedPrefManager.isAutoSave())
				{
					findViewById(R.id.btn_save).performClick();
					}else{
						toast("完成");
					}
		}
		
		else
		{
			toast("未获取到图片");
			ImeUtils.showIme(etInput);
		}

		pbProcess.animate().alpha(0f).start();
	}

	@Override
	public void onError(String info)
	{
		toast(info);
	}

	public void viewImage(View view)
	{
		if (ivEmptyView.getAlpha() == 1)
		{
			return;
		}
		ActivityOptions ops=  ActivityOptions.makeSceneTransitionAnimation(this, view, getString(R.string.transition_img));
		Intent i=new Intent(this, ImageViewerActivity.class);
		//不直接传递bitmap，因为过大的bitmap会导致错误，所以我们传递图片的缓存路径
		i.putExtra("source_img_path", mBean.cachePath);
		startActivity(i, ops.toBundle());
	}

	public void shareCover(View view){
		try{
		File file=new File(mBean.cachePath);
		Uri photoURI = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", file);
		Intent i=new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_STREAM, photoURI);
		i.setType("image/*");
		i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Intent chooser=Intent.createChooser(i,"分享到");
			
		startActivity(chooser);
		}catch(Exception e){
			toast("分享失败");
			e.printStackTrace();
		}
	}
	public void clearInput(View voew){
		etInput.getText().clear();
	}
	
	public void saveToStorage(View view){
		try
		{	
				FileChannel inChannel =new FileInputStream(mBean.cachePath).getChannel();  
				FileChannel outChannel=new FileOutputStream(SharedPrefManager.getSavePath() + "/" + mBean.filename).getChannel();  			
				inChannel.transferTo(0, inChannel.size(), outChannel);			
				inChannel.close();  
				outChannel.close(); 
				toast("已保存到本地！");			
		}
		catch (Exception e)
		{
              toast("保存失败");
		}
	}
	
	
	public void getImage(View view)
	{
		if (etInput.getText().toString().trim().equals(""))
		{
			//toast("");
			return;
		}
		ImeUtils.hideIme(view);
		mBean.type = mType;
		mBean.orginalURL = etInput.getText().toString().replaceFirst("http:", "https:");
		mBean.id = URLProcessor.extractIDFromInput(mBean.type, mBean.orginalURL);
		mBean.implingURL = URLProcessor.processUrl(mBean.type, mBean.orginalURL, mBean.id);
		new ImageGetterTask(mBean, this).execute();
	}

	public void setAutoSave(View view)
	{
		CheckedTextView ctv=(CheckedTextView) view;
		SharedPrefManager.setIsAutoSave(!ctv.isChecked());
	    ctv.setChecked(!ctv.isChecked());

	}

	public void clearCache(View view)
	{
		
		new AlertDialog.Builder(this).setTitle("清除缓存").setMessage("你确定要清除历史图片缓存吗？").setPositiveButton(android.R.string.ok
			, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				
					try
					{
						ivImagePreview.setImageBitmap(null);
						for (File i:new File(CACHE_PATH).listFiles())
						{
							i.delete();
						}toast("完成！");
					}
					catch (Exception E)
					{
						toast("失败！");
					}

				}
			}).show();

	}

	public void showInfo(View view)
	{
		try
		{
			final String info="下载地址：" + mBean.coverURL + "\n" +
				"地址来源：" + mBean.implingURL + "\n"
				+ "类型：" + mStrTypes[mBean.type] +"（id:"+ mBean.id+"）" + "\n"
				+ "体积：" + new File(mBean.cachePath).length() / 1024 + "kb" + "\n"
				+ "规格：" + mBean.coverBmap.getWidth() + "x" + mBean.coverBmap.getHeight() + "\n"
				+ "缓存位置：" + mBean.cachePath;

			UiUtils.createTextAutoLinkDialog(this,info).setTitle("详情")
				.setPositiveButton(android.R.string.copy, new DialogInterface.
				OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						copyToClipboard(info);
						toast("复制完成");
					}
				})
				.show();
		}
		catch (Exception e)
		{
			toast("未取得信息");
		}
	}

	public static final int REQUSET_PATH=1;
	public void selectDownloadFolder(View view)
	{
		startActivityForResult(new Intent(this, FileChooserActivity.class), REQUSET_PATH);
		//new A14Dialog(this).setView(R.layout.main).show();
	}

	private class SpinnerTypeAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mStrTypes.length;
		}

		@Override
		public Object getItem(int p1)
		{

			return null;
		}

		@Override
		public long getItemId(int p1)
		{

			return 0;
		}

		@Override
		public View getView(int p1, View p2, ViewGroup p3)
		{
			ViewGroup layoutType=(ViewGroup) getLayoutInflater().inflate(R.layout.layout_spinner_type_item, null, false);
			((ImageView)layoutType.findViewById(R.id.iv_type)).setImageResource(mIcons[p1]);
			((TextView)layoutType.findViewById(R.id.tv_type)).setText(mStrTypes[p1]);
			return layoutType;
		}


	}

	private void toast(String obj)
	{
		if (obj == null)
		{obj = "null";}
		Toast.makeText(this, obj.toString(), 0).show();
	}
	private void toast(int  res)
	{
		Toast.makeText(this, res, 0).show();
	}
	
	public  void copyToClipboard(String str)
	{
		clipManager.setText(str);
	}



}
