
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;


public class Experiment4 {

	public static void main(String[] args) {
		
		
		/////////// INIT UI
		Display d = new Display();
		Shell s = new Shell(d);

		s.setLayout(new FillLayout());
		
		
		GLData data = new GLData();
		data.doubleBuffer = true;
		
		GLCanvas c = new GLCanvas(s,0, data);
		c.setRedraw(true);
		c.setCurrent();
		GL.createCapabilities();
		

		
		//// REGISTER CUSTOM LISTENERS
//		long start = System.nanoTime();
		Point coord = new Point(0,0);
		
		c.addPaintListener(new PaintListener() {
			
			
			
			
			@Override
			public void paintControl(PaintEvent e) {

				//glClearColor(  (float)Math.abs(Math.sin( (System.nanoTime()-start)/1e8 ))  , 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT);
				
				
				Rectangle bounds = c.getBounds();
				glViewport(0, 0, bounds.width, bounds.height);
				
				
				glPushMatrix();
				
				System.out.println(" UI: "+coord.x+", "+coord.y);
				glTranslated(coord.x*0.001, coord.y*0.001, 0);
				glScaled(.2, .2, .2);
				
				
				glBegin(GL_QUADS);
					
				glVertex2d(-1,-1);
				glVertex2d(+1,-1);
				glVertex2d(+1,+1);
				glVertex2d(-1,+1);
				glEnd();
				glPopMatrix();
				
				
				c.swapBuffers();
				
				c.redraw();
			}
		});
		
		c.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				coord.x = e.x;
				coord.y = e.y;
				
				System.out.println("Mouse: "+ e.x+" "+e.y);
			}
		});
		
		
		s.setBounds(100, 100, 800, 800);
		s.setVisible(true);
		
		
		
		
		//////////////// GRACEFUL EVENT LOOP 
//		while (!d.isDisposed()) {
//			while (d.readAndDispatch());
//			d.sleep();
//		}
		
		for (;!d.isDisposed();d.sleep())
			while (d.readAndDispatch());
		
	}
}
