package de.dualuse.glove;

import static android.opengl.GLES20.*;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.opengl.GL11;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public class RenderedTexture2D implements Texture2D {
	
	final int internalformat, width, height;
	
	/////
	
	public RenderedTexture2D(int internalformat, int width, int height) {
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
	
	
	private class UpdateTracker extends ConcurrentLinkedQueue<TextureUpdate> implements Texture.UpdateTracker {
		private static final long serialVersionUID = 1L;

		private int updateCounter = -1;

		public void dispose() {
			synchronized(RenderedTexture2D.this) {
				for (int i=0,I=trackers.length;i<I;i++)
					if (trackers[i]==this) {
						trackers[i] = trackers[I-1];
						trackers = Arrays.copyOf(trackers, I-1);
						return;
					}
			}
		}
		
		public boolean update(GLBoundTexture bt, int[] planeTargets, int level, FlowControl f, StreamProgress p) {
			if (updateCounter==-1) {
				for (int target: planeTargets)
					glTexImage2D(target, level, internalformat, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, null);
			
				updateCounter = 0;
			}

			if (isEmpty()) 
				return false;
			
			
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
			
//			if (p!=null)
//				p.update(bt, level, x0, y0, x1-x0, y1-y0, isEmpty());
			
//			boolean done = isEmpty();
//			
//			if (done)
//				f.announce(-announced.getAndSet(0));
//				
//			return done;
			
			return true;
		}

	}
	
	/////////////////////
	static private interface UpdateRenderer {
		public void render(int x, int y, int width, int height, IntBuffer to, int offset, int scan);
	}
	
	static private interface UpdateableTexture {
		public void update( int sx, int sy, int width, int height, UpdateRenderer renderer);
	}
	
	static private interface TextureUpdate {
		public void update( UpdateableTexture ur );
	}
	
	
	@Override
	public RenderedTexture2D set(final int x, int y, final int width, final int height, int[] pixels, int offset, int scan) {
		return set(x,y,width,height, (px,py,sample) -> sample.rgb(pixels[x+y*scan+offset]));
	}
	
	@Override
	public RenderedTexture2D set(int targetX, int targetY, int width, int height, Pixel2D pixels) {
		return set(targetX,targetY,width,height, (px,py,sample) -> sample.rgb(pixels.sample(px, py)) );
	}
	
	public RenderedTexture2D set(int sx, int sy, int sourceWidth, int sourceHeight, Sampler2D pixelSampler) {
		
		for (UpdateTracker ut: trackers) 
			ut.add( (t) -> t.update(sx, sy, sourceWidth, sourceHeight, new IntBufferSampleRenderer(pixelSampler)) );
		
		return this;
	}

	public RenderedTexture2D set(int sx, int sy, int sourceWidth, int sourceHeight, final Grabber2D pixels) {
		for (UpdateTracker ut: trackers)
			ut.add( (t) ->   t.update(sx, sy, sourceWidth, sourceHeight, (rx,ry,rw,rh,to,o,scan) -> to.put( pixels.grab(rx,ry,rw,rh,new int[scan*rh],0,scan), o, scan*rh) ) );
		
//		// provides an interface to dump all pixels to be updated into an IntBuffer
//		UpdateRenderer ur = new UpdateRenderer() {
//			public void render(int renderX, int renderY, int renderWidth, int renderHeight, IntBuffer to, int offset, int scan) {
//				int[] values = pixels.grab(renderX, renderY, renderWidth, renderHeight, new int[scan*height], 0, scan);
//				to.put(values, offset, scan*height);
//			}
//		};
//		
//		// appends to all trackers an texture update, that performs a simple rectangular update
//		for (UpdateTracker ut: trackers)
//			ut.add( new TextureUpdate() {
//				public void update(UpdateableTexture u) {
//					u.update(sx, sy, sourceWidth, sourceHeight, ur);
//				}
//			} );
		
		
		
		return this;
	}

	
	public static class IntBufferSampleRenderer extends AbstractSampleRenderer implements UpdateRenderer {
		IntBuffer b;
		int offset, scan, cursor;
		
		Sampler2D s;
		
		public IntBufferSampleRenderer(Sampler2D s) {
			this.s = s;
		}
		

		@Override
		public void rgb(int argb) {
			b.put(cursor,  argb);
		}

		@Override
		public void render(int rx, int ry, int rwidth, int rheight, IntBuffer to, int offset, int scan) {
			cursor = offset;
			for (int y=ry,Y=y+rheight,r=scan-rwidth;y<Y;y++,cursor+=r)
				for (int x=rx,X=x+rwidth;x<X;x++,cursor++)
					s.sample(x, y, this);			
		}
	}


	@Override public Texture2D set(int[] pixels, int offset, int scan) { return set(0,0,width,height,pixels,offset,scan); }
	@Override public Texture2D set(Sampler2D raster) { return set(0,0,width,height,raster); }
	@Override public Texture2D set(Grabber2D raster) { return set(0,0,width,height,raster); }
	@Override public Texture2D set(Pixel2D raster) {  return set(0,0,width,height,raster); }
	
}
