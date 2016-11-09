package de.dualuse.glove;

public class Range {

	public int min, max;
	
	public synchronized boolean subtract(int lower, int upper) {
		
		if (lower<=min && min<upper)
			min = upper;
		
		if (lower<max && max<=upper )
			max = lower;
		
		return min == max;
	}
	
	public synchronized boolean union(int lower, int upper) {
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

	public synchronized boolean isEmpty() {
		return max-min<=0;
	}
	
	public synchronized void reset() {
		min=max=0;
	}

	@Override
	public synchronized String toString() {
		return "["+min+","+max+"]";
	}
	
	
	public static void main(String[] args) {
		
		Range r = new Range();
		r.union(10, 20);
		
		r.subtract(12, 14);
		
		System.out.println(r +" "+r.isEmpty());
		
		
	}
}
