
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.CDATASection;

import static org.lwjgl.opengl.GL11.*;

public class Experiment1 {

	public static void main(String[] args) {
		
		
		
		Display d = new Display();
		Shell s = new Shell(d);

		s.setLayout(new FillLayout());
		
		Canvas c = new Canvas(s,0);
		c.addPaintListener(new PaintListener() {
			long start = System.nanoTime();
			
			@Override
			public void paintControl(PaintEvent e) {
				
				System.out.println("blah");
				Rectangle r = c.getBounds();
				
				
				Transform t = new Transform(e.gc.getDevice());
				e.gc.getTransform(t);
				
				long now = System.nanoTime();
				t.rotate( (float)((now-start)/1e8) );
				e.gc.setTransform(t);
				
				e.gc.drawLine(0, 0, r.width, r.height);
				
				c.redraw();
			}
		});
		
		s.setBounds(100, 100, 800, 800);
		s.setVisible(true);
		
		while (!d.isDisposed())
			if (!d.readAndDispatch())
				d.sleep();
		
	}
}
