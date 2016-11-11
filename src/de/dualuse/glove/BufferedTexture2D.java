package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import de.dualuse.glove.GLTexture.GLBoundTexture;
import de.dualuse.glove.RenderedTexture2D.IntBufferSampleRenderer;
import de.dualuse.glove.Texture2D.Grabber2D;
import de.dualuse.glove.Texture2D.Pixel2D;
import de.dualuse.glove.Texture2D.Sampler2D;

public class BufferedTexture2D implements Texture2D {
	
	final IntBuffer pixels;
	final int internalformat, width, height, format, type, offset, scan, pixelsize;
	
	/////
	
	private static IntBuffer bufferForSize(int width, int height) {
		return ByteBuffer.allocateDirect(width*height*4).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public BufferedTexture2D(int internalformat, int width, int height) {
		this(internalformat,width,height, GL_BGRA, GL_UNSIGNED_BYTE, bufferForSize(width, height), 0, width);
	}
	
	public BufferedTexture2D(int internalformat, int width, int height, int format, int type, IntBuffer pixels, int offset, int scan) {
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
			synchronized(BufferedTexture2D.this) {
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
					glTexImage2D(target, level, internalformat, width, height, 0, format, type, null);
			
				updateCounter = 0;
				union(0, width, 0, height);
			}

			if (isEmpty()) 
				return false;
			
			//XXX extremely hard to test: lim_t->+0/0 (announced Flow) == 0? 
			int announcement = pending.getAndSet(0);
			f.announce(announcement);
			announced.addAndGet(announcement);
			
			
			// always try to push through the remaining texture at once
			long biteSize = f.allocate( this.area()*pixelsize );
			this.chop( (biteSize/pixelsize), this);
			
			if (x0==0&&y0==0 && x1-x0==BufferedTexture2D.this.width)
			for (int target: planeTargets)
				glTexSubImage2D(target, level, 0, 0, width, y1, format, type, pixels);
			else 
			if (x0==0 && x1-x0==BufferedTexture2D.this.width)
				for (int target: planeTargets)
					glTexSubImage2D(target, level, 0, y0, width, (y1-y0), format, type, (IntBuffer)pixels.slice().position(scan*y0));
			else {
				glPixelStorei(GL_UNPACK_ROW_LENGTH, scan);
				for (int target: planeTargets)
					glTexSubImage2D(target, level, x0, y0, x1-x0, y1-y0, format, type, pixels.slice().position(x0+y0*scan));
				glPixelStorei(GL_UNPACK_ROW_LENGTH, 0); //XXX sucks!
			}
			
			if (p!=null)
				p.update(bt, level, x0, y0, x1-x0, y1-y0, isEmpty());
			
			boolean done = isEmpty();
			
			if (done)
				f.announce(-announced.getAndSet(0));
				
			return done;
		}

	}
	
	/////////////////////

	private BufferedTexture2D updateRectangle(final int xoffset, int yoffset, final int width, final int height) {
		for (UpdateTracker ut: trackers) 
			ut.union(xoffset, xoffset+width, yoffset, yoffset+height);
		
		return this;
	}
	
	@Override
	public BufferedTexture2D set(int targetX, int targetY, int targetWidth, int targetHeight, Sampler2D pixelSampler) {
		new IntBufferSampleRenderer(pixelSampler).render(0, 0, targetWidth, targetHeight, pixels, offset+targetX+targetY*scan, scan);
		return updateRectangle(targetX, targetY, targetWidth, targetHeight);
	}

	@Override
	public BufferedTexture2D set(final int xoffset, int yoffset, final int width, final int height, int[] pixels, int offset, int scan) {
		IntBuffer qixels = this.pixels.slice(); //XXX don't!
		qixels.position(xoffset+yoffset*this.scan+this.offset);
		for (int y = yoffset, Y=y+height,o=offset,p=xoffset+y*this.scan+this.offset,r=this.scan;y<Y;y++,o+=scan, qixels.position(p+=r))
			qixels.put(pixels, o, width);

		return updateRectangle(xoffset, yoffset, width, height);
	}
	
	@Override
	public BufferedTexture2D set(int tx, int ty, int targetWidth, int targetHeight, Grabber2D raster) {
		int line[] = new int[targetWidth];
		for (int y=0,o=tx+ty*scan;y<targetHeight;y++,o+=scan)
			pixels.put(raster.grab(0, y, targetWidth, 1, line, 0, targetWidth), o, targetWidth);
		
		return updateRectangle(tx, ty, targetWidth, targetHeight);
	}

	@Override
	public BufferedTexture2D set(int tx, int ty, int targetWidth, int targetHeight, Pixel2D raster) {
		for (int y=0,o=tx+ty*scan,r=scan-targetWidth;y<targetHeight;y++,o+=r)
			for (int x=0;x<targetWidth;x++,o++)
				pixels.put(o, raster.sample(x, y));
		
		return this;
	}

	@Override public Texture2D set(int[] pixels, int offset, int scan) { return set(0,0,width,height,pixels,offset,scan); }
	@Override public Texture2D set(Sampler2D raster) { return set(0,0,width,height,raster); }
	@Override public Texture2D set(Grabber2D raster) { return set(0,0,width,height,raster); }
	@Override public Texture2D set(Pixel2D raster) {  return set(0,0,width,height,raster); }
	
	
	
	
}
