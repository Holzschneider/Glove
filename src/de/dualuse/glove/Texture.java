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
		void luminance(int y);
		void luminance(int y, int a);
		
		void rgb(int r, int g, int b);
		void rgb(int r, int g, int b, int a);
		void rgb(int argb);
	}
	
	public static abstract class AbstractSampleRenderer implements Sample {
		@Override public void luminance(int y) { rgb(y,y,y,255); }
		@Override public void luminance(int y, int a) { rgb(y,y,y,a); }
		@Override public void rgb(int r, int g, int b) { rgb(r,g,b,255); }

		@Override public void rgb(int r, int g, int b, int a) 
		{ rgb( ((a&0xFF)<<24)|((r&0xFF)<<16)|((g&0xFF)<<8)|(b&0xFF) ); }

		@Override abstract public void rgb(int argb);
	}
	
}
