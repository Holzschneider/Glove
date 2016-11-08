package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import de.dualuse.collections.LinkedNode;

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

	
	public synchronized UpdateTracker trackUpdates() {
		return dirty.append(new UpdateTracker());
	}

	UpdateTracker dirty = new UpdateTracker();
	
	class UpdateTracker extends LinkedNode<UpdateTracker> implements Texture.UpdateTracker {
		private int changeCounter = 1, updateCounter = -1;
		
		private Range dx = new Range();
		private Range dy = new Range();
		
		private Range sx = new Range();
		private Range sy = new Range();
		
		
		public void extend(int x0, int x1, int y0, int y1) {
			for (UpdateTracker cursor=this,current=null;current!=this;current=cursor=cursor.next()) 
				if (cursor.dx.extend(x0, x1) | cursor.dy.extend(y0, y1))
					cursor.changeCounter++;
		}
		
		public void dispose() {
			synchronized(Texture2D.this) {
				remove();
			}
		}
		
		boolean greedy = false;
		
		public boolean update(int[] planeTargets, int level, FlowControl f) {
			if (changeCounter==updateCounter)
				return false;
			
			if (updateCounter==-1) {
				for (int target: planeTargets)
					glTexImage2D(target, level, internalformat, width, height, 0, format, type, null);
			
				updateCounter = 0;
				sx.extend(0, width);
				sy.extend(0, height);
			}

			boolean dirty = !(dx.isEmpty() || dy.isEmpty());
			boolean streaming = !(sx.isEmpty() || sy.isEmpty());
			
			if ( !dirty && !streaming )
				return false;

			
			if (dirty && (!streaming || greedy) ) {
				int was = (sx.max-sx.min)*(sy.max-sy.min);
				
				sx.transfer(dx);
				sy.transfer(dy);
				
				dirty = false;
				
				int is = (sx.max-sx.min)*(sy.max-sy.min);
				
				System.out.println("updating streaming range: ["+sx.min+","+sx.max+"]x["+sy.min+","+sy.max+"]");
				
				f.announce( is-was );
			}
			
			final int x0 = sx.min, y0 = sy.min, x1 = sx.max, y1 = sy.max;
			
			// always try to push through all the leftover pixels at once
			long biteSize = f.allocate( (x1-x0)*(y1-y0)*pixelsize ); 
			int lineSize = (int)((x1-x0)*pixelsize), linesPerBite = (int)((biteSize/lineSize));
			for (int i=0,biteLines = linesPerBite;i<2 && biteLines>0;i++) {
				
				final int y0_ = y0, y1_ = (int)(biteLines>y1-y0_?y1:biteLines+y0_);
				
				biteLines -= y1_-y0_;
//				lineProgress += y1_-y0_;
	
				System.out.println("Lines: "+y0_+" -> "+y1_);
				
				if (x0==0&&y0_==0 && x1-x0==Texture2D.this.width)
				for (int target: planeTargets)
					glTexSubImage2D(target, level, 0, 0, width, y1_, format, type, pixels);
				else 
				if (x0==0 && x1-x0==Texture2D.this.width)
					for (int target: planeTargets)
						glTexSubImage2D(target, level, 0, y0, width, (y1_-y0_), format, type, (IntBuffer)pixels.slice().position(scan*y0_));
				else {
					glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
					for (int target: planeTargets)
						glTexSubImage2D(target, level, x0, y0, x1-x0, y1_-y0_, format, type, pixels.slice().position(x0+y0*scan));
					glPixelStorei(GL_UNPACK_ROW_LENGTH, 0); //XXX sucks!
				}
				
				if (y0_==sy.min)
					sy.min = y1_;
				else				
				if (y1_==sy.max)
					sy.max = y0_;
				
				if (sy.isEmpty()) {
					sx.reset();
					if (dirty && !(dirty=false)) { //if dirty, enter block and set dirty=false, otherwise skip
						sx.transfer(dx);
						sy.transfer(dy);
						f.announce( (sx.max-sx.min)*(sy.max-sy.min) );
						System.out.println("post-updating streaming range: ["+sx.min+","+sx.max+"]x["+sy.min+","+sy.max+"]");						
					}
					
				}
			}			
			
			if (!dirty && (sx.isEmpty() || sy.isEmpty()))
				updateCounter = changeCounter;
			
			return true;
		}

	}
	
	
	/////////////////////
	
	
	public Texture2D set(final int xoffset, int yoffset, final int width, final int height, int[] pixels, int offset, int scan) {

		IntBuffer qixels = this.pixels.slice();
		qixels.position(xoffset+yoffset*this.scan+this.offset);
		for (int y = yoffset, Y=y+height,o=offset,p=xoffset+y*this.scan+this.offset,r=this.scan;y<Y;y++,o+=scan, qixels.position(p+=r))
			qixels.put(pixels, o, width);

		dirty.extend(xoffset, xoffset+width,  yoffset, yoffset+height);
		
		return this;
	}
	
	public Texture2D set(int x, int y, int width, int height, Grabber pixels) {
		throw new UnsupportedOperationException();
	}
	
	public Texture2D set(int x, int y, int width, int height, Sampler pixels) {
		throw new UnsupportedOperationException();
	}
	
	
	
//	public Texture2D stream(FlowControl c)

	
	
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
