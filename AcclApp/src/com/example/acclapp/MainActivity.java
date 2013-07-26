package com.example.acclapp;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent; 
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager; 
	private Sensor mAcc; 
	TextView title, x, y, z; 
	RelativeLayout layout; 
	
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		layout = (RelativeLayout)findViewById(R.id.relative); 
		title = (TextView)findViewById(R.id.name); 
		x = (TextView)findViewById(R.id.xval);
		y = (TextView)findViewById(R.id.yval);
		z = (TextView)findViewById(R.id.zval);
	}
	@Override 
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	//Do something here if sensor accuracy changes.
		
	}
	
	@Override 
	public final void onSensorChanged(SensorEvent event) {
		float xacc = event.values[0]; 
		float yacc = event.values[1]; 
		float zacc = event.values[2]; 
		title.setText(R.string.app_name); 
		x.setText("X axis" +"\t\t"+xacc);
		y.setText("Y axis" +"\t\t"+yacc);
		z.setText("Z axis" +"\t\t"+zacc);
	}
	
	@Override 
	protected void onResume() { 
		super.onResume() ; 
		mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override 
	protected void onPause () { 
		super.onPause(); 
		mSensorManager.unregisterListener(this); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
