package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_10_10_10_2;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import de.dualuse.glove.BytesRasterizer.BytesRenderer;
import de.dualuse.glove.Texture2D.Sampler2D;



abstract class SampleConverter {
	
	static interface LuminanceRenderer {
		void renderLuminance(int y);
	}
	
}


interface SampleRasterizer {
	void rasterize(int sx, int sy, int width, int height, Sampler2D s, ByteBuffer to, int lineSizeInElements);
	
	SampleRasterizer get();
}





class BytesRasterizer extends ThreadLocal<BytesRasterizer> implements SampleRasterizer, de.dualuse.glove.Texture.Sample, Cloneable {
	public interface BytesRenderer { void render(ByteBuffer buffer, byte r, byte g, byte b, byte a); }
	protected BytesRasterizer initialValue() { return new BytesRasterizer(renderer); }
	public BytesRasterizer(BytesRenderer b) { renderer = b; }
	final BytesRenderer renderer;
	
	ByteBuffer target;
	public void rgb(byte r, byte g, byte b, byte a) {  renderer.render(target, r, g, b, a); }
	final public void rgb(short r, short g, short b, short a) { rgb((byte)(r>>>8),(byte)(g>>>8),(byte)(b>>>8),(byte)(a>>>8));}
	final public void rgb(float r, float g, float b, float a) { rgb((byte)(255*r),(byte)(255*g),(byte)(255*b),(byte)(255*a));}
	
	final public void rgb(int argb) { rgb((byte)((argb>>16)&0xFF),(byte)((argb>>8)&0xFF),(byte)((argb>>0)&0xFF),(byte)((argb>>24)&0xFF)); }
	
	final public void rgb(byte r, byte g, byte b) { rgb(r,g,b,(byte)0xFF); }
	final public void rgb(short r, short g, short b) { rgb((byte)(r>>>8),(byte)(g>>>8),(byte)(b>>>8),(byte)0xFF); }
	final public void rgb(float r, float g, float b) { rgb(r,g,b,1f); }
	
	final public void luminance(byte y) { luminance(y,(byte)0xFF); }
	final public void luminance(short y) { luminance((byte)(y>>>8),(byte)0xFF); }
	final public void luminance(float y) { luminance(y, 1f); }
	
	final public void luminance(byte y, byte a) { rgb(y,y,y,a); }
	final public void luminance(short y, short a) { luminance( (byte)(y>>>8), (byte)(0xFF) ); }
	final public void luminance(float y, float a) { luminance( (byte)(y*255), (byte)(a*255) ); }
	public void depth(int d, int s) {}
	public void depth(int d) {}
	
	public void rasterize(int sx, int sy, int width, int height, Sampler2D s, ByteBuffer to, int lineSizeInElements) {
		target = to;
		
		for (int y=sy,Y=y+height,p=to.position();y<Y;y++,to.position(p+lineSizeInElements))
			for (int x=0,X=x+width;x<X;x++)
				s.sample(x, y, this);
	}
	public void luminance(int y) {
		// TODO Auto-generated method stub
		
	}
	public void luminance(int y, int a) {
		// TODO Auto-generated method stub
		
	}
	public void rgb(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}
	public void rgb(int r, int g, int b, int a) {
		// TODO Auto-generated method stub
		
	}
}


//class ShortsRasterizer extends ThreadLocal<ShortsRasterizer> implements SampleRasterizer, Sample, Cloneable {
//	public interface ShortsRenderer { void render(ShortBuffer buffer, int r, int g, int b, int a); }
//	protected ShortsRasterizer initialValue() { return new ShortsRasterizer(renderer); }
//	public ShortsRasterizer(ShortsRenderer b) { renderer = b; }
//	final ShortsRenderer renderer;
//	
//	ShortBuffer target;
//	public void rgb(int r, int g, int b, int a) {  renderer.render(target, r, g, b, a); }
//	
//	final public void rgb(int argb) { rgb((argb>>24)&0xFF,(argb>>16)&0xFF,(argb>>8)&0xFF,(argb>>0)&0xFF); }
//	final public void rgb(float r, float g, float b, float a) { rgb((int)(255*r),(int)(255*g),(int)(255*b),(int)(255*a));}
//	final public void rgb(float r, float g, float b) { rgb(r,g,b,1f); }
//	final public void rgb(int r, int g, int b) { rgb(r,g,b,255); }
//	final public void luminance(int y, int a) { rgb(y,y,y,a); }
//	final public void luminance(float y, float a) { luminance( (int)(y*255), (int)(a*255) ); }
//	final public void luminance(int y) { luminance(y,255); }
//	final public void luminance(float y) { luminance(y, 1f); } 
//	public void depth(int d, int s) {}
//	public void depth(int d) {}
//	
//	public void rasterize(int sx, int sy, int width, int height, Sampler2D s, ByteBuffer to, int lineSizeInElements) {
//		target = to.asShortBuffer();
//		
//		for (int y=sy,Y=y+height,p=to.position();y<Y;y++,to.position(p+lineSizeInElements))
//			for (int x=0,X=x+width;x<X;x++)
//				s.sample(x, y, this);
//	}
//}



class FloatsRasterizer extends ThreadLocal<FloatsRasterizer> implements SampleRasterizer, de.dualuse.glove.Texture.Sample, Cloneable {
	public interface FloatsRenderer { void render(ByteBuffer buffer, float r, float g, float b, float a); }
	protected FloatsRasterizer initialValue() { return new FloatsRasterizer(renderer); }
	public FloatsRasterizer(FloatsRenderer b) { renderer = b; }
	final FloatsRenderer renderer;
	
	ByteBuffer target;
	
	final public void rgb(int argb) { rgb((byte)((argb>>16)&0xFF),(byte)((argb>>8)&0xFF),(byte)((argb>>0)&0xFF),(byte)((argb>>24)&0xFF)); }
	final public void rgb(byte r, byte g, byte b) { rgb((r&0xFF)/255f,(g&0xFF)/255f,(b&0xFF)/255f,1f); }
	final public void rgb(short r, short g, short b) { rgb((r&0xFFFF)/65535f,(g&0xFFFF)/65535f,(b&0xFFFF)/65535f,1f); }
	final public void rgb(float r, float g, float b) { rgb(r,g,b,1f); }
	
	final public void rgb(byte r, byte g, byte b, byte a) { rgb((r&0xFF)/255f,(g&0xFF)/255f,(b&0xFF)/255f, (a&0xFF)/255f); }
	final public void rgb(short r, short g, short b, short a) { rgb((r&0xFFFF)/65535f,(g&0xFFFF)/65535f,(b&0xFFFF)/65535f,(a&0xFFFF)/65535f); }
	public void rgb(float r, float g, float b, float a) {  renderer.render(target, r, g, b, a); }
	
	final public void luminance(byte y) {}
	final public void luminance(short y) {}
	final public void luminance(float y) { luminance(y, 1f); } 

	final public void luminance(byte y, byte a) { luminance(y/255f,a/255f); }
	final public void luminance(short y, short a) {}
	final public void luminance(float y, float a) { rgb( y,y,y,a ); }
	
	public void depth(int d, int s) {}
	public void depth(int d) {}
	
	public void rasterize(int sx, int sy, int width, int height, Sampler2D s, ByteBuffer to, int lineSizeInElements) {
		target = to;
		
		for (int y=sy,Y=y+height,p=to.position();y<Y;y++,to.position(p+lineSizeInElements))
			for (int x=0,X=x+width;x<X;x++)
				s.sample(x, y, this);
	}
	public void luminance(int y) {
		// TODO Auto-generated method stub
		
	}
	public void luminance(int y, int a) {
		// TODO Auto-generated method stub
		
	}
	public void rgb(int r, int g, int b) {
		// TODO Auto-generated method stub
		
	}
	public void rgb(int r, int g, int b, int a) {
		// TODO Auto-generated method stub
		
	}
	
}




interface Format {
	
	static enum UNSIGNED_BYTE {
		RED(1, (to, r,g,b,a) -> to.put((byte)(r&0xFF)) );
		
		public final SampleRasterizer rasterizer;
		private UNSIGNED_BYTE(int bytesPerPixel, BytesRenderer br) {
			this.rasterizer = new BytesRasterizer(br); 
		}
		
	}
	
	static enum RED {
		UNSIGNED_BYTE(1, new BytesRasterizer( (to, r,g,b,a) -> to.put((byte)(r&0xFF))) ),
		FLOAT(4, new FloatsRasterizer( (to, r,g,b,a) -> to.putFloat(r) ));
//		UNSIGNED_SHORT(2),
//		UNSIGNED_INT(4),
		
		int bytesPerPixel;
		
		final public SampleRasterizer rasterizer;
		private RED(int bytesPerPixel, SampleRasterizer sr) {
			this.bytesPerPixel = bytesPerPixel;
			this.rasterizer = sr;
		}
	}

	static enum BGRA {
		
	}
	
	
}

public enum SourceFormat {
	
	ALPHA(GL_ALPHA,1),
	LUMINANCE(GL_LUMINANCE,1),
	LUMINANCE_ALPHA(GL_LUMINANCE_ALPHA,2),
	RED(GL_RED,1), 
	RG(GL_RG,2), 
	RGB(GL_RGB,3), 
	BGR(GL_BGR,3), 
	RGBA(GL_RGBA,4), 
	BGRA(GL_BGRA,4), 
//	RED_INTEGER(GL_RED_INTEGER,1), 
//	RG_INTEGER(GL_RG_INTEGER,1), 
//	RGB_INTEGER(GL_RGB_INTEGER,1), 
////	BGR_INTEGER(GL_BGRA_), 
//	RGBA_INTEGER(GL_RGBA_INTEGER,1), 
////	BGRA_INTEGER(), 
//	STENCIL_INDEX(GL_STENCIL_INDEX,1), 
	DEPTH_COMPONENT(GL_DEPTH_COMPONENT,1), 
	DEPTH_STENCIL(GL_DEPTH_STENCIL,2);
	
	public final int format;
	
	private SourceFormat(int format, int componentsPerTexel ) {
		this.format = format;
	}

}


enum SourceType {
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE,1, new SampleRenderer().luminance( (t,y) -> t.put(y) )),
	BYTE(GL_BYTE,1),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT,1),
	SHORT(GL_SHORT,1),
	UNSIGNED_INT(GL_UNSIGNED_INT,1),
	INT(GL_INT,1),
	FLOAT(GL_FLOAT,1),
	UNSIGNED_SHORT_5_6_5(GL_UNSIGNED_SHORT_5_6_5,3),
	UNSIGNED_SHORT_4_4_4_4(GL_UNSIGNED_SHORT_4_4_4_4,4),
	UNSIGNED_SHORT_5_5_5_1(GL_UNSIGNED_SHORT_5_5_5_1,4),
	UNSIGNED_INT_8_8_8_8(GL_UNSIGNED_INT_8_8_8_8,4),
	UNSIGNED_INT_8_8_8_8_REV(GL_UNSIGNED_INT_8_8_8_8_REV,4),
	UNSIGNED_INT_10_10_10_2(GL_UNSIGNED_INT_10_10_10_2,4),
	UNSIGNED_INT_2_10_10_10_REV(GL_UNSIGNED_INT_2_10_10_10_REV,4);
	
	private final int type;
	private final int componentsPerElement;
		
	private SourceType(int type, int componentsPerElement) { 
		this.type = type; 
		this.componentsPerElement = componentsPerElement;
	}
	
	private SourceType(int type, int elementsPerValue, SampleRenderer sr) {
		this.type = type;
		this.componentsPerElement = elementsPerValue;
	}	
}




class SampleRenderer extends ThreadLocal<SampleRenderer> implements de.dualuse.glove.Texture.Sample {
	protected SampleRenderer initialValue() 
	{ return new SampleRenderer(); }
	
	private SampleRenderer set(ByteBuffer buf)
	{ this.to = buf; return this; }
	
	public SampleRenderer wrap(ByteBuffer buf) 
	{ return set(buf); }
	
	ByteBuffer to = null;
	
	LUMINANCE8UI luminance8ui = null;
	LUMINANCE16UI luminance16ui = null;
	LUMINANCE32F luminance32f = null;
	public SampleRenderer luminance(LUMINANCE8UI l) { this.luminance8ui=l; return this; }
	
	static interface LUMINANCE8UI { void luminance(ByteBuffer t, byte y); } 
	static interface LUMINANCE16UI { void luminance(ByteBuffer t, short y); } 
	static interface LUMINANCE32F { void luminance(ByteBuffer t, float y); }
	
	static interface LUMINANCE_ALPHA8UI { void luminance(ByteBuffer t, byte y, byte a); }
	static interface LUMINANCE_ALPHA16UI { void luminance(ByteBuffer t, short y, short a); }
	static interface LUMINANCE_ALPHA32F { void luminance(ByteBuffer t, float y, float a); }
	
	static interface RGB8UI { void rgb(ByteBuffer t, byte r, byte g, byte b); }
	static interface RGB16UI { void rgb(ByteBuffer t, short r, short g, short b); }
	static interface RGB32F { void rgb(ByteBuffer t, float r, float g, float b); }
	
	static interface RGBA8UI { void rgb(ByteBuffer t, byte r, byte g, byte b, byte a); }
	static interface RGBA16UI { void rgb(ByteBuffer t, short r, short g, short b, short a); }
	static interface RGBA32F { void rgb(ByteBuffer t, float r, float g, float b, float a); }
	
	public void luminance(int y) { }
	public void luminance(float y) { }
	public void luminance(int y, int a) { }
	public void luminance(float y, float a) { }
	public void rgb(int r, int g, int b) { }
	public void rgb(float r, float g, float b) { }
	public void rgb(int r, int g, int b, int a) { }
	public void rgb(float r, float g, float b, float a) { }
	public void rgb(int argb) { }
	public void depth(int d) { }
	public void depth(int d, int s) { }
}






