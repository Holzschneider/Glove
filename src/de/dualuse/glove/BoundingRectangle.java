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


public class BoundingRectangle extends AtomicLong {
	
	public int area() {
		final long was = this.get();
		
		int minX = (int) (was&0xFFFFL);
		int maxX = (int) ((was>>>16)&0xFFFFL);
		int minY = (int) ((was>>>32)&0xFFFFL);
		int maxY = (int) ((was>>>48)&0xFFFFL);
		
		return (maxX-minX)*(maxY-minY);
	}

	public void define(int minX, int maxX, int minY, int maxY) {
		long is = 0;
		is = minX;
		is|= maxX<<16;
		is|= minY<<32;
		is|= maxY<<48;
		
		this.set(is);;
	}
	
	public BoundingRectangle chop(long howMuchArea, BoundingRectangle rest) {
		long was, is, minX, maxX, minY, maxY;
		long width, height, shrinkY;
		do {
			was = this.get(); 
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
		} while (!this.compareAndSet(was, is));
		
		rest.define((int)minX, (int)maxX, (int)minY, (int)(minY+shrinkY));
		
		return this;
	}
	
	
	/**
	 * 
	 * @param x0
	 * @param x1
	 * @param y0
	 * @param y1
	 * @return increase in total area
	 */
	
	public int union(int x0, int x1, int y0, int y1) {
		long was, is, minX, maxX, minY, maxY, before, after;
		
		do {
			was = this.get(); 
			minX =  was&0xFFFFL;
			maxX = (was>>>16)&0xFFFFL;
			minY = (was>>>32)&0xFFFFL;
			maxY = (was>>>48)&0xFFFFL;
			
			before = (maxX-minX)*(maxY-minY);
			
			boolean empty =  maxX<=minX||maxY<=minY;
			is = (minX = (empty||x0<minX?x0:minX));
			is|= (maxX=(empty||x1>maxX?x1:maxX))<<16;
			is|= (minY=(empty||y0<minY?y0:minY))<<32;
			is|= (maxY=(empty||y1>maxY?y1:maxY))<<48;
			
			after = (maxX-minX)*(maxY-minY);
			
		} while (!this.compareAndSet(was, is));
		
		return (int)(after-before);
//		return this;
	}
	
	
	public boolean isEmpty() {
		final long was = this.get(); 
		final long minX =  was&0xFFFFL;
		final long maxX = (was>>>16)&0xFFFFL;
		final long minY = (was>>>32)&0xFFFFL;
		final long maxY = (was>>>48)&0xFFFFL;
		
		return minX>=maxX || minY>=maxY;
	}
	
	public BoundingRectangle reset() {
		this.set(0);
		return this;
	}
	
	@Override
	public String toString() {
		long was = this.get(); 
		long minX =  was&0xFFFFL;
		long maxX = (was>>>16)&0xFFFFL;
		long minY = (was>>>32)&0xFFFFL;
		long maxY = (was>>>48)&0xFFFFL;
		
		return "["+minX+","+maxX+"]x["+minY+","+maxY+"]";
	}
	 
}



