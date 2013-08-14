package com.example.acclapp;

public class RingBuffer {
	private int head, tail; 
	private int numEntries; 
	private final int BUFFER_SIZE = 1024;
	private float[] buffer = new float[BUFFER_SIZE]; 

	public RingBuffer() {
		head = 0; 
		tail = 0; 
		numEntries = 0; 
	}
	
	public void add( float input ){
		if(numEntries < BUFFER_SIZE){
			numEntries++; 
			buffer[head] = input; 
			head = (head + 1)%BUFFER_SIZE; 
		}else{
			buffer[head] = input; 
			head = (head + 1)%BUFFER_SIZE; 
			tail = (tail + 1)%BUFFER_SIZE; 
		}
		
	}
	
	public float get(){
		if(numEntries > 0){
			float value = buffer[tail];
			tail = (tail + 1)%BUFFER_SIZE;
			numEntries = numEntries -1; 
			return value; 
		}else{ 
			return 0; 
		}
	}
	
	public int getNumEntries(){ 
		return numEntries; 
	}
	
	
}

