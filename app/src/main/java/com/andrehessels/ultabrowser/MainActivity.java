package com.andrehessels.ultabrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ProgressBar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;


public class MainActivity extends  AppCompatActivity  { 
	
	private Timer _timer = new Timer();
	
	private double opt = 0;
	private double n = 0;
	private double b = 0;
	
	private ArrayList<HashMap<String, Object>> history = new ArrayList<>();
	private ArrayList<Double> num = new ArrayList<>();
	
	private LinearLayout linear1;
	private ListView listview1;
	private WebView webview1;
	private LinearLayout linear3;
	private ImageView imageview1;
	private EditText edittext1;
	private ProgressBar progressbar2;
	private ImageView imageview3;
	private ImageView imageview2;
	
	private TimerTask t;
	private TimerTask t2;
	private AlertDialog.Builder d;
	private Intent i = new Intent();
	private SharedPreferences settings;
	private SharedPreferences tempmem;
	private SharedPreferences hist;
	private TimerTask t3;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		listview1 = (ListView) findViewById(R.id.listview1);
		webview1 = (WebView) findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		progressbar2 = (ProgressBar) findViewById(R.id.progressbar2);
		imageview3 = (ImageView) findViewById(R.id.imageview3);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		d = new AlertDialog.Builder(this);
		settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		tempmem = getSharedPreferences("tempmem", Activity.MODE_PRIVATE);
		hist = getSharedPreferences("hist", Activity.MODE_PRIVATE);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				listview1.setVisibility(View.GONE);
				webview1.setVisibility(View.VISIBLE);
				imageview2.setVisibility(View.VISIBLE);
				opt = 0;
				webview1.loadUrl(history.get((int)_position).get("history").toString());
			}
		});
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				if (imageview3.getVisibility() == View.GONE) {
					imageview3.setVisibility(View.VISIBLE);
				}
				if (_url.contains("http://")) {
					t2.cancel();
					SketchwareUtil.showMessage(getApplicationContext(), "The website you are trying to visit is blocked by the security settings of this browser.");
					webview1.goBack();
				}
				hist.edit().putString(String.valueOf((long)(n)), _url).commit();
				n++;
				edittext1.setText(webview1.getUrl());
				imageview3.setVisibility(View.GONE);
				progressbar2.setVisibility(View.VISIBLE);
				t2 = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressbar2.setProgress(webview1.getProgress());
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(t2, (int)(50), (int)(50));
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				t2.cancel();
				progressbar2.setVisibility(View.GONE);
				imageview3.setVisibility(View.VISIBLE);
				if (webview1.getUrl().contains("https://")) {
					imageview3.setImageResource(R.drawable.safeweb);
				}
				else {
					imageview3.setImageResource(R.drawable.unsafeweb);
				}
				super.onPageFinished(_param1, _param2);
			}
		});
		
		imageview1.setOnLongClickListener(new View.OnLongClickListener() {
			 @Override
				public boolean onLongClick(View _view) {
				t3 = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (hist.getString(String.valueOf((long)(n)), "").equals("")) {
									t3.cancel();
								}
								else {
									{
										HashMap<String, Object> _item = new HashMap<>();
										_item.put("history", hist.getString(String.valueOf((long)(n)), ""));
										history.add(_item);
									}
									
									n++;
								}
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(t3, (int)(10), (int)(10));
				listview1.setAdapter(new Listview1Adapter(history));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				if (opt == 0) {
					opt = 6;
					SketchwareUtil.showMessage(getApplicationContext(), "Hold the settings button again to go back to the browser.");
					webview1.setVisibility(View.GONE);
					listview1.setVisibility(View.VISIBLE);
					imageview2.setVisibility(View.GONE);
				}
				else {
					listview1.setVisibility(View.GONE);
					webview1.setVisibility(View.VISIBLE);
					imageview2.setVisibility(View.VISIBLE);
					opt = 0;
				}
				return true;
				}
			 });
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setAction(Intent.ACTION_VIEW);
				i.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(i);
			}
		});
		
		edittext1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				((EditText)edittext1).selectAll();
			}
		});
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (webview1.getUrl().contains("https://")) {
					d.setTitle("Secured connection");
					d.setMessage("This website uses the HTTPS protocol to secure your connection.");
					d.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					d.create().show();
				}
				else {
					d.setTitle("Unsecured connection");
					d.setMessage("This website does not use the HTTPS protocol to secure your connection, and can potentially be dangerous, although this isn't the case for all websites.");
					d.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					d.create().show();
				}
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Search();
			}
		});
	}
	
	private void initializeLogic() {
		webview1.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		opt = 0;
		listview1.setVisibility(View.GONE);
		imageview3.setVisibility(View.GONE);
		edittext1.setOnEditorActionListener(new EditText.OnEditorActionListener() { public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO) { 
					_Search();
					return true; } return false; } });
		progressbar2.setVisibility(View.GONE);
		_StatusBar();
		_RadiusAndShadow(linear3, 25, 25, "#FFFFFF");
		_RippleEffect("#EEEEEE", imageview1);
		_RippleEffect("#EEEEEE", imageview2);
		_RippleEffect("#EEEEEE", imageview3);
		linear1.setBackgroundColor(0xFFFFFFFF);
		switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
			    case Configuration.UI_MODE_NIGHT_YES:
			        
			if (settings.getString("darkmode", "").equals("Auto") || settings.getString("darkmode", "").equals("Dark")) {
				tempmem.edit().putString("darkmode", "true").commit();
				_darkappearance();
			}
			break;
			    case Configuration.UI_MODE_NIGHT_NO:
			        
			        
			if (settings.getString("darkmode", "").equals("Dark")) {
				_darkappearance();
			}
			break; 
		}
		n = 0;
		t3 = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (hist.getString(String.valueOf((long)(n)), "").equals("")) {
							t3.cancel();
						}
						else {
							{
								HashMap<String, Object> _item = new HashMap<>();
								_item.put("history", hist.getString(String.valueOf((long)(n)), ""));
								history.add(_item);
							}
							
							n++;
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(t3, (int)(10), (int)(10));
		listview1.setAdapter(new Listview1Adapter(history));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (webview1.canGoBack()) {
			webview1.goBack();
		}
	}
	public void _RippleEffect (final String _color, final View _view) {
		android.content.res.ColorStateList clr = new android.content.res.ColorStateList(new int[][]{new int[]{}},new int[]{Color.parseColor(_color)});
		android.graphics.drawable.RippleDrawable ripdr = new android.graphics.drawable.RippleDrawable(clr, null, null);
		_view.setBackground(ripdr);
	}
	
	
	public void _RadiusAndShadow (final View _view, final double _shadow, final double _radius, final String _color) {
		android.graphics.drawable.GradientDrawable ab = new android.graphics.drawable.GradientDrawable();
		
		ab.setColor(Color.parseColor(_color));
		ab.setCornerRadius((float) _radius);
		_view.setElevation((float) _shadow);
		_view.setBackground(ab);
	}
	
	
	public void _Fade (final View _view, final String _propertyName, final double _value, final double _duration) {
		ObjectAnimator anim = new ObjectAnimator();
		anim.setTarget(_view);
		anim.setPropertyName(_propertyName);
		anim.setFloatValues((float)_value);
		anim.setDuration((long)_duration);
		anim.start();
	}
	
	
	public void _StatusBar () {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
	}
	
	
	public void _Search () {
		if (edittext1.getText().toString().contains(".")) {
			if (edittext1.getText().toString().contains("http://") || edittext1.getText().toString().contains("https://")) {
				webview1.loadUrl(edittext1.getText().toString());
			}
			else {
				webview1.loadUrl("http://".concat(edittext1.getText().toString()));
			}
		}
		else {
			if (settings.getString("searchengine", "").equals("") || settings.getString("searchengine", "").equals("DuckDuckGo")) {
				webview1.loadUrl("https://duckduckgo.com/?q=".concat(edittext1.getText().toString()));
			}
			else {
				if (settings.getString("searchengine", "").equals("Google")) {
					webview1.loadUrl("https://google.com/search?q=".concat(edittext1.getText().toString()));
				}
				else {
					webview1.loadUrl("https://www.bing.com/search?q=".concat(edittext1.getText().toString()));
				}
			}
		}
	}
	
	
	public void _darkappearance () {
		Window w = this.getWindow();
		w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#000000"));
		linear1.setBackgroundColor(0xFF000000);
		linear3.setBackgroundColor(0xFF424242);
		imageview3.setBackgroundColor(0xFF424242);
		edittext1.setBackgroundColor(0xFF000000);
		edittext1.setTextColor(0xFFFFFFFF);
		imageview1.setImageResource(R.drawable.ic_settings_white);
		imageview2.setImageResource(R.drawable.ic_search_white);
		_RadiusAndShadow(linear3, 25, 25, "#424242");
		_RadiusAndShadow(edittext1, 25, 25, "#FFFFFF");
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.listc, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _view.findViewById(R.id.linear1);
			final TextView textview1 = (TextView) _view.findViewById(R.id.textview1);
			
			textview1.setText(history.get((int)_position).get("history").toString());
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}