package de.dualuse.glove;

import java.util.concurrent.atomic.AtomicLong;

public class BoundingRange extends AtomicLong {

	private static final long serialVersionUID = 1L;
	
	public BoundingRange union( int x0, int x1 ) {
		long was, is, min, max;
		
		do {
			was = get(); 
			min =  was&0xFFFFFFFFL;
			max = (was>>>32)&0xFFFFFFFFL;
			is = (x0<min?x0:min)&0xFFFFFFFFL | (((max<x1?x1:max)&0xFFFFFFFFL)<<32);
		} while (!compareAndSet(was, is));
		
		return this;
	}
	
	public boolean isEmpty() {
		final long minmax = get();
		final int min = (int)(minmax & 0xFFFFFFFFl);
		final int max = (int)((minmax>>>32) & 0xFFFFFFFFl);
		
		return min>=max;
	}
	
	
	@Override
	public String toString() {
		final long minmax = get();
		final int min = (int)(minmax & 0xFFFFFFFFl);
		final int max = (int)((minmax>>>32) & 0xFFFFFFFFl);
		
		return "["+min+","+max+"]";
	}
	
}
