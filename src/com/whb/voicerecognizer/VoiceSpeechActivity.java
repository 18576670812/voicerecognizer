package com.whb.voicerecognizer;

import java.util.ArrayList;
import java.util.List;

import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class VoiceSpeechActivity extends ActionBarActivity {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 9527; 
	
	public static Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_speech);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mContext = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice_speech, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
		implements View.OnClickListener{
		Button mButton;
		ListView mList;
		
		public PlaceholderFragment() {
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			// get the recognized data from network(google)   
	        if(requestCode==VOICE_RECOGNITION_REQUEST_CODE && resultCode==RESULT_OK){  
	            // get the recognized result(ArrayList)
	            ArrayList<String> results = 
	            		data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);  
	            // set result into ListView
	            mList.setAdapter(new ArrayAdapter<String>(mContext,  
	                    android.R.layout.simple_list_item_1, results));  
	        } else {
	        	switch(resultCode) {
	        	case RecognizerIntent.RESULT_AUDIO_ERROR:
	        		Toast.makeText(mContext, "音频拖后腿啦！", Toast.LENGTH_SHORT).show(); 
	        		break;
	        	
	        	case RecognizerIntent.RESULT_CLIENT_ERROR:
	        		Toast.makeText(mContext, "客户端拖后腿啦！", Toast.LENGTH_SHORT).show();
	        		break;
	        		
	        	case RecognizerIntent.RESULT_NETWORK_ERROR:
	        		Toast.makeText(mContext, "网络拖后腿啦！", Toast.LENGTH_SHORT).show();
	        		break;
	        		
	        	case RecognizerIntent.RESULT_NO_MATCH:
	        		Toast.makeText(mContext, "没有匹配到任何结果，请注意发音口型！", Toast.LENGTH_SHORT).show();
	        		break;
	        	
	        	case RecognizerIntent.RESULT_SERVER_ERROR:
	        		Toast.makeText(mContext, "人太丑，服务器不吊你！", Toast.LENGTH_SHORT).show();
	        		break;
	        	}
	        	
	        }
			super.onActivityResult(requestCode, resultCode, data);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_voice_speech,
					container, false);
			mButton = (Button)rootView.findViewById(R.id.start);
			mList = (ListView)rootView.findViewById(R.id.list);
			
			// Check to see if a recognition activity is present
	        PackageManager pm = mContext.getPackageManager();
	        List<ResolveInfo> activities = pm.queryIntentActivities(
	                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	        if(activities.size() > 0) {
	        	//mButton.setClickable(false);
	        	mButton.setEnabled(false);
	        } else {
	        	mButton.setOnClickListener(this);
	        }
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//通过Intent传递语音识别的模式，开启语音
			Intent mRecognizerIntent = 
					new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			//语言模式和自由模式的语音识别  
			mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			//提示语音开始  
			mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"开始语音");
			//开始语音识别  
			try {
				/*
				startActivityForResult(mRecognizerIntent,
						VOICE_RECOGNITION_REQUEST_CODE);
						*/
				Intent intent = new Intent(Intent.ACTION_CALL, 
						Uri.fromParts("tel", "112", null));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "找不到语音设备！", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

/*================================================================================
 *
 * 	常	量 							描	述
 --------------------------------------------------------------------------
	ACTION_RECOGNIZE_SPEECH			开启语音活动
 --------------------------------------------------------------------------
	ACTION_WEB_SEARCH				开启网络语音模式，结果将以网页搜索显示
 --------------------------------------------------------------------------
	EXTRA_LANGUAGE					设置语言库
 --------------------------------------------------------------------------
	EXTRA_LANGUAGE_MODEL			语音识别模式
 --------------------------------------------------------------------------
	EXTRA_MAX_RESULTS				返回的最大结果
 --------------------------------------------------------------------------
	EXTRA_PROMPT					提示用户可以开始语音
 --------------------------------------------------------------------------
	EXTRA _RESULTS					将字符串返回到一个ArrayList中
 --------------------------------------------------------------------------
	LANGUAGE_MODEL-FREE_FORM		在一种语言模式上自由语言
 --------------------------------------------------------------------------	
	LANGUAGE_MODEL-WEB_SEARCH		使用语言模型在Web上搜索
 --------------------------------------------------------------------------
	RESULT_AUDIO_ERROR				返回结果时，音频遇到错误
 --------------------------------------------------------------------------
	RESULT_CLIENT_ERROR				返回结果时，客户端遇到错误
 --------------------------------------------------------------------------
	RESULT_NETWORK_ERROR			返回结果时，网络遇到错误
 --------------------------------------------------------------------------
	RESULT_NO_MATCH					没有检测到语音的错误
 --------------------------------------------------------------------------
	RESULT_SERVER_ERROR				返回结果时，服务器遇到错误
 --------------------------------------------------------------------------
*/