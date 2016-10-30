package de.dualuse.glove;

public class Range3D extends Range2D {
	int z0, z1;
	
	
	public Range3D extend(int x0, int x1, int y0, int y1, int z0, int z1) {
	
		if (this.x1-this.x0==0 || this.y1-this.y0==0 || this.z1-this.z0==0) {
			this.x0 = x0;
			this.y0 = y0;
			this.z0 = z0;
			
			this.x1 = x1;
			this.y1 = y1;
			this.z1 = z1;
		} else {
			this.x0 = this.x0<x0?this.x0:x0;
			this.y0 = this.y0<y0?this.y0:y0;
			this.z0 = this.z0<z0?this.z0:z0;
			
			this.x1 = this.x1>x1?this.x1:x1;
			this.y1 = this.y1>y1?this.y1:y1;
			this.z1 = this.z1>z1?this.z1:z1;
		}
		
		return this;
	}
	
	public void reset() {
		x0=x1=y0=y1=z0=z1=0;
	}

}
