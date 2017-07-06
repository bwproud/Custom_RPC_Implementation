package a4;

import java.util.concurrent.ArrayBlockingQueue;

public class WrapperBlockingQueue<Object> extends ArrayBlockingQueue<Object>{
	private int count;
	public WrapperBlockingQueue(int size){
		super(size);
		count = 0;
	}
	
	public Object take() throws InterruptedException{
		count++;
		return super.take();
	}
	
	public int getCount(){
		return count;
	}
	
	public boolean offer(Object e){
		count--;
		return super.offer(e);
	}
}
