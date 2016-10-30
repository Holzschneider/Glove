package de.dualuse.glove;

public class Range2D extends Range {
	
	int y0, y1;
	
	public Range2D extend(int x0, int x1, int y0, int y1) {

		if (this.max-this.min==0 || this.y1-this.y0==0) {
			this.min = x0;
			this.y0 = y0;
			
			this.max = x1;
			this.y1 = y1;
		} else {
			this.min = this.min<x0?this.min:x0;
			this.y0 = this.y0<y0?this.y0:y0;
			
			this.max = this.max>x1?this.max:x1;
			this.y1 = this.y1>y1?this.y1:y1;
		}
		
		return this;
	}

	boolean isEmpty() {
		return (this.max-this.min==0 || this.y1-this.y0==0);
	}
	
	public void reset() {
		min=max=y0=y1=0;
	}
}
