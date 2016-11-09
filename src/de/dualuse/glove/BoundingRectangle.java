package de.dualuse.glove;

import java.util.concurrent.atomic.AtomicLong;

/**
 * BoundingRectangle is an lock-free thread-safe rectangle defined by range bounds.
 * Simple geometric operations as union, intersect, subtract, etc. are executed atomically.
 * 
 * This way the it's suitable for concurrent dirty region management & update scheduling in images and textures.
 * 
 * @author holzschneider
 *
 */


public class BoundingRectangle {
	
	public static interface RectangularArea {
		public void define(int x0, int x1, int y0, int y1);
	}
		
	
	private final AtomicLong r = new AtomicLong(0); 
	
	public int area() {
		final long was = r.get();
		
		int minX = (int) (was&0xFFFFL);
		int maxX = (int) ((was>>>16)&0xFFFFL);
		int minY = (int) ((was>>>32)&0xFFFFL);
		int maxY = (int) ((was>>>48)&0xFFFFL);
		
		return (maxX-minX)*(maxY-minY);
	}
	
	public BoundingRectangle chop(long howMuchArea, RectangularArea rest) {
		long was, is, minX, maxX, minY, maxY;
		long width, height, shrinkY;
		do {
			was = r.get(); 
			minX = (was&0xFFFFL);
			maxX = ((was>>>16)&0xFFFFL);
			minY = ((was>>>32)&0xFFFFL);
			maxY = ((was>>>48)&0xFFFFL);

			width = maxX-minX;
			height = maxY-minY;
					
			if (maxX<=minX||maxY<=minY) 
				return this;
			
			shrinkY = (shrinkY = howMuchArea/width)<height?shrinkY:height;
			
			is = minX;
			is|= maxX<<16;
			is|= (minY+shrinkY)<<32;
			is|= maxY<<48;
		} while (!r.compareAndSet(was, is));
		
		rest.define((int)minX, (int)maxX, (int)minY, (int)(minY+shrinkY));
		
		return this;
	}
	
	
	public BoundingRectangle union(int x0, int x1, int y0, int y1) {
		long was, is, minX, maxX, minY, maxY;
		
		do {
			was = r.get(); 
			minX =  was&0xFFFFL;
			maxX = (was>>>16)&0xFFFFL;
			minY = (was>>>32)&0xFFFFL;
			maxY = (was>>>48)&0xFFFFL;
			
			boolean empty =  maxX<=minX||maxY<=minY;
			is = (empty||x0<minX?x0:minX);
			is|= (empty||x1>maxX?x1:maxX)<<16;
			is|= (empty||y0<minY?y0:minY)<<32;
			is|= (empty||y1>maxY?y1:maxY)<<48;
		} while (!r.compareAndSet(was, is));
		
		return this;
	}
	
	public BoundingRectangle subtract(int x0, int x1, int y0, int y1) {
		long was, is, minX, maxX, minY, maxY;
		
		do {
			was = r.get(); 
			minX =  was&0xFFFFL;
			maxX = (was>>>16)&0xFFFFL;
			minY = (was>>>32)&0xFFFFL;
			maxY = (was>>>48)&0xFFFFL;
			
			if (x0<=minX && y0<=minY && maxX<=x1 && maxY<=y1)
				minX = maxX = minY = maxY = 0;
			else 
				if (x0<=minX && maxX<=x1)
					if (y0<=minY)
						minY = y1;
					else
					if (maxY<=y1)
						maxY = y0;
					else;
				else
				if (y0<=minY && maxY<=y1)
					if (x0<minX)
						minX = x1;
					else
					if (maxX<x1)
						maxX = x0;
						
			
			is = minX;
			is|= maxX<<16;
			is|= minY<<32;
			is|= maxY<<48;
		} while (!r.compareAndSet(was, is));
		
		return this;
	}
	
	public boolean isEmpty() {
		final long was = r.get(); 
		final long minX =  was&0xFFFFL;
		final long maxX = (was>>>16)&0xFFFFL;
		final long minY = (was>>>32)&0xFFFFL;
		final long maxY = (was>>>48)&0xFFFFL;
		
		return minX>=maxX || minY>=maxY;
	}
	
	public BoundingRectangle reset() {
		r.set(0);
		return this;
	}
	
	@Override
	public String toString() {
		long was = r.get(); 
		long minX =  was&0xFFFFL;
		long maxX = (was>>>16)&0xFFFFL;
		long minY = (was>>>32)&0xFFFFL;
		long maxY = (was>>>48)&0xFFFFL;
		
		return "["+minX+","+maxX+"]x["+minY+","+maxY+"]";
	}


	 
}



