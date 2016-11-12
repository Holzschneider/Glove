package de.dualuse.glove;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public interface Texture {
	
	public static interface UpdateTracker {
		public boolean update(GLBoundTexture t, int[] imagePlaneTargets, int level, FlowControl c, StreamProgress p);
		public void dispose();
	}
	
	UpdateTracker trackUpdates();
	
//	public void init(int[] imagePlaneTargets, int level);
//	public void update(int[] imagePlaneTargets, int level);
	
	
	public static interface Sample {
//		void luminance(byte y);
//		void luminance(short y);
		void luminance(int y);
		void luminance(float y);
		
//		void luminance(byte y, byte a);
//		void luminance(short y, short a);
		void luminance(int y, int a);
		void luminance(float y, float a);
		
//		void rgb(byte r, byte g, byte b);
//		void rgb(short r, short g, short b);
		void rgb(int r, int g, int b);
		void rgb(float r, float g, float b);

		void rgb(int r, int g, int b, int a);
		void rgb(float r, float g, float b, float a);
		
		void rgb(int argb);
		
		void depth(int d);
		void depth(int d, int s);
	}
	
	public static abstract class AbstractSampleRenderer implements Sample {
		@Override public void depth(int y) { }
		@Override public void depth(int y, int a) { }
		
		@Override public void luminance(int y) { rgb(y,y,y,255); }
		@Override public void luminance(float y) { int Y = (int)(y*255); rgb(Y,Y,Y,255); }
		
		@Override public void luminance(int y, int a) { rgb(y,y,y,a); }
		@Override public void luminance(float y, float a) { int Y = (int)(y*255); rgb(Y,Y,Y,(int)(255*a)); }
		
		@Override public void rgb(int r, int g, int b) { rgb(r,g,b,(int)255); }
		@Override public void rgb(float r, float g, float b) { rgb((int)(r*255),(int)(g*255),(int)(b*255),255); }
		
		@Override public void rgb(int r, int g, int b, int a) 
		{ rgb( ((a&0xFF)<<24)|((r&0xFF)<<16)|((g&0xFF)<<8)|(b&0xFF) ); }

		@Override public void rgb(float r, float g, float b, float a) 
		{ rgb((int)(r*255),(int)(g*255),(int)(b*255),(int)(a*255)); }
		
		@Override abstract public void rgb(int argb);
	}
	
//	public static abstract class SampleRenderer
	
}
