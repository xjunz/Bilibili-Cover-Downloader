package me.xjz814.bili_cover_downloader;
import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.content.*;
import android.transition.*;
import android.view.*;
import android.widget.*;
import com.bm.library.*;
import java.io.*;
import me.xjz814.ui.*;

public class ImageViewerActivity extends BaseActivity
{

	private Toolbar toolbar;
	private PhotoView mPhotoView;
	private Bitmap mCoverImage;
	private View mBack;
	private String mCoverImagePath;
	@Override
	public boolean onBack()
	{
		return false;
	}
	private int mColorLight,mColorDark;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);
		mPhotoView = findView(R.id.pv_img);
		mBack = findView(R.id.rl_back);
		mColorLight = getColor(android.R.color.background_light);
		mColorDark = getColor(android.R.color.background_dark);
		curBackgroundColor = mColorLight;
		Intent i=getIntent();
		mCoverImagePath = i.getStringExtra("source_img_path");
		mCoverImage=BitmapFactory.decodeFile(mCoverImagePath);
		mPhotoView.enable();
		TransitionSet set=new TransitionSet();
		set.addTransition(new ChangeBounds());
	    set.addTransition(new ChangeClipBounds());
		set.addTransition(new ChangeImageTransform());
		mPhotoView.setImageBitmap(mCoverImage);
		getWindow().setSharedElementEnterTransition(set);
		toolbar = findView(R.id.toolbar_image_viewer);
		setActionBar(toolbar);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.viewer_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private int curBackgroundColor;
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.share:
				File file=new File(mCoverImagePath);
				Uri photoURI = FileProvider.getUriForFile(this, "me.a14xjz.biligetter.fileprovider", file);
				Intent i=new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_STREAM, photoURI);
				i.setType("image/*");
				i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				startActivity(i);
				break;
			case R.id.invert_colors:
			    new ObjectAnimator().ofArgb(mBack, "backgroundColor", curBackgroundColor, curBackgroundColor == mColorDark ?mColorLight: mColorDark).start();
				curBackgroundColor = curBackgroundColor == mColorDark ?mColorLight: mColorDark;
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}



}
