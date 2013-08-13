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

	// objects of the type Sensor, SensorManager, LocationManager, LocationListener, context  
	private SensorManager mSensorManager; 
	private Sensor mAcc; 
	private Sensor mGeo; 
	private Sensor mGravAcc; 
	protected LocationManager mLoc; 
	protected LocationListener mLocationListen; 
	protected Context context; 
	
	//all of the textview objects and declaring a layout
	TextView title, x, y, z, a, b, c, d, e, yawtext, pitchtext, rolltext, xvelocity, yvelocity, zvelocity; 
	RelativeLayout layout;   
	
	//arrays for euler angles and orientation 
	float[] euler_angles = new float[3]; // yaw, pitch, roll
	float[] orientation_R = new float[9];
	float[] orientation_I = new float[9];
	float[] geomag = new float[3]; 
	float[] accel = new float[3]; 
	
	//array for getting linear acceleration data
	float[] linearacc_data_x = new float[1000];
	float[] linearacc_data_y = new float[1000];
	float[] linearacc_data_z = new float[1000];
	
	// variable for collection function
	float xacc; 
	float yacc; 
	float zacc; 
	
	// variable for integration function
	float xtemp = 0; 
	float ytemp = 0; 
	float ztemp = 0; 
	
	
	//"Overrides" overwrite a function that is included by the Android operating system 
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		//This is a referring to the onCreate function of the Activity class, so we are really just adding to it because we have put back everything we overwrote 
		super.onCreate(savedInstanceState);
		
		//Method to setContentView
		setContentView(R.layout.activity_main);
		
		//initializing the variables to be the sensors they need to be, using a reference to a method in the SensorManager class 
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mGravAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
		mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mGeo = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		mLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); 
		
		//initializing the textview objects by pointing to them to certain XML format labeled by a unique id  
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
		yawtext = (TextView)findViewById(R.id.yawval); 
		pitchtext = (TextView)findViewById(R.id.pitchval);
		rolltext = (TextView)findViewById(R.id.rollval);
		xvelocity = (TextView)findViewById(R.id.xvelocity); 
		yvelocity = (TextView)findViewById(R.id.yvelocity);
		zvelocity = (TextView)findViewById(R.id.zvelocity);
		
	}
	
	//following four methods are the methods required by the LocationListener interface
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
	
	// calculate the Euler angles of our device
	public void getEulerAngles(){
		// get rotation matrix
		SensorManager.getRotationMatrix(orientation_R, orientation_I, accel, geomag);
		// get euler angles
		SensorManager.getOrientation(orientation_R, euler_angles);
		
	}
	
	public void poparr() {
		// for loop to populate array 
		for (int i = 0; i < 1000; i++){
				 linearacc_data_x[i] = xacc;
				 linearacc_data_y[i] = yacc; 
				 linearacc_data_z[i] = zacc;
		} 
	}
	
	public void integratearr(){
		//integrating the collected data
		if (xacc > yacc && xacc > zacc) {
			for (int i=0; i < 1000; i=i+1){
				xtemp = xtemp + linearacc_data_x[i]; 
				ytemp = 0; 
				ztemp = 0;
			}
		}else if (yacc > xacc && yacc > zacc){
			for (int i=0; i < 1000; i=i+1){
				ytemp = ytemp + linearacc_data_y[i];
				xtemp = 0; 
				ztemp = 0; 
			} 
		}else if (zacc > xacc && zacc > yacc){
			for (int i=0; i < 1000; i=i+1){
				ztemp = ztemp + linearacc_data_z[i];
				xtemp = 0; 
				ytemp = 0; 
			}  
		}
	}
	 
	@Override 
	public final void onSensorChanged(SensorEvent event) {
		
		//an if-else logic block in order to use more than one sensor
		Sensor sensor = event.sensor; 
		if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			
			//linear acceleration eliminates gravity
			xacc = event.values[0];  
			yacc = event.values[1]; 
			zacc = event.values[2]; 			
			
			//call to the populate method and integrate method
			poparr(); 
			integratearr(); 
			
			//actually taking a TextView object and declaring what text it should read
			x.setText("X axis" +"\t\t"+ xacc);
			y.setText("Y axis" +"\t\t"+ yacc);
			z.setText("Z axis" +"\t\t"+ zacc);
			xvelocity.setText("X Velocity \t\t" + xtemp);
			yvelocity.setText("Y Velocity \t\t" + ytemp);
			zvelocity.setText("Z Velocity \t\t" + ztemp);
			
		}else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			
			float xmag = event.values[0]; 
			float ymag = event.values[1]; 
			float zmag = event.values[2]; 
			geomag[0] = xmag;
			geomag[1] = ymag; 
			geomag[2] = zmag; 
			a.setText("xmag" +"\t\t"+xmag);
			b.setText("ymag" +"\t\t"+ymag);
			c.setText("zmag" +"\t\t"+zmag);
		}else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){ 
			accel[0] = event.values[0];
			accel[1] = event.values[1]; 
			accel[2] = event.values[2]; 
			
			getEulerAngles(); 
			yawtext.setText("Yaw \t\t" +euler_angles[0]); 
			pitchtext.setText("Pitch \t\t" +euler_angles[1]); 
			rolltext.setText("Roll \t\t" +euler_angles[2]); 
		}
		
		
		
		title.setText(R.string.app_name); 
	}
	
	
	//method to register the sensors so they can resume
	@Override 
	protected void onResume() { 
		super.onResume() ; 
		mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mGeo, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mGravAcc, SensorManager.SENSOR_DELAY_NORMAL); 
	}
	
	//method to unregister the sensors so they can turn off when the application is paused
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
