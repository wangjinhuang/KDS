package com.example.kds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kds.action.ExpressModel;
import com.example.kds.adapter.CompanyListAdapter;
import com.example.kds.bean.Express;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MainActivity extends BaseActivity {

	private TextView tvKd;
	CompanyListAdapter adapter;
	ArrayList<HashMap<String, Object>> companyList;
	AlertDialog cpListDialog;
	private EditText etDingdan;
	private Button btnSearch;
	private HttpUtils httpUtils;
	private String url = "http://api.ickd.cn/?id=102616&secret=16135ea51cb60246eff620f130a005bd";
	private String com = "shunfeng";
	private String nu;
	MenuDrawer bottomMenuDrawer;
	private TextView hisrory;
	private TextView about;
	private TextView exit;
	private ImageButton btnSpeach;
	protected SpeechRecognizer mIat;
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				dialog.dismiss();
				Toast.makeText(context,
						getResources().getString(R.string.no_new_verson_tips),
						Toast.LENGTH_LONG).show();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);

		/**
		 * 底部菜单
		 */

		bottomMenuDrawer = MenuDrawer.attach(this, Position.BOTTOM);// MenuDrawer.Type.OVERLAY,
		bottomMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		bottomMenuDrawer.setMenuView(R.layout.bottom_menu);
		bottomMenuDrawer.setContentView(R.layout.kuaidi_main);
		bottomMenuDrawer.setMenuSize(100);

		hisrory = (TextView) findViewById(R.id.history);
		about = (TextView) findViewById(R.id.about);
		exit = (TextView) findViewById(R.id.exit);
		etDingdan = (EditText) findViewById(R.id.etDingdan);
		tvKd = (TextView) findViewById(R.id.tvKD);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSpeach = (ImageButton) findViewById(R.id.btnSpeach);
		btnSpeach.setOnClickListener(speachOnclick);
		httpUtils = new HttpUtils();
		/**
		 * 初始化即创建语音配置对象，只有初始化后才可以使用MSC的各项服务。
		 */
		SpeechUtility
				.createUtility(context, SpeechConstant.APPID + "=562ed2a1");
		mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
		mIatDialog = new RecognizerDialog(context, mInitListener);

		/**
		 * companyList初始化
		 */

		companyList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < getResources().getStringArray(R.array.company_logo).length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			int resId = getResources().getIdentifier(
					getResources().getStringArray(R.array.company_logo)[i],
					"drawable", context.getPackageName());
			map.put("logo", resId);
			map.put("name",
					getResources().getStringArray(R.array.company_name)[i]);

			companyList.add(map);
		}

		/**
		 * 快递选择点击事件
		 */

		tvKd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showCompanyList();

			}
		});

		/**
		 * 查询按钮点击事件
		 */
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showProgressDialog(getResources().getString(
						R.string.msg_load_ing));
				searchDetails();
			}
		});

		/**
		 * 三个按钮
		 */

		hisrory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSuccess(v);
			}
		});
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSuccess(v);
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSuccess(v);
			}
		});

	}

	/**
	 * 语音按钮
	 */
	private OnClickListener speachOnclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mIatResults.clear();
			setParam();
			mIatDialog.setListener(mRecognizerDialogListener);
			mIatDialog.show();
		}
	};
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				showToast("初始化失败，错误码：" + code);
			}
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		etDingdan.setText(resultBuffer.toString());
		etDingdan.setSelection(resultBuffer.length());
	}

	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showToast("无法识别！");
		}

	};

	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");
	}

	/**
	 * 菜单按钮的点击事件
	 * 
	 * @param v
	 *            点击的按钮
	 */

	public void showSuccess(View v) {

		bottomMenuDrawer.setActiveView(v);
		switch (v.getId()) {
		case R.id.history:
			startActivity(new Intent(MainActivity.this, HistoryActivity.class));
			break;

		case R.id.exit:
			SysApplication.getInstance().exit();
			break;

		case R.id.about:
			showProgressDialog(getResources().getString(
					R.string.check_update_tips));

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {

					try {

						Thread.sleep(3000);// 让他显示3秒后，取消ProgressDialog

						mHandler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						e.printStackTrace();

					}

				}

			});

			t.start();
			break;

		default:
			break;
		}
		// bottomMenuDrawer.toggleMenu();
		bottomMenuDrawer.closeMenu(true);

	}

	/**
	 * 查询按钮的点击事件
	 */

	protected void searchDetails() {

		nu = etDingdan.getText().toString().trim();
		url = url + "&com=" + com + "&nu=" + nu;

		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("Gson", arg1);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				String str = arg0.result;
				Express express = new Gson().fromJson(str, Express.class);
				dialog.dismiss();
				if (express.status == 0) {

					Toast.makeText(MainActivity.this, express.message,
							Toast.LENGTH_LONG).show();
					etDingdan.setText("");
				} else {
					Intent intent = new Intent(MainActivity.this,
							ResultActivity.class);
					intent.putExtra("json", str);
					startActivity(intent);
					new ExpressModel(context).insert(express, str);
				}

			}
		});
	}

	/**
	 * AlerDialog
	 */

	protected void showCompanyList() {

		Builder builder = new Builder(context);
		cpListDialog = builder.create();
		View view = LayoutInflater.from(context).inflate(R.layout.company_list,
				null);
		ListView list = (ListView) view.findViewById(R.id.list);
		adapter = new CompanyListAdapter(companyList, context);
		list.setAdapter(adapter);

		cpListDialog.setView(view);
		cpListDialog.setCancelable(true);
		cpListDialog.show();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				tvKd.setText((CharSequence) ((HashMap<String, Object>) adapter
						.getItem(position)).get("name"));
				com = company_code[position];
				cpListDialog.dismiss();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
