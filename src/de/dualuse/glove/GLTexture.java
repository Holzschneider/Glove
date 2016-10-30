package de.dualuse.glove;


//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL14.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL21.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Queue;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


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
	
	
	
	private Queue<Runnable> updates = null;
	private int[] textureName = null;
	private Texture[] sources = {};
	private int initialized = 0;
	private int target = 0;
	
	public GLTexture() { this.target = 0;  }
	
	public GLTexture(int target, Texture... sources) {
		this.target = target;
		this.sources = sources.clone();
	}
	
	public GLTexture reset() {
		initialized = 0;
		sources = new Texture[0];
		return this;
	}
	
	public GLTexture attach(Texture a) {
		final int I = sources.length;
		Texture[] attached = sources;
//		for (int i=0;i<I;i++)
//			if (attached[i]==a) {
//				attached[i] = sources[I-1];
//				attached[I-1] = a;
//				return this;
//			}
		
		attached = Arrays.copyOf(sources, I+1);
		attached[I] = a;
		sources = attached;
				
		return this;
	}
	

	public GLBoundTexture bindTexture() {
		if (textureName == null)
			glGenTextures(1, textureName = new int[1], 0);

		glBindTexture(target, textureName[0]);
		
		GLBoundTexture bt = new GLBoundTexture();
		
		Texture[] attached = sources;
		
		for (int i=0;i<initialized;i++)
			attached[i].update(bt, i);
		
		for (int i=initialized,I=attached.length;i<I;i++)
			attached[i].init(bt, i);
		
		initialized = attached.length;
		
		return bt;
	}
	
	public void dispose() {
		glDeleteTextures(1, Buffers.name(textureName[1]));
	}
	

	public GLTexture texParameter(final int pname, final float param) {
		updates.add(new Runnable() {
			public void run() {
				glTexParameterf(target, pname, param);
			};
		});
		return this;
	}

	public GLTexture texParameter(final int pname, final int param) {
		updates.add(new Runnable() {
			public void run() {
				glTexParameteri(target, pname, param);
			};
		});
		return this;
	}

	public GLTexture minFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MIN_FILTER, f.param); }
	public GLTexture magFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MAG_FILTER, f.param); }
	public GLTexture maxLevel(int i) { return texParameter(GL_TEXTURE_MAX_LEVEL, i); }
	
	
	public class GLBoundTexture extends GLTexture {

		public GLBoundTexture() {
			
		}
		
		@Override
		public GLBoundTexture bindTexture() {
//			return this;
			return GLTexture.this.bindTexture(); //this guarantees it's rebound
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
		
		


		public GLBoundTexture texParameter(int pname, float param) { glTexParameterf(target, pname, param); return this; }
		public GLBoundTexture texParameter(int pname, int param) { glTexParameteri(target, pname, param); return this; }
		public GLBoundTexture minFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MIN_FILTER, f.param); }
		public GLBoundTexture magFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MAG_FILTER, f.param); }
		public GLBoundTexture maxLevel(int i) { return texParameter(GL_TEXTURE_MAX_LEVEL, i); }
		
		public GLBoundTexture generateMipmap() {
			glGenerateMipmap(target);
			return this;
		}
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
			
			MouseAdapter ma = new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
//					System.out.println("hallo");
					for (int y =0;y<H;y++)
						for (int x =0;x<W;x++)
							pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF0000FF:0xFFFF0000));
	
//					bt.set(0, 0, W, H, pixelArray,0, W);
//					bt.set(0, 100, W, H-200, pixelArray,100*W, W);
					bt.set(100, 100, W-200, H-200, pixelArray,100*W+100, W);
				}
			};
			
			int W = 1000, H = 1000;
			IntBuffer pixels = ByteBuffer.allocateDirect(W*H*4).order(ByteOrder.nativeOrder()).asIntBuffer();
			int[] pixelArray = new int[W*H];
			
			{
				c.addMouseListener(ma);
				for (int y =0;y<H;y++)
					for (int x =0;x<W;x++)
//						pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF0000FF:0xFFFF0000));
						pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF00FF00:0xFF000000));

				pixels.rewind();
			}
			
			
			Texture2D bt = new Texture2D(GL_RGBA, W, H).set(0, 0, W, H, pixelArray, 0, W);
			
			GLTexture tex = new GLTexture(GL_TEXTURE_2D, bt)
					.bindTexture()
//					.texImage2D(0, GL_RGBA, W, H, 0, GL12.GL_BGRA, GL_UNSIGNED_BYTE, pixels)
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
				

				tex.bindTexture();
				
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
