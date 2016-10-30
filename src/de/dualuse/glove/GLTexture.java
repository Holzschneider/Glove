package de.dualuse.glove;


//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL14.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL21.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;


public class GLTexture {
	public static enum TextureClamp {
		REPEAT(GL_REPEAT),
		MIRROR(GL_MIRRORED_REPEAT),
		CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE);
		
		final int param;
		private TextureClamp(int param) {
			this.param = param;
		}
	}
	
	public static enum TextureFilter {
		NEAREST(GL_NEAREST),
		LINEAR(GL_LINEAR),
		LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
		LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);
		
		final int param;
		private TextureFilter(int param) {
			this.param = param;
		}
	}
	
	public class GLBoundTexture extends GLTexture {
		final int target;
		public GLBoundTexture(int target) {
			this.target = target;
		}
		
		@Override
		public GLBoundTexture bindTexture(int target) {
			return GLTexture.this.bindTexture(target);
		}
		
		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) 
		{ glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); return this; }

		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) 
		{ glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); return this; }
			
		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) 
		{ glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); return this; }
		
		// ------
		
		public GLBoundTexture texSubImage2D(int level, int internalformat, int xoffset, int yoffset, int width, int height, int border, int format, int type, ByteBuffer pixels) 
		{ glTexSubImage2D(target,level,xoffset,yoffset,width,height,format, type, pixels); return this; }

		public GLBoundTexture texSubImage2D(int level, int internalformat, int xoffset, int yoffset, int width, int height, int border, int format, int type, IntBuffer pixels) 
		{ glTexSubImage2D(target,level,xoffset,yoffset,width,height,format, type, pixels); return this; }
			
		public GLBoundTexture texSubImage2D(int level, int internalformat, int xoffset, int yoffset, int width, int height, int border, int format, int type, FloatBuffer pixels) 
		{ glTexSubImage2D(target,level,xoffset,yoffset,width,height,format, type, pixels); return this; }
		
		
//		public GLBoundTexture texImage2D(Texture2D tex) 
//		{ tex.update(this); return this; }
//
//		public GLBoundTexture texSubImage2D(Texture2D tex) 
//		{ tex.update(this); return this; }
//
//		public GLBoundTexture texSubImage2D(int xoffset, int yoffset, int width, int height, Texture2D tex) 
//		{ tex.update(xoffset, yoffset, width, height, this); return this; }


		public GLBoundTexture texParameter(int pname, float param) { glTexParameterf(target, pname, param); return this; }
		public GLBoundTexture texParameter(int pname, int param) { glTexParameteri(target, pname, param); return this; }
		public GLBoundTexture minFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MIN_FILTER, f.param); }
		public GLBoundTexture magFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MAG_FILTER, f.param); }

		public GLBoundTexture generateMipmap() {
			glGenerateMipmap(target);
			return this;
		}
	}
	
	
	public static GLTexture DEFAULT = new GLTexture(0);

	private int[] textureName = null;
	
	Texture[] sources = {};

	private GLTexture() { }

	private GLTexture(int name) {
		this.textureName = new int[] { name };
	}
	
	public GLTexture(Texture... sources) {
		this.sources = sources.clone();
	}
	
	
	
	

	public GLBoundTexture bindTexture(int target) {
		if (textureName == null)
			glGenTextures(1, textureName = new int[1], 0);

		glBindTexture(target, textureName[0]);

//		for (Texture t: sources)
//			t.update();
		
		return new GLBoundTexture(target);
	}
	
	public void dispose() {
		glDeleteTextures(1, Buffers.name(textureName[1]));
	}
	

	static public GLTexture[] genTextures(int n) {
		GLTexture[] buffers = new GLTexture[n];
		int names[] = new int[n];

		android.opengl.GLES20.glGenTextures(n, names, 0);

		for (int i = 0; i < n; i++)
			buffers[i] = new GLTexture(names[i]);

		return buffers;
	}

	
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
			
			int W = 1000, H = 1000;
			IntBuffer pixels = ByteBuffer.allocateDirect(W*H*4).order(ByteOrder.nativeOrder()).asIntBuffer();
			{
				for (int y =0;y<H;y++)
					for (int x =0;x<W;x++)
						pixels.put(x+y*W, (x&y)>0?0xFF00FF00:0xFF000000);

				pixels.rewind();
			}
			
			
			GLTexture tex = new GLTexture()
					.bindTexture(GL_TEXTURE_2D)
					.texImage2D(0, GL_RGBA, W, H, 0, GL12.GL_BGRA, GL_UNSIGNED_BYTE, pixels)
					.minFilter(TextureFilter.LINEAR)
					.magFilter(TextureFilter.LINEAR)
					;
					
			
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
			
			
//			GLTexture tex = new GLTexture();
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
				Point size = c.getSize(); 
				glViewport(0, 0, size.x, size.y);

				long now = System.nanoTime();
				glClearColor((float) Math.abs(Math.sin((now - start) / 1e9)), 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				

				tex.bindTexture(GL_TEXTURE_2D);
				
				glEnable(GL_TEXTURE_2D);
				GL11.glBegin(GL11.GL_QUADS);
				
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2d(-1, -1);
	
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2d( 1, -1);
					
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2d( 1,  1);
					
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2d(-1,  1);
				
				GL11.glEnd();
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
