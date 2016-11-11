package de.dualuse.glove;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
import org.lwjgl.opengl.GL11;

import de.dualuse.glove.GLTexture.TextureFilter;

public class RenderedTexture2DTest {

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
			
//			MouseAdapter ma = new MouseAdapter() {
//				@Override
//				public void mouseDown(MouseEvent e) {
//					System.out.println("hallo");
//					for (int y =0;y<H;y++)
//						for (int x =0;x<W;x++)
//							pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF0000FF:0xFFFF0000));
//	
////					bt.set(0, 0, W, H, pixelArray,0, W);
////					bt.set(0, 100, W, H-200, pixelArray,100*W, W);
//					bt.set(100, 100, W-200, H-200, pixelArray,100*W+100, W);
//				}
//			};
			
			int W = 1000, H = 1000;
			IntBuffer pixels = ByteBuffer.allocateDirect(W*H*4).order(ByteOrder.nativeOrder()).asIntBuffer();
			int[] pixelArray = new int[W*H];
			
			{
//				c.addMouseListener(ma);
				for (int y =0;y<H;y++)
					for (int x =0;x<W;x++)
//						pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF0000FF:0xFFFF0000));
						pixels.put(x+y*W, pixelArray[x+y*W] = ((x&y)>0?0xFF00FF00:0xFF000000));

				pixels.rewind();
			}
			
			
			RenderedTexture2D bt = new RenderedTexture2D(GL_RGB, W, H);
//			BufferedTexture2D bt = new BufferedTexture2D(GL_RGB, W, H).set(0, 0, W, H, pixelArray, 0, W);
		
					
//					set(0, 0, W, H, pixelArray, 0, W);
			
			GLTexture tex = new GLTexture2D(bt)
//					.stream(new FlowControl.Chunked(W*4*220), new StreamProgress() {
//						public void update(GLBoundTexture bt, int level, int x, int y, int w, int h, boolean done) {
////							System.out.println(x+","+y+","+w+","+h+(done?" done":""));
//							if (done)
//								bt.generateMipmap();
//						}
//					} )
//					.bindTexture()
//					.texImage2D(0, GL_RGBA, W, H, 0, GL12.GL_BGRA, GL_UNSIGNED_BYTE, pixels)
					.minFilter(TextureFilter.LINEAR)
					.magFilter(TextureFilter.LINEAR)
					;
			
			{
//				bt.set( (x,y, sampler) -> sampler.rgb(255,0,0,0) );
				bt.set( (x,y) -> 0xFFFFFF00+y*x );
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

			int count = 0;
			public void paintControl(PaintEvent e) {
//				if (count<10)
//					System.out.println(count);
				
//				if (count++==2)
//					ma.mouseDown(null);
				
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

		
//		sh.setBounds(200, 100, 1600, 1200);
		sh.setBounds(-1200, 100, 800, 600);
		sh.setVisible(true);
		
		while (!di.isDisposed())
			if (!di.readAndDispatch())
				di.sleep();
		di.dispose();


	}
}
