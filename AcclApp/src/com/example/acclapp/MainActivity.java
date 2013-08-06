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
import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 

public class MainActivity extends Activity implements SensorEventListener, LocationListener {

	private SensorManager mSensorManager; 
	private Sensor mAcc; 
	private Sensor mGeo; 
	protected LocationManager mLoc; 
	protected LocationListener mLocationListen; 
	protected Context context; 
	
	TextView title, x, y, z, a, b, c, d, e; 
	RelativeLayout layout;   
	
	
	
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mGeo = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		mLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); 
		layout = (RelativeLayout)findViewById(R.id.relative); 
		title = (TextView)findViewById(R.id.name); 
		x = (TextView)findViewById(R.id.xval);
		y = (TextView)findViewById(R.id.yval);
		z = (TextView)findViewById(R.id.zval);
		a = (TextView)findViewById(R.id.aval);
		b = (TextView)findViewById(R.id.bval); 
		c = (TextView)findViewById(R.id.cval); 
		d = (TextView)findViewById(R.id.dval);
		e = (TextView)findViewById(R.id.eval);
		
		
	}
	
	
	@Override
	public void onLocationChanged(Location location){ 
		d.setText("LAT" +"\t\t"+ location.getLatitude());
		e.setText("LONG" +"\t\t"+ location.getLongitude()); 
	}
	
	
	@Override 
	public void onProviderDisabled(String provider){ 
		Log.d("Latitude", "disable"); 
	}
	
	@Override
	public void onProviderEnabled(String provider){ 
		Log.d("Latitude", "enable"); 
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras){ 
		Log.d("Latitude", "status"); 
	}
	
	@Override 
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	//Do something here if sensor accuracy changes.
		
	}
	
	@Override 
	public final void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor; 
		if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			
			float xacc = event.values[0];  
			float yacc = event.values[1]; 
			float zacc = event.values[2]; 
			x.setText("X axis" +"\t\t"+xacc);
			y.setText("Y axis" +"\t\t"+yacc);
			z.setText("Z axis" +"\t\t"+zacc);
		}else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			
			float xmag = event.values[0]; 
			float ymag = event.values[1]; 
			float zmag = event.values[2]; 
			a.setText("xmag" +"\t\t"+xmag);
			b.setText("ymag" +"\t\t"+ymag);
			c.setText("zmag" +"\t\t"+zmag);
		}
		
		
		
		
		
		title.setText(R.string.app_name); 
	}
	
	@Override 
	protected void onResume() { 
		super.onResume() ; 
		mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mGeo, SensorManager.SENSOR_DELAY_NORMAL);
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
