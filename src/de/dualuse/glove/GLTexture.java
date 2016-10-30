package de.dualuse.glove;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import android.opengl.GLES20;

public class GLTexture {
	public static double STREAM_CAPACITY_LIMIT = 10;

	public Exception transferException = null;
	public static GLTexture DEFAULT = new GLTexture(0);

	private int[] textureName = null;
	private Queue<TextureUpdate> updates = new ConcurrentLinkedQueue<TextureUpdate>();

	public GLTexture() {
	}

	private GLTexture(int name) {
		this.textureName = new int[] { name };
	}

	public void bindTexture(int target) {
		if (textureName == null)
			android.opengl.GLES20.glGenTextures(1, textureName = new int[1], 0);

		android.opengl.GLES20.glBindTexture(target, textureName[0]);

		TextureUpdate tu = updates.peek();
		if (tu != null) {
			tu.apply();
			updates.poll();
		}
	}
	
	public void dispose() {
		android.opengl.GLES20.glDeleteTextures(1, Buffers.name(textureName[1]));
	}
	

	static public GLTexture[] genTextures(int n) {
		GLTexture[] buffers = new GLTexture[n];
		int names[] = new int[n];

		android.opengl.GLES20.glGenTextures(n, names, 0);

		for (int i = 0; i < n; i++)
			buffers[i] = new GLTexture(names[i]);

		return buffers;
	}

	class TextureUpdate {
		TextureUpdate next = null;

		TextureUpdate(TextureUpdate next) {
			this.next = next;
		}

		void execute() { };

		final void apply() {
			if (next != null)
				next.apply();
			this.execute();
		}
		
		public GLTexture dispatch() {
			GLTexture.this.updates.add(this);
			return GLTexture.this;
		}
		
		
		TextureUpdate texImage2d(final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final Buffer pixels) {
			return null;
		}
	}
	
//	class TexImage2d extends TextureUpdate {
//		
//		
//	}
	
	
	public TextureUpdate update() {
		
		return null;
	}
	
	
	
	
	/*
	
	static class TextureSetting {
		final public static TextureSetting NOP = new TextureSetting(null);
		TextureSetting next = null;

		public TextureSetting(TextureSetting next) {
			this.next = next;
		}

		void set() {
		};

		void apply() {
			if (next != null)
				next.apply();
			this.set();
		}
	}

	abstract public class TextureUpdate {
		public GLTexture dispatch() {
			updates.offer(this);
			return GLTexture.this;
		}

		abstract void execute();
	}

	public class TexSubImage2D extends TextureUpdate {
		final int target, level, internalformat, x, y, width, height, format, border, type;
		TextureSetting settings = TextureSetting.NOP;
		final Buffer pixels;

		public TexSubImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border,
				int format, int type, Buffer pixels) {
			this.target = target;
			this.level = level;
			this.internalformat = internalformat;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.border = border;
			this.format = format;
			this.type = type;
			this.pixels = pixels;
		}

		public TexSubImage2D generateMipmap() {
			settings = new TextureSetting(settings) {
				void set() {
					android.opengl.GLES20.glGenerateMipmap(target);
				}
			};
			return this;
		}

		public TexSubImage2D texParameter(final int param, final int value) {
			settings = new TextureSetting(settings) {
				void set() {
					android.opengl.GLES20.glTexParameteri(target, param, value);
				}
			};
			return this;
		}

		public TexSubImage2D stream(StreamProgress p) {
			return stream(p, FlowControl.DEFAULT);
		};

		public TexSubImage2D stream(StreamProgress p, FlowControl f) {

			return this;
		}

		void execute() {
			android.opengl.GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
			settings.apply();
		}
	}

	public boolean isUpToDate() {
		return updates.isEmpty();
	}

	static public void glBindDefaultTexture(int target) {
		android.opengl.GLES20.glBindTexture(target, 0);
	}


	public interface StreamProgress {
		public void updated(int x, int y, int width, int height);
	}

	*/
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		Display di = new Display();
		Shell sh = new Shell(di);

		sh.setLayout(new FillLayout());
		
		
		GLData d = new GLData();
		d.doubleBuffer = true;
		final GLCanvas c = new GLCanvas(sh, 0, d);
		c.setRedraw(true);
		c.setCurrent();

		GL.createCapabilities();

		c.addPaintListener(new PaintListener() {
			
			{
				IntBuffer pixels = ByteBuffer.allocateDirect(1000*1000*4).order(ByteOrder.nativeOrder()).asIntBuffer();
				
				for (int y =0;y<1000;y++)
					for (int x =0;x<1000;x++)
						pixels.put(x+y*1000, (x&y)>0?0xFF00FF00:0xFF000000);
				
				
			}
			
//			GLTexture tex = new GLTexture()
//					.update()
////					.texImage2d(target, level, internalformat, width, height, border, format, type, pixels)
//					.texParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR)
//					.texParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR)
//					.dispatch();

			
//			GLTexture tex = new GLTexture();
//				tex
//				.new TexSubImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 0, 0, 1000, 1000, 0, GL12.GL_BGRA, GLES20.GL_UNSIGNED_BYTE, pixels)
//				.texParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST)
//				.texParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST)
//				.dispatch();
			
			
			GLTexture tex = new GLTexture();
//					.bindTexture(GL_TEXTURE_2D)
//					.texImage2d(...., streaming source) 
					//always deferred to next bind? 
					//or if executed on bindResult or same thread, executed directly? 
					//or always directly, but streamingsource supplies data deferred.
					//texImage2d sucks, cuz of openGL semantic implication
					//DirectStateAccess sucks even more, since its opengl 4.5
					//everything can be stored and set once on bind
					//maybe create objects "TexImage2D", "TexImage1D", that encapsulate everything, allow multi thread manipulation, and can be assigned to a GLTexture
					//texImage2D / texSubImage2D is then called internally in secrecy, when doing bind. or explicitly when doing texImage2d/... calls
//					.texParameter   //deferred to bind 
//					...
			
//			TexImage2D img = tex.new TexImage2D(target, level, internalformat, width, height)

			long start = System.nanoTime();

			public void paintControl(PaintEvent e) {

				long now = System.nanoTime();
				GL11.glClearColor((float) Math.abs(Math.sin((now - start) / 1e9)), 0, 0, 1);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
				
				
//				tex.bindTexture(GL11.GL_TEXTURE_2D);
//				tex.texImage2d(GL_TEXTURE_2D, ..., streamingImageSource); 
				//<--- does not invoke at all if streaming is done    
				//<--- implicitely executes streaming on bindTexture   
				//<--- streaming contents may change over later-on
				//<--- region dirty / notifications / etc
				//<--- even programmatic pixel source
				
				
				
				glEnable(GL_TEXTURE_2D);
				glBegin(GL_QUADS);
				
					glTexCoord2f(0, 0);
					glVertex2d(-1, -1);
	
					glTexCoord2f(1, 0);
					glVertex2d( 1, -1);
					
					glTexCoord2f(1, 1);
					glVertex2d( 1,  1);
					
					glTexCoord2f(0, 1);
					glVertex2d(-1,  1);
				
				glEnd();
				glDisable(GL_TEXTURE_2D);
				
				
				
				
				if (!c.isDisposed())
					c.swapBuffers();

				c.redraw();
			}
		});

		
		sh.setBounds(200, 100, 1600, 1200);
		sh.setVisible(true);
		
		while (!di.isDisposed())
			if (!di.readAndDispatch())
				di.sleep();
		di.dispose();


	}

}
