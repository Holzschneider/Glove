package de.dualuse.glove;

public class Range1D {

	int x0, x1;
	
	public Range1D extend(int x0, int x1) {

		if (this.x1-this.x0==0) {
			this.x0 = x0;
			this.x1 = x1;
		} else {
			this.x0 = this.x0<x0?this.x0:x0;
			this.x1 = this.x1>x1?this.x1:x1;
		}
		
		return this;
	}

	public void reset() {
		x0=x1=0;
	}

}
