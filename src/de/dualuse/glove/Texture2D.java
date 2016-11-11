package de.dualuse.glove;

public abstract interface Texture2D extends Texture {
	public Texture2D set(int[] pixels, int offset, int scan);
	public Texture2D set(Sampler2D raster); 
	public Texture2D set(Grabber2D raster);
	public Texture2D set(Pixel2D raster);

	public Texture2D set(int tx, int ty, int targetWidth, int targetHeight, int[] pixels, int offset, int scan);
	public Texture2D set(int tx, int ty, int targetWidth, int targetHeight, Sampler2D raster); 
	public Texture2D set(int tx, int ty, int targetWidth, int targetHeight, Grabber2D raster);
	public Texture2D set(int tx, int ty, int targetWidth, int targetHeight, Pixel2D raster);
	
	public static interface Grabber2D {
		int[] grab(int x, int y, int width, int height, int pixels[], int offset, int scan);
	}
	
	public static interface Pixel2D {
		int sample(int x, int y);
	}
	
	public static interface Sampler2D {
		void sample(int x, int y, Sample w);
		
	}
	
	
}
