
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;


public class Experiment2 {

	public static void main(String[] args) {
		
		Display d = new Display();
		Shell s = new Shell(d);

		s.setLayout(new FillLayout());
		
		
		GLData data = new GLData();
		data.doubleBuffer = true;
		
		GLCanvas c = new GLCanvas(s,0, data);
		
		c.setCurrent();
		GL.createCapabilities();
		

		long start = System.nanoTime();
		
//		c.addPaintListener(new PaintListener() {
//		
//			
//			@Override
//			public void paintControl(PaintEvent e) {
//
//				glClearColor(  (float)Math.abs(Math.sin( (System.nanoTime()-start)/1e8 ))  , 0, 0, 1);
//				glClear(GL_COLOR_BUFFER_BIT);
//
//				c.swapBuffers();
//				c.redraw();
//			}
//		});
		
		c.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				System.out.println(e.x+" "+e.y);
			}
		});
		
		c.setRedraw(true);
		
		s.setBounds(100, 100, 800, 800);
		s.setVisible(true);
		
		
		//// EVENTS HERE
		while (!d.isDisposed()) {
			while (d.readAndDispatch());
			
			glClearColor(  (float)Math.abs(Math.sin( (System.nanoTime()-start)/1e8 ))  , 0, 0, 1);
			glClear(GL_COLOR_BUFFER_BIT);

			c.swapBuffers();
			
			
		}
//			if (!d.readAndDispatch())
//				d.sleep();
		
	}
}