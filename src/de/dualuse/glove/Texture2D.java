package de.dualuse.glove;

import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;
import static android.opengl.GLES11Ext.GL_BGRA;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public class Texture2D implements Texture {
	
	IntBuffer pixels;
	int target, level, internalformat;
	int width, height, format, type, offset, scan, pixelsize;
	
	/////

	final Range2D dirty = new Range2D();
	
	private static IntBuffer bufferForSize(int width, int height) {
		return ByteBuffer.allocateDirect(width*height*4).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public Texture2D(int level, int internalformat, int width, int height) {
		this(GL_TEXTURE_2D, level, internalformat,width,height, GL_BGRA, GL_UNSIGNED_BYTE, bufferForSize(width, height), 0, width);
	}
	
	public Texture2D(int target, int level, int internalformat, int width, int height, int format, int type, IntBuffer pixels, int offset, int scan) {
		this.width = width;
		this.height = height;
		this.format = format;
		this.type = type;
		this.pixels = pixels;
		this.offset = offset;
		this.scan = scan;
	}

	public void update(GLBoundTexture bt) {
		
	}

	
	public void update() {
		synchronized(this) {
			pending.update();
			
			upload(dirty.x0, dirty.y0, dirty.x1, dirty.y1);
			pending = null;
			
			dirty.reset();
		}
	}
	
	protected void upload(int x0, int y0, int x1, int y1) {
		if (x0==0&&y0==0 && x1-x0==Texture2D.this.width)
			glTexSubImage2D(target,level, 0, 0, width, height, format, type, Texture2D.this.pixels);
		else 
		if (x0==0&&y0==0 && x1-x0==Texture2D.this.width)
			glTexSubImage2D(target,level, 0, 0, width, height, format, type, Texture2D.this.pixels.position(scan*pixelsize));
		else {
			glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
			glPixelStorei(GL_PACK_SKIP_PIXELS, x0);
			glPixelStorei(GL_PACK_SKIP_ROWS, y0);
			glTexSubImage2D(target,level, x0, y0, x1-x0, y1-y0, format, type, Texture2D.this.pixels);
			glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
			glPixelStorei(GL_PACK_SKIP_PIXELS, 0);
			glPixelStorei(GL_PACK_SKIP_ROWS, 0);
		}
	}
	
	Update pending = new Update() {
		@Override public void execute() {
			glTexImage2D(target, level, internalformat, Texture2D.this.width, Texture2D.this.height, 0, format, type, null);
			glTexParameterf(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		};
	};
	
	public Texture2D set(final int x, int y, final int width, final int height, int[] pixels, int offset, int scan) {

		IntBuffer qixels = this.pixels.slice();
		qixels.position(x+y*this.scan+this.offset);
		for (int Y=y+height,o=offset,p=x+y*this.scan+this.offset,r=this.scan;y<Y;y++,o+=scan, qixels.position(p+=r))
			qixels.put(pixels, o, width);
		
		return this;
	}
	
	public Texture2D set(int x, int y, int width, int height, Grabber pixels) {
		return this;
	}
	
	public Texture2D set(int x, int y, int width, int height, Sampler pixels) {
		return this;
	}
	
	
	public Texture2D generateMipMap() {
		pending = new Update(pending) {
			@Override public void execute() {
				glGenerateMipmap(target);
			}
		};
		
		return this;
	}
	
	
	public Texture2D texParameter(final int pname, final int param) {
		pending = new Update(pending) {
			@Override public void execute() {
				glTexParameterf(target, pname, param);
			}
		};
		return this;
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
	
	
	class Update {
		final Update previous;
		
		public Update() { 
			previous = null; 
		}
		
		public Update(Update previous) { 
			this.previous = previous; 
		} 

		public final void update() {
			if (previous!=null)
				previous.update();
			
			execute();
		}
		
		public void execute() {
			
		}
		
	}
}
