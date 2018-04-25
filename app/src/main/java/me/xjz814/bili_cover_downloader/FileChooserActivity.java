package me.xjz814.bili_cover_downloader;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import me.xjz814.ui.*;
import me.xjz814.ui.widget.*;
import org.json.*;
import android.widget.CompoundButton.*;
import me.xjz814.ui.util.*;
import java.util.*;
import android.util.*;
import android.content.*;
import android.transition.*;

public class FileChooserActivity extends BaseActivity
{

	private ListView mFileList;
    private RadioButton rbCurdir;
	private A14Dialog dialog;
	private TextView tvCurPath;

	public static final String PATH_RETURNED="path_returned";
	@Override
	public boolean onBack()
	{
		return false;
	}
	private String selectedPath;
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		ViewGroup content=(ViewGroup) getLayoutInflater().inflate(R.layout.layout_dir_chooser, null);
		mFileList = (ListView) content.findViewById(R.id.lv_file);
		tvCurPath=(TextView) content.findViewById(R.id.tv_cur_path);
		rbCurdir=(RadioButton) content.findViewById(R.id.rb_cur_dir);
		rbCurdir.setChecked(SharedPrefManager.getSavePath().equals(curPath));
		rbCurdir.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{ 
				  if(!selectedPath.equals(curPath)){
					 ((BaseAdapter) mFileList.getAdapter()).notifyDataSetChanged();
					  selectedPath=curPath;
				  }
				}
			});
		tvCurPath.setText(curPath);
		selectedPath=SharedPrefManager.getSavePath();
		dialog = new A14Dialog(this).setCancelable(false)
			.setPositiveButton("选中", new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i=new Intent();
					setIntent(i.putExtra(PATH_RETURNED,selectedPath));
					setResult(RESULT_OK,i);
					finish();
					
				}

			
			}).setNegativeButton("取消", new OnClickListener(){

									 @Override
									 public void onClick(View p1)
									 {
										 setResult(RESULT_CANCELED);
										 finish();
									 }
									 
			
		}).setView(content);
		dialog.show();
		mFileList.setAdapter(new FileListAdapter(new File("/sdcard")));
		mFileList.setOnScrollListener(new ListView.OnScrollListener() {     

				/**   
				 * 滚动状态改变时调用   
				 */    
				@Override    
				public void onScrollStateChanged(AbsListView view, int scrollState) {    				}     
				

				/**   
				 * 滚动时调用   
				 */    
				@Override    
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {     
				position=view.getFirstVisiblePosition();
				}     
			});    
	}

	private SparseIntArray posRecord=new SparseIntArray();
	private String curPath="/sdcard";
	
	public void gotoPreviousPath(View view){
		
	    File parent=new File(curPath).getParentFile();
		if(parent.getPath().equals("/")){
			return;
		}
		mFileList.setAdapter(new FileListAdapter(parent));
		curPath=parent.getPath();
		if(rbCurdir.isChecked()){
			selectedPath=curPath;
		}
		tvCurPath.setText(curPath);
		mFileList.setSelection(posRecord.get(posRecord.size()-1));
		posRecord.removeAt(posRecord.size()-1);
	   
	}
	
	
	 class FileListAdapter extends BaseAdapter
	{
		private File file;
		private File[] dirList;
		
		
		public FileListAdapter(File file)
		{
			this.file = file;
			
			dirList = file.listFiles(new FileFilter(){

					@Override
					public boolean accept(File p1)
					{
						return p1.isDirectory();
					}
				});
			Arrays.sort(dirList, new Comparator<File>(){

					@Override
					public int compare(File p1, File p2)
					{
						
						return p1.getName().compareToIgnoreCase(p2.getName());
					}
					
				
			});
		}

		@Override
		public int getCount()
		{		

			return dirList.length;
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
		
		private ViewHolder holder;
		@Override
		public View getView( final int p1, View p2, ViewGroup p3)
		{	

			
			if (p2 == null)
			{
				p2= (ViewGroup) getLayoutInflater().inflate(R.layout.layout_dir_chooser_item, null); 

				holder = new ViewHolder((ViewGroup)p2);
				p2.setTag(holder);
				holder.tvFilename = (TextView) p2.findViewById(R.id.tv_filename);
				holder.rbFile = (RadioButton) p2.findViewById(R.id.rb_file);
				
			}
			
			holder = (FileChooserActivity.FileListAdapter.ViewHolder) p2.getTag();
			holder.tvFilename.setText(dirList[p1].getName());
			
			if (dirList[p1].getPath().equals(selectedPath))
			{
				holder.rbFile.setChecked(true);
			}else{
				holder.rbFile.setChecked(false);
			}
			holder.rbFile.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						if(((RadioButton)v).isChecked()){
							selectedPath=dirList[p1].getPath();
							rbCurdir.setChecked(false);
							Toast.makeText(FileChooserActivity.this,selectedPath,0).show();
							notifyDataSetChanged();
						}
					}
				});
				
			holder.itemView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v)
					{
					   posRecord.put(posRecord.size(),position);
					   curPath=dirList[p1].getPath();
				       mFileList.setAdapter(new FileListAdapter(dirList[p1]));
					   tvCurPath.setText(curPath);
					   if(rbCurdir.isChecked()){
						  selectedPath= curPath;
					   }
					}	
			});
			
			return p2;
		}

		class ViewHolder
		{
			ViewGroup itemView;
			TextView tvFilename;
			RadioButton rbFile;
			public ViewHolder(ViewGroup item)
			{
				itemView = item;
				
			}
		}
	}

}
