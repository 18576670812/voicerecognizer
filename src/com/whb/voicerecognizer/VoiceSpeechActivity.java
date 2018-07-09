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
	        		Toast.makeText(mContext, "��Ƶ�Ϻ�������", Toast.LENGTH_SHORT).show(); 
	        		break;
	        	
	        	case RecognizerIntent.RESULT_CLIENT_ERROR:
	        		Toast.makeText(mContext, "�ͻ����Ϻ�������", Toast.LENGTH_SHORT).show();
	        		break;
	        		
	        	case RecognizerIntent.RESULT_NETWORK_ERROR:
	        		Toast.makeText(mContext, "�����Ϻ�������", Toast.LENGTH_SHORT).show();
	        		break;
	        		
	        	case RecognizerIntent.RESULT_NO_MATCH:
	        		Toast.makeText(mContext, "û��ƥ�䵽�κν������ע�ⷢ�����ͣ�", Toast.LENGTH_SHORT).show();
	        		break;
	        	
	        	case RecognizerIntent.RESULT_SERVER_ERROR:
	        		Toast.makeText(mContext, "��̫�󣬷����������㣡", Toast.LENGTH_SHORT).show();
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
			//ͨ��Intent��������ʶ���ģʽ����������
			Intent mRecognizerIntent = 
					new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			//����ģʽ������ģʽ������ʶ��  
			mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			//��ʾ������ʼ  
			mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					"��ʼ����");
			//��ʼ����ʶ��  
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
				Toast.makeText(mContext, "�Ҳ��������豸��", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

/*================================================================================
 *
 * 	��	�� 							��	��
 --------------------------------------------------------------------------
	ACTION_RECOGNIZE_SPEECH			���������
 --------------------------------------------------------------------------
	ACTION_WEB_SEARCH				������������ģʽ�����������ҳ������ʾ
 --------------------------------------------------------------------------
	EXTRA_LANGUAGE					�������Կ�
 --------------------------------------------------------------------------
	EXTRA_LANGUAGE_MODEL			����ʶ��ģʽ
 --------------------------------------------------------------------------
	EXTRA_MAX_RESULTS				���ص������
 --------------------------------------------------------------------------
	EXTRA_PROMPT					��ʾ�û����Կ�ʼ����
 --------------------------------------------------------------------------
	EXTRA _RESULTS					���ַ������ص�һ��ArrayList��
 --------------------------------------------------------------------------
	LANGUAGE_MODEL-FREE_FORM		��һ������ģʽ����������
 --------------------------------------------------------------------------	
	LANGUAGE_MODEL-WEB_SEARCH		ʹ������ģ����Web������
 --------------------------------------------------------------------------
	RESULT_AUDIO_ERROR				���ؽ��ʱ����Ƶ��������
 --------------------------------------------------------------------------
	RESULT_CLIENT_ERROR				���ؽ��ʱ���ͻ�����������
 --------------------------------------------------------------------------
	RESULT_NETWORK_ERROR			���ؽ��ʱ��������������
 --------------------------------------------------------------------------
	RESULT_NO_MATCH					û�м�⵽�����Ĵ���
 --------------------------------------------------------------------------
	RESULT_SERVER_ERROR				���ؽ��ʱ����������������
 --------------------------------------------------------------------------
*/