package de.dualuse.glove;

public class Range2D extends Range1D {
	
	int y0, y1;
	
	public Range2D extend(int x0, int x1, int y0, int y1) {

		if (this.x1-this.x0==0 || this.y1-this.y0==0) {
			this.x0 = x0;
			this.y0 = y0;
			
			this.x1 = x1;
			this.y1 = y1;
		} else {
			this.x0 = this.x0<x0?this.x0:x0;
			this.y0 = this.y0<y0?this.y0:y0;
			
			this.x1 = this.x1>x1?this.x1:x1;
			this.y1 = this.y1>y1?this.y1:y1;
		}
		
		return this;
	}

	public void reset() {
		x0=x1=y0=y1=0;
	}
}
