package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Texture2D implements Texture {
	
	final IntBuffer pixels;
	final int internalformat, width, height, format, type, offset, scan, pixelsize;
	
	/////
	
	private static IntBuffer bufferForSize(int width, int height) {
		return ByteBuffer.allocateDirect(width*height*4).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public Texture2D(int internalformat, int width, int height) {
		this(internalformat,width,height, GL_BGRA, GL_UNSIGNED_BYTE, bufferForSize(width, height), 0, width);
	}
	
	public Texture2D(int internalformat, int width, int height, int format, int type, IntBuffer pixels, int offset, int scan) {
		this.internalformat = internalformat;
		this.width = width;
		this.height = height;
		this.format = format;
		this.type = type;
		this.pixels = pixels;
		this.offset = offset;
		this.scan = scan;
		this.pixelsize = 4;
	}

	
	public synchronized Texture.UpdateTracker trackUpdates() {
		Arrays.copyOf(trackers, trackers.length+1);
		return trackers[trackers.length-1] = new UpdateTracker();
	}

	//XXX maybe use CopyOnWriteArray Fancy Collection
	private UpdateTracker[] trackers = new UpdateTracker[0];
	
	
	private class UpdateTracker extends BoundingRectangle implements Texture.UpdateTracker {
		private static final long serialVersionUID = 1L;

		private int updateCounter = -1;
		private int x0=0, x1=0, y0=0, y1=0;
		
		public void define(int x0, int x1, int y0, int y1) {
			this.x0=x0;
			this.x1=x1;
			this.y0=y0;
			this.y1=y1;
		}
		
		public void dispose() {
			synchronized(Texture2D.this) {
				for (int i=0,I=trackers.length;i<I;i++)
					if (trackers[i]==this) {
						trackers[i] = trackers[I-1];
						trackers = Arrays.copyOf(trackers, I-1);
						return;
					}
			}
		}
		
		public boolean update(int[] planeTargets, int level, FlowControl f) {
			if (updateCounter==-1) {
				for (int target: planeTargets)
					glTexImage2D(target, level, internalformat, width, height, 0, format, type, null);
			
				updateCounter = 0;
				union(0, width, 0, height);
			}

			if (isEmpty()) 
				return false;
			
			// always try to push through the remaining texture at once
			long biteSize = f.allocate( this.area()*pixelsize );
			this.chop( (biteSize/pixelsize), this);
			
			if (x0==0&&y0==0 && x1-x0==Texture2D.this.width)
			for (int target: planeTargets)
				glTexSubImage2D(target, level, 0, 0, width, y1, format, type, pixels);
			else 
			if (x0==0 && x1-x0==Texture2D.this.width)
				for (int target: planeTargets)
					glTexSubImage2D(target, level, 0, y0, width, (y1-y0), format, type, (IntBuffer)pixels.slice().position(scan*y0));
			else {
				glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
				for (int target: planeTargets)
					glTexSubImage2D(target, level, x0, y0, x1-x0, y1-y0, format, type, pixels.slice().position(x0+y0*scan));
				glPixelStorei(GL_UNPACK_ROW_LENGTH, 0); //XXX sucks!
			}
			
			return true;
		}

	}
	
	/////////////////////

	public Texture2D updateRectangle(final int xoffset, int yoffset, final int width, final int height) {
		for (UpdateTracker ut: trackers) 
			ut.union(xoffset, yoffset, xoffset+width, yoffset+height);
		
		return this;
	}
	
	public Texture2D set(final int xoffset, int yoffset, final int width, final int height, int[] pixels, int offset, int scan) {

		IntBuffer qixels = this.pixels.slice();
		qixels.position(xoffset+yoffset*this.scan+this.offset);
		for (int y = yoffset, Y=y+height,o=offset,p=xoffset+y*this.scan+this.offset,r=this.scan;y<Y;y++,o+=scan, qixels.position(p+=r))
			qixels.put(pixels, o, width);

		return updateRectangle(xoffset, yoffset, width, height);
	}
	
	
	public Texture2D set(int x, int y, int width, int height, Grabber pixels) {
		throw new UnsupportedOperationException();
	}
	
	public Texture2D set(int x, int y, int width, int height, Sampler pixels) {
		throw new UnsupportedOperationException();
	}
	
	
	
	
	public static interface Grabber {
		void grab(int x, int y, int width, int height, int pixels[], int offset, int scan);
	}
	
	public static interface Sampler {
		int sample(int x, int y);
	}
	
	public static interface Sampler2 {
		void sample(int x, int y, int colors[]);
	}
	
	public static interface SamplerLuminance {
		void sample(int x, int y, Luminance w);
		public static interface Luminance {
			void color(float y);
		}
	}

	public static interface SamplerLuminanceAlpha {
		void sample(int x, int y, Luminance w);
		public static interface Luminance {
			void color(float y);
		}

		public static interface LuminanceAlpha {
			void color(float y, float a);
		}
	}
	
	public static interface SamplerRGB {
		void sample(int x, int y, RGB rgb);
		public static interface RGB {
			void color(float r, float g, float b);
		}
	}
	
	public static interface SamplerRGBA {
		void sample(int x, int y, RGBA rgb);
		public static interface RGBA {
			void color(float r, float g, float b, float a);
		}
	}

	
	
}
