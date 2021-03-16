package com.andrehessels.ultabrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
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
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;


public class SettingsActivity extends  AppCompatActivity  { 
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private String var1 = "";
	private double n = 0;
	
	private ArrayList<String> searchengines = new ArrayList<>();
	private ArrayList<String> darkmode = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private TextView textview3;
	private LinearLayout linear2;
	private TextView textview2;
	private TextView textview4;
	private LinearLayout linear5;
	private TextView textview6;
	private LinearLayout linear7;
	private TextView textview8;
	private TextView textview10;
	private LinearLayout linear10;
	private TextView textview12;
	private TextView textview1;
	private LinearLayout linear4;
	private Switch switch1;
	private TextView textview5;
	private LinearLayout linear6;
	private Spinner spinner1;
	private TextView textview7;
	private LinearLayout linear8;
	private Spinner spinner2;
	private TextView textview11;
	private LinearLayout linear11;
	private Button button1;
	
	private SharedPreferences settings;
	private Intent i = new Intent();
	private SharedPreferences tempmem;
	private AlertDialog.Builder d;
	private TimerTask t3;
	private SharedPreferences hist;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_app_bar = (AppBarLayout) findViewById(R.id._app_bar);
		_coordinator = (CoordinatorLayout) findViewById(R.id._coordinator);
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		textview3 = (TextView) findViewById(R.id.textview3);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview4 = (TextView) findViewById(R.id.textview4);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		textview6 = (TextView) findViewById(R.id.textview6);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		textview8 = (TextView) findViewById(R.id.textview8);
		textview10 = (TextView) findViewById(R.id.textview10);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		textview12 = (TextView) findViewById(R.id.textview12);
		textview1 = (TextView) findViewById(R.id.textview1);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		switch1 = (Switch) findViewById(R.id.switch1);
		textview5 = (TextView) findViewById(R.id.textview5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		textview7 = (TextView) findViewById(R.id.textview7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		textview11 = (TextView) findViewById(R.id.textview11);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		button1 = (Button) findViewById(R.id.button1);
		settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
		tempmem = getSharedPreferences("tempmem", Activity.MODE_PRIVATE);
		d = new AlertDialog.Builder(this);
		hist = getSharedPreferences("hist", Activity.MODE_PRIVATE);
		
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					settings.edit().putString("enforcehttps", "true").commit();
				}
				else {
					settings.edit().putString("enforcehttps", "false").commit();
				}
			}
		});
		
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				settings.edit().putString("searchengine", searchengines.get((int)(_position))).commit();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				var1 = settings.getString("darkmode", "");
				settings.edit().putString("darkmode", darkmode.get((int)(_position))).commit();
				if (!var1.equals(settings.getString("darkmode", ""))) {
					d.setTitle("Relaunch required");
					d.setIcon(R.drawable.ic_settings_black);
					d.setMessage("In order to apply the app theme, you have to relaunch the app by force closing it from the app switcher. Unsaved data will go lost.");
					d.setPositiveButton("Close app", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							finishAffinity();
						}
					});
					d.create().show();
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				t3 = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (hist.getString(String.valueOf((long)(n)), "").equals("")) {
									t3.cancel();
									button1.setText("✓");
									button1.setBackgroundColor(0xFF212121);
								}
								else {
									hist.edit().remove(String.valueOf((long)(n))).commit();
									n++;
								}
							}
						});
					}
				};
				_timer.scheduleAtFixedRate(t3, (int)(10), (int)(10));
			}
		});
	}
	
	private void initializeLogic() {
		searchengines.add("DuckDuckGo");
		searchengines.add("Google");
		searchengines.add("Bing");
		spinner1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, searchengines));
		darkmode.add("Light");
		darkmode.add("Dark");
		darkmode.add("Auto");
		spinner2.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, darkmode));
		if (settings.getString("searchengine", "").equals("") || settings.getString("searchengine", "").equals("DuckDuckGo")) {
			spinner1.setSelection((int)(0));
		}
		else {
			if (settings.getString("searchengine", "").equals("Google")) {
				spinner1.setSelection((int)(1));
			}
			else {
				spinner1.setSelection((int)(2));
			}
		}
		if (settings.getString("enforcehttps", "").equals("true")) {
			switch1.setChecked(true);
		}
		if (settings.getString("darkmode", "").equals("") || settings.getString("darkmode", "").equals("Light")) {
			spinner2.setSelection((int)(0));
		}
		else {
			if (settings.getString("darkmode", "").equals("Dark")) {
				spinner2.setSelection((int)(1));
				vscroll1.setBackgroundColor(0xFF000000);
				_RadiusAndShadow(linear1, 0, 25, "#FFFFFF");
			}
			else {
				spinner2.setSelection((int)(2));
				if (tempmem.getString("darkmode", "").equals("true")) {
					vscroll1.setBackgroundColor(0xFF000000);
					_RadiusAndShadow(linear1, 0, 25, "#FFFFFF");
				}
			}
		}
		Window w = this.getWindow();
		w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor("#00C853"));
		setTitle("Settings");
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
		i.setAction(Intent.ACTION_VIEW);
		i.setClass(getApplicationContext(), MainActivity.class);
		startActivity(i);
	}
	public void _RadiusAndShadow (final View _view, final double _shadow, final double _radius, final String _color) {
		android.graphics.drawable.GradientDrawable ab = new android.graphics.drawable.GradientDrawable();
		
		ab.setColor(Color.parseColor(_color));
		ab.setCornerRadius((float) _radius);
		_view.setElevation((float) _shadow);
		_view.setBackground(ab);
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