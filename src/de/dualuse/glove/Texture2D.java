package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Texture2D implements Texture {
	
	final IntBuffer pixels;
	final int internalformat, width, height, format, type, offset, scan, pixelsize;
	
	/////
	
//	horizontal, vertical, altitudial
	final Range dx = new Range();
	final Range dy = new Range();
	
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

	public void init(int[] targets, int level) {
		
		for (int target: targets)
			glTexImage2D(target,level, internalformat, width, height, 0, format, type, pixels);
		
		dx.reset();
		dy.reset();
	}

	public void update(int[] targets, int level) {
		if (dx.isEmpty() || dy.isEmpty())
			return;
		
		final int x0 = dx.min, y0 = dy.min, x1 = dx.max, y1 = dy.max;
		
		if (x0==0&&y0==0 && x1-x0==Texture2D.this.width)
			for (int target:targets)
				glTexSubImage2D(target, level, 0, 0, width, height, format, type, pixels);
		else 
		if (x0==0 && x1-x0==Texture2D.this.width)
			for (int target:targets)
				glTexSubImage2D(target, level, 0, y0, width, y1-y0, format, type, (IntBuffer)pixels.slice().position(scan*y0));
		else {
			glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
			for (int target:targets)
				glTexSubImage2D(target, level, x0, y0, x1-x0, y1-y0, format, type, pixels.slice().position(x0+y0*scan));
			glPixelStorei(GL_UNPACK_ROW_LENGTH, 0); //XXX sucks!
		}
		
		dx.reset();
		dy.reset();
	}

	
	/////////////////////
	
	
	public Texture2D set(final int xoffset, int yoffset, final int width, final int height, int[] pixels, int offset, int scan) {

		IntBuffer qixels = this.pixels.slice();
		qixels.position(xoffset+yoffset*this.scan+this.offset);
		for (int y = yoffset, Y=y+height,o=offset,p=xoffset+y*this.scan+this.offset,r=this.scan;y<Y;y++,o+=scan, qixels.position(p+=r))
			qixels.put(pixels, o, width);
		
		dx.extend(xoffset, xoffset+width);
		dy.extend(yoffset, yoffset+height);
		
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
