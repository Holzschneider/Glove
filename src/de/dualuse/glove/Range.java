package de.dualuse.glove;

public class Range {

	int min, max;
	
	public boolean extend(int lower, int upper) {

		boolean change = lower<min || max< upper;
		
		if (this.max-this.min==0) {
			this.min = lower;
			this.max = upper;
		} else {
			this.min = this.min<lower?this.min:lower;
			this.max = this.max>upper?this.max:upper;
		}
		
		return change;
	}

	boolean isEmpty() {
		return max-min==0;
	}
	
	public void reset() {
		min=max=0;
	}

	@Override
	public String toString() {
		return "["+min+","+max+"]";
	}
}
