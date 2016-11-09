package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.opengl.GL11;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public class SourceTexture2D implements Texture {
	
	final int internalformat, width, height;
	
	/////
	
	public SourceTexture2D(int internalformat, int width, int height) {
		this.internalformat = internalformat;
		this.width = width;
		this.height = height;
	}

	
	public synchronized Texture.UpdateTracker trackUpdates() {
		trackers = Arrays.copyOf(trackers, trackers.length+1);
		return trackers[trackers.length-1] = new UpdateTracker();
	}
	
	//XXX maybe use CopyOnWriteArray Fancy Collection
	private UpdateTracker[] trackers = new UpdateTracker[0];
	
	
	private class UpdateTracker extends BoundingRectangle implements Texture.UpdateTracker {
		private static final long serialVersionUID = 1L;

		private int updateCounter = -1;
		private int x0=0, x1=0, y0=0, y1=0;
		private AtomicInteger pending = new AtomicInteger(0);
		private AtomicInteger announced = new AtomicInteger(0);
		
		public void define(int x0, int x1, int y0, int y1) {
			this.x0=x0;
			this.x1=x1;
			this.y0=y0;
			this.y1=y1;
		}
	
		public void dispose() {
			synchronized(SourceTexture2D.this) {
				for (int i=0,I=trackers.length;i<I;i++)
					if (trackers[i]==this) {
						trackers[i] = trackers[I-1];
						trackers = Arrays.copyOf(trackers, I-1);
						return;
					}
			}
		}
		
		@Override
		public int union(int x0, int x1, int y0, int y1) {
			int increase = super.union(x0, x1, y0, y1);
			pending.addAndGet(increase);
			return increase;
		}
		
		public boolean update(GLBoundTexture bt, int[] planeTargets, int level, FlowControl f, StreamProgress p) {
			if (updateCounter==-1) {
				for (int target: planeTargets)
					glTexImage2D(target, level, internalformat, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, null);
			
				updateCounter = 0;
				union(0, width, 0, height);
			}

			if (isEmpty()) 
				return false;
			
			//XXX extremely hard to test: lim_t->+0/0 (announced Flow) == 0? 
			int announcement = pending.getAndSet(0);
			f.announce(announcement);
			announced.addAndGet(announcement);
			
			
//			// always try to push through the remaining texture at once
//			long biteSize = f.allocate( this.area()*pixelsize );
//			this.chop( (biteSize/pixelsize), this);
//			
//			if (x0==0&&y0==0 && x1-x0==SourceTexture2D.this.width)
//			for (int target: planeTargets)
//				glTexSubImage2D(target, level, 0, 0, width, y1, format, type, pixels);
//			else 
//			if (x0==0 && x1-x0==SourceTexture2D.this.width)
//				for (int target: planeTargets)
//					glTexSubImage2D(target, level, 0, y0, width, (y1-y0), format, type, (IntBuffer)pixels.slice().position(scan*y0));
//			else {
//				glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
//				for (int target: planeTargets)
//					glTexSubImage2D(target, level, x0, y0, x1-x0, y1-y0, format, type, pixels.slice().position(x0+y0*scan));
//				glPixelStorei(GL_UNPACK_ROW_LENGTH, 0); //XXX sucks!
//			}
			
			if (p!=null)
				p.update(bt, level, x0, y0, x1-x0, y1-y0, isEmpty());
			
			boolean done = isEmpty();
			
			if (done)
				f.announce(-announced.getAndSet(0));
				
			return done;
		}

	}
	
	/////////////////////

	public SourceTexture2D updateRectangle(final int xoffset, int yoffset, final int width, final int height) {
		for (UpdateTracker ut: trackers) 
			ut.union(xoffset, xoffset+width, yoffset, yoffset+height);
		
		return this;
	}
	
	public SourceTexture2D set(final int xoffset, int yoffset, final int width, final int height, int[] pixels, int offset, int scan) {
		
		return updateRectangle(xoffset, yoffset, width, height);
	}
	
	
	public SourceTexture2D set(int x, int y, int width, int height, Grabber pixels) {
		throw new UnsupportedOperationException();
	}
	
	public SourceTexture2D set(int x, int y, int width, int height, SamplerARGB pixels) {
		throw new UnsupportedOperationException();
	}
	
	
	public SourceTexture2D set(int x, int y, int width, int height, Sampler pixels) {
		throw new UnsupportedOperationException();
	}

	
	public static interface Grabber {
		void grab(int x, int y, int width, int height, int pixels[], int offset, int scan);
	}
	
	public static interface SamplerARGB {
		int sample(int x, int y);
	}
	
	public static interface Sampler {
		void sample(int x, int y, Sample w);
		
	}

	public static interface Sample {
		void luminance(int y);
		void luminanceAlpha(int y, int a);
		
		void rgb(int r, int g, int b);
		void rgba(int r, int g, int b, int a);
	}
	
	
}
