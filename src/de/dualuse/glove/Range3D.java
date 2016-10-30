package de.dualuse.glove;

public class Range3D extends Range2D {
	int z0, z1;
	
	
	public Range3D extend(int x0, int x1, int y0, int y1, int z0, int z1) {
	
		if (this.max-this.min==0 || this.y1-this.y0==0 || this.z1-this.z0==0) {
			this.min = x0;
			this.y0 = y0;
			this.z0 = z0;
			
			this.max = x1;
			this.y1 = y1;
			this.z1 = z1;
		} else {
			this.min = this.min<x0?this.min:x0;
			this.y0 = this.y0<y0?this.y0:y0;
			this.z0 = this.z0<z0?this.z0:z0;
			
			this.max = this.max>x1?this.max:x1;
			this.y1 = this.y1>y1?this.y1:y1;
			this.z1 = this.z1>z1?this.z1:z1;
		}
		
		return this;
	}
	
	public void reset() {
		min=max=y0=y1=z0=z1=0;
	}

}
